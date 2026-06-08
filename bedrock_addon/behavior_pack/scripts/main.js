// Chanequemon — Bedrock Edition Script
// Defensive publication. Public domain creature-capture RPG.
// Turn-based menu combat (avoids all Nintendo creature-capture patents).

import { world, system, ItemStack, Player, Entity, Container } from "@minecraft/server";
import { ActionFormData, ModalFormData, MessageFormData } from "@minecraft/server-ui";

// ─── Creature Database ───────────────────────────────────────────────────
const CREATURES = {
  anubis: { name: "Anubis", type: "SPIRIT", affinity: "SPIRIT", hp: 80, atk: 60, def: 70, spd: 50, captureRate: 0.06, biomes: ["desert"], time: "NIGHT" },
  kitsune: { name: "Kitsune", type: "SPIRIT", affinity: "SPIRIT", hp: 60, atk: 50, def: 50, spd: 90, captureRate: 0.08, biomes: ["forest"], time: "NIGHT" },
  fenrir: { name: "Fenrir", type: "BEAST", affinity: "BEAST", hp: 120, atk: 95, def: 50, spd: 70, captureRate: 0.04, biomes: ["taiga"], time: "NIGHT" },
  phoenix: { name: "Fenix", type: "MYTHIC", affinity: "FIRE", hp: 75, atk: 70, def: 40, spd: 95, captureRate: 0.03, biomes: ["desert"], time: "DAY" },
  // ... 50+ more entries in production
};

const ELEMENT_CHART = {
  FIRE: { strong: ["PLANT", "UNDEAD"], weak: ["WATER", "DRAGON"] },
  WATER: { strong: ["FIRE", "EARTH"], weak: ["PLANT", "SPIRIT"] },
  AIR: { strong: ["PLANT", "BEAST"], weak: ["EARTH", "DRAGON"] },
  EARTH: { strong: ["AIR", "FIRE"], weak: ["WATER", "MAGICAL"] },
  SPIRIT: { strong: ["UNDEAD", "MAGICAL"], weak: ["BEAST", "DRAGON"] },
  UNDEAD: { strong: ["SPIRIT", "BEAST"], weak: ["FIRE", "MAGICAL"] },
  DRAGON: { strong: ["FIRE", "AIR"], weak: ["UNDEAD", "SPIRIT"] },
  BEAST: { strong: ["SPIRIT", "EARTH"], weak: ["AIR", "DRAGON"] },
  MAGICAL: { strong: ["EARTH", "WATER"], weak: ["SPIRIT", "UNDEAD"] },
  PLANT: { strong: ["WATER", "EARTH"], weak: ["FIRE", "AIR"] },
};

const DEFAULT_SUPPORT = {
  FIRE: { aura: "BUFF", effect: "fire_resistance", amplifier: 0, radius: 8, target: "PLAYER" },
  WATER: { aura: "BUFF", effect: "water_breathing", amplifier: 0, radius: 8, target: "PLAYER" },
  AIR: { aura: "BUFF", effect: "speed", amplifier: 0, radius: 8, target: "PLAYER" },
  EARTH: { aura: "BUFF", effect: "resistance", amplifier: 0, radius: 8, target: "PLAYER" },
  SPIRIT: { aura: "BUFF", effect: "regeneration", amplifier: 0, radius: 10, target: "PLAYER" },
  UNDEAD: { aura: "DEBUFF", effect: "weakness", amplifier: 0, radius: 10, target: "MOBS" },
  DRAGON: { aura: "BUFF", effect: "strength", amplifier: 0, radius: 12, target: "PLAYER" },
  BEAST: { aura: "DEBUFF", effect: "slowness", amplifier: 0, radius: 8, target: "MOBS" },
  MAGICAL: { aura: "BUFF", effect: "night_vision", amplifier: 0, radius: 8, target: "PLAYER" },
  PLANT: { aura: "BUFF", effect: "regeneration", amplifier: 0, radius: 8, target: "ALLIES" },
};

const ENTITY_TYPES = {
  SPIRIT: "chanequemon:generic_creature",
  BEAST: "chanequemon:generic_creature",
  DRAGON: "chanequemon:generic_creature",
  MYTHIC: "chanequemon:generic_creature",
  UNDEAD: "chanequemon:generic_creature",
};

// ─── State ───────────────────────────────────────────────────────────────
const activeBattles = new Map();
const activeSummons = new Map();
const lecternCreatures = new Map();

// ─── Utility ─────────────────────────────────────────────────────────────
function getCreatureLevel(c) {
  const hpLevel = Math.max(1, Math.floor(c.hp / 18));
  const rateBonus = c.captureRate > 0 ? Math.round((0.2 - Math.min(c.captureRate, 0.2)) * 50) : 8;
  return Math.max(1, Math.min(30, hpLevel + rateBonus));
}

