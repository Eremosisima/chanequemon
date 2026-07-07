local Creatures = {}

Creatures.CERBERUS = {
	name = "Cerbero",
	type = "BEAST",
	affinity = "UNDEAD",
	hp = 110,
	atk = 80,
	def = 90,
	spd = 40,
	captureRate = 0.05,
	spawnWeight = 3,
	biomes = {"Nether"},
	time = "ANY",
	lore = "Can de tres cabezas que guarda las puertas del Inframundo.",
	passive = { name = "Vigia del Hades", desc = "Reduce ataque enemigo al entrar en combate.", effect = "WEAKNESS", target = "ENEMY" },
	moves = {
		{ name = "Triple Mordisco", power = 55, element = "UNDEAD", moveType = "PHYSICAL", ap = 10, desc = "Ataca con sus tres cabezas.", statusEffect = nil },
		{ name = "Rugido Infernal", power = 0, element = "UNDEAD", moveType = "DEBUFF", ap = 8, desc = "Reduce defensa del objetivo.", statusEffect = "DEFENSE_DOWN" },
		{ name = "Colmillo Venenoso", power = 35, element = "UNDEAD", moveType = "PHYSICAL", ap = 12, desc = "Envenena al objetivo.", statusEffect = "POISON" },
		{ name = "Guardian Eterno", power = 0, element = "UNDEAD", moveType = "BUFF", ap = 6, desc = "Aumenta su defensa 2 turnos.", statusEffect = nil },
	},
}

Creatures.HYDRA = {
	name = "Hidra",
	type = "BEAST",
	affinity = "WATER",
	hp = 130, atk = 70, def = 60, spd = 30,
	captureRate = 0.05, spawnWeight = 3,
	biomes = {"Swamp"},
	time = "ANY",
	lore = "Serpiente acuatica de multiples cabezas que se regeneran al cortarse.",
	passive = { name = "Regeneracion", desc = "Recupera 5 HP por turno.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Cabezas Multiples", power = 50, element = "WATER", moveType = "PHYSICAL", ap = 10, desc = "Golpea con varias cabezas.", statusEffect = nil },
		{ name = "Aliento Acido", power = 45, element = "WATER", moveType = "MAGICAL", ap = 8, desc = "Escupe acido corrosivo.", statusEffect = "POISON" },
		{ name = "Hidrochorro", power = 60, element = "WATER", moveType = "MAGICAL", ap = 6, desc = "Dispara agua a presion.", statusEffect = nil },
		{ name = "Crecimiento", power = 0, element = "WATER", moveType = "BUFF", ap = 5, desc = "Recupera 30 HP.", statusEffect = nil },
	},
}

Creatures.MINOTAUR = {
	name = "Minotauro",
	type = "BEAST",
	affinity = "EARTH",
	hp = 100, atk = 95, def = 80, spd = 45,
	captureRate = 0.06, spawnWeight = 3,
	biomes = {"Badlands"},
	time = "ANY",
	lore = "Bestia con cuerpo de hombre y cabeza de toro, habita en laberintos subterraneos.",
	passive = { name = "Furia del Laberinto", desc = "Ataques hacen 1.3x de dano si el objetivo tiene menos HP.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Cornada", power = 60, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Embiste con sus cuernos.", statusEffect = nil },
		{ name = "Pisotón", power = 40, element = "EARTH", moveType = "PHYSICAL", ap = 12, desc = "Pisotea al objetivo.", statusEffect = "SPEED_DOWN" },
		{ name = "Muro de Piedra", power = 0, element = "EARTH", moveType = "BUFF", ap = 8, desc = "Aumenta defensa drasticamente.", statusEffect = nil },
		{ name = "Carga Brutal", power = 80, element = "EARTH", moveType = "PHYSICAL", ap = 4, desc = "Ataque devastador pero agota.", statusEffect = nil },
	},
}

Creatures.MEDUSA = {
	name = "Medusa",
	type = "SPIRIT",
	affinity = "MAGICAL",
	hp = 70, atk = 60, def = 50, spd = 80,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Badlands"},
	time = "ANY",
	lore = "Gorgona de serpientes en vez de cabello. Su mirada convierte en piedra.",
	passive = { name = "Mirada Petrea", desc = "30% de paralizar al enemigo al inicio.", effect = nil, target = "ENEMY" },
	moves = {
		{ name = "Mirada Petrificante", power = 0, element = "MAGICAL", moveType = "DEBUFF", ap = 6, desc = "Paraliza al objetivo.", statusEffect = "PARALYSIS" },
		{ name = "Cabello de Serpientes", power = 40, element = "MAGICAL", moveType = "PHYSICAL", ap = 12, desc = "Serpientes muerden al objetivo.", statusEffect = "POISON" },
		{ name = "Susurro Encantador", power = 0, element = "MAGICAL", moveType = "DEBUFF", ap = 8, desc = "Duerme al objetivo.", statusEffect = "SLEEP" },
		{ name = "Rayo de Piedra", power = 65, element = "MAGICAL", moveType = "MAGICAL", ap = 5, desc = "Rayo petrificante.", statusEffect = nil },
	},
}

Creatures.CHIMERA = {
	name = "Quimera", type = "BEAST", affinity = "FIRE",
	hp = 90, atk = 85, def = 65, spd = 60,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Grasslands"},
	time = "ANY",
	lore = "Hibrido de leon, cabra y serpiente que exhala llamas.",
	passive = { name = "Hibrido", desc = "Resiste FIRE, EARTH y BEAST.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Aliento de Fuego", power = 55, element = "FIRE", moveType = "MAGICAL", ap = 10, desc = "Exhala llamas.", statusEffect = "BURN" },
		{ name = "Garra Leonina", power = 45, element = "BEAST", moveType = "PHYSICAL", ap = 12, desc = "Arazanzo de leon.", statusEffect = nil },
		{ name = "Coletazo", power = 35, element = "BEAST", moveType = "PHYSICAL", ap = 14, desc = "Golpe con cola de serpiente.", statusEffect = "POISON" },
		{ name = "Embestida", power = 70, element = "EARTH", moveType = "PHYSICAL", ap = 5, desc = "Carga imparable.", statusEffect = nil },
	},
}

