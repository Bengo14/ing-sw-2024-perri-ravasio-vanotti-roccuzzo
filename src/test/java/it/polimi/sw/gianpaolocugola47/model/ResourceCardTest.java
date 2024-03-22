package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    @Test
    public void testConstructorAndGetterResourceCard() {
        ResourceCard r = new ResourceCard( "back", "front", 1, Resources.PLANT);
        ResourceCard rc = new ResourceCard( "back", "front", 1, Resources.PLANT);
        assertNotNull(rc);
        assertEquals(r, rc.getResourceCentreBack());
        assertEquals(1, rc.getPoints(null));
    }


}