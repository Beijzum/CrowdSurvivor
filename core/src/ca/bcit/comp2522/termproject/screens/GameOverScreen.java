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

public class GameOverScreen implements Screen, ActorManager, MessageLayout {
    final int numberOfButtons = 2;
    final private CrowdSurvivor game;
    final private TextButton[] menuItems = new TextButton[numberOfButtons]; // try again, quit
    private GlyphLayout[] messageLayouts;
    final private Music music;
    final private Music startOfMusic;

    public GameOverScreen(CrowdSurvivor game) {
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
        TextButton tryAgainButton = new TextButton("Try Again", game.getSkin());
        tryAgainButton.setSize(buttonWidth, buttonHeight);
        tryAgainButton.setPosition(buttonPositionX, firstButtonPositionY);
        tryAgainButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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
        TextButton quitButton = new TextButton("Return To Menu", game.getSkin());
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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
        GlyphLayout timeElapsedMessage = createLayout(String.format("TIME ELAPSED: %d:%02d",
                Math.round(game.getInGameScreen().getTimeElapsed()) / 60,
                Math.round(game.getInGameScreen().getTimeElapsed()) % 60), 2f);
        GlyphLayout scoreMessage = createLayout(String.format("SCORE: %d",
                        game.getInGameScreen().getPlayer().getCollectedCurrency()
                                + game.getInGameScreen().getPlayer().getAccumulatedEXP()),
                2f);
        return new GlyphLayout[]{youDiedMessage, gameOverMessage, timeElapsedMessage, scoreMessage};
    }

    @Override
    public void show() {
        this.messageLayouts = createMessageLayout();
        addActors(game.getButtonsUI(), menuItems);
        Gdx.input.setInputProcessor(game.getButtonsUI());
        music.setLooping(true);
        startOfMusic.play();
    }

    @Override
    public void render(float v) {
        if (!startOfMusic.isPlaying()) {
            music.play();
        }
        game.getInGameScreen().renderFrameAsBackground();
        game.getButtonsUI().act();
        game.getButtonsUI().draw();
        drawMultipleMessageFromCenter(this.messageLayouts, game.getBatch(),
                game.getInGameScreen().getCamera().position.x, game.getInGameScreen().getCamera().position.y
                        + Gdx.graphics.getHeight() / 2f, 2f);
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
        game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency()
                + game.getInGameScreen().getPlayer().getCollectedCurrency());
    }

    @Override
    public void dispose() {
        startOfMusic.dispose();
        music.dispose();
    }
}
