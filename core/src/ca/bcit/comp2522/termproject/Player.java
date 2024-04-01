package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Player extends Entity {
    final private static double DEFAULT_DEFENSE = 0.0;
    final private static int DEFAULT_MAX_HEALTH = 100;
    final private static int DEFAULT_SPEED = 200;
    final private static double DEFAULT_ATTACK_SPEED = 1;
    final private static double DEFAULT_CRIT_RATE = 0.1;
    final private static double DEFAULT_CRIT_MULTIPLIER = 1.5;
    final private static int DEFAULT_ULTIMATE_CD = 45;
    final private static double DEFAULT_HEALTH_REGEN_MULTIPLIER = 0.01;
    final private static int DEFAULT_ATTACK = 20;
    private static Player instance = null;
    private double attackSpeed;
    private double critRate;
    private double critMultiplier;
    private int ultimateCD;
    private double healthRegenMultiplier;
    private Projectile projectile;

    private Player() {
        resetStats();
        this.sprite = new Sprite(new Texture(Gdx.files.internal("tempPlayerSprite.png")));
        this.sprite.setSize(100, 100);
        this.setSize(100, 100);
        this.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        this.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public static Player createPlayer() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setCritMultiplier(float critMultiplier) {
        this.critMultiplier = critMultiplier;
    }

    public void setCritRate(float critRate) {
        this.critRate = critRate;
    }

    public void setHealthRegenMultiplier(float healthRegenMultiplier) {
        this.healthRegenMultiplier = healthRegenMultiplier;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public void setUltimateCD(int ultimateCD) {
        this.ultimateCD = ultimateCD;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public double getCritMultiplier() {
        return critMultiplier;
    }

    public double getCritRate() {
        return critRate;
    }

    public double getHealthRegenMultiplier() {
        return healthRegenMultiplier;
    }

    public int getUltimateCD() {
        return ultimateCD;
    }

    public Projectile getProjectile() {
        return projectile;
    }


    public void resetStats() {
        this.maxHealth = DEFAULT_MAX_HEALTH;
        this.health = DEFAULT_MAX_HEALTH;
        this.attack = DEFAULT_ATTACK;
        this.defense = DEFAULT_DEFENSE;
        this.speed = DEFAULT_SPEED;
        this.attackSpeed = DEFAULT_ATTACK_SPEED;
        this.critRate = DEFAULT_CRIT_RATE;
        this.critMultiplier = DEFAULT_CRIT_MULTIPLIER;
        this.ultimateCD = DEFAULT_ULTIMATE_CD;
        this.healthRegenMultiplier = DEFAULT_HEALTH_REGEN_MULTIPLIER;
    }

    public void resetPosition() {

    }

    // implement later for when saving upgrades is being worked on
    public void loadPlayerUpgrades() {

    }


    // called every frame
    public void handleMovement() {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            return;
        }
        float deltaY = 0, deltaX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            deltaY = this.speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            deltaY = -this.speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            deltaX = -this.speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            deltaX = this.speed * Gdx.graphics.getDeltaTime();
        }
        // normalize vector
        if (deltaX != 0 && deltaY != 0) {
            deltaX = deltaX * (float) Math.abs(Math.cos(Math.atan(deltaY / deltaX)));
            deltaY = deltaY * (float) Math.abs(Math.sin(Math.atan(deltaY / deltaX)));
        }
        this.setX(getX() + deltaX);
        this.setY(getY() + deltaY);
    }

    @Override
    public String toString() {
        return "Player{" +
                "attackSpeed=" + attackSpeed +
                ", critRate=" + critRate +
                ", critMultiplier=" + critMultiplier +
                ", ultimateCD=" + ultimateCD +
                ", healthRegenMultiplier=" + healthRegenMultiplier +
                ", projectile=" + projectile +
                '}';
    }
}
