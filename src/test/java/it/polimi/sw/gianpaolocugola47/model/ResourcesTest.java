package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesTest {

    @Test
    void test() {
        Resources resource = Resources.ANIMAL;
        assertEquals("blue", resource.getColour());
        assertEquals("", resource.getImgPath());
        assertEquals("\033[0;34m", resource.getAsciiEscape());
        assertEquals('A', resource.getSymbol());
    }
}