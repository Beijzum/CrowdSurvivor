package ca.bcit.comp2522.termproject.enemies;

import ca.bcit.comp2522.termproject.Projectile;
import ca.bcit.comp2522.termproject.screens.InGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Objects;
import java.util.Random;

public final class EnemyManager {
    private static EnemyManager instance = null;
    private static final int BASE_BASIC_ENEMY_HEALTH = 100;
    private static final int BASE_BOSS_HEALTH = 500;
    private static final int BASE_BASIC_ENEMY_SPAWN_TIME = 2;
    private static final int BASE_CHARGER_ENEMY_SPAWN_TIME = 10;
    private static final int BASE_RANGED_ENEMY_SPAWN_TIME = 5;
    private static final int BASE_CHARGER_HEALTH = 70;
    private static final int BASE_ENEMY_SPEED = 100;
    private static final int WAVE_SIZE = 10;
    private static final int BOSS_SPAWN_TIMER = 30;
    private static final int BASE_BOSS_SIZE = 100;
    private static final int BASE_BOSS_ATTACK = 30;
    private final InGameScreen gameScreen;
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
    private final Sound bossDeathSFX;
    private final Random randomNumberGenerator = new Random();

    private EnemyManager(final InGameScreen inGameScreen) {
        this.gameScreen = inGameScreen;
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
        this.bossDeathSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/bossDeathSFX.mp3"));
    }

    public static EnemyManager createManager(final InGameScreen gameScreen) {
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
        if (this.gameScreen.getEnemyProjectilesOnScreen().peek() != null
                && Objects.requireNonNull(this.gameScreen.getEnemyProjectilesOnScreen().peek()).isOverLifeTime()) {
            this.gameScreen.getEnemyProjectilesOnScreen().removeFirst();
        }
        if (this.gameScreen.getBossProjectilesOnScreen().peek() != null
                && Objects.requireNonNull(this.gameScreen.getBossProjectilesOnScreen().peek()).isOverLifeTime()) {
            this.gameScreen.getBossProjectilesOnScreen().removeFirst();
        }

        // move projectile
        for (Projectile enemyProjectile : this.gameScreen.getEnemyProjectilesOnScreen()) {
            enemyProjectile.incrementLifetimeTimer();
            enemyProjectile.moveProjectile();
            final float speedDivisor = 75f;
            enemyProjectile.spinProjectile(enemyProjectile.getSpeed() / speedDivisor);
        }
        for (Projectile bossProjectile : this.gameScreen.getBossProjectilesOnScreen()) {
            bossProjectile.incrementLifetimeTimer();
            bossProjectile.moveProjectile();
            final int spinSpeed = 30;
            bossProjectile.spinProjectile(spinSpeed);
        }

        // fire enemy projectile
        for (Enemy enemy : this.gameScreen.getOnFieldEnemies()) {
            if (!(enemy instanceof RangedEnemy)) {
                continue;
            }
            RangedEnemy rangedEnemy = (RangedEnemy) enemy;
            rangedEnemy.fireProjectile(this.gameScreen.getEnemyProjectilesOnScreen(),
                    this.gameScreen.getPlayer().getCenterX(), this.gameScreen.getPlayer().getCenterY());
        }

        // fire boss projectile
        for (Boss boss : this.gameScreen.getOnFieldBosses()) {
            boolean fired = boss.fireProjectile(this.gameScreen.getBossProjectilesOnScreen(),
                    this.gameScreen.getPlayer().getCenterX(), this.gameScreen.getPlayer().getCenterY());
            if (fired) {
                this.bossProjectileSFX.play();
            }
        }
    }

