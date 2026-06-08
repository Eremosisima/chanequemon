package com.chanequemon.model;

import java.util.Map;

public class SupportAbility {
    private final String aura;
    private final String effect;
    private final int amplifier;
    private final int radius;
    private final String target;
    private final String description;

    public SupportAbility(String aura, String effect, int amplifier, int radius, String target, String description) {
        this.aura = aura;
        this.effect = effect;
        this.amplifier = amplifier;
        this.radius = radius;
        this.target = target;
        this.description = description;
    }

    @SuppressWarnings("unchecked")
    public static SupportAbility fromMap(Map<String, Object> map) {
        String aura = str(map.get("aura"));
        String effect = str(map.get("effect"));
        int amplifier = intOr(map.get("amplifier"), 0);
        int radius = intOr(map.get("radius"), 8);
        String target = str(map.get("target"));
        String description = str(map.get("description"));
        return new SupportAbility(aura, effect, amplifier, radius, target, description);
    }

    public String aura() { return aura; }
    public String effect() { return effect; }
    public int amplifier() { return amplifier; }
    public int radius() { return radius; }
    public String target() { return target; }
    public String description() { return description; }

    public boolean isBuff() { return "BUFF".equalsIgnoreCase(aura); }
    public boolean isDebuff() { return "DEBUFF".equalsIgnoreCase(aura); }

    private static String str(Object o) { return o != null ? o.toString() : ""; }
    private static int intOr(Object o, int def) { return o instanceof Number n ? n.intValue() : def; }
}
