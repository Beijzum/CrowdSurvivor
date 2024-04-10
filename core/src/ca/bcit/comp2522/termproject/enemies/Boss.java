package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

public class Boss extends Enemy {
    private final Projectile projectileTemplate;
    private float attackTimer = 0;

    public Boss(final int health, final int speed, final int attack, final int size, final Sprite sprite,
                final Sprite projectileSprite) {
        super(health, speed, attack, sprite);
        this.getSprite().setSize(size, size);
        final int projectileSpeed = 200;
        final int projectileLifetime = 15;
        final int projectileSize = 200;
        this.projectileTemplate = new Projectile(projectileSprite, projectileSpeed, projectileLifetime, projectileSize);
    }

    public boolean fireProjectile(final LinkedList<Projectile> enemyProjectiles, final float playerCoordX,
                                  final float playerCoordY) {
        final float attackSpeed = 10f;
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
}
