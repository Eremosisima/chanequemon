local ELEMENT_CHART = {
	FIRE = { strong = {"PLANT", "UNDEAD"}, weak = {"WATER", "DRAGON"} },
	WATER = { strong = {"FIRE", "EARTH"}, weak = {"PLANT", "SPIRIT"} },
	AIR = { strong = {"PLANT", "BEAST"}, weak = {"EARTH", "DRAGON"} },
	EARTH = { strong = {"AIR", "FIRE"}, weak = {"WATER", "MAGICAL"} },
	SPIRIT = { strong = {"UNDEAD", "MAGICAL"}, weak = {"BEAST", "DRAGON"} },
	UNDEAD = { strong = {"SPIRIT", "BEAST"}, weak = {"FIRE", "MAGICAL"} },
	DRAGON = { strong = {"FIRE", "AIR"}, weak = {"UNDEAD", "SPIRIT"} },
	BEAST = { strong = {"SPIRIT", "EARTH"}, weak = {"AIR", "DRAGON"} },
	MAGICAL = { strong = {"EARTH", "WATER"}, weak = {"SPIRIT", "UNDEAD"} },
	PLANT = { strong = {"WATER", "EARTH"}, weak = {"FIRE", "AIR"} },
}

function calcEffectiveness(atkElem, defElem)
	local chart = ELEMENT_CHART[atkElem]
	if not chart then return 1.0 end
	for _, strong in ipairs(chart.strong) do
		if strong == defElem then return 2.0 end
	end
	for _, weak in ipairs(chart.weak) do
		if weak == defElem then return 0.5 end
	end
	return 1.0
end

function calcDamage(attacker, move, defender)
	local base = move.power or 40
	local atk = attacker.atk or 50
	local def = defender.def or 50
	local stab = 1.0
	if move.element == attacker.affinity then
		stab = 1.25
	end
	local eff = calcEffectiveness(move.element, defender.affinity)
	local variance = 0.85 + math.random() * 0.3
	return math.max(0, math.floor(base * (atk / def) * 0.4 * stab * eff * variance))
end

function getCreatureLevel(c)
	local hpLevel = math.max(1, math.floor(c.hp / 18))
	local rateBonus = c.captureRate and c.captureRate > 0 and math.round((0.2 - math.min(c.captureRate, 0.2)) * 50) or 8
	return math.max(1, math.min(30, hpLevel + rateBonus))
end

function getDifficulty(c)
	local lv = getCreatureLevel(c)
	if lv <= 5 then return "Facil" end
	if lv <= 10 then return "Media" end
	if lv <= 18 then return "Dificil" end
	if lv <= 25 then return "Muy Dificil" end
	return "Legendaria"
end

function initCreatureAP(creature)
	local totalAP = 0
	for _, m in ipairs(creature.moves) do
		totalAP = totalAP + (m.ap or 10)
	end
	return math.max(20, totalAP)
end

return {
	calcEffectiveness = calcEffectiveness,
	calcDamage = calcDamage,
	getCreatureLevel = getCreatureLevel,
	getDifficulty = getDifficulty,
	initCreatureAP = initCreatureAP,
	ELEMENT_CHART = ELEMENT_CHART,
}
