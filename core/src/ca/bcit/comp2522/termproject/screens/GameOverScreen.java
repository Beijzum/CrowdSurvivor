package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameOverScreen implements Screen, ActorManager {
    final int numberOfButtons = 2;
    final private CrowdSurvivor game;
    final private TextButton[] menuItems = new TextButton[numberOfButtons]; // try again, quit

    public GameOverScreen(CrowdSurvivor game) {
        this.game = game;
        createButtons();
    }

    private void createButtons() {

        // calculate values for menu placement
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int firstButtonPositionY = Gdx.graphics.getHeight() / 2;

        // start game button
        TextButton tryAgainButton = new TextButton("Try Again", game.skin);
        tryAgainButton.setSize(buttonWidth, buttonHeight);
        tryAgainButton.setPosition(buttonPositionX, firstButtonPositionY);
        tryAgainButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.buttonsUI);
                game.inGameScreen.resetGameState();
                game.setScreen(game.inGameScreen);
                return true;
            }
        });
        this.menuItems[0] = tryAgainButton;

        // quit game button
        TextButton quitButton = new TextButton("Return To Menu", game.skin);
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.inGameScreen.resetGameState();
                clearStage(game.buttonsUI);
                game.inGameScreen.dispose();
                game.setScreen(game.mainMenuScreen);
                return true;
            }
        });
        this.menuItems[1] = quitButton;
    }

    @Override
    public void show() {
        addActors(game.buttonsUI, menuItems);
        Gdx.input.setInputProcessor(game.buttonsUI);
    }

    @Override
    public void render(float v) {
        game.inGameScreen.renderFrameAsBackground();
        game.buttonsUI.act();
        game.buttonsUI.draw();
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

    }
}
