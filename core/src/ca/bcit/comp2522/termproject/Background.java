package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Background {
    default void renderBackground(CrowdSurvivor game, Sprite background) {
        background.setSize(game.viewportX, game.viewportY);

        game.batch.begin();
        game.batch.draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(), background.getWidth(), background.getHeight(), background.getScaleX(),
                background.getScaleY(), background.getRotation());
        game.batch.end();
    }
}
