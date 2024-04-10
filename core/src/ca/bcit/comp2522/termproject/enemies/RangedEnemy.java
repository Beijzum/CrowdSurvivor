package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

public class RangedEnemy extends Enemy {
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

    public static RangedEnemy createBasic(final float timeElapsed, final Sprite sprite,
                                          final Sprite projectileSprite) {
        final int health = Math.round(BASIC_BASE_HEALTH + timeElapsed / 5);
        final int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, BASIC_ATTACK, BASIC_PROJECTILE_SPEED,
                BASIC_ATTACK_SPEED, projectileSprite, sprite);
    }

    public static RangedEnemy createSpewer(final float timeElapsed, final Sprite sprite,
                                           final Sprite projectileSprite) {
        final int health = Math.round(SPEWER_BASE_HEALTH + timeElapsed / 5);
        final int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, SPEWER_ATTACK, SPEWER_PROJECTILE_SPEED,
                SPEWER_ATTACK_SPEED, projectileSprite, sprite);
    }

    public static RangedEnemy createSniper(final float timeElapsed, final Sprite sprite,
                                           final Sprite projectileSprite) {
        final int health = Math.round(SNIPER_BASE_HEALTH + timeElapsed / 5);
        final int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, SNIPER_ATTACK, SNIPER_PROJECTILE_SPEED,
                SNIPER_ATTACK_SPEED, projectileSprite, sprite);
    }

    private RangedEnemy(final int health, final int speed, final int attack, final int projectileSpeed,
                        final float attackSpeed, final Sprite projectileSprite, final Sprite sprite) {
        super(health, speed, attack, sprite);
        this.projectileTemplate =
                new Projectile(projectileSprite, projectileSpeed, PROJECTILE_LIFETIME, PROJECTILE_SIZE);
        this.attackSpeed = attackSpeed;
        this.attackTimer = 0;
    }

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
}
