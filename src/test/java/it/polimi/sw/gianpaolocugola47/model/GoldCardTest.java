package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    @Test
    public void testConstructorAndGetterGoldCard() {
        GoldCard g = new GoldCard("back", "front", 2, true, Resources.FUNGI, null, false, null);
        assertNotNull(g);
        assertEquals(Resources.FUNGI, g.getResourceCentreBack());
        assertTrue(g.isPointsForCorners());
        assertFalse(g.isPointsForItems());
        assertNotEquals(Items.INKWELL, g.getItemRequired());
    }
    @Test
    public void testGetresourcedRequired() {
        Deck.initDeck();
        GoldCard gold = Deck.getGoldCardsDeck().get(0);
        gold.getResourcesRequired();
        System.out.println(gold.getResourcesRequired());
    }

    @Test
    public void testGetCardPointForPlacing() {
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(0);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(12);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        main.setNumOfPlayers(2);
        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_1,plant_2,g});
        main.setPlayerTable(1,p);
        main.turnCardOnHand(1,1, true);
        main.turnCardOnHand(1,0, true);
        main.turnCardOnHand(1,2, true);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0,10,10,0,1);
        main.playCardAndUpdatePoints(1,10,10,1,1);
        main.playCardAndUpdatePoints(2,10,10,2,1);

        assertEquals(5, main.getBoardPoints(1));
    }
    @Test
    public void testGetCardPointForCorners() {
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(11);
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        start.switchFrontBack();
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        main.setNumOfPlayers(2);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{plant_1, plant_2, g});
        main.setPlayerTable(1, player);
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1, obj);
        main.turnCardOnHand(1,2, true);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 0, 1);
        main.playCardAndUpdatePoints(1, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 1);
        main.playCardAndUpdatePoints(2, 9, 9, 1, 1);
        assertEquals(4, main.getBoardPoints(1));
    }
    @Test
    public void testGetCardPointForItems(){
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(3);
        StartingCard start = Deck.getStartingCardsDeck().get(1);
        start.switchFrontBack();
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        main.setNumOfPlayers(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(23);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(16);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{res_1, res_2, g});
        main.setPlayerTable(1, player);
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1, obj);
        main.turnCardOnHand(1,2, true);
        main.turnCardOnHand(1,0, true);
        main.turnCardOnHand(1,1, true);
        main.playCardAndUpdatePoints(1, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 1);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 3, 1);
        main.playCardAndUpdatePoints(2, 9, 11, 0, 1);
        assertEquals(2, main.getBoardPoints(1));
        assertEquals(2,player.getResourceCounter(6));
    }
    @Test
    public void testIsNotFront(){
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(0);
        StartingCard start = Deck.getStartingCardsDeck().get(1);
        start.switchFrontBack();
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{g});
        main.setPlayerTable(1, player);
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 1);
        assertEquals(0, main.getBoardPoints(1));
    }
    @Test
    public void testUpdateResourceCounter(){
       MainTable main = new MainTable();
       Deck.initDeck();
       GoldCard g = Deck.getGoldCardsDeck().get(3);
       StartingCard start = Deck.getStartingCardsDeck().get(1);
       start.switchFrontBack();
       main.setNumOfPlayers(2);
       PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{g});
       main.setPlayerTable(1, player);
       Objectives obj = Deck.getObjectiveCardsDeck().get(0);
       main.setPlayerStartingCard(1, start);
       main.setPlayerSecretObjective(1,obj);
       main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 1);
    }


}


