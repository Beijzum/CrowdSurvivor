package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Background {
    default void renderBackground(CrowdSurvivor game, Sprite background) {
        background.setSize(game.viewportX, game.viewportY);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();
    }
}
