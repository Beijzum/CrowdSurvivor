package ca.bcit.comp2522.termproject.screens;

import ca.bcit.comp2522.termproject.interfaces.ActorManager;
import ca.bcit.comp2522.termproject.interfaces.Background;
import ca.bcit.comp2522.termproject.CrowdSurvivor;
import ca.bcit.comp2522.termproject.interfaces.MessageLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Arrays;

/**
 * Represents the shop screen where players can purchase various upgrades and items using in-game currency.
 * This screen provides a selection of upgrades to enhance their character.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class ShopScreen implements Screen, Background, ActorManager, MessageLayout {
    private static final int BASE_ITEM_PRICE = 100;
    private final CrowdSurvivor game;
    private final Music music;
    private final TextButton[] menuItems;
    private final Color backgroundFilter = new Color(50 / 255f, 50 / 255f, 100 / 255f, 1);
    private final Sprite background = new Sprite(new Texture("backgrounds/shopMenuBackground.jpg"));
    private final GlyphLayout shopMessage;
    private final GlyphLayout currencyMessage;
    private final Sound purchaseSFX;
    private final Sound failPurchaseSFX;
    private final TextButton buyAttack;
    private final TextButton buyHealth;
    private final TextButton buySpeed;
    private final TextButton buyDefense;
    private final TextButton buyEXP;
    private final TextButton buyCurrency;
    private final TextButton buyAttackSpeed;
    private final TextButton buyCritRate;
    private final TextButton buyCritDamage;
    private final TextButton buyHealthRegen;
    private final TextButton backButton;
    private final int buyAttackPrice = BASE_ITEM_PRICE;
    private final int buyHealthPrice = BASE_ITEM_PRICE;
    private final int buySpeedPrice = BASE_ITEM_PRICE;
    private final int buyDefensePrice = Math.round(BASE_ITEM_PRICE * 1.5f);
    private final int buyEXPPrice = BASE_ITEM_PRICE * 2;
    private final int buyCurrencyPrice = BASE_ITEM_PRICE * 2;
    private final int buyAttackSpeedPrice = Math.round(BASE_ITEM_PRICE * 1.75f);
    private final int buyCritRatePrice = BASE_ITEM_PRICE * 4;
    private final int buyCritDamagePrice = BASE_ITEM_PRICE * 3;
    private final int buyHealthRegenPrice = BASE_ITEM_PRICE * 5;

    /**
     * Constructs a shop screen for the Crowd Survivor game.
     *
     * @param crowdSurvivor CrowdSurvivor object representing the game instance.
     */
    public ShopScreen(final CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/shopMenuMusic.mp3"));
        this.shopMessage = createLayout("SHOP", 2f);
        this.currencyMessage = new GlyphLayout();
        this.buyAttack = new TextButton(String.format("$%d - Buy Attack", this.buyAttackPrice), this.game.getSkin());
        this.buyHealth = new TextButton(String.format("$%d - Buy Health", this.buyHealthPrice), this.game.getSkin());
        this.buySpeed = new TextButton(String.format("$%d - Buy Speed", this.buySpeedPrice), this.game.getSkin());
        this.buyDefense = new TextButton(String.format("$%d - Buy Defense", this.buyDefensePrice), this.game.getSkin());
        this.buyEXP = new TextButton(String.format("$%d - Buy Base EXP Gain", this.buyEXPPrice), this.game.getSkin());
        this.buyCurrency = new TextButton(String.format("$%d - Buy Base Currency Gain", this.buyCurrencyPrice),
                this.game.getSkin());
        this.buyAttackSpeed = new TextButton(String.format("$%d - Buy Attack Speed", this.buyAttackSpeedPrice),
                this.game.getSkin());
        this.buyCritRate = new TextButton(String.format("$%d - Buy Critical Hit Rate", this.buyCritRatePrice),
                this.game.getSkin());
        this.buyCritDamage = new TextButton(String.format("$%d - Buy Critical Hit Damage", this.buyCritDamagePrice),
                this.game.getSkin());
        this.buyHealthRegen = new TextButton(String.format("$%d - Buy Health Regeneration", this.buyHealthRegenPrice),
                this.game.getSkin());
        this.backButton = new TextButton("Back To Menu", this.game.getSkin());
        this.menuItems = createButtons();
        this.purchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/purchaseSFX.mp3"));
        this.failPurchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/failPurchaseSFX.mp3"));
        positionMenuItems();
    }

    /**
     * Initializes the shop screen.
     * Sets the input processor, plays background music, and adds menu items to the UI stage.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
        this.music.setLooping(true);
        this.music.play();
        addActors(this.game.getButtonsUI(), this.menuItems);
    }

    /**
     * Renders the assets for the shop screen.
     *
     * @param deltaTime the delta time since last frame.
     */
    @Override
    public void render(final float deltaTime) {
        final float screenUtilsValueB = 0.2f;
        ScreenUtils.clear(0, 0, screenUtilsValueB, 1);

        setTextAndScale(this.currencyMessage, String.format("CURRENCY: $%d",
                this.game.getPlayerProfile().getCurrency()), 2f);

        renderBackgroundWithFilter(this.game, this.background, this.backgroundFilter);

        final int drawMessageX = 20;
        final float drawMessageDivisor = 1.01f;
        drawMessage(this.shopMessage, this.game.getBatch(), drawMessageX,
                Gdx.graphics.getHeight() / drawMessageDivisor, 2f);
        final int drawMessageHeightAdjuster = 20;
        drawMessage(this.currencyMessage, this.game.getBatch(),
                drawMessageX, Gdx.graphics.getHeight() / drawMessageDivisor - this.shopMessage.height
                        - drawMessageHeightAdjuster, 2f);

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
        clearStage(this.game.getButtonsUI());
        dispose();
    }

    /**
     * Disposes of resources and clears the music.
     */
    @Override
    public void dispose() {
        this.music.dispose();
    }

    private TextButton[] createButtons() {
        handleBuyAttack();
        handleBuyHealth();
        handleBuySpeed();
        handleBuyDefense();
        handleBuyEXP();
        handleBuyCurrency();
        handleBuyAttackSpeed();
        handleBuyCritRate();
        handleBuyCritDamage();
        handleBuyHealthRegen();
        handleBackButton();

        positionBackButton(this.backButton);
        return new TextButton[]{
                this.buyAttack, this.buySpeed, this.buyHealth, this.buyHealthRegen, this.buyEXP, this.buyAttackSpeed,
                this.buyDefense, this.buyCritRate, this.buyCritDamage, this.buyCurrency, this.backButton
        };
    }

    private void handleBuyAttack() {
        this.buyAttack.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyAttackPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final int upgradeAttackValue = 5;
                    game.getPlayerProfile().setAttackBooster(game.getPlayerProfile()
                            .getAttackBooster() + upgradeAttackValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyAttackPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyHealth() {
        buyHealth.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyHealthPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final int upgradeHealthValue = 5;
                    game.getPlayerProfile().setMaxHealthBooster(game.getPlayerProfile()
                            .getMaxHealthBooster() + upgradeHealthValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyHealthPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuySpeed() {
        buySpeed.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buySpeedPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final int upgradeSpeedValue = 5;
                    game.getPlayerProfile().setSpeedBooster(game.getPlayerProfile()
                            .getSpeedBooster() + upgradeSpeedValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buySpeedPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyDefense() {
        buyDefense.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyDefensePrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final float upgradeDefenseValue = 0.1f;
                    game.getPlayerProfile().setDefenseBooster(game.getPlayerProfile()
                            .getDefenseBooster() + upgradeDefenseValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyDefensePrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyEXP() {
        buyEXP.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyEXPPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final float upgradeEXPValue = 0.05f;
                    game.getPlayerProfile().setEXPMultiplierBooster(game.getPlayerProfile()
                            .getEXPMultiplierBooster() + upgradeEXPValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyEXPPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyCurrency() {
        buyCurrency.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyCurrencyPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final float upgradeCurrencyValue = 0.05f;
                    game.getPlayerProfile()
                            .setCurrencyMultiplierBooster(game.getPlayerProfile()
                                    .getCurrencyMultiplierBooster() + upgradeCurrencyValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyCurrencyPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyAttackSpeed() {
        buyAttackSpeed.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyAttackSpeedPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final float upgradeAttackSpeedValue = 0.05f;
                    game.getPlayerProfile().setAttackSpeedBooster(game.getPlayerProfile()
                            .getAttackSpeedBooster() - upgradeAttackSpeedValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyAttackSpeedPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyCritRate() {
        buyCritRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyCritRatePrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final float upgradeCritRateValue = 0.02f;
                    game.getPlayerProfile().setCritRateBooster(game.getPlayerProfile()
                            .getCritRateBooster() + upgradeCritRateValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyCritRatePrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBuyCritDamage() {
        buyCritDamage.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyCritDamagePrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                final float upgradeCritDamageValue = 0.05f;
                game.getPlayerProfile().setCritMultiplierBooster(game.getPlayerProfile()
                        .getCritMultiplierBooster() + upgradeCritDamageValue);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyCritDamagePrice);
                return true;
            }
        });
    }

    private void handleBuyHealthRegen() {
        buyHealthRegen.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button == Input.Buttons.LEFT) {
                    if (game.getPlayerProfile().getCurrency() < buyHealthRegenPrice) {
                        final float sfxValue = 0.5f;
                        failPurchaseSFX.play(sfxValue);
                        return true;
                    }
                    purchaseSFX.play();
                    final float upgradeHealthRegenValue = 0.02f;
                    game.getPlayerProfile().setHealthRegenMultiplierBooster(game.getPlayerProfile()
                            .getHealthRegenMultiplierBooster() + upgradeHealthRegenValue);
                    game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyHealthRegenPrice);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void handleBackButton() {
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                game.getButtonClickSFX().play();
                dispose();
                game.setScreen(game.getMainMenuScreen());
                return true;
            }
        });
    }

    private void positionMenuItems() {
        for (int col = 0; col < 2; col++) {
            final int rowNum = 5;
            final int rowMultiplier = 4;
            for (int row = 0; row < rowNum; row++) {
                positionItemButton(this.menuItems[col + row + rowMultiplier * col], col, row);
            }
        }
    }

    private void positionBackButton(final TextButton button) {
        final int buttonWidth = Gdx.graphics.getWidth() / 2;
        final int buttonHeight = Gdx.graphics.getHeight() / 10;
        final int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        final int buttonPositionY = buttonHeight / 2;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }

    private void positionItemButton(final TextButton button, final int col, final int row) {
        // col max = 1, row max = 4
        final int buttonWidth = Gdx.graphics.getWidth() / 3;
        final int buttonHeight = Gdx.graphics.getHeight() / 10;
        final int buttonPositionX = Gdx.graphics.getWidth() / 4 - buttonWidth / 2 + Gdx.graphics.getWidth() / 2 * col;
        final int buttonPositionY = Gdx.graphics.getHeight() / 4 + buttonHeight * row;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }

    /**
     * Returns a string representation of the ShopScreen object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "ShopScreen{"
                + "game=" + this.game
                + ", music=" + this.music
                + ", menuItems=" + Arrays.toString(this.menuItems)
                + ", backgroundFilter=" + this.backgroundFilter
                + ", background=" + this.background
                + ", shopMessage=" + this.shopMessage
                + ", currencyMessage=" + this.currencyMessage
                + ", purchaseSFX=" + this.purchaseSFX
                + ", failPurchaseSFX=" + this.failPurchaseSFX
                + ", buyAttack=" + this.buyAttack
                + ", buyHealth=" + this.buyHealth
                + ", buySpeed=" + this.buySpeed
                + ", buyDefense=" + this.buyDefense
                + ", buyEXP=" + this.buyEXP
                + ", buyCurrency=" + this.buyCurrency
                + ", buyAttackSpeed=" + this.buyAttackSpeed
                + ", buyCritRate=" + this.buyCritRate
                + ", buyCritDamage=" + this.buyCritDamage
                + ", buyHealthRegen=" + this.buyHealthRegen
                + ", backButton=" + this.backButton
                + ", buyAttackPrice=" + this.buyAttackPrice
                + ", buyHealthPrice=" + this.buyHealthPrice
                + ", buySpeedPrice=" + this.buySpeedPrice
                + ", buyDefensePrice=" + this.buyDefensePrice
                + ", buyEXPPrice=" + this.buyEXPPrice
                + ", buyCurrencyPrice=" + this.buyCurrencyPrice
                + ", buyAttackSpeedPrice=" + this.buyAttackSpeedPrice
                + ", buyCritRatePrice=" + this.buyCritRatePrice
                + ", buyCritDamagePrice=" + this.buyCritDamagePrice
                + ", buyHealthRegenPrice=" + this.buyHealthRegenPrice
                + '}';
    }
}
