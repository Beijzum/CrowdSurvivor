package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import org.w3c.dom.ranges.Range;

import java.awt.event.WindowAdapter;
import java.util.Random;

final public class EnemyManager {
    final private static int BASE_BASIC_ENEMY_HEALTH = 100;
    final private static int BASE_BOSS_HEALTH = 500;
    final private static int BASE_BASIC_ENEMY_SPAWN_TIME = 2;
    final private static int BASE_CHARGER_ENEMY_SPAWN_TIME = 10;
    final private static int BASE_RANGED_ENEMY_SPAWN_TIME = 5;
    final private static int BASE_CHARGER_HEALTH = 125;
    final private static int BASE_ENEMY_SPEED = 100;
    final private static int WAVE_SIZE = 8;
    final private static int BOSS_SPAWN_TIMER = 30;
    final private static int BASE_BOSS_SIZE = 100;
    final private static int BASE_BOSS_ATTACK = 30;
    final private InGameScreen gameScreen;
    private static EnemyManager instance = null;
    private int currentBasicEnemySpawnTime;
    private int currentRangedEnemySpawnTime;
    private int currentChargerEnemySpawnTime;
    private float basicEnemyTimer;
    private float rangedEnemyTimer;
    private float chargerEnemyTimer;
    private float bossTimer;
    private final Sound bossSpawnSFX;
    private final Sound enemySpawnSFX;
    private final Sound bossProjectileSFX;
    final private Random randomNumberGenerator = new Random();

    private EnemyManager(InGameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.basicEnemyTimer = 0;
        this.rangedEnemyTimer = 0;
        this.chargerEnemyTimer = 0;
        this.currentBasicEnemySpawnTime = BASE_BASIC_ENEMY_SPAWN_TIME;
        this.currentChargerEnemySpawnTime = BASE_CHARGER_ENEMY_SPAWN_TIME;
        this.currentRangedEnemySpawnTime = BASE_RANGED_ENEMY_SPAWN_TIME;
        this.bossTimer = 0;
        this.bossSpawnSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/bossSpawnSFX.mp3"));
        this.enemySpawnSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/enemySpawnSFX.mp3"));
        this.bossProjectileSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/bossProjectileSFX.mp3"));
    }

    public static EnemyManager createManager(InGameScreen gameScreen) {
        if (instance == null) {
            instance = new EnemyManager(gameScreen);
        }
        return instance;
    }

    public void incrementTimers() {
        this.basicEnemyTimer += Gdx.graphics.getDeltaTime();
        this.chargerEnemyTimer += Gdx.graphics.getDeltaTime();
        this.rangedEnemyTimer += Gdx.graphics.getDeltaTime();
        this.bossTimer += Gdx.graphics.getDeltaTime();
    }

    public void handleEnemyProjectiles() {
        // remove projectile
        if (gameScreen.getEnemyProjectilesOnScreen().peek() != null
                && gameScreen.getEnemyProjectilesOnScreen().peek().isOverLifeTime()) {
            gameScreen.getEnemyProjectilesOnScreen().removeFirst();
        }
        if (gameScreen.getBossProjectilesOnScreen().peek() != null
        && gameScreen.getBossProjectilesOnScreen().peek().isOverLifeTime()) {
            gameScreen.getBossProjectilesOnScreen().removeFirst();
        }

        // move projectile
        for (Projectile enemyProjectile : gameScreen.getEnemyProjectilesOnScreen()) {
            enemyProjectile.incrementLifetimeTimer();
            enemyProjectile.moveProjectile();
            enemyProjectile.spinProjectile(enemyProjectile.getSpeed() / 75f);
        }
        for (Projectile bossProjectile : gameScreen.getBossProjectilesOnScreen()) {
            bossProjectile.incrementLifetimeTimer();
            bossProjectile.moveProjectile();
            bossProjectile.spinProjectile(30);
        }

        // fire enemy projectile
        for (Enemy enemy : gameScreen.getOnFieldEnemies()) {
            if (!(enemy instanceof RangedEnemy)) {
                continue;
            }
            RangedEnemy rangedEnemy = (RangedEnemy) enemy;
            rangedEnemy.fireProjectile(gameScreen.getEnemyProjectilesOnScreen(),
                    gameScreen.getPlayer().getCenterX(), gameScreen.getPlayer().getCenterY());
        }

        // fire boss projectile
        for (Boss boss : gameScreen.getOnFieldBosses()) {
            boolean fired = boss.fireProjectile(gameScreen.getBossProjectilesOnScreen(),
                    gameScreen.getPlayer().getCenterX(), gameScreen.getPlayer().getCenterY());
            if (fired) {
                this.bossProjectileSFX.play();
            }
        }
    }

