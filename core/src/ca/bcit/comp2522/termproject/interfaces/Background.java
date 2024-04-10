package ca.bcit.comp2522.termproject.interfaces;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Represents the interface for rendering background images with and without color filters.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public interface Background {

    /**
     * Renders the background sprite without any color filter.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     * @param background    Sprite object representing the background image.
     */
    default void renderBackground(CrowdSurvivor crowdSurvivor, Sprite background) {
        crowdSurvivor.getBatch().disableBlending();
        crowdSurvivor.getBatch().begin();
        crowdSurvivor.getBatch().draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(), background.getWidth(), background.getHeight(), background.getScaleX(),
                background.getScaleY(), background.getRotation());
        crowdSurvivor.getBatch().end();
        crowdSurvivor.getBatch().enableBlending();
    }

    /**
     * Renders the background sprite with a specified color filter.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     * @param background    Sprite object representing the background image.
     * @param filter        Color object representing the filter color to apply.
     */
    default void renderBackgroundWithFilter(CrowdSurvivor crowdSurvivor, Sprite background, Color filter) {
        crowdSurvivor.getBatch().setColor(filter);
        crowdSurvivor.getBatch().begin();
        crowdSurvivor.getBatch().draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(), background.getWidth(), background.getHeight(), background.getScaleX(),
                background.getScaleY(), background.getRotation());
        crowdSurvivor.getBatch().end();
        crowdSurvivor.getBatch().setColor(CrowdSurvivor.getStandardColour());
    }
}
