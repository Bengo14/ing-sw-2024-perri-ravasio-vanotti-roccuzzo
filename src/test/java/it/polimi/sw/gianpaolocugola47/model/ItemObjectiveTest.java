package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class ItemObjectiveTest {
    @Test
    public void testConstructorAndGetterItemObjective() {
        ArrayList<Items> items = new ArrayList<>();
        items.add(Items.QUILL);
        ItemObjective i = new ItemObjective(5, "imgPathFront", "imgPathBack", items);
        assertNotNull(i);
        assertEquals(5, i.getPoints());
        assertEquals("imgPathFront", i.getImgPathFront());
        assertEquals("imgPathBack", i.getImgPathBack());
        assertEquals(items, i.getItemsRequired());
    }
}