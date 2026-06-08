package com.chanequemon.model;

import java.util.List;
import java.util.UUID;

public class CombatSession {
    public enum State {
        MAIN, FIGHT_SELECT, BAG_SELECT, CREATURE_SELECT, ANIMATING, CAPTURING, ENDED
    }

    private final UUID playerId;
    private final CreatureInstance playerCreature;
    private final List<CreatureInstance> playerParty;
    private final CreatureInstance wildCreature;
    private State state;
    private String message;
    private int selectedMoveIndex;
    private boolean playerTurn;

    public CombatSession(UUID playerId, CreatureInstance playerCreature,
                         List<CreatureInstance> playerParty, CreatureInstance wildCreature) {
        this.playerId = playerId;
        this.playerCreature = playerCreature;
        this.playerParty = playerParty;
        this.wildCreature = wildCreature;
        this.state = State.MAIN;
        this.message = "Salvaje " + wildCreature.template().displayName() + " aparecio!";
        this.selectedMoveIndex = -1;
        this.playerTurn = playerCreature.speedStat() >= wildCreature.speedStat();
    }

    public UUID playerId() { return playerId; }
    public CreatureInstance playerCreature() { return playerCreature; }
    public List<CreatureInstance> playerParty() { return playerParty; }
    public CreatureInstance wildCreature() { return wildCreature; }
    public State state() { return state; }
    public void setState(State state) { this.state = state; }
    public String message() { return message; }
    public void setMessage(String msg) { this.message = msg; }
    public int selectedMoveIndex() { return selectedMoveIndex; }
    public void setSelectedMoveIndex(int i) { this.selectedMoveIndex = i; }
    public boolean isPlayerTurn() { return playerTurn; }
    public void setPlayerTurn(boolean pt) { this.playerTurn = pt; }
}
