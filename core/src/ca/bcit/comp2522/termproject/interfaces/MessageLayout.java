package ca.bcit.comp2522.termproject.interfaces;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Represents an interface for creating, drawing, and manipulating message layouts.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public interface MessageLayout {
    /**
     * Creates a GlyphLayout object with the provided message and scale.
     *
     * @param message string representing the message to be displayed.
     * @param scale   float representing the scale factor for the font.
     * @return the created GlyphLayout object.
     */
    default GlyphLayout createLayout(String message, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        GlyphLayout newLayout = new GlyphLayout(CrowdSurvivor.getFont(), message);
        CrowdSurvivor.getFont().getData().setScale(1);
        return newLayout;
    }

    /**
     * Draws multiple text messages from the center with the provided GlyphLayouts, Batch, position, and scale.
     *
     * @param layouts GlyphLayout array objects to be drawn.
     * @param batch   Batch object used for rendering.
     * @param x       float representing the x-coordinate of the center point.
     * @param y       float representing the y-coordinate of the center point.
     * @param scale   float representing the scale factor for the font.
     */
    default void drawMultipleMessageFromCenter(GlyphLayout[] layouts, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        batch.begin();
        for (int i = 0; i < layouts.length; i++) {
            final float messageHeightMultiplier = 1.5f;
            CrowdSurvivor.getFont().draw(batch, layouts[i], x - layouts[i].width / 2,
                    y - layouts[i].height - layouts[i].height * i * messageHeightMultiplier);
        }
        batch.end();
        CrowdSurvivor.getFont().getData().setScale(1);
    }

    /**
     * Sets the text and scale for the provided GlyphLayout object.
     *
     * @param layout  GlyphLayout array objects to set the text for.
     * @param message string representing the message to be set.
     * @param scale   float representing the scale factor for the font.
     */
    default void setTextAndScale(GlyphLayout layout, String message, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        layout.setText(CrowdSurvivor.getFont(), message);
        CrowdSurvivor.getFont().getData().setScale(1);
    }

    /**
     * Draws a single text message with the provided GlyphLayout, Batch, position, and scale.
     *
     * @param layout GlyphLayout array objects to be drawn.
     * @param batch  Batch object used for rendering.
     * @param x      float representing the x-coordinate of the drawing position.
     * @param y      float representing the y-coordinate of the drawing position.
     * @param scale  float representing the scale factor for the font.
     */
    default void drawMessage(GlyphLayout layout, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        batch.begin();
        CrowdSurvivor.getFont().draw(batch, layout, x, y);
        batch.end();
        CrowdSurvivor.getFont().getData().setScale(1);
    }

    /**
     * Draws a single text message from the center with the provided GlyphLayout, Batch, position, and scale.
     *
     * @param layout GlyphLayout array objects to be drawn.
     * @param batch  Batch object used for rendering.
     * @param x      float representing the x-coordinate of the center point.
     * @param y      float representing the y-coordinate of the center point.
     * @param scale  float representing the scale factor for the font.
     */
    default void drawMessageFromCenter(GlyphLayout layout, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        batch.begin();
        CrowdSurvivor.getFont().draw(batch, layout, x - layout.width / 2, y - layout.height / 2);
        batch.end();
        CrowdSurvivor.getFont().getData().setScale(1);
    }
}
