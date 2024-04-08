package ca.bcit.comp2522.termproject.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Charger extends Enemy {
    final private int acceleration;
    final private Vector2 chargingVector = new Vector2();
    public Charger(int health, int acceleration, int attack, String filepath) {
        super(health, 0, attack, filepath);
        this.acceleration = acceleration;
    }

    @Override
    public void calculateDirectionVector(float x, float y) {
        this.getDirectionVector().set(x, y);
        if (chargingVector.dot(this.getDirectionVector()) <= 0) {
            if (Math.abs(this.speed) <= 600) {
                this.speed -= Math.round(acceleration * Gdx.graphics.getDeltaTime());
            }
            this.chargingVector.add(getDirectionVector());
            return;
        }
        if (Math.abs(this.speed) <= 600) {
            this.speed += Math.round(acceleration * Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void move() {
        float angle = this.chargingVector.angleRad();

        float deltaX = Math.abs(this.speed) * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = Math.abs(this.speed) * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }
}
