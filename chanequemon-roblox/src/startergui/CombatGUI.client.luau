local ReplicatedStorage = game:GetService("ReplicatedStorage")
local Players = game:GetService("Players")
local UserInputService = game:GetService("UserInputService")
local Player = Players.LocalPlayer
local PlayerGui = Player:WaitForChild("PlayerGui")

local remotes = ReplicatedStorage.ChanequemonRemotes
local StartCombatEvent = remotes.StartCombat
local CombatActionEvent = remotes.CombatAction
local CombatUpdateEvent = remotes.CombatUpdate
local CaptureResultEvent = remotes.CaptureResult

local screenGui = Instance.new("ScreenGui")
screenGui.Name = "ChanequemonGUI"
screenGui.ResetOnSpawn = false
screenGui.Parent = PlayerGui

local function createButton(name, text, position, size, callback)
	local button = Instance.new("TextButton")
	button.Name = name
	button.Text = text
	button.Size = size or UDim2.new(0, 200, 0, 50)
	button.Position = position
	button.BackgroundColor3 = Color3.fromRGB(40, 40, 50)
	button.TextColor3 = Color3.fromRGB(255, 255, 255)
	button.TextScaled = true
	button.Font = Enum.Font.GothamBold
	button.BorderSizePixel = 2
	button.BorderColor3 = Color3.fromRGB(100, 100, 120)
	button.Parent = screenGui
	if callback then
		button.MouseButton1Click:Connect(callback)
	end
	return button
end

local function createLabel(name, text, position, size, color)
	local label = Instance.new("TextLabel")
	label.Name = name
	label.Text = text
	label.Size = size or UDim2.new(0, 400, 0, 30)
	label.Position = position
	label.BackgroundTransparency = 1
	label.TextColor3 = color or Color3.fromRGB(255, 255, 255)
	label.TextScaled = true
	label.Font = Enum.Font.GothamBold
	label.Parent = screenGui
	return label
end

local function clearButtons()
	for _, child in ipairs(screenGui:GetChildren()) do
		if child:IsA("TextButton") or child:IsA("TextLabel") then
			child:Destroy()
		end
	end
end

function showCombatMenu(session)
	clearButtons()

	local wild = session.wildCreature
	local playerCre = session.playerCreature

	local infoLabel = createLabel("Info", session.message or "", UDim2.new(0, 50, 0, 10), UDim2.new(0, 500, 0, 40), Color3.fromRGB(255, 255, 0))

	local wildHP = createLabel("WildHP", wild.name .. " HP: " .. wild.hp .. "/" .. wild.maxHp, UDim2.new(0, 50, 0, 50), UDim2.new(0, 300, 0, 30), Color3.fromRGB(255, 100, 100))
	local playerHP = createLabel("PlayerHP", playerCre.name .. " HP: " .. playerCre.hp .. "/" .. playerCre.maxHp .. " AP: " .. playerCre.currentAP .. "/" .. playerCre.maxAP, UDim2.new(0, 50, 0, 80), UDim2.new(0, 300, 0, 30), Color3.fromRGB(100, 200, 255))

	if session.turn == "player" then
		showMoveMenu(session)
	else
		local waitLabel = createLabel("Wait", "Turno del enemigo...", UDim2.new(0, 50, 0, 120), UDim2.new(0, 300, 0, 30), Color3.fromRGB(200, 200, 200))
	end
end

function showMoveMenu(session)
	local moves = session.playerCreature.moves
	if not moves then return end

	local yPos = 120
	for i, move in ipairs(moves) do
		local canUse = session.playerCreature.currentAP >= (move.ap or 0)
		local color = canUse and Color3.fromRGB(100, 200, 100) or Color3.fromRGB(150, 50, 50)
		local btn = createButton(
			"Move_" .. i,
			move.name .. " (P:" .. (move.power or 0) .. " AP:" .. (move.ap or 0) .. ")",
			UDim2.new(0, 50, 0, yPos),
			UDim2.new(0, 300, 0, 40),
			function()
				if canUse then
					CombatActionEvent:FireServer("MOVE", { moveIndex = i })
				end
			end
		)
		if not canUse then
			btn.TextTransparency = 0.5
		end
		yPos = yPos + 45
	end

	createButton("Flee", "HUIR", UDim2.new(0, 50, 0, yPos), UDim2.new(0, 300, 0, 40), function()
		CombatActionEvent:FireServer("FLEE")
	end)
end

function showCaptureUI(captured, name, message)
	clearButtons()
	local msg = captured and "Has capturado a " .. name .. "!" or message
	createLabel("CaptureMsg", msg, UDim2.new(0, 50, 0, 50), UDim2.new(0, 400, 0, 50), captured and Color3.fromRGB(100, 255, 100) or Color3.fromRGB(255, 100, 100))
	createButton("Close", "Cerrar", UDim2.new(0, 50, 0, 100), UDim2.new(0, 200, 0, 40), function()
		clearButtons()
	end)
end

function showStartupMessage()
	createLabel("Welcome", "Chanequemon v1.0 - Presiona C para menu", UDim2.new(0, 50, 0, 10), UDim2.new(0, 400, 0, 30), Color3.fromRGB(255, 170, 0))
end

StartCombatEvent.OnClientEvent:Connect(function(success, message)
	if success then
		clearButtons()
		createLabel("CombatStart", message, UDim2.new(0, 50, 0, 10), UDim2.new(0, 400, 0, 30), Color3.fromRGB(255, 200, 0))
	else
		createLabel("Error", message, UDim2.new(0, 50, 0, 10), UDim2.new(0, 400, 0, 30), Color3.fromRGB(255, 50, 50))
	end
end)

CombatUpdateEvent.OnClientEvent:Connect(function(session)
	if session then
		showCombatMenu(session)
	end
end)

CaptureResultEvent.OnClientEvent:Connect(function(captured, name, message)
	showCaptureUI(captured, name, message)
end)

UserInputService.InputBegan:Connect(function(input, gameProcessed)
	if gameProcessed then return end
	if input.KeyCode == Enum.KeyCode.C then
		clearButtons()
		local btn = createButton("FindCombat", "Buscar Combate", UDim2.new(0, 50, 0, 10), UDim2.new(0, 200, 0, 50), function()
			StartCombatEvent:InvokeServer({ creatureIndex = 1 })
		end)
	end
end)

showStartupMessage()
print("[Chanequemon] GUI loaded!")
