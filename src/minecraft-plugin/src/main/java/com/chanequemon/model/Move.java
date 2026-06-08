package com.chanequemon.model;

public class Move {
    private final String id;
    private final String displayName;
    private final int power;
    private final MoveType moveType;
    private final Element element;
    private final int apCost;
    private final String statusEffect;

    public Move(String id, String displayName, int power, MoveType moveType,
                Element element, int apCost, String statusEffect) {
        this.id = id;
        this.displayName = displayName;
        this.power = power;
        this.moveType = moveType;
        this.element = element;
        this.apCost = apCost;
        this.statusEffect = statusEffect;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public int power() { return power; }
    public MoveType moveType() { return moveType; }
    public Element element() { return element; }
    public int apCost() { return apCost; }
    public String statusEffect() { return statusEffect; }
    public boolean isStatus() { return moveType == MoveType.STATUS; }
}
