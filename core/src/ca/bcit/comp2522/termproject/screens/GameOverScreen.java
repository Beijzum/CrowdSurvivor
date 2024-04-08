package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
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
 * Represents the Game Over screen displayed when the player loses the game.
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
    private GlyphLayout[] messageLayouts;
    private final Music music;
    private final Music startOfMusic;

    /**
     * Constructs an instance of the GameOverScreen class.
     * Initializes the game over music and sound effects.
     *
     * @param game CrowdSurvivor object representing the game instance.
     */
    public GameOverScreen(final CrowdSurvivor game) {
        this.game = game;
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/gameOverMusic.mp3"));
        this.startOfMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/gameOverSFX.mp3"));
        createButtons();
    }

    private void createButtons() {

        // calculate values for menu placement
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int firstButtonPositionY = Gdx.graphics.getHeight() / 2;

        // start game button
        TextButton tryAgainButton = new TextButton("Try Again", this.game.getSkin());
        tryAgainButton.setSize(buttonWidth, buttonHeight);
        tryAgainButton.setPosition(buttonPositionX, firstButtonPositionY);
        tryAgainButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                dispose();
                game.getInGameScreen().resetGameState();
                clearStage(game.getButtonsUI());
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
        this.menuItems[0] = tryAgainButton;

        // quit game button
        TextButton quitButton = new TextButton("Return To Menu", this.game.getSkin());
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                dispose();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().resetGameState();
                game.getInGameScreen().dispose();
                game.setScreen(game.getMainMenuScreen());
                return true;
            }
        });
        this.menuItems[1] = quitButton;
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

    @Override
    public void render(final float v) {
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
     * It updates the player's total currency in their profile by adding the currency they have collected during the current in-game session.
     * The updated currency value is then set back to the player's profile.
     */
    @Override
    public void hide() {
        this.game.getPlayerProfile().setCurrency(this.game.getPlayerProfile().getCurrency()
                + this.game.getInGameScreen().getPlayer().getCollectedCurrency());
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
