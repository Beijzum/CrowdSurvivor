package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.interfaces.Background;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import ca.bcit.comp2522.termproject.interfaces.MessageLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen, Background, ActorManager, MessageLayout {
    final private static int NUMBER_OF_BUTTONS = 3;
    OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/mainMenuBackground.jpg"));
    final private TextButton[] menuItems = new TextButton[NUMBER_OF_BUTTONS]; // start game, shop, quit buttons

    public MainMenuScreen(final CrowdSurvivor game) {
        this.camera = new OrthographicCamera();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/mainMenuMusic.mp3"));
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game = game;
        createButtons();
    }

    @Override
    public void show() {
        game.getPlayerProfile().saveProfileState();
        Gdx.input.setInputProcessor(game.getButtonsUI());
        addActors(game.getButtonsUI(), menuItems);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        renderBackground(game, background);
        drawMessageFromCenter(createLayout("CROWD SURVIVOR", 3), game.getBatch(),
                Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 8 / 10f, 2);
        game.getButtonsUI().act();
        game.getButtonsUI().draw();
    }

    /**
     * Handles the resizing of the screen.
     *
     * @param width  the new width of the screen.
     * @param height the new height of the screen.
     */
    @Override
    public void resize(final int width, final int height) {

    }

    /**
     * Pauses the game.
     */
    @Override
    public void pause() {

    }

    /**
     * Resumes the game after it has been paused.
     */
    @Override
    public void resume() {

    }

    /**
     * Hides the screen when no longer active or visible.
     */
    @Override
    public void hide() {
        clearStage(game.getButtonsUI());
        dispose();
    }

    /**
     * Disposes of resources and clears the music.
     */
    @Override
    public void dispose() {
        music.dispose();
    }

    private void createButtons() {
        // calculate values for menu placement
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int firstButtonPositionY = Gdx.graphics.getHeight() / 3;

        // start game button
        TextButton startGameButton = new TextButton("Start Game", game.getSkin());
        startGameButton.setSize(buttonWidth, buttonHeight);
        startGameButton.setPosition(buttonPositionX, firstButtonPositionY);
        startGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getPlayerProfile().applyPlayerUpgrades(game.getInGameScreen().getPlayer());
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
        this.menuItems[0] = startGameButton;

        // shop button
        TextButton shopButton = new TextButton("Shop", game.getSkin());
        shopButton.setSize(buttonWidth, buttonHeight);
        shopButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight);
        shopButton.addListener(new InputListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.setScreen(game.getShopScreen());
                return true;
            }
        });
        this.menuItems[1] = shopButton;

        // quit game button
        TextButton quitButton = new TextButton("Quit", game.getSkin());
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                Gdx.app.exit();
                return true;
            }
        });
        this.menuItems[2] = quitButton;
    }
}
