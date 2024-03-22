package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class CornerTest {
    @Test
    public void testConstructorAndGetterCorner() {
        Corner c = new Corner(false,false);
        assertNotNull(c);
        assertFalse(c.isBuildable());
        assertFalse(c.isEmpty());
        assertFalse(c.isCovered());
        assertTrue(c.isCovered());
        assertNull(c.getLinkedCorner());
        c.setLinkedCorner(new Corner(true,true));
        assertNotNull(c.getLinkedCorner());
    }
    @Test
    public void testSetIsCovered() {
        Corner c = new Corner(false,false);
        assertFalse(c.isCovered());
        c.setIsCovered();
        assertTrue(c.isCovered());
    }
    @Test
    void getLinkedCorner() {
    }
}