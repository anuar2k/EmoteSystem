package org.terasology.emoteSystem.bind;

import org.terasology.input.BindButtonEvent;
import org.terasology.input.DefaultBinding;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;
import org.terasology.input.RegisterBindButton;

@RegisterBindButton(id = "emoteConfig", description = "Allows the player to select emotes for later use.")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.F9)
public class EmoteConfigButton extends BindButtonEvent {
}
