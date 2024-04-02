package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends Entity {
    final private static double DEFAULT_DEFENSE = 0.0;
    private float acceleration;

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
        this.sprite.setSize(100, 100);
    }

    public float getAcceleration() {
        return this.acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void move(float x, float y) {

    }

    public void setCenterPosition(float x, float y) {
        this.sprite.setCenter(x, y);
    }

    public void draw(Batch batch) {
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
    }

    public void takeDamage(Rectangle projectileHitbox, int damage) {
        if (this.sprite.getBoundingRectangle().overlaps(projectileHitbox)) {
            this.health -= damage;
            System.out.println(this.health);
        }
    }

}
