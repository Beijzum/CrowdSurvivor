package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public interface MessageLayout {
    default GlyphLayout createLayout(String message, float scale) {
        CrowdSurvivor.font.getData().setScale(scale);
        GlyphLayout newLayout = new GlyphLayout(CrowdSurvivor.font, message);
        CrowdSurvivor.font.getData().setScale(1);
        return newLayout;
    }

    default void drawMultipleMessageFromCenter(GlyphLayout[] layouts, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.font.getData().setScale(scale);
        batch.begin();
        for (int i = 0; i < layouts.length; i++) {
            System.out.println(layouts[i].width);
            System.out.println(layouts[i].glyphCount);
            CrowdSurvivor.font.draw(batch, layouts[i], x - layouts[i].width / 2,
                    y - layouts[i].height - layouts[i].height * i * 1.5f);
        }
        batch.end();
        CrowdSurvivor.font.getData().setScale(1);
    }

    default void drawMessageFromCenter(GlyphLayout layout, Batch batch, float x, float y) {
        batch.begin();
        CrowdSurvivor.font.draw(batch, layout, x - layout.width / 2, y - layout.height / 2);
        batch.end();
    }

    default void drawMessageFromCenter(GlyphLayout layout, Batch batch, float x, float y, float scale) {
        CrowdSurvivor.font.getData().setScale(scale);
        batch.begin();
        CrowdSurvivor.font.draw(batch, layout, x - layout.width / 2, y - layout.height / 2);
        batch.end();
        CrowdSurvivor.font.getData().setScale(1);
    }
}
