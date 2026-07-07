# Chanequemon — Defensive Publication

**Title:** System and Method for Menu-Based Capture of Mythological Creatures in an Open-World RPG

**Author:** [PUBLIC DOMAIN DEDICATION — ver LICENSE]

**Date:** 2026-06-07

**Status:** Public Disclosure for Prior Art Purposes

**Repository:** https://github.com/eremosisima/Chanequemon
**Series:** Cipactli: The Lesser Key (narrative pivot from Chanequemon)

---

## 1. Abstract

A turn-based role-playing game system where the player explores an open world, encounters mythological creatures from global folklore and public-domain literary works, and captures them exclusively through menu-driven actions in a turn-based combat screen. The system explicitly avoids any real-time aiming, reticle display, probability indicators, capture-mode toggling, or thrown-object mechanics. Creatures are summoned via menu selection with a portal/conjuration animation, not launched or thrown. The system is designed for implementation on Minecraft (via paper plugin or datapack) or Roblox (via Luau script), using each platform's native turn-based approximation systems.

---

## 2. Background — Problem Solved

### 2.1 The Patent Landscape

Existing patents held by Nintendo and The Pokémon Company (JP7545191, JP7493117, JP7528390, US 12,179,111, US 12,220,638, US 12,403,397) cover:

- Determining an aiming direction based on player input
- Displaying an aiming point or reticle
- Displaying a visual indicator of capture success probability
- Launching/throwing a capture item toward a field character in real-time
- Toggling between a "capture mode" and a "battle mode"
- Throwing/launching a captured creature to initiate combat
- Summoning a secondary character via aimed launch

These patents share a common technical特征: they all involve **real-time aiming and throwing** in a 3D overworld space.

### 2.2 The Prior Art Gap

The traditional turn-based menu capture system used in Pokémon Red/Blue (1996) through Pokémon Sword/Shield (2019) — where capture is selected from a battle menu without aiming, reticles, or overworld throwing — is unpatented prior art. However, no formal defensive publication exists that systematically documents this class of mechanics for mythological/public-domain creature capture, particularly for implementation on modding platforms like Minecraft or Roblox.

### 2.3 Problem Statement

Existing solutions either:
(a) Use real-time aiming/throw mechanics that fall within Nintendo's patent claims, or
(b) Fail to provide a documented, enabling disclosure of a menu-based alternative specifically designed to avoid all known patent claims in the creature-capture genre.

This disclosure solves both problems.

---

## 3. System Architecture

### 3.1 Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     Game Instance                           │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │ Overworld   │→ │ Encounter    │→ │ Turn-Based       │   │
│  │ Exploration │  │ Trigger      │  │ Combat Screen    │   │
│  └─────────────┘  └──────────────┘  └──────────────────┘   │
│                                          ↓                  │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │ Creature    │← │ Capture      │← │ Action Menu      │   │
│  │ Registry    │  │ Resolution   │  │ [Fight/Item/     │   │
│  └─────────────┘  └──────────────┘  │  Capture/Run]    │   │
│                                      └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Platform Abstraction Layer

The system defines a platform-agnostic core with adapters for:

- **Minecraft (Paper/Bukkit plugin or datapack):** Uses Minecraft's existing scoreboard system for turn tracking, custom crafting for capture items, command blocks or Java plugin logic for creature spawning.
- **Roblox (Luau + ModuleScript):** Uses Roblox's built-in turn-based framework, RemoteEvents for player-creature interaction, and ModuleScripts for creature data.

Both implementations share identical game logic; only the rendering and event-dispatch layers differ.

### 3.3 Core Data Structures

