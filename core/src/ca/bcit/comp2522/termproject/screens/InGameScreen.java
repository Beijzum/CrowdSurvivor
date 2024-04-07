package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import ca.bcit.comp2522.termproject.enemies.Enemy;
import ca.bcit.comp2522.termproject.enemies.EnemyManager;
import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.interfaces.Background;

import java.util.ArrayList;
import java.util.LinkedList;

import static ca.bcit.comp2522.termproject.CrowdSurvivor.font;

public class InGameScreen implements Screen, Background, ActorManager, InputProcessor {
    final private static int MAX_GAME_LENGTH = 300;
    public float timeElapsed = 0;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    final private Stage gameUI = new Stage();
    final public Player player;
    final private HPBar hpBar;
    final private EXPBar expBar;
    final private EnemyManager enemyManager;
    final private PlayerManager playerManager;
    final private Color darkTint = new Color(75 / 255f, 75 / 255f, 75 / 255f, 1);
    private int enterUpgradeScreenAmount;
    final public ArrayList<Enemy> onFieldEnemies = new ArrayList<>();
    final public LinkedList<Projectile> playerProjectilesOnScreen = new LinkedList<>();
    final public LinkedList<Projectile> enemyProjectilesOnScreen = new LinkedList<>();


    public InGameScreen(CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.hpBar = new HPBar(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 300, 20,
                player.getMaxHP(), Color.RED, Color.GREEN);
        this.expBar = new EXPBar((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight(),
                (float) (Gdx.graphics.getWidth() / 1.05), 20, player.getLevel(), player.getLevelUpThreshold(),
                Color.BLUE, Color.CYAN);
        this.enemyManager = EnemyManager.createManager(this);
        this.playerManager = PlayerManager.createPlayerManager(this);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.resetGameState();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float v) {
        if (timeElapsed >= MAX_GAME_LENGTH) { // later add the stipulation that the boss needs to be killed too
            dispose();
            game.setScreen(game.winScreen);
            return;
        }
        ScreenUtils.clear(0, 0, 0.2f, 1);
        game.buttonsUI.act();

        game.batch.setProjectionMatrix(camera.combined);

        // handle camera
        camera.position.set(player.getCenterX(), player.getCenterY(), 0);
        camera.update();

        // handle game logic
        enemyManager.incrementTimers();
        playerManager.incrementPlayerIframe();
        playerManager.handleContinuousPlayerKeyboardInput();
        playerManager.handleAttack();
        playerManager.handlePlayerHealth();
        enemyManager.handleEnemies();
        enemyManager.handleEnemySpawn();
        enemyManager.handleEnemyProjectiles();

        // draw assets
        renderBackground(game, background);
        player.draw(game.batch);
        this.drawEnemies();
        this.drawAllEnemyProjectiles();
        this.drawAllPlayerProjectiles();
        game.buttonsUI.draw();

        // draws HUD
        drawHPBar();
        drawEXPBar();
        drawCurrencyCounter();

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

        timeElapsed += Gdx.graphics.getDeltaTime();
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
        shapeRenderer.setProjectionMatrix(camera.combined);

        for (Enemy enemy : this.onFieldEnemies) {
            enemy.draw(game.batch);
            enemy.drawDamageNumbers(game.batch);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            enemy.drawHPBar(shapeRenderer);
            shapeRenderer.end();
        }
    }

    private void drawHPBar() {
        hpBar.setPosition((float) (player.getX() - Gdx.graphics.getWidth() / 2.2),
                player.getY() + (float) Gdx.graphics.getHeight() / 2);
        hpBar.setMaxHP(player.getMaxHP());
        hpBar.setCurrentHP(player.getCurrentHP());
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        hpBar.draw(shapeRenderer, game.batch);
        shapeRenderer.end();
    }

    private void drawEXPBar() {
        expBar.setPosition((float) (player.getX() - Gdx.graphics.getWidth() / 2.2),
                player.getY() + (float) Gdx.graphics.getHeight() / 2 + 25);
        expBar.setMaxEXP(player.getLevelUpThreshold());
        expBar.setCurrentEXP(player.getCurrentEXP());
        expBar.setCurrentLevel(player.getLevel());
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        expBar.draw(shapeRenderer, game.batch);
        shapeRenderer.end();
    }

    private void drawCurrencyCounter() {
        game.batch.begin();
        font.setColor(Color.YELLOW);
        font.draw(game.batch, "Currency: " + player.getCollectedCurrency(),
                player.getX() - Gdx.graphics.getWidth() / 2.2f,
                player.getY() + (float) Gdx.graphics.getHeight() / 2 - 30);
        font.setColor(Color.WHITE);
        game.batch.end();
    }

    public void resetGameState() {
        player.resetPosition();
        player.resetStats();
        this.timeElapsed = 0;
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
