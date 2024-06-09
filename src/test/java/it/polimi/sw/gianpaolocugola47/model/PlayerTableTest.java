package it.polimi.sw.gianpaolocugola47.model;


import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;


class PlayerTableTest {



    @Test
    void setCardOnHandInTheEmptyPosition() {
        Deck.initDeck();
        ResourceCard plant_0 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
        p.setCardOnHandInTheEmptyPosition(Deck.getResourceCardsDeck().get(0));
        assertNotNull(p.getCardOnHand(2));
    }

    @Test
    public void testIsPlaceable(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);


        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{});
        p.setStartingCard(start);
        start.setCoordinates(PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos());

        boolean val = p.isPlaceable(9,9);

        System.out.println(val);


        assertTrue(p.isPlaceable(9, 9));


    }


    @Test
    public void testConstructorAndGetterPlayerTable(){
        Deck.initDeck();
        ResourceCard plant_0 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);

        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_0,plant_1,plant_2});

        assertNotNull(p);
        assertEquals(1, p.getId());
        assertEquals("name", p.getNickName());
        assertEquals(0, p.getResourceCounter(0));
        assertNotNull(p.getCardOnHand(0));
    }
    @Test
    public void testCheckAndPlaceCard(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard res = Deck.getResourceCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{res});
        p.setStartingCard(start);

      //  assertNotNull(p.getPlacedCard(28,30));
        p.checkAndPlaceCard(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 3);
        assertNotNull(p.getPlacedCard(11,11));
    }
    @Test
    public void testPlaceStartingCard(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{});
        p.setStartingCard(start);
        assertNotNull(p.getPlacedCard(PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos()));
        System.out.println(p.getPlacedCard(PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos()));
    }
    @Test
    public void testIsCheap(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        ResourceCard plant_0 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(7);
        GoldCard gold = Deck.getGoldCardsDeck().get(2);
        start.switchFrontBack();
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,gold});
        p.setStartingCard(start);

        p.checkAndPlaceCard(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 3);
        p.checkAndPlaceCard(1, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1);
        gold.switchFrontBack();
        p.checkAndPlaceCard(2, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 0);
    }
    @Test
    public void testGetCardsOnHand(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard res = Deck.getResourceCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{res});
        p.setStartingCard(start);
        assertNotNull(p.getCardsOnHand());
        p.checkAndPlaceCard(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 3);
        assertNotNull(p.getPlacedCards());
    }


}