function getDifficulty(c) {
  const lv = getCreatureLevel(c);
  if (lv <= 5) return "Facil";
  if (lv <= 10) return "Media";
  if (lv <= 18) return "Dificil";
  if (lv <= 25) return "Muy Dificil";
  return "Legendaria";
}

function calcEffectiveness(atkElem, defElem) {
  const chart = ELEMENT_CHART[atkElem];
  if (!chart) return 1.0;
  if (chart.strong.includes(defElem)) return 2.0;
  if (chart.weak.includes(defElem)) return 0.5;
  return 1.0;
}

function calcDamage(attacker, move, defender) {
  const base = move.power || 40;
  const atk = attacker.atk || 50;
  const def = defender.def || 50;
  const stab = move.element === attacker.affinity ? 1.25 : 1.0;
  const eff = calcEffectiveness(move.element, defender.affinity);
  const variance = 0.85 + Math.random() * 0.3;
  return Math.max(0, Math.floor(base * (atk / def) * 0.4 * stab * eff * variance));
}

function isNight(time) {
  return time >= 13000 || time < 1000;
}

function getBiomeId(loc) {
  try {
    return loc.biome.id.toLowerCase();
  } catch(e) {
    return "plains";
  }
}

// ─── Creature Spawning ──────────────────────────────────────────────────
function findSpawnCandidate(player) {
  const loc = player.location;
  const biome = getBiomeId(loc);
  const time = world.getTime();
  const isNightTime = isNight(time);

  const candidates = Object.values(CREATURES).filter(c => {
    if (!c.biomes.some(b => biome.includes(b))) return false;
    if (c.time === "DAY" && isNightTime) return false;
    if (c.time === "NIGHT" && !isNightTime) return false;
    return true;
  });

  if (candidates.length === 0) return null;
  const totalWeight = candidates.reduce((s, c) => s + c.captureRate, 0);
  let roll = Math.random() * totalWeight;
  for (const c of candidates) {
    roll -= c.captureRate;
    if (roll <= 0) return { ...c, hp: c.hp, maxHp: c.hp };
  }
  return { ...candidates[0], hp: candidates[0].hp, maxHp: candidates[0].hp };
}

// ─── Combat System (Turn-based, Menu-driven) ────────────────────────────
function startCombat(player, creature) {
  const playerCreature = {
    ...Object.values(CREATURES)[0],
    hp: 70, maxHp: 70,
    moves: [
      { name: "Golpe", power: 40, element: "BEAST", type: "PHYSICAL", ap: 10 },
      { name: "Espiritu", power: 35, element: "SPIRIT", type: "MAGICAL", ap: 12 },
    ]
  };

  const session = {
    player,
    playerCreature,
    wildCreature: creature,
    turn: "player",
    state: "MAIN",
    message: "Un " + creature.name + " salvaje aparece!",
  };

  activeBattles.set(player.id, session);
  showCombatMain(player, session);
}

function showCombatMain(player, session) {
  const form = new ActionFormData();
  form.title("§4Chanequemon - Combate");
  form.body(session.message + "\n\n§c" + session.wildCreature.name + " §7HP: " +
    session.wildCreature.hp + "/" + session.wildCreature.maxHp + "\n" +
    "§b" + session.playerCreature.name + " §7HP: " +
    session.playerCreature.hp + "/" + session.playerCreature.maxHp);

  form.button("§cPELEAR", "textures/ui/icon_sword");
  form.button("§dCRIATURAS", "textures/ui/icon_bossbar");
  form.button("§6CAPTURAR (" + getCurseBookCount(player) + ")", "textures/ui/book_icon");
  form.button("§7HUIR", "textures/ui/icon_exit");

  form.show(player).then(resp => {
    if (resp.canceled) return;
    switch (resp.selection) {
      case 0: showFightScreen(player, session); break;
      case 1: session.message = "Cambiar criatura no implementado aun"; showCombatMain(player, session); break;
      case 2: showCaptureScreen(player, session); break;
      case 3: fleeCombat(player, session); break;
    }
  });
}

function showFightScreen(player, session) {
  const form = new ActionFormData();
  form.title("§cSelecciona un movimiento");

  for (const move of session.playerCreature.moves) {
    form.button("§f" + move.name + "\n§7Poder: " + move.power + " AP: " + move.ap, "textures/ui/icon_sword");
  }
  form.button("§cVOLVER", "textures/ui/icon_exit");

  form.show(player).then(resp => {
    if (resp.canceled) return;
    if (resp.selection >= session.playerCreature.moves.length) {
      session.state = "MAIN";
      showCombatMain(player, session);
      return;
    }
    executePlayerMove(player, session, resp.selection);
  });
}

