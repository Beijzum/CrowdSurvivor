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

/**
 * Represents the upgrade selection screen where players can select various upgrades after leveling up.
 * This screen displays a selection of four random upgrade buttons from which the player can choose.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
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

    /**
     * Constructs an upgrade selection screen for the Crowd Survivor game.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     */
    public UpgradeSelectionScreen(final CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.attackUpgrade = new TextButton("+10 Attack", this.game.getSkin());
        this.speedUpgrade = new TextButton("+10 Speed", this.game.getSkin());
        this.healthUpgrade = new TextButton("+10 Health", this.game.getSkin());
        this.expUpgrade = new TextButton("+10% Experience Gain", this.game.getSkin());
        this.currencyUpgrade = new TextButton("+10% Currency Gain", this.game.getSkin());
        this.attackSpeedUpgrade = new TextButton("+10% Attack Speed", this.game.getSkin());
        this.iFrameUpgrade = new TextButton("+10% Longer Invincibility Frames", this.game.getSkin());
        this.critDamageUpgrade = new TextButton("+10% Critical Hit Damage", this.game.getSkin());
        this.critRateUpgrade = new TextButton("+5% Critical Hit Rate", this.game.getSkin());
        this.defenseUpgrade = new TextButton("+5% Defense", this.game.getSkin());
        this.healthRegenUpgrade = new TextButton("+5% Health Regeneration", this.game.getSkin());
        this.possibleUpgrades = createButtons();
    }

    /**
     * Initializes the upgrade selection screen.
     * Randomly selects and positions a set number of upgrade buttons on the screen.
     */
    @Override
    public void show() {
        for (int i = 0; i < BUTTON_COUNT_ON_SCREEN; i++) {
            TextButton chosenUpgrade = chooseRandomButton();
            positionButton(chosenUpgrade, i);
            this.game.getButtonsUI().addActor(chosenUpgrade);
        }
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
    }

    /**
     * Renders the assets for the upgrade selection screen.
     *
     * @param deltaTime The time passed since the last frame.
     */
    @Override
    public void render(final float deltaTime) {
        this.game.getInGameScreen().renderFrameAsBackground();
        this.game.getButtonsUI().act();
        this.game.getButtonsUI().draw();
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
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes of resources.
     */
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
        final int buttonHeightDivisor = 6;
        int buttonHeight = Gdx.graphics.getHeight() / buttonHeightDivisor;
        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        final int buttonPosYDivisor = 3;
        int buttonPositionY = Gdx.graphics.getHeight() * 2 / buttonPosYDivisor - buttonHeight * buttonNumber;

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
        handleIFrameUpgrade();
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
                final int attackValue = 10;
                game.getInGameScreen().getPlayer().setAttack(game.getInGameScreen().getPlayer()
                        .getAttack() + attackValue);
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
                final int speedValue = 10;
                game.getInGameScreen().getPlayer().setSpeed(game.getInGameScreen().getPlayer()
                        .getSpeed() + speedValue);
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
                final int healthValue = 10;
                game.getInGameScreen().getPlayer().setMaxHealth(game.getInGameScreen().getPlayer()
                        .getMaxHealth() + healthValue);
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
                final float expValue = 1.1f;
                game.getInGameScreen().getPlayer().setEXPMultiplier(game.getInGameScreen().getPlayer()
                        .getEXPMultiplier() * expValue);
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
                final float currencyValue = 1.1f;
                game.getInGameScreen().getPlayer().setCurrencyMultiplier(game.getInGameScreen().getPlayer()
                        .getCurrencyMultiplier() * currencyValue);
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
                final float attackSpeedValue = 0.9f;
                game.getInGameScreen().getPlayer().setAttackSpeed(game.getInGameScreen().getPlayer()
                        .getAttackSpeed() * attackSpeedValue);
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
                final float iFramesValue = 1.1f;
                game.getInGameScreen().getPlayer().setIFramesLength(game.getInGameScreen().getPlayer()
                        .getIFramesLength() * iFramesValue);
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
                final float critDamageValue = 0.1f;
                game.getInGameScreen().getPlayer().setCritMultiplier(game.getInGameScreen().getPlayer()
                        .getCritMultiplier() + critDamageValue);
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
                final float critRateValue = 0.05f;
                game.getInGameScreen().getPlayer().setCritRate(game.getInGameScreen().getPlayer()
                        .getCritRate() + critRateValue);
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
                final float defenseValue = 0.05f;
                game.getInGameScreen().getPlayer().setDefense(game.getInGameScreen().getPlayer()
                        .getDefense() + defenseValue);
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
                final float healthRegenValue = 0.05f;
                game.getInGameScreen().getPlayer().setHealthRegenMultiplier(game.getInGameScreen().getPlayer()
                        .getHealthRegenMultiplier() + healthRegenValue);
                game.setScreen(game.getInGameScreen());
                return true;
            }
        });
    }

}
