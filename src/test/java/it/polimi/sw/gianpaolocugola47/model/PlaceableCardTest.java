package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

class PlaceableCardTest {
   /*@Test
    public void testConstructor() {
    PlaceableCard p = new PlaceableCard("back", "front");
    assertNotNull(p);

    }*/


    @Test
    void switchFrontBack() {
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        assert(p.isFront());
        p.switchFrontBack();
        assert(!p.isFront());
    }
    @Test
    public void testGetvisibleCorner(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        Corner[] c = p.getVisibleCorners();
        for (int i = 0; i < 4; i++) {
            assert(c[i] == p.getCorners()[i]);
        }
    }
    @Test
    public void testGetcorner(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        Corner[] c = p.getCorners();
        for (int i = 0; i < 8; i++) {
            assert(c[i] == p.getCorners()[i]);
        }
    }
}