function executePlayerMove(player, session, moveIndex) {
  const move = session.playerCreature.moves[moveIndex];
  const damage = calcDamage(session.playerCreature, move, session.wildCreature);
  session.wildCreature.hp = Math.max(0, session.wildCreature.hp - damage);

  session.message = "§e" + session.playerCreature.name + " uso " + move.name + "! §c" + damage + " de dano!";

  if (session.wildCreature.hp <= 0) {
    player.sendMessage("§aHas vencido a " + session.wildCreature.name + "!");
    activeBattles.delete(player.id);
    return;
  }

  system.runTimeout(() => executeWildTurn(player, session), 20);
}

function executeWildTurn(player, session) {
  const move = { name: "Ataque", power: 30, element: session.wildCreature.affinity };
  const damage = calcDamage(session.wildCreature, move, session.playerCreature);
  session.playerCreature.hp = Math.max(0, session.playerCreature.hp - damage);

  session.message = "§c" + session.wildCreature.name + " uso " + move.name + "! §c" + damage + " de dano!";

  if (session.playerCreature.hp <= 0) {
    player.sendMessage("§cTu criatura ha sido vencida!");
    activeBattles.delete(player.id);
    return;
  }

  session.state = "MAIN";
  showCombatMain(player, session);
}

function fleeCombat(player, session) {
  if (Math.random() < 0.5) {
    player.sendMessage("§aHas huido del combate!");
    activeBattles.delete(player.id);
  } else {
    session.message = "§eNo pudiste huir!";
    showCombatMain(player, session);
  }
}

function showCaptureScreen(player, session) {
  const wild = session.wildCreature;
  const level = getCreatureLevel(wild);
  const diff = getDifficulty(wild);
  const hpPct = Math.round((wild.hp / wild.maxHp) * 100);

  const rate = wild.captureRate || 0.3;
  const hpFactor = 1.0 - (1.0 - wild.hp / wild.maxHp) * 0.5;
  const levelResist = Math.max(0.05, 1.0 - level * 0.03);
  const threshold = Math.round(rate * hpFactor * levelResist * 100);

  const form = new ActionFormData();
  form.title("§6Capturar con Libro Maldito");
  form.body("§c" + wild.name + "\n" +
    "§aNivel: " + level + " §7(" + diff + ")\n" +
    "§aHP: " + wild.hp + "/" + wild.maxHp + " (" + hpPct + "%)\n" +
    "§eProbabilidad: " + threshold + "%\n" +
    "§5Libros: " + getCurseBookCount(player));

  if (getCurseBookCount(player) > 0) {
    form.button("§6USAR LIBRO MALDITO", "textures/ui/book_icon");
  } else {
    form.button("§8SIN LIBROS!", "textures/ui/icon_exit");
  }
  form.button("§cVOLVER", "textures/ui/icon_exit");

  form.show(player).then(resp => {
    if (resp.canceled) return;
    if (resp.selection === 0 && getCurseBookCount(player) > 0) {
      attemptCapture(player, session);
    } else {
      session.state = "MAIN";
      showCombatMain(player, session);
    }
  });
}

function attemptCapture(player, session) {
  removeCurseBook(player);
  const wild = session.wildCreature;
  const level = getCreatureLevel(wild);
  const rate = wild.captureRate || 0.3;
  const hpFactor = 1.0 - (1.0 - wild.hp / wild.maxHp) * 0.5;
  const levelResist = Math.max(0.05, 1.0 - level * 0.03);
  const threshold = rate * hpFactor * levelResist;

  const success = Math.random() < threshold;
  if (success) {
    const book = new ItemStack("chanequemon:captured_book", 1);
    book.nameTag = "§6Esencia de " + wild.name;
    book.setLore(["§aNivel " + level, "§7Tipo: " + wild.type, "§8Esencia: " + wild.affinity]);
    player.getComponent("inventory").container.addItem(book);
    player.sendMessage("§aHas capturado a " + wild.name + " (Nivel " + level + ")!");
    activeBattles.delete(player.id);
  } else {
    player.sendMessage("§c" + wild.name + " resistio! El libro se ha consumido.");
    showCombatMain(player, session);
  }
}

// ─── Curse Book Management ──────────────────────────────────────────────
function getCurseBookCount(player) {
  const inv = player.getComponent("inventory").container;
  let count = 0;
  for (let i = 0; i < inv.size; i++) {
    const item = inv.getItem(i);
    if (item?.typeId === "chanequemon:curse_book") count += item.amount;
  }
  return count;
}

