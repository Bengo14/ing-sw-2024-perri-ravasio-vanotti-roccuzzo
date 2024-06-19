package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    public void testConstructorAndGetterCorner() {
        Corner c = new Corner(false, false, false,null, Resources.INSECTS);
        assertNotNull(c);
        assertFalse(c.isBuildable());
        assertFalse(c.isItem());
        assertFalse(c.isResource());
        assertFalse(c.isCovered());
        assertNull(c.getLinkedCorner());
        c.setLinkedCorner(new Corner(true,true,true,null,null));
        assertNotNull(c.getLinkedCorner());
    }

    @Test
    public void testSetIsCovered() {
        Corner c = new Corner(false,false,false,Items.INKWELL,null);
        assertFalse(c.isCovered());
        c.setIsCovered();
        assertTrue(c.isCovered());
    }

    @Test
    public void testGetResourceAndGetItem() {
        Corner c_resource = new Corner(false,false,true,null,Resources.INSECTS);
        assertEquals(Resources.INSECTS, c_resource.getResource());
       Corner c_item = new Corner(false,true,false,Items.INKWELL,null);
       assertEquals(Items.INKWELL, c_item.getItem());
    }

}