```pseudo
CLASS Creature:
    id: String                        // e.g. "vampire_transylvanian"
    displayName: String                // e.g. "Vampiro de Transilvania"
    loreOrigin: String                 // e.g. "Folclore rumano / Drácula (Stoker, 1897)"
    publicDomainSource: Bool           // true — obra en dominio público
    type: CreatureType                 // enum: UNDEAD, MYTHICAL, BEAST, SPIRIT, ELEMENTAL, etc.
    stats: CreatureStats
    moveSet: List<Move>
    captureRate: Float                 // 0.0 to 1.0 — base capture probability
    evolutionTree: List<String>        // IDs de evoluciones (opcional)
    summonAnimation: AnimationType     // enum: PORTAL, SUMMON_CIRCLE, MIST, LIGHT, SHADOW

CLASS CreatureStats:
    hp: Integer
    maxHp: Integer
    attack: Integer
    defense: Integer
    speed: Integer
    elementalAffinity: Element

CLASS Move:
    id: String
    displayName: String
    power: Integer
    moveType: MoveType                 // enum: PHYSICAL, MAGICAL, STATUS, BUFF, DEBUFF
    element: Element
    description: String
    apCost: Integer
    priority: Integer                  // for speed tie-breaking
    statusEffect: StatusEffect         // optional
```

### 3.4 Creature Roster (Partial — Public Domain Sources)

The creature roster draws exclusively from:
- World mythology and folklore (uncopyrightable)
- Literary works in the public domain (pre-1929 US, life+70 EU)

| Creature ID | Name | Origin Source | Public Domain Status |
|---|---|---|---|
| vampire_dracula | Vampiro | Drácula (Bram Stoker, 1897) | ✅ <sup>ver nota</sup> |
| vampire_strigoi | Strigoi | Folclore rumano | ✅ Folclore |
| werewolf | Hombre Lobo | Folclore europeo | ✅ Folclore |
| frankenstein | Criatura de Frankenstein | Mary Shelley (1818) | ✅ |
| chimera | Quimera | Mitología griega | ✅ Mitología |
| dragon_european | Dragón Europeo | Folclore europeo | ✅ Folclore |
| dragon_asian | Dragón Asiático | Mitología china | ✅ Mitología |
| kappa | Kappa | Folclore japonés | ✅ Folclore |
| chupacabra | Chupacabras | Folclore latinoamericano moderno | ✅ Folclore contemporáneo |
| kraken | Kraken | Mitología nórdica | ✅ Mitología |
| tengu | Tengu | Folclore japonés | ✅ Folclore |
| banshee | Banshee | Folclore irlandés | ✅ Folclore |
| wendigo | Wendigo | Folclore algonquino | ✅ Folclore |
| kitsune | Kitsune | Folclore japonés | ✅ Folclore |
| hydra | Hidra | Mitología griega | ✅ Mitología |
| cerberus | Cerbero | Mitología griega | ✅ Mitología |
| sphinx | Esfinge | Mitología egipcia/griega | ✅ Mitología |
| minotaur | Minotauro | Mitología griega | ✅ Mitología |
| phoenix | Fénix | Mitología egipcia/griega | ✅ Mitología |
| pegasus | Pegaso | Mitología griega | ✅ Mitología |
| griffin | Grifo | Mitología greco-persa | ✅ Mitología |
| leprechaun | Leprechaun | Folclore irlandés | ✅ Folclore |
| medusa | Medusa (Gorgona) | Mitología griega | ✅ Mitología |
| djinn | Djinn | Folclore árabe | ✅ Folclore |
| yeti | Yeti | Folclore himalayo | ✅ Folclore |
| roc | Roc (Ave gigante) | Mitología árabe (Las Mil y Una Noches) | ✅ |
| mermaid | Sirena | Folclore global | ✅ Folclore |
| cyclops | Cíclope | Mitología griega | ✅ Mitología |
| oni | Oni | Folclore japonés | ✅ Folclore |
| basilisk | Basilisco | Bestiario medieval europeo | ✅ Folclore |

> **Nota legal sobre vampiros:** El concepto de "vampiro" como criatura folclórica es de dominio público. La representación específica de Drácula de Bram Stoker (1897) también es dominio público. No se utilizarán elementos protegidos por derechos de autor de adaptaciones modernas (películas, juegos, etc.).

