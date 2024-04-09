package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

public class RangedEnemy extends Enemy {
    final private static int SPEWER_PROJECTILE_SPEED = 130;
    final private static int SNIPER_PROJECTILE_SPEED = 500;
    final private static int BASIC_PROJECTILE_SPEED = 300;
    final private static float SPEWER_ATTACK_SPEED = 0.5f;
    final private static float SNIPER_ATTACK_SPEED = 2f;
    final private static float BASIC_ATTACK_SPEED = 1f;
    final private static int PROJECTILE_LIFETIME = 15;
    final private static int PROJECTILE_SIZE = 50;
    final private static int SPEWER_BASE_HEALTH = 50;
    final private static int BASIC_BASE_HEALTH = 125;
    final private static int SNIPER_BASE_HEALTH = 75;
    final private static int SPEWER_ATACK = 10;
    final private static int BASIC_ATTACK = 10;
    final private static int SNIPER_ATTACK = 20;
    final private static int BASE_SPEED = 75;
    final private float attackSpeed;
    private float attackTimer;
    final private Projectile projectileTemplate;

    public static RangedEnemy createBasic(float timeElapsed, Sprite sprite, Sprite projectileSprite) {
        int health = Math.round(BASIC_BASE_HEALTH + timeElapsed / 5);
        int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, BASIC_ATTACK, BASIC_PROJECTILE_SPEED,
                BASIC_ATTACK_SPEED, projectileSprite, sprite);
    }

    public static RangedEnemy createSpewer(float timeElapsed, Sprite sprite, Sprite projectileSprite) {
        int health = Math.round(SPEWER_BASE_HEALTH + timeElapsed / 5);
        int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, SPEWER_ATACK, SPEWER_PROJECTILE_SPEED,
                SPEWER_ATTACK_SPEED, projectileSprite, sprite);
    }

    public static RangedEnemy createSniper(float timeElapsed, Sprite sprite, Sprite projectileSprite) {
        int health = Math.round(SNIPER_BASE_HEALTH + timeElapsed / 5);
        int speed = Math.round(BASE_SPEED + timeElapsed / 5);
        return new RangedEnemy(health, speed, SNIPER_ATTACK, SNIPER_PROJECTILE_SPEED,
                SNIPER_ATTACK_SPEED, projectileSprite, sprite);
    }

    private RangedEnemy(int health, int speed, int attack, int projectileSpeed,
                        float attackSpeed, Sprite projectileSprite, Sprite sprite) {
        super(health, speed, attack, sprite);
        this.projectileTemplate =
                new Projectile(projectileSprite, projectileSpeed, PROJECTILE_LIFETIME, PROJECTILE_SIZE);
        this.attackSpeed = attackSpeed;
        this.attackTimer = 0;
    }

    public void fireProjectile(LinkedList<Projectile> enemyProjectiles, float playerCoordX, float playerCoordY) {
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
