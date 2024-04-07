package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import static ca.bcit.comp2522.termproject.CrowdSurvivor.font;

public class EXPBar {
    private float x, y;
    private final float width;
    private final float height;
    private int levelThreshold;
    private int currentEXP;
    private final Color backgroundColor;
    private final Color foregroundColor;
    private int playerLevel;

    public EXPBar(float x, float y, float width, float height, int playerLevel, int levelThreshold,
                  Color backgroundColor, Color foregroundColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.levelThreshold = levelThreshold;
        this.currentEXP = 0;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.playerLevel = playerLevel;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setPosition(float x, float y) {
        // set x, y position
        this.x = x;
        this.y = y;
    }

    public void setCurrentEXP(int currentEXP) {
        // set current experience
        this.currentEXP = MathUtils.clamp(currentEXP, 0, this.levelThreshold);
    }

    public void setMaxEXP(int maxEXP) {
        // set max experience
        this.levelThreshold = maxEXP;
    }

    public void setCurrentLevel(int currentLevel) {
        this.playerLevel = currentLevel;
    }

    public void draw(ShapeRenderer shapeRenderer, Batch batch) {
        // draw background
        shapeRenderer.setColor(this.backgroundColor);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);

        // draw foreground (experience)
        float foregroundWidth = (float) this.currentEXP / this.levelThreshold * this.width;
        shapeRenderer.setColor(this.foregroundColor);
        shapeRenderer.rect(this.x, this.y, foregroundWidth, this.height);

        batch.begin();
        float textX = (float) (this.x + this.getWidth() / 1.05);
        float textY = this.y + this.getHeight() / 2 + 5;
        font.setColor(Color.WHITE); // Set font color
        font.draw(batch, "Level: " + this.playerLevel, textX, textY);
        batch.end();
    }
}
