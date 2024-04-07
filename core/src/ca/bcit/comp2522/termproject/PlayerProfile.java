package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.*;

final public class PlayerProfile implements Serializable {
    private int maxHealthBooster;
    private int attackBooster;
    private float defenseBooster;
    private int speedBooster;
    private float EXPMultiplierBooster;
    private float currencyMultiplierBooster;
    private float attackSpeedBooster;
    private float critRateBooster;
    private float critMultiplierBooster;
    private float healthRegenMultiplierBooster;
    private int currency;
    private static PlayerProfile instance = null;

    // getters and setters to be used in shop
    public float getAttackSpeedBooster() {
        return attackSpeedBooster;
    }

    public float getCritMultiplierBooster() {
        return critMultiplierBooster;
    }

    public float getCritRateBooster() {
        return critRateBooster;
    }

    public float getDefenseBooster() {
        return defenseBooster;
    }

    public float getCurrencyMultiplierBooster() {
        return currencyMultiplierBooster;
    }

    public float getEXPMultiplierBooster() {
        return EXPMultiplierBooster;
    }

    public float getHealthRegenMultiplierBooster() {
        return healthRegenMultiplierBooster;
    }

    public int getAttackBooster() {
        return attackBooster;
    }

    public int getMaxHealthBooster() {
        return maxHealthBooster;
    }

    public int getSpeedBooster() {
        return speedBooster;
    }

    public int getCurrency() {
        return currency;
    }
    public void setAttackBooster(int attackBooster) {
        this.attackBooster = attackBooster;
    }

    public void setAttackSpeedBooster(float attackSpeedBooster) {
        this.attackSpeedBooster = attackSpeedBooster;
    }

    public void setCritMultiplierBooster(float critMultiplierBooster) {
        this.critMultiplierBooster = critMultiplierBooster;
    }

    public void setCritRateBooster(float critRateBooster) {
        this.critRateBooster = critRateBooster;
    }

    public void setCurrencyMultiplierBooster(float currencyMultiplierBooster) {
        this.currencyMultiplierBooster = currencyMultiplierBooster;
    }

    public void setDefenseBooster(float defenseBooster) {
        this.defenseBooster = defenseBooster;
    }

    public void setEXPMultiplierBooster(float EXPMultiplierBooster) {
        this.EXPMultiplierBooster = EXPMultiplierBooster;
    }

    public void setHealthRegenMultiplierBooster(float healthRegenMultiplierBooster) {
        this.healthRegenMultiplierBooster = healthRegenMultiplierBooster;
    }

    public void setMaxHealthBooster(int maxHealthBooster) {
        this.maxHealthBooster = maxHealthBooster;
    }

    public void setSpeedBooster(int speedBooster) {
        this.speedBooster = speedBooster;
    }
    public void setCurrency(int currency) {
        this.currency = currency;
    }

    private PlayerProfile() {
        setThisToDefault();
    }

    public static PlayerProfile createPlayerProfile() {
        if (instance != null) {
            return instance;
        }
        PlayerProfile loadedProfile = loadProfile();
        if (loadedProfile == null) {
            instance = new PlayerProfile();
            return instance;
        }
        instance = loadedProfile;
        return loadedProfile;
    }

    public void applyPlayerUpgrades(Player player) {
        player.setMaxHealth(player.getMaxHealth() + this.maxHealthBooster);
        player.setHealth(player.getMaxHealth());
        player.setAttack(player.getAttack() + this.attackBooster);
        player.setDefense(player.getDefense() + this.defenseBooster);
        player.setSpeed(player.getSpeed() + this.speedBooster);
        player.setEXPMultiplier(player.getEXPMultiplier() + this.EXPMultiplierBooster);
        player.setCurrencyMultiplier(player.getCurrencyMultiplier() + this.currencyMultiplierBooster);
        player.setAttackSpeed(player.getAttackSpeed() + this.attackSpeedBooster);
        player.setCritRate(player.getCritRate() + this.critRateBooster);
        player.setCritMultiplier(player.getCritMultiplier() + this.critMultiplierBooster);
        player.setHealthRegenMultiplier(player.getHealthRegenMultiplier() + this.healthRegenMultiplierBooster);
    }

    private void setThisToDefault() {
        this.maxHealthBooster = 0;
        this.attackBooster = 0;
        this.defenseBooster = 0;
        this.speedBooster = 0;
        this.EXPMultiplierBooster = 0;
        this.currencyMultiplierBooster = 0;
        this.attackSpeedBooster = 0;
        this.critRateBooster = 0;
        this.critMultiplierBooster = 0;
        this.healthRegenMultiplierBooster = 0;
        this.currency = 500000;
    }

    public void saveProfileState() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                Gdx.files.local("save/profile.ser").write(false))) {
            outputStream.writeObject(this);
        } catch (IOException err) {
            System.out.println("Error, something went wrong when auto-saving...");
        }
    }

    private static PlayerProfile loadProfile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(Gdx.files.local("save/profile.ser").read())) {
            return (PlayerProfile) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | GdxRuntimeException err) {
            return null;
        }
    }
}
