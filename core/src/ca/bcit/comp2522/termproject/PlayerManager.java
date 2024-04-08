package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.enemies.Boss;
import ca.bcit.comp2522.termproject.enemies.Enemy;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

import java.util.Objects;

/**
 * Represents the PlayerManager class.
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
     * Creates a single instance of the PlayerManager object.
     *
     * @param gameScreen GameScreen object used to represent the game screen.
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
     * Regenerates the player's health using the regenHealth method from the game screen.
     * Iterates through the list of on-field enemies and inflicts damage to the player based on each enemy's attack.
     * Iterates through the list of enemy projectiles on the screen and each projectile inflicts damage to the player.
     */
    public void handlePlayerHealth() {
        this.gameScreen.getPlayer().regenHealth();
        for (Enemy enemy : this.gameScreen.getOnFieldEnemies()) {
            this.gameScreen.getPlayer().takeDamage(enemy.getHitbox(), enemy.getAttack());
        }
        for (Boss boss : this.gameScreen.getOnFieldBosses()) {
            this.gameScreen.getPlayer().takeDamage(boss.getHitbox(), boss.getAttack());
        }
        for (Projectile projectile : this.gameScreen.getEnemyProjectilesOnScreen()) {
            final int projectileDamage = 10;
            this.gameScreen.getPlayer().takeDamage(projectile.getHitbox(), projectileDamage);
        }
        for (Projectile projectile : this.gameScreen.getBossProjectilesOnScreen()) {
            final int projectileDamage = 30;
            this.gameScreen.getPlayer().takeDamage(projectile.getHitbox(), projectileDamage);
        }
    }

    /**
     * Increments the player's invincibility frames (IFrames).
     */
    public void incrementPlayerIframe() {
        this.gameScreen.getPlayer().incrementIFrames();
    }

    /**
     * Handles continuous player movement based on keyboard input.
     * Allows the player to move in four directions: up, down, left, and right.
     * The movement speed of the player is determined by the player's current speed attribute.
     * The movement is updated based on the delta time to for consistent movement across different frame rates.
     * The method calculates the movement vector based on the keys pressed and updates the player's position.
     * If multiple directional keys are pressed simultaneously, the movement vector is normalized for consistent speed.
     */
    public void handleContinuousPlayerKeyboardInput() {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            return;
        }

        float deltaX = 0;
        float deltaY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            deltaY = this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            deltaY = -this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            deltaX = -this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            deltaX = this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        // normalize vector
        if (deltaX != 0 && deltaY != 0) {
            deltaX = deltaX * (float) Math.abs(Math.cos(Math.atan(deltaY / deltaX)));
            deltaY = deltaY * (float) Math.abs(Math.sin(Math.atan(deltaY / deltaX)));
        }

        this.gameScreen.getPlayer().setX(this.gameScreen.getPlayer().getX() + deltaX);
        if (!gameScreen.getBackground().getBoundingRectangle().contains(gameScreen.getPlayer().getHitbox())) {
            this.gameScreen.getPlayer().setX(this.gameScreen.getPlayer().getX() - deltaX);
        }
        this.gameScreen.getPlayer().setY(this.gameScreen.getPlayer().getY() + deltaY);
        if (!gameScreen.getBackground().getBoundingRectangle().contains(gameScreen.getPlayer().getHitbox())) {
            this.gameScreen.getPlayer().setY(this.gameScreen.getPlayer().getY() - deltaY);
        }
    }

    /**
     * Handles the player's attack mechanism.
     * Manages the firing of projectiles and their movement and lifetime.
     * Fires a new projectile towards the current mouse position.
     * After firing, adds it to the list of active projectiles on the screen.
     * Updates the position and lifetime of active projectiles to move them and check if they are expired.
     * Removes expired projectiles from the screen to free up memory and resources.
     */
    public void handleAttack() {
        // remove expired projectiles
        if (this.gameScreen.getPlayerProjectilesOnScreen().peek() != null
                && Objects.requireNonNull(this.gameScreen.getPlayerProjectilesOnScreen().peek()).isOverLifeTime()) {
            this.gameScreen.getPlayerProjectilesOnScreen().removeFirst();
        }

        // move projectiles not expired
        for (Projectile projectile : this.gameScreen.getPlayerProjectilesOnScreen()) {
            projectile.incrementLifetimeTimer();
            projectile.moveProjectile();
        }

        // fire projectile
        this.mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mousePosition = this.gameScreen.getCamera().unproject(this.mouseVector);
        this.gameScreen.getPlayer()
                .fireProjectile(this.gameScreen.getPlayerProjectilesOnScreen(), mousePosition.x, mousePosition.y);

    }

    /**
     * Returns a string representation of the PlayerManager object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "PlayerManager{"
                + "gameScreen=" + this.gameScreen
                + ", mouseVector=" + this.mouseVector
                + '}';
    }
}