    public void handleEnemies() {
        for (Enemy enemy : this.gameScreen.getOnFieldEnemies()) {
            for (Projectile playerProjectile : this.gameScreen.getPlayerProjectilesOnScreen()) {
                if (!enemy.getHitbox().overlaps(playerProjectile.getHitbox())) {
                    continue;
                }
                int damage = calculateDamage();
                enemy.takeDamage(playerProjectile, damage, damage != this.gameScreen.getPlayer().getAttack());
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
        for (Boss boss : this.gameScreen.getOnFieldBosses()) {
            for (Projectile playerProjectile : this.gameScreen.getPlayerProjectilesOnScreen()) {
                if (!boss.getHitbox().overlaps(playerProjectile.getHitbox())) {
                    continue;
                }
                int damage = calculateDamage();
                boss.takeDamage(playerProjectile, damage, damage != this.gameScreen.getPlayer().getAttack());
            }
            boss.incrementDamageTintTimer();
            boss.updateDamageNumbers(Gdx.graphics.getDeltaTime());
            moveToPlayer(boss);
        }
    }

    public void handleEnemySpawn() {
        if (this.basicEnemyTimer >= this.currentBasicEnemySpawnTime) {
            final int randomEnemy = 10;
            final int randomEnemyCompare = 4;
            if (this.randomNumberGenerator.nextInt(randomEnemy) == randomEnemyCompare) {
                for (int i = 0; i < WAVE_SIZE; i++) {
                    float[] spawnPoint = generateSpawnPoint();
                    this.gameScreen.getOnFieldEnemies()
                            .add(createBasicEnemy(spawnPoint[0] + i, spawnPoint[1] + i));
                }
            } else {
                // randomize spawn point outside of screen, camera position x, y returns center of screen
                float[] spawnPoint = generateSpawnPoint();
                this.gameScreen.getOnFieldEnemies().add(createBasicEnemy(spawnPoint[0], spawnPoint[1]));
            }
            this.basicEnemyTimer = 0;
            this.currentBasicEnemySpawnTime = this.randomNumberGenerator
                    .nextInt(BASE_BASIC_ENEMY_SPAWN_TIME) + BASE_BASIC_ENEMY_SPAWN_TIME;
            this.enemySpawnSFX.play();
        }

        if (this.chargerEnemyTimer >= this.currentChargerEnemySpawnTime) {
            final int randomCharge = 10;
            final int randomChargeCompare = 4;
            if (randomNumberGenerator.nextInt(randomCharge) == randomChargeCompare) {
                for (int i = 0; i < WAVE_SIZE; i++) {
                    float[] spawnPoint = generateSpawnPoint();
                    gameScreen.getOnFieldEnemies()
                            .add(createCharger(spawnPoint[0] + i, spawnPoint[1] + i));
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
            final int randomRange = 10;
            final int randomRangeCompare = 4;
            if (this.randomNumberGenerator.nextInt(randomRange) == randomRangeCompare) {
                if (this.randomNumberGenerator.nextInt(randomRange) == randomRangeCompare) {
                    float[] spawnPoint = generateSpawnPoint();
                    this.gameScreen.getOnFieldEnemies().add(createRangedEnemy(spawnPoint[0], spawnPoint[1]));
                }
            } else {
                float[] spawnPoint = generateSpawnPoint();
                this.gameScreen.getOnFieldEnemies().add(createRangedEnemy(spawnPoint[0], spawnPoint[1]));
            }
            this.rangedEnemyTimer = 0;
            this.currentRangedEnemySpawnTime = this.randomNumberGenerator
                    .nextInt(BASE_RANGED_ENEMY_SPAWN_TIME) + BASE_RANGED_ENEMY_SPAWN_TIME;
            this.enemySpawnSFX.play();
        }

        if (this.bossTimer >= BOSS_SPAWN_TIMER && this.gameScreen.getTimeElapsed() <= InGameScreen.getMaxGameLength()) {
            float[] spawnPoint = generateSpawnPoint();
            this.gameScreen.getOnFieldBosses().add(createBoss(spawnPoint[0], spawnPoint[1]));
            this.bossTimer = 0;
            this.bossSpawnSFX.play();
        }
    }

    private int calculateDamage() {
        int damage;
        if (this.randomNumberGenerator.nextFloat(1) <= this.gameScreen.getPlayer().getCritRate()) {
            damage = Math.round(this.gameScreen.getPlayer().getAttack()
                    * this.gameScreen.getPlayer().getCritMultiplier());
        } else {
            damage = this.gameScreen.getPlayer().getAttack();
        }
        return damage;
    }

    private void killEnemy() {
        Enemy deadEnemy = null;
        for (Enemy enemy : this.gameScreen.getOnFieldEnemies()) {
            if (enemy.isDead()) {
                deadEnemy = enemy;
            }
        }
        if (deadEnemy != null) {
            deadEnemy.clearHitByProjectileList();
            this.gameScreen.getOnFieldEnemies().remove(deadEnemy);
            this.gameScreen.handlePlayerKill(deadEnemy);
        }
    }

    private void killBoss() {
        Boss deadBoss = null;
        for (Boss boss : this.gameScreen.getOnFieldBosses()) {
            if (boss.isDead()) {
                deadBoss = boss;
            }
        }
        if (deadBoss != null) {
            deadBoss.clearHitByProjectileList();
            this.gameScreen.getOnFieldBosses().remove(deadBoss);
            this.gameScreen.handlePlayerKill(deadBoss);
            this.bossDeathSFX.play();
        }
    }

    private float[] generateSpawnPoint() {
        final float xDivisor = 4.0f;
        final float yDivisor = 5.0f;
        final int widthMultiplier = 5;
        final int heightMultiplier = 6;
        float randomX = this.randomNumberGenerator.nextFloat(
                0 - Gdx.graphics.getWidth() / xDivisor, Gdx.graphics.getWidth()
                        * widthMultiplier / xDivisor);
        float randomY = this.randomNumberGenerator.nextFloat(
                0 - Gdx.graphics.getHeight() / yDivisor, Gdx.graphics.getHeight()
                        * heightMultiplier / yDivisor);

        // adjust if spawn point in camera view
        if (this.gameScreen.getCamera().frustum.pointInFrustum(randomX, randomY, 0)) {
            final float divisorValue = 1.5f;
            if (this.randomNumberGenerator.nextBoolean()) {
                // modify x if true
                if (randomX < this.gameScreen.getCamera().position.x) {
                    randomX -= Gdx.graphics.getWidth() / divisorValue;
                } else {
                    randomX += Gdx.graphics.getWidth() / divisorValue;
                }
            } else {
                // modify y if true
                if (randomY < this.gameScreen.getCamera().position.y) {
                    randomY -= Gdx.graphics.getWidth() / divisorValue;
                } else {
                    randomY += Gdx.graphics.getWidth() / divisorValue;
                }
            }
            return new float[]{randomX, randomY};
        }
        return new float[]{randomX, randomY};
    }

    private Boss createBoss(final float xCoord, final float yCoord) {
        final int health = BASE_BOSS_HEALTH * Math.round(this.gameScreen.getTimeElapsed()) / BOSS_SPAWN_TIMER;
        final int speed = BASE_ENEMY_SPEED + Math.round(this.gameScreen.getTimeElapsed() / 5);
        final int size = BASE_BOSS_SIZE + 25 * Math.round(this.gameScreen.getTimeElapsed() / 30) - 25;
        final int attack = BASE_BOSS_ATTACK + 5 * Math.round(this.gameScreen.getTimeElapsed() / 30) - 10;
        Boss newBoss = new Boss(health, speed, attack, size,
                this.gameScreen.getAtlas().createSprite("enemies/boss"),
                this.gameScreen.getAtlas().createSprite("projectiles/bossProjectile"));
        newBoss.setCenterPosition(xCoord, yCoord);
        return newBoss;
    }

    private Enemy createCharger(final float xCoord, final float yCoord) {
        final int health = Math.round(BASE_CHARGER_HEALTH + this.gameScreen.getTimeElapsed() / 5); // temp scaling
        final int acceleration = 100;
        final int attack = 10;
        Charger newCharger = new Charger(health, acceleration, attack, this.gameScreen.getAtlas()
                .createSprite("enemies/charger"));
        newCharger.setCenterPosition(xCoord, yCoord);
        return newCharger;
    }

    private RangedEnemy createRangedEnemy(final float xCoord, final float yCoord) {
        final int choice = this.randomNumberGenerator.nextInt(3); // basic, spewer, sniper
        RangedEnemy newEnemy;
        switch (choice) {
            case 0:
                newEnemy = RangedEnemy.createSniper(this.gameScreen.getTimeElapsed(),
                        this.gameScreen.getAtlas().createSprite("enemies/sniper"),
                        this.gameScreen.getAtlas().createSprite("projectiles/enemyProjectile"));
                break;
            case 1:
                newEnemy = RangedEnemy.createSpewer(this.gameScreen.getTimeElapsed(),
                        this.gameScreen.getAtlas().createSprite("enemies/spewer"),
                        this.gameScreen.getAtlas().createSprite("projectiles/enemyProjectile"));
                break;
            default:
                newEnemy = RangedEnemy.createBasic(this.gameScreen.getTimeElapsed(),
                        this.gameScreen.getAtlas().createSprite("enemies/basicRanged"),
                        this.gameScreen.getAtlas().createSprite("projectiles/enemyProjectile"));
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

    private Enemy createBasicEnemy(final float xCoord, final float yCoord) {
        final int health = Math.round(BASE_BASIC_ENEMY_HEALTH + this.gameScreen.getTimeElapsed() / 5); // temp
        final int speed = Math.round(BASE_ENEMY_SPEED + this.gameScreen.getTimeElapsed() / 5);
        final int attack = 10;
        Enemy newEnemy = new Enemy(health, speed, attack, this.gameScreen.getAtlas()
                .createSprite("enemies/enemy"));
        newEnemy.setCenterPosition(xCoord, yCoord);
        return newEnemy;
    }

    private void moveToPlayer(final Enemy enemy) {
        enemy.calculateDirectionVector(this.gameScreen.getPlayer().getCenterX() - enemy.getCenterX(),
                this.gameScreen.getPlayer().getCenterY() - enemy.getCenterY());
        enemy.move();
    }
}