function removeCurseBook(player) {
  const inv = player.getComponent("inventory").container;
  for (let i = 0; i < inv.size; i++) {
    const item = inv.getItem(i);
    if (item?.typeId === "chanequemon:curse_book" && item.amount > 0) {
      if (item.amount > 1) {
        item.amount -= 1;
        inv.setItem(i, item);
      } else {
        inv.setItem(i, undefined);
      }
      return;
    }
  }
}

function isCapturedBook(item) {
  return item?.typeId === "chanequemon:captured_book";
}

// ─── Summon System ──────────────────────────────────────────────────────
function summonCreature(player, creature) {
  // Dismiss existing summon
  if (activeSummons.has(player.id)) {
    dismissSummon(player);
  }

  const loc = player.location;
  const entity = player.dimension.spawnEntity("chanequemon:generic_creature", {
    x: loc.x + 2, y: loc.y, z: loc.z
  });
  entity.nameTag = "§b" + creature.name;
  entity.setDynamicProperty("chanequemon_owner", player.id);
  entity.setDynamicProperty("chanequemon_creature", creature.name);
  entity.triggerEvent("chanequemon:set_summoned");

  player.sendMessage("§6Has invocado a §b" + creature.name + "§6!");

  const support = creature.support || DEFAULT_SUPPORT[creature.affinity];
  const interval = system.runInterval(() => {
    if (!entity.isValid() || entity.isDead()) {
      system.clearRun(interval);
      return;
    }

    const dist = distance(entity.location, player.location);
    if (dist > 20) {
      entity.teleport({ x: loc.x + 2, y: loc.y, z: loc.z });
    }

    if (support) {
      applyAuraEffect(player, entity, support);
    }
  }, 40);

  activeSummons.set(player.id, { entity, creature, interval, support });
}

function dismissSummon(player) {
  const summon = activeSummons.get(player.id);
  if (summon) {
    if (summon.entity?.isValid()) summon.entity.remove();
    if (summon.interval) system.clearRun(summon.interval);
    activeSummons.delete(player.id);
    player.sendMessage("§7La criatura ha regresado al libro.");
  }
}

function applyAuraEffect(player, entity, support) {
  if (!support) return;
  const radius = support.radius || 8;

  if (support.aura === "BUFF") {
    if (support.target === "PLAYER" || support.target === "ALLIES") {
      player.addEffect(support.effect, 100, { amplifier: support.amplifier || 0, showParticles: false });
    }
    if (support.target === "ALLIES") {
      const allies = player.dimension.getEntities({
        location: entity.location, maxDistance: radius,
        families: ["player"]
      });
      for (const ally of allies) {
        if (ally.id !== player.id) {
          ally.addEffect(support.effect, 100, { amplifier: support.amplifier || 0, showParticles: false });
        }
      }
    }
  } else if (support.aura === "DEBUFF") {
    const mobs = player.dimension.getEntities({
      location: entity.location, maxDistance: radius,
      excludeFamilies: ["player"]
    });
    for (const mob of mobs) {
      if (mob.typeId !== "minecraft:player") {
        mob.addEffect(support.effect, 100, { amplifier: support.amplifier || 0, showParticles: false });
      }
    }
  }
}

function distance(a, b) {
  return Math.sqrt((a.x - b.x) ** 2 + (a.y - b.y) ** 2 + (a.z - b.z) ** 2);
}

// ─── Lectern System ─────────────────────────────────────────────────────
// Handled via events below

// ─── Event Handlers ─────────────────────────────────────────────────────
world.afterEvents.playerSpawn.subscribe(event => {
  const player = event.player;
  player.sendMessage("§5§lChanequemon §r§7v1.0 — Captura criaturas mitologicas!");
  player.sendMessage("§7Usa /chanequemon para ayuda.");
});

world.beforeEvents.itemUse.subscribe(event => {
  const player = event.source;
  const item = event.itemStack;

  if (isCapturedBook(item)) {
    event.cancel = true;
    const creatureName = item.nameTag?.replace("§6Esencia de ", "") || "Criatura";
    const creature = Object.values(CREATURES).find(c => c.name === creatureName);
    if (creature) {
      if (player.isSneaking) {
        dismissSummon(player);
      } else {
        summonCreature(player, creature);
      }
    }
  }
});

world.afterEvents.playerLeave.subscribe(event => {
  const playerId = event.playerId;
  activeBattles.delete(playerId);
  dismissSummonById(playerId);
  // Cleanup lectern creatures for this player
  for (const [loc, data] of lecternCreatures) {
    if (data.ownerId === playerId) {
      if (data.entity?.isValid()) data.entity.remove();
      if (data.interval) system.clearRun(data.interval);
      lecternCreatures.delete(loc);
    }
  }
});

