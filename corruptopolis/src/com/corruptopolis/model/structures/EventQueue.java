package com.corruptopolis.model.structures;

import com.corruptopolis.model.events.GameEvent;
import java.util.*;

public class EventQueue {
    private PriorityQueue<GameEvent> events;

    public EventQueue() {
        this.events = new PriorityQueue<>(Comparator.comparingInt(GameEvent::getTurn));
    }

    public void addEvent(GameEvent event) {
        events.offer(event);
    }

    public List<GameEvent> getEventsForTurn(int turn) {
        List<GameEvent> currentTurnEvents = new ArrayList<>();
        while (!events.isEmpty() && events.peek().getTurn() == turn) {
            currentTurnEvents.add(events.poll());
        }
        return currentTurnEvents;
    }
}
