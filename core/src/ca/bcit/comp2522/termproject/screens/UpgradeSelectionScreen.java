package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.interfaces.Background;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Random;

public class UpgradeSelectionScreen implements Screen, ActorManager, Background {
    final private static int BUTTON_COUNT_ON_SCREEN = 4;
    final private CrowdSurvivor game;
    final private Random randomNumberGenerator = new Random();
    final private TextButton[] possibleUpgrades;

    public UpgradeSelectionScreen(CrowdSurvivor game) {
        this.game = game;
        possibleUpgrades = createButtons();
    }

    @Override
    public void show() {
        for (int i = 0; i < BUTTON_COUNT_ON_SCREEN; i++) {
            TextButton chosenUpgrade = chooseRandomButton();
            positionButton(chosenUpgrade, i);
            game.getButtonsUI().addActor(chosenUpgrade);
        }
        Gdx.input.setInputProcessor(game.getButtonsUI());
    }

    @Override
    public void render(float v) {
        game.inGameScreen.renderFrameAsBackground();
        game.getButtonsUI().act();
        game.getButtonsUI().draw();
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

    private TextButton chooseRandomButton() {
        int buttonIndex = randomNumberGenerator.nextInt(possibleUpgrades.length);
        if (game.getButtonsUI().getActors().contains(possibleUpgrades[buttonIndex], true)) {
            return chooseRandomButton();
        }
        return possibleUpgrades[buttonIndex];
    }

    private void positionButton(TextButton button, int buttonNumber) {
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 6;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int buttonPositionY = Gdx.graphics.getHeight() * 2 / 3 - buttonHeight * buttonNumber;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }

    private TextButton[] createButtons() {
        TextButton attackUpgrade = new TextButton("+10 Attack", game.getSkin());
        attackUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setAttack(game.inGameScreen.player.getAttack() + 10);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton speedUpgrade = new TextButton("+10 Speed", game.getSkin());
        speedUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setSpeed(game.inGameScreen.player.getSpeed() + 10);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton healthUpgrade = new TextButton("+10 Health", game.getSkin());

        healthUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setMaxHealth(game.inGameScreen.player.getMaxHealth() + 10);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton EXPUpgrade = new TextButton("+10% More Experience", game.getSkin());
        EXPUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setEXPMultiplier(game.inGameScreen.player.getEXPMultiplier() * (float) 1.1);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton currencyUpgrade = new TextButton("+10% More Currency", game.getSkin());
        currencyUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player
                        .setCurrencyMultiplier(game.inGameScreen.player.getCurrencyMultiplier() * (float) 1.1);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton attackSpeedUpgrade = new TextButton("+10% More Attack Speed", game.getSkin());
        attackSpeedUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setAttackSpeed(game.inGameScreen.player.getAttackSpeed() * (float) 0.9);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton iFrameUpgrade = new TextButton("+10% Longer Invicibility After Taking Damage", game.getSkin());
        iFrameUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setIFramesLength(game.inGameScreen.player.getIFramesLength() * (float) 1.1);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton critDamageUpgrade = new TextButton("+10% Critical Hit Damage", game.getSkin());
        critDamageUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setCritMultiplier(game.inGameScreen.player.getCritMultiplier() + 0.1f);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton critRateUpgrade = new TextButton("+5% Critical Hit Rate", game.getSkin());
        critRateUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setCritRate(game.inGameScreen.player.getCritRate() + 0.05f);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton defenseUpgrade = new TextButton("+5 Defense", game.getSkin());
        defenseUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player.setDefense(game.inGameScreen.player.getDefense() + 0.05f);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        TextButton healthRegenUpgrade = new TextButton("+5% Health Regeneration", game.getSkin());
        healthRegenUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                clearStage(game.getButtonsUI());
                game.inGameScreen.player
                        .setHealthRegenMultiplier(game.inGameScreen.player.getHealthRegenMultiplier() + 0.05f);
                game.setScreen(game.inGameScreen);
                return true;
            }
        });

        return new TextButton[]{
                attackUpgrade, speedUpgrade, healthUpgrade, EXPUpgrade, currencyUpgrade, attackSpeedUpgrade,
                iFrameUpgrade, critDamageUpgrade, critRateUpgrade, defenseUpgrade, healthRegenUpgrade
        };
    }
}
