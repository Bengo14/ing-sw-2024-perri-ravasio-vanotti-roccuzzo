package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemsTest {

    @Test
    void testItemsClass() {
        Items item = Items.QUILL;
        assertEquals("quill", item.getName());
        assertEquals("", item.getImgPath());
        assertEquals('q', item.getSymbol());
    }

}