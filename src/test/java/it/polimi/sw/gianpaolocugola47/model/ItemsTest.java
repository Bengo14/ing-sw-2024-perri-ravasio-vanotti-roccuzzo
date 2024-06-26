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
    @Test
    void testGetName() {
        assertEquals("quill", Items.QUILL.getName());
        assertEquals("inkwell", Items.INKWELL.getName());
        assertEquals("manuscript", Items.MANUSCRIPT.getName());
    }
    @Test
    void testGetImgPath() {
        assertEquals("", Items.QUILL.getImgPath());
        assertEquals("", Items.INKWELL.getImgPath());
        assertEquals("", Items.MANUSCRIPT.getImgPath());
    }
    @Test
    void testGetSymbol() {
        assertEquals('q', Items.QUILL.getSymbol());
        assertEquals('i', Items.INKWELL.getSymbol());
        assertEquals('m', Items.MANUSCRIPT.getSymbol());
    }

}