package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceableCardTest {

    @Test
    public void switchFrontBack() {
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        assert(p.getIsFront());
        p.switchFrontBack();
        assert(!p.getIsFront());
    }

    @Test
    public void testSetFront(){
        Deck.initDeck();
        PlaceableCard p = Deck.drawCardFromResourceDeck();
        p.setFront(true);
        assertTrue(p.getIsFront());
    }

    @Test
    public void testFlaggedForObjective(){
        Deck.initDeck();
        PlaceableCard p = Deck.drawCardFromResourceDeck();
        p.setFlaggedForObjective(true);
        assertTrue(p.getIsFlaggedForObjective());
    }

    @Test
    public void testGetVisibleCorner(){
        Deck.initDeck();
        PlaceableCard p = Deck.drawCardFromResourceDeck();
        Corner[] c = p.getVisibleCorners();
        assertEquals(4,c.length);
        for (int i = 0; i < 4; i++) {
            assertEquals(c[i], p.getVisibleCorners()[i]);
        }
        p.switchFrontBack();
        c = p.getVisibleCorners();
        for (int i = 0; i < 4; i++) {
            assertEquals(c[i], p.getVisibleCorners()[i]);
        }
    }

    @Test
    public void testSetCoordinates(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        p.setCoordinates(1, 2);
        assert(p.getLine() == 1);
        assert(p.getColumn() == 2);
    }

    @Test
    public void testGetImgPath(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        assert(p.getBackImgPath().equals("back"));
        assert(p.getFrontImgPath().equals("front"));
    }
    @Test
    public void testGetCorners(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        Corner[] c = p.getCorners();
        assertEquals(8,c.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(c[i], p.getCorners()[i]);
        }
    }
    @Test
    public void testGetPoints(){
        PlayerTable player = new PlayerTable();
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        assertEquals(0,p.getPoints(player));
    }

}