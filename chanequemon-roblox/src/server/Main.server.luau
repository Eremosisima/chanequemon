local ReplicatedStorage = game:GetService("ReplicatedStorage")
local Players = game:GetService("Players")
local RunService = game:GetService("RunService")
local MapGenerator = require(script.Parent.MapGenerator)

local Creatures = require(ReplicatedStorage.Chanequemon.Creatures)
local Elements = require(ReplicatedStorage.Chanequemon.Elements)
local CombatUtils = require(ReplicatedStorage.Chanequemon.CombatUtils)

local ACTIVE_BATTLES = {}
local ACTIVE_SUMMONS = {}
local PLAYER_CREATURES = {}

local REMOTE_EVENTS = Instance.new("Folder")
REMOTE_EVENTS.Name = "ChanequemonRemotes"
REMOTE_EVENTS.Parent = ReplicatedStorage

local StartCombatEvent = Instance.new("RemoteEvent")
StartCombatEvent.Name = "StartCombat"
StartCombatEvent.Parent = REMOTE_EVENTS

local CombatActionEvent = Instance.new("RemoteEvent")
CombatActionEvent.Name = "CombatAction"
CombatActionEvent.Parent = REMOTE_EVENTS

local CombatUpdateEvent = Instance.new("RemoteEvent")
CombatUpdateEvent.Name = "CombatUpdate"
CombatUpdateEvent.Parent = REMOTE_EVENTS

local SummonEvent = Instance.new("RemoteEvent")
SummonEvent.Name = "SummonCreature"
SummonEvent.Parent = REMOTE_EVENTS

local CaptureResultEvent = Instance.new("RemoteEvent")
CaptureResultEvent.Name = "CaptureResult"
CaptureResultEvent.Parent = REMOTE_EVENTS

function findSpawnCandidate(player)
	local character = player.Character
	if not character then return nil end
	local humanoidRootPart = character:FindFirstChild("HumanoidRootPart")
	if not humanoidRootPart then return nil end

	local pos = humanoidRootPart.Position
	local timeOfDay = game:GetService("Lighting").ClockTime
	local isNight = timeOfDay >= 18 or timeOfDay < 6

	local biome = "Grasslands"
	local terrainPos = pos
	local material = workspace.Terrain:ReadVoxel(terrainPos, 1, 1, 1)

	if material == Enum.Material.Water or material == Enum.Material.Subsea then
		biome = "Ocean"
	end

	local allCreatures = Creatures.getAll()
	local candidates = {}
	for _, c in ipairs(allCreatures) do
		local biomeMatch = false
		for _, b in ipairs(c.biomes) do
			if biome:find(b) then
				biomeMatch = true
				break
			end
		end
		if not biomeMatch then continue end

		if c.time == "DAY" and isNight then continue end
		if c.time == "NIGHT" and not isNight then continue end

		table.insert(candidates, c)
	end

	if #candidates == 0 then return nil end

	local totalWeight = 0
	for _, c in ipairs(candidates) do
		totalWeight = totalWeight + (c.spawnWeight or 1)
	end

	local roll = math.random() * totalWeight
	for _, c in ipairs(candidates) do
		roll = roll - (c.spawnWeight or 1)
		if roll <= 0 then
			local clone = table.clone(c)
			clone.hp = c.hp
			clone.maxHp = c.hp
			clone.moves = CombatUtils.deepCopyMoves(c.moves)
			return clone
		end
	end

	local c = candidates[1]
	local clone = table.clone(c)
	clone.hp = c.hp
	clone.maxHp = c.hp
	clone.moves = CombatUtils.deepCopyMoves(c.moves)
	return clone
end

function startCombat(player, wildCreature)
	local creatures = PLAYER_CREATURES[player.UserId] or {}
	if #creatures == 0 then
		StartCombatEvent:FireClient(player, nil, "No tienes criaturas capturadas! Usa /capture para ayuda.")
		return
	end

	local session = CombatUtils.createCombatSession(player, creatures[1], wildCreature)
	session.playerCreature.moves = CombatUtils.deepCopyMoves(creatures[1].moves)
	session.playerCreature.currentAP = Elements.initCreatureAP(creatures[1])
	session.playerCreature.maxAP = Elements.initCreatureAP(creatures[1])
	session.message = "Un " .. wildCreature.name .. " salvaje aparece!"

	ACTIVE_BATTLES[player.UserId] = session

	StartCombatEvent:FireClient(player, true, session.message)
	CombatUpdateEvent:FireClient(player, session)