    public void handleEnemies() {
        for (Enemy enemy : gameScreen.getOnFieldEnemies()) {
            for (Projectile playerProjectile : gameScreen.getPlayerProjectilesOnScreen()) {
                if (!enemy.getHitbox().overlaps(playerProjectile.getHitbox())) {
                    continue;
                }
                int damage = calculateDamage();
                enemy.takeDamage(playerProjectile, damage, damage != gameScreen.getPlayer().getAttack());
            }
            enemy.incrementDamageTintTimer();
            enemy.updateDamageNumbers(Gdx.graphics.getDeltaTime());
            moveToPlayer(enemy);
        }

        // kill one enemy and one boss per frame
        killEnemy();
        killBoss();
    }

    public void handleBosses() {
        for (Boss boss : gameScreen.getOnFieldBosses()) {
            for (Projectile playerProjectile : gameScreen.getPlayerProjectilesOnScreen()) {
                if (!boss.getHitbox().overlaps(playerProjectile.getHitbox())) {
                    continue;
                }
                int damage = calculateDamage();
                boss.takeDamage(playerProjectile, damage, damage != gameScreen.getPlayer().getAttack());
            }
            boss.incrementDamageTintTimer();
            boss.updateDamageNumbers(Gdx.graphics.getDeltaTime());
            moveToPlayer(boss);
        }
    }

    public void handleEnemySpawn() {
        if (this.basicEnemyTimer >= this.currentBasicEnemySpawnTime) {
            if (randomNumberGenerator.nextInt(10) == 4) {
                for (int i = 0; i < WAVE_SIZE; i++) {
                    float[] spawnPoint = generateSpawnPoint();
                    gameScreen.getOnFieldEnemies().add(createBasicEnemy(spawnPoint[0] + i, spawnPoint[1] + i));
                }
            } else {
                // randomize spawn point outside of screen, camera position x, y returns center of screen
                float[] spawnPoint = generateSpawnPoint();
                gameScreen.getOnFieldEnemies().add(createBasicEnemy(spawnPoint[0], spawnPoint[1]));
            }
            this.basicEnemyTimer = 0;
            this.currentBasicEnemySpawnTime = this.randomNumberGenerator
                    .nextInt(BASE_BASIC_ENEMY_SPAWN_TIME) + BASE_BASIC_ENEMY_SPAWN_TIME;
            this.enemySpawnSFX.play();
        }

        if (this.chargerEnemyTimer >= this.currentChargerEnemySpawnTime) {
            if (randomNumberGenerator.nextInt(10) == 4) {
                for (int i = 0; i < WAVE_SIZE; i++) {
                    float[] spawnPoint = generateSpawnPoint();
                    gameScreen.getOnFieldEnemies().add(createCharger(spawnPoint[0] + i, spawnPoint[1] + i));
                }
            } else {
                float[] spawnPoint = generateSpawnPoint();
                gameScreen.getOnFieldEnemies().add(createCharger(spawnPoint[0], spawnPoint[1]));
            }
            this.chargerEnemyTimer = 0;
            this.currentChargerEnemySpawnTime = this.randomNumberGenerator
                    .nextInt(BASE_CHARGER_ENEMY_SPAWN_TIME) + BASE_CHARGER_ENEMY_SPAWN_TIME;
            this.enemySpawnSFX.play();
        }

        if (this.rangedEnemyTimer >= this.currentRangedEnemySpawnTime) {
            if (randomNumberGenerator.nextInt(10) == 4) {
                if (randomNumberGenerator.nextInt(10) == 4) {
                    float[] spawnPoint = generateSpawnPoint();
                    gameScreen.getOnFieldEnemies().add(createRangedEnemy(spawnPoint[0], spawnPoint[1]));
                }
            } else {
                float[] spawnPoint = generateSpawnPoint();
                gameScreen.getOnFieldEnemies().add(createRangedEnemy(spawnPoint[0], spawnPoint[1]));
            }
            this.rangedEnemyTimer = 0;
            this.currentRangedEnemySpawnTime = this.randomNumberGenerator
                    .nextInt(BASE_RANGED_ENEMY_SPAWN_TIME) + BASE_RANGED_ENEMY_SPAWN_TIME;
            this.enemySpawnSFX.play();
        }

        if (this.bossTimer >= BOSS_SPAWN_TIMER && gameScreen.getTimeElapsed() <= InGameScreen.getMaxGameLength()) {
            float[] spawnPoint = generateSpawnPoint();
            gameScreen.getOnFieldBosses().add(createBoss(spawnPoint[0], spawnPoint[1]));
            this.bossTimer = 0;
            this.bossSpawnSFX.play();
        }
    }

    private int calculateDamage() {
        int damage;
        if (randomNumberGenerator.nextFloat(1) <= gameScreen.getPlayer().getCritRate()) {
            damage = Math.round(gameScreen.getPlayer().getAttack() * gameScreen.getPlayer().getCritMultiplier());
        } else {
            damage = gameScreen.getPlayer().getAttack();
        }
        return damage;
    }

    private void killEnemy() {
        Enemy deadEnemy = null;
        for (Enemy enemy : gameScreen.getOnFieldEnemies()) {
            if (enemy.isDead()) {
                deadEnemy = enemy;
            }
        }
        if (deadEnemy != null) {
            deadEnemy.clearHitByProjectileList();
            gameScreen.getOnFieldEnemies().remove(deadEnemy);
            gameScreen.handlePlayerKill(deadEnemy);
        }
    }

