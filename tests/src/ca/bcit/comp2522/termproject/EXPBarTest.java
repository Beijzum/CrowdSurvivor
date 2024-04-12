package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EXPBarTest {

    @Test
    void testSetPosition() {
        EXPBar expBar = new EXPBar(0, 0, 200, 20, 1, 100);

        expBar.setPosition(50, 50);

        assertEquals(50, expBar.getX());
        assertEquals(50, expBar.getY());
    }
}