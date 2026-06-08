package com.chanequemon.player;

import java.util.*;

public class PlayerData {
    private final UUID playerId;
    private final Set<String> capturedIds;
    private int chanequeballs;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.capturedIds = new LinkedHashSet<>();
        this.chanequeballs = 5;
    }

    public UUID playerId() { return playerId; }
    public Set<String> capturedIds() { return capturedIds; }
    public int chanequeballs() { return chanequeballs; }
    public void setChanequeballs(int count) { this.chanequeballs = count; }

    public boolean addCaptured(String creatureId) { return capturedIds.add(creatureId); }
    public boolean hasCaptured(String creatureId) { return capturedIds.contains(creatureId); }
    public int capturedCount() { return capturedIds.size(); }
}
