package org.terasology.emoteSystem.component;

import org.terasology.entitySystem.Component;

/**
 * Component carrying all information about the emote.
 * This component must be included in a prefab.
 * Currently, the EmoteComponent has only a message to send.
 */
public class EmoteComponent implements Component {
    public String message = "<default message>";
}