---

## 4. Capture Mechanic — Detailed Specification

### 4.1 The Menu-Based Capture Process (Patent-Avoidant)

This is the core innovation. The capture system is designed so that EVERY element of Nintendo's patent claims is absent by design.

**Step-by-step flow:**

```
1. Player explores overworld (WASD / joystick movement)
   ↓
2. Random encounter OR scripted encounter triggers
   ↓
3. Screen transitions to COMBAT VIEW (no overworld aiming)
   ↓
4. Combat menu displays options:
   ┌─────────────────────┐
   │  PELEAR             │
   │  ITEM               │
   │  CAPTURAR           │  ← Player selects this
   │  HUIDA              │
   └─────────────────────┘
   ↓
5. Sub-menu of capture devices appears:
   ┌─────────────────────┐
   │  Sello Básico       │
   │  Sello Avanzado     │  ← Player selects one
   │  Sello de Élite     │
   └─────────────────────┘
   ↓
6. Capture resolution animation plays:
   - A sigil/summoning circle appears on the creature (pre-rendered animation)
   - NOT a thrown object
   - NO aiming reticle displayed at any point
   - NO probability indicator shown to player before resolution
   - NO directional input required
   ↓
7. Success/failure determined by RNG formula (see §4.2)
   ↓
8. If success: creature added to player's registry
   If failure: creature breaks free, continues combat
   ↓
9. Player remains in combat screen — NO mode toggle
```

**Explicit patent-avoidance mapping:**

| Patent Claim Element | This System | Avoidance Rationale |
|---|---|---|
| "determining an aiming direction based on a direction input" | ❌ No aiming. No direction input for capture. Capture selected from menu. | Menu selection is not "aiming" |
| "generating data for display of a first aiming point" | ❌ No aiming point, reticle, crosshair, or shadow indicator rendered at any time | No visual aiming aid |
| "generating data for display of an indicator indicating a likelihood of success" | ❌ No capture probability shown to player pre-capture | No indicator |
| "launching, in the aiming direction, the obtaining item" | ❌ No launch. No aiming direction. Item effect plays in-place animation on the target. | Throwing/launching requires ballistic trajectory — absent here |
| "selecting an obtaining item from a plurality of obtaining items" | ✅ Player selects from menu | This is generic UI, not patentable alone |
| "determining obtaining as successful in association with the obtaining item arriving within a vicinity" | ❌ No "arrival within a vicinity" — success determined by formula after animation plays | No spatial proximity test |
| "in association with the player character performing a second game action... launching a first virtual object representing the obtained first virtual character" | ❌ Creature is summoned via menu selection with portal animation. Not launched/thrown. | Summoning via menu ≠ launching a virtual object in aiming direction |
| "toggling between first mode and second mode" | ❌ No mode toggle. Single combat mode. | Single persistent mode |

### 4.2 Capture Success Formula

```
P_capture = baseCaptureRate × healthModifier × statusModifier × itemModifier

Where:
  baseCaptureRate = Creature.captureRate (0.0–1.0)
  healthModifier = 1.0 + (1.0 - currentHP / maxHP) × 1.5
  statusModifier = 1.0 (normal) | 1.2 (poisoned) | 1.3 (asleep) | 1.4 (paralyzed) | 1.5 (frozen)
  itemModifier = 1.0 (basic seal) | 1.5 (advanced seal) | 2.0 (elite seal)

Shake checks: roll 0.0–1.0 against P_capture, repeat up to 4 times.
If all 4 rolls < P_capture → capture succeeds.
```

This formula is publicly documented in prior art (Pokémon Gen III-IV capture mechanics, 2002–2010) and is unpatented.

### 4.3 Creature Deployment for Battle

When the player selects a creature from their party to send into battle:

