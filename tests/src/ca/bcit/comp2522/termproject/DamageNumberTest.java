package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DamageNumberTest {

    private DamageNumber damageNumber;

    @BeforeEach
    public void setUp() {
        damageNumber = new DamageNumber(100, 100, 50, false);
    }

    @Test
    public void testIsNotExpired() {
        assertFalse(damageNumber.isExpired());
    }

    @Test
    public void testIsExpired() {
        float defaultLifeTime = 1.0f;
        damageNumber.update(defaultLifeTime);

        assertTrue(damageNumber.isExpired());
    }

}