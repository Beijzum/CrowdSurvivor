package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private int speed;
    private int lifetime;
    private Sprite sprite;
    private float lifetimeTimer = 0;

    public Projectile(Sprite sprite, int speed, int lifetime) {
        this.sprite = sprite;
        this.speed = speed;
        this.lifetime = lifetime;
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public void setX(float x) {
        this.sprite.setX(x);
    }

    public void setY(float y) {
        this.sprite.setY(y);
    }

    public void setSpriteOrigin(float x, float y) {
        this.sprite.setOrigin(x, y);
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

    public Projectile copyProjectile() {
        return new Projectile(this.sprite, this.speed, this.lifetime);
    }

    public void draw(Batch batch) {
        batch.begin();
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
        batch.end();
    }

    public void moveProjectile(float x, float y) {

    }
}
