package com.chanequemon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Creature {
    private final String id;
    private final String displayName;
    private final String loreOrigin;
    private final boolean publicDomainSource;
    private final Element type;
    private final Element affinity;
    private final int baseHp;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;
    private final int baseLck;
    private final int baseWis;
    private final double captureRate;
    private final String summonAnimation;
    private final List<Move> moves;
    private final List<String> spawnBiomes;
    private final String spawnTime;
    private final double spawnProbability;
    private final List<String> spawnStructures;
    private final List<String> spawnPotionEffects;
    private final List<String> spawnPlayerEffects;
    private final List<String> spawnEnchantments;
    private final int badOmenMin;
    private final int badOmenMax;
    private final List<SupportAbility> supportAbilities;

    @SuppressWarnings("unchecked")
    public Creature(String id, String displayName, String loreOrigin, boolean publicDomainSource,
                    String typeStr, Map<String, Object> statsRaw, String affinityStr,
                    double captureRate, String summonAnimation,
                    List<Map<String, Object>> moveMaps,
                    Map<String, Object> spawnConditions,
                    List<Map<String, Object>> supportMaps) {
        this.id = id;
        this.displayName = displayName;
        this.loreOrigin = loreOrigin;
        this.publicDomainSource = publicDomainSource;
        this.type = safeParseElement(typeStr);
        this.affinity = safeParseElement(affinityStr);
        this.baseHp = intOr(statsRaw.get("hp"), 50);
        this.baseAttack = intOr(statsRaw.get("attack"), 50);
        this.baseDefense = intOr(statsRaw.get("defense"), 50);
        this.baseSpeed = intOr(statsRaw.get("speed"), 50);
        this.baseLck = intOr(statsRaw.get("lck"), 10);
        this.baseWis = intOr(statsRaw.get("wis"), 10);
        this.captureRate = captureRate;
        this.summonAnimation = summonAnimation;

        this.moves = new ArrayList<>();
        if (moveMaps != null) {
            for (Map<String, Object> m : moveMaps) {
                moves.add(new Move(
                    str(m.get("id")),
                    str(m.get("displayName")),
                    intOr(m.get("power"), 0),
                    parseMoveType(str(m.get("type"))),
                    safeParseElement(str(m.get("element"))),
                    intOr(m.get("apCost"), 10),
                    str(m.get("statusEffect"))
                ));
            }
        }

        List<String> pe = List.of();
        List<String> ench = List.of();
        int boMin = 0, boMax = 0;

        if (spawnConditions != null && !spawnConditions.isEmpty()) {
            Object biomesObj = spawnConditions.get("biomes");
            List<String> biomes = biomesObj instanceof List ? (List<String>) biomesObj : List.of();
            this.spawnBiomes = biomes != null ? biomes : List.of();
            this.spawnTime = str(spawnConditions.get("time"));
            this.spawnProbability = doubleOr(spawnConditions.get("probability"), 0.05);
            Object structsObj = spawnConditions.get("structures");
            this.spawnStructures = structsObj instanceof List
                ? ((List<String>) structsObj).stream().map(s -> s.toUpperCase()).toList()
                : List.of();
            Object condsObj = spawnConditions.get("conditions");
            if (condsObj instanceof Map conds) {
                Object peObj = conds.get("potion_effects");
                this.spawnPotionEffects = peObj instanceof List
                    ? ((List<String>) peObj).stream().map(String::toUpperCase).toList()
                    : List.of();
                Object playerEffObj = conds.get("player_effects");
                pe = playerEffObj instanceof List
                    ? ((List<String>) playerEffObj).stream().map(String::toUpperCase).toList()
                    : List.of();
                Object enchObj = conds.get("enchantments");
                ench = enchObj instanceof List
                    ? ((List<String>) enchObj).stream().map(String::toUpperCase).toList()
                    : List.of();
                Object boObj = conds.get("bad_omen_level");
                if (boObj instanceof Map boMap) {
                    boMin = intOr(boMap.get("min"), 0);
                    boMax = intOr(boMap.get("max"), 5);
                }
            } else {
                this.spawnPotionEffects = List.of();
            }
        } else {
            this.spawnBiomes = List.of();
            this.spawnTime = "ANY";
            this.spawnProbability = 0.05;
            this.spawnStructures = List.of();
            this.spawnPotionEffects = List.of();
        }
        this.spawnPlayerEffects = pe;
        this.spawnEnchantments = ench;
        this.badOmenMin = boMin;
        this.badOmenMax = boMax;

        this.supportAbilities = new ArrayList<>();
        if (supportMaps != null) {
            for (Map<String, Object> m : supportMaps) {
                this.supportAbilities.add(SupportAbility.fromMap(m));
            }
        }
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String loreOrigin() { return loreOrigin; }
    public boolean isPublicDomain() { return publicDomainSource; }
    public Element type() { return type; }
    public Element affinity() { return affinity; }
    public int baseHp() { return baseHp; }
    public int baseAttack() { return baseAttack; }
    public int baseDefense() { return baseDefense; }
    public int baseSpeed() { return baseSpeed; }
    public int baseLck() { return baseLck; }
    public int baseWis() { return baseWis; }
    public double captureRate() { return captureRate; }
    public String summonAnimation() { return summonAnimation; }
    public List<Move> moves() { return moves; }
    public List<String> spawnBiomes() { return spawnBiomes; }
    public String spawnTime() { return spawnTime; }
    public double spawnProbability() { return spawnProbability; }
    public List<String> spawnStructures() { return spawnStructures; }
    public List<String> spawnPotionEffects() { return spawnPotionEffects; }
    public List<String> spawnPlayerEffects() { return spawnPlayerEffects; }
    public List<String> spawnEnchantments() { return spawnEnchantments; }
    public int badOmenMin() { return badOmenMin; }
    public int badOmenMax() { return badOmenMax; }
    public List<SupportAbility> supportAbilities() { return supportAbilities; }

    public boolean hasSpawnConditions() {
        return !spawnBiomes.isEmpty() || !spawnStructures.isEmpty()
            || !spawnPotionEffects.isEmpty() || !spawnPlayerEffects.isEmpty()
            || !spawnEnchantments.isEmpty() || badOmenMin > 0;
    }

    private static Element safeParseElement(String s) {
        if (s == null || s.isBlank()) return Element.BEAST;
        try { return Element.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return Element.BEAST; }
    }

    private static MoveType parseMoveType(String s) {
        if (s == null || s.isBlank()) return MoveType.PHYSICAL;
        try { return MoveType.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e) { return MoveType.PHYSICAL; }
    }

    private static String str(Object o) { return o != null ? o.toString() : ""; }
    private static int intOr(Object o, int def) { return o instanceof Number n ? n.intValue() : def; }
    private static double doubleOr(Object o, double def) { return o instanceof Number n ? n.doubleValue() : def; }
}
