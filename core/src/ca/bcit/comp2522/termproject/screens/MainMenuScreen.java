package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.ActorManager;
import ca.bcit.comp2522.termproject.Background;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen, Background, ActorManager {
    final int numberOfButtons = 3;
    OrthographicCamera camera;
    final private CrowdSurvivor game;
    final private Music music;
    final private Sprite background = new Sprite(new Texture("backgrounds/mainMenuBackground.jpg"));
    final private TextButton[] menuItems = new TextButton[numberOfButtons]; // start game, shop, quit buttons

    public MainMenuScreen(final CrowdSurvivor game) {
        this.camera = new OrthographicCamera();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/mainMenuMusic.mp3"));
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game = game;
        createButtons();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(game.stageUI);
        music.setLooping(true);
        music.play();
        addActors(game.stageUI, menuItems);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        renderBackground(game, background);
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

    // runs when screen is left
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        clearStage(game.stageUI);
        dispose();
    }

    @Override
    public void dispose() {
        music.dispose();
    }

    private void createButtons() {
        Skin buttonSkin = new Skin(Gdx.files.internal("skin/vhs-ui.json"));

        // calculate values for menu placement
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int firstButtonPositionY = Gdx.graphics.getHeight() / 3;

        // start game button
        TextButton startGameButton = new TextButton("Start Game", buttonSkin);
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
        TextButton shopButton = new TextButton("Shop", buttonSkin);
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
        TextButton quitButton = new TextButton("Quit", buttonSkin);
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.setPosition(buttonPositionX, firstButtonPositionY - buttonHeight * 2);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.stageUI);
                Gdx.app.exit();
                return true;
            }
        });
        this.menuItems[2] = quitButton;
    }
}
