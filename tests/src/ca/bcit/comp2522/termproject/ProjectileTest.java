package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectileTest {
    @Mock
    Sprite sprite;

    private Projectile projectile;


    @BeforeEach
    public void setUp() {
        Graphics mockGraphics = mock(Graphics.class);
        Gdx.graphics = mockGraphics;

        sprite = mock(Sprite.class);
        projectile = new Projectile(sprite, 10, 5, 100);
        when(mockGraphics.getDeltaTime()).thenReturn(1f);
    }


    @Test
    public void testIsOverLifetimeLessThanTimer() {
        projectile.incrementLifetimeTimer();

        assertFalse(projectile.isOverLifeTime());
    }

    @Test
    public void testIsOverLifetimeMoreThanTimer() {
        projectile.incrementLifetimeTimer();
        projectile.incrementLifetimeTimer();
        projectile.incrementLifetimeTimer();
        projectile.incrementLifetimeTimer();
        projectile.incrementLifetimeTimer();
        projectile.incrementLifetimeTimer();

        assertTrue(projectile.isOverLifeTime());
    }

}
