package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Represents the experience points (EXP) bar for the player HUD.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class EXPBar {
    private float x;
    private float y;
    private final float width;
    private final float height;
    private int levelThreshold;
    private int currentEXP;
    private final Color backgroundColour = Color.BLUE;
    private final Color foregroundColour = Color.CYAN;
    private int playerLevel;

    /**
     * Constructs an EXP bar with custom parameters.
     *
     * @param x              float representing the x-axis.
     * @param y              float representing the y-axis.
     * @param width          float representing the width.
     * @param height         float representing the height.
     * @param playerLevel    int representing the player's level.
     * @param levelThreshold int representing the EXP needed to level up.
     */
    public EXPBar(final float x, final float y, final float width, final float height, final int playerLevel,
                  final int levelThreshold) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.levelThreshold = levelThreshold;
        this.currentEXP = 0;
        this.playerLevel = playerLevel;
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
     * Retrieves the width of the EXP bar.
     *
     * @return the width of the EXP bar.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Retrieves the height of the EXP bar.
     *
     * @return the height of the EXP bar.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Sets the position of the EXP bar.
     *
     * @param posX float representing the x-coordinate of the position.
     * @param posY float representing the y-coordinate of the position.
     */
    public void setPosition(final float posX, final float posY) {
        this.x = posX;
        this.y = posY;
    }

    /**
     * Sets the current EXP of the player.
     * The currentEXP is clamped between 0 and the levelThreshold.
     *
     * @param currentEXP int representing the current EXP of the player.
     */
    public void setCurrentEXP(final int currentEXP) {
        this.currentEXP = MathUtils.clamp(currentEXP, 0, this.levelThreshold);
    }

    /**
     * Sets the maximum EXP required to level up.
     *
     * @param maxEXP int representing the maximum EXP required to level up.
     */
    public void setMaxEXP(final int maxEXP) {
        this.levelThreshold = maxEXP;
    }

    /**
     * Sets the current level of the player.
     *
     * @param currentLevel int representing the current level of the player.
     */
    public void setCurrentLevel(final int currentLevel) {
        this.playerLevel = currentLevel;
    }

    /**
     * Draws the EXP bar using the provided ShapeRenderer and Batch.
     * This method draws the background, foreground colours, and the player level text.
     *
     * @param shapeRenderer ShapeRenderer object used for rendering shapes.
     * @param batch         Batch object used for rendering text.
     */
    public void draw(final ShapeRenderer shapeRenderer, final Batch batch) {
        // draw background
        shapeRenderer.setColor(this.backgroundColour);
        final float rectWidth = 1.075f;
        shapeRenderer.rect(this.x, this.y, (this.width / rectWidth), this.height);

        // draw foreground (experience)
        float foregroundWidth = (float) this.currentEXP / this.levelThreshold * this.width;
        shapeRenderer.setColor(this.foregroundColour);
        shapeRenderer.rect(this.x, this.y, foregroundWidth, this.height);

        batch.begin();
        final float textX = (this.x + this.getWidth() / 1.05f);
        final float textY = this.y + this.getHeight() / 2;
        final float textEXPScale = 0.75f;
        CrowdSurvivor.getFont().getData().setScale(textEXPScale);
        CrowdSurvivor.getFont().draw(batch, "Level: " + this.playerLevel, textX, textY);
        CrowdSurvivor.getFont().getData().setScale(1);
        batch.end();
    }

    /**
     * Returns a string representation of the EXPBar object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "EXPBar{"
                + "x=" + this.x
                + ", y=" + this.y
                + ", width=" + this.width
                + ", height=" + this.height
                + ", levelThreshold=" + this.levelThreshold
                + ", currentEXP=" + this.currentEXP
                + ", backgroundColour=" + this.backgroundColour
                + ", foregroundColour=" + this.foregroundColour
                + ", playerLevel=" + this.playerLevel
                + '}';
    }
}
