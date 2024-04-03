package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class PlayerManager implements InputProcessor {
    final private InGameScreen gameScreen;
    private boolean isPaused = false;
    private static PlayerManager instance = null;
    private float mousePositionX;
    private float mousePositionY;
    private PlayerManager(InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public boolean getIsPaused() {
        return this.isPaused;
    }

    public void resumeGame() {
        this.isPaused = false;
    }

    public static PlayerManager createPlayerManager(InGameScreen gameScreen) {
        if (instance == null) {
            instance = new PlayerManager(gameScreen);
        }
        return instance;
    }

    public void handleUltimateCD() {
        if (!gameScreen.player.ultimateIsReady()) {
            gameScreen.player.waitForCD();
        }
    }

    public void handlePlayerHealth() {
        gameScreen.player.regenHealth();
        for (Enemy enemy : gameScreen.onFieldEnemies) {
            gameScreen.player.takeDamage(enemy.getHitbox(), enemy.getAttack());
        }
    }

    public void handleContinuousPlayerKeyboardInput() {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            return;
        }
        float deltaY = 0, deltaX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            deltaY = gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            deltaY = -gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            deltaX = -gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            deltaX = gameScreen.player.getSpeed() * Gdx.graphics.getDeltaTime();
        }
        // normalize vector
        if (deltaX != 0 && deltaY != 0) {
            deltaX = deltaX * (float) Math.abs(Math.cos(Math.atan(deltaY / deltaX)));
            deltaY = deltaY * (float) Math.abs(Math.sin(Math.atan(deltaY / deltaX)));
        }
        gameScreen.player.setX(gameScreen.player.getX() + deltaX);
        gameScreen.player.setY(gameScreen.player.getY() + deltaY);
    }

    public void handleAttack() {
        // remove expired projectiles
        if (gameScreen.playerProjectilesOnScreen.peek() != null
        && gameScreen.playerProjectilesOnScreen.peek().isOverLifeTime()) {
            gameScreen.playerProjectilesOnScreen.removeFirst();
        }

        // move projectiles not expired
        for (Projectile projectile : gameScreen.playerProjectilesOnScreen) {
            projectile.incrementLifetimeTimer();
            projectile.moveProjectile();
        }

        // fire projectile
        gameScreen.player
                .fireProjectile(this.gameScreen.playerProjectilesOnScreen, this.mousePositionX, this.mousePositionY);

    }

    // implement later for when saving upgrades is being worked on
    public void loadPlayerUpgrades() {

    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.F) {
            gameScreen.player.useUltimate();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keyCode) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        this.mousePositionX = Gdx.input.getX();
        // mousePositionY is flipped from the rest of the coordinates system in libgdx
        this.mousePositionY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        this.mousePositionX = Gdx.input.getX();
        this.mousePositionY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return true;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
