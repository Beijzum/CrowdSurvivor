package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.enemies.Enemy;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

/**
 * Represents the player manager class.
 * Implements the singleton method design pattern.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public final class PlayerManager {
    private static PlayerManager instance = null;
    private final InGameScreen gameScreen;
    private final Vector3 mouseVector = new Vector3(0, 0, 0);

    private PlayerManager(final InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Creates a single instance of PlayerManager.
     *
     * @param gameScreen gameScreen Object used to represent the game screen.
     * @return the created or loaded PlayerManager instance.
     */
    public static PlayerManager createPlayerManager(final InGameScreen gameScreen) {
        if (instance == null) {
            instance = new PlayerManager(gameScreen);
        }
        return instance;
    }

    /**
     * Handles the player's health regeneration and damage taken from enemies and projectiles.
     */
    public void handlePlayerHealth() {
        gameScreen.player.regenHealth();
        for (Enemy enemy : gameScreen.onFieldEnemies) {
            gameScreen.player.takeDamage(enemy.getHitbox(), enemy.getAttack());
        }
        for (Projectile projectile : gameScreen.enemyProjectilesOnScreen) {
            final int projectileDamage = 10;
            gameScreen.player.takeDamage(projectile.getHitbox(), projectileDamage);
        }
    }

    /**
     * Increments the player's invincibility frames (IFrames).
     */
    public void incrementPlayerIframe() {
        gameScreen.player.incrementIFrames();
    }

    /**
     * Handles continuous player movement based on keyboard input.
     * Allows the player to move in four directions: up, down, left, and right.
     */
    public void handleContinuousPlayerKeyboardInput() {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            return;
        }

        float deltaX = 0;
        float deltaY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            deltaY = gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            deltaY = -gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            deltaX = -gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            deltaX = gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        // normalize vector
        if (deltaX != 0 && deltaY != 0) {
            deltaX = deltaX * (float) Math.abs(Math.cos(Math.atan(deltaY / deltaX)));
            deltaY = deltaY * (float) Math.abs(Math.sin(Math.atan(deltaY / deltaX)));
        }
        gameScreen.player.setX(gameScreen.player.getX() + deltaX);
        gameScreen.player.setY(gameScreen.player.getY() + deltaY);
    }

    /**
     * Handles the player's attack mechanism.
     * Manages the firing of projectiles and their movement and lifetime.
     */
    public void handleAttack() {
        // remove expired projectiles
        if (gameScreen.playerProjectilesOnScreen.peek() != null
                && gameScreen.playerProjectilesOnScreen.peek().isOverLifeTime()) {
            gameScreen.playerProjectilesOnScreen.removeFirst();
        }

        // move projectiles not expired
        for (Projectile projectile : gameScreen.playerProjectilesOnScreen) {
            projectile.incrementLifetimeTimer();
            projectile.moveProjectile();
        }

        // fire projectile
        this.mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mousePosition = gameScreen.camera.unproject(mouseVector);
        gameScreen.player
                .fireProjectile(this.gameScreen.playerProjectilesOnScreen, mousePosition.x, mousePosition.y);

    }

    /**
     * Returns a string representation of the PlayerManager object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "PlayerManager{"
                + "gameScreen=" + gameScreen
                + ", mouseVector=" + mouseVector
                + '}';
    }
}
