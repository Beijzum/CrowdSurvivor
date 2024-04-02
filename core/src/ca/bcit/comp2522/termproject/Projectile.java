package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private int speed;
    private int lifetime;
    private Sprite sprite;
    private float lifetimeTimer = 0;
    final private Vector2 directionVector;

    public Projectile(Sprite sprite, int speed, int lifetime, int size) {
        this.sprite = sprite;
        this.speed = speed;
        this.lifetime = lifetime;
        this.directionVector = new Vector2();
        this.sprite.setSize(size, size);
        this.sprite.setOriginCenter();
    }

    public Projectile(Projectile projectileTemplate) {
        this.speed = projectileTemplate.getSpeed();
        this.lifetime = projectileTemplate.getLifetime();
        this.directionVector = new Vector2(projectileTemplate.getDirectionVector());
        this.sprite = new Sprite(projectileTemplate.getSprite());
        this.sprite.setSize(projectileTemplate.getSprite().getWidth(), projectileTemplate.getSprite().getHeight());
    }

    public int getSpeed() {
        return speed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getHitbox() {
        return this.sprite.getBoundingRectangle();
    }
    public Vector2 getDirectionVector() {
        return this.directionVector;
    }
    public int getLifetime() {
        return lifetime;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public void setSpriteOrigin(float x, float y) {
        this.sprite.setOrigin(x, y);
    }
    public void setProjectileCenter(float x, float y) {
        this.sprite.setCenter(x, y);
    }

    public void setSpriteRotation(float angleDegrees) {
        this.sprite.setRotation(angleDegrees);
    }

    public boolean isOverLifeTime() {
        if (this.lifetimeTimer >= this.lifetime) {
            return true;
        }
        return false;
    }

    public void incrementLifetimeTimer() {
        this.lifetimeTimer += Gdx.graphics.getDeltaTime();
    }

    public void draw(Batch batch) {
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
    }

    public void moveProjectile() {
        float angle = this.directionVector.angleRad();

        // vector already normalized
        float deltaX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.setProjectileCenter(this.getCenterX() + deltaX, this.getCenterY() + deltaY);
    }

    private float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    private float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    @Override
    public String toString() {
        return "Projectile{" +
                "speed=" + speed +
                ", lifetime=" + lifetime +
                ", sprite=" + sprite +
                ", lifetimeTimer=" + lifetimeTimer +
                ", directionVector=" + directionVector +
                '}';
    }
}
