package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnimatedSpriteTest {

    private AnimatedSprite animatedSprite;
    private Texture[] frames = {
            new Texture(Gdx.files.internal("player/playerSpriteFrame0.png")),
            new Texture(Gdx.files.internal("player/playerSpriteFrame1.png")),
            new Texture(Gdx.files.internal("player/playerSpriteFrame2.png")),
            new Texture(Gdx.files.internal("player/playerSpriteFrame3.png"))
    };
    private float framesPerSecond = 10f;

    @Before
    public void setUp() {
        animatedSprite = new AnimatedSprite(frames, framesPerSecond);
    }

    @Test
    public void testSetPausedAnimation() {
        animatedSprite.setPausedAnimation(true);
        assertTrue(animatedSprite.toString().contains("pausedAnimation=true"));

        animatedSprite.setPausedAnimation(false);
        assertFalse(animatedSprite.toString().contains("pausedAnimation=true"));
    }

    @Test
    public void testResetFrames() {
        animatedSprite.resetFrames();
        assertEquals(0, animatedSprite.getCurrentFrame());

        animatedSprite.update();
        assertEquals(1, animatedSprite.getCurrentFrame());

        animatedSprite.resetFrames();
        assertEquals(0, animatedSprite.getCurrentFrame());
    }

    @Test
    public void testUpdate() {
        animatedSprite.setPausedAnimation(false);

        for (int i = 0; i < frames.length * 10; i++) {
            animatedSprite.update();
        }

        assertEquals(frames.length - 1, animatedSprite.getCurrentFrame());

        animatedSprite.update();
        assertEquals(0, animatedSprite.getCurrentFrame());

        animatedSprite.setPausedAnimation(true);
        int currentFrameBeforePause = animatedSprite.getCurrentFrame();
        animatedSprite.update();
        assertEquals(currentFrameBeforePause, animatedSprite.getCurrentFrame());
    }
}