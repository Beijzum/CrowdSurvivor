package ca.bcit.comp2522.termproject;

public class Hitbox {

    private final int width;
    private final int height;
    private int topLeftXCoord;
    private int topLeftYCoord;

    Hitbox (int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTopLeftXCoord() {
        return this.topLeftXCoord;
    }

    public int getTopLeftYCoord() {
        return this.topLeftYCoord;
    }

    public void updateBoxPosition(final int newTopLeftXCoord, final int newTopLeftYCoord) {
        this.topLeftXCoord = newTopLeftXCoord;
        this.topLeftYCoord = newTopLeftYCoord;
    }

    public boolean overlaps(final Hitbox hitbox) {
        final int thisBoxWidthRange = this.topLeftXCoord + this.width;
        final int thisBoxHeightRange = this.topLeftYCoord + this.height;

        if (hitbox.getTopLeftXCoord() > this.topLeftXCoord && hitbox.getTopLeftXCoord() < thisBoxWidthRange) {
            return true;
        }
        if (hitbox.getTopLeftYCoord() > this.topLeftYCoord && hitbox.getTopLeftYCoord() < thisBoxHeightRange) {
            return true;
        }
        return false;
    }
}
