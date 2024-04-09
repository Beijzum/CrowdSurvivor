package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.GameOverScreen;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import ca.bcit.comp2522.termproject.screens.MainMenuScreen;
import ca.bcit.comp2522.termproject.screens.PauseMenuScreen;
import ca.bcit.comp2522.termproject.screens.ShopScreen;
import ca.bcit.comp2522.termproject.screens.UpgradeSelectionScreen;
import ca.bcit.comp2522.termproject.screens.WinScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Represents the Crowd Survivor class, which manages the game's screens, rendering, and resources.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class CrowdSurvivor extends Game {
    private static BitmapFont font;
    private static final Color STANDARD_COLOUR = new Color(1, 1, 1, 1);
    private Sound buttonClickSFX;
    private SpriteBatch batch;
    private Stage buttonsUI;
    private Skin skin;
    private MainMenuScreen mainMenuScreen;
    private ShopScreen shopScreen;
    private InGameScreen inGameScreen;
    private PauseMenuScreen pauseMenuScreen;
    private UpgradeSelectionScreen upgradeSelectionScreen;
    private GameOverScreen gameOverScreen;
    private WinScreen winScreen;
    private PlayerProfile playerProfile;

    /**
     * Constructs essential Crowd Survivor game components.
     * This includes the sprite batch, UI stage, skin, font, and game screens.
     */
    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.buttonsUI = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/vhs-ui.json"));
        font = this.skin.getFont("font");
        this.mainMenuScreen = new MainMenuScreen(this);
        this.shopScreen = new ShopScreen(this);
        this.inGameScreen = new InGameScreen(this);
        this.pauseMenuScreen = new PauseMenuScreen(this);
        this.upgradeSelectionScreen = new UpgradeSelectionScreen(this);
        this.gameOverScreen = new GameOverScreen(this);
        this.winScreen = new WinScreen(this);
        this.playerProfile = PlayerProfile.createPlayerProfile();
        this.buttonClickSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/buttonClickSFX.mp3"));
        this.setScreen(this.mainMenuScreen);
    }

    /**
     * Retrieves the sound effect for button clicks.
     *
     * @return the button click sound effect.
     */
    public Sound getButtonClickSFX() {
        return this.buttonClickSFX;
    }

    /**
     * Retrieves the font used for rendering text in the game.
     *
     * @return the font.
     */
    public static BitmapFont getFont() {
        return font;
    }

    /**
     * Retrieves the standard color used for various UI and game elements.
     *
     * @return the standard color used for various UI and game elements.
     */
    public static Color getStandardColour() {
        return STANDARD_COLOUR;
    }

    /**
     * Retrieves the sprite batch used for rendering 2D graphics.
     *
     * @return the sprite batch used for rendering 2D graphics.
     */
    public SpriteBatch getBatch() {
        return this.batch;
    }

    /**
     * Retrieves the UI stage containing buttons and other UI elements.
     *
     * @return the UI stage containing buttons and other UI elements.
     */
    public Stage getButtonsUI() {
        return this.buttonsUI;
    }

    /**
     * Retrieves the skin used for styling UI elements.
     *
     * @return the skin used for styling UI elements.
     */
    public Skin getSkin() {
        return this.skin;
    }

    /**
     * Retrieves the main menu screen.
     *
     * @return the main menu screen.
     */
    public MainMenuScreen getMainMenuScreen() {
        return this.mainMenuScreen;
    }

    /**
     * Retrieves the shop screen.
     *
     * @return the shop screen.
     */
    public ShopScreen getShopScreen() {
        return this.shopScreen;
    }

    /**
     * Retrieves the in-game screen.
     *
     * @return the in-game screen.
     */
    public InGameScreen getInGameScreen() {
        return this.inGameScreen;
    }

    /**
     * Retrieves the pause menu screen.
     *
     * @return the pause menu screen.
     */
    public PauseMenuScreen getPauseMenuScreen() {
        return this.pauseMenuScreen;
    }

    /**
     * Retrieves the upgrade selection screen.
     *
     * @return the upgrade selection screen.
     */
    public UpgradeSelectionScreen getUpgradeSelectionScreen() {
        return this.upgradeSelectionScreen;
    }

    /**
     * Retrieves the game over screen.
     *
     * @return the game over screen.
     */
    public GameOverScreen getGameOverScreen() {
        return this.gameOverScreen;
    }

    /**
     * Retrieves the win screen.
     *
     * @return the win screen.
     */
    public WinScreen getWinScreen() {
        return this.winScreen;
    }

    /**
     * Retrieves the player profile containing player-specific data.
     *
     * @return the player profile containing player-specific data.
     */
    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    /**
     * Sets the UI stage containing buttons and other UI elements.
     *
     * @param buttonsUI Stage object representing the UI elements.
     */
    public void setButtonsUI(final Stage buttonsUI) {
        this.buttonsUI = buttonsUI;
    }

    /**
     * Sets the shop screen.
     *
     * @param shopScreen ShopScreen object representing the shop screen.
     */
    public void setShopScreen(final ShopScreen shopScreen) {
        this.shopScreen = shopScreen;
    }

    /**
     * Sets the in-game screen.
     *
     * @param inGameScreen InGameScreen object representing the in-game screen.
     */
    public void setInGameScreen(final InGameScreen inGameScreen) {
        this.inGameScreen = inGameScreen;
    }

    /**
     * Sets the pause menu screen.
     *
     * @param pauseMenuScreen PauseMenuScreen object representing the pause menu screen.
     */
    public void setPauseMenuScreen(final PauseMenuScreen pauseMenuScreen) {
        this.pauseMenuScreen = pauseMenuScreen;
    }

    /**
     * Sets the upgrade selection screen.
     *
     * @param upgradeSelectionScreen UpgradeSelectionScreen object representing the upgrade selection screen.
     */
    public void setUpgradeSelectionScreen(final UpgradeSelectionScreen upgradeSelectionScreen) {
        this.upgradeSelectionScreen = upgradeSelectionScreen;
    }

    /**
     * Sets the game over screen.
     *
     * @param gameOverScreen GameOverScreen object representing the game over screen.
     */
    public void setGameOverScreen(final GameOverScreen gameOverScreen) {
        this.gameOverScreen = gameOverScreen;
    }

    /**
     * Sets the win screen.
     *
     * @param winScreen WinScreen object representing the win screen.
     */
    public void setWinScreen(final WinScreen winScreen) {
        this.winScreen = winScreen;
    }

    /**
     * Renders the current screen and updates the game state.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Disposes the resources used by the game when it is closed.
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    /**
     * Returns a string representation of the CrowdSurvivor object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "CrowdSurvivor{"
                + "batch=" + batch
                + ", buttonsUI=" + buttonsUI
                + ", skin=" + skin
                + ", mainMenuScreen=" + mainMenuScreen
                + ", shopScreen=" + shopScreen
                + ", inGameScreen=" + inGameScreen
                + ", pauseMenuScreen=" + pauseMenuScreen
                + ", upgradeSelectionScreen=" + upgradeSelectionScreen
                + ", gameOverScreen=" + gameOverScreen
                + ", winScreen=" + winScreen
                + ", playerProfile=" + playerProfile
                + '}';
    }
}
