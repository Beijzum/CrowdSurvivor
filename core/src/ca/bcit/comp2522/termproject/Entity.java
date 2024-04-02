package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;

abstract public class Entity {
    protected int health;
    protected int maxHealth;
    protected int speed;
    protected int attack;
    protected double defense;
    protected Sprite sprite;


    public int getAttack() {
        return attack;
    }

    public double getDefense() {
        return defense;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Rectangle getHitbox() {
        return this.sprite.getBoundingRectangle();
    }
    public boolean isDead() {
        if (this.health <= 0) {
            return true;
        }
        return false;
    }

    public void draw(Batch batch) {
        batch.begin();
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
        batch.end();
    }
}
