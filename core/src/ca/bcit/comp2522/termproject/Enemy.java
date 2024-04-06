package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Enemy extends Entity {
    final protected static float DEFAULT_DEFENSE = 0.0f;
    final protected static float DAMAGE_TINT_TIME = 0.6f;
    final protected static int BASE_CURRENCY_DROP_AMOUNT = 2;
    final private static int CURRENCY_CALCULATION_DIVISOR = 100;
    final private Color damageTint = new Color(1, 0, 0, 1);
    final private LinkedList<DamageNumber> activeDamageNumbers = new LinkedList<>();
    protected float tintTimer = 0;
    private float acceleration;
    protected boolean isTakingDamage;
    final private Vector2 directionVector = new Vector2();
    final private LinkedList<Projectile> hitByProjectileList = new LinkedList<>();
    final protected Random randomNumberGenerator = new Random();

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

    public float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    public float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getDirectionVector() {
        return this.directionVector;
    }

    public int getDropEXP() {
        return this.randomNumberGenerator.nextInt(this.maxHealth / 2, this.maxHealth * 3 / 4);
    }

    public int getDropCurrency() {
        return randomNumberGenerator
                .nextInt(this.maxHealth / CURRENCY_CALCULATION_DIVISOR) + BASE_CURRENCY_DROP_AMOUNT;
    }

    public void move() {
        float angle = this.directionVector.angleRad();

        // vector already normalized
        float deltaX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }

    public void setCenterPosition(float x, float y) {
        this.sprite.setCenter(x, y);
    }

    @Override
    public void draw(Batch batch) {
        batch.begin();
        if (this.isTakingDamage) {
            Color originalColor = new Color(batch.getColor());
            if (originalColor.equals(CrowdSurvivor.STANDARD_COLOR)) {
                batch.setColor(this.damageTint);
            } else {
                batch.setColor(originalColor.r + 0.3f, originalColor.g, originalColor.b, originalColor.a);
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

    public void drawHPBar(ShapeRenderer shapeRenderer) {
        float hpBarWidth = this.sprite.getWidth();
        float hpBarHeight = 5;
        float hpBarX = this.sprite.getX();
        float hpBarY = this.sprite.getY() + this.sprite.getHeight() + 5;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hpBarX, hpBarY, hpBarWidth, hpBarHeight);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(hpBarX, hpBarY, hpBarWidth * ((float) this.health / this.maxHealth), hpBarHeight);
    }

    public void drawDamageNumbers(Batch batch) {
        batch.begin();
        for (DamageNumber damageNumber : activeDamageNumbers) {
            damageNumber.draw((SpriteBatch) batch, CrowdSurvivor.font);
        }
        batch.end();
    }

    public void updateDamageNumbers(float deltaTime) {
        for (DamageNumber number : activeDamageNumbers) {
            number.update(deltaTime);
        }
        if (!activeDamageNumbers.isEmpty() && activeDamageNumbers.peek().isExpired()) {
            activeDamageNumbers.removeFirst();
        }
    }

    public void takeDamage(Projectile projectile, int damage, boolean isCritical) {
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
