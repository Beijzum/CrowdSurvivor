package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Represents the health points (HP) bar for the player HUD.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class HPBar {
    private float x;
    private float y;
    private final float width;
    private final float height;
    private int maxHP;
    private int currentHP;
    private final Color backgroundColor = Color.RED;
    private final Color foregroundColor = Color.GREEN;

    /**
     * Constructs an HP bar with custom parameters.
     *
     * @param x      float representing the x-axis.
     * @param y      float representing the y-axis.
     * @param width  float representing the width.
     * @param height float representing the height.
     * @param maxHP  int representing the player's maximum HP.
     */
    public HPBar(final float x, final float y, final float width, final float height, final int maxHP) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
    }

    private enum Colour {
        RED(Color.RED),
        GREEN(Color.GREEN);

        private final Color colourValue;

        Colour(final Color colourValue) {
            this.colourValue = colourValue;
        }

        private Color getColourValue() {
            return this.colourValue;
        }
    }

    /**
     * Retrieves the x-coordinate of the HP bar.
     *
     * @return the x-coordinate of the HP bar.
     */
    public float getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the HP bar.
     *
     * @return the y-coordinate of the HP bar.
     */
    public float getY() {
        return y;
    }

    /**
     * Retrieves the width of the HP bar.
     *
     * @return the width of the HP bar.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Retrieves the height of the HP bar.
     *
     * @return the height of the HP bar.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Sets the position of the HP bar.
     *
     * @param posX float representing the x-axis position.
     * @param posY float representing the y-axis position.
     */
    public void setPosition(final float posX, final float posY) {
        this.x = posX;
        this.y = posY;
    }

    /**
     * Sets the current HP of the player.
     * The current HP is clamped between 0 and the maxHP.
     *
     * @param currentHP int representing the player's current HP.
     */
    public void setCurrentHP(final int currentHP) {
        this.currentHP = MathUtils.clamp(currentHP, 0, this.maxHP);
    }

    /**
     * Sets the maximum HP of the player.
     *
     * @param maxHP int representing the player's maximum HP.
     */
    public void setMaxHP(final int maxHP) {
        this.maxHP = maxHP;
    }

    /**
     * Draws the HP bar using the provided ShapeRenderer and Batch.
     * This method draws the background, foreground colours, and the current/max HP text.
     *
     * @param shapeRenderer ShapeRenderer object used to draw shapes.
     * @param batch         Batch object used to draw text.
     */
    public void draw(final ShapeRenderer shapeRenderer, final Batch batch) {
        // draw background
        shapeRenderer.setColor(Colour.RED.getColourValue());
        shapeRenderer.rect(this.x, this.y, this.width, this.height);

        // draw foreground (HP)
        float foregroundWidth = (float) this.currentHP / this.maxHP * this.width;
        shapeRenderer.setColor(Colour.GREEN.getColourValue());
        shapeRenderer.rect(this.x, this.y, foregroundWidth, this.height);

        // draw HP current/max text
        batch.begin();
        final float textX = this.x + 310;
        final float textY = this.y + this.getHeight() / 2;
        final float textHPScale = 0.5f;
        CrowdSurvivor.getFont().getData().setScale(textHPScale);
        CrowdSurvivor.getFont().draw(batch, this.currentHP + "/" + this.maxHP, textX, textY);
        CrowdSurvivor.getFont().getData().setScale(1);
        batch.end();
    }

    /**
     * Returns a string representation of the HPBar object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "HPBar{"
                + "x=" + this.x
                + ", y=" + this.y
                + ", width=" + this.width
                + ", height=" + this.height
                + ", maxHP=" + this.maxHP
                + ", currentHP=" + this.currentHP
                + ", backgroundColor=" + this.backgroundColor
                + ", foregroundColor=" + this.foregroundColor
                + '}';
    }
}
