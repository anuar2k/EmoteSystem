package org.terasology.emoteSystem.event;

import org.terasology.entitySystem.event.Event;

/**
 * Event called by EmoteScreen when user selects a emote and releases the {@link org.terasology.emoteSystem.bind.EmoteScreenButton} key.
 */
public class EmoteSelectedEvent implements Event {
    private String emoteID;

    public EmoteSelectedEvent(String emoteID) {
        this.emoteID = emoteID;
    }

    public String getEmoteID() {
        return emoteID;
    }
}
