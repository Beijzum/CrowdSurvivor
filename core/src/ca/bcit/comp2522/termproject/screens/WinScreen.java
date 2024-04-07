package ca.bcit.comp2522.termproject.screens;

import interfaces.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import interfaces.MessageLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class WinScreen implements Screen, ActorManager, MessageLayout {
    public CrowdSurvivor game;
    final private TextButton[] menuItems;
    final private Music music;
    private GlyphLayout[] messageLayouts;

    public WinScreen(CrowdSurvivor game) {
        this.game = game;
        menuItems = createButtons();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/winMusic.mp3"));
    }

    private TextButton[] createButtons() {

        // calculate values for menu placement
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int firstButtonPositionY = Gdx.graphics.getHeight() / 2;

        // play again button
        TextButton playAgain = new TextButton("Play Again", game.skin);
        playAgain.setSize(buttonWidth, buttonHeight);
        playAgain.setPosition(buttonPositionX, firstButtonPositionY);
        playAgain.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.buttonsUI);
                game.setScreen(game.inGameScreen);
                game.inGameScreen.resetGameState();
                dispose();
                return true;
            }
        });

        // return to menu button
        TextButton returnToMenu = new TextButton("Return To Menu", game.skin);
        returnToMenu.setSize(buttonWidth, buttonHeight);
        returnToMenu.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        returnToMenu.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.buttonsUI);
                game.inGameScreen.resetGameState();
                game.setScreen(game.mainMenuScreen);
                dispose();
                return true;
            }
        });
        return new TextButton[] {playAgain, returnToMenu};
    }

    private GlyphLayout[] createMessageLayout() {
        GlyphLayout congratulationMessage = createLayout("CONGRATULATIONS YOU WIN!", 2.5f);
        GlyphLayout timeElapsedMessage = createLayout(String.format("TIME ELAPSED: %d:%02d",
                Math.round(game.inGameScreen.timeElapsed) / 60,
                Math.round(game.inGameScreen.timeElapsed) % 60), 2.5f);
        GlyphLayout scoreMessage = createLayout(String.format("SCORE: %d",
                game.inGameScreen.player.getCollectedCurrency() + game.inGameScreen.player.getAccumulatedEXP()),
                2.5f);
        return new GlyphLayout[] {congratulationMessage, timeElapsedMessage, scoreMessage};
    }

    @Override
    public void show() {
        messageLayouts = createMessageLayout();
        music.setLooping(true);
        music.play();
        addActors(game.buttonsUI, menuItems);
        Gdx.input.setInputProcessor(game.buttonsUI);
    }

    @Override
    public void render(float v) {
        game.inGameScreen.renderFrameAsBackground();
        game.buttonsUI.act();
        game.buttonsUI.draw();
        drawMultipleMessageFromCenter(messageLayouts, game.batch, game.inGameScreen.camera.position.x,
                game.inGameScreen.camera.position.y + Gdx.graphics.getHeight() / 2f, 2.5f);

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
        game.playerProfile.setCurrency(game.playerProfile.getCurrency()
                + game.inGameScreen.player.getCollectedCurrency());
    }

    @Override
    public void dispose() {
        music.dispose();
    }
}
