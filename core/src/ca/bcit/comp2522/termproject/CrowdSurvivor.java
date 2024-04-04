package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CrowdSurvivor extends Game {

	public int viewportX;
	public int viewportY;
	public SpriteBatch batch;
	public BitmapFont font;
	public Stage buttonsUI;
	public Skin skin;
	public MainMenuScreen mainMenuScreen;
	public StartMenuScreen startMenuScreen;
	public ShopScreen shopScreen;
	public InGameScreen inGameScreen;
	public PauseMenuScreen pauseMenuScreen;
	public UpgradeSelectionScreen upgradeSelectionScreen;
	public GameOverScreen gameOverScreen;
	public PlayerProfile playerProfile;

	@Override
	public void create () {
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
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
