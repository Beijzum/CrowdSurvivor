package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.enemies.Boss;
import ca.bcit.comp2522.termproject.enemies.Enemy;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
    private final Sound playerProjectileSFX;
    private final Sound playerDamageSFX;
    private final Sound playerLevelUpSFX;
    private float deltaX = 0f;
    private float deltaY = 0f;

    private PlayerManager(final InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.playerProjectileSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/playerProjectileSFX.mp3"));
        this.playerDamageSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/playerDamageSFX.mp3"));
        this.playerLevelUpSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/playerLevelUpSFX.mp3"));
    }

    /**
     * Retrieves the player level up SFX.
     *
     * @return the player level up SFX.
     */
    public Sound getPlayerLevelUpSFX() {
        return this.playerLevelUpSFX;
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
        boolean damaged = false;
        for (Enemy enemy : this.gameScreen.getOnFieldEnemies()) {
            if (this.gameScreen.getPlayer().takeDamage(enemy.getHitbox(), enemy.getAttack())) {
                damaged = true;
            }
        }
        for (Boss boss : this.gameScreen.getOnFieldBosses()) {
            if (this.gameScreen.getPlayer().takeDamage(boss.getHitbox(), boss.getAttack())) {
                damaged = true;
            }
        }
        for (Projectile projectile : this.gameScreen.getEnemyProjectilesOnScreen()) {
            final int projectileDamage = 10;
            if (this.gameScreen.getPlayer().takeDamage(projectile.getHitbox(), projectileDamage)) {
                damaged = true;
            }
        }
        for (Projectile projectile : this.gameScreen.getBossProjectilesOnScreen()) {
            final int projectileDamage = 50;
            if (this.gameScreen.getPlayer().takeDamage(projectile.getHitbox(), projectileDamage)) {
                damaged = true;
            }
        }
        if (damaged) {
            final float damageSFX = 0.2f;
            playerDamageSFX.play(damageSFX);
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
     * Handles the map boundary by resetting player's x, y values when their hitbox is out of bounds.
     */
    public void handleContinuousPlayerKeyboardInput() {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.deltaY = this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.deltaY = -this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.deltaX = -this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.deltaX = this.gameScreen.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
        }

        // normalize vector
        if (this.deltaX != 0 && this.deltaY != 0) {
            this.deltaX = this.deltaX * (float) Math.abs(Math.cos(Math.atan(this.deltaY / this.deltaX)));
            this.deltaY = this.deltaY * (float) Math.abs(Math.sin(Math.atan(this.deltaY / this.deltaX)));
        }

        // handle map boundary;
        handleMapBoundary();
    }

    private void handleMapBoundary() {
        this.gameScreen.getPlayer().setX(this.gameScreen.getPlayer().getX() + this.deltaX);
        if (!this.gameScreen.getBackground().getBoundingRectangle().contains(this.gameScreen.getPlayer().getHitbox())) {
            this.gameScreen.getPlayer().setX(this.gameScreen.getPlayer().getX() - this.deltaX);
        }
        this.gameScreen.getPlayer().setY(this.gameScreen.getPlayer().getY() + this.deltaY);
        if (!this.gameScreen.getBackground().getBoundingRectangle().contains(this.gameScreen.getPlayer().getHitbox())) {
            this.gameScreen.getPlayer().setY(this.gameScreen.getPlayer().getY() - this.deltaY);
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
            final int projectileSpinSpeed = 20;
            projectile.spinProjectile(projectileSpinSpeed);
        }

        // fire projectile
        this.mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mousePosition = this.gameScreen.getCamera().unproject(this.mouseVector);
        final float projectileSFXValue = 0.5f;
        boolean fired = this.gameScreen.getPlayer()
                .fireProjectile(this.gameScreen.getPlayerProjectilesOnScreen(), mousePosition.x, mousePosition.y);
        if (fired) {
            this.playerProjectileSFX.play(projectileSFXValue);
        }
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
