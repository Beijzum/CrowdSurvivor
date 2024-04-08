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
    static BitmapFont font;
    private SpriteBatch batch;
    private Stage buttonsUI;
    public Skin skin;
    public static Color STANDARD_COLOR = new Color(1, 1, 1, 1);
    public MainMenuScreen mainMenuScreen;
    public ShopScreen shopScreen;
    public InGameScreen inGameScreen;
    public PauseMenuScreen pauseMenuScreen;
    public UpgradeSelectionScreen upgradeSelectionScreen;
    public GameOverScreen gameOverScreen;
    public WinScreen winScreen;
    public PlayerProfile playerProfile;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.buttonsUI = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/vhs-ui.json"));
        font = skin.getFont("font");
        mainMenuScreen = new MainMenuScreen(this);
        shopScreen = new ShopScreen(this);
        inGameScreen = new InGameScreen(this);
        pauseMenuScreen = new PauseMenuScreen(this);
        upgradeSelectionScreen = new UpgradeSelectionScreen(this);
        gameOverScreen = new GameOverScreen(this);
        winScreen = new WinScreen(this);
        playerProfile = PlayerProfile.createPlayerProfile();

        this.setScreen(mainMenuScreen);
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public Stage getButtonsUI() {
        return this.buttonsUI;
    }

    public static BitmapFont getFont() {
        return font;
    }

    public void setButtonsUI(final Stage buttonsUI) {
        this.buttonsUI = buttonsUI;
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
