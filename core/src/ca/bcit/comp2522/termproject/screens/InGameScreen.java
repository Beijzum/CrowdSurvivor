package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import ca.bcit.comp2522.termproject.EXPBar;
import ca.bcit.comp2522.termproject.HPBar;
import ca.bcit.comp2522.termproject.Player;
import ca.bcit.comp2522.termproject.PlayerManager;
import ca.bcit.comp2522.termproject.Projectile;
import ca.bcit.comp2522.termproject.enemies.Enemy;
import ca.bcit.comp2522.termproject.enemies.EnemyManager;
import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.interfaces.Background;
import ca.bcit.comp2522.termproject.interfaces.MessageLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;

public class InGameScreen implements Screen, Background, ActorManager, InputProcessor, MessageLayout {
    private static final int MAX_GAME_LENGTH = 300;
    private float timeElapsed = 0;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final OrthographicCamera camera;
    private final CrowdSurvivor game;
    private final Music music;
    private final Sprite background = new Sprite(new Texture("backgrounds/tempBackground.jpg"));
    private final Stage gameUI = new Stage();
    private final HPBar hpBar;
    private final EXPBar expBar;
    private final EnemyManager enemyManager;
    private final PlayerManager playerManager;
    private final Color darkTint = new Color(75 / 255f, 75 / 255f, 75 / 255f, 1);
    private final Player player;
    private final ArrayList<Enemy> onFieldEnemies = new ArrayList<>();
    private final LinkedList<Projectile> playerProjectilesOnScreen = new LinkedList<>();
    private final LinkedList<Projectile> enemyProjectilesOnScreen = new LinkedList<>();
    private final GlyphLayout timeElapsedMessage = new GlyphLayout(CrowdSurvivor.getFont(),
            String.format("%d:%02d", Math.round(this.timeElapsed) / 60, Math.round(this.timeElapsed) % 60));
    private int enterUpgradeScreenAmount;


    public InGameScreen(final CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        this.hpBar = new HPBar(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 300, 20,
                player.getMaxHP());
        this.expBar = new EXPBar((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight(),
                (float) (Gdx.graphics.getWidth() / 1.05), 20, player.getLevel(), player.getLevelUpThreshold());
        this.enemyManager = EnemyManager.createManager(this);
        this.playerManager = PlayerManager.createPlayerManager(this);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.resetGameState();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public float getTimeElapsed() {
        return this.timeElapsed;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<Enemy> getOnFieldEnemies() {
        return this.onFieldEnemies;
    }

    public LinkedList<Projectile> getPlayerProjectilesOnScreen() {
        return this.playerProjectilesOnScreen;
    }

    public LinkedList<Projectile> getEnemyProjectilesOnScreen() {
        return this.enemyProjectilesOnScreen;
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
            game.setScreen(game.getWinScreen());
            return;
        }
        ScreenUtils.clear(0, 0, 0.2f, 1);
        game.getButtonsUI().act();

        game.getBatch().setProjectionMatrix(camera.combined);

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
        player.draw(game.getBatch());
        this.drawEnemies();
        this.drawAllEnemyProjectiles();
        this.drawAllPlayerProjectiles();
        this.updateTimerMessage();
        drawMessageFromCenter(timeElapsedMessage, game.getBatch(),
                camera.position.x, camera.position.y + Gdx.graphics.getHeight() / 3f, 1);


        // draws HUD
        drawHPBar();
        drawEXPBar();
        drawCurrencyCounter();

        // check if player is dead, move to game over screen if so
        if (player.isDead()) {
            dispose();
            game.setScreen(game.getGameOverScreen());
            return;
        }

        // go to level up screen if leveled up
        if (enterUpgradeScreenAmount > 0) {
            game.setScreen(game.getUpgradeSelectionScreen());
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
        clearStage(game.getButtonsUI());
        music.dispose();
    }

    private void updateTimerMessage() {
        this.timeElapsedMessage.setText(CrowdSurvivor.getFont(),
                String.format("%d:%02d", Math.round(this.timeElapsed) / 60, Math.round(this.timeElapsed) % 60));
    }

    private void drawAllPlayerProjectiles() {
        if (this.playerProjectilesOnScreen.isEmpty()) {
            return;
        }
        game.getBatch().begin();
        for (Projectile playerProjectile : playerProjectilesOnScreen) {
            playerProjectile.draw(game.getBatch());
        }
        game.getBatch().end();
    }

    private void drawAllEnemyProjectiles() {
        if (this.enemyProjectilesOnScreen.isEmpty()) {
            return;
        }
        game.getBatch().begin();
        for (Projectile enemyProjectile : enemyProjectilesOnScreen) {
            enemyProjectile.draw(game.getBatch());
        }
        game.getBatch().end();
    }

    private void drawEnemies() {
        shapeRenderer.setProjectionMatrix(camera.combined);

        for (Enemy enemy : this.onFieldEnemies) {
            enemy.draw(game.getBatch());
            enemy.drawDamageNumbers(game.getBatch());

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
        hpBar.draw(shapeRenderer, game.getBatch());
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
        expBar.draw(shapeRenderer, game.getBatch());
        shapeRenderer.end();
    }

    private void drawCurrencyCounter() {
        game.getBatch().begin();
        CrowdSurvivor.getFont().setColor(Color.YELLOW);
        CrowdSurvivor.getFont().draw(game.getBatch(), "Currency: " + player.getCollectedCurrency(),
                player.getX() - Gdx.graphics.getWidth() / 2.2f,
                player.getY() + (float) Gdx.graphics.getHeight() / 2 - 30);
        CrowdSurvivor.getFont().setColor(Color.WHITE);
        game.getBatch().end();
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
        game.getBatch().setColor(this.darkTint);
        renderBackground(game, background);
        game.getInGameScreen().player.draw(game.getBatch());
        game.getInGameScreen().drawEnemies();
        game.getInGameScreen().drawAllPlayerProjectiles();
        this.gameUI.draw();
        game.getBatch().setColor(CrowdSurvivor.getStandardColour());
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            this.game.setScreen(game.getPauseMenuScreen());
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
