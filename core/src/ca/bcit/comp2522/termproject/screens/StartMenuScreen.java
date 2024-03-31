package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.Background;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;

public class StartMenuScreen implements Screen, Background {

    final private OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Texture logo = new Texture("logo.jpg");
    final private Sprite background = new Sprite(new Texture("backgrounds/startMenuBackground.jpg"));

    public StartMenuScreen(final CrowdSurvivor game) {
        this.camera = new OrthographicCamera();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/startMenuMusic.mp3"));
        camera.setToOrtho(false, game.viewportX, game.viewportY);
        this.game = game;
    }

    // run when the screen is first shown
    @Override
    public void show() {
        music.setLooping(true);
        music.play();
    }

    // run every frame
    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        renderBackground(game, background);

        game.batch.begin();
        game.font.draw(game.batch, "Click Anywhere To Start", game.viewportX / 2, game.viewportY * 7 / 8);
        game.batch.draw(logo, game.viewportX / 2, game.viewportY / 8);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            music.stop();
            dispose();
        }
    }

    // run when called
    @Override
    public void dispose() {
        music.dispose();
        logo.dispose();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
