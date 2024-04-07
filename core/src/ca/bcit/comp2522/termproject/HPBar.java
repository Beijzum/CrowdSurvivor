package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import static ca.bcit.comp2522.termproject.CrowdSurvivor.font;

public class HPBar {
    private float x, y;
    private final float width;
    private final float height;
    private int maxHP;
    private int currentHP;
    private final Color backgroundColor;
    private final Color foregroundColor;

    public HPBar(float x, float y, float width, float height, int maxHP,
                 Color backgroundColor, Color foregroundColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
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

    public void setCurrentHP(int currentHP) {
        // set current HP
        this.currentHP = MathUtils.clamp(currentHP, 0, this.maxHP);
    }

    public void setMaxHP(int maxHP) {
        // set max HP
        this.maxHP = maxHP;
    }

    public void draw(ShapeRenderer shapeRenderer, Batch batch) {
        // draw background
        shapeRenderer.setColor(this.backgroundColor);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);

        // draw foreground (HP)
        float foregroundWidth = (float) this.currentHP / this.maxHP * this.width;
        shapeRenderer.setColor(this.foregroundColor);
        shapeRenderer.rect(this.x, this.y, foregroundWidth, this.height);

        // draw HP current/max text
        batch.begin();
        float textX = this.x + 310;
        float textY = this.y + this.getHeight() / 2 + 5;
        font.getData().setScale(0.5F);
        font.draw(batch, currentHP + "/" + maxHP, textX, textY);
        font.getData().setScale(1);
        batch.end();
    }

}