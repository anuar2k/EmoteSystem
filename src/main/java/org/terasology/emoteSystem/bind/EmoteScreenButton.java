package org.terasology.emoteSystem.bind;

import org.terasology.input.BindButtonEvent;
import org.terasology.input.DefaultBinding;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;
import org.terasology.input.RegisterBindButton;

/**
 * Bind used to open emote selection screen.
 */
@RegisterBindButton(id = "emote", description = "Displays available emotes")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.C)
public class EmoteScreenButton extends BindButtonEvent {
}
