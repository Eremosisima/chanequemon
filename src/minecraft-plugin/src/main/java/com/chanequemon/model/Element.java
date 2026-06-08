package com.chanequemon.model;

import java.util.Set;

public enum Element {
    BEAST, SPIRIT, UNDEAD, DRAGON, MYTHIC, DEMON,
    WATER, FIRE, EARTH, AIR, PLANT;

    private static final Set<Element> UNDEAD_LIKE = Set.of(SPIRIT, UNDEAD, DEMON);

    public boolean isUndeadLike() {
        return UNDEAD_LIKE.contains(this);
    }

    public static boolean isUndeadLike(String name) {
        if (name == null) return false;
        try { return valueOf(name.toUpperCase()).isUndeadLike(); }
        catch (IllegalArgumentException e) { return false; }
    }
}
