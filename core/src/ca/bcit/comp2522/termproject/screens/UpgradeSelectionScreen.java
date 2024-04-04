package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Random;

public class UpgradeSelectionScreen implements Screen {
    final int numberOfButtons = 4;
    final private CrowdSurvivor game;
    final private TextButton[] menuItems = new TextButton[numberOfButtons]; // different upgrades to choose from
    final private Random randomNumberGenerator = new Random();
    final private String[] possibleUpgrades = {
            "+10 Speed", // +10
            "+10 Attack", // +10
            "+10 Health", // +10
            "+10% More Experience", // *1.1
            "+10% More Currency", // *1.1
            "+10% Attack Speed", // *1.1
            "+10% Longer Invincibility After Taking Damage", // *1.1
            "+10% Critical Damage Multiplier", // +0.1
            "+5% Critical Hit Rate", // +0.05
            "+5 Defense", // +0.05
            "+5% Health Regeneration" // +0.05
    };

    public UpgradeSelectionScreen(CrowdSurvivor game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

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

    }
}
