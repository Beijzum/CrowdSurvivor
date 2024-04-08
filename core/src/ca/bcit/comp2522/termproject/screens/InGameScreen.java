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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents the in-game screen where the main gameplay takes place.
 * It manages the game's state, rendering, and input processing.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class InGameScreen implements Screen, Background, ActorManager, InputProcessor {
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
    private int enterUpgradeScreenAmount;


    public InGameScreen(final CrowdSurvivor crowdSurvivor) {
        this.camera = new OrthographicCamera();
        this.game = crowdSurvivor;
        this.player = Player.createPlayer();
        final int barHeight = 20;
        final int hpBarWidth = 300;
        this.hpBar = new HPBar(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), hpBarWidth, barHeight,
                this.player.getMaxHP());
        final float expBarWidth = 1.05f;
        this.expBar = new EXPBar((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight(),
                (Gdx.graphics.getWidth() / expBarWidth), barHeight, this.player.getLevel(), this.player.getLevelUpThreshold());
        this.enemyManager = EnemyManager.createManager(this);
        this.playerManager = PlayerManager.createPlayerManager(this);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.resetGameState();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        this.music.setLooping(true);
        this.music.play();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(final float v) {
        if (this.timeElapsed >= MAX_GAME_LENGTH) { // later add the stipulation that the boss needs to be killed too
            dispose();
            this.game.setScreen(this.game.getWinScreen());
            return;
        }
        final float screenUtilsValueB = 0.2f;
        ScreenUtils.clear(0, 0, screenUtilsValueB, 1);
        this.game.getButtonsUI().act();

        this.game.getBatch().setProjectionMatrix(this.camera.combined);

        // handle camera
        this.camera.position.set(this.player.getCenterX(), this.player.getCenterY(), 0);
        this.camera.update();

        // handle game logic
        handleGameLogic();

        // draw assets
        drawAssets();

        // draws HUD
        drawHUD();

        // check if player is dead, move to game over screen if so
        if (this.player.isDead()) {
            dispose();
            this.game.setScreen(this.game.getGameOverScreen());
            return;
        }

        // go to level up screen if leveled up
        if (this.enterUpgradeScreenAmount > 0) {
            this.game.setScreen(this.game.getUpgradeSelectionScreen());
            this.enterUpgradeScreenAmount--;
        }

        this.timeElapsed += Gdx.graphics.getDeltaTime();
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
        clearStage(this.game.getButtonsUI());
        this.music.dispose();
    }

    private void handleGameLogic() {
        this.enemyManager.incrementTimers();
        this.playerManager.incrementPlayerIframe();
        this.playerManager.handleContinuousPlayerKeyboardInput();
        this.playerManager.handleAttack();
        this.playerManager.handlePlayerHealth();
        this.enemyManager.handleEnemies();
        this.enemyManager.handleEnemySpawn();
        this.enemyManager.handleEnemyProjectiles();
    }

    private void drawAssets() {
        renderBackground(this.game, this.background);
        this.player.draw(this.game.getBatch());
        this.drawEnemies();
        this.drawAllEnemyProjectiles();
        this.drawAllPlayerProjectiles();
        this.game.getButtonsUI().draw();
    }

    private void drawHUD() {
        drawHPBar();
        drawEXPBar();
        drawCurrencyCounter();
    }

    private void drawAllPlayerProjectiles() {
        if (this.playerProjectilesOnScreen.isEmpty()) {
            return;
        }
        this.game.getBatch().begin();
        for (Projectile playerProjectile : this.playerProjectilesOnScreen) {
            playerProjectile.draw(this.game.getBatch());
        }
        this.game.getBatch().end();
    }

    private void drawAllEnemyProjectiles() {
        if (this.enemyProjectilesOnScreen.isEmpty()) {
            return;
        }
        this.game.getBatch().begin();
        for (Projectile enemyProjectile : this.enemyProjectilesOnScreen) {
            enemyProjectile.draw(this.game.getBatch());
        }
        this.game.getBatch().end();
    }

    private void drawEnemies() {
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);

        for (Enemy enemy : this.onFieldEnemies) {
            enemy.draw(this.game.getBatch());
            enemy.drawDamageNumbers(this.game.getBatch());

            this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            enemy.drawHPBar(this.shapeRenderer);
            this.shapeRenderer.end();
        }
    }

    private void drawHPBar() {
        final float hpBarDivisor = 2.2f;
        this.hpBar.setPosition((this.player.getX() - Gdx.graphics.getWidth() / hpBarDivisor),
                this.player.getY() + (float) Gdx.graphics.getHeight() / 2);
        this.hpBar.setMaxHP(this.player.getMaxHP());
        this.hpBar.setCurrentHP(this.player.getCurrentHP());
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.hpBar.draw(this.shapeRenderer, game.getBatch());
        this.shapeRenderer.end();
    }

    private void drawEXPBar() {
        final float expBarDivisor = 2.2f;
        final int expBarHeightAdjuster = 25;
        this.expBar.setPosition((player.getX() - Gdx.graphics.getWidth() / expBarDivisor),
                this.player.getY() + (float) Gdx.graphics.getHeight() / 2 + expBarHeightAdjuster);
        this.expBar.setMaxEXP(this.player.getLevelUpThreshold());
        this.expBar.setCurrentEXP(this.player.getCurrentEXP());
        this.expBar.setCurrentLevel(this.player.getLevel());
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.expBar.draw(this.shapeRenderer, this.game.getBatch());
        this.shapeRenderer.end();
    }

    private void drawCurrencyCounter() {
        this.game.getBatch().begin();
        CrowdSurvivor.getFont().setColor(Color.YELLOW);
        CrowdSurvivor.getFont().draw(this.game.getBatch(), "Currency: " + this.player.getCollectedCurrency(),
                this.player.getX() - Gdx.graphics.getWidth() / 2.2f,
                this.player.getY() + (float) Gdx.graphics.getHeight() / 2 - 30);
        CrowdSurvivor.getFont().setColor(Color.WHITE);
        this.game.getBatch().end();
    }

    public void resetGameState() {
        this.player.resetPosition();
        this.player.resetStats();
        this.timeElapsed = 0;
        this.onFieldEnemies.clear();
        this.playerProjectilesOnScreen.clear();
        this.enemyProjectilesOnScreen.clear();
    }


    public void handlePlayerKill(Enemy enemy) {
        this.player.addCollectedCurrency(enemy.getDropCurrency());
        this.enterUpgradeScreenAmount = this.player.addEXP(enemy.getDropEXP());
    }

    public void renderFrameAsBackground() {
        this.game.getBatch().setColor(this.darkTint);
        renderBackground(this.game, this.background);
        this.game.getInGameScreen().player.draw(this.game.getBatch());
        this.game.getInGameScreen().drawEnemies();
        this.game.getInGameScreen().drawAllPlayerProjectiles();
        this.gameUI.draw();
        this.game.getBatch().setColor(CrowdSurvivor.getStandardColour());
    }

    @Override
    public boolean keyDown(final int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            this.game.setScreen(this.game.getPauseMenuScreen());
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(final int keyCode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int i, final int i1, int i2, int i3) {
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
