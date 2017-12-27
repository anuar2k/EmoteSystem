package org.terasology.emoteSystem.ui;

import org.terasology.emoteSystem.component.EmoteComponent;
import org.terasology.emoteSystem.event.EmoteSelectedEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.UIRadialRing;
import org.terasology.rendering.nui.widgets.UIRadialSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Screen used to select one of the registered emotes.
 * @author anuar2k
 */
public class EmoteScreen extends CoreScreenLayer {
    private UIRadialRing ring;

    /**
     * List used to bind together UIRadialSection's index with emote's ID.
     */
    private List<String> idList = new ArrayList<>();
    private EntityRef entity;

    @Override
    public void initialise() {
        ring = find("ring", UIRadialRing.class);
    }

    /**
     * Creates a list of UIRadialSections based on loadedEmotes map supplied by {@link org.terasology.emoteSystem.system.EmoteSystem}.
     */
    public void setEmotes(Map<String, EmoteComponent> emotesToShow) {
        List<UIRadialSection> newSections = new ArrayList<>();

        for (Map.Entry<String, EmoteComponent> entry : emotesToShow.entrySet()) {
            UIRadialSection newSection = new UIRadialSection();
            newSection.setText(entry.getValue().message);

            newSections.add(newSection);
            idList.add(entry.getKey());
        }

        ring.setSections(newSections);
    }

    /**
     * Gets the entity used to call {@link EmoteSelectedEvent} from it.
     * @param entity of the client.
     */
    public void setEntity(EntityRef entity) {
        this.entity = entity;
    }

    @Override
    public void onClosed() {
        int selectedTab = ring.getSelectedTab();
        if (selectedTab >= 0) {
            entity.send(new EmoteSelectedEvent(idList.get(ring.getSelectedTab())));
        }
    }

    @Override
    public boolean isModal() {
        return false;
    }
}
