package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class InGameScreen implements Screen, Background, ActorManager {
    OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    private ArrayList<Enemy> onFieldEnemies;
    private ArrayList<Projectile> projectilesOnScreen;
    final private Player player;

    public InGameScreen(CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        camera.setToOrtho(false, 800, 400);
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();

        game.stage.addActor(player);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.stage.act();

        game.batch.setProjectionMatrix(camera.combined);
        renderBackground(game, background);

        player.handleMovement();

        game.stage.draw();
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

    @Override
    public void dispose() {

    }
}
