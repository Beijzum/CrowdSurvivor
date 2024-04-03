package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Background {
    default void renderBackground(CrowdSurvivor game, Sprite background) {
        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(), background.getWidth(), background.getHeight(), background.getScaleX(),
                background.getScaleY(), background.getRotation());
        game.batch.end();
        game.batch.enableBlending();
    }
}
