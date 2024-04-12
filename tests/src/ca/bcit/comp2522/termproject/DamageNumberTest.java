package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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