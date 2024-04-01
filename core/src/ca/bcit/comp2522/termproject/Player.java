package ca.bcit.comp2522.termproject;

public class Player extends Entity {
    private static Player instance = null;
    private int attackSpeed;
    private float critRate;
    private float critMultiplier;
    private int ultimateCD;
    private float healthRegenMultiplier;


    Player() {

    }

    public static Player createPlayer() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    @Override
    public void move(int x, int y) {

    }
}