Creatures.GRIFFIN = {
	name = "Grifo", type = "BEAST", affinity = "AIR",
	hp = 85, atk = 75, def = 70, spd = 75,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Mountains"},
	time = "DAY",
	lore = "Criatura con cuerpo de leon y cabeza de aguila, guardiana de tesoros.",
	passive = { name = "Ojo de Aguila", desc = "Nunca falla sus ataques.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Picado", power = 55, element = "AIR", moveType = "PHYSICAL", ap = 10, desc = "Se lanza en picada.", statusEffect = nil },
		{ name = "Torbellino", power = 40, element = "AIR", moveType = "MAGICAL", ap = 12, desc = "Crea un torbellino.", statusEffect = nil },
		{ name = "Garra Afilada", power = 50, element = "BEAST", moveType = "PHYSICAL", ap = 10, desc = "Arazanzo con garras.", statusEffect = nil },
		{ name = "Viento Protector", power = 0, element = "AIR", moveType = "BUFF", ap = 6, desc = "Aumenta velocidad.", statusEffect = nil },
	},
}

Creatures.CYCLOPS = {
	name = "Ciclope", type = "BEAST", affinity = "EARTH",
	hp = 120, atk = 90, def = 85, spd = 20,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Mountains"},
	time = "NIGHT",
	lore = "Gigante de un solo ojo, hijo de Poseidon.",
	passive = { name = "Ojo Unico", desc = "Alta precision pero vulnerable a magia.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Mazazo", power = 70, element = "EARTH", moveType = "PHYSICAL", ap = 8, desc = "Golpe con maza.", statusEffect = nil },
		{ name = "Lanzar Roca", power = 55, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Arroja una roca.", statusEffect = nil },
		{ name = "Pisoton Sismico", power = 45, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Sacude la tierra.", statusEffect = "SPEED_DOWN" },
		{ name = "Piel de Piedra", power = 0, element = "EARTH", moveType = "BUFF", ap = 5, desc = "Aumenta defensa.", statusEffect = nil },
	},
}

Creatures.SPHINX = {
	name = "Esfinge", type = "SPIRIT", affinity = "MAGICAL",
	hp = 75, atk = 55, def = 60, spd = 85,
	captureRate = 0.05, spawnWeight = 2,
	biomes = {"Desert"},
	time = "ANY",
	lore = "Criatura con cabeza humana y cuerpo de leon, guardiana de acertijos.",
	passive = { name = "Acertijo Mortal", desc = "Ataques magicos ignoran 30% de defensa.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Acertijo", power = 0, element = "MAGICAL", moveType = "DEBUFF", ap = 8, desc = "Confunde al objetivo.", statusEffect = "CONFUSION" },
		{ name = "Rayo Mistico", power = 55, element = "MAGICAL", moveType = "MAGICAL", ap = 10, desc = "Energia arcana.", statusEffect = nil },
		{ name = "Carga Alada", power = 45, element = "BEAST", moveType = "PHYSICAL", ap = 12, desc = "Garras de leon.", statusEffect = nil },
		{ name = "Sabiduria Antigua", power = 0, element = "MAGICAL", moveType = "BUFF", ap = 6, desc = "Aumenta ataque magico.", statusEffect = nil },
	},
}

Creatures.PEGASUS = {
	name = "Pegaso", type = "BEAST", affinity = "AIR",
	hp = 65, atk = 50, def = 55, spd = 110,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Grasslands"},
	time = "DAY",
	lore = "Caballo alado nacido de la sangre de Medusa.",
	passive = { name = "Vuelo Supremo", desc = "Siempre ataca primero.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Ataque Aereo", power = 40, element = "AIR", moveType = "PHYSICAL", ap = 12, desc = "Golpea desde el cielo.", statusEffect = nil },
		{ name = "Ventisca", power = 50, element = "AIR", moveType = "MAGICAL", ap = 10, desc = "Rafagas de viento helado.", statusEffect = nil },
		{ name = "Cascada Luminosa", power = 35, element = "AIR", moveType = "MAGICAL", ap = 14, desc = "Luz cegadora.", statusEffect = "SPEED_DOWN" },
		{ name = "Vuelo Rapido", power = 0, element = "AIR", moveType = "BUFF", ap = 6, desc = "Aumenta velocidad.", statusEffect = nil },
	},
}

-- NORSE
Creatures.FENRIR = {
	name = "Fenrir", type = "BEAST", affinity = "BEAST",
	hp = 120, atk = 100, def = 55, spd = 70,
	captureRate = 0.04, spawnWeight = 2,
	biomes = {"Snowy", "Tundra"},
	time = "NIGHT",
	lore = "Lobo gigante hijo de Loki, destinado a devorar a Odin.",
	passive = { name = "Hambre de Ragnarok", desc = "1.2x dano a criaturas con poca vida.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Mordisco Voraz", power = 65, element = "BEAST", moveType = "PHYSICAL", ap = 8, desc = "Mordisco que devora vitalidad.", statusEffect = nil },
		{ name = "Aullido Lunar", power = 0, element = "BEAST", moveType = "BUFF", ap = 6, desc = "Aumenta ataque y velocidad.", statusEffect = nil },
		{ name = "Garra Salvaje", power = 45, element = "BEAST", moveType = "PHYSICAL", ap = 12, desc = "Arazanzo de lobo.", statusEffect = nil },
		{ name = "Persecucion", power = 50, element = "BEAST", moveType = "PHYSICAL", ap = 10, desc = "Ignora cambios de velocidad.", statusEffect = nil },
	},
}