end

function handlePlayerMove(player, moveIndex)
	local session = ACTIVE_BATTLES[player.UserId]
	if not session or session.turn ~= "player" then return end

	local creature = session.playerCreature
	local move = creature.moves[moveIndex]
	if not move then return end

	if creature.currentAP < (move.ap or 0) then
		session.message = "No tienes suficiente AP!"
		CombatUpdateEvent:FireClient(player, session)
		return
	end

	creature.currentAP = math.max(0, creature.currentAP - (move.ap or 0))
	local bonus = CombatUtils.applyPassiveCombatBonus(creature, session.wildCreature, true)
	local damage = math.floor(Elements.calcDamage(creature, move, session.wildCreature) * bonus)
	session.wildCreature.hp = math.max(0, session.wildCreature.hp - damage)
	session.message = creature.name .. " uso " .. move.name .. "! " .. damage .. " de dano!"

	if session.wildCreature.hp <= 0 then
		handleCapture(player, session)
		return
	end

	session.turn = "wild"
	CombatUpdateEvent:FireClient(player, session)

	task.wait(1.5)

	executeWildTurn(player, session)
end

function executeWildTurn(player, session)
	local wild = session.wildCreature
	local move = CombatUtils.getRandomWildMove(wild)
	local bonus = CombatUtils.applyPassiveCombatBonus(wild, session.playerCreature, false)
	local damage = (move.power or 0) > 0 and math.floor(Elements.calcDamage(wild, move, session.playerCreature) * bonus) or 0
	session.playerCreature.hp = math.max(0, session.playerCreature.hp - damage)
	session.message = wild.name .. " uso " .. move.name .. "!" .. (damage > 0 and " " .. damage .. " de dano!" or "")

	if session.playerCreature.hp <= 0 then
		if CombatUtils.checkPhoenixRevival(session) then
			session.message = "El Phoenix renace de sus cenizas!"
			session.turn = "player"
			CombatUpdateEvent:FireClient(player, session)
			return
		end
		session.message = session.playerCreature.name .. " ha sido vencido!"
		ACTIVE_BATTLES[player.UserId] = nil
		CombatUpdateEvent:FireClient(player, session)
		return
	end

	session.playerCreature.currentAP = math.min(session.playerCreature.maxAP, session.playerCreature.currentAP + 5)
	session.turn = "player"
	CombatUpdateEvent:FireClient(player, session)
end

function handleCapture(player, session)
	local wild = session.wildCreature
	local captured = false

	if math.random() < wild.captureRate then
		captured = true
	end

	if captured then
		local creatureData = table.clone(wild)
		creatureData.level = Elements.getCreatureLevel(wild)
		local creatures = PLAYER_CREATURES[player.UserId] or {}
		table.insert(creatures, creatureData)
		PLAYER_CREATURES[player.UserId] = creatures

		session.message = "Has capturado a " .. wild.name .. "!"
	else
		session.message = wild.name .. " escapo!"
	end

	ACTIVE_BATTLES[player.UserId] = nil
	CaptureResultEvent:FireClient(player, captured, wild.name, session.message)
end

function handleFlee(player)
	local session = ACTIVE_BATTLES[player.UserId]
	if not session then return end

	if CombatUtils.canFlee(session) then
		session.message = "Has huido del combate!"
		ACTIVE_BATTLES[player.UserId] = nil
	else
		session.message = "No pudiste huir!"
		session.turn = "wild"
		CombatUpdateEvent:FireClient(player, session)

		task.wait(1.5)
		executeWildTurn(player, session)
	end
	CombatUpdateEvent:FireClient(player, session)
end

