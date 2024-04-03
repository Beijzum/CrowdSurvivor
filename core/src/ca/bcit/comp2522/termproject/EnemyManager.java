package ca.bcit.comp2522.termproject;

import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import jdk.javadoc.internal.doclets.toolkit.taglets.snippet.Style;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

final public class EnemyManager {
    final private static int BASE_BASIC_ENEMY_HEALTH = 100;
    final private static int BASE_BOSS_HEALTH = 500;
    final private static int BASE_PROJECTILE_ENEMY_HEALTH = 50;
    final private static int BASE_BASIC_ENEMY_SPAWN_TIME = 3;
    final private static int BASE_CHARGER_ENEMY_SPAWN_TIME = 20;
    final private static int BASE_RANGED_ENEMY_SPAWN_TIME = 10;
    final private static int BASE_CHARGER_HEALTH = 125;
    final private static int BASE_ENEMY_SPEED = 100;
    final private static int BASE_WAVE_SIZE = 10;
    final private static int MAX_GAME_LENGTH = 600;
    final private static int BOSS_SPAWN_TIMER = 120;
    final private InGameScreen gameScreen;
    private static EnemyManager instance = null;
    private int currentBasicEnemySpawnTime;
    private int currentRangedEnemySpawnTime;
    private int currentChargerEnemySpawnTime;
    private float basicEnemyTimer;
    private float rangedEnemyTimer;
    private float chargerEnemyTimer;
    private float bossTimer;
    private float timeElapsed;
    final private Random randomNumberGenerator = new Random();

    private EnemyManager(InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.timeElapsed = 0;
        this.basicEnemyTimer = 0;
        this.rangedEnemyTimer = 0;
        this.chargerEnemyTimer = 0;
        this.currentBasicEnemySpawnTime = BASE_BASIC_ENEMY_SPAWN_TIME;
        this.currentChargerEnemySpawnTime = BASE_CHARGER_ENEMY_SPAWN_TIME;
        this.currentRangedEnemySpawnTime = BASE_RANGED_ENEMY_SPAWN_TIME;
        this.bossTimer = 0;
    }

    public static EnemyManager createManager(InGameScreen gameScreen) {
        if (instance == null) {
            instance = new EnemyManager(gameScreen);
        }
        return instance;
    }

    public void incrementTimers() {
        this.timeElapsed += Gdx.graphics.getDeltaTime();
        this.basicEnemyTimer += Gdx.graphics.getDeltaTime();
        this.chargerEnemyTimer += Gdx.graphics.getDeltaTime();
        this.rangedEnemyTimer += Gdx.graphics.getDeltaTime();
        this.bossTimer += Gdx.graphics.getDeltaTime();
    }

    public float getTimeElapsed() {
        return timeElapsed;
    }

    public void resetTimeElapsed() {
        this.timeElapsed = 0;
    }

    public void handleEnemyProjectiles() {

    }

    public void handleEnemies() {
        for (Enemy enemy : gameScreen.onFieldEnemies) {
            for (Projectile playerProjectile : gameScreen.playerProjectilesOnScreen) {
                enemy.takeDamage(playerProjectile,
                        (int) Math.round(gameScreen.player.attack * (1 - enemy.getDefense())));
            }
            moveToPlayer(enemy);
        }

        // kill one enemy per frame
        killEnemy();
    }

    public void handleEnemySpawn() {
        if (this.basicEnemyTimer > this.currentBasicEnemySpawnTime) {
            // randomize spawn point outside of screen, camera position x, y returns center of screen
            float[] spawnPoint = generateSpawnPoint();

            gameScreen.onFieldEnemies.add(createBasicEnemy(spawnPoint[0], spawnPoint[1]));
            this.basicEnemyTimer = 0;
            this.currentBasicEnemySpawnTime = this.randomNumberGenerator
                    .nextInt(BASE_BASIC_ENEMY_SPAWN_TIME) + BASE_BASIC_ENEMY_SPAWN_TIME;
        }

    }

    private void killEnemy() {
        Enemy deadEnemy = null;
        for (Enemy enemy : gameScreen.onFieldEnemies) {
            if (enemy.isDead()) {
                deadEnemy = enemy;
            }
        }
        if (deadEnemy != null) {
            deadEnemy.clearHitByProjectileList();
            gameScreen.onFieldEnemies.remove(deadEnemy);
            gameScreen.player.addEXP(deadEnemy.getDropEXP());
            gameScreen.player.addCollectedCurrency(deadEnemy.getDropCurrency());

            System.out.println(gameScreen.player.getCollectedCurrency());
            System.out.println(gameScreen.player.getLevel());
        }
    }

    private float[] generateSpawnPoint() {
        float randomX = randomNumberGenerator.nextFloat(
                0 - Gdx.graphics.getWidth() / (float) 4.0, Gdx.graphics.getWidth() * 5 / (float) 4.0);
        float randomY = randomNumberGenerator.nextFloat(
                0 - Gdx.graphics.getHeight() / (float) 5.0, Gdx.graphics.getHeight() * 6 / (float) 5.0);

        // adjust if spawn point in camera view
        if (this.gameScreen.camera.frustum.pointInFrustum(randomX, randomY, 0)) {
            if (randomNumberGenerator.nextBoolean()) {
                // modify x if true
                if (randomX < this.gameScreen.camera.position.x) {
                    randomX -= Gdx.graphics.getWidth() / (float) 1.5;
                } else {
                    randomX += Gdx.graphics.getWidth() / (float) 1.5;
                }
            } else {
                // modify y if true
                if (randomY < this.gameScreen.camera.position.y) {
                    randomY -= Gdx.graphics.getWidth() / (float) 1.5;
                } else {
                    randomY += Gdx.graphics.getWidth() / (float) 1.5;
                }
            }
            return new float[] {randomX, randomY};
        }
        return new float[] {randomX, randomY};
    }

    private Boss createBoss(float xCoord, float yCoord) {
        return new Boss();
    }

    private Enemy createCharger(float xCoord, float yCoord) {
        // temp scaling
        int health = Math.round(BASE_CHARGER_HEALTH + timeElapsed / 5);
        int speed = Math.round(BASE_ENEMY_SPEED + timeElapsed / 5);
        int acceleration = 20;
        int attack = 20;
        Enemy newEnemy = new Enemy(health, speed, acceleration, attack, "enemy.png"); // change this later
        newEnemy.setCenterPosition(xCoord, yCoord);
        return newEnemy;
    }

    private RangedEnemy createRangedEnemy(float xCoord, float yCoord) {
        return new RangedEnemy();
    }

    private Enemy createBasicEnemy(float xCoord, float yCoord) {
        int health = Math.round(BASE_BASIC_ENEMY_HEALTH + timeElapsed / 5); // temp scaling
        int speed = Math.round(BASE_ENEMY_SPEED + timeElapsed / 5);
        int attack = 10;
        Enemy newEnemy = new Enemy(health, speed, 0, attack, "enemy.png");
        newEnemy.setCenterPosition(xCoord, yCoord);
        return newEnemy;
    }

    private void moveToPlayer(Enemy enemy) {
        enemy.getDirectionVector().set(gameScreen.player.getCenterX() - enemy.getCenterX(),
                gameScreen.player.getCenterY() - enemy.getCenterY());
        enemy.move();
    }

    private void moveAwayFromPlayer() {

    }
}