Creatures.JORMUNGANDR = {
	name = "Jormundgander", type = "DRAGON", affinity = "WATER",
	hp = 140, atk = 85, def = 65, spd = 30,
	captureRate = 0.03, spawnWeight = 1,
	biomes = {"Ocean"},
	time = "ANY",
	lore = "Serpiente mundial que rodea Midgard, hijo de Loki.",
	passive = { name = "Espiral del Mundo", desc = "Resiste WATER y FIRE.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Constriccion", power = 60, element = "WATER", moveType = "PHYSICAL", ap = 8, desc = "Aprieta al objetivo.", statusEffect = nil },
		{ name = "Maremoto", power = 70, element = "WATER", moveType = "MAGICAL", ap = 6, desc = "Ola gigantesca.", statusEffect = nil },
		{ name = "Veneno de Midgard", power = 40, element = "WATER", moveType = "MAGICAL", ap = 10, desc = "Veneno corrosivo.", statusEffect = "POISON" },
		{ name = "Tormenta Marina", power = 55, element = "WATER", moveType = "MAGICAL", ap = 8, desc = "Tormenta de agua.", statusEffect = nil },
	},
}

Creatures.SLEIPNIR = {
	name = "Sleipnir", type = "BEAST", affinity = "SPIRIT",
	hp = 80, atk = 60, def = 60, spd = 100,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Grasslands"},
	time = "NIGHT",
	lore = "Caballo de ocho patas de Odin, capaz de cabalgar entre los mundos.",
	passive = { name = "Corcel de los Mundos", desc = "Puede escapar siempre del combate.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Carga Espectral", power = 50, element = "SPIRIT", moveType = "PHYSICAL", ap = 10, desc = "Carga fantasmal.", statusEffect = nil },
		{ name = "Patada Galactica", power = 45, element = "SPIRIT", moveType = "PHYSICAL", ap = 12, desc = "Patada de ocho patas.", statusEffect = "SPEED_DOWN" },
		{ name = "Salto Dimensional", power = 0, element = "SPIRIT", moveType = "BUFF", ap = 6, desc = "Aumenta velocidad.", statusEffect = nil },
		{ name = "Brillo Estelar", power = 35, element = "SPIRIT", moveType = "MAGICAL", ap = 14, desc = "Polvo de estrellas.", statusEffect = "CONFUSION" },
	},
}

Creatures.TROLL = {
	name = "Troll", type = "BEAST", affinity = "EARTH",
	hp = 100, atk = 70, def = 90, spd = 25,
	captureRate = 0.12, spawnWeight = 4,
	biomes = {"Snowy", "Tundra", "Mountains"},
	time = "NIGHT",
	lore = "Gigante de piedra que habita en montañas y puentes.",
	passive = { name = "Petreo", desc = "Resiste EARTH. Debil a FIRE.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Golpe Petreo", power = 50, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Punetazo de roca.", statusEffect = nil },
		{ name = "Defensa Rocosa", power = 0, element = "EARTH", moveType = "BUFF", ap = 6, desc = "Aumenta defensa.", statusEffect = nil },
		{ name = "Aplastar", power = 65, element = "EARTH", moveType = "PHYSICAL", ap = 8, desc = "Aplasta al objetivo.", statusEffect = nil },
		{ name = "Terremoto", power = 45, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Sacude el suelo.", statusEffect = "SPEED_DOWN" },
	},
}

-- EGYPTIAN
Creatures.ANUBIS = {
	name = "Anubis", type = "SPIRIT", affinity = "SPIRIT",
	hp = 80, atk = 65, def = 70, spd = 55,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Desert"},
	time = "NIGHT",
	lore = "Dios chacal guardian de los muertos, pesa las almas.",
	passive = { name = "Pesaje de Almas", desc = "Cura 10% del dano que inflige.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Baculo de Anubis", power = 50, element = "SPIRIT", moveType = "MAGICAL", ap = 10, desc = "Golpe con su baculo.", statusEffect = nil },
		{ name = "Maldicion del Mas Alla", power = 0, element = "SPIRIT", moveType = "DEBUFF", ap = 8, desc = "Reduce defensa.", statusEffect = "DEFENSE_DOWN" },
		{ name = "Juicio", power = 60, element = "SPIRIT", moveType = "MAGICAL", ap = 8, desc = "Juicio divino.", statusEffect = nil },
		{ name = "Alma Guardiana", power = 0, element = "SPIRIT", moveType = "BUFF", ap = 6, desc = "Escudo espiritual.", statusEffect = nil },
	},
}

Creatures.SEKHMET = {
	name = "Sekhmet", type = "BEAST", affinity = "FIRE",
	hp = 95, atk = 90, def = 60, spd = 65,
	captureRate = 0.05, spawnWeight = 2,
	biomes = {"Desert"},
	time = "DAY",
	lore = "Diosa leona de la guerra y el sol.",
	passive = { name = "Colera Solar", desc = "Ataques FIRE hacen 1.3x de dano.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Garra Solar", power = 60, element = "FIRE", moveType = "PHYSICAL", ap = 8, desc = "Garras ardientes.", statusEffect = "BURN" },
		{ name = "Rugido de Guerra", power = 0, element = "FIRE", moveType = "BUFF", ap = 6, desc = "Aumenta ataque.", statusEffect = nil },
		{ name = "Llama Devastadora", power = 70, element = "FIRE", moveType = "MAGICAL", ap = 6, desc = "Llamas purificadoras.", statusEffect = nil },
		{ name = "Mordisco del Leon", power = 45, element = "BEAST", moveType = "PHYSICAL", ap = 10, desc = "Mordisco felino.", statusEffect = nil },
	},
}

