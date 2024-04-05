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
    private float healthRegenMultiplierBooster;
    private int currency;
    private static PlayerProfile instance = null;

    // getters and setters to be used in shop
    public double getAttackSpeedBooster() {
        return attackSpeedBooster;
    }

    public double getCritMultiplierBooster() {
        return critMultiplierBooster;
    }

    public double getCritRateBooster() {
        return critRateBooster;
    }

    public double getDefenseBooster() {
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

    public void setAttackSpeedBooster(double attackSpeedBooster) {
        this.attackSpeedBooster = attackSpeedBooster;
    }

    public void setCritMultiplierBooster(double critMultiplierBooster) {
        this.critMultiplierBooster = critMultiplierBooster;
    }

    public void setCritRateBooster(double critRateBooster) {
        this.critRateBooster = critRateBooster;
    }

    public void setCurrencyMultiplierBooster(float currencyMultiplierBooster) {
        this.currencyMultiplierBooster = currencyMultiplierBooster;
    }

    public void setDefenseBooster(double defenseBooster) {
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
    }

    private boolean loadProfile() {
        // implement later, gets permanent upgrades from file and loads values into instance variables, call in constructor
        // if file couldn't be loaded, then returns false
        return false;
    }
}
