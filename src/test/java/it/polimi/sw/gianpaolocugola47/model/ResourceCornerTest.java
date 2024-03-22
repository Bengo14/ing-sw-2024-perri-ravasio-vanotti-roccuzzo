package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCornerTest {
    @Test
    public void testConstructor() {
        Resources r = Resources.INSECTS;
        ResourceCorner rc = new ResourceCorner(r);
        assertNotNull(rc);
        assertEquals(r, rc.getResource());
    }

}