function dismissSummonById(playerId) {
  const summon = activeSummons.get(playerId);
  if (summon) {
    if (summon.entity?.isValid()) summon.entity.remove();
    if (summon.interval) system.clearRun(summon.interval);
    activeSummons.delete(playerId);
  }
}

// Lectern interaction
world.afterEvents.playerInteractWithBlock.subscribe(event => {
  const player = event.player;
  const block = event.block;

  if (block.typeId !== "minecraft:lectern") return;

  const lectern = block;

  // Check if lectern has a captured book
  // Bedrock lectern stores book in its container
  // We need to check the lectern's inventory
  try {
    const container = lectern.getComponent("inventory")?.container;
    if (!container) return;

    const book = container.getItem(0);
    if (!book || !isCapturedBook(book)) return;

    event.cancel = true;

    const creatureName = book.nameTag?.replace("§6Esencia de ", "") || "Criatura";
    const creature = Object.values(CREATURES).find(c => c.name === creatureName);
    if (!creature) return;

    // Show book pages
    showBookPages(player, creature, book);

    // Spawn lectern wanderer if not already present
    const locKey = block.location.x + "," + block.location.y + "," + block.location.z;
    if (!lecternCreatures.has(locKey)) {
      spawnLecternWanderer(block, creature, player);
    }
  } catch(e) {
    // Silently fail
  }
});

function showBookPages(player, creature, book) {
  const form = new ActionFormData();
  form.title("§6" + creature.name);
  form.body(
    "§7Tipo: §b" + creature.type + "\n" +
    "§7Esencia: §b" + creature.affinity + "\n\n" +
    "§6Estadisticas:\n" +
    "§aHP: " + creature.hp + "\n" +
    "§cAtaque: " + creature.atk + "\n" +
    "§eDefensa: " + creature.def + "\n" +
    "§bVelocidad: " + creature.spd + "\n\n" +
    "§7Colocada en un atril, la criatura\n" +
    "§7deambula en las cercanias."
  );
  form.button("§aCerrar", "textures/ui/icon_exit");
  form.show(player).then(() => {});
}

function spawnLecternWanderer(block, creature, player) {
  const loc = { x: block.location.x + 1, y: block.location.y + 1, z: block.location.z };
  const entity = player.dimension.spawnEntity("chanequemon:generic_creature", loc);
  entity.nameTag = "§7" + creature.name;
  entity.setDynamicProperty("chanequemon_lectern", true);
  entity.triggerEvent("chanequemon:set_lectern");

  const locKey = block.location.x + "," + block.location.y + "," + block.location.z;

  const interval = system.runInterval(() => {
    if (!entity.isValid() || entity.isDead()) {
      system.clearRun(interval);
      lecternCreatures.delete(locKey);
      return;
    }

    // Check if book still in lectern
    try {
      const container = block.getComponent("inventory")?.container;
      if (!container || !container.getItem(0) || !isCapturedBook(container.getItem(0))) {
        entity.remove();
        system.clearRun(interval);
        lecternCreatures.delete(locKey);
        return;
      }
    } catch(e) {
      entity.remove();
      system.clearRun(interval);
      lecternCreatures.delete(locKey);
      return;
    }

    // Keep within 12 blocks
    const dist = distance(entity.location, { x: block.location.x, y: block.location.y, z: block.location.z });
    if (dist > 12) {
      entity.teleport({ x: block.location.x + 1, y: block.location.y + 1, z: block.location.z });
    }
  }, 60);

  lecternCreatures.set(locKey, { entity, ownerId: player.id, interval });
}

// ─── Encounter Manager (Passive Spawning via Movement) ──────────────────
const lastEncounters = new Map();

world.afterEvents.playerDimensionChange.subscribe(event => {
  lastEncounters.delete(event.player.id);
});

system.runInterval(() => {
  for (const player of world.getAllPlayers()) {
    if (activeBattles.has(player.id)) continue;
    if (player.isSleeping) continue;

    const now = Date.now();
    const last = lastEncounters.get(player.id) || 0;
    if (now - last < 5000) continue;

    const creature = findSpawnCandidate(player);
    if (!creature) continue;

    lastEncounters.set(player.id, now);

    // 3% chance per check
    if (Math.random() < 0.03) {
      player.sendMessage("§cUn " + creature.name + " salvaje aparece!");
      startCombat(player, creature);
    }
  }
}, 100); // every 5 seconds (100 ticks)
