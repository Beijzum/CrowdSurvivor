package ca.bcit.comp2522.termproject;

final public class PlayerProfile {
    private int maxHealthBooster;
    private int attackBooster;
    private double defenseBooster;
    private int speedBooster;
    private float EXPMultiplierBooster;
    private float currencyMultiplierBooster;
    private double attackSpeedBooster;
    private double critRateBooster;
    private double critMultiplierBooster;
    private float healhRegenMultiplierBooster;
    private static PlayerProfile instance = null;

    private PlayerProfile() {
        // do later when serialization is done
        if (!loadProfile()) {
            setThisToDefault();
        }
    }

    public static PlayerProfile createPlayerProfile() {
        if (instance == null) {
            instance = new PlayerProfile();
        }
        return instance;
    }

    public void saveProfile() {
        // implement later, saves upgrades into file
    }

    public void applyPlayerUpgrades(Player player) {
        player.setMaxHealth(player.getMaxHealth() + this.maxHealthBooster);
        player.setAttack(player.getAttack() + this.attackBooster);
        player.setDefense(player.getDefense() + this.defenseBooster);
        player.setSpeed(player.getSpeed() + this.speedBooster);
        player.setEXPMultiplier(player.getEXPMultiplier() + this.EXPMultiplierBooster);
        player.setCurrencyMultiplier(player.getCurrencyMultipler() + this.currencyMultiplierBooster);
        player.setAttackSpeed(player.getAttackSpeed() + this.attackSpeedBooster);
        player.setCritRate(player.getCritRate() + this.critRateBooster);
        player.setCritMultiplier(player.getCritMultiplier() + this.critMultiplierBooster);
        player.setHealthRegenMultiplier(player.getHealthRegenMultiplier() + this.healhRegenMultiplierBooster);
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
        this.healhRegenMultiplierBooster = 0;
    }

    private boolean loadProfile() {
        // implement later, gets permanent upgrades from file and loads values into instance variables, call in constructor
        // if file couldn't be loaded, then returns false
        return false;
    }
}
