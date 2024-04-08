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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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

        this.setScreen(this.mainMenuScreen);
    }

    public static BitmapFont getFont() {
        return font;
    }

    public static Color getStandardColour() {
        return STANDARD_COLOUR;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public Stage getButtonsUI() {
        return this.buttonsUI;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public MainMenuScreen getMainMenuScreen() {
        return this.mainMenuScreen;
    }

    public ShopScreen getShopScreen() {
        return this.shopScreen;
    }

    public InGameScreen getInGameScreen() {
        return this.inGameScreen;
    }

    public PauseMenuScreen getPauseMenuScreen() {
        return this.pauseMenuScreen;
    }

    public UpgradeSelectionScreen getUpgradeSelectionScreen() {
        return this.upgradeSelectionScreen;
    }

    public GameOverScreen getGameOverScreen() {
        return this.gameOverScreen;
    }

    public WinScreen getWinScreen() {
        return this.winScreen;
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    public void setButtonsUI(final Stage buttonsUI) {
        this.buttonsUI = buttonsUI;
    }

    public void setShopScreen(final ShopScreen shopScreen) {
        this.shopScreen = shopScreen;
    }

    public void setInGameScreen(final InGameScreen inGameScreen) {
        this.inGameScreen = inGameScreen;
    }

    public void setPauseMenuScreen(final PauseMenuScreen pauseMenuScreen) {
        this.pauseMenuScreen = pauseMenuScreen;
    }

    public void setUpgradeSelectionScreen(final UpgradeSelectionScreen upgradeSelectionScreen) {
        this.upgradeSelectionScreen = upgradeSelectionScreen;
    }

    public void setGameOverScreen(final GameOverScreen gameOverScreen) {
        this.gameOverScreen = gameOverScreen;
    }

    public void setWinScreen(final WinScreen winScreen) {
        this.winScreen = winScreen;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}