    private void killBoss() {
        Boss deadBoss = null;
        for (Boss boss : gameScreen.getOnFieldBosses()) {
            if (boss.isDead()) {
                deadBoss = boss;
            }
        }
        if (deadBoss != null) {
            deadBoss.clearHitByProjectileList();
            gameScreen.getOnFieldBosses().remove(deadBoss);
            gameScreen.handlePlayerKill(deadBoss);
        }
    }

    private float[] generateSpawnPoint() {
        float randomX = randomNumberGenerator.nextFloat(
                0 - Gdx.graphics.getWidth() / (float) 4.0, Gdx.graphics.getWidth() * 5 / (float) 4.0);
        float randomY = randomNumberGenerator.nextFloat(
                0 - Gdx.graphics.getHeight() / (float) 5.0, Gdx.graphics.getHeight() * 6 / (float) 5.0);

        // adjust if spawn point in camera view
        if (this.gameScreen.getCamera().frustum.pointInFrustum(randomX, randomY, 0)) {
            if (randomNumberGenerator.nextBoolean()) {
                // modify x if true
                if (randomX < this.gameScreen.getCamera().position.x) {
                    randomX -= Gdx.graphics.getWidth() / (float) 1.5;
                } else {
                    randomX += Gdx.graphics.getWidth() / (float) 1.5;
                }
            } else {
                // modify y if true
                if (randomY < this.gameScreen.getCamera().position.y) {
                    randomY -= Gdx.graphics.getWidth() / (float) 1.5;
                } else {
                    randomY += Gdx.graphics.getWidth() / (float) 1.5;
                }
            }
            return new float[]{randomX, randomY};
        }
        return new float[]{randomX, randomY};
    }

    private Boss createBoss(float xCoord, float yCoord) {
        int health = BASE_BOSS_HEALTH * Math.round(gameScreen.getTimeElapsed()) / BOSS_SPAWN_TIMER;
        int speed = BASE_ENEMY_SPEED + Math.round(gameScreen.getTimeElapsed() / 5);
        int size = BASE_BOSS_SIZE + 25 * Math.round(gameScreen.getTimeElapsed() / 30) - 25;
        int attack = BASE_BOSS_ATTACK + 5 * Math.round(gameScreen.getTimeElapsed() / 30) - 10;
        Boss newBoss = new Boss(health, speed, attack, size, "boss.png");
        newBoss.setCenterPosition(xCoord, yCoord);
        return newBoss;
    }

    private Enemy createCharger(float xCoord, float yCoord) {
        // temp scaling
        int health = Math.round(BASE_CHARGER_HEALTH + gameScreen.getTimeElapsed() / 5);
        int acceleration = 100;
        int attack = 20;
        Charger newCharger = new Charger(health, acceleration, attack, "charger.jpg");
        newCharger.setCenterPosition(xCoord, yCoord);
        return newCharger;
    }

    private RangedEnemy createRangedEnemy(float xCoord, float yCoord) {
        int choice = randomNumberGenerator.nextInt(3); // basic, spewer, sniper
        RangedEnemy newEnemy;
        switch (choice) {
            case 0:
                newEnemy = RangedEnemy.createSniper(gameScreen.getTimeElapsed());
                break;
            case 1:
                newEnemy = RangedEnemy.createSpewer(gameScreen.getTimeElapsed());
                break;
            default:
                newEnemy = RangedEnemy.createBasic(gameScreen.getTimeElapsed());
                break;
        }
        newEnemy.setCenterPosition(xCoord, yCoord);
        return newEnemy;
    }

    public void resetEnemyTimers() {
        this.bossTimer = 0;
        this.rangedEnemyTimer = 0;
        this.basicEnemyTimer = 0;
        this.chargerEnemyTimer = 0;
        this.currentRangedEnemySpawnTime = BASE_RANGED_ENEMY_SPAWN_TIME;
        this.currentBasicEnemySpawnTime = BASE_BASIC_ENEMY_SPAWN_TIME;
        this.currentChargerEnemySpawnTime = BASE_CHARGER_ENEMY_SPAWN_TIME;
    }

    private Enemy createBasicEnemy(float xCoord, float yCoord) {
        int health = Math.round(BASE_BASIC_ENEMY_HEALTH + gameScreen.getTimeElapsed() / 5); // temp scaling
        int speed = Math.round(BASE_ENEMY_SPEED + gameScreen.getTimeElapsed() / 5);
        int attack = 10;
        Enemy newEnemy = new Enemy(health, speed, attack, "enemy.png");
        newEnemy.setCenterPosition(xCoord, yCoord);
        return newEnemy;
    }

    private void moveToPlayer(Enemy enemy) {
        enemy.calculateDirectionVector(gameScreen.getPlayer().getCenterX() - enemy.getCenterX(),
                gameScreen.getPlayer().getCenterY() - enemy.getCenterY());
        enemy.move();
    }
}
