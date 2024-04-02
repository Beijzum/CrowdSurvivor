package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;

public class InGameScreen implements Screen, Background, ActorManager {
    OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    private ArrayList<Enemy> onFieldEnemies = new ArrayList<>();
    final private LinkedList<Projectile> playerProjectilesOnScreen = new LinkedList<>();
    final private LinkedList<Projectile> enemyProjectilesOnScreen = new LinkedList<>();
    final private Player player;

    public InGameScreen(CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        camera.setToOrtho(false, game.viewportX, game.viewportY);
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.stageUI.act();

        game.batch.setProjectionMatrix(camera.combined);
        renderBackground(game, background);

        // handle logic firs
        player.handleMovement();
        player.handleUltimateCD();
        player.handleAttack(this.playerProjectilesOnScreen);

        // draw assets
        player.draw(game.batch);
        this.drawAllPlayerProjectiles();
        game.stageUI.draw();
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

    private void drawAllPlayerProjectiles() {
        if (this.playerProjectilesOnScreen.isEmpty()) {
            return;
        }
        game.batch.begin();
        for (Projectile playerProjectile : playerProjectilesOnScreen) {
            playerProjectile.draw(game.batch);
        }
        game.batch.end();
    }

    private void drawAllEnemyProjectiles() {
        if (this.enemyProjectilesOnScreen.isEmpty()) {
            return;
        }
        game.batch.begin();
        for (Projectile enemyProjectile : enemyProjectilesOnScreen) {
            enemyProjectile.draw(game.batch);
        }
        game.batch.end();
    }
}
