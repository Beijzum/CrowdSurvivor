package ca.bcit.comp2522.termproject.interfaces;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Background {
    default void renderBackground(CrowdSurvivor game, Sprite background) {
        game.getBatch().disableBlending();
        game.getBatch().begin();
        game.getBatch().draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(), background.getWidth(), background.getHeight(), background.getScaleX(),
                background.getScaleY(), background.getRotation());
        game.getBatch().end();
        game.getBatch().enableBlending();
    }

    default void renderBackgroundWithFilter(CrowdSurvivor game, Sprite background, Color filter) {
        game.getBatch().setColor(filter);
        game.getBatch().begin();
        game.getBatch().draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(), background.getWidth(), background.getHeight(), background.getScaleX(),
                background.getScaleY(), background.getRotation());
        game.getBatch().end();
        game.getBatch().setColor(CrowdSurvivor.STANDARD_COLOR);
    }
}
