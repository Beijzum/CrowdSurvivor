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
    private final Sprite background = new Sprite(new Texture("backgrounds/gameBackground.png"));
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

    /**
     * Constructs the in-game screen for the Crowd Survivor game.
     *
     * @param crowdSurvivor CrowdSurvivor object that represents the main game instance.
     */
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
                (Gdx.graphics.getWidth() / expBarWidth), barHeight, this.player.getLevel(),
                this.player.getLevelUpThreshold());
        this.enemyManager = EnemyManager.createManager(this);
        this.playerManager = PlayerManager.createPlayerManager(this);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/inGameMusic.mp3"));
        final int backgroundMultiplier = 3;
        this.background.setSize(Gdx.graphics.getWidth() * backgroundMultiplier,
                Gdx.graphics.getHeight() * backgroundMultiplier);
        this.resetGameState();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Retrieves the time elapsed in the current game.
     *
     * @return the time elapsed.
     */
    public float getTimeElapsed() {
        return this.timeElapsed;
    }

    /**
     * Retrieves the OrthographicCamera used for rendering.
     *
     * @return the OrthographicCamera instance.
     */
    public OrthographicCamera getCamera() {
        return this.camera;
    }

    /**
     * Retrieves the player instance.
     *
     * @return the player instance.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Retrieves the list of enemies currently on the field.
     *
     * @return the list of on-field enemies.
     */
    public ArrayList<Enemy> getOnFieldEnemies() {
        return this.onFieldEnemies;
    }

    /**
     * Retrieves the list of player projectiles on screen.
     *
     * @return the list of player projectiles.
     */
    public LinkedList<Projectile> getPlayerProjectilesOnScreen() {
        return this.playerProjectilesOnScreen;
    }

    /**
     * Retrieves the list of enemy projectiles on screen.
     *
     * @return the list of enemy projectiles.
     */
    public LinkedList<Projectile> getEnemyProjectilesOnScreen() {
        return this.enemyProjectilesOnScreen;
    }

    /**
     * Initializes the screen and starts playing the background music.
     */
    @Override
    public void show() {
        this.music.setLooping(true);
        this.music.play();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Renders the assets for the in-game screen.
     * Handles game logic, drawing, and state transitions.
     *
     * @param deltaTime the delta time since the last frame.
     */
    @Override
    public void render(final float deltaTime) {
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

    /**
     * Handles the resizing of the screen.
     *
     * @param width  the new width of the screen.
     * @param height the new height of the screen.
     */
    @Override
    public void resize(final int width, final int height) {

    }

    /**
     * Pauses the game.
     */
    @Override
    public void pause() {

    }

    /**
     * Resumes the game after it has been paused.
     */
    @Override
    public void resume() {

    }

    /**
     * Hides the current screen.
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes of resources and clears the stage.
     */
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
                this.player.getY() + Gdx.graphics.getHeight() / 2f);
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
                this.player.getY() + Gdx.graphics.getHeight() / 2f + expBarHeightAdjuster);
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
        final float currencyBarDivisor = 2.2f;
        final int currencyHeightAdjuster = 30;
        CrowdSurvivor.getFont().draw(this.game.getBatch(), "Currency: " + this.player.getCollectedCurrency(),
                this.player.getX() - Gdx.graphics.getWidth() / currencyBarDivisor,
                this.player.getY() + Gdx.graphics.getHeight() / 2f - currencyHeightAdjuster);
        CrowdSurvivor.getFont().setColor(Color.WHITE);
        this.game.getBatch().end();
    }

    /**
     * Resets the game state, including player position, stats, and other relevant entities.
     */
    public void resetGameState() {
        this.player.resetPosition();
        this.player.resetStats();
        this.timeElapsed = 0;
        this.onFieldEnemies.clear();
        this.playerProjectilesOnScreen.clear();
        this.enemyProjectilesOnScreen.clear();
    }

    /**
     * Handles actions to be taken when a player kills an enemy, such as collecting currency and experience points.
     *
     * @param enemy Enemy object representing an enemy instance.
     */
    public void handlePlayerKill(final Enemy enemy) {
        this.player.addCollectedCurrency(enemy.getDropCurrency());
        this.enterUpgradeScreenAmount = this.player.addEXP(enemy.getDropEXP());
    }

    /**
     * Renders the current frame as a background and tints it.
     */
    public void renderFrameAsBackground() {
        this.game.getBatch().setColor(this.darkTint);
        renderBackground(this.game, this.background);
        this.game.getInGameScreen().player.draw(this.game.getBatch());
        this.game.getInGameScreen().drawEnemies();
        this.game.getInGameScreen().drawAllPlayerProjectiles();
        this.gameUI.draw();
        this.game.getBatch().setColor(CrowdSurvivor.getStandardColour());
    }

    /**
     * Handles the key press events.
     *
     * @param keyCode the key code of the pressed key.
     * @return true if the event was handled, otherwise false.
     */
    @Override
    public boolean keyDown(final int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            this.game.setScreen(this.game.getPauseMenuScreen());
            return true;
        }
        return false;
    }

    /**
     * Handles the key release events.
     *
     * @param keyCode the key code of the released key.
     * @return false if the event was handled.
     */
    @Override
    public boolean keyUp(final int keyCode) {
        return false;
    }

    /**
     * Handles the typed key events.
     *
     * @param character the character of the typed key.
     * @return false if the event was handled.
     */
    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    /**
     * Handles the touch-down events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @param button  the button.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchDown(final int touchX, final int touchY, final int pointer, final int button) {
        return false;
    }

    /**
     * Handles the touch-up events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @param button  the button.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchUp(final int touchX, final int touchY, final int pointer, final int button) {
        return false;
    }

    /**
     * Handles the touch-cancelled events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @param button  the button.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchCancelled(final int touchX, final int touchY, final int pointer, final int button) {
        return false;
    }

    /**
     * Handles the touch-dragged events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchDragged(final int touchX, final int touchY, final int pointer) {
        return false;
    }

    /**
     * Handles the mouse-moved events.
     *
     * @param mouseX the x-coordinate of the mouse.
     * @param mouseY the y-coordinate of the mouse.
     * @return false if the event was handled.
     */
    @Override
    public boolean mouseMoved(final int mouseX, final int mouseY) {
        return false;
    }

    /**
     * Handles the scrolled events.
     *
     * @param scrollHorizontal the horizontal scrolling amount.
     * @param scrollVertical   the vertical scrolling amount.
     * @return false if the event was handled.
     */
    @Override
    public boolean scrolled(final float scrollHorizontal, final float scrollVertical) {
        return false;
    }

    /**
     * Returns a string representation of the InGameScreen object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "InGameScreen{"
                + "timeElapsed=" + timeElapsed
                + ", shapeRenderer=" + shapeRenderer
                + ", camera=" + camera
                + ", game=" + game
                + ", music=" + music
                + ", background=" + background
                + ", gameUI=" + gameUI
                + ", hpBar=" + hpBar
                + ", expBar=" + expBar
                + ", enemyManager=" + enemyManager
                + ", playerManager=" + playerManager
                + ", darkTint=" + darkTint
                + ", player=" + player
                + ", onFieldEnemies=" + onFieldEnemies
                + ", playerProjectilesOnScreen=" + playerProjectilesOnScreen
                + ", enemyProjectilesOnScreen=" + enemyProjectilesOnScreen
                + ", enterUpgradeScreenAmount=" + enterUpgradeScreenAmount
                + '}';
    }
}
