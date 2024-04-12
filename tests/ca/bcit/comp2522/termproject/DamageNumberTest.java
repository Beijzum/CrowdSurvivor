package ca.bcit.comp2522.termproject;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DamageNumberTest {

    @Test
    public void isExpired() {
        DamageNumber damageNumber = new DamageNumber(100, 100, 50, false);

        assertFalse(damageNumber.isExpired());

        float deltaTime = damageNumber.getLifetime() + 1f;
        damageNumber.update(deltaTime);

        assertTrue(damageNumber.isExpired());
    }
}