Creatures.BASTET = {
	name = "Bastet", type = "BEAST", affinity = "MAGICAL",
	hp = 65, atk = 50, def = 50, spd = 95,
	captureRate = 0.08, spawnWeight = 3,
	biomes = {"Desert", "Jungle"},
	time = "NIGHT",
	lore = "Diosa gata del hogar, la fertilidad y la proteccion.",
	passive = { name = "Agilidad Felina", desc = "30% de esquivar ataques fisicos.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Garra Sutil", power = 35, element = "BEAST", moveType = "PHYSICAL", ap = 14, desc = "Arazanzo rapido.", statusEffect = nil },
		{ name = "Rayo Lunar", power = 50, element = "MAGICAL", moveType = "MAGICAL", ap = 10, desc = "Energia lunar.", statusEffect = nil },
		{ name = "Danza Felina", power = 0, element = "MAGICAL", moveType = "BUFF", ap = 6, desc = "Aumenta velocidad.", statusEffect = nil },
		{ name = "Maullido Hipnotico", power = 0, element = "MAGICAL", moveType = "DEBUFF", ap = 8, desc = "Duerme al objetivo.", statusEffect = "SLEEP" },
	},
}

-- JAPANESE
Creatures.KITSUNE = {
	name = "Kitsune", type = "SPIRIT", affinity = "FIRE",
	hp = 60, atk = 55, def = 45, spd = 95,
	captureRate = 0.08, spawnWeight = 3,
	biomes = {"Forest"},
	time = "NIGHT",
	lore = "Zorro espiritual de nueve colas, maestro del engano.",
	passive = { name = "Colas Eternas", desc = "Captura mas facil si tienes menos criaturas.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Fuego de Zorro", power = 50, element = "FIRE", moveType = "MAGICAL", ap = 10, desc = "Llamas fantasmales.", statusEffect = "BURN" },
		{ name = "Ilusion", power = 0, element = "SPIRIT", moveType = "DEBUFF", ap = 8, desc = "Confunde al objetivo.", statusEffect = "CONFUSION" },
		{ name = "Cola de Fuego", power = 40, element = "FIRE", moveType = "MAGICAL", ap = 12, desc = "Colas ardientes.", statusEffect = nil },
		{ name = "Cambiaformas", power = 0, element = "SPIRIT", moveType = "BUFF", ap = 6, desc = "Aumenta velocidad.", statusEffect = nil },
	},
}

Creatures.TENGU = {
	name = "Tengu", type = "SPIRIT", affinity = "AIR",
	hp = 70, atk = 70, def = 50, spd = 85,
	captureRate = 0.08, spawnWeight = 3,
	biomes = {"Mountains"},
	time = "DAY",
	lore = "Criatura alada con rostro de cuervo, maestro marcial.",
	passive = { name = "Maestro Marcial", desc = "Ataques fisicos hacen 1.15x de dano.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Golpe de Viento", power = 45, element = "AIR", moveType = "MAGICAL", ap = 12, desc = "Torbellino cortante.", statusEffect = nil },
		{ name = "Patada Ciclon", power = 55, element = "AIR", moveType = "PHYSICAL", ap = 10, desc = "Patada giratoria.", statusEffect = nil },
		{ name = "Pluma Venenosa", power = 30, element = "AIR", moveType = "PHYSICAL", ap = 14, desc = "Plumas envenenadas.", statusEffect = "POISON" },
		{ name = "Meditacion", power = 0, element = "SPIRIT", moveType = "BUFF", ap = 6, desc = "Aumenta ataque y defensa.", statusEffect = nil },
	},
}

Creatures.KAPPA = {
	name = "Kappa", type = "SPIRIT", affinity = "WATER",
	hp = 60, atk = 45, def = 65, spd = 60,
	captureRate = 0.15, spawnWeight = 4,
	biomes = {"Swamp"},
	time = "ANY",
	lore = "Duende acuatico con caparazon de tortuga.",
	passive = { name = "Agua Vital", desc = "Cura 10 HP al usar movimientos WATER.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Bofetada de Agua", power = 35, element = "WATER", moveType = "PHYSICAL", ap = 14, desc = "Golpe con agua.", statusEffect = nil },
		{ name = "Casco Vacio", power = 0, element = "WATER", moveType = "DEBUFF", ap = 8, desc = "Paraliza.", statusEffect = "PARALYSIS" },
		{ name = "Hidropulso", power = 45, element = "WATER", moveType = "MAGICAL", ap = 10, desc = "Onda de agua.", statusEffect = nil },
		{ name = "Caparazon", power = 0, element = "WATER", moveType = "BUFF", ap = 6, desc = "Aumenta defensa.", statusEffect = nil },
	},
}

Creatures.ONI = {
	name = "Oni", type = "SPIRIT", affinity = "UNDEAD",
	hp = 95, atk = 85, def = 70, spd = 50,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Nether"},
	time = "NIGHT",
	lore = "Demonio gigante de cuernos y piel roja.",
	passive = { name = "Furia Oni", desc = "Mientras menos HP, mas dano causa.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Kanabo", power = 65, element = "UNDEAD", moveType = "PHYSICAL", ap = 8, desc = "Mazaso con kanabo.", statusEffect = nil },
		{ name = "Grito Infernal", power = 0, element = "UNDEAD", moveType = "DEBUFF", ap = 6, desc = "Reduce defensa.", statusEffect = "DEFENSE_DOWN" },
		{ name = "Puno Demonico", power = 50, element = "UNDEAD", moveType = "PHYSICAL", ap = 10, desc = "Punetazo infernal.", statusEffect = nil },
		{ name = "Mascara de Guerra", power = 0, element = "UNDEAD", moveType = "BUFF", ap = 6, desc = "Aumenta ataque y defensa.", statusEffect = nil },
	},
}

