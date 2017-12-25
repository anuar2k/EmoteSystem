package org.terasology.emoteSystem.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.emoteSystem.bind.EmoteScreenButton;
import org.terasology.emoteSystem.component.EmoteComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RegisterSystem(RegisterMode.CLIENT)
public class EmoteSystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(EmoteSystem.class);

    @In
    PrefabManager prefabManager;

    private Map<String, EmoteComponent> loadedEmotes = new HashMap<>();

    @Override
    public void preBegin() {
        Collection<Prefab> emotePrefabs = prefabManager.listPrefabs(EmoteComponent.class);
        for (Prefab prefab : emotePrefabs) {
            EmoteComponent emote = prefab.getComponent(EmoteComponent.class);
            loadedEmotes.put(prefab.getName(), emote);
            logger.info("Loaded emote -> ID: {} | message: {}", prefab.getName(), emote.message);
        }
    }

    @ReceiveEvent
    public void onEmoteScreenButton(EmoteScreenButton event, EntityRef entity) {
        switch (event.getState()) {
            case DOWN:
                break;
            case UP:
                for (Map.Entry<String, EmoteComponent> entry : loadedEmotes.entrySet()) {
                    String key = entry.getKey();
                    EmoteComponent emote = entry.getValue();
                    logger.info("Key: {} Value: {}", key, emote.message);
                }
                break;
            case REPEAT:
                break;
            }
        event.consume();
    }

}
