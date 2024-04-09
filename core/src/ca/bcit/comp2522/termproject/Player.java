package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.LinkedList;

/**
 * Represents the Player class.
 * Inherits from the Entity class.
 * Implements the singleton method design pattern.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public final class Player extends Entity {
    private static Player instance = null;
    private static final float DEFAULT_DEFENSE = 0.0f;
    private static final int DEFAULT_MAX_HEALTH = 100;
    private static final int DEFAULT_SPEED = 200;
    private static final float DEFAULT_ATTACK_SPEED = 1.5f;
    private static final float DEFAULT_CRIT_RATE = 0.1f;
    private static final float DEFAULT_CRIT_MULTIPLIER = 1.5f;
    private static final float DEFAULT_HEALTH_REGEN_MULTIPLIER = 0.05f;
    private static final int DEFAULT_ATTACK = 20;
    private static final float BASE_IFRAME_LENGTH = 1.5f;
    private static final float HEALTH_REGEN_TICK_TIME = 1.5f;
    private static final int BASE_LEVEL_UP_THRESHOLD = 50;
    private static final int BASE_EXP_MULTIPLIER = 1;
    private static final int BASE_CURRENCY_MULTIPLIER = 1;
    private static final Texture[] PLAYER_FRAMES = new Texture[]{
            new Texture(Gdx.files.internal("player/playerSpriteFrame0.png")),
            new Texture(Gdx.files.internal("player/playerSpriteFrame1.png")),
            new Texture(Gdx.files.internal("player/playerSpriteFrame2.png")),
            new Texture(Gdx.files.internal("player/playerSpriteFrame3.png"))
    };
    private int levelUpThreshold;
    private int accumulatedEXP;
    private int level;
    private int currentEXP;
    private float expMultiplier;
    private int collectedCurrency;
    private float currencyMultiplier;
    private float attackSpeed;
    private float critRate;
    private float critMultiplier;
    private float attackTimer;
    private float iFramesLength;
    private float iFramesTimer;
    private float healthRegenTimer;
    private boolean iFrameIsOn = false;
    private float healthRegenMultiplier;
    private Projectile projectileTemplate;
    private boolean isMoving = false;
    private boolean isFacingLeft = true;

    private Player() {
        resetStats();
        Sprite projectileSprite = new Sprite(new Texture(Gdx.files.internal("projectiles/playerProjectile.png")));
        final int speedValue = 500;
        final int lifetimeValue = 3;
        final int sizeValue = 150;
        this.projectileTemplate = new Projectile(projectileSprite, speedValue, lifetimeValue, sizeValue);
        final int framesPerSecond = 20;
        this.sprite = new AnimatedSprite(PLAYER_FRAMES, framesPerSecond);
        final int spriteSize = 100;
        this.sprite.setSize(spriteSize, spriteSize);
    }

    /**
     * Creates a single instance of the Player object.
     *
     * @return the created or loaded PlayerManager instance.
     */
    public static Player createPlayer() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    /**
     * Retrieves the player's attack speed.
     *
     * @return the player's attack speed.
     */
    public float getAttackSpeed() {
        return this.attackSpeed;
    }

    /**
     * Retrieves the player's critical hit multiplier.
     *
     * @return the player's critical hit multiplier.
     */
    public float getCritMultiplier() {
        return this.critMultiplier;
    }

    /**
     * Retrieves the player's critical hit rate.
     *
     * @return the player's critical hit rate.
     */
    public float getCritRate() {
        return this.critRate;
    }

    /**
     * Retrieves the player's health regeneration multiplier.
     *
     * @return the player's health regeneration multiplier.
     */
    public float getHealthRegenMultiplier() {
        return this.healthRegenMultiplier;
    }

    /**
     * Retrieves the player's invincibility frame (IFrame) length.
     *
     * @return the player's IFrame length.
     */
    public float getIFramesLength() {
        return this.iFramesLength;
    }

    /**
     * Retrieves the player's current experience points (EXP).
     *
     * @return the player's EXP.
     */
    public int getCurrentEXP() {
        return this.currentEXP;
    }

    /**
     * Retrieves the player's accumulated EXP.
     *
     * @return the player's accumulated EXP.
     */
    public int getAccumulatedEXP() {
        return this.accumulatedEXP;
    }

    /**
     * Retrieves the player's level up threshold.
     *
     * @return the player's level up threshold.
     */
    public int getLevelUpThreshold() {
        return this.levelUpThreshold;
    }

    /**
     * Retrieves the player's collected currency.
     *
     * @return the player's collected currency.
     */
    public int getCollectedCurrency() {
        return this.collectedCurrency;
    }

    /**
     * Retrieves the player's level.
     *
     * @return the player's level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Retrieves the player's x-coordinate position.
     *
     * @return the player's x-coordinate position.
     */
    public float getX() {
        return this.sprite.getX();
    }

    /**
     * Gets the player's y-coordinate position.
     *
     * @return the player's y-coordinate position.
     */
    public float getY() {
        return this.sprite.getY();
    }

    /**
     * Retrieves the player's center x-coordinate position.
     *
     * @return the player's center x-coordinate position.
     */
    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    /**
     * Retrieves the player's center y-coordinate position.
     *
     * @return the player's center y-coordinate position.
     */
    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    /**
     * Retrieves the experience multiplier.
     *
     * @return the experience multiplier.
     */
    public float getEXPMultiplier() {
        return this.expMultiplier;
    }

    /**
     * Retrieves the currency multiplier.
     *
     * @return the currency multiplier.
     */
    public float getCurrencyMultiplier() {
        return this.currencyMultiplier;
    }

    /**
     * Retrieves the player's maximum health points.
     *
     * @return the player's maximum health points.
     */
    public int getMaxHP() {
        return this.maxHealth;
    }

    /**
     * Retrieves the player's current health points.
     *
     * @return the player's current health points.
     */
    public int getCurrentHP() {
        return this.health;
    }

    /**
     * Sets the player's attack speed.
     *
     * @param attackSpeed float representing the new attack speed value.
     */
    public void setAttackSpeed(final float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    /**
     * Sets the player's critical hit multiplier.
     *
     * @param critMultiplier float representing the new critical hit multiplier value.
     */
    public void setCritMultiplier(final float critMultiplier) {
        this.critMultiplier = critMultiplier;
    }

    /**
     * Sets the player's critical hit rate.
     *
     * @param critRate float representing the new critical hit rate value.
     */
    public void setCritRate(final float critRate) {
        this.critRate = critRate;
    }

    /**
     * Sets the player's health regeneration multiplier.
     *
     * @param healthRegenMultiplier float representing the new health regeneration multiplier value.
     */
    public void setHealthRegenMultiplier(final float healthRegenMultiplier) {
        this.healthRegenMultiplier = healthRegenMultiplier;
    }

    /**
     * Sets the player's projectile template.
     *
     * @param projectile Projectile object used as the new projectile template.
     */
    public void setProjectileTemplate(final Projectile projectile) {
        this.projectileTemplate = projectile;
    }

    /**
     * Sets the player's IFrame length.
     *
     * @param newLength float representing the new length of the IFrames.
     */
    public void setIFramesLength(final float newLength) {
        this.iFramesLength = newLength;
    }

    /**
     * Sets the player's x-coordinate position.
     *
     * @param x float representing the new x-coordinate value.
     */
    public void setX(final float x) {
        this.sprite.setX(x);
    }

    /**
     * Sets the player's y-coordinate position.
     *
     * @param y float representing the new y-coordinate value.
     */
    public void setY(final float y) {
        this.sprite.setY(y);
    }

    /**
     * Sets the experience multiplier.
     *
     * @param experienceMultiplier float representing the new experience multiplier value.
     */
    public void setEXPMultiplier(final float experienceMultiplier) {
        this.expMultiplier = experienceMultiplier;
    }

    /**
     * Sets the currency multiplier.
     *
     * @param currencyMultiplier float representing the new currency multiplier value.
     */
    public void setCurrencyMultiplier(final float currencyMultiplier) {
        this.currencyMultiplier = currencyMultiplier;
    }

    /**
     * Retrieves the move state of the sprite.
     *
     * @param state boolean representing the move state of the sprite.
     */
    public void setIsMoving(final boolean state) {
        this.isMoving = state;
    }

    /**
     * Flips the sprite to the right.
     */
    public void flipRight() {
        if (this.isFacingLeft) {
            this.sprite.flip(true, false);
            this.isFacingLeft = false;
        }
    }

    /**
     * Flips the sprite to the left.
     */
    public void flipLeft() {
        if (!this.isFacingLeft) {
            this.sprite.flip(true, false);
            this.isFacingLeft = true;
        }
    }

    /**
     * Resets the player's statistics to their default values.
     */
    public void resetStats() {
        this.maxHealth = DEFAULT_MAX_HEALTH;
        this.health = DEFAULT_MAX_HEALTH;
        this.attack = DEFAULT_ATTACK;
        this.defense = DEFAULT_DEFENSE;
        this.speed = DEFAULT_SPEED;
        this.attackSpeed = DEFAULT_ATTACK_SPEED;
        this.critRate = DEFAULT_CRIT_RATE;
        this.critMultiplier = DEFAULT_CRIT_MULTIPLIER;
        this.healthRegenMultiplier = DEFAULT_HEALTH_REGEN_MULTIPLIER;
        this.iFramesLength = BASE_IFRAME_LENGTH;
        this.expMultiplier = BASE_EXP_MULTIPLIER;
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
        this.isMoving = false;
    }

    /**
     * Resets the player's position to the center of the screen.
     */
    public void resetPosition() {
        final float spriteX = 1.5f;
        final float spriteY = 1.5f;
        this.sprite.setCenter(Gdx.graphics.getWidth() * spriteX, Gdx.graphics.getHeight() * spriteY);
    }

    /**
     * Fires a projectile from the player towards a specified target position.
     * Determines the direction of the projectile based on the target position and calculates its angle for orientation.
     * Checks if the attack timer has reached the attack speed threshold to determine if a new projectile can be fired.
     * If true, creates a new projectile object, sets its direction vector towards the target position,
     * and calculates its orientation angle.
     * Adds the new projectile to the linked list of player's projectiles.
     * Resets the attack timer to 0 to start counting for the next attack.
     * If the attack timer has not reached the attack speed threshold, increments the attack timer by the elapsed time.
     *
     * @param playerProjectiles LinkedList of Projectiles that contains the list of player's projectiles.
     * @param mouseX            float representing the x-coordinate of the target position.
     * @param mouseY            float representing the y-coordinate of the target position.
     * @return boolean          returns true if projectile was successfully fired, returns false otherwise.
     */
    public boolean fireProjectile(final LinkedList<Projectile> playerProjectiles, final float mouseX,
                                  final float mouseY) {
        // runs every attackSpeed seconds
        if (this.attackTimer >= this.attackSpeed) {
            Projectile newProjectile = new Projectile(this.projectileTemplate);

            newProjectile.getDirectionVector()
                    .set(mouseX - this.getCenterX(), mouseY - this.getCenterY());

            float angle = newProjectile.getDirectionVector().angleDeg();
            newProjectile.setProjectileCenter(this.getCenterX(), this.getCenterY());
            newProjectile.setSpriteRotation(angle);

            playerProjectiles.add(newProjectile);
            this.attackTimer = 0;
            return true;
        } else {
            this.attackTimer += Gdx.graphics.getDeltaTime();
            return false;
        }
    }

    /**
     * Regenerates the player's health over time.
     * If the player's health is full, no regeneration occurs.
     */
    public void regenHealth() {
        if (this.health == this.maxHealth) {
            return;
        }
        if (this.healthRegenTimer >= HEALTH_REGEN_TICK_TIME) {
            int newHealth = this.health + Math.round(this.maxHealth * this.healthRegenMultiplier);
            this.health = Math.min(newHealth, this.maxHealth);
            this.healthRegenTimer = 0;
        } else {
            this.healthRegenTimer += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Inflicts damage on the player based on enemy attack and collision.
     * If the player is currently invincible, no damage is taken.
     *
     * @param enemyHitbox Rectangle object representing the hitbox of the enemy that caused the damage.
     * @param enemyAttack int representing the attack value of the enemy.
     * @return boolean    returns true if damage was taken, false no damage was taken.
     */
    public boolean takeDamage(final Rectangle enemyHitbox, final int enemyAttack) {
        if (this.sprite.getBoundingRectangle().overlaps(enemyHitbox) && !this.iFrameIsOn) {
            this.health -= Math.round(enemyAttack * (1 - this.defense));
            this.iFrameIsOn = true;
            return true;
        } else if (this.sprite.getBoundingRectangle().overlaps(enemyHitbox) && this.iFrameIsOn) {
            this.iFramesTimer += Gdx.graphics.getDeltaTime();
        }
        if (!this.iFrameIsOn) {
            return false;
        }
        if (this.iFramesTimer >= this.iFramesLength) {
            this.iFrameIsOn = false;
            this.iFramesTimer = 0;
        }
        return false;
    }

    /**
     * Increments the IFrame timer if the player is currently invincible.
     */
    public void incrementIFrames() {
        if (this.iFrameIsOn) {
            this.iFramesTimer += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Converts accumulated experience into player levels.
     * Updates the player's level and adjusts the experience points and level up threshold.
     *
     * @param exp int representing the amount of experience points to be added.
     * @return the number of levels gained from the added experience.
     */
    public int addEXP(final int exp) {
        this.accumulatedEXP += exp;
        this.currentEXP += (int) (exp * this.expMultiplier);
        if (this.currentEXP >= this.levelUpThreshold) {
            int leveledAmount = calculateLeveledAmount();
            this.level += leveledAmount;
            this.currentEXP %= this.levelUpThreshold;
            final int levelDivisor = 4;
            this.levelUpThreshold += this.levelUpThreshold / levelDivisor * leveledAmount;
            return leveledAmount;
        }
        return 0;
    }

    private int calculateLeveledAmount() {
        return this.currentEXP / this.levelUpThreshold;
    }

    /**
     * Increases the player's collected currency by a specified amount.
     * The currency is multiplied by the currency multiplier.
     *
     * @param currency int representing the amount of currency to be added.
     */
    public void addCollectedCurrency(final int currency) {
        this.collectedCurrency += Math.round(currency * this.currencyMultiplier);
    }

    /**
     * Returns a string representation of the Player object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "Player{"
                + "levelUpThreshold=" + this.levelUpThreshold
                + ", accumulatedEXP=" + this.accumulatedEXP
                + ", level=" + this.level
                + ", currentEXP=" + this.currentEXP
                + ", EXPMultiplier=" + this.expMultiplier
                + ", collectedCurrency=" + this.collectedCurrency
                + ", currencyMultiplier=" + this.currencyMultiplier
                + ", attackSpeed=" + this.attackSpeed
                + ", critRate=" + this.critRate
                + ", critMultiplier=" + this.critMultiplier
                + ", attackTimer=" + this.attackTimer
                + ", iFramesLength=" + this.iFramesLength
                + ", iFramesTimer=" + this.iFramesTimer
                + ", healthRegenTimer=" + this.healthRegenTimer
                + ", iFrameIsOn=" + this.iFrameIsOn
                + ", healthRegenMultiplier=" + this.healthRegenMultiplier
                + ", projectileTemplate=" + this.projectileTemplate
                + '}';
    }

    @Override
    public void draw(final Batch batch) {
        if (this.isMoving) {
            this.sprite.setPausedAnimation(false);
        } else {
            this.sprite.setPausedAnimation(true);
            this.sprite.resetFrames();
        }
        this.sprite.update();
        batch.begin();
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
        batch.end();
    }
}
