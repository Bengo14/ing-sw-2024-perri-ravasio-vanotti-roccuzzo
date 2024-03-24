package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectivesTest {
    @Test
    public void testConstructorAndGetterObjectives() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack") {
            @Override
            public int checkPatternAndComputePoints(PlayerTable playerTable) {
                return 0;
            }
        };
        assertNotNull(o);
        assertEquals(5, o.getPoints());
        assertEquals("imgPathFront", o.getImgPathFront());
        assertEquals("imgPathBack", o.getImgPathBack());
    }
}