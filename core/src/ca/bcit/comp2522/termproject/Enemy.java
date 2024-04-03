package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

public class Enemy extends Entity {
    final private static double DEFAULT_DEFENSE = 0.0;
    final private static float DAMAGE_TINT_TIME = 1;
    final private Color damageTint = new Color(200, 0, 0, 1);
    private Color normalColor;
    private float tintTimer = 0;
    private float acceleration;
    private boolean isTakingDamage;
    final private Vector2 directionVector = new Vector2();
    final private LinkedList<Projectile> hitByProjectileList = new LinkedList<>();

    public Enemy() {
        // do later
    }

    public Enemy(int health, int speed, int acceleration, int attack, String imageFilePath) {
        this.maxHealth = health;
        this.health = this.maxHealth;
        this.speed = speed;
        this.acceleration = acceleration;
        this.attack = attack;
        this.defense = DEFAULT_DEFENSE;
        this.sprite = new Sprite(new Texture(Gdx.files.internal(imageFilePath)));
        this.normalColor = new Color(this.sprite.getColor());

        this.sprite.setSize(100, 100);
    }

    public float getAcceleration() {
        return this.acceleration;
    }

    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getDirectionVector() {
        return this.directionVector;
    }

    public void move() {
        float angle = this.directionVector.angleRad();

        // vector already normalized
        float deltaX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }

    public void setCenterPosition(float x, float y) {
        this.sprite.setCenter(x, y);
    }

    public void draw(Batch batch) {
        if (this.isTakingDamage) {
            batch.setColor(this.damageTint);
        }
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
        batch.setColor(this.normalColor);
    }

    public void takeDamage(Projectile projectile, int damage) {
        if (this.sprite.getBoundingRectangle().overlaps(projectile.getHitbox())
                && !this.hitByProjectileList.contains(projectile)) {
            this.isTakingDamage = true;
            this.hitByProjectileList.add(projectile);
            this.health -= (int) Math.round(damage * (1 - this.defense));
        }
        if (this.tintTimer > DAMAGE_TINT_TIME) {
            this.tintTimer = 0;
            isTakingDamage = false;
        } else if (this.isTakingDamage) {
            this.tintTimer += Gdx.graphics.getDeltaTime();
        }
        removeFromHitByProjectileList();
    }

    public void clearHitByProjectileList() {
        this.hitByProjectileList.clear();
    }

    public void removeFromHitByProjectileList() {
        if (this.hitByProjectileList.peek() != null && this.hitByProjectileList.peek().isOverLifeTime()) {
            this.hitByProjectileList.removeFirst();
        }
    }
}
