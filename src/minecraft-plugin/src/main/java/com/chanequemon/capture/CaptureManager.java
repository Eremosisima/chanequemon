package com.chanequemon.capture;

import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.CreatureInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class CaptureManager {
    private static final double BASE_CAPTURE_RATE = 0.3;
    private final ChanequeCurseManager curseManager;
    private final Random rng = new Random();

    public CaptureManager(ChanequeCurseManager curseManager) {
        this.curseManager = curseManager;
    }

    public int getCreatureLevel(CreatureInstance creature) {
        int hpLevel = Math.max(1, creature.maxHp() / 18);
        double rate = creature.template().captureRate();
        int rateBonus = rate > 0
            ? (int) Math.round((0.2 - Math.min(rate, 0.2)) * 50)
            : 8;
        return Math.max(1, Math.min(30, hpLevel + rateBonus));
    }

    public String getDifficultyLabel(CreatureInstance creature) {
        int level = getCreatureLevel(creature);
        if (level <= 5) return "Facil";
        if (level <= 10) return "Media";
        if (level <= 18) return "Dificil";
        if (level <= 25) return "Muy Dificil";
        return "Legendaria";
    }

    public boolean attemptCapture(CreatureInstance creature) {
        int level = getCreatureLevel(creature);
        double rate = creature.template().captureRate() > 0
            ? creature.template().captureRate() : BASE_CAPTURE_RATE;
        double hpFactor = 1.0 - (1.0 - creature.hpRatio()) * 0.5;
        double statusBonus = creature.statusEffect() != null ? 0.15 : 0.0;
        double levelResistance = Math.max(0.05, 1.0 - (level * 0.03));
        double threshold = rate * hpFactor * (1.0 + statusBonus) * levelResistance;
        return rng.nextDouble() < threshold;
    }

    public int getCurseBookCount(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (curseManager.isCurseBook(item)) count += item.getAmount();
        }
        return count;
    }

    public int findCurseBookSlot(Player player) {
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getContents()[i];
            if (curseManager.isCurseBook(item)) return i;
        }
        return -1;
    }

    public ItemStack removeCurseBook(Player player) {
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getContents()[i];
            if (curseManager.isCurseBook(item)) {
                ItemStack removed = item.clone();
                removed.setAmount(1);
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    player.getInventory().setItem(i, null);
                }
                return removed;
            }
        }
        return null;
    }

    public double getCaptureThreshold(CreatureInstance creature) {
        int level = getCreatureLevel(creature);
        double rate = creature.template().captureRate() > 0
            ? creature.template().captureRate() : BASE_CAPTURE_RATE;
        double hpFactor = 1.0 - (1.0 - creature.hpRatio()) * 0.5;
        double statusBonus = creature.statusEffect() != null ? 0.15 : 0.0;
        double levelResistance = Math.max(0.05, 1.0 - (level * 0.03));
        return rate * hpFactor * (1.0 + statusBonus) * levelResistance;
    }
}