-- EUROPEAN FOLKLORE
Creatures.BANSHEE = {
	name = "Banshee", type = "SPIRIT", affinity = "SPIRIT",
	hp = 55, atk = 65, def = 40, spd = 90,
	captureRate = 0.09, spawnWeight = 3,
	biomes = {"Forest", "Swamp"},
	time = "NIGHT",
	lore = "Espiritu femenino cuyo lamento anuncia la muerte.",
	passive = { name = "Lamento Mortal", desc = "20% de dormir al enemigo al inicio.", effect = nil, target = "ENEMY" },
	moves = {
		{ name = "Alarido Mortal", power = 60, element = "SPIRIT", moveType = "MAGICAL", ap = 8, desc = "Grito que dana el alma.", statusEffect = nil },
		{ name = "Lamento", power = 0, element = "SPIRIT", moveType = "DEBUFF", ap = 8, desc = "Duerme al objetivo.", statusEffect = "SLEEP" },
		{ name = "Niebla Espectral", power = 35, element = "SPIRIT", moveType = "MAGICAL", ap = 12, desc = "Niebla que drena.", statusEffect = nil },
		{ name = "Susurro de Muerte", power = 0, element = "SPIRIT", moveType = "DEBUFF", ap = 6, desc = "Reduce ataque.", statusEffect = "ATTACK_DOWN" },
	},
}

Creatures.LEPRECHAUN = {
	name = "Leprechaun", type = "SPIRIT", affinity = "MAGICAL",
	hp = 50, atk = 40, def = 40, spd = 80,
	captureRate = 0.18, spawnWeight = 5,
	biomes = {"Forest", "Grasslands"},
	time = "ANY",
	lore = "Duende zapatero guardian de ollas de oro.",
	passive = { name = "Oro Escondido", desc = "Dropea objetos valiosos al ser derrotado.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Golpe de Zapato", power = 30, element = "MAGICAL", moveType = "PHYSICAL", ap = 16, desc = "Golpe de zapato.", statusEffect = nil },
		{ name = "Arcoiris", power = 40, element = "MAGICAL", moveType = "MAGICAL", ap = 12, desc = "Destello de colores.", statusEffect = nil },
		{ name = "Polvo de Hada", power = 0, element = "MAGICAL", moveType = "BUFF", ap = 8, desc = "Aumenta velocidad.", statusEffect = nil },
		{ name = "Truco", power = 35, element = "MAGICAL", moveType = "MAGICAL", ap = 14, desc = "Truco impredecible.", statusEffect = "CONFUSION" },
	},
}

Creatures.WEREWOLF = {
	name = "Hombre Lobo", type = "BEAST", affinity = "BEAST",
	hp = 90, atk = 85, def = 60, spd = 75,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Forest"},
	time = "NIGHT",
	lore = "Humano maldito que se transforma en bestia bajo la luna llena.",
	passive = { name = "Luna Llena", desc = "De noche, ataque y velocidad aumentan 1.2x.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Garra Lunar", power = 55, element = "BEAST", moveType = "PHYSICAL", ap = 10, desc = "Garras lunares.", statusEffect = nil },
		{ name = "Aullido", power = 0, element = "BEAST", moveType = "BUFF", ap = 6, desc = "Aumenta ataque.", statusEffect = nil },
		{ name = "Mordisco Salvaje", power = 50, element = "BEAST", moveType = "PHYSICAL", ap = 10, desc = "Mordisco que infecta.", statusEffect = "POISON" },
		{ name = "Persecucion Nocturna", power = 45, element = "BEAST", moveType = "PHYSICAL", ap = 12, desc = "Velocidad nocturna.", statusEffect = nil },
	},
}

Creatures.VAMPIRE = {
	name = "Vampiro", type = "UNDEAD", affinity = "UNDEAD",
	hp = 85, atk = 75, def = 50, spd = 70,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Forest", "Swamp"},
	time = "NIGHT",
	lore = "No-muerto que se alimenta de sangre.",
	passive = { name = "Chupasangre", desc = "Recupera 30% del dano que inflige.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Mordisco de Vampiro", power = 50, element = "UNDEAD", moveType = "PHYSICAL", ap = 10, desc = "Muerde y recupera HP.", statusEffect = nil },
		{ name = "Niebla Sombría", power = 0, element = "UNDEAD", moveType = "BUFF", ap = 6, desc = "Aumenta evasión.", statusEffect = nil },
		{ name = "Drenar Vida", power = 60, element = "UNDEAD", moveType = "MAGICAL", ap = 8, desc = "Drena energia vital.", statusEffect = nil },
		{ name = "Hipnosis", power = 0, element = "SPIRIT", moveType = "DEBUFF", ap = 8, desc = "Duerme al objetivo.", statusEffect = "SLEEP" },
	},
}

Creatures.DRAGON = {
	name = "Dragon Europeo", type = "DRAGON", affinity = "DRAGON",
	hp = 130, atk = 100, def = 85, spd = 40,
	captureRate = 0.03, spawnWeight = 1,
	biomes = {"Mountains"},
	time = "ANY",
	lore = "Dragon alado guardian de tesoros, exhala fuego.",
	passive = { name = "Escamas de Dragon", desc = "Resiste FIRE, DRAGON y MAGICAL.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Aliento de Dragon", power = 75, element = "DRAGON", moveType = "MAGICAL", ap = 6, desc = "Llama dragoniana.", statusEffect = "BURN" },
		{ name = "Garra de Dragon", power = 60, element = "DRAGON", moveType = "PHYSICAL", ap = 8, desc = "Garras de dragon.", statusEffect = nil },
		{ name = "Vuelo Ardiente", power = 55, element = "DRAGON", moveType = "PHYSICAL", ap = 8, desc = "Ataque aereo en llamas.", statusEffect = nil },
		{ name = "Rugido Imponente", power = 0, element = "DRAGON", moveType = "DEBUFF", ap = 6, desc = "Reduce ataque y defensa.", statusEffect = "ATTACK_DOWN" },
	},
}

