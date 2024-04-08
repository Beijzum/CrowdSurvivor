package ca.bcit.comp2522.termproject.interfaces;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public interface MessageLayout {
    default GlyphLayout createLayout(String message, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        GlyphLayout newLayout = new GlyphLayout(CrowdSurvivor.getFont(), message);
        CrowdSurvivor.getFont().getData().setScale(1);
        return newLayout;
    }

    default void drawMultipleMessageFromCenter(GlyphLayout[] layouts, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        batch.begin();
        for (int i = 0; i < layouts.length; i++) {
            CrowdSurvivor.getFont().draw(batch, layouts[i], x - layouts[i].width / 2,
                    y - layouts[i].height - layouts[i].height * i * 1.5f);
        }
        batch.end();
        CrowdSurvivor.getFont().getData().setScale(1);
    }

    default void setTextAndScale(GlyphLayout layout, String message, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        layout.setText(CrowdSurvivor.getFont(), message);
        CrowdSurvivor.getFont().getData().setScale(1);
    }

    default void drawMessage(GlyphLayout layout, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        batch.begin();
        CrowdSurvivor.getFont().draw(batch, layout, x, y);
        batch.end();
        CrowdSurvivor.getFont().getData().setScale(1);
    }

    default void drawMessageFromCenter(GlyphLayout layout, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.getFont().getData().setScale(scale);
        batch.begin();
        CrowdSurvivor.getFont().draw(batch, layout, x - layout.width / 2, y - layout.height / 2);
        batch.end();
        CrowdSurvivor.getFont().getData().setScale(1);
    }
}
