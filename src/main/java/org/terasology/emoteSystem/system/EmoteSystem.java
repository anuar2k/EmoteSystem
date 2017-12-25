package org.terasology.emoteSystem.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.emoteSystem.bind.EmoteConfigButton;
import org.terasology.emoteSystem.component.EmoteComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.input.Keyboard;
import org.terasology.input.events.KeyEvent;
import org.terasology.logic.chat.ChatMessageEvent;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.utilities.random.FastRandom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RegisterSystem(RegisterMode.CLIENT)
public class EmoteSystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(EmoteSystem.class);
    private static final String UI_EMOTE_SCREEN = "EmoteSystem:EmoteScreen";
    private static final String UI_EMOTE_CONFIG = "EmoteSystem:EmoteConfig";

    @In
    PrefabManager prefabManager;
    @In
    NUIManager nuiManager;

    private Map<String, EmoteComponent> loadedEmotes = new HashMap<>();

    //TODO: Will use FlexibleConfigs when they will be available.
    /*
    private Map<String, Boolean> emoteSettings = new HashMap<>();
    */

    @Override
    public void preBegin() {
        Collection<Prefab> emotePrefabs = prefabManager.listPrefabs(EmoteComponent.class);
        for (Prefab prefab : emotePrefabs) {
            EmoteComponent emote = prefab.getComponent(EmoteComponent.class);
            loadedEmotes.put(prefab.getName(), emote);
            logger.info("Registered emote ID: {} | message: {}", prefab.getName(), emote.message);
        }
        //loadSettingsFile(); - //TODO: Will use FlexibleConfigs when they will be available.
    }

    //TODO: Toggled screen steals KeyEvent for its NUIKeyEvent, so it's not possible to retrieve EmoteScreenButton.
    @ReceiveEvent(components = ClientComponent.class)
    public void onEmoteScreenButton(KeyEvent event, EntityRef entity) {
        if (event.getKey() == Keyboard.Key.C) {
            switch (event.getState()) {
                case DOWN:
                    nuiManager.toggleScreen(UI_EMOTE_SCREEN);
                    break;
                case UP:
                    int random = new FastRandom().nextInt();
                    random = (random < 0) ? (random+Integer.MAX_VALUE) % loadedEmotes.size() : random % loadedEmotes.size();

                    int entryNumber = 0;
                    for (Map.Entry<String, EmoteComponent> entry : loadedEmotes.entrySet()) {
                        if (entryNumber == random) {
                            EmoteComponent emote = entry.getValue();
                            entity.send(new ChatMessageEvent(emote.message, entity.getComponent(ClientComponent.class).clientInfo));
                            break;
                        }
                        entryNumber++;
                    }

                    nuiManager.closeScreen(UI_EMOTE_SCREEN);
                    break;
                case REPEAT:
                    break;
            }
        }
    }

    @ReceiveEvent(components = ClientComponent.class)
    public void onEmoteConfigButton(EmoteConfigButton event, EntityRef entity) {
        if (event.getState() != ButtonState.DOWN) {
            return;
        }

        nuiManager.toggleScreen(UI_EMOTE_CONFIG);
    }

    //TODO: Allow user to open up configuration screen and activate/deactivate emotes. Not possible at the time as key classes needed for this system are disallowed by ModuleClassLoader; waiting for FlexibleConfigs release.
    /*
    private void loadSettingsFile() {
        try {
            String settingsStr = new String(Files.readAllBytes(getPath()));
            JsonObject settingsObj = new JsonParser().parse(settingsStr).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : settingsObj.entrySet()) {
                emoteSettings.put(entry.getKey(), entry.getValue().getAsBoolean());
            }

            for (String key: loadedEmotes.keySet()) {
                emoteSettings.putIfAbsent(key, true);
            }

            logger.info("loaded file!");
            saveSettingsFile();
        } catch (Exception e) {
            logger.info("load failed");

            emoteSettings = new HashMap<>();

            for (String key: loadedEmotes.keySet()) {
                emoteSettings.put(key, true);
            }

            saveSettingsFile();
        }
    }

    private void saveSettingsFile() {
        try {
            JsonObject settingsObj = new JsonObject();

            for (Map.Entry<String, Boolean> entry : emoteSettings.entrySet()) {
                settingsObj.addProperty(entry.getKey(), entry.getValue());
            }

            String settingsStr = settingsObj.toString();
            Files.write(getPath(), settingsStr.getBytes());
            logger.info("saved file!");
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    private Path getPath() {
        return PathManager.getInstance().getHomePath().resolve("emoteSettings.json");
    }
    */
}
