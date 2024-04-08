package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimatedSprite extends Sprite {
    private final Texture[] frames;
    private int currentFrame;
    private final float framesPerSecond;
    private float timer;
    private boolean pausedAnimation;

    public AnimatedSprite(Texture texture) {
        super(texture);
        this.frames = new Texture[] {texture};
        this.framesPerSecond = 0;
        this.timer = 0;
    }

    public AnimatedSprite(Texture[] frames, float framesPerSecond) {
        super(frames[0]);
        this.frames = frames;
        this.framesPerSecond = framesPerSecond;
        this.currentFrame = 0;
        this.timer = 0;
    }

    public void update() {
        if (this.pausedAnimation) {
            return;
        }
        if (this.timer > 1 / framesPerSecond) {
            this.nextFrame();
            this.timer = 0;
            return;
        }
        this.timer += Gdx.graphics.getDeltaTime();
    }

    public void resetFrames() {
        this.currentFrame = 0;
    }

    public void setPausedAnimation(boolean state) {
        this.pausedAnimation = state;
    }

    private void nextFrame() {
        this.currentFrame++;
        if (currentFrame >= frames.length) {
            resetFrames();
        }
        setTexture(frames[currentFrame]);
    }
}
