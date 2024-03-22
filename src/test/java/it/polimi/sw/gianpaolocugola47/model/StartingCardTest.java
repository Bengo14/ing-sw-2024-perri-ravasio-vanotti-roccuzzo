package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StartingCardTest {
    @Test
    public void testConstructor( ){
        StartingCard sc = new StartingCard("back", "front");
        assertNotNull(sc);
    }
    @Test
    void getResourcesCentreBack() {
    }

    @Test
    void setResourcesCentreBack() {
    }
}