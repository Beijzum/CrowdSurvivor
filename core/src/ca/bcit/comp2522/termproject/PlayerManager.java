package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.InGameScreen;

public class PlayerManager {
    final private InGameScreen gameScreen;
    private static PlayerManager instance = null;
    private PlayerManager(InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public static PlayerManager createPlayerManager(InGameScreen gameScreen) {
        if (instance == null) {
            instance = new PlayerManager(gameScreen);
        }
        return instance;
    }

    public void handleDamage() {
        for (Enemy enemy : gameScreen.onFieldEnemies) {
            gameScreen.player.takeDamage(enemy.getHitbox(), enemy.getAttack());
        }
    }
}
