package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.GameOverScreen;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import ca.bcit.comp2522.termproject.screens.MainMenuScreen;
import ca.bcit.comp2522.termproject.screens.PauseMenuScreen;
import ca.bcit.comp2522.termproject.screens.ShopScreen;
import ca.bcit.comp2522.termproject.screens.StartMenuScreen;
import ca.bcit.comp2522.termproject.screens.UpgradeSelectionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CrowdSurvivor extends Game {

    public int viewportX;
    public int viewportY;
    public SpriteBatch batch;
    public static BitmapFont font;
    public Stage buttonsUI;
    public Skin skin;
    public static Color STANDARD_COLOR = new Color(1, 1, 1, 1);
    public MainMenuScreen mainMenuScreen;
    public StartMenuScreen startMenuScreen;
    public ShopScreen shopScreen;
    public InGameScreen inGameScreen;
    public PauseMenuScreen pauseMenuScreen;
    public UpgradeSelectionScreen upgradeSelectionScreen;
    public GameOverScreen gameOverScreen;
    public PlayerProfile playerProfile;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        buttonsUI = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/vhs-ui.json"));

        viewportX = Gdx.graphics.getWidth();
        viewportY = Gdx.graphics.getHeight();

        mainMenuScreen = new MainMenuScreen(this);
        startMenuScreen = new StartMenuScreen(this);
        shopScreen = new ShopScreen(this);
        inGameScreen = new InGameScreen(this);
        pauseMenuScreen = new PauseMenuScreen(this);
        upgradeSelectionScreen = new UpgradeSelectionScreen(this);
        gameOverScreen = new GameOverScreen(this);
        playerProfile = PlayerProfile.createPlayerProfile();

        this.setScreen(startMenuScreen);
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
