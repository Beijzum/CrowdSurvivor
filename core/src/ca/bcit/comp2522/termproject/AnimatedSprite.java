package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Arrays;

/**
 * Represents an animated sprite that can display multiple frames at a specified frame rate.
 * This class extends the LibGDX Sprite class to provide additional functionality for animation.
 * It manages the animation state, updates frames over time, and allows pausing and resetting of animation.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class AnimatedSprite extends Sprite {
    private final Texture[] frames;
    private int currentFrame;
    private final float framesPerSecond;
    private float timer;
    private boolean pausedAnimation;

    /**
     * Constructs an animated sprite with multiple frames and a specified frame rate.
     *
     * @param frames          Texture array objects representing the animation frames.
     * @param framesPerSecond float representing the animation frame rate.
     */
    public AnimatedSprite(final Texture[] frames, final float framesPerSecond) {
        super(frames[0]);
        this.frames = frames;
        this.framesPerSecond = framesPerSecond;
        this.currentFrame = 0;
        this.timer = 0;
    }

    /**
     * Updates the animated sprite by advancing to the next frame based on the frame rate.
     * If animation is paused, no update occurs.
     */
    public void update() {
        if (this.pausedAnimation) {
            return;
        }
        if (this.timer > 1 / this.framesPerSecond) {
            this.nextFrame();
            this.timer = 0;
            return;
        }
        this.timer += Gdx.graphics.getDeltaTime();
    }

    /**
     * Resets the current animation frame to the first frame.
     */
    public void resetFrames() {
        this.currentFrame = 0;
    }

    public int getCurrentFrame() {
        return this.currentFrame;
    }

    /**
     * Sets the animation state to paused or unpaused.
     *
     * @param state Boolean representing the animation state.
     */
    public void setPausedAnimation(final boolean state) {
        this.pausedAnimation = state;
    }

    private void nextFrame() {
        this.currentFrame++;
        if (this.currentFrame >= this.frames.length) {
            resetFrames();
        }
        setTexture(this.frames[this.currentFrame]);
    }

    /**
     * Returns the toString description of the AnimatedSprite object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "AnimatedSprite{"
                + "frames=" + Arrays.toString(this.frames)
                + ", currentFrame=" + this.currentFrame
                + ", framesPerSecond=" + this.framesPerSecond
                + ", timer=" + this.timer
                + ", pausedAnimation=" + this.pausedAnimation
                + '}';
    }
}
