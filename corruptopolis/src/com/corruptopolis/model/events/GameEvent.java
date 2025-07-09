package com.corruptopolis.model.events;

public class GameEvent {
    private EventType type;
    private int turn;
    private String description;

    public GameEvent(EventType type, int turn, String description) {
        this.type = type;
        this.turn = turn;
        this.description = description;
    }

    public EventType getType() { return type; }
    public int getTurn() { return turn; }
    public String getDescription() { return description; }
}
