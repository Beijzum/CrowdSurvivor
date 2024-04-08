package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseMenuScreen implements Screen, ActorManager, InputProcessor {
    private final int numberOfButtons = 2;
    private final CrowdSurvivor game;
    private final TextButton[] menuItems = new TextButton[numberOfButtons]; // resume, quit
    private final InputMultiplexer inputManager;
    private final TextButton resumeGameButton;
    private final TextButton quitButton;
    private final float buttonWidth;
    private final float buttonHeight;
    private final float buttonPositionX;
    private final float firstButtonPositionY;

    public PauseMenuScreen(final CrowdSurvivor game) {
        this.game = game;
        this.inputManager = new InputMultiplexer(this, this.game.getButtonsUI());
        final float buttonWidthDivisor = 3f;
        this.buttonWidth = Gdx.graphics.getWidth() / buttonWidthDivisor;
        final float buttonHeightDivisor = 10f;
        this.buttonHeight = Gdx.graphics.getHeight() / buttonHeightDivisor;
        this.buttonPositionX = Gdx.graphics.getWidth() / 2f - this.buttonWidth / 2;
        this.firstButtonPositionY = Gdx.graphics.getHeight() / 2f;
        this.resumeGameButton = new TextButton("Resume", this.game.getSkin());
        this.quitButton = new TextButton("Quit", this.game.getSkin());
        createButtons();
    }

    @Override
    public void show() {
        addActors(this.game.getButtonsUI(), this.menuItems);
        Gdx.input.setInputProcessor(this.inputManager);
    }

    @Override
    public void render(final float deltaTime) {
        this.game.getInGameScreen().renderFrameAsBackground();
        this.game.getButtonsUI().act();
        this.game.getButtonsUI().draw();
    }

    @Override
    public void resize(final int i, final int i1) {

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
        // resume game button
        handleResumeGameButton();
        this.menuItems[0] = this.resumeGameButton;

        // quit game button
        handleQuitButton();
        this.menuItems[1] = this.quitButton;
    }

    private void handleResumeGameButton() {
        this.resumeGameButton.setSize(this.buttonWidth, this.buttonHeight);
        this.resumeGameButton.setPosition(this.buttonPositionX, this.firstButtonPositionY);
        this.resumeGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
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
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency()
                        + game.getInGameScreen().getPlayer().getCollectedCurrency());
                game.getInGameScreen().resetGameState();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().dispose();
                game.setScreen(game.getMainMenuScreen());
                return true;
            }
        });
    }

    /**
     * Handles the key press events.
     *
     * @param keyCode the key code of the pressed key.
     * @return true if the event was handled, otherwise false.
     */
    @Override
    public boolean keyDown(final int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            this.game.setScreen(this.game.getInGameScreen());
            return true;
        }
        return false;
    }

    /**
     * Handles the key release events.
     *
     * @param keyCode the key code of the released key.
     * @return false if the event was handled.
     */
    @Override
    public boolean keyUp(final int keyCode) {
        return false;
    }

    /**
     * Handles the typed key events.
     *
     * @param character the character of the typed key.
     * @return false if the event was handled.
     */
    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    /**
     * Handles the touch-down events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @param button  the button.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchDown(final int touchX, final int touchY, final int pointer, final int button) {
        return false;
    }

    /**
     * Handles the touch-up events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @param button  the button.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchUp(final int touchX, final int touchY, final int pointer, final int button) {
        return false;
    }

    /**
     * Handles the touch-cancelled events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @param button  the button.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchCancelled(final int touchX, final int touchY, final int pointer, final int button) {
        return false;
    }

    /**
     * Handles the touch-dragged events.
     *
     * @param touchX  the x-coordinate of the touch.
     * @param touchY  the y-coordinate of the touch.
     * @param pointer the pointer.
     * @return false if the event was handled.
     */
    @Override
    public boolean touchDragged(final int touchX, final int touchY, final int pointer) {
        return false;
    }

    /**
     * Handles the mouse-moved events.
     *
     * @param mouseX the x-coordinate of the mouse.
     * @param mouseY the y-coordinate of the mouse.
     * @return false if the event was handled.
     */
    @Override
    public boolean mouseMoved(final int mouseX, final int mouseY) {
        return false;
    }

    /**
     * Handles the scrolled events.
     *
     * @param scrollHorizontal the horizontal scrolling amount.
     * @param scrollVertical   the vertical scrolling amount.
     * @return false if the event was handled.
     */
    @Override
    public boolean scrolled(final float scrollHorizontal, final float scrollVertical) {
        return false;
    }
}
