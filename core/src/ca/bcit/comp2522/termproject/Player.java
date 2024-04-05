package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.LinkedList;

public class Player extends Entity {
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
    final private static int BASE_LEVEL_UP_THRESHOLD = 50;
    final private static int BASE_EXP_MULTIPLIER = 1;
    final private static int BASE_CURRENCY_MULTIPLIER = 1;
    private int levelUpThreshold;
    private int accumulatedEXP;
    private int level;
    private int currentEXP;
    private float EXPMultiplier;
    private int collectedCurrency;
    private float currencyMultiplier;
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

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setCritMultiplier(double critMultiplier) {
        this.critMultiplier = critMultiplier;
    }

    public void setCritRate(double critRate) {
        this.critRate = critRate;
    }

    public void setHealthRegenMultiplier(double healthRegenMultiplier) {
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

    public double getIFramesLength() {
        return iFramesLength;
    }

    public void setIFramesLength(double newLength) {
        this.iFramesLength = newLength;
    }

    public int getCurrentEXP() {
        return this.currentEXP;
    }

    public int getAccumulatedEXP() {
        return this.accumulatedEXP;
    }

    public int getLevelUpThreshold() {
        return this.levelUpThreshold;
    }

    public int getCollectedCurrency() {
        return this.collectedCurrency;
    }

    public int getLevel() {
        return this.level;
    }

    public void setX(float x) {
        this.sprite.setX(x);
    }

    public void setY(float y) {
        this.sprite.setY(y);
    }

    public float getX() {
        return this.sprite.getX();
    }

    public float getY() {
        return this.sprite.getY();
    }

    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    public float getEXPMultiplier() {
        return this.EXPMultiplier;
    }

    public void setEXPMultiplier(float EXPMultiplier) {
        this.EXPMultiplier = EXPMultiplier;
    }

    public float getCurrencyMultiplier() {
        return this.currencyMultiplier;
    }

    public void setCurrencyMultiplier(float currencyMultiplier) {
        this.currencyMultiplier = currencyMultiplier;
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
        this.EXPMultiplier = BASE_EXP_MULTIPLIER;
        this.currencyMultiplier = BASE_CURRENCY_MULTIPLIER;
        this.iFrameIsOn = false;
        this.levelUpThreshold = BASE_LEVEL_UP_THRESHOLD;
        this.accumulatedEXP = 0;
        this.currentEXP = 0;
        this.level = 1;
        this.collectedCurrency = 0;
        this.iFramesTimer = 0;
        this.attackTimer = 0;
        this.healthRegenTimer = 0;
        this.ultimateCDTimer = 0;
    }

    public void resetPosition() {
        this.sprite.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void fireProjectile(LinkedList<Projectile> playerProjectiles, float mouseX, float mouseY) {
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

    public void waitForCD() {
        ultimateCDTimer += Gdx.graphics.getDeltaTime();
    }

    public boolean ultimateIsReady() {
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

    public void useUltimate() {
        if (ultimateCDTimer >= ultimateCD) {
            // some ultimate effect here
            System.out.println("BIG ULTIMATE HERE");
            resetUltimateTimer();
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "levelUpThreshold=" + levelUpThreshold +
                ", accumulatedEXP=" + accumulatedEXP +
                ", level=" + level +
                ", currentEXP=" + currentEXP +
                ", EXPMultiplier=" + EXPMultiplier +
                ", collectedCurrency=" + collectedCurrency +
                ", currencyMultiplier=" + currencyMultiplier +
                ", attackSpeed=" + attackSpeed +
                ", critRate=" + critRate +
                ", critMultiplier=" + critMultiplier +
                ", ultimateCDTimer=" + ultimateCDTimer +
                ", attackTimer=" + attackTimer +
                ", ultimateCD=" + ultimateCD +
                ", iFramesLength=" + iFramesLength +
                ", iFramesTimer=" + iFramesTimer +
                ", healthRegenTimer=" + healthRegenTimer +
                ", iFrameIsOn=" + iFrameIsOn +
                ", healthRegenMultiplier=" + healthRegenMultiplier +
                ", projectileTemplate=" + projectileTemplate +
                '}';
    }

    // returns the amount of levels gained from adding exp to player
    public int addEXP(int EXP) {
        this.accumulatedEXP += EXP;
        this.currentEXP += EXP * EXPMultiplier;
        if (this.currentEXP >= this.levelUpThreshold) {
            int leveledAmount = calculateLeveledAmount();
            this.level += leveledAmount;
            this.currentEXP %= this.levelUpThreshold;
            this.levelUpThreshold += this.levelUpThreshold / 4 * leveledAmount;
            return leveledAmount;
        }
        return 0;
    }

    public int getMaxHP() {
        return this.maxHealth;
    }

    public int getCurrentHP() {
        return this.health;
    }

    public void addCollectedCurrency(int currency) {
        this.collectedCurrency += currency * currencyMultiplier;
    }

    private int calculateLeveledAmount() {
        return this.currentEXP / this.levelUpThreshold;
    }

    public void setMaxHP(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrentHP() {
        this.health = MathUtils.clamp(this.health, 0, this.maxHealth);
    }
}