Creatures.BASILISK = {
	name = "Basilisco", type = "BEAST", affinity = "EARTH",
	hp = 80, atk = 70, def = 75, spd = 35,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Desert"},
	time = "DAY",
	lore = "Rey de las serpientes, su mirada es letal.",
	passive = { name = "Mirada Letal", desc = "10% de paralizar al atacar.", effect = nil, target = "ENEMY" },
	moves = {
		{ name = "Mordisco Letal", power = 55, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Mordisco venenoso.", statusEffect = "POISON" },
		{ name = "Aliento Toxico", power = 45, element = "EARTH", moveType = "MAGICAL", ap = 10, desc = "Nube venenosa.", statusEffect = "POISON" },
		{ name = "Mirada de Basilisco", power = 0, element = "EARTH", moveType = "DEBUFF", ap = 6, desc = "Paraliza.", statusEffect = "PARALYSIS" },
		{ name = "Cola Escorpion", power = 40, element = "EARTH", moveType = "PHYSICAL", ap = 12, desc = "Golpe de cola.", statusEffect = nil },
	},
}

Creatures.KRAKEN = {
	name = "Kraken", type = "BEAST", affinity = "WATER",
	hp = 140, atk = 90, def = 70, spd = 25,
	captureRate = 0.04, spawnWeight = 1,
	biomes = {"Ocean"},
	time = "ANY",
	lore = "Monstruo marino colosal, capaz de hundir barcos.",
	passive = { name = "Torbellino", desc = "Dana a todos al entrar.", effect = nil, target = "ENEMY" },
	moves = {
		{ name = "Tentaculo", power = 60, element = "WATER", moveType = "PHYSICAL", ap = 8, desc = "Golpe de tentaculo.", statusEffect = nil },
		{ name = "Hundir", power = 0, element = "WATER", moveType = "DEBUFF", ap = 6, desc = "Reduce velocidad.", statusEffect = "SPEED_DOWN" },
		{ name = "Maremoto", power = 70, element = "WATER", moveType = "MAGICAL", ap = 6, desc = "Ola gigante.", statusEffect = nil },
		{ name = "Constriccion Marina", power = 50, element = "WATER", moveType = "PHYSICAL", ap = 8, desc = "Aprieta con tentaculos.", statusEffect = "PARALYSIS" },
	},
}

Creatures.ROC = {
	name = "Roc", type = "BEAST", affinity = "AIR",
	hp = 100, atk = 85, def = 55, spd = 70,
	captureRate = 0.05, spawnWeight = 2,
	biomes = {"Mountains"},
	time = "DAY",
	lore = "Ave colosal de Las Mil y Una Noches.",
	passive = { name = "Sombra Alada", desc = "50% de que el primer ataque falle.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Picado Mortal", power = 65, element = "AIR", moveType = "PHYSICAL", ap = 8, desc = "Picada mortal.", statusEffect = nil },
		{ name = "Vendaval", power = 50, element = "AIR", moveType = "MAGICAL", ap = 10, desc = "Vientos huracanados.", statusEffect = nil },
		{ name = "Garra de Aguila", power = 45, element = "BEAST", moveType = "PHYSICAL", ap = 12, desc = "Garras que aprisionan.", statusEffect = nil },
		{ name = "Vuelo Ascendente", power = 0, element = "AIR", moveType = "BUFF", ap = 6, desc = "Aumenta velocidad.", statusEffect = nil },
	},
}

Creatures.UNICORN = {
	name = "Unicornio", type = "BEAST", affinity = "MAGICAL",
	hp = 70, atk = 55, def = 55, spd = 85,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Forest", "Grasslands"},
	time = "DAY",
	lore = "Caballo blanco con cuerno espiral, simbolo de pureza.",
	passive = { name = "Pureza", desc = "Inmune a veneno y paralisis.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Cornada Sagrada", power = 50, element = "MAGICAL", moveType = "PHYSICAL", ap = 10, desc = "Embestida con cuerno.", statusEffect = nil },
		{ name = "Luz Sanadora", power = 0, element = "MAGICAL", moveType = "BUFF", ap = 6, desc = "Recupera 35 HP.", statusEffect = nil },
		{ name = "Destello Radiante", power = 45, element = "MAGICAL", moveType = "MAGICAL", ap = 10, desc = "Luz purificadora.", statusEffect = nil },
		{ name = "Galope Magico", power = 40, element = "MAGICAL", moveType = "PHYSICAL", ap = 12, desc = "Galope potenciado.", statusEffect = nil },
	},
}

-- LATIN AMERICAN
Creatures.CHUPACABRA = {
	name = "Chupacabras", type = "BEAST", affinity = "UNDEAD",
	hp = 65, atk = 70, def = 45, spd = 80,
	captureRate = 0.10, spawnWeight = 3,
	biomes = {"Desert"},
	time = "NIGHT",
	lore = "Criatura criptica que ataca al ganado, descubierta en Puerto Rico.",
	passive = { name = "Sigilo", desc = "50% de que el enemigo no detecte su ataque.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Colmillo Drenador", power = 45, element = "UNDEAD", moveType = "PHYSICAL", ap = 12, desc = "Drena sangre.", statusEffect = nil },
		{ name = "Salto Sorpresa", power = 50, element = "BEAST", moveType = "PHYSICAL", ap = 10, desc = "Salta desde sombras.", statusEffect = nil },
		{ name = "Garra Rapaz", power = 40, element = "BEAST", moveType = "PHYSICAL", ap = 14, desc = "Golpe rapido.", statusEffect = nil },
		{ name = "Chillido", power = 0, element = "UNDEAD", moveType = "DEBUFF", ap = 8, desc = "Reduce ataque.", statusEffect = "ATTACK_DOWN" },
	},
}

