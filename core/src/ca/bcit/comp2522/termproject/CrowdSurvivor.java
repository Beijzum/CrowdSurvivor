package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.StartMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CrowdSurvivor extends Game {

	public int viewportX;
	public int viewportY;
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		viewportX = Gdx.graphics.getWidth();
		viewportY = Gdx.graphics.getHeight();
		this.setScreen(new StartMenuScreen(this));
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
