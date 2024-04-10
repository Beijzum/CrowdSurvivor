package ca.bcit.comp2522.termproject.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Charger extends Enemy {
    private final int acceleration;
    private final Vector2 chargingVector = new Vector2();

    public Charger(final int health, final int acceleration, final int attack, final Sprite sprite) {
        super(health, 0, attack, sprite);
        this.acceleration = acceleration;
    }

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

    @Override
    public void move() {
        float angle = this.chargingVector.angleRad();

        float deltaX = Math.abs(this.speed) * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = Math.abs(this.speed) * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.getSprite().setX(this.getSprite().getX() + deltaX);
        this.getSprite().setY(this.getSprite().getY() + deltaY);
    }
}
