package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

public class Boss extends Enemy {
    final private static Sprite PROJECTILE_SPRITE = new Sprite(
            new Texture(Gdx.files.internal("projectiles/bossProjectile.png")));
    final private float attackSpeed;
    private final Projectile projectileTemplate;
    private float attackTimer = 0;
    public Boss(int health, int speed, int attack, int size, String filepath) {
        super(health, speed, attack, filepath);
        this.sprite.setSize(size, size);
        this.attackSpeed = 10f;
        this.projectileTemplate = new Projectile(PROJECTILE_SPRITE, 175, 10, 200);
    }

    public boolean fireProjectile(LinkedList<Projectile> enemyProjectiles, float playerCoordX, float playerCoordY) {
        if (this.attackTimer >= this.attackSpeed) {
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
