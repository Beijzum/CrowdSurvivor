package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;

public class InGameScreen implements Screen, Background, ActorManager, InputProcessor {
    public OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    final public Player player;
    final private EnemyManager enemyManager;
    final private PlayerManager playerManager;
    final private InputMultiplexer inputManager;
    private int enterUpgradeScreenAmount;
    final public ArrayList<Enemy> onFieldEnemies = new ArrayList<>();
    final public LinkedList<Projectile> playerProjectilesOnScreen = new LinkedList<>();
    final public LinkedList<Projectile> enemyProjectilesOnScreen = new LinkedList<>();

    public InGameScreen(CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.enemyManager = EnemyManager.createManager(this);
        this.playerManager = PlayerManager.createPlayerManager(this);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.resetGameState();
        this.inputManager = new InputMultiplexer(this, playerManager);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();
        Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        game.stageUI.act();

        game.batch.setProjectionMatrix(camera.combined);

        // handle camera
        camera.position.set(player.getCenterX(), player.getCenterY(), 0);
        camera.update();
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        // handle game logic
        enemyManager.incrementTimers();
        playerManager.handleContinuousPlayerKeyboardInput();
        playerManager.handleUltimateCD();
        playerManager.handleAttack();
        playerManager.handlePlayerHealth();
        enemyManager.handleEnemies();
        enemyManager.handleEnemySpawn();
        enemyManager.handleEnemyProjectiles();

        // draw assets
        renderBackground(game, background);
        player.draw(game.batch);
        this.drawEnemies();
        this.drawAllPlayerProjectiles();
        game.stageUI.draw();

        // go to level up screen if leveled up
        if (enterUpgradeScreenAmount > 0) {
            game.setScreen(game.upgradeSelectionScreen);
            this.enterUpgradeScreenAmount--;
        }

        // check if player is dead, move to game over screen if so
        if (player.isDead()) {
            dispose();
            game.setScreen(game.gameOverScreen);
        }
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
        clearStage(game.stageUI);
        music.dispose();
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
        for (Enemy enemy : this.onFieldEnemies) {
            enemy.draw(game.batch);
        }
    }

    public void resetGameState() {
        player.resetPosition();
        player.resetStats();
        this.onFieldEnemies.clear();
        this.playerProjectilesOnScreen.clear();
        this.enemyProjectilesOnScreen.clear();
    }


    public void handlePlayerKill(Enemy enemy) {
        player.addCollectedCurrency(enemy.getDropCurrency());
        this.enterUpgradeScreenAmount = player.addEXP(enemy.getDropEXP());
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            this.game.setScreen(game.pauseMenuScreen);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
