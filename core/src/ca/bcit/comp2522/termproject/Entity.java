package ca.bcit.comp2522.termproject;

/**
 * Represents the abstract Entity class, which serves as the base class for all entities in the game.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public abstract class Entity {
    /**
     * The health of the entity.
     */
    protected int health;
    /**
     * The maximum health of the entity.
     */
    protected int maxHealth;
    /**
     * The speed of the entity.
     */
    protected int speed;
    /**
     * The attack power of the entity.
     */
    protected int attack;
    /**
     * The defense of the entity.
     */
    protected float defense;

    /**
     * Retrieves the attack power of the entity.
     *
     * @return the attack power of the entity.
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Retrieves the defense of the entity.
     *
     * @return the defense value of the entity.
     */
    public float getDefense() {
        return defense;
    }

    /**
     * Retrieves the current health of the entity.
     *
     * @return the current health value of the entity.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Retrieves the maximum health of the entity.
     *
     * @return the maximum health value of the entity.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Retrieves the speed of the entity.
     *
     * @return the speed value of the entity.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the attack power of the entity.
     *
     * @param attack int representing the new attack power value.
     */
    public void setAttack(final int attack) {
        this.attack = attack;
    }

    /**
     * Sets the defense of the entity.
     *
     * @param defense float representing the new defense value.
     */
    public void setDefense(final float defense) {
        this.defense = defense;
    }

    /**
     * Sets the current health of the entity.
     *
     * @param health int representing the new health value.
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Sets the maximum health of the entity.
     *
     * @param maxHealth int representing the new maximum health value.
     */
    public void setMaxHealth(final int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Sets the speed of the entity.
     *
     * @param speed int representing the new speed value.
     */
    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    /**
     * Checks if the entity is dead based on its current health.
     *
     * @return true if the entity is dead, false otherwise.
     */
    public boolean isDead() {
        return this.health <= 0;
    }

    /**
     * Returns a string representation of the abstract Entity class.
     *
     * @return toString description.
     */
    @Override
    public String toString() {
        return "Entity{"
                + "health=" + this.health
                + ", maxHealth=" + this.maxHealth
                + ", speed=" + this.speed
                + ", attack=" + this.attack
                + ", defense=" + this.defense
                + '}';
    }
}