```
1. Player opens party menu
2. Selects creature from list
3. Animation plays: a portal/summoning circle opens near the player character
4. Creature emerges from portal
5. Combat begins

Key patent-avoidant features:
- No aiming direction input
- No reticle display
- No "launching" of a virtual object
- Creature appears at a fixed position relative to player
- No toggle between modes
```

---

## 5. Movement System

### 5.1 Overworld Movement

- Standard WASD/joystick directional movement in a 3D block-based or mesh-based world
- No rideable creatures with aerial/water/land mode switching
- Fast travel via discovered waypoints (teleport to previously visited locations)
- Movement speed is constant (no mount speed escalation that could trigger ride-switching patents)

### 5.2 Portal Travel Between Dimensions

The game includes dimensional travel via static portal points placed in the world:

```
1. Player approaches a portal structure in the overworld
2. Interaction prompt appears: "Enter Portal?"
3. Player confirms via keypress (E / A button)
4. Loading screen transitions to new dimension/map
5. Player arrives at corresponding exit portal in destination

Key patent-avoidant features:
- Portal is a level-transition trigger, not a player-placed object
- No "aiming" at a destination
- No real-time switching between rideable objects mid-air
```

This is standard level-loading portal technology, prior art since Super Mario 64 (1996) and earlier.

---

## 6. Combat System — Turn-Based RPG Mechanics

### 6.1 Combat Flow

```
┌───────────────────────────────────────────────────────┐
│                  TURN-BASED COMBAT                      │
├───────────────────────────────────────────────────────┤
│  1. Speed calculation phase                             │
│     (each combatant sorted by speed stat)               │
├───────────────────────────────────────────────────────┤
│  2. Command input phase (player)                        │
│     ┌─────────────────────────────────────────────┐    │
│     │ FIGHT  → select move from creature's moveset │    │
│     │ ITEMS  → use consumable item                 │    │
│     │ CAPTURE→ use capture device (see §4)        │    │
│     │ SWAP   → switch active creature              │    │
│     │ FLEE   → attempt escape                     │    │
│     └─────────────────────────────────────────────┘    │
├───────────────────────────────────────────────────────┤
│  3. Action execution phase                              │
│     (actions resolved in speed order)                   │
├───────────────────────────────────────────────────────┤
│  4. Status effect application phase                     │
├───────────────────────────────────────────────────────┤
│  5. Check win/lose/capture conditions                   │
│     → If all enemy creatures defeated: VICTORY         │
│     → If all player creatures defeated: DEFEAT         │
│     → If capture succeeded: creature added to registry │
└───────────────────────────────────────────────────────┘
```

### 6.2 Move Set Examples (Public Domain Folklore-Based)

| Move Name | Element | Power | Type | Description |
|---|---|---|---|---|
| Mordida de Vampiro | UNDEAD | 40 | PHYSICAL | El vampiro muerde al objetivo, recuperando HP equivalente al daño |
| Aullido Lunar | BEAST | 0 | BUFF | Aumenta el ataque del invocador por 3 turnos |
| Mirada Petrificante | MYTHICAL | 0 | DEBUFF | Paraliza al objetivo (basado en Medusa) |
| Aliento de Fuego | DRAGON | 60 | MAGICAL | El dragón exhala llamas |
| Maldición Milenaria | SPIRIT | 50 | MAGICAL | Daño con probabilidad de maldición (reduce defensa) |
| Canto de Sirena | SPIRIT | 0 | DEBUFF | Duerme al objetivo (basado en Odisea) |
| Transformación Loba | BEAST | 0 | BUFF | Transforma al licántropo, aumentando ataque y velocidad |
| Portal Sombrio | MYTHICAL | 0 | SPECIAL | Cambia al invocador por otro del equipo sin costo de turno |
| Toque Helado | UNDEAD | 30 | MAGICAL | Daño con probabilidad de congelar |
| Escudo de Égida | MYTHICAL | 0 | BUFF | Aumenta defensa del equipo por 2 turnos |

### 6.3 Elemental Affinity System

