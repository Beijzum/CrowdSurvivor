package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

/**
 * Represents the RangedEnemy class.
 * Extends the Enemy class and encapsulates the behavior and attributes of a ranged enemy.
 * Manages attack speed, projectile firing, and specific attack properties.
 * Inherits from the Entity class.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public final class RangedEnemy extends Enemy {
    private static final int SPEWER_PROJECTILE_SPEED = 130;
    private static final int SNIPER_PROJECTILE_SPEED = 500;
    private static final int BASIC_PROJECTILE_SPEED = 300;
    private static final float SPEWER_ATTACK_SPEED = 0.5f;
    private static final float SNIPER_ATTACK_SPEED = 2f;
    private static final float BASIC_ATTACK_SPEED = 1f;
    private static final int PROJECTILE_LIFETIME = 15;
    private static final int PROJECTILE_SIZE = 50;
    private static final int SPEWER_BASE_HEALTH = 50;
    private static final int BASIC_BASE_HEALTH = 125;
    private static final int SNIPER_BASE_HEALTH = 75;
    private static final int SPEWER_ATTACK = 10;
    private static final int BASIC_ATTACK = 10;
    private static final int SNIPER_ATTACK = 20;
    private static final int BASE_SPEED = 75;
    private final float attackSpeed;
    private float attackTimer;
    private final Projectile projectileTemplate;

    private RangedEnemy(final int health, final int speed, final int attack, final int projectileSpeed,
                        final float attackSpeed, final Sprite projectileSprite, final Sprite sprite) {
        super(health, speed, attack, sprite);
        this.projectileTemplate =
                new Projectile(projectileSprite, projectileSpeed, PROJECTILE_LIFETIME, PROJECTILE_SIZE);
        this.attackSpeed = attackSpeed;
        this.attackTimer = 0;
    }

    /**
     * Creates a basic RangedEnemy with increasing health and speed over time.
     *
     * @param timeElapsed      float representing the time elapsed in the game.
     * @param sprite           Sprite object representing the enemy's sprite.
     * @param projectileSprite Sprite object representing the projectile's sprite.
     * @return a new basic RangedEnemy object.
     */
    public static RangedEnemy createBasic(final float timeElapsed, final Sprite sprite,
                                          final Sprite projectileSprite) {
        final int health = Math.round(BASIC_BASE_HEALTH + timeElapsed / 5);
        final int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, BASIC_ATTACK, BASIC_PROJECTILE_SPEED,
                BASIC_ATTACK_SPEED, projectileSprite, sprite);
    }

    /**
     * Creates a spewer RangedEnemy with increasing health and speed over time.
     *
     * @param timeElapsed      float representing the time elapsed in the game.
     * @param sprite           Sprite object representing the enemy's sprite.
     * @param projectileSprite Sprite object representing the projectile's sprite.
     * @return a new spewer RangedEnemy object.
     */
    public static RangedEnemy createSpewer(final float timeElapsed, final Sprite sprite,
                                           final Sprite projectileSprite) {
        final int health = Math.round(SPEWER_BASE_HEALTH + timeElapsed / 5);
        final int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, SPEWER_ATTACK, SPEWER_PROJECTILE_SPEED,
                SPEWER_ATTACK_SPEED, projectileSprite, sprite);
    }

    /**
     * Creates a sniper RangedEnemy with increasing health and speed over time.
     *
     * @param timeElapsed      float representing the time elapsed in the game.
     * @param sprite           Sprite object representing the enemy's sprite.
     * @param projectileSprite Sprite object representing the projectile's sprite.
     * @return a new sniper RangedEnemy object.
     */
    public static RangedEnemy createSniper(final float timeElapsed, final Sprite sprite,
                                           final Sprite projectileSprite) {
        final int health = Math.round(SNIPER_BASE_HEALTH + timeElapsed / 5);
        final int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, SNIPER_ATTACK, SNIPER_PROJECTILE_SPEED,
                SNIPER_ATTACK_SPEED, projectileSprite, sprite);
    }

    /**
     * Fires a projectile towards the player's position.
     *
     * @param enemyProjectiles LinkedList of Projectile objects representing enemy projectiles.
     * @param playerCoordX     float representing the x-coordinate of the player's position.
     * @param playerCoordY     float representing the y-coordinate of the player's position.
     */
    public void fireProjectile(final LinkedList<Projectile> enemyProjectiles, final float playerCoordX,
                               final float playerCoordY) {
        if (this.attackTimer >= this.attackSpeed) {
            Projectile newProjectile = new Projectile(this.projectileTemplate);
            newProjectile.getDirectionVector().set(playerCoordX - this.getCenterX(), playerCoordY - this.getCenterY());
            newProjectile.setProjectileCenter(this.getCenterX(), this.getCenterY());
            enemyProjectiles.add(newProjectile);

            this.attackTimer = 0;
        } else {
            this.attackTimer += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Returns a string representation of the RangedEnemy object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "RangedEnemy{"
                + "attackSpeed=" + this.attackSpeed
                + ", attackTimer=" + this.attackTimer
                + ", projectileTemplate=" + this.projectileTemplate
                + '}';
    }
}
