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
    final private static double DEFAULT_ATTACK_SPEED = 0.5; // RMBR TO CHANGE, DEFAULT = 2
    final private static double DEFAULT_CRIT_RATE = 0.1;
    final private static double DEFAULT_CRIT_MULTIPLIER = 1.5;
    final private static int DEFAULT_ULTIMATE_CD = 5;
    final private static double DEFAULT_HEALTH_REGEN_MULTIPLIER = 0.01;
    final private static int DEFAULT_ATTACK = 20;
    private int collectCurrency;
    private static Player instance = null;
    private double attackSpeed;
    private double critRate;
    private double critMultiplier;
    private float ultimateCDTimer;
    private float attackTimer;
    private int ultimateCD;
    private double healthRegenMultiplier;
    private Projectile projectileTemplate;
    private float mousePositionX;
    private float mousePositionY;

    private Player() {
        resetStats();
        Sprite projectileSprite = new Sprite(new Texture(Gdx.files.internal("projectiles/tempSlash.png")));
        this.projectileTemplate = new Projectile(projectileSprite, 500, 3);
        int spriteX = 100, spriteY = 100;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("tempPlayerSprite.png")));
        this.sprite.setSize(spriteX, spriteY);
        this.sprite.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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
    public int getCollectCurrency() {
        return this.collectCurrency;
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
        this.collectCurrency = 0;
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

    public void handleAttack(LinkedList<Projectile> playerProjectiles) {
        // check if first in linked list is expired
        if (playerProjectiles.peek() != null && playerProjectiles.peek().isOverLifeTime()) {
            playerProjectiles.removeFirst();
        }

        // move not expired projectiles
        for (Projectile projectile : playerProjectiles) {
            projectile.incrementLifetimeTimer();
            projectile.moveProjectile();
        }

        System.out.println(playerProjectiles.size());

        // runs every attackSpeed seconds
        if (this.attackTimer >= this.attackSpeed) {
            Projectile newProjectile = this.projectileTemplate.copyProjectile();

            newProjectile.getDirectionVector()
                    .set(mousePositionX - this.getCenterX(), mousePositionY - this.getCenterY());

            float angle = newProjectile.getDirectionVector().angleDeg();
            newProjectile.setProjectileCenter(this.getCenterX(), this.getCenterY());
            newProjectile.setSpriteRotation(angle);

            playerProjectiles.add(newProjectile);
            this.attackTimer = 0;
        } else {
            this.attackTimer += Gdx.graphics.getDeltaTime();
        }
    }

    // implement later enemies are done
    public void handleDamage() {

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

    @Override
    public String toString() {
        return "Player{" +
                "attackSpeed=" + attackSpeed +
                ", critRate=" + critRate +
                ", critMultiplier=" + critMultiplier +
                ", ultimateCD=" + ultimateCD +
                ", healthRegenMultiplier=" + healthRegenMultiplier +
                ", projectile=" + projectileTemplate +
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
        this.collectCurrency += currency;
    }
}
