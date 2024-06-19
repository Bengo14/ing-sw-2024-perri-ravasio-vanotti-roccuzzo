package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class PlayerTableTest {

    @Test
    public void testConstructor() {
        PlayerTable p = new PlayerTable(0);
        assertEquals(0, p.getId());
        assertEquals("",p.getNickName());
        assertTrue(p.getCanPlay());
        assertNotNull(p.getResourceCounter());
        assertNotNull(p.getCardsOnHand());
        assertNotNull(p.getPlacedCards());
    }

    @Test
    public void setCardOnHandInTheEmptyPosition() {
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
        assertTrue(p.isPlaceable(PlayerTable.STARTING_CARD_POS-1, PlayerTable.STARTING_CARD_POS-1));
    }

    @Test
    public void testConstructorAndGetterPlayerTable(){
        Deck.initDeck();
        ResourceCard plant_0 = Deck.getResourceCardsDeck().get(14);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(17);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(18);

        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_0,plant_1,plant_2});

        assertNotNull(p);
        assertEquals(1, p.getId());
        assertEquals("name", p.getNickName());
        assertEquals(0, p.getResourceCounter(0));
        assertNotNull(p.getCardOnHand(2));
    }

    @Test
    public void testCheckAndPlaceCard(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard res = Deck.getResourceCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{res});
        p.setStartingCard(start);

        p.checkAndPlaceCard(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 3);
        assertNotNull(p.getPlacedCard(PlayerTable.STARTING_CARD_POS+1,PlayerTable.STARTING_CARD_POS+1));
    }

    @Test
    public void testPlaceStartingCard(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{});
        p.setStartingCard(start);
        assertNotNull(p.getPlacedCard(PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos()));
    }

    @Test
    public void testIsCheap(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        ResourceCard fungi_0 = Deck.getResourceCardsDeck().get(6);
        ResourceCard fungi_1 = Deck.getResourceCardsDeck().get(7);
        GoldCard gold = Deck.getGoldCardsDeck().get(2);
        start.switchFrontBack();
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{fungi_0, fungi_1,gold});
        p.setStartingCard(start);

        p.checkAndPlaceCard(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 3);
        p.checkAndPlaceCard(1, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1);
        gold.setFront(true);
        p.checkAndPlaceCard(2, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 0);
        assertNotNull(p.getPlacedCard(PlayerTable.getStartingCardPos()-1,PlayerTable.getStartingCardPos()-1));
    }

    @Test
    public void testGetCardsOnHand(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard res = Deck.getResourceCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{res});
        p.setStartingCard(start);
        assertNotNull(p.getCardsOnHand());
        p.checkAndPlaceCard(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 0);
        assertNotNull(p.getCardsOnHand());
    }

}