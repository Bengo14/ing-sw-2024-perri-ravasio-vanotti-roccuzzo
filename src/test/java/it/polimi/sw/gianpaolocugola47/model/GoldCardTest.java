package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    @Test
    public void testConstructorAndGetterGoldCard() {
        GoldCard g = new GoldCard("back", "front", 5, true, Resources.FUNGI, Items.INKWELL, true, Items.INKWELL);
        assertNotNull(g);
        assertEquals(Resources.FUNGI, g.getResourceCentreBack());
        assertEquals(Items.INKWELL, g.getItemRequired());
        assertTrue(g.isPointsForCorners());
        assertTrue(g.isPointsForItems());
        assertEquals(Items.INKWELL, g.getItemThatGivesPoints());
    }

    @Test
    void getResourcesRequired() {
    }
    @Test
    void setResourcesRequired() {
    }

}