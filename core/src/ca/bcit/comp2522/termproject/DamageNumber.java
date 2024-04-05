package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DamageNumber {
    private final float originalX;
    private final float originalY;
    final private int DAMAGE;
    private float X;
    private float Y;
    final private float LIFETIME;
    private float timer;


    public DamageNumber(float x, float y, int damage) {
        this.X = x;
        this.Y = y;
        this.DAMAGE = damage;
        this.LIFETIME = 1.0f;
        this.timer = 0.0f;
        this.originalX = x;
        this.originalY = y;
    }

    public void update(float deltaTime) {
        timer += deltaTime;

        float ARC_PERIOD = 1f;
        float ARC_HEIGHT = 20f;

        float xOffset = ARC_HEIGHT * timer;
        float yOffset = ARC_HEIGHT * 4 * timer * (1 - timer / ARC_PERIOD);

        this.X = originalX + xOffset;
        this.Y = originalY + yOffset;
    }

    public boolean isExpired() {
        return timer >= LIFETIME;
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        font.getData().setScale(2);
        font.draw(batch, String.valueOf(this.DAMAGE), this.X, this.Y);
        font.getData().setScale(1);
    }
}
