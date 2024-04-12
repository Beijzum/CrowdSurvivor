package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HPBarTest {

    @Test
    public void testSetPosition() {
        HPBar hpBar = new HPBar(0, 0, 200, 20, 100);

        hpBar.setPosition(50, 50);

        assertEquals(50, hpBar.getX());
        assertEquals(50, hpBar.getY());
    }
}
