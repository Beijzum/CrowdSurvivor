package ca.bcit.comp2522.termproject.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Charger extends Enemy {
    final private int acceleration;
    private float turnRadius = 0.3f;
    private float testSpeed = 100;
    final private Vector2 accelerationVector = new Vector2();
    private float turn;
    public Charger(int health, int acceleration, int attack, String filepath) {
        super(health, 0, attack, filepath);
        this.acceleration = acceleration;
    }

    public int getAcceleration() {
        return this.acceleration;
    }

    @Override
    public void calculateDirectionVector(float x, float y) {
        accelerationVector.set(x, y);
        float angle = accelerationVector.angleRad();
        accelerationVector.set(this.acceleration * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle),
                this.acceleration * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle));
        this.getDirectionVector().add(x * Gdx.graphics.getDeltaTime(), y * Gdx.graphics.getDeltaTime());
    }

    @Override
    public void move() {
        float angle = this.getDirectionVector().angleRad();
        // determine how to get magnitude from a vector that starts at 0,0 from calculate direction vector
//        float deltaX = this.acceleration * this.getDirectionVector().x * Gdx.graphics.getDeltaTime();
//        float deltaY = this.acceleration * this.getDirectionVector().y * Gdx.graphics.getDeltaTime();

        // vector already normalized
        float deltaX = this.testSpeed * Gdx.graphics.getDeltaTime() * (float) Math.cos(angle);
        float deltaY = this.testSpeed * Gdx.graphics.getDeltaTime() * (float) Math.sin(angle);
        this.sprite.setX(this.sprite.getX() + deltaX);
        this.sprite.setY(this.sprite.getY() + deltaY);
    }
}
