package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents a damage number displayed on the screen.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class DamageNumber {
    private final float x;
    private final float y;
    private final int damage;
    private final float lifetime;
    private final boolean isCritical;
    private float newX;
    private float newY;
    private float timer;

    /**
     * Constructs a damage number with custom parameters.
     *
     * @param x          float representing the x-axis position.
     * @param y          float representing the y-axis position.
     * @param damage     int representing the damage value.
     * @param isCritical boolean indicating if the damage is a critical hit.
     */
    public DamageNumber(final float x, final float y, final int damage, final boolean isCritical) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.lifetime = 1.0f;
        this.timer = 0.0f;
        this.newX = x;
        this.newY = y;
        this.isCritical = isCritical;
    }

    public float getLifetime() {
        return this.lifetime;
    }

    /**
     * Updates the position of the damage number based on elapsed time.
     * The damage number will move in an arc pattern.
     *
     * @param deltaTime float representing the elapsed time since the last update.
     */
    public void update(final float deltaTime) {
        this.timer += deltaTime;

        final float arcHeight = 20f;
        final float arcPeriod = 1f;

        final float xOffset = arcHeight * this.timer;
        final float yOffset = arcHeight * 4 * this.timer * (1 - this.timer / arcPeriod);

        this.newX = this.x + xOffset;
        this.newY = this.y + yOffset;
    }

    /**
     * Checks if the damage number has expired.
     *
     * @return true if the damage number has expired, otherwise false.
     */
    public boolean isExpired() {
        return this.timer >= this.lifetime;
    }

    /**
     * Draws the damage number using the provided SpriteBatch and BitmapFont.
     * Critical damage numbers are displayed in yellow with an increased scale.
     *
     * @param batch SpriteBatch object used to render the damage number.
     * @param font  BitmapFont object used to display the damage value.
     */
    public void draw(final SpriteBatch batch, final BitmapFont font) {
        if (this.isCritical) {
            font.setColor(Color.YELLOW);
            final float textCritScale = 1.5f;
            font.getData().setScale(textCritScale);
            font.draw(batch, String.format("%d!", this.damage), this.newX, this.newY);
            font.getData().setScale(1);
            font.setColor(Color.WHITE);
            return;
        }
        font.draw(batch, String.valueOf(this.damage), this.newX, this.newY);
    }

    /**
     * Returns a string representation of the DamageNumber object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "DamageNumber{"
                + "x=" + this.x
                + ", y=" + this.y
                + ", damage=" + this.damage
                + ", lifetime=" + this.lifetime
                + ", isCritical=" + this.isCritical
                + ", newX=" + this.newX
                + ", newY=" + this.newY
                + ", timer=" + this.timer
                + '}';
    }
}
