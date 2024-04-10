package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import ca.bcit.comp2522.termproject.DamageNumber;
import ca.bcit.comp2522.termproject.Entity;
import ca.bcit.comp2522.termproject.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.Random;

/**
 * Represents the Enemy class.
 * Encapsulates the behavior and attributes of Enemy, including health, speed, attack, and sprite representation.
 * Inherits from the Entity class.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class Enemy extends Entity {
    private static final float DEFAULT_DEFENSE = 0.0f;
    private static final float DAMAGE_TINT_TIME = 0.6f;
    private static final int BASE_CURRENCY_DROP_AMOUNT = 2;
    private static final int CURRENCY_CALCULATION_DIVISOR = 50;
    private final Color damageTint = new Color(1, 0, 0, 1);
    private final LinkedList<DamageNumber> activeDamageNumbers = new LinkedList<>();
    private float tintTimer = 0;
    private boolean isTakingDamage;
    private final Vector2 directionVector = new Vector2();
    private final LinkedList<Projectile> hitByProjectileList = new LinkedList<>();
    private final Random randomNumberGenerator = new Random();
    private final Sprite sprite;

    /**
     * Constructs an Enemy object with custom parameters.
     *
     * @param health      int representing the health points of the enemy.
     * @param speed       int representing the speed of the enemy.
     * @param attack      int representing the attack points of the enemy.
     * @param enemySprite Sprite object representing the Enemy sprite.
     */
    public Enemy(final int health, final int speed, final int attack, final Sprite enemySprite) {
        this.maxHealth = health;
        this.health = this.maxHealth;
        this.speed = speed;
        this.attack = attack;
        this.defense = DEFAULT_DEFENSE;
        this.sprite = enemySprite;
        final int spriteSize = 100;
        this.sprite.setSize(spriteSize, spriteSize);
    }

    /**
     * Retrieves the sprite of the enemy.
     *
     * @return the sprite of the enemy.
     */
    public Sprite getSprite() {
        return this.sprite;
    }

    /**
     * Retrieves the center x-coordinate of the enemy.
     *
     * @return the center x-coordinate of the enemy.
     */
    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    /**
     * Retrieves the center y-coordinate of the enemy.
     *
     * @return the center y-coordinate of the enemy.
     */
    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    /**
     * Retrieves the direction vector of the enemy.
     *
     * @return the direction vector of the enemy.
     */
    public Vector2 getDirectionVector() {
        return this.directionVector;
    }

    /**
     * Retrieves the experience points dropped by the enemy upon defeat.
     *
     * @return the experience points dropped by the enemy.
     */
    public int getDropEXP() {
        final int expHealthMultiplier = 3;
        final int expDropDivisor = 4;
        return this.randomNumberGenerator.nextInt(this.maxHealth / 2,
                this.maxHealth * expHealthMultiplier / expDropDivisor);
    }

    /**
     * Retrieves the currency dropped by the enemy upon defeat.
     *
     * @return the currency dropped by the enemy.
     */
    public int getDropCurrency() {
        return randomNumberGenerator
                .nextInt(this.maxHealth / CURRENCY_CALCULATION_DIVISOR) + BASE_CURRENCY_DROP_AMOUNT;
    }

    /**
     * Retrieves the hitbox rectangle of the enemy.
     *
     * @return the hitbox rectangle of the enemy.
     */
    public Rectangle getHitbox() {
        return this.sprite.getBoundingRectangle();
    }

    /**
     * Sets the center position of the enemy sprite.
     *
     * @param x float representing the x-coordinate of the center position.
     * @param y float representing the y-coordinate of the center position.
     */
    public void setCenterPosition(final float x, final float y) {
        this.sprite.setCenter(x, y);
    }

    /**
     * Moves the enemy based on its direction vector.
     */
    public void move() {
        float angle = this.directionVector.angleRad();

        // vector already normalized
        float deltaX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }

    /**
     * Calculates the direction vector towards a specified position.
     *
     * @param x float representing the x-coordinate of the target position.
     * @param y float representing the y-coordinate of the target position.
     */
    public void calculateDirectionVector(final float x, final float y) {
        this.directionVector.set(x, y);
    }

    /**
     * Draws the enemy sprite using the provided Batch.
     * Changes the sprite to the colour red for a moment when enemy takes damage.
     *
     * @param batch Batch object used for rendering sprites.
     */
    public void draw(final Batch batch) {
        batch.begin();
        if (this.isTakingDamage) {
            Color originalColor = new Color(batch.getColor());
            if (originalColor.equals(CrowdSurvivor.getStandardColour())) {
                batch.setColor(this.damageTint);
            } else {
                final float redValue = 0.3f;
                batch.setColor(originalColor.r + redValue, originalColor.g, originalColor.b, originalColor.a);
            }
            batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                    this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                    this.sprite.getScaleY(), this.sprite.getRotation());
            batch.setColor(originalColor);
            batch.end();
            return;
        }
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
        batch.end();
    }

    /**
     * Draws the enemy's health bar using the provided ShapeRenderer.
     *
     * @param shapeRenderer ShapeRenderer object used to draw shapes.
     */
    public void drawHPBar(final ShapeRenderer shapeRenderer) {
        final float hpBarWidth = this.sprite.getWidth();
        final float hpBarHeight = 5;
        final float hpBarX = this.sprite.getX();
        final float hpBarY = this.sprite.getY() + this.sprite.getHeight() + hpBarHeight;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hpBarX, hpBarY, hpBarWidth, hpBarHeight);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(hpBarX, hpBarY, hpBarWidth * ((float) this.health / this.maxHealth), hpBarHeight);
    }

    /**
     * Draws the damage numbers caused to the enemy.
     *
     * @param batch the batch used for rendering.
     */
    public void drawDamageNumbers(final Batch batch) {
        batch.begin();
        for (DamageNumber damageNumber : this.activeDamageNumbers) {
            damageNumber.draw((SpriteBatch) batch, CrowdSurvivor.getFont());
        }
        batch.end();
    }

    /**
     * Updates the active damage numbers.
     *
     * @param deltaTime the delta time since the last frame.
     */
    public void updateDamageNumbers(final float deltaTime) {
        for (DamageNumber number : this.activeDamageNumbers) {
            number.update(deltaTime);
        }
        if (!this.activeDamageNumbers.isEmpty() && this.activeDamageNumbers.peek().isExpired()) {
            this.activeDamageNumbers.removeFirst();
        }
    }

    /**
     * Inflicts damage on the enemy.
     *
     * @param projectile Projectile object representing the player's projectile.
     * @param damage     int representing the amount of damage inflicted.
     * @param isCritical boolean indicating if the damage is critical.
     */
    public void takeDamage(final Projectile projectile, final int damage, final boolean isCritical) {
        if (!this.hitByProjectileList.contains(projectile)) {
            this.isTakingDamage = true;
            this.hitByProjectileList.add(projectile);
            this.health -= Math.round(damage * (1 - this.defense));

            // adds damage number
            DamageNumber damageNumber = new DamageNumber(this.getCenterX(), this.getCenterY(), damage, isCritical);
            this.activeDamageNumbers.add(damageNumber);
        }
        removeFromHitByProjectileList();
    }


    /**
     * Increments the damage tint timer.
     */
    public void incrementDamageTintTimer() {
        if (!this.isTakingDamage) {
            return;
        }
        if (this.tintTimer > DAMAGE_TINT_TIME) {
            this.tintTimer = 0;
            isTakingDamage = false;
        } else {
            this.tintTimer += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Clears the list of projectiles hitting the enemy.
     */
    public void clearHitByProjectileList() {
        this.hitByProjectileList.clear();
    }

    /**
     * Removes expired projectiles from the hit by projectiles list.
     */
    public void removeFromHitByProjectileList() {
        if (this.hitByProjectileList.peek() != null && this.hitByProjectileList.peek().isOverLifeTime()) {
            this.hitByProjectileList.removeFirst();
        }
    }

    /**
     * Returns a string representation of the Enemy object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "Enemy{"
                + "damageTint=" + this.damageTint
                + ", activeDamageNumbers=" + this.activeDamageNumbers
                + ", tintTimer=" + this.tintTimer
                + ", isTakingDamage=" + this.isTakingDamage
                + ", directionVector=" + this.directionVector
                + ", hitByProjectileList=" + this.hitByProjectileList
                + ", randomNumberGenerator=" + this.randomNumberGenerator
                + ", sprite=" + this.sprite
                + '}';
    }
}
