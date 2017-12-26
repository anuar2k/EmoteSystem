package org.terasology.emoteSystem.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.emoteSystem.component.EmoteComponent;
import org.terasology.emoteSystem.event.EmoteSelectedEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.UIRadialRing;
import org.terasology.rendering.nui.widgets.UIRadialSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmoteScreen extends CoreScreenLayer {
    private static final Logger logger = LoggerFactory.getLogger(EmoteScreen.class);
    private List<String> idList = new ArrayList<>();

    private UIRadialRing ring;
    private EntityRef entity;

    @Override
    public void initialise() {
        ring = find("ring", UIRadialRing.class);
    }

    @Override
    public boolean isModal() {
        return false;
    }

    @Override
    public void onClosed() {
        int selectedTab = ring.getSelectedTab();
        if (selectedTab >= 0) {
            entity.send(new EmoteSelectedEvent(idList.get(ring.getSelectedTab())));
        }
    }

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

    public void setEntity(EntityRef entity) {
        this.entity = entity;
    }
}
