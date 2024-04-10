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

    public Sprite getSprite() {
        return this.sprite;
    }

    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    public Vector2 getDirectionVector() {
        return this.directionVector;
    }

    public int getDropEXP() {
        final int expHealthMultiplier = 3;
        final int expDropDivisor = 4;
        return this.randomNumberGenerator.nextInt(this.maxHealth / 2,
                this.maxHealth * expHealthMultiplier / expDropDivisor);
    }

    public int getDropCurrency() {
        return randomNumberGenerator
                .nextInt(this.maxHealth / CURRENCY_CALCULATION_DIVISOR) + BASE_CURRENCY_DROP_AMOUNT;
    }

    public Rectangle getHitbox() {
        return this.sprite.getBoundingRectangle();
    }

    public void move() {
        float angle = this.directionVector.angleRad();

        // vector already normalized
        float deltaX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }

    public void calculateDirectionVector(final float x, final float y) {
        this.directionVector.set(x, y);
    }

    public void setCenterPosition(final float x, final float y) {
        this.sprite.setCenter(x, y);
    }

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

    public void drawDamageNumbers(final Batch batch) {
        batch.begin();
        for (DamageNumber damageNumber : activeDamageNumbers) {
            damageNumber.draw((SpriteBatch) batch, CrowdSurvivor.getFont());
        }
        batch.end();
    }

    public void updateDamageNumbers(final float deltaTime) {
        for (DamageNumber number : activeDamageNumbers) {
            number.update(deltaTime);
        }
        if (!activeDamageNumbers.isEmpty() && activeDamageNumbers.peek().isExpired()) {
            activeDamageNumbers.removeFirst();
        }
    }

    public void takeDamage(final Projectile projectile, final int damage, final boolean isCritical) {
        if (!this.hitByProjectileList.contains(projectile)) {
            this.isTakingDamage = true;
            this.hitByProjectileList.add(projectile);
            this.health -= Math.round(damage * (1 - this.defense));

            // adds damage number
            DamageNumber damageNumber = new DamageNumber(this.getCenterX(), this.getCenterY(), damage, isCritical);
            activeDamageNumbers.add(damageNumber);
        }
        removeFromHitByProjectileList();
    }

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

    public void clearHitByProjectileList() {
        this.hitByProjectileList.clear();
    }

    public void removeFromHitByProjectileList() {
        if (this.hitByProjectileList.peek() != null && this.hitByProjectileList.peek().isOverLifeTime()) {
            this.hitByProjectileList.removeFirst();
        }
    }
}
