package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class EXPBar {
    private float x, y;
    private final float width;
    private final float height;
    private int levelthreshold;
    private int currentEXP;
    private final Color backgroundColor;
    private final Color foregroundColor;

    public EXPBar(float x, float y, float width, float height, int levelthreshold,
                  Color backgroundColor, Color foregroundColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.levelthreshold = levelthreshold;
        this.currentEXP = 0;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
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
        this.currentEXP = MathUtils.clamp(currentEXP, 0, this.levelthreshold);
    }

    public void setMaxEXP(int maxEXP) {
        // set max experience
        this.levelthreshold = maxEXP;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        // draw background
        shapeRenderer.setColor(this.backgroundColor);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);

        // draw foreground (experience)
        float foregroundWidth = (float) this.currentEXP / this.levelthreshold * this.width;
        shapeRenderer.setColor(this.foregroundColor);
        shapeRenderer.rect(this.x, this.y, foregroundWidth, this.height);
    }
}
