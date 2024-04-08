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

public class ShopScreen implements Screen, Background, ActorManager, MessageLayout {
    final private static int BASE_ITEM_PRICE = 500;
    final private CrowdSurvivor game;
    final private Music music;
    final private TextButton[] menuItems;
    final private Color backgroundFilter = new Color(50 / 255f, 50 / 255f, 100 / 255f, 1);
    final private Sprite background = new Sprite(new Texture("backgrounds/shopMenuBackground.jpg"));
    final private GlyphLayout shopMessage;
    final private GlyphLayout currencyMessage;
    final private Sound purchaseSFX;
    final private Sound failPurchaseSFX;

    public ShopScreen(CrowdSurvivor crowdSurvivor) {
        this.game = crowdSurvivor;
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/shopMenuMusic.mp3"));
        this.shopMessage = createLayout("SHOP", 2f);
        this.currencyMessage = new GlyphLayout();
        this.menuItems = createButtons();
        this.purchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/purchaseSFX.mp3"));
        this.failPurchaseSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/failPurchase.mp3"));
        positionMenuItems();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(game.buttonsUI);
        music.setLooping(true);
        music.play();
        addActors(game.buttonsUI, menuItems);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        setTextAndScale(this.currencyMessage, String.format("CURRENCY: $%d", game.playerProfile.getCurrency()), 2f);
        renderBackgroundWithFilter(game, background, backgroundFilter);
        drawMessage(shopMessage, game.getBatch(), 20, Gdx.graphics.getHeight() / 1.01f, 2f);
        drawMessage(currencyMessage, game.getBatch(),
                20, Gdx.graphics.getHeight() / 1.01f - this.shopMessage.height - 20, 2f);
        game.buttonsUI.act();
        game.buttonsUI.draw();
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
        clearStage(game.buttonsUI);
        dispose();
    }

    @Override
    public void dispose() {
        music.dispose();
    }

    private TextButton[] createButtons() {
        int buyAttackPrice = BASE_ITEM_PRICE;
        TextButton buyAttack = new TextButton(String.format("$%d - Buy Attack", buyAttackPrice), game.skin);
        buyAttack.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyAttackPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setAttackBooster(game.playerProfile.getAttackBooster() + 5);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyAttackPrice);
                return true;
            }
        });

        int buyHealthPrice = BASE_ITEM_PRICE;
        TextButton buyHealth = new TextButton(String.format("$%d - Buy Health", buyHealthPrice), game.skin);
        buyHealth.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyHealthPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setMaxHealthBooster(game.playerProfile.getMaxHealthBooster() + 5);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyHealthPrice);
                return true;
            }
        });

        int buySpeedPrice = BASE_ITEM_PRICE;
        TextButton buySpeed = new TextButton(String.format("$%d - Buy Speed", buySpeedPrice), game.skin);
        buySpeed.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buySpeedPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setSpeedBooster(game.playerProfile.getSpeedBooster() + 5);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buySpeedPrice);
                return true;
            }
        });

        int buyDefensePrice = Math.round(BASE_ITEM_PRICE * 1.5f);
        TextButton buyDefense = new TextButton(String.format("$%d - Buy Defense", buyDefensePrice), game.skin);
        buyDefense.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyDefensePrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setDefenseBooster(game.playerProfile.getDefenseBooster() + 0.02f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyDefensePrice);
                return true;
            }
        });

        int buyEXPPrice = BASE_ITEM_PRICE;
        TextButton buyEXP = new TextButton(String.format("$%d - Buy Base EXP Gain", buyEXPPrice), game.skin);
        buyEXP.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyEXPPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setEXPMultiplierBooster(game.playerProfile.getEXPMultiplierBooster() + 0.05f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyEXPPrice);
                return true;
            }
        });

        int buyCurrencyPrice = BASE_ITEM_PRICE * 2;
        TextButton buyCurrency = new TextButton(
                String.format("$%d - Buy Base Currency Gain", buyCurrencyPrice), game.skin);
        buyCurrency.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyCurrencyPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile
                        .setCurrencyMultiplierBooster(game.playerProfile.getCurrencyMultiplierBooster() + 0.05f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyCurrencyPrice);
                return true;
            }
        });

        int buyAtttackSpeedPrice = Math.round(BASE_ITEM_PRICE * 1.75f);
        TextButton buyAttackSpeed = new TextButton(
                String.format("$%d - Buy Attack Speed", buyAtttackSpeedPrice), game.skin);
        buyAttackSpeed.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyAtttackSpeedPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setAttackSpeedBooster(game.playerProfile.getAttackSpeedBooster() - 0.05f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyAtttackSpeedPrice);
                return true;
            }
        });

        int buyCritRatePrice = BASE_ITEM_PRICE * 2;
        TextButton buyCritRate = new TextButton(
                String.format("$%d - Buy Critical Hit Rate", buyCritRatePrice), game.skin);
        buyCritRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyCritRatePrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setCritRateBooster(game.playerProfile.getCritRateBooster() + 0.02f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyCritRatePrice);
                return true;
            }
        });

        int buyCritDamagePrice = BASE_ITEM_PRICE * 2;
        TextButton buyCritDamage = new TextButton(
                String.format("$%d - Buy Critical Hit Damage", buyCritDamagePrice), game.skin);
        buyCritDamage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyCritDamagePrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile.setCritMultiplierBooster(game.playerProfile.getCritMultiplierBooster() + 0.05f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyCritDamagePrice);
                return true;
            }
        });

        int buyHealthRegenPrice = BASE_ITEM_PRICE;
        TextButton buyHealthRegen = new TextButton(
                String.format("$%d - Buy Health Regeneration", buyHealthRegenPrice), game.skin);
        buyHealthRegen.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }

                if (game.playerProfile.getCurrency() < buyHealthRegenPrice) {
                    failPurchaseSFX.play(0.5f);
                    return true;
                }
                purchaseSFX.play();
                game.playerProfile
                        .setHealthRegenMultiplierBooster(game.playerProfile.getHealthRegenMultiplierBooster() + 0.02f);
                game.playerProfile.setCurrency(game.playerProfile.getCurrency() - buyHealthPrice);
                return true;
            }
        });

        TextButton backButton = new TextButton("Back To Menu", game.skin);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                dispose();
                game.setScreen(game.mainMenuScreen);
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

    private void adjustButtonColor(int price, TextButton button) {
        if (game.playerProfile.getCurrency() < price) {
            button.setColor(175 / 255f, 25 / 255f, 25 / 255f, 1);
        }
    }

    private void positionBackButton(TextButton button) {
        int buttonWidth = Gdx.graphics.getWidth() / 2;
        int buttonHeight = Gdx.graphics.getHeight() / 10;

        int buttonPositionX = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;
        int buttonPositionY = buttonHeight / 2;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }

    private void positionItemButton(TextButton button, int col, int row) {
        // col max = 1, row max = 4
        int buttonWidth = Gdx.graphics.getWidth() / 4;
        int buttonHeight = Gdx.graphics.getHeight() / 10;

        int buttonPositionX = Gdx.graphics.getWidth() / 4 - buttonWidth / 2 + Gdx.graphics.getWidth() / 2 * col;
        int buttonPositionY = Gdx.graphics.getHeight() / 4 + buttonHeight * row;

        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(buttonPositionX, buttonPositionY);
    }
}
