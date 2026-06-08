package com.chanequemon.model;

import com.chanequemon.util.FibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatureInstance {
    private static final Random RNG = new Random();

    private final Creature template;
    private final int baseMaxHp;
    private int hp;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;
    private final int lck;
    private final int wis;
    private final List<Move> moves;
    private final List<Integer> moveAp;
    private String statusEffect;
    private boolean wild;

    private int xp;
    private int battleLevel;
    private int statBonus;

    public CreatureInstance(Creature template, boolean wild) {
        this.template = template;
        this.wild = wild;
        int ivHp = RNG.nextInt(16);
        int ivAtk = RNG.nextInt(16);
        int ivDef = RNG.nextInt(16);
        int ivSpd = RNG.nextInt(16);
        int ivLck = RNG.nextInt(16);
        int ivWis = RNG.nextInt(16);
        this.baseMaxHp = (template.baseHp() * 2 + ivHp) * 5 + 50;
        this.hp = baseMaxHp;
        this.baseAttack = (template.baseAttack() * 2 + ivAtk) * 5 + 5;
        this.baseDefense = (template.baseDefense() * 2 + ivDef) * 5 + 5;
        this.baseSpeed = (template.baseSpeed() * 2 + ivSpd) * 5 + 5;
        this.lck = (template.baseLck() * 2 + ivLck) * 5 + 5;
        this.wis = (template.baseWis() * 2 + ivWis) * 5 + 5;
        this.moves = new ArrayList<>(template.moves());
        this.moveAp = new ArrayList<>();
        for (Move m : moves) {
            this.moveAp.add(m.apCost() * 3);
        }
        this.statusEffect = null;
        this.xp = 0;
        this.battleLevel = 1;
        this.statBonus = 0;
    }

    public Creature template() { return template; }
    public int maxHp() { return baseMaxHp + statBonus; }
    public int hp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(maxHp(), hp)); }
    public int attackStat() {
        int base = baseAttack + statBonus;
        if ("BURN".equals(statusEffect)) return base / 2;
        return base;
    }
    public int defenseStat() { return baseDefense + statBonus; }
    public int speedStat() {
        int base = baseSpeed + statBonus;
        if ("PARALYSIS".equals(statusEffect)) return base / 2;
        return base;
    }
    public int lckStat() { return lck; }
    public int wisStat() { return wis; }
    public boolean isWild() { return wild; }
    public void setWild(boolean wild) { this.wild = wild; }
    public String statusEffect() { return statusEffect; }
    public void setStatusEffect(String effect) { this.statusEffect = effect; }
    public List<Move> moves() { return moves; }
    public List<Integer> moveAp() { return moveAp; }
    public boolean isFainted() { return hp <= 0; }
    public int xp() { return xp; }
    public int battleLevel() { return battleLevel; }
    public int statBonus() { return statBonus; }

    public void setPersistentData(int level, int xp, int statBonus) {
        this.battleLevel = level;
        this.xp = xp;
        this.statBonus = statBonus;
    }

    public double hpRatio() { return (double) hp / maxHp(); }

    public int xpToNextLevel() {
        return Math.max(1, FibUtil.fib(battleLevel + 2));
    }

    public boolean gainXp(int amount) {
        xp += amount;
        boolean leveled = false;
        while (xp >= xpToNextLevel()) {
            xp -= xpToNextLevel();
            battleLevel++;
            statBonus += 2;
            hp += 2;
            leveled = true;
        }
        return leveled;
    }

    public boolean canAct() {
        if (statusEffect == null) return true;
        return switch (statusEffect.toUpperCase()) {
            case "SLEEP" -> RNG.nextDouble() < 0.5;
            case "PARALYSIS" -> RNG.nextDouble() < 0.75;
            default -> true;
        };
    }

    public void processStatusEndOfTurn() {
        if (statusEffect == null) return;
        switch (statusEffect.toUpperCase()) {
            case "POISON" -> hp = Math.max(0, hp - Math.max(1, maxHp() / 8));
            case "BURN" -> hp = Math.max(0, hp - Math.max(1, maxHp() / 16));
        }
    }

    public int calculateDamage(Move move, CreatureInstance defender) {
        if (move.isStatus()) return 0;
        double a = this.attackStat();
        double d = defender.defenseStat();
        double power = move.power();
        double stab = move.element() == template.affinity() ? 1.5 : 1.0;
        double effectiveness = typeEffectiveness(move.element(), defender.template().affinity());
        double raw = ((2.0 * 50 / 5 + 2) * power * a / d / 50 + 2) * stab * effectiveness;
        double variance = 0.85 + RNG.nextDouble() * 0.15;
        int damage = Math.max(1, (int) Math.round(raw * variance));
        if (RNG.nextDouble() < this.lck / 1000.0) {
            damage = (int) (damage * 1.5);
        }
        return damage;
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
