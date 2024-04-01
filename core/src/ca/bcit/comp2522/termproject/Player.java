package ca.bcit.comp2522.termproject;

public class Player extends Entity {
    private Player instance = null;

    Player() {

    }

    public Player createPlayer() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }
}
