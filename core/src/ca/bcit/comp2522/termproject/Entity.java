package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;

abstract public class Entity {
    private int health;
    private int maxHealth;
    private int speed;
    private int attack;
    private int defense;
    protected Sprite sprite;
    protected Rectangle hitbox;


    public int getAttack() {
        return attack;
    }

    public int getDefense() {
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

    abstract public void move(int x, int y);
}
