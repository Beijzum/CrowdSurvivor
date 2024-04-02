package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;

public class InGameScreen implements Screen, Background, ActorManager {
    OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    final public Player player;
    final private EnemyManager enemyManager;
    final public ArrayList<Enemy> onFieldEnemies = new ArrayList<>();
    final public LinkedList<Projectile> playerProjectilesOnScreen = new LinkedList<>();
    final public LinkedList<Projectile> enemyProjectilesOnScreen = new LinkedList<>();

    public InGameScreen(CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.enemyManager = EnemyManager.createManager(this);
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
        player.handleMovement();
        game.stageUI.act();

        game.batch.setProjectionMatrix(camera.combined);
        renderBackground(game, background);

        // handle logic first
        Vector3 cameraPosition = camera.position;
        Vector2 playerPosition = player.getPosition();
        float lerp = 8.5f;
        cameraPosition.x += (playerPosition.x - cameraPosition.x) * lerp * v;
        cameraPosition.y += (playerPosition.y - cameraPosition.y) * lerp * v;
        camera.update();

        enemyManager.incrementTimers();
        player.handleUltimateCD();
        player.handleAttack(this.playerProjectilesOnScreen);
        enemyManager.handleEnemies();
        enemyManager.handleEnemySpawn();
        enemyManager.handleEnemyProjectiles();

        // draw assets
        player.draw(game.batch);
        this.drawAllPlayerProjectiles();
        this.drawEnemies();
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

    private void drawEnemies() {
        game.batch.begin();
        for (Enemy enemy : this.onFieldEnemies) {
            enemy.draw(game.batch);
        }
        game.batch.end();
    }
}
