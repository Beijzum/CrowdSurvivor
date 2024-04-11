package ca.bcit.comp2522.termproject.unittests;

import ca.bcit.comp2522.termproject.Player;
import ca.bcit.comp2522.termproject.enemies.Enemy;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static ca.bcit.comp2522.termproject.Player.createPlayer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnemyTest {
    @Mock
    Files files;
    @Mock
    FileHandle fileHandle;
    @Mock
    GL20 mockGL;

    @BeforeEach
    public void setUp() {
        InGameScreen gameScreen = mock(InGameScreen.class);
        Texture mockTexture = mock(Texture.class);
        Enemy enemy = new Enemy(100, 100, 10, gameScreen.getAtlas()
                .createSprite("enemies/enemy"));
        Player player = createPlayer(gameScreen.getAtlas());

        Gdx.gl = mockGL;
        Gdx.files = files;
        Gdx.graphics = mock(Graphics.class);
        gameScreen.getGame() = player;
        gameScreen.game = mock(COMP2522TermProject.class);
        gameScreen.getGame().getBatch() = mock(SpriteBatch.class);

        MockitoAnnotations.openMocks(this);

        when(files.internal("mock.jpg")).thenReturn(fileHandle);
        when(Gdx.graphics.getDeltaTime()).thenReturn(0.016f);
    }

}
