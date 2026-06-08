package com.chanequemon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatureInstance {
    private static final Random RNG = new Random();

    private final Creature template;
    private final int maxHp;
    private int hp;
    private final int attack;
    private final int defense;
    private final int speed;
    private final List<Move> moves;
    private final List<Integer> moveAp;
    private String statusEffect;
    private boolean wild;

    public CreatureInstance(Creature template, boolean wild) {
        this.template = template;
        this.wild = wild;
        int ivHp = RNG.nextInt(16);
        int ivAtk = RNG.nextInt(16);
        int ivDef = RNG.nextInt(16);
        int ivSpd = RNG.nextInt(16);
        this.maxHp = (template.baseHp() * 2 + ivHp) * 5 + 50;
        this.hp = maxHp;
        this.attack = (template.baseAttack() * 2 + ivAtk) * 5 + 5;
        this.defense = (template.baseDefense() * 2 + ivDef) * 5 + 5;
        this.speed = (template.baseSpeed() * 2 + ivSpd) * 5 + 5;
        this.moves = new ArrayList<>(template.moves());
        this.moveAp = new ArrayList<>();
        for (Move m : moves) {
            this.moveAp.add(m.apCost() * 3);
        }
        this.statusEffect = null;
    }

    public Creature template() { return template; }
    public int maxHp() { return maxHp; }
    public int hp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(maxHp, hp)); }
    public int attackStat() { return attack; }
    public int defenseStat() { return defense; }
    public int speedStat() { return speed; }
    public boolean isWild() { return wild; }
    public void setWild(boolean wild) { this.wild = wild; }
    public String statusEffect() { return statusEffect; }
    public void setStatusEffect(String effect) { this.statusEffect = effect; }
    public List<Move> moves() { return moves; }
    public List<Integer> moveAp() { return moveAp; }
    public boolean isFainted() { return hp <= 0; }

    public double hpRatio() { return (double) hp / maxHp; }

    public int calculateDamage(Move move, CreatureInstance defender) {
        if (move.isStatus()) return 0;
        double a = this.attack;
        double d = defender.defense;
        double power = move.power();
        double stab = move.element() == template.affinity() ? 1.5 : 1.0;
        double effectiveness = typeEffectiveness(move.element(), defender.template().affinity());
        double raw = ((2.0 * 50 / 5 + 2) * power * a / d / 50 + 2) * stab * effectiveness;
        double variance = 0.85 + RNG.nextDouble() * 0.15;
        return Math.max(1, (int) Math.round(raw * variance));
    }

    private static double typeEffectiveness(Element attack, Element defend) {
        if (attack == defend) return 1.0;
        switch (attack) {
            case SPIRIT: return (defend == UNDEAD) ? 2.0 : (defend == BEAST ? 0.5 : 1.0);
            case UNDEAD: return (defend == SPIRIT) ? 2.0 : (defend == MYTHIC ? 0.5 : 1.0);
            case DRAGON: return (defend == MYTHIC) ? 2.0 : (defend == SPIRIT ? 0.5 : 1.0);
            case MYTHIC: return (defend == DRAGON) ? 2.0 : (defend == UNDEAD ? 0.5 : 1.0);
            case BEAST:  return (defend == PLANT) ? 2.0 : (defend == SPIRIT ? 0.5 : 1.0);
            case FIRE:   return (defend == PLANT) ? 2.0 : (defend == WATER ? 0.5 : 1.0);
            case WATER:  return (defend == FIRE) ? 2.0 : (defend == EARTH ? 0.5 : 1.0);
            case EARTH:  return (defend == WATER) ? 2.0 : (defend == AIR ? 0.5 : 1.0);
            case AIR:    return (defend == EARTH) ? 2.0 : (defend == FIRE ? 0.5 : 1.0);
            case PLANT:  return (defend == EARTH) ? 2.0 : (defend == FIRE ? 0.5 : 1.0);
            default:     return 1.0;
        }
    }

    public void useAp(int moveIndex) {
        if (moveIndex >= 0 && moveIndex < moveAp.size()) {
            moveAp.set(moveIndex, Math.max(0, moveAp.get(moveIndex) - 1));
        }
    }

    public boolean hasAp(int moveIndex) {
        return moveIndex >= 0 && moveIndex < moveAp.size() && moveAp.get(moveIndex) > 0;
    }
}
