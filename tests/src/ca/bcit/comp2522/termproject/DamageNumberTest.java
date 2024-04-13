package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DamageNumberTest {

    @Test
    public void testIsExpired() {
        DamageNumber damageNumber = new DamageNumber(100, 100, 50, false);

        assertFalse(damageNumber.isExpired());

        float defaultLifeTime = 1.0f;

        damageNumber.update(defaultLifeTime);

        assertTrue(damageNumber.isExpired());
    }
}