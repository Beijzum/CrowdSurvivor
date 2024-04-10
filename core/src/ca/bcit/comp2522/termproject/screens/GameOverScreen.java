package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.interfaces.MessageLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Represents the game over screen displayed when the player loses the game.
 * This screen provides options to try again or return to the main menu.
 * It also displays relevant game statistics such as time elapsed and the player's score.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class GameOverScreen implements Screen, ActorManager, MessageLayout {
    private final int numberOfButtons = 2;
    private final CrowdSurvivor game;
    private final TextButton[] menuItems = new TextButton[numberOfButtons]; // try again, quit
    private final TextButton tryAgainButton;
    private final TextButton quitButton;
    private GlyphLayout[] messageLayouts;
    private final Music music;
    private final Music startOfMusic;
    private final float buttonWidth;
    private final float buttonHeight;
    private final float buttonPositionX;
    private final float firstButtonPositionY;

    /**
     * Constructs an instance of the GameOverScreen class.
     * Initializes the game over music and sound effects.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     */
    public GameOverScreen(final CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/gameOverMusic.mp3"));
        this.startOfMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/gameOverSFX.mp3"));
        final float buttonWidthDivisor = 3f;
        this.buttonWidth = Gdx.graphics.getWidth() / buttonWidthDivisor;
        final float buttonHeightDivisor = 10f;
        this.buttonHeight = Gdx.graphics.getHeight() / buttonHeightDivisor;
        this.buttonPositionX = Gdx.graphics.getWidth() / 2f - this.buttonWidth / 2;
        this.firstButtonPositionY = Gdx.graphics.getHeight() / 2f;
        this.tryAgainButton = new TextButton("Try Again", this.game.getSkin());
        this.quitButton = new TextButton("Return To Menu", this.game.getSkin());
        createButtons();
    }

    private void createButtons() {
        // start game button
        handleTryAgainButton();
        this.menuItems[0] = this.tryAgainButton;

        // quit game button
        handleQuitButton();
        this.menuItems[1] = this.quitButton;
    }

    private GlyphLayout[] createMessageLayout() {
        GlyphLayout youDiedMessage = createLayout("YOU DIED", 2f);
        GlyphLayout gameOverMessage = createLayout("GAME OVER", 2f);
        final int conversionValue = 60;
        GlyphLayout timeElapsedMessage = createLayout(String.format("TIME ELAPSED: %d:%02d",
                Math.round(this.game.getInGameScreen().getTimeElapsed()) / conversionValue,
                Math.round(this.game.getInGameScreen().getTimeElapsed()) % conversionValue), 2f);
        GlyphLayout scoreMessage = createLayout(String.format("SCORE: %d",
                this.game.getInGameScreen().getPlayer().getCollectedCurrency()
                        + this.game.getInGameScreen().getPlayer().getAccumulatedEXP()), 2f);
        return new GlyphLayout[]{youDiedMessage, gameOverMessage, timeElapsedMessage, scoreMessage};
    }

    private void handleTryAgainButton() {
        this.tryAgainButton.setSize(this.buttonWidth, this.buttonHeight);
        this.tryAgainButton.setPosition(this.buttonPositionX, this.firstButtonPositionY);
        this.tryAgainButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                dispose();
                clearStage(game.getButtonsUI());
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleQuitButton() {
        this.quitButton.setSize(this.buttonWidth, this.buttonHeight);
        this.quitButton.setPosition(this.buttonPositionX, this.firstButtonPositionY - this.buttonHeight * 2);
        this.quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                dispose();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().dispose();
                game.setScreen(game.getMainMenuScreen());
                return true;
            }
        });
    }

    /**
     * Initializes the screen when active.
     * Plays the background music and presents messages when initialized.
     */
    @Override
    public void show() {
        this.messageLayouts = createMessageLayout();
        addActors(this.game.getButtonsUI(), this.menuItems);
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
        this.music.setLooping(true);
        this.startOfMusic.play();
    }

    /**
     * Renders the assets for the game over screen.
     *
     * @param deltaTime the delta time since the last frame.
     */
    @Override
    public void render(final float deltaTime) {
        if (!this.startOfMusic.isPlaying()) {
            this.music.play();
        }
        this.game.getInGameScreen().renderFrameAsBackground();
        this.game.getButtonsUI().act();
        this.game.getButtonsUI().draw();
        drawMultipleMessageFromCenter(this.messageLayouts, this.game.getBatch(),
                this.game.getInGameScreen().getCamera().position.x,
                this.game.getInGameScreen().getCamera().position.y + Gdx.graphics.getHeight() / 2f, 2f);
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
     * Adds the currency found during gameplay to the player's profile.
     */
    @Override
    public void hide() {
        this.game.getPlayerProfile().setCurrency(this.game.getPlayerProfile().getCurrency()
                + this.game.getInGameScreen().getPlayer().getCollectedCurrency());
        this.game.getInGameScreen().resetGameState();
    }

    /**
     * Disposes of resources and clears the music.
     */
    @Override
    public void dispose() {
        this.startOfMusic.dispose();
        this.music.dispose();
    }
}
