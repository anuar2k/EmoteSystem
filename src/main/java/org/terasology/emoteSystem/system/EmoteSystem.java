package org.terasology.emoteSystem.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.emoteSystem.bind.EmoteConfigButton;
import org.terasology.emoteSystem.bind.EmoteScreenButton;
import org.terasology.emoteSystem.component.EmoteComponent;
import org.terasology.emoteSystem.event.EmoteSelectedEvent;
import org.terasology.emoteSystem.ui.EmoteScreen;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.logic.chat.ChatMessageEvent;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * System covering most of the module's functionality. Loads EmoteComponents from prefabs and handles all events.
 * @author anuar2k
 */
//TODO: Create settings system to enable/disable emotes. Waiting for FlexibleConfig's release for that.
@RegisterSystem(RegisterMode.CLIENT)
public class EmoteSystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(EmoteSystem.class);
    private static final String EMOTE_SCREEN = "EmoteSystem:EmoteScreen";
    private static final String EMOTE_CONFIG = "EmoteSystem:EmoteConfig";

    @In
    PrefabManager prefabManager;
    @In
    NUIManager nuiManager;

    /**
     * A map of prefab's name and EmoteComponent, used to manage emotes by its prefab's name.
     */
    private Map<String, EmoteComponent> loadedEmotes = new HashMap<>();

    /**
     * This method is called when all required modules are loaded (during the world loading).
     * It searches for all prefabs containing {@link EmoteComponent} and puts the components into a HashMap of prefab's name and corresponding component.
     */
    @Override
    public void preBegin() {
        Collection<Prefab> emotePrefabs = prefabManager.listPrefabs(EmoteComponent.class);
        for (Prefab prefab : emotePrefabs) {
            EmoteComponent emote = prefab.getComponent(EmoteComponent.class);
            loadedEmotes.put(prefab.getName(), emote);
            logger.info("Registered emote ID: {} | message: {}", prefab.getName(), emote.message);
        }
    }

    /**
     * Receives {@link EmoteSelectedEvent} called by {@link EmoteScreen} to send a chat message.
     * @param event called by {@link EmoteScreen}
     * @param entity of the client.
     */
    @ReceiveEvent(components = ClientComponent.class)
    public void showEmote(EmoteSelectedEvent event, EntityRef entity) {
        entity.send(new ChatMessageEvent(loadedEmotes.get(event.getEmoteID()).message, entity.getComponent(ClientComponent.class).clientInfo));
    }

    /**
     * Receives {@link EmoteScreenButton} event to open emote selection screen.
     * @param event called on button press.
     * @param entity of the client.
     */
    @ReceiveEvent(components = ClientComponent.class)
    public void onEmoteScreenButton(EmoteScreenButton event, EntityRef entity) {
        switch (event.getState()) {
            case DOWN:
                nuiManager.toggleScreen(EMOTE_SCREEN);
                EmoteScreen emoteScreen = (EmoteScreen)nuiManager.getScreen(EMOTE_SCREEN);
                //push the map of emotes used to create the screen
                emoteScreen.setEmotes(loadedEmotes);
                //push the client entity to the screen to call the EmoteSelectedEvent from it.
                emoteScreen.setEntity(entity);
                break;
            case UP:
                nuiManager.closeScreen(EMOTE_SCREEN);
                break;
        }
    }

    /**
     * Receives {@link EmoteConfigButton} event to open configuration screen (currently just a dummy).
     * @param event called on button press.
     * @param entity of the client.
     */
    @ReceiveEvent(components = ClientComponent.class)
    public void onEmoteConfigButton(EmoteConfigButton event, EntityRef entity) {
        if (event.getState() != ButtonState.DOWN) {
            return;
        }
        nuiManager.toggleScreen(EMOTE_CONFIG);
    }
}
