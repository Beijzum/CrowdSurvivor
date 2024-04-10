package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

/**
 * Represents the Boss class, a stronger variant of the basic Enemy.
 * Extends the Enemy class and encapsulates the behavior and attributes of a Boss enemy.
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
public class Boss extends Enemy {
    private final Projectile projectileTemplate;
    private float attackTimer = 0;

    /**
     * Constructs a Boss object with custom parameters.
     *
     * @param health           int representing the health points of the boss.
     * @param speed            int representing the speed of the boss.
     * @param attack           int representing the attack points of the boss.
     * @param size             int representing the size of the boss's sprite.
     * @param sprite           Sprite object representing the boss's sprite.
     * @param projectileSprite Sprite object representing the projectile's sprite.
     */
    public Boss(final int health, final int speed, final int attack, final int size, final Sprite sprite,
                final Sprite projectileSprite) {
        super(health, speed, attack, sprite);
        this.getSprite().setSize(size, size);
        final int projectileSpeed = 200;
        final int projectileLifetime = 15;
        final int projectileSize = 200;
        this.projectileTemplate = new Projectile(projectileSprite, projectileSpeed, projectileLifetime, projectileSize);
    }

    /**
     * Fires a projectile towards the player's position.
     *
     * @param enemyProjectiles LinkedList of Projectile objects representing enemy projectiles.
     * @param playerCoordX     float representing the x-coordinate of the player's position.
     * @param playerCoordY     float representing the y-coordinate of the player's position.
     * @return true if a projectile is fired, false otherwise.
     */
    public boolean fireProjectile(final LinkedList<Projectile> enemyProjectiles, final float playerCoordX,
                                  final float playerCoordY) {
        final float attackSpeed = 10f; // atk speed in seconds
        if (this.attackTimer >= attackSpeed) {
            Projectile newProjectile = new Projectile(this.projectileTemplate);
            newProjectile.getDirectionVector().set(playerCoordX - this.getCenterX(), playerCoordY - this.getCenterY());
            newProjectile.setProjectileCenter(this.getCenterX(), this.getCenterY());
            enemyProjectiles.add(newProjectile);

            this.attackTimer = 0;
            return true;
        } else {
            this.attackTimer += Gdx.graphics.getDeltaTime();
            return false;
        }
    }

    /**
     * Returns a string representation of the Boss object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "Boss{"
                + "projectileTemplate=" + this.projectileTemplate
                + ", attackTimer=" + this.attackTimer
                + '}';
    }
}