```
FIRE  >  UNDEAD  >  SPIRIT  >  BEAST  >  DRAGON  >  FIRE
                    MYTHICAL (neutral)
```

Each creature has 1-2 elemental affinities. Moves of a matching affinity deal 1.5× damage. Opposing affinity deals 0.5× damage.

---

## 7. Primary Platform Implementation: Minecraft (Paper Plugin)

### 7.1 Architecture (Paper Plugin 1.21.4)

```
ChanequemonPlugin/
├── src/main/java/com/chanequemon/
│   ├── ChanequemonPlugin.java        // Main class, onEnable/onDisable
│   ├── combat/
│   │   ├── CombatManager.java         // Turn-based combat loop
│   │   ├── CombatScreen.java          // GUI menu (chest GUI)
│   │   ├── CaptureResolver.java       // Capture formula + shake checks
│   │   └── MoveExecutor.java          // Execute moves with effects
│   ├── creatures/
│   │   ├── CreatureRegistry.java      // Load creature data from YAML/JSON
│   │   ├── CreatureSpawner.java       // Spawn custom mobs with NBT
│   │   └── CreatureModel.java         // Data model
│   ├── world/
│   │   ├── PortalManager.java         // Dimensional travel
│   │   ├── EncounterManager.java      // Biome-based encounter tables
│   │   └── WaypointManager.java       // Fast travel
│   ├── items/
│   │   ├── CaptureSealItem.java       // Custom item for capture devices
│   │   └── BestiaryItem.java          // Creature registry book
│   └── config/
│       ├── config.yml                 // Capture rates, move data, spawn rates
│       └── creatures/                 // One YAML per creature
│           ├── vampire.yml
│           ├── werewolf.yml
│           └── ...
```

Key Minecraft-specific considerations:

- **Capture animation**: Particle effects (spell particles, end rod particles) form a summoning circle around the creature. No thrown item entity is used.
- **Combat UI**: Chest GUI with item icons representing menu options. No hotbar aiming.
- **No aiming**: Capture is triggered by clicking a GUI slot, not by looking at a mob and right-clicking.
- **Creature models**: Use Minecraft's existing entity models with custom NBT tags for stats. No custom model imports needed for basic implementation.

### 7.2 Crafting Capture Items

```
Sello Básico: 4× Hierro + 1× Ojo de Araña + 1× Lapislázuli
Sello Avanzado: 4× Oro + 1× Perla de Ender + 1× Polvo de Redstone
Sello de Élite: 4× Diamante + 1× Estrella de Nether + 1× Bloque de Cuarzo
```

---

## 8. Alternative Platform: Roblox

### 8.1 Architecture

```
Chanequemon.rbxl
├── ServerScriptService/
│   ├── CombatHandler.lua           // Turn-based combat server logic
│   ├── CaptureHandler.lua          // Capture formula execution
│   ├── CreatureRegistry.lua        // Creature database ModuleScript
│   ├── EncounterManager.lua        // Biome-based encounter spawning
│   └── PortalHandler.lua           // Teleportation between maps
├── ReplicatedStorage/
│   ├── CreatureData/               // ModuleScripts per creature
│   │   ├── Vampire.lua
│   │   └── ...
│   ├── GuiComponents/              // Combat GUI (ScreenGui)
│   │   ├── CombatMenuFrame
│   │   ├── MoveSelectionFrame
│   │   └── CaptureAnimation
│   └── SharedTypes.lua             // Type definitions
└── StarterGui/
    └── ChanequemonGui            // Main game GUI
```

Key Roblox-specific considerations:

- **Combat GUI**: ScreenGui with TextButtons for menu actions. No mouse-aiming reticle.
- **Capture animation**: TweenService animates a Beam or ParticleEmitter in a circle pattern. No projectile.
- **No raycasting for capture**: The capture action does not use raycasts, Mouse.Hit, or any directional input.
- **Creature models**: MeshParts with custom textures. No reliance on Roblox's built-in character rigs.