Creatures.CUCA = {
	name = "Cuca", type = "BEAST", affinity = "EARTH",
	hp = 75, atk = 60, def = 70, spd = 55,
	captureRate = 0.11, spawnWeight = 3,
	biomes = {"Jungle", "Swamp"},
	time = "NIGHT",
	lore = "Criatura del folclore brasileno, una calabaza monstruosa.",
	passive = { name = "Caparazon de Calabaza", desc = "Resiste EARTH. Debil a FIRE.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Mordisco de Caiman", power = 50, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Mandibulas poderosas.", statusEffect = nil },
		{ name = "Rodar", power = 45, element = "EARTH", moveType = "PHYSICAL", ap = 12, desc = "Arremetida giratoria.", statusEffect = nil },
		{ name = "Lodo", power = 35, element = "EARTH", moveType = "MAGICAL", ap = 14, desc = "Lanza lodo.", statusEffect = "SPEED_DOWN" },
		{ name = "Excavar", power = 0, element = "EARTH", moveType = "BUFF", ap = 8, desc = "Aumenta defensa.", statusEffect = nil },
	},
}

-- SLAVIC
Creatures.BABA_YAGA = {
	name = "Baba Yaga", type = "SPIRIT", affinity = "MAGICAL",
	hp = 75, atk = 70, def = 55, spd = 70,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Forest", "Swamp"},
	time = "NIGHT",
	lore = "Bruja eslava que habita en una cabana con patas de gallina.",
	passive = { name = "Pocion Bruja", desc = "25% de envenenar al enemigo al inicio.", effect = nil, target = "ENEMY" },
	moves = {
		{ name = "Escoba Voladora", power = 45, element = "AIR", moveType = "PHYSICAL", ap = 10, desc = "Golpe con escoba.", statusEffect = nil },
		{ name = "Pocion", power = 0, element = "MAGICAL", moveType = "STATUS", ap = 8, desc = "Envenena.", statusEffect = "POISON" },
		{ name = "Rayo Arcano", power = 60, element = "MAGICAL", moveType = "MAGICAL", ap = 8, desc = "Energia magica.", statusEffect = nil },
		{ name = "Cabana Andante", power = 0, element = "EARTH", moveType = "BUFF", ap = 6, desc = "Aumenta defensa.", statusEffect = nil },
	},
}

Creatures.LESHY = {
	name = "Leshy", type = "SPIRIT", affinity = "PLANT",
	hp = 80, atk = 60, def = 70, spd = 60,
	captureRate = 0.08, spawnWeight = 3,
	biomes = {"Forest"},
	time = "ANY",
	lore = "Espiritu del bosque eslavo, protector de arboles.",
	passive = { name = "Voz del Bosque", desc = "Cura 5 HP por turno en bosques.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Latigo de Raices", power = 45, element = "PLANT", moveType = "PHYSICAL", ap = 12, desc = "Raices que azotan.", statusEffect = nil },
		{ name = "Esporas", power = 35, element = "PLANT", moveType = "MAGICAL", ap = 14, desc = "Esporas que duermen.", statusEffect = "SLEEP" },
		{ name = "Barro Curativo", power = 0, element = "PLANT", moveType = "BUFF", ap = 6, desc = "Recupera 25 HP.", statusEffect = nil },
		{ name = "Enredadera", power = 40, element = "PLANT", moveType = "MAGICAL", ap = 12, desc = "Enreda al objetivo.", statusEffect = "PARALYSIS" },
	},
}

-- CELTIC
Creatures.DULLAHAN = {
	name = "Dullahan", type = "UNDEAD", affinity = "UNDEAD",
	hp = 75, atk = 80, def = 55, spd = 75,
	captureRate = 0.07, spawnWeight = 2,
	biomes = {"Forest", "Grasslands"},
	time = "NIGHT",
	lore = "Jinete sin cabeza del folclore irlandes.",
	passive = { name = "Farol de Muerte", desc = "Al derrotar un enemigo, recupera 30% HP.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Latigo de Cadaver", power = 50, element = "UNDEAD", moveType = "PHYSICAL", ap = 10, desc = "Latigo de hueso.", statusEffect = nil },
		{ name = "Mirada de la Cabeza", power = 0, element = "UNDEAD", moveType = "DEBUFF", ap = 8, desc = "Paraliza.", statusEffect = "PARALYSIS" },
		{ name = "Carga Fantasmal", power = 55, element = "UNDEAD", moveType = "PHYSICAL", ap = 10, desc = "Carga de corcel negro.", statusEffect = nil },
		{ name = "Farol Espectral", power = 40, element = "UNDEAD", moveType = "MAGICAL", ap = 12, desc = "Luz de ultratumba.", statusEffect = nil },
	},
}

Creatures.SELKIE = {
	name = "Selkie", type = "SPIRIT", affinity = "WATER",
	hp = 60, atk = 45, def = 45, spd = 75,
	captureRate = 0.12, spawnWeight = 3,
	biomes = {"Ocean"},
	time = "ANY",
	lore = "Foca magica que muda su piel para volverse humana.",
	passive = { name = "Piel de Foca", desc = "Bajo el agua, velocidad y defensa aumentan.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Canto Marino", power = 40, element = "WATER", moveType = "MAGICAL", ap = 12, desc = "Cancion hipnotica.", statusEffect = "SLEEP" },
		{ name = "Cola de Foca", power = 35, element = "WATER", moveType = "PHYSICAL", ap = 14, desc = "Golpe de cola.", statusEffect = nil },
		{ name = "Lagrima de Sal", power = 0, element = "WATER", moveType = "BUFF", ap = 8, desc = "Recupera 20 HP.", statusEffect = nil },
		{ name = "Hidrochorro", power = 45, element = "WATER", moveType = "MAGICAL", ap = 10, desc = "Chorro de agua.", statusEffect = nil },
	},
}

