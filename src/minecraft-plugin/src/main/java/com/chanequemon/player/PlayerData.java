package com.chanequemon.player;

import java.util.*;

public class PlayerData {
    private final UUID playerId;
    private final Set<String> capturedIds;
    private final Map<String, int[]> creatureProgression;
    private int chanequeballs;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.capturedIds = new LinkedHashSet<>();
        this.creatureProgression = new HashMap<>();
        this.chanequeballs = 5;
    }

    public UUID playerId() { return playerId; }
    public Set<String> capturedIds() { return capturedIds; }
    public int chanequeballs() { return chanequeballs; }
    public void setChanequeballs(int count) { this.chanequeballs = count; }

    public boolean addCaptured(String creatureId) { return capturedIds.add(creatureId); }
    public boolean hasCaptured(String creatureId) { return capturedIds.contains(creatureId); }
    public int capturedCount() { return capturedIds.size(); }

    public int[] getProgression(String creatureId) {
        return creatureProgression.getOrDefault(creatureId, new int[]{1, 0, 0});
    }

    public void setProgression(String creatureId, int level, int xp, int statBonus) {
        creatureProgression.put(creatureId, new int[]{level, xp, statBonus});
    }
}
