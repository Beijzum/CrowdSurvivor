package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.ActorManager;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.w3c.dom.Text;
import sun.font.TrueTypeFont;

public class WinScreen implements Screen, ActorManager {

    public CrowdSurvivor game;
    final private TextButton[] menuItems;
    final private Music music;

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

//        TextButton youWinMessage = new TextButton("YOU WIN!", game.skin);
//        TextButton score = new TextButton(String.format("SCORE: %d",
//                        game.inGameScreen.player.getCollectedCurrency() + game.inGameScreen.player.getAccumulatedEXP()),
//                        game.skin);
//
//        youWinMessage.setDisabled(true);
//        score.setDisabled(true);
//        youWinMessage.setSize(buttonWidth * 1.5f, buttonHeight);
//        score.setSize(buttonWidth, buttonHeight);
//        youWinMessage.setPosition(buttonPositionX, Gdx.graphics.getHeight() - buttonHeight);
//        score.setSize(buttonPositionX, Gdx.graphics.getHeight() - buttonHeight - youWinMessage.getHeight());

        return new TextButton[] {playAgain, returnToMenu};
    }

    private void drawWinMessage() {
        game.batch.begin();
        CrowdSurvivor.font.getData().setScale(2.5f);
        CrowdSurvivor.font.draw(game.batch, "YOU WIN!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 200);
        CrowdSurvivor.font.draw(game.batch, String.format("SCORE: %d", game.inGameScreen.player.getCollectedCurrency() + game.inGameScreen.player.getAccumulatedEXP()), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 400);
        CrowdSurvivor.font.getData().setScale(1);
        game.batch.end();
    }

    @Override
    public void show() {
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
        drawWinMessage();
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
        music.dispose();
    }
}
