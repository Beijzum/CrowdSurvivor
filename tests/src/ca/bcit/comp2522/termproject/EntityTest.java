package ca.bcit.comp2522.termproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class EntityTest {

    private Entity entity;

    @BeforeEach
    public void setUp() {
        entity = new Entity() {
        };
    }

    @Test
    public void testIsDeadFalse() {
        entity.setHealth(50);
        Assertions.assertFalse(entity.isDead(), "Entity should not be dead when health is greater than 0.");
    }

    @Test
    public void testIsDeadTrue() {
        entity.setHealth(0);
        Assertions.assertTrue(entity.isDead(), "Entity should be dead when health is 0.");
    }

    @Test
    public void testIsDeadTrueNegativeHealth() {
        entity.setHealth(-50);
        Assertions.assertTrue(entity.isDead(), "Entity should be dead when health is less than 0.");
    }
}
