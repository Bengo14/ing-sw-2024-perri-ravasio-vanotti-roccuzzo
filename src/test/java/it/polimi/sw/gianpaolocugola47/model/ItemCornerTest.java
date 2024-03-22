package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCornerTest {
    @Test
    void getItem() {
        ItemCorner itemCorner = new ItemCorner(Items.MANUSCRIPT);
        assertEquals(Items.MANUSCRIPT, itemCorner.getItem());
    }
    @Test
    void testConstructor() {
        ItemCorner itemCorner = new ItemCorner(Items.MANUSCRIPT);
        assertNotNull(itemCorner);
    }
}