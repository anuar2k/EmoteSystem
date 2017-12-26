package org.terasology.emoteSystem.event;

import org.terasology.entitySystem.event.Event;

public class EmoteSelectedEvent implements Event {
    private String emoteID;

    public EmoteSelectedEvent(String emoteID) {
        this.emoteID = emoteID;
    }

    public String getEmoteID() {
        return emoteID;
    }
}
