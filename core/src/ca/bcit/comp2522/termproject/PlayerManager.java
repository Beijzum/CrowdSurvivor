package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import ca.bcit.comp2522.termproject.enemies.Enemy;

public class PlayerManager {
    final private InGameScreen gameScreen;
    private static PlayerManager instance = null;
    final private Vector3 mouseVector = new Vector3(0, 0, 0);

    private PlayerManager(InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public static PlayerManager createPlayerManager(InGameScreen gameScreen) {
        if (instance == null) {
            instance = new PlayerManager(gameScreen);
        }
        return instance;
    }

    public void handlePlayerHealth() {
        gameScreen.player.regenHealth();
        for (Enemy enemy : gameScreen.onFieldEnemies) {
            gameScreen.player.takeDamage(enemy.getHitbox(), enemy.getAttack());
        }
        for (Projectile projectile : gameScreen.enemyProjectilesOnScreen) {
            final int projectileDamage = 10;
            gameScreen.player.takeDamage(projectile.getHitbox(), projectileDamage);
        }
    }

    public void incrementPlayerIframe() {
        gameScreen.player.incrementIFrames();
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
        this.mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mousePosition = gameScreen.camera.unproject(mouseVector);
        gameScreen.player
                .fireProjectile(this.gameScreen.playerProjectilesOnScreen, mousePosition.x, mousePosition.y);

    }
}
