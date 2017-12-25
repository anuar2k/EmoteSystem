package org.terasology.emoteSystem.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.events.NUIKeyEvent;

public class EmoteScreen extends CoreScreenLayer {

    @In
    private static Logger logger = LoggerFactory.getLogger(EmoteScreen.class);

    @Override
    public void initialise() {
    }

    @Override
    public boolean canBeFocus() {
        return false;
    }

    @Override
    public boolean onKeyEvent(NUIKeyEvent event) {
        logger.info(String.valueOf(event.getKey()));

        if (super.onKeyEvent(event)) {
            return true;
        } else {
            return false;
        }
    }
}
