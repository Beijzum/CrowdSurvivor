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

import java.util.Arrays;

/**
 * Represents the main menu screen displayed when the game starts.
 * This screen provides options for the player to start the game, access the shop, or quit the game.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class MainMenuScreen implements Screen, Background, ActorManager, MessageLayout {
    private static final int NUMBER_OF_BUTTONS = 3;
    private final OrthographicCamera camera;
    private final CrowdSurvivor game;
    private final Music music;
    private final Sprite background = new Sprite(new Texture("backgrounds/mainMenuBackground.jpg"));
    private final TextButton[] menuItems = new TextButton[NUMBER_OF_BUTTONS]; // start game, shop, quit buttons
    private final TextButton startGameButton;
    private final TextButton shopButton;
    private final TextButton quitButton;
    private final float buttonWidth;
    private final float buttonHeight;
    private final float buttonPositionX;
    private final float firstButtonPositionY;

    /**
     * Constructs a main menu screen for the Crowd Survivor game.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     */
    public MainMenuScreen(final CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.camera = new OrthographicCamera();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/mainMenuMusic.mp3"));
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        final float buttonWidthDivisor = 3f;
        this.buttonWidth = Gdx.graphics.getWidth() / buttonWidthDivisor;
        final float buttonHeightDivisor = 10f;
        this.buttonHeight = Gdx.graphics.getHeight() / buttonHeightDivisor;
        this.buttonPositionX = Gdx.graphics.getWidth() / 2f - this.buttonWidth / 2;
        final float buttonYDivisor = 3f;
        this.firstButtonPositionY = Gdx.graphics.getHeight() / buttonYDivisor;
        this.startGameButton = new TextButton("Start Game", this.game.getSkin());
        this.shopButton = new TextButton("Shop", this.game.getSkin());
        this.quitButton = new TextButton("Quit", this.game.getSkin());
        createButtons();
    }

    /**
     * Initializes the main menu screen.
     * Saves the player's profile, sets input, and plays music.
     */
    @Override
    public void show() {
        this.game.getPlayerProfile().saveProfileState();
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
        addActors(this.game.getButtonsUI(), this.menuItems);
        this.music.setLooping(true);
        this.music.play();
    }

    /**
     * Renders the assets for the main menu screen.
     *
     * @param deltaTime the delta time since the last frame.
     */
    @Override
    public void render(final float deltaTime) {
        final float screenUtilsValueB = 0.2f;
        ScreenUtils.clear(0, 0, screenUtilsValueB, 1);

        this.camera.update();
        this.game.getBatch().setProjectionMatrix(this.camera.combined);
        renderBackground(this.game, this.background);
        final int drawMessageScale = 3;
        final float drawMessageHeightDivisor = 8f / 10f;
        drawMessageFromCenter(createLayout("CROWD SURVIVOR", drawMessageScale), this.game.getBatch(),
                Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * drawMessageHeightDivisor, 2);
        this.game.getButtonsUI().act();
        this.game.getButtonsUI().draw();
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
        clearStage(this.game.getButtonsUI());
        dispose();
    }

    /**
     * Disposes of resources and clears the music.
     */
    @Override
    public void dispose() {
        this.music.dispose();
    }

    private void createButtons() {
        // start game button
        handleStartGameButton();
        this.menuItems[0] = this.startGameButton;

        // shop button
        handleShopButton();
        this.menuItems[1] = this.shopButton;

        // quit game button
        handleQuitButton();
        this.menuItems[2] = this.quitButton;
    }

    private void handleStartGameButton() {
        this.startGameButton.setSize(this.buttonWidth, this.buttonHeight);
        this.startGameButton.setPosition(this.buttonPositionX, this.firstButtonPositionY);
        this.startGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                game.getPlayerProfile().applyPlayerUpgrades(game.getInGameScreen().getPlayer());
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleShopButton() {
        this.shopButton.setSize(this.buttonWidth, this.buttonHeight);
        this.shopButton.setPosition(this.buttonPositionX, this.firstButtonPositionY - this.buttonHeight);
        this.shopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                game.setScreen(game.getShopScreen());
                return true;
            }
        });
    }

    private void handleQuitButton() {
        this.quitButton.setSize(this.buttonWidth, this.buttonHeight);
        this.quitButton.setPosition(this.buttonPositionX, this.firstButtonPositionY - this.buttonHeight * 2);
        this.quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                Gdx.app.exit();
                return true;
            }
        });
    }

    /**
     * Returns a string representation of the MainMenuScreen object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "MainMenuScreen{"
                + "camera=" + this.camera
                + ", game=" + this.game
                + ", music=" + this.music
                + ", background=" + this.background
                + ", menuItems=" + Arrays.toString(this.menuItems)
                + ", startGameButton=" + this.startGameButton
                + ", shopButton=" + this.shopButton
                + ", quitButton=" + this.quitButton
                + ", buttonWidth=" + this.buttonWidth
                + ", buttonHeight=" + this.buttonHeight
                + ", buttonPositionX=" + this.buttonPositionX
                + ", firstButtonPositionY=" + this.firstButtonPositionY
                + '}';
    }
}
