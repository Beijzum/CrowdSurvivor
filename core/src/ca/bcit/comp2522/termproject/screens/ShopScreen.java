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
import org.w3c.dom.Text;

public class ShopScreen implements Screen, Background, ActorManager, MessageLayout {
    private static final int BASE_ITEM_PRICE = 500;
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
    private final int buyAttackPrice;
    private final int buyHealthPrice;
    private final int buySpeedPrice;
    private final int buyDefensePrice;
    private final int buyEXPPrice;
    private final int buyCurrencyPrice;
    private final int buyAttackSpeedPrice;
    private final int buyCritRatePrice;
    private final int buyCritDamagePrice;
    private final int buyHealthRegenPrice;

    public ShopScreen(final CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/shopMenuMusic.mp3"));
        this.shopMessage = createLayout("SHOP", 2f);
        this.currencyMessage = new GlyphLayout();
        this.buyAttackPrice = BASE_ITEM_PRICE;
        this.buyHealthPrice = BASE_ITEM_PRICE;
        this.buySpeedPrice = BASE_ITEM_PRICE;
        this.buyDefensePrice = Math.round(BASE_ITEM_PRICE * 1.5f);
        this.buyEXPPrice = BASE_ITEM_PRICE;
        this.buyCurrencyPrice = BASE_ITEM_PRICE * 2;
        this.buyAttackSpeedPrice = Math.round(BASE_ITEM_PRICE * 1.75f);
        this.buyCritRatePrice = BASE_ITEM_PRICE * 2;
        this.buyCritDamagePrice = BASE_ITEM_PRICE * 2;
        this.buyHealthRegenPrice = BASE_ITEM_PRICE;
        this.buyAttack = new TextButton(String.format("$%d - Buy Attack", this.buyAttackPrice), this.game.getSkin());
        this.buyHealth = new TextButton(String.format("$%d - Buy Health", this.buyHealthPrice), this.game.getSkin());
        this.buySpeed = new TextButton(String.format("$%d - Buy Speed", this.buySpeedPrice), this.game.getSkin());
        this.buyDefense = new TextButton(String.format("$%d - Buy Defense", this.buyDefensePrice), this.game.getSkin());
        this.buyEXP = new TextButton(String.format("$%d - Buy Base EXP Gain", this.buyEXPPrice), this.game.getSkin());
        this.buyCurrency = new TextButton(String.format("$%d - Buy Base Currency Gain", this.buyCurrencyPrice), this.game.getSkin());
        this.buyAttackSpeed = new TextButton(String.format("$%d - Buy Attack Speed", this.buyAttackSpeedPrice), this.game.getSkin());
        this.buyCritRate = new TextButton(String.format("$%d - Buy Critical Hit Rate", this.buyCritRatePrice), this.game.getSkin());
        this.buyCritDamage = new TextButton(String.format("$%d - Buy Critical Hit Damage", this.buyCritDamagePrice), this.game.getSkin());
        this.buyHealthRegen = new TextButton(String.format("$%d - Buy Health Regeneration", this.buyHealthRegenPrice), this.game.getSkin());
        this.backButton = new TextButton("Back To Menu", this.game.getSkin());
        this.menuItems = createButtons();
        this.purchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/purchaseSFX.mp3"));
        this.failPurchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/failPurchaseSFX.mp3"));
        positionMenuItems();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.game.getButtonsUI());
        this.music.setLooping(true);
        this.music.play();
        addActors(this.game.getButtonsUI(), this.menuItems);
    }

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
        this.buyAttack.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyAttackPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setAttackBooster(game.getPlayerProfile().getAttackBooster() + 5);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyAttackPrice);
                return true;
            }
        });


        buyHealth.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyHealthPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setMaxHealthBooster(game.getPlayerProfile().getMaxHealthBooster() + 5);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyHealthPrice);
                return true;
            }
        });


        buySpeed.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buySpeedPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setSpeedBooster(game.getPlayerProfile().getSpeedBooster() + 5);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buySpeedPrice);
                return true;
            }
        });


        buyDefense.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyDefensePrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setDefenseBooster(game.getPlayerProfile().getDefenseBooster() + 0.02f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyDefensePrice);
                return true;
            }
        });


        buyEXP.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyEXPPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setEXPMultiplierBooster(game.getPlayerProfile().getEXPMultiplierBooster() + 0.05f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyEXPPrice);
                return true;
            }
        });


        buyCurrency.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyCurrencyPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile()
                        .setCurrencyMultiplierBooster(game.getPlayerProfile().getCurrencyMultiplierBooster() + 0.05f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyCurrencyPrice);
                return true;
            }
        });


        buyAttackSpeed.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyAttackSpeedPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setAttackSpeedBooster(game.getPlayerProfile().getAttackSpeedBooster() - 0.05f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyAttackSpeedPrice);
                return true;
            }
        });


        buyCritRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyCritRatePrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile().setCritRateBooster(game.getPlayerProfile().getCritRateBooster() + 0.02f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyCritRatePrice);
                return true;
            }
        });


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
                game.getPlayerProfile().setCritMultiplierBooster(game.getPlayerProfile().getCritMultiplierBooster() + 0.05f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyCritDamagePrice);
                return true;
            }
        });


        buyHealthRegen.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                                     final int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.getPlayerProfile().getCurrency() < buyHealthRegenPrice) {
                    final float sfxValue = 0.5f;
                    failPurchaseSFX.play(sfxValue);
                    return true;
                }
                purchaseSFX.play();
                game.getPlayerProfile()
                        .setHealthRegenMultiplierBooster(game.getPlayerProfile().getHealthRegenMultiplierBooster() + 0.02f);
                game.getPlayerProfile().setCurrency(game.getPlayerProfile().getCurrency() - buyHealthPrice);
                return true;
            }
        });


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

        positionBackButton(backButton);
        return new TextButton[]{
                buyAttack, buySpeed, buyHealth, buyHealthRegen, buyEXP, buyAttackSpeed, buyDefense, buyCritRate,
                buyCritDamage, buyCurrency, backButton
        };
    }

    private void positionMenuItems() {
        for (int col = 0; col < 2; col++) {
            for (int row = 0; row < 5; row++) {
                positionItemButton(menuItems[col + row + 4 * col], col, row);
            }
        }
    }

    private void positionBackButton(final TextButton button) {
        int buttonWidth = Gdx.graphics.getWidth() / 2;
        int buttonHeight = Gdx.graphics.getHeight() / 10;

        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int buttonPositionY = buttonHeight / 2;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }

    private void positionItemButton(final TextButton button, final int col, final int row) {
        // col max = 1, row max = 4
        int buttonWidth = Gdx.graphics.getWidth() / 3;
        int buttonHeight = Gdx.graphics.getHeight() / 10;

        int buttonPositionX = Gdx.graphics.getWidth() / 4 - buttonWidth / 2 + Gdx.graphics.getWidth() / 2 * col;
        int buttonPositionY = Gdx.graphics.getHeight() / 4 + buttonHeight * row;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }
}
