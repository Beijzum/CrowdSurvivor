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
 * Represents the win screen when the player successfully completes the game.
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
public class WinScreen implements Screen, ActorManager, MessageLayout {
    private final CrowdSurvivor game;
    private final TextButton[] menuItems;
    private GlyphLayout[] messageLayouts;
    private final Music music;
    private final TextButton playAgain;
    private final TextButton returnToMenu;
    private final float buttonWidth;
    private final float buttonHeight;
    private final float buttonPositionX;
    private final float firstButtonPositionY;

    /**
     * Constructs a win screen for the Crowd Survivor game.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     */
    public WinScreen(final CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/winMusic.mp3"));
        final float buttonWidthDivisor = 3f;
        this.buttonWidth = Gdx.graphics.getWidth() / buttonWidthDivisor;
        final float buttonHeightDivisor = 10f;
        this.buttonHeight = Gdx.graphics.getHeight() / buttonHeightDivisor;
        this.buttonPositionX = Gdx.graphics.getWidth() / 2f - this.buttonWidth / 2;
        this.firstButtonPositionY = Gdx.graphics.getHeight() / 2f;
        this.playAgain = new TextButton("Play Again", this.game.getSkin());
        this.returnToMenu = new TextButton("Return To Menu", this.game.getSkin());
        this.menuItems = createButtons();
    }

    private TextButton[] createButtons() {
        handlePlayAgain();
        handleReturnToMenu();

        return new TextButton[]{this.playAgain, this.returnToMenu};
    }

    private void handlePlayAgain() {
        this.playAgain.setSize(this.buttonWidth, this.buttonHeight);
        this.playAgain.setPosition(this.buttonPositionX, this.firstButtonPositionY);
        this.playAgain.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                dispose();
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleReturnToMenu() {
        this.returnToMenu.setSize(this.buttonWidth, this.buttonHeight);
        this.returnToMenu.setPosition(this.buttonPositionX, this.firstButtonPositionY - this.buttonHeight * 2);
        this.returnToMenu.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.setScreen(game.getMainMenuScreen());
                dispose();
                return true;
            }
        });
    }

    private GlyphLayout[] createMessageLayout() {
        final float glyphLayoutScale = 2.5f;
        final int conversionValue = 60;
        GlyphLayout congratulationMessage = createLayout("CONGRATULATIONS YOU WIN!", glyphLayoutScale);
        GlyphLayout timeElapsedMessage = createLayout(String.format("TIME ELAPSED: %d:%02d",
                Math.round(this.game.getInGameScreen().getTimeElapsed()) / conversionValue,
                Math.round(this.game.getInGameScreen().getTimeElapsed()) % conversionValue), glyphLayoutScale);
        GlyphLayout scoreMessage = createLayout(String.format("SCORE: %d",
                this.game.getInGameScreen().getPlayer().getCollectedCurrency() + this.game.getInGameScreen()
                        .getPlayer().getAccumulatedEXP()), glyphLayoutScale);
        return new GlyphLayout[]{congratulationMessage, timeElapsedMessage, scoreMessage};
    }

    /**
     * Initializes the screen, draws text, and starts playing the background music when active.
     */
    @Override
    public void show() {
        this.messageLayouts = createMessageLayout();
        this.music.setLooping(true);
        this.music.play();
        addActors(this.game.getButtonsUI(), this.menuItems);
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
    }

    /**
     * Renders the assets for the win screen.
     * Handles game logic, drawing, and state transitions.
     *
     * @param deltaTime the delta time since the last frame.
     */
    @Override
    public void render(final float deltaTime) {
        this.game.getInGameScreen().renderFrameAsBackground();
        this.game.getButtonsUI().act();
        this.game.getButtonsUI().draw();
        final float drawMessageScale = 2.5f;
        drawMultipleMessageFromCenter(this.messageLayouts, this.game.getBatch(), this.game.getInGameScreen()
                .getCamera().position.x, this.game.getInGameScreen().getCamera().position.y
                + Gdx.graphics.getHeight() / 2f, drawMessageScale);

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
     * Shows the game score and time elapsed.
     */
    @Override
    public void hide() {
        this.game.getPlayerProfile().setCurrency(this.game.getPlayerProfile().getCurrency()
                + this.game.getInGameScreen().getPlayer().getCollectedCurrency()
                + Math.round(this.game.getInGameScreen().getTimeElapsed() * 2));
        this.game.getInGameScreen().resetGameState();
    }

    /**
     * Disposes of resources and clears the music.
     */
    @Override
    public void dispose() {
        this.music.dispose();
    }
}
