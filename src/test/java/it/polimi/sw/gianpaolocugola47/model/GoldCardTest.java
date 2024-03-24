package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    @Test
    public void testConstructorAndGetterGoldCard() {
        GoldCard g = new GoldCard("back","front",2,true,Resources.FUNGI,null,false,null);
        assertNotNull(g);
        assertEquals(Resources.FUNGI, g.getResourceCentreBack());
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