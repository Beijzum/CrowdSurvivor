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
    private static final int BUTTON_COUNT_ON_SCREEN = 4;
    private final CrowdSurvivor game;
    private final Random randomNumberGenerator = new Random();
    private final TextButton[] possibleUpgrades;
    private final TextButton attackUpgrade;
    private final TextButton speedUpgrade;
    private final TextButton healthUpgrade;
    private final TextButton expUpgrade;
    private final TextButton currencyUpgrade;
    private final TextButton attackSpeedUpgrade;
    private final TextButton iFrameUpgrade;
    private final TextButton critDamageUpgrade;
    private final TextButton critRateUpgrade;
    private final TextButton defenseUpgrade;
    private final TextButton healthRegenUpgrade;

    public UpgradeSelectionScreen(final CrowdSurvivor game) {
        this.game = game;
        this.attackUpgrade = new TextButton("+10 Attack", this.game.getSkin());
        this.speedUpgrade = new TextButton("+10 Speed", this.game.getSkin());
        this.healthUpgrade = new TextButton("+10 Health", this.game.getSkin());
        this.expUpgrade = new TextButton("+10% More Experience", this.game.getSkin());
        this.currencyUpgrade = new TextButton("+10% More Currency", this.game.getSkin());
        this.attackSpeedUpgrade = new TextButton("+10% More Attack Speed", this.game.getSkin());
        this.iFrameUpgrade = new TextButton("+10% Longer Invincibility After Taking Damage", this.game.getSkin());
        this.critDamageUpgrade = new TextButton("+10% Critical Hit Damage", this.game.getSkin());
        this.critRateUpgrade = new TextButton("+5% Critical Hit Rate", this.game.getSkin());
        this.defenseUpgrade = new TextButton("+5 Defense", this.game.getSkin());
        this.healthRegenUpgrade = new TextButton("+5% Health Regeneration", this.game.getSkin());
        this.possibleUpgrades = createButtons();
    }

    @Override
    public void show() {
        for (int i = 0; i < BUTTON_COUNT_ON_SCREEN; i++) {
            TextButton chosenUpgrade = chooseRandomButton();
            positionButton(chosenUpgrade, i);
            this.game.getButtonsUI().addActor(chosenUpgrade);
        }
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
    }

    @Override
    public void render(float v) {
        this.game.getInGameScreen().renderFrameAsBackground();
        this.game.getButtonsUI().act();
        this.game.getButtonsUI().draw();
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
        int buttonIndex = this.randomNumberGenerator.nextInt(this.possibleUpgrades.length);
        if (this.game.getButtonsUI().getActors().contains(this.possibleUpgrades[buttonIndex], true)) {
            return chooseRandomButton();
        }
        return this.possibleUpgrades[buttonIndex];
    }

    private void positionButton(final TextButton button, final int buttonNumber) {
        int buttonWidth = Gdx.graphics.getWidth() / 2;
        int buttonHeight = Gdx.graphics.getHeight() / 6;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int buttonPositionY = Gdx.graphics.getHeight() * 2 / 3 - buttonHeight * buttonNumber;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }

    private TextButton[] createButtons() {

        handleAttackUpgrade();
        handleSpeedUpgrade();
        handleHealthUpgrade();
        handleExpUpgrade();
        handleCurrencyUpgrade();
        handleAttackSpeedUpgrade();
        handleCritDamageUpgrade();
        handleCritRateUpgrade();
        handleDefenseUpgrade();
        handleHealthRegenUpgrade();

        return new TextButton[]{
                this.attackUpgrade, this.speedUpgrade, this.healthUpgrade, this.expUpgrade, this.currencyUpgrade,
                this.attackSpeedUpgrade, this.iFrameUpgrade, this.critDamageUpgrade, this.critRateUpgrade,
                this.defenseUpgrade, this.healthRegenUpgrade
        };
    }

    private void handleAttackUpgrade() {
        this.attackUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setAttack(game.getInGameScreen().getPlayer().getAttack() + 10);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleSpeedUpgrade() {
        this.speedUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setSpeed(game.getInGameScreen().getPlayer().getSpeed() + 10);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleHealthUpgrade() {
        this.healthUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setMaxHealth(game.getInGameScreen().getPlayer().getMaxHealth() + 10);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleExpUpgrade() {
        this.expUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setEXPMultiplier(game.getInGameScreen().getPlayer().getEXPMultiplier() * (float) 1.1);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleCurrencyUpgrade() {
        this.currencyUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setCurrencyMultiplier(game.getInGameScreen().getPlayer().getCurrencyMultiplier() * (float) 1.1);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleAttackSpeedUpgrade() {
        this.attackSpeedUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setAttackSpeed(game.getInGameScreen().getPlayer().getAttackSpeed() * (float) 0.9);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleIFrameUpgrade() {
        this.iFrameUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setIFramesLength(game.getInGameScreen().getPlayer().getIFramesLength() * (float) 1.1);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleCritDamageUpgrade() {
        this.critDamageUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setCritMultiplier(game.getInGameScreen().getPlayer().getCritMultiplier() + 0.1f);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleCritRateUpgrade() {
        this.critRateUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setCritRate(game.getInGameScreen().getPlayer().getCritRate() + 0.05f);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleDefenseUpgrade() {
        this.defenseUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setDefense(game.getInGameScreen().getPlayer().getDefense() + 0.05f);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

    private void handleHealthRegenUpgrade() {
        this.healthRegenUpgrade.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                clearStage(game.getButtonsUI());
                game.getInGameScreen().getPlayer().setHealthRegenMultiplier(game.getInGameScreen().getPlayer().getHealthRegenMultiplier() + 0.05f);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

}
