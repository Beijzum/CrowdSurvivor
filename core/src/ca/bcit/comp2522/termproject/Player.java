package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player extends Entity implements InputProcessor {
    final private static double DEFAULT_DEFENSE = 0.0;
    final private static int DEFAULT_MAX_HEALTH = 100;
    final private static int DEFAULT_SPEED = 200;
    final private static double DEFAULT_ATTACK_SPEED = 1.5;
    final private static double DEFAULT_CRIT_RATE = 0.1;
    final private static double DEFAULT_CRIT_MULTIPLIER = 1.5;
    final private static int DEFAULT_ULTIMATE_CD = 5;
    final private static double DEFAULT_HEALTH_REGEN_MULTIPLIER = 0.01;
    final private static int DEFAULT_ATTACK = 20;
    final private static double BASE_IFRAME_LENGTH = 2.5;
    final private static double HEALTH_REGEN_TICK_TIME = 1.5;
    private int collectedCurrency;
    private static Player instance = null;
    private double attackSpeed;
    private double critRate;
    private double critMultiplier;
    private float ultimateCDTimer;
    private float attackTimer;
    private int ultimateCD;
    private double iFramesLength;
    private float iFramesTimer;
    private float healthRegenTimer;
    private boolean iFrameIsOn = false;
    private double healthRegenMultiplier;
    private Projectile projectileTemplate;
    private float mousePositionX;
    private float mousePositionY;


    private Player() {
        resetStats();
        Sprite projectileSprite = new Sprite(new Texture(Gdx.files.internal("projectiles/tempSlash.png")));
        this.projectileTemplate = new Projectile(projectileSprite, 500, 3, 150);
        int spriteX = 100, spriteY = 100;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("tempPlayerSprite.png")));
        this.sprite.setSize(spriteX, spriteY);
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

    public void setProjectileTemplate(Projectile projectile) {
        this.projectileTemplate = projectile;
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

    public int getCollectedCurrency() {
        return this.collectedCurrency;
    }

    public double getiFramesLength() {
        return iFramesLength;
    }

    public void setiFramesLength(double newLength) {
        this.iFramesLength = newLength;
    }

    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
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
        this.iFramesLength = BASE_IFRAME_LENGTH;
        this.iFrameIsOn= false;
        this.collectedCurrency = 0;
        this.iFramesTimer = 0;
        this.attackTimer = 0;
        this.healthRegenTimer = 0;
        this.ultimateCDTimer = 0;
    }

    public void resetPosition() {
        this.sprite.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }

    public void handleUltimateCD() {
        if (this.ultimateIsReady()) {
            return;
        }
        waitForCD();
    }

    public void handleAttack(LinkedList<Projectile> playerProjectiles, float mouseX, float mouseY) {
        // check if first in linked list is expired
        if (playerProjectiles.peek() != null && playerProjectiles.peek().isOverLifeTime()) {
            playerProjectiles.removeFirst();
        }

        // move not expired projectiles
        for (Projectile projectile : playerProjectiles) {
            projectile.incrementLifetimeTimer();
            projectile.moveProjectile();
        }

        // runs every attackSpeed seconds
        if (this.attackTimer >= this.attackSpeed) {
            Projectile newProjectile = new Projectile(projectileTemplate);

            newProjectile.getDirectionVector()
                    .set(mouseX - this.getCenterX(), mouseY - this.getCenterY());

            float angle = newProjectile.getDirectionVector().angleDeg();
            newProjectile.setProjectileCenter(this.getCenterX(), this.getCenterY());
            newProjectile.setSpriteRotation(angle);

            playerProjectiles.add(newProjectile);
            this.attackTimer = 0;
        } else {
            this.attackTimer += Gdx.graphics.getDeltaTime();
        }
    }

    public void regenHealth() {
        if (this.health == this.maxHealth) {
            return;
        }
        if (this.healthRegenTimer >= HEALTH_REGEN_TICK_TIME) {
            int newHealth = this.health + (int) Math.round(this.maxHealth * healthRegenMultiplier);
            this.health = Math.min(newHealth, this.maxHealth);
            this.healthRegenTimer = 0;
        } else {
            this.healthRegenTimer += Gdx.graphics.getDeltaTime();
        }
    }

    private void waitForCD() {
        ultimateCDTimer += Gdx.graphics.getDeltaTime();
    }

    private boolean ultimateIsReady() {
        return ultimateCDTimer >= ultimateCD;
    }

    private void resetUltimateTimer() {
        this.ultimateCDTimer = 0;
    }

    public void takeDamage(Rectangle enemyHitbox, int enemyAttack) {
        if (this.sprite.getBoundingRectangle().overlaps(enemyHitbox) && !this.iFrameIsOn) {
            System.out.println(this.health);
            this.health -= (int) Math.round(enemyAttack * (1 - this.defense));
            this.iFrameIsOn = true;
            return;
        }
        if (!this.iFrameIsOn) {
            return;
        }
        if (this.iFramesTimer >= this.iFramesLength) {
            this.iFrameIsOn = false;
            this.iFramesTimer = 0;
            return;
        }
        this.iFramesTimer += Gdx.graphics.getDeltaTime();
    }

    private void turnOffIFrames() {
        this.iFrameIsOn = false;
    }

    @Override
    public String toString() {
        return "Player{" +
                "collectedCurrency=" + collectedCurrency +
                ", attackSpeed=" + attackSpeed +
                ", critRate=" + critRate +
                ", critMultiplier=" + critMultiplier +
                ", ultimateCDTimer=" + ultimateCDTimer +
                ", attackTimer=" + attackTimer +
                ", ultimateCD=" + ultimateCD +
                ", healthRegenMultiplier=" + healthRegenMultiplier +
                ", projectileTemplate=" + projectileTemplate +
                ", mousePositionX=" + mousePositionX +
                ", mousePositionY=" + mousePositionY +
                '}';
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.F) {
            if (ultimateCDTimer > ultimateCD) {
                // some ultimate effect here
                System.out.println("Ultimate Used!!!!");
                resetUltimateTimer();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        this.mousePositionX = Gdx.input.getX();
        // mousePositionY is flipped from the rest of the coordinates system in libgdx
        this.mousePositionY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        this.mousePositionX = Gdx.input.getX();
        this.mousePositionY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return true;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    private void incrementCollectedCurrency(int currency) {
        this.collectedCurrency += currency;
    }
}
