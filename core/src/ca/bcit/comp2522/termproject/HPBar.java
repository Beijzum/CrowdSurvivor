package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import static ca.bcit.comp2522.termproject.CrowdSurvivor.font;

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

    /**
     * Returns the width of the HP bar.
     *
     * @return the width of the HP bar.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Returns the height of the HP bar.
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
     * @param shapeRenderer the ShapeRenderer used to draw shapes.
     * @param batch         the Batch used to draw text.
     */
    public void draw(final ShapeRenderer shapeRenderer, final Batch batch) {
        // draw background
        shapeRenderer.setColor(this.backgroundColor);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);

        // draw foreground (HP)
        float foregroundWidth = (float) this.currentHP / this.maxHP * this.width;
        shapeRenderer.setColor(this.foregroundColor);
        shapeRenderer.rect(this.x, this.y, foregroundWidth, this.height);

        // draw HP current/max text
        batch.begin();
        final float textHPX = 310;
        float textX = this.x + textHPX;
        float textY = this.y + this.getHeight() / 2;
        final float textHPScale = 0.5F;
        font.getData().setScale(textHPScale);
        font.draw(batch, currentHP + "/" + maxHP, textX, textY);
        font.getData().setScale(1);
        batch.end();
    }

}
