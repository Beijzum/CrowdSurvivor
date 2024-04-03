package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseMenuScreen implements Screen, ActorManager {
    final int numberOfButtons = 3;
    final private CrowdSurvivor game;
    final private TextButton[] menuItems = new TextButton[numberOfButtons]; // resume, see player stats, quit

    public PauseMenuScreen(CrowdSurvivor game) {
        this.game = game;
        createButtons();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(game.stageUI);
        addActors(game.stageUI, menuItems);
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
        int firstButtonPositionY = Gdx.graphics.getHeight() / 3;

        // start game button
        TextButton startGameButton = new TextButton("Start Game", game.skin);
        startGameButton.setSize(buttonWidth, buttonHeight);
        startGameButton.setPosition(buttonPositionX, firstButtonPositionY);
        startGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
//                game.setScreen(game.loadoutMenuScreen); TO IMPLEMENT LATER, GET THE GAME DONE FIRST
                game.setScreen(game.inGameScreen);
                return true;
            }
        });
        this.menuItems[0] = startGameButton;

        // shop button
        TextButton shopButton = new TextButton("Shop", game.skin);
        shopButton.setSize(buttonWidth, buttonHeight);
        shopButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight);
        shopButton.addListener(new InputListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.setScreen(game.shopScreen);
                return true;
            }
        });
        this.menuItems[1] = shopButton;

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
                Gdx.app.exit();
                return true;
            }
        });
        this.menuItems[2] = quitButton;
    }
}
