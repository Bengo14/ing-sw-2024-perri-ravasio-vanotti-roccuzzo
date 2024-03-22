package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectivesTest {
    @Test
    public void testConstructor() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack");
        assertNotNull(o);
        assertEquals(5, o.getPoints());
        assertEquals("imgPathFront", o.getImgPathFront());
        assertEquals("imgPathBack", o.getImgPathBack());
    }


    @Test
    void setPoints() {
    }


    @Test
    public void getItems() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack");
        assertNotNull(o.getItems());
    }

    @Test
    public void setItems() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack");
        o.setItems(null);
        assertNull(o.getItems());
    }

    @Test
    public void getResources() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack");
        assertNotNull(o.getResources());
    }

    @Test
    public void setResources() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack");
        o.setResources(null);
        assertNull(o.getResources());
    }

    @Test
    public void getPattern() {
        Objectives o = new Objectives(5, "imgPathFront", "imgPathBack");
        assertNotNull(o.getPattern());
    }

    @Test
    public void checkPatternAndComputePoints() {

    }
}