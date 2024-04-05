package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import jdk.tools.jlink.internal.ExecutableImage;
import jdk.tools.jlink.internal.PostProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InGameScreen implements Screen, Background, ActorManager, InputProcessor {
    public OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    final private Stage gameUI = new Stage();
    final public Player player;

    final private HPBar hpBar;
    final private EnemyManager enemyManager;
    final private PlayerManager playerManager;
    final private InputMultiplexer inputManager;
    final private Color darkTint = new Color(75 / 255f, 75 / 255f, 75 / 255f, 1);
    private int enterUpgradeScreenAmount;
    final public ArrayList<Enemy> onFieldEnemies = new ArrayList<>();
    final public LinkedList<Projectile> playerProjectilesOnScreen = new LinkedList<>();
    final public LinkedList<Projectile> enemyProjectilesOnScreen = new LinkedList<>();

    public InGameScreen(CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.hpBar = new HPBar(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 100, 10,
                player.getMaxHP(), Color.RED, Color.GREEN);
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
        game.buttonsUI.act();

        game.batch.setProjectionMatrix(camera.combined);

        // handle camera
        camera.position.set(player.getCenterX(), player.getCenterY(), 0);
        camera.update();

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
        game.buttonsUI.draw();

        // draws HP bar
        drawHPBar();

        // check if player is dead, move to game over screen if so
        if (player.isDead()) {
            dispose();
            game.setScreen(game.gameOverScreen);
            return;
        }

        // go to level up screen if leveled up
        if (enterUpgradeScreenAmount > 0) {
            game.setScreen(game.upgradeSelectionScreen);
            this.enterUpgradeScreenAmount--;
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
        clearStage(game.buttonsUI);
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
            enemy.drawDamageNumbers(game.batch);
        }
    }

    private void drawHPBar() {
        hpBar.setPosition(player.getX(), player.getY() - hpBar.getHeight());
        hpBar.setMaxHP(player.getMaxHP());
        hpBar.setCurrentHP(player.getCurrentHP());
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        hpBar.draw(shapeRenderer);
        shapeRenderer.end();
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

    public void renderFrameAsBackground() {
        game.batch.setColor(this.darkTint);
        renderBackground(game, background);
        game.inGameScreen.player.draw(game.batch);
        game.inGameScreen.drawEnemies();
        game.inGameScreen.drawAllPlayerProjectiles();
        this.gameUI.draw();
        game.batch.setColor(CrowdSurvivor.STANDARD_COLOR);
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