---

## 9. Patent Avoidance — Comprehensive Analysis

### 9.1 Nintendo/Game Freak Patents Addressed

| Patent | Claims | This System | Status |
|---|---|---|---|
| JP7545191 / US 12,409,387 | Rideable character switching mid-air | ❌ No rideable characters. Standard walking + waypoint teleport. | ✅ Avoided |
| JP7493117 / US 12,179,111 | Throwing capture item + directional aiming | ❌ Menu capture. No throwing. No aiming. No directional input for capture. | ✅ Avoided |
| JP7528390 / US 12,220,638 | Aim capture → launch creature into battle | ❌ Capture is menu-based. Creature deployment is menu-based portal summon. | ✅ Avoided |
| US 12,403,397 | Summoning a secondary character via launch | ❌ Creature summoned via menu + animation at fixed position. No launch. | ✅ Avoided |
| US 12,246,255 | Switching between rideable objects | ❌ No rideable objects. | ✅ Avoided |

### 9.2 Other Known Game Patents

| Patent Holder | Patent | Claims | This System | Status |
|---|---|---|---|---|
| Warner Bros. | US 8,082,245 (Nemesis System) | Procedural enemy relationships | ❌ No procedural nemesis system. Static creature encounters. | ✅ Avoided |
| Activision | US 8,403,743 | Match-3 puzzle mechanics | ❌ Not applicable. | ✅ Avoided |
| Sega | US 6,200,220 | Rhythm game timing | ❌ Not applicable. | ✅ Avoided |

### 9.3 Design Rationale for Patent Avoidance

Every design decision in this system follows three rules:

1. **No aiming input for capture or summoning.** The player never specifies a direction, angle, or target reticle. All target selection is handled by the combat engine.
2. **No mode toggling.** The game does not switch between "capture mode" and "battle mode." Capture is a battle action, not a separate game state.
3. **No thrown/launched objects.** All capture devices and creature deployments use in-place animations (sigils, portals, summoning circles). No projectile entity with ballistic trajectory is created.

These three rules ensure the system operates in the same legal space as Pokémon Red/Blue (1996), which is unpatented prior art.

---

## 10. Alternative Embodiments

To prevent narrow workarounds, the following alternative implementations are also disclosed:

### 10.1 Capture Animation Variations
- Instead of a sigil: a portal opens beneath the creature, it sinks in. No thrown object.
- Instead of a sigil: chains/shadows emerge from the ground and restrain the creature. No thrown object.
- Instead of a sigil: the creature is surrounded by floating runes that fade in. No thrown object.

### 10.2 Combat UI Variations
- Command list (text-based, like classic RPGs) instead of icon grid.
- Radial menu instead of linear list.
- Keyboard shortcuts (1-5) instead of mouse click.
- Voice command input for accessibility.

### 10.3 Capture Item Variations
- Instead of "seals": enchanted flasks, crystal orbs, ancient scrolls, totems.
- Instead of items in inventory: capture ability unlocked via quest progression.

### 10.4 Platform Variations
- Standalone PC executable (Godot, Unity, Unreal Engine — using platform-agnostic UI frameworks).
- Web browser (Phaser, PixiJS).
- Console (as a custom game, not a mod).
- Roblox (Luau, see §8).

### 10.5 Trigger Variations for Encounters
- Random encounters in grass/caves (classic RPG style).
- Visible creatures in overworld that trigger combat on proximity (no aiming).
- Scripted boss encounters at fixed locations.
- Fishing/minigame encounters at specific biomes.

---

## 11. Public Domain Dedication

This document and all associated source code are dedicated to the public domain under CC0 1.0 Universal.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED.

The author reserves no patent, copyright, or other intellectual property rights in the systems, methods, and algorithms described herein. This disclosure is made for the purpose of establishing prior art and preventing future patent claims on the described subject matter.

---

## 12. Appendix: Creature Data Examples

