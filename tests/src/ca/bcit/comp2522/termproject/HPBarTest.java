package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HPBarTest {

    @Test
    public void testSetPosition() {
        // Create an instance of HPBar with initial position (0, 0)
        HPBar hpBar = new HPBar(0, 0, 200, 20, 100);

        // Set new position
        hpBar.setPosition(50, 50);

        // Verify the new position
        assertEquals(50, hpBar.getX());
        assertEquals(50, hpBar.getY());
    }
}
