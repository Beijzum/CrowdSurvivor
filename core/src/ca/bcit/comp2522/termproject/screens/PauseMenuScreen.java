package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseMenuScreen implements Screen, ActorManager, InputProcessor {
    final int numberOfButtons = 2;
    final private CrowdSurvivor game;
    final private TextButton[] menuItems = new TextButton[numberOfButtons]; // resume, quit
    final private InputMultiplexer inputManager;

    public PauseMenuScreen(CrowdSurvivor game) {
        this.game = game;
        this.inputManager = new InputMultiplexer(this, game.stageUI);
        createButtons();
    }

    @Override
    public void show() {
        addActors(game.stageUI, menuItems);
        Gdx.input.setInputProcessor(this.inputManager);
    }

    @Override
    public void render(float v) {
        game.stageUI.act();
        game.stageUI.draw();
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

    private void createButtons() {

        // calculate values for menu placement
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int firstButtonPositionY = Gdx.graphics.getHeight() / 2;

        // start game button
        TextButton resumeGameButton = new TextButton("Resume", game.skin);
        resumeGameButton.setSize(buttonWidth, buttonHeight);
        resumeGameButton.setPosition(buttonPositionX, firstButtonPositionY);
        resumeGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.stageUI);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });
        this.menuItems[0] = resumeGameButton;

        // quit game button
        TextButton quitButton = new TextButton("Quit", game.skin);
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.inGameScreen.resetGameState();
                clearStage(game.stageUI);
                game.inGameScreen.dispose();
                game.setScreen(game.mainMenuScreen);
                return true;
            }
        });
        this.menuItems[1] = quitButton;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            clearStage(game.stageUI);
            game.setScreen(game.inGameScreen);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
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
