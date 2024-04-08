package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents the player profile for the game.
 * Implements serializable to enable saving and loading the player's progress.
 * Implements the singleton method design pattern.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public final class PlayerProfile implements Serializable {
    private static PlayerProfile instance = null;
    private int maxHealthBooster;
    private int attackBooster;
    private float defenseBooster;
    private int speedBooster;
    private float expMultiplierBooster;
    private float currencyMultiplierBooster;
    private float attackSpeedBooster;
    private float critRateBooster;
    private float critMultiplierBooster;
    private float healthRegenMultiplierBooster;
    private int currency;

    private PlayerProfile() {
        setThisToDefault();
    }

    /**
     * Creates a single instance of PlayerProfile.
     *
     * @return the created or loaded PlayerProfile instance.
     */
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


    /**
     * Retrieves the attack speed booster upgrade.
     *
     * @return the attack speed booster upgrade.
     */
    public float getAttackSpeedBooster() {
        return attackSpeedBooster;
    }

    /**
     * Retrieves the critical multiplier booster upgrade.
     *
     * @return the critical multiplier booster upgrade.
     */
    public float getCritMultiplierBooster() {
        return critMultiplierBooster;
    }

    /**
     * Retrieves the critical rate booster upgrade.
     *
     * @return the critical rate booster upgrade.
     */
    public float getCritRateBooster() {
        return critRateBooster;
    }

    /**
     * Retrieves the defense booster upgrade.
     *
     * @return the defense booster upgrade.
     */
    public float getDefenseBooster() {
        return defenseBooster;
    }

    /**
     * Retrieves the currency multiplier booster upgrade.
     *
     * @return the currency multiplier booster upgrade.
     */
    public float getCurrencyMultiplierBooster() {
        return currencyMultiplierBooster;
    }

    /**
     * Retrieves the experience multiplier booster upgrade.
     *
     * @return the experience multiplier booster upgrade.
     */
    public float getEXPMultiplierBooster() {
        return expMultiplierBooster;
    }

    /**
     * Retrieves the health regeneration multiplier booster upgrade.
     *
     * @return the health regeneration multiplier booster upgrade.
     */
    public float getHealthRegenMultiplierBooster() {
        return healthRegenMultiplierBooster;
    }

    /**
     * Retrieves the attack booster upgrade.
     *
     * @return the attack booster upgrade.
     */
    public int getAttackBooster() {
        return attackBooster;
    }

    /**
     * Retrieves the maximum health booster upgrade.
     *
     * @return the maximum health booster upgrade.
     */
    public int getMaxHealthBooster() {
        return maxHealthBooster;
    }

    /**
     * Retrieves the speed booster upgrade.
     *
     * @return the speed booster upgrade.
     */
    public int getSpeedBooster() {
        return speedBooster;
    }

    /**
     * Retrieves the current currency.
     *
     * @return the current currency.
     */
    public int getCurrency() {
        return currency;
    }

    /**
     * Sets the attack booster upgrade value.
     *
     * @param attackBooster int representing the attack booster upgrade value.
     */
    public void setAttackBooster(final int attackBooster) {
        this.attackBooster = attackBooster;
    }

    /**
     * Sets the attack speed booster upgrade value.
     *
     * @param attackSpeedBooster float representing the attack speed booster upgrade value.
     */
    public void setAttackSpeedBooster(final float attackSpeedBooster) {
        this.attackSpeedBooster = attackSpeedBooster;
    }

    /**
     * Sets the critical multiplier booster upgrade value.
     *
     * @param critMultiplierBooster float representing the critical multiplier booster upgrade value.
     */
    public void setCritMultiplierBooster(final float critMultiplierBooster) {
        this.critMultiplierBooster = critMultiplierBooster;
    }

    /**
     * Sets the critical rate booster upgrade value.
     *
     * @param critRateBooster float representing the critical rate booster upgrade value.
     */
    public void setCritRateBooster(final float critRateBooster) {
        this.critRateBooster = critRateBooster;
    }

    /**
     * Sets the currency multiplier booster upgrade value.
     *
     * @param currencyMultiplierBooster float representing the currency multiplier booster upgrade value.
     */
    public void setCurrencyMultiplierBooster(final float currencyMultiplierBooster) {
        this.currencyMultiplierBooster = currencyMultiplierBooster;
    }

    /**
     * Sets the defense booster upgrade value.
     *
     * @param defenseBooster float representing the defense booster upgrade value.
     */
    public void setDefenseBooster(final float defenseBooster) {
        this.defenseBooster = defenseBooster;
    }

    /**
     * Sets the experience multiplier booster upgrade value.
     *
     * @param expMultiplyBooster float representing the experience multiplier booster upgrade value.
     */
    public void setEXPMultiplierBooster(final float expMultiplyBooster) {
        this.expMultiplierBooster = expMultiplyBooster;
    }

    /**
     * Sets the health regeneration multiplier booster upgrade value.
     *
     * @param healthRegenMultiplierBooster float representing the health regeneration multiplier booster upgrade value.
     */
    public void setHealthRegenMultiplierBooster(final float healthRegenMultiplierBooster) {
        this.healthRegenMultiplierBooster = healthRegenMultiplierBooster;
    }

    /**
     * Sets the maximum health booster upgrade value.
     *
     * @param maxHealthBooster int representing the maximum health booster upgrade value.
     */
    public void setMaxHealthBooster(final int maxHealthBooster) {
        this.maxHealthBooster = maxHealthBooster;
    }

    /**
     * Sets the speed booster upgrade value.
     *
     * @param speedBooster int representing the speed booster upgrade value.
     */
    public void setSpeedBooster(final int speedBooster) {
        this.speedBooster = speedBooster;
    }

    /**
     * Sets the current currency value.
     *
     * @param currency int representing the current currency value.
     */
    public void setCurrency(final int currency) {
        this.currency = currency;
    }


    /**
     * Applies the player upgrades stored in this profile to the Player object.
     *
     * @param player Player object representing the player that the upgrades apply to.
     */
    public void applyPlayerUpgrades(final Player player) {
        player.setMaxHealth(player.getMaxHealth() + this.maxHealthBooster);
        player.setHealth(player.getMaxHealth());
        player.setAttack(player.getAttack() + this.attackBooster);
        player.setDefense(player.getDefense() + this.defenseBooster);
        player.setSpeed(player.getSpeed() + this.speedBooster);
        player.setEXPMultiplier(player.getEXPMultiplier() + this.expMultiplierBooster);
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
        this.expMultiplierBooster = 0;
        this.currencyMultiplierBooster = 0;
        this.attackSpeedBooster = 0;
        this.critRateBooster = 0;
        this.critMultiplierBooster = 0;
        this.healthRegenMultiplierBooster = 0;
        this.currency = 0;
    }

    /**
     * Saves the current state of the player profile to a file.
     * The profile is saved as a serialized object to enable loading later.
     */
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

    /**
     * Returns a string representation of the PlayerProfile object.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "PlayerProfile{"
                + "maxHealthBooster=" + maxHealthBooster
                + ", attackBooster=" + attackBooster
                + ", defenseBooster=" + defenseBooster
                + ", speedBooster=" + speedBooster
                + ", expMultiplierBooster=" + expMultiplierBooster
                + ", currencyMultiplierBooster=" + currencyMultiplierBooster
                + ", attackSpeedBooster=" + attackSpeedBooster
                + ", critRateBooster=" + critRateBooster
                + ", critMultiplierBooster=" + critMultiplierBooster
                + ", healthRegenMultiplierBooster=" + healthRegenMultiplierBooster
                + ", currency=" + currency
                + '}';
    }
}
