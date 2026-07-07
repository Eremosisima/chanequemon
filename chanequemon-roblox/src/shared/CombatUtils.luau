local STATUS_EFFECTS = {
	POISON = { duration = 10, amplifier = 0, chance = 0.7, name = "Veneno" },
	BURN = { duration = 8, amplifier = 0, chance = 0.8, name = "Quemadura" },
	SLEEP = { duration = 6, amplifier = 0, chance = 0.6, name = "Sueno" },
	PARALYSIS = { duration = 5, amplifier = 0, chance = 0.6, name = "Paralisis" },
	CONFUSION = { duration = 8, amplifier = 0, chance = 0.6, name = "Confusion" },
	DEFENSE_DOWN = { duration = 10, amplifier = 0, chance = 0.8, name = "Defensa baja" },
	ATTACK_DOWN = { duration = 10, amplifier = 1, chance = 0.8, name = "Ataque bajo" },
	SPEED_DOWN = { duration = 10, amplifier = 0, chance = 0.8, name = "Velocidad baja" },
}

function createCombatSession(player, playerCreature, wildCreature)
	return {
		player = player,
		playerCreature = {
			name = playerCreature.name,
			type = playerCreature.type,
			affinity = playerCreature.affinity,
			hp = playerCreature.hp,
			maxHp = playerCreature.maxHp or playerCreature.hp,
			atk = playerCreature.atk,
			def = playerCreature.def,
			spd = playerCreature.spd,
			lore = playerCreature.lore,
			passive = playerCreature.passive,
			moves = {},
			currentAP = 0,
			maxAP = 0,
			statusEffects = {},
			revived = false,
		},
		wildCreature = {
			name = wildCreature.name,
			type = wildCreature.type,
			affinity = wildCreature.affinity,
			hp = wildCreature.hp,
			maxHp = wildCreature.maxHp or wildCreature.hp,
			atk = wildCreature.atk,
			def = wildCreature.def,
			spd = wildCreature.spd,
			lore = wildCreature.lore,
			passive = wildCreature.passive,
			moves = {},
			statusEffects = {},
		},
		turn = "player",
		message = "Combate iniciado!",
	}
end

function deepCopyMoves(moves)
	local copy = {}
	for _, m in ipairs(moves or {}) do
		table.insert(copy, {
			name = m.name,
			power = m.power,
			element = m.element,
			moveType = m.moveType,
			ap = m.ap,
			desc = m.desc,
			statusEffect = m.statusEffect,
		})
	end
	return copy
end

function applyPassiveCombatBonus(attacker, defender, isPlayerAttacking)
	local passive = attacker.passive
	if not passive then return 1.0 end

	if attacker.name == "Minotauro" and defender.hp < defender.maxHp * 0.5 then
		return 1.3
	end
	if attacker.name == "Fenrir" and defender.hp < defender.maxHp * 0.3 then
		return 1.2
	end
	if attacker.name == "Sekhmet" and attacker.affinity == "FIRE" then
		return 1.3
	end
	if attacker.name == "Tengu" then
		return 1.15
	end
	return 1.0
end

function checkPhoenixRevival(session)
	local creature = session.playerCreature
	if creature.name == "Phoenix" and not creature.revived and creature.hp <= 0 then
		creature.hp = math.floor(creature.maxHp * 0.5)
		creature.revived = true
		return true
	end
	return false
end

function canFlee(session)
	if session.playerCreature.name == "Sleipnir" then return true end
	return math.random() < 0.5
end

function getRandomWildMove(wild)
	local offensiveMoves = {}
	local supportMoves = {}
	for _, m in ipairs(wild.moves or {}) do
		if (m.power or 0) > 0 then
			table.insert(offensiveMoves, m)
		elseif m.moveType == "BUFF" or m.moveType == "DEBUFF" or m.moveType == "STATUS" then
			table.insert(supportMoves, m)
		end
	end

	local move
	if wild.hp < wild.maxHp * 0.3 and #supportMoves > 0 and math.random() < 0.5 then
		move = supportMoves[math.random(#supportMoves)]
	elseif #offensiveMoves > 0 then
		move = offensiveMoves[math.random(#offensiveMoves)]
	else
		move = { name = "Ataque", power = 30, element = wild.affinity, moveType = "PHYSICAL", ap = 99 }
	end
	return move
end

return {
	STATUS_EFFECTS = STATUS_EFFECTS,
	createCombatSession = createCombatSession,
	deepCopyMoves = deepCopyMoves,
	applyPassiveCombatBonus = applyPassiveCombatBonus,
	checkPhoenixRevival = checkPhoenixRevival,
	canFlee = canFlee,
	getRandomWildMove = getRandomWildMove,
}