### Example 1: Vampire (YAML format for Minecraft implementation)

```yaml
id: vampire_dracula
displayName: "Vampiro de Transilvania"
loreOrigin: "Drácula — Bram Stoker (1897); folclore rumano"
publicDomainSource: true
type: UNDEAD
stats:
  hp: 85
  maxHp: 85
  attack: 75
  defense: 50
  speed: 65
  elementalAffinity: UNDEAD
captureRate: 0.15
evolutionTree: []
summonAnimation: MIST
moveSet:
  - id: bite
    displayName: "Mordida de Vampiro"
    power: 40
    moveType: PHYSICAL
    element: UNDEAD
    description: "Muerde al objetivo. Recupera HP igual al daño infligido."
    apCost: 10
    priority: 1
    statusEffect: null
  - id: hypnosis
    displayName: "Hipnosis"
    power: 0
    moveType: STATUS
    element: SPIRIT
    description: "Duerme al objetivo."
    apCost: 15
    priority: 0
    statusEffect: SLEEP
  - id: shadow_mist
    displayName: "Niebla Sombría"
    power: 0
    moveType: BUFF
    element: UNDEAD
    description: "Aumenta la evasión del invocador por 3 turnos."
    apCost: 8
    priority: 2
    statusEffect: null
  - id: drain_life
    displayName: "Drenar Vida"
    power: 55
    moveType: MAGICAL
    element: UNDEAD
    description: "Drena la energía vital del objetivo."
    apCost: 12
    priority: 0
    statusEffect: null
spawnConditions:
  biomes:
    - DARK_FOREST
    - SWAMP
    - ROOFED_FOREST
  time: NIGHT
  probability: 0.08
drops:
  - item: "polvo_de_vampiro"
    chance: 0.5
    amount: 1
```

### Example 2: Kitsune (YAML)

```yaml
id: kitsune
displayName: "Kitsune"
loreOrigin: "Folclore japonés"
publicDomainSource: true
type: SPIRIT
stats:
  hp: 60
  maxHp: 60
  attack: 55
  defense: 40
  speed: 80
  elementalAffinity: MYSTICAL
captureRate: 0.20
evolutionTree:
  - kitsune_nueve_colas
summonAnimation: LIGHT
moveSet:
  - id: fox_fire
    displayName: "Fuego de Zorro"
    power: 35
    moveType: MAGICAL
    element: MYSTICAL
    description: "Lanza llamas fantasmales."
    apCost: 8
    priority: 0
    statusEffect: BURN
  - id: illusion
    displayName: "Ilusión"
    power: 0
    moveType: DEBUFF
    element: MYSTICAL
    description: "Confunde al objetivo."
    apCost: 12
    priority: 1
    statusEffect: CONFUSE
  - id: shapeshift
    displayName: "Cambiaformas"
    power: 0
    moveType: BUFF
    element: MYSTICAL
    description: "Aumenta velocidad y evasión."
    apCost: 10
    priority: 2
    statusEffect: null
spawnConditions:
  biomes:
    - BAMBOO_JUNGLE
    - CHERRY_GROVE
    - FLOWER_FOREST
  time: ANY
  probability: 0.05
```

---

## 13. Appendix: Move Data from Tuxemon (Prior Art Reference)

The movement/move system is structurally identical to the system used in Tuxemon (GPLv3, open source, available at https://github.com/Tuxemon/Tuxemon). Tuxemon's move system consists of:

- **Techniques**: Learnable combat actions with power, accuracy, type, and effects
- **AP (Action Points)**: Limited uses per technique, restored at rest points
- **Type effectiveness**: Elemental rock-paper-scissors system
- **Status effects**: Poison, sleep, paralysis, burn, freeze, confusion

This system is derived from prior art predating 2000 (Pokémon Red/Blue, 1996) and is unpatented. The implementation in this disclosure follows the same unpatented pattern.

---

*End of disclosure.*
