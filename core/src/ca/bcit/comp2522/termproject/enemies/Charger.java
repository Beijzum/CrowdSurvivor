package ca.bcit.comp2522.termproject.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the Charger class, a type of Enemy that charges towards a specified direction.
 * Manages additional properties such as acceleration and charging movement.
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
public class Charger extends Enemy {
    private final int acceleration;
    private final Vector2 chargingVector = new Vector2();

    /**
     * Constructs a Charger object with custom parameters.
     *
     * @param health       int representing the health points of the charger.
     * @param acceleration int representing the acceleration value of the charger.
     * @param attack       int representing the attack points of the charger.
     * @param sprite       Sprite object representing the charger's sprite.
     */
    public Charger(final int health, final int acceleration, final int attack, final Sprite sprite) {
        super(health, 0, attack, sprite);
        this.acceleration = acceleration;
    }

    /**
     * Calculates the direction vector towards a specified position and adjusts the charging behavior.
     * Points direction vector from the Charger's current position to the specified target position.
     * Decelerates if the charging vector and the direction vector are not aligned.
     * Accelerates if the charging vector and the direction vector are aligned.
     *
     * @param x float representing the x-coordinate of the target position.
     * @param y float representing the y-coordinate of the target position.
     */
    @Override
    public void calculateDirectionVector(final float x, final float y) {
        this.getDirectionVector().set(x, y);
        final int speedValue = 600;
        if (chargingVector.dot(this.getDirectionVector()) <= 0) {

            if (Math.abs(this.speed) <= speedValue) {
                this.speed -= Math.round(acceleration * Gdx.graphics.getDeltaTime());
            }
            this.chargingVector.add(getDirectionVector());
            return;
        }
        if (Math.abs(this.speed) <= speedValue) {
            this.speed += Math.round(acceleration * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Moves the charger based on its charging vector and speed.
     * Updates the Charger's sprite position with the calculated delta x and delta y values.
     */
    @Override
    public void move() {
        float angle = this.chargingVector.angleRad();

        float deltaX = Math.abs(this.speed) * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = Math.abs(this.speed) * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.getSprite().setX(this.getSprite().getX() + deltaX);
        this.getSprite().setY(this.getSprite().getY() + deltaY);
    }
}
