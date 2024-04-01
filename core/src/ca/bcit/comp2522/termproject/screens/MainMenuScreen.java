package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.ActorManager;
import ca.bcit.comp2522.termproject.Background;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
        camera.setToOrtho(false, 800, 400);
        this.game = game;
        createButtons();
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();
        createButtons();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        renderBackground(game, background);
        game.stage.act();
        game.stage.draw();
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

    private void createButtons() {
        Skin buttonSkin = new Skin(Gdx.files.internal("skin/vhs-ui.json"));

        // start game button
        TextButton startGameButton = new TextButton("Start Game", buttonSkin);
        startGameButton.setSize(game.viewportX / 3, game.viewportY / 10);
        startGameButton.setPosition(game.viewportX / 5, game.viewportY / 5);
        startGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.loadoutMenuScreen);
                clearStage(game.stage);
                System.out.println("Clicked");
                return true;
            }
        });
        this.menuItems[0] = startGameButton;

        // shop button
        TextButton shopButton = new TextButton("Shop", buttonSkin);
        shopButton.setSize(game.viewportX / 3, game.viewportY / 10);
        shopButton.setPosition(game.viewportX / 5, game.viewportY / 10);
        shopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.shopScreen);
                clearStage(game.stage);
                return true;
            }
        });
        this.menuItems[1] = shopButton;

        // quit game button
        TextButton quitButton = new TextButton("Quit", buttonSkin);
        quitButton.setSize(game.viewportX / 3, game.viewportY / 10);
        quitButton.setPosition(game.viewportX / 5, game.viewportY / 15);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clearStage(game.stage);
                dispose();
                game.dispose();
                Gdx.app.exit();
                return true;
            }
        });
        this.menuItems[2] = quitButton;

        for (TextButton menuItem : this.menuItems) {
            game.stage.addActor(menuItem);
        }
    }

    private void removeActors(Stage stage) {

    }

    @Override
    public void addActor(Actor actor) {
        game.stage.addActor(actor);
    }
}