function handleSummon(player, creatureIndex)
	local creatures = PLAYER_CREATURES[player.UserId] or {}
	local creatureData = creatures[creatureIndex]
	if not creatureData then return end

	if ACTIVE_SUMMONS[player.UserId] then
		dismissSummon(player)
	end

	local character = player.Character
	if not character then return end
	local hrp = character:FindFirstChild("HumanoidRootPart")
	if not hrp then return end

	local pos = hrp.Position + Vector3.new(3, 0, 0)
	local model = Instance.new("Model")
	model.Name = "Chanequemon_" .. creatureData.name
	model.Parent = workspace

	local part = Instance.new("Part")
	part.Name = "Creature"
	part.Size = Vector3.new(2, 3, 1)
	part.Position = pos
	part.Anchored = false
	part.Parent = model

	local humanoid = Instance.new("Humanoid")
	humanoid.Parent = model
	humanoid.DisplayName = creatureData.name
	humanoid.MaxHealth = creatureData.hp * 10

	local billboard = Instance.new("BillboardGui")
	billboard.Name = "NameTag"
	billboard.Size = UDim2.new(0, 200, 0, 50)
	billboard.StudsOffset = Vector3.new(0, 3, 0)
	billboard.AlwaysOnTop = true
	billboard.Parent = part

	local nameLabel = Instance.new("TextLabel")
	nameLabel.Name = "CreatureName"
	nameLabel.Size = UDim2.new(1, 0, 1, 0)
	nameLabel.BackgroundTransparency = 1
	nameLabel.Text = creatureData.name
	nameLabel.TextColor3 = Color3.fromRGB(255, 170, 0)
	nameLabel.TextScaled = true
	nameLabel.Font = Enum.Font.GothamBold
	nameLabel.Parent = billboard

	ACTIVE_SUMMONS[player.UserId] = { model = model, creature = creatureData }

	task.spawn(function()
		while ACTIVE_SUMMONS[player.UserId] and model.Parent do
			task.wait(2)
			if character and hrp then
				local dist = (hrp.Position - pos).Magnitude
				if dist > 20 then
					part.Position = hrp.Position + Vector3.new(3, 0, 0)
				end
			end
		end
	end)
end

function dismissSummon(player)
	local summon = ACTIVE_SUMMONS[player.UserId]
	if summon and summon.model then
		summon.model:Destroy()
	end
	ACTIVE_SUMMONS[player.UserId] = nil
end

function giveCaptureBook(player)
	local creatures = PLAYER_CREATURES[player.UserId] or {}
	if #creatures >= 6 then
		return "Solo puedes tener hasta 6 criaturas!"
	end
	return nil
end

Players.PlayerAdded:Connect(function(player)
	PLAYER_CREATURES[player.UserId] = {}
	ACTIVE_BATTLES[player.UserId] = nil

	player:WaitForChild("PlayerGui")
end)

Players.PlayerRemoving:Connect(function(player)
	ACTIVE_BATTLES[player.UserId] = nil
	dismissSummon(player)
	PLAYER_CREATURES[player.UserId] = nil
end)

game:GetService("ReplicatedStorage").ChanequemonRemotes.StartCombat.OnServerInvoke = function(player, data)
	if data and data.creatureIndex then
		local creatures = PLAYER_CREATURES[player.UserId] or {}
		local c = creatures[data.creatureIndex]
		if c then
			local wild = findSpawnCandidate(player)
			if wild then
				startCombat(player, wild)
			end
		end
	end
end

CombatActionEvent.OnServerEvent:Connect(function(player, action, data)
	if action == "MOVE" and data then
		handlePlayerMove(player, data.moveIndex)
	elseif action == "FLEE" then
		handleFlee(player)
	elseif action == "SUMMON" and data then
		handleSummon(player, data.creatureIndex)
	elseif action == "DISMISS" then
		dismissSummon(player)
	end
end)

local encounterTimers = {}
RunService.Heartbeat:Connect(function()
	for _, player in ipairs(Players:GetPlayers()) do
		if ACTIVE_BATTLES[player.UserId] then continue end

		local last = encounterTimers[player.UserId] or 0
		if os.clock() - last < 5 then continue end

		encounterTimers[player.UserId] = os.clock()

		local creature = findSpawnCandidate(player)
		if creature and math.random() < 0.03 then
			startCombat(player, creature)
		end
	end
end)

-- Generate map on startup
MapGenerator.generate()

-- Setup spawn point
local spawnLocation = Instance.new("SpawnLocation")
spawnLocation.Name = "ChanequemonSpawn"
spawnLocation.Position = MapGenerator.getSpawnPoint()
spawnLocation.Parent = workspace

print("[Chanequemon] Server loaded!")
