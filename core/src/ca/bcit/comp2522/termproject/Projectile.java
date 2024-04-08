package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents a projectile fired by an entity.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class Projectile {
    private final Vector2 directionVector;
    private int speed;
    private int lifetime;
    private Sprite sprite;
    private float lifetimeTimer = 0;

    /**
     * Constructs a projectile with custom parameters.
     *
     * @param sprite   Sprite object representing the projectile.
     * @param speed    int representing speed of the projectile.
     * @param lifetime int representing the lifetime of the projectile.
     * @param size     int representing the size of the projectile sprite.
     */
    public Projectile(final Sprite sprite, final int speed, final int lifetime, final int size) {
        this.sprite = sprite;
        this.speed = speed;
        this.lifetime = lifetime;
        this.directionVector = new Vector2();
        this.sprite.setSize(size, size);
        this.sprite.setOriginCenter();
    }

    /**
     * Constructs a projectile using another projectile as a template.
     *
     * @param projectileTemplate projectile object representing the projectile to copy from.
     */
    public Projectile(final Projectile projectileTemplate) {
        this.speed = projectileTemplate.getSpeed();
        this.lifetime = projectileTemplate.getLifetime();
        this.lifetimeTimer = 0;
        this.directionVector = new Vector2(projectileTemplate.getDirectionVector());
        this.sprite = new Sprite(projectileTemplate.getSprite());
        this.sprite.setSize(projectileTemplate.getSprite().getWidth(), projectileTemplate.getSprite().getHeight());
    }

    /**
     * Retrieves the speed of the projectile.
     *
     * @return the speed of the projectile.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Retrieves the sprite of the projectile.
     *
     * @return the sprite of the projectile.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Retrieves the hitbox of the projectile as a rectangle.
     *
     * @return the hitbox of the projectile as a Rectangle.
     */
    public Rectangle getHitbox() {
        return this.sprite.getBoundingRectangle();
    }

    /**
     * Retrieves the direction vector of the projectile.
     *
     * @return the direction vector of the projectile.
     */
    public Vector2 getDirectionVector() {
        return this.directionVector;
    }

    /**
     * Retrieves the lifetime of the projectile.
     *
     * @return the lifetime of the projectile.
     */
    public int getLifetime() {
        return lifetime;
    }

    /**
     * Sets the speed of the projectile.
     *
     * @param speed int representing the speed to set.
     */
    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    /**
     * Sets the sprite of the projectile.
     *
     * @param sprite Sprite object representing the sprite to set.
     */
    public void setSprite(final Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Sets the lifetime of the projectile.
     *
     * @param lifetime int representing the lifetime to set.
     */
    public void setLifetime(final int lifetime) {
        this.lifetime = lifetime;
    }

    /**
     * Sets the x and y coordinates for the origin of the sprite.
     *
     * @param x float representing the x-coordinate of the sprite's origin.
     * @param y float representing the y-coordinate of the sprite's origin.
     */
    public void setSpriteOrigin(final float x, final float y) {
        this.sprite.setOrigin(x, y);
    }

    /**
     * Sets the center of the projectile sprite.
     *
     * @param x float representing the x-coordinate of the sprite's center.
     * @param y float representing the y-coordinate of the sprite's center.
     */
    public void setProjectileCenter(final float x, final float y) {
        this.sprite.setCenter(x, y);
    }

    /**
     * Sets the rotation angle of the sprite.
     *
     * @param angleDegrees float representing the sprite's rotation angle in degrees.
     */
    public void setSpriteRotation(final float angleDegrees) {
        this.sprite.setRotation(angleDegrees);
    }

    /**
     * Checks if the projectile has exceeded its lifetime.
     *
     * @return true if the projectile has exceeded its lifetime, otherwise false.
     */
    public boolean isOverLifeTime() {
        return this.lifetimeTimer >= this.lifetime;
    }

    /**
     * Increments the lifetime timer of the projectile based on the elapsed time.
     */
    public void incrementLifetimeTimer() {
        this.lifetimeTimer += Gdx.graphics.getDeltaTime();
    }

    /**
     * Draws the projectile using the provided batch.
     *
     * @param batch Batch object used to render the projectile.
     */
    public void draw(final Batch batch) {
        batch.draw(this.sprite, this.sprite.getX(), this.sprite.getY(), this.sprite.getOriginX(),
                this.sprite.getOriginY(), this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getScaleX(),
                this.sprite.getScaleY(), this.sprite.getRotation());
    }

    /**
     * Moves the projectile based on its direction and speed.
     * The movement is calculated using the projectile's direction vector and speed and the elapsed time since
     * the last frame (delta time).
     * The method updates the position of the projectile's center accordingly.
     */
    public void moveProjectile() {
        float angle = this.directionVector.angleRad();

        // vector already normalized
        float deltaX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.setProjectileCenter(this.getCenterX() + deltaX, this.getCenterY() + deltaY);
    }

    /**
     * Rotates the projectile sprite based on its speed.
     * The rotation angle is determined by dividing the projectile's speed by a constant divisor.
     */
    public void spinProjectile() {
        final float constantDivisor = 100f;
        this.sprite.rotate(this.speed / constantDivisor);
    }

    private float getCenterX() {
        return this.sprite.getX() + this.sprite.getWidth() / 2;
    }

    private float getCenterY() {
        return this.sprite.getY() + this.sprite.getHeight() / 2;
    }

    /**
     * Returns a string representation of the Projectile object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "Projectile{"
                + "speed=" + speed
                + ", lifetime=" + lifetime
                + ", sprite=" + sprite
                + ", lifetimeTimer=" + lifetimeTimer
                + ", directionVector=" + directionVector
                + '}';
    }
}