-- LITERATURE
Creatures.FRANKENSTEIN = {
	name = "Frankenstein", type = "UNDEAD", affinity = "EARTH",
	hp = 120, atk = 95, def = 75, spd = 35,
	captureRate = 0.04, spawnWeight = 2,
	biomes = {"Snowy", "Tundra"},
	time = "ANY",
	lore = "Criatura ensamblada por Victor Frankenstein (Mary Shelley, 1818).",
	passive = { name = "Cuerpo Ensamblado", desc = "Resiste UNDEAD y EARTH. Debil a FIRE.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Puno de Hierro", power = 65, element = "EARTH", moveType = "PHYSICAL", ap = 8, desc = "Punetazo brutal.", statusEffect = nil },
		{ name = "Rugido del Monstruo", power = 0, element = "UNDEAD", moveType = "DEBUFF", ap = 6, desc = "Reduce defensa.", statusEffect = "DEFENSE_DOWN" },
		{ name = "Cuerpo a Tierra", power = 50, element = "EARTH", moveType = "PHYSICAL", ap = 10, desc = "Aplasta.", statusEffect = nil },
		{ name = "Voluntad de Vivir", power = 0, element = "UNDEAD", moveType = "BUFF", ap = 5, desc = "Recupera 45 HP.", statusEffect = nil },
	},
}

Creatures.HEADLESS_HORSEMAN = {
	name = "Jinete Sin Cabeza", type = "UNDEAD", affinity = "SPIRIT",
	hp = 85, atk = 80, def = 60, spd = 70,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Grasslands", "Forest"},
	time = "NIGHT",
	lore = "Jinete fantasma de la Leyenda de Sleepy Hollow (Washington Irving, 1820).",
	passive = { name = "Caceria Nocturna", desc = "Ataques mas fuertes de noche.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Calabaza Ardiente", power = 55, element = "FIRE", moveType = "MAGICAL", ap = 8, desc = "Calabaza en llamas.", statusEffect = "BURN" },
		{ name = "Espada Fantasma", power = 50, element = "SPIRIT", moveType = "PHYSICAL", ap = 10, desc = "Espada espectral.", statusEffect = nil },
		{ name = "Galope Aterrador", power = 45, element = "SPIRIT", moveType = "PHYSICAL", ap = 12, desc = "Carga fantasmal.", statusEffect = nil },
		{ name = "Grito Silencioso", power = 0, element = "SPIRIT", moveType = "DEBUFF", ap = 8, desc = "Reduce ataque.", statusEffect = "ATTACK_DOWN" },
	},
}

-- ARABIAN
Creatures.DJINN = {
	name = "Djinn", type = "SPIRIT", affinity = "FIRE",
	hp = 80, atk = 75, def = 55, spd = 80,
	captureRate = 0.06, spawnWeight = 2,
	biomes = {"Desert"},
	time = "NIGHT",
	lore = "Ser de fuego sin humo del folclore arabe, concede deseos.",
	passive = { name = "Voluntad de Fuego", desc = "Con poca vida, ataques hacen 1.5x.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Llamarada", power = 60, element = "FIRE", moveType = "MAGICAL", ap = 8, desc = "Explosion de fuego.", statusEffect = "BURN" },
		{ name = "Espejismo", power = 0, element = "FIRE", moveType = "DEBUFF", ap = 8, desc = "Confunde.", statusEffect = "CONFUSION" },
		{ name = "Golpe de Humo", power = 40, element = "FIRE", moveType = "PHYSICAL", ap = 12, desc = "Golpe de humo.", statusEffect = nil },
		{ name = "Deseo", power = 0, element = "FIRE", moveType = "BUFF", ap = 6, desc = "Aumenta todos los stats.", statusEffect = nil },
	},
}

-- MISC
Creatures.PHOENIX = {
	name = "Phoenix", type = "MYTHIC", affinity = "FIRE",
	hp = 80, atk = 75, def = 40, spd = 100,
	captureRate = 0.03, spawnWeight = 1,
	biomes = {"Desert"},
	time = "DAY",
	lore = "Ave de fuego que renace de sus cenizas.",
	passive = { name = "Renacimiento", desc = "Revive una vez con 50% HP.", effect = nil, target = "SELF" },
	moves = {
		{ name = "Explosion Solar", power = 70, element = "FIRE", moveType = "MAGICAL", ap = 6, desc = "Explosion de luz solar.", statusEffect = nil },
		{ name = "Pluma Ardiente", power = 45, element = "FIRE", moveType = "PHYSICAL", ap = 10, desc = "Plumas de fuego.", statusEffect = "BURN" },
		{ name = "Vuelo Igneo", power = 55, element = "FIRE", moveType = "PHYSICAL", ap = 8, desc = "Carga en llamas.", statusEffect = nil },
		{ name = "Renacer", power = 0, element = "FIRE", moveType = "BUFF", ap = 2, desc = "Se prepara para renacer.", statusEffect = nil },
	},
}

function Creatures.getAll()
	local list = {}
	for _, c in pairs(Creatures) do
		if type(c) == "table" and c.name then
			table.insert(list, c)
		end
	end
	return list
end

function Creatures.findById(id)
	return Creatures[id]
end

function Creatures.findByName(name)
	for _, c in pairs(Creatures) do
		if type(c) == "table" and c.name == name then
			return c
		end
	end
	return nil
end

return Creatures
