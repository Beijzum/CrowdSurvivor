package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import jdk.javadoc.internal.tool.Start;
import jdk.tools.jmod.Main;

public class CrowdSurvivor extends Game {

	public int viewportX;
	public int viewportY;
	public SpriteBatch batch;
	public BitmapFont font;
	public Stage stage;
	public MainMenuScreen mainMenuScreen;
	public StartMenuScreen startMenuScreen;
	public ShopScreen shopScreen;
	public LoadoutMenuScreen loadoutMenuScreen;
	public GachaScreen gachaScreen;
	public InGameScreen inGameScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		stage = new Stage(new ScreenViewport());

		viewportX = Gdx.graphics.getWidth();
		viewportY = Gdx.graphics.getHeight();

		mainMenuScreen = new MainMenuScreen(this);
		startMenuScreen = new StartMenuScreen(this);
		shopScreen = new ShopScreen(this);
		loadoutMenuScreen = new LoadoutMenuScreen(this);
		gachaScreen = new GachaScreen(this);
		inGameScreen = new InGameScreen(this);

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
