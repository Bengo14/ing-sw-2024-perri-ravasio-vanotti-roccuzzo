package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
    public void testGetResourcedRequired() {
        Deck.initDeck();
        GoldCard gold = Deck.getGoldCardsDeck().getFirst();
        ArrayList<Resources> requisites = new ArrayList<>();
        requisites.add(Resources.FUNGI);
        requisites.add(Resources.FUNGI);
        requisites.add(Resources.ANIMAL);
        assertEquals(requisites, gold.getResourcesRequired());
    }

    @Test
    public void testGetItemRequired(){
        Deck.initDeck();
        GoldCard gold = Deck.getGoldCardsDeck().getFirst();
        assertEquals(Items.QUILL, gold.getItemRequired());
    }

    @Test
    public void testGetCardPointForPlacing() {
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(16);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(10);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        start.setFront(true);
        Objectives obj = Deck.getObjectiveCardsDeck().getFirst();
        main.setNumOfPlayers(2);
        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_1,plant_2,g});
        main.setPlayerTable(1,p);
        main.turnCardOnHand(1,0, true);
        main.turnCardOnHand(1,1, true);
        main.turnCardOnHand(1,2, true);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,1);
        assertEquals(0, main.getBoardPoints(1));
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),2,1);
        assertEquals(0, main.getBoardPoints(1));
        main.playCardAndUpdatePoints(2, PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),3,1);
        assertEquals(3, main.getBoardPoints(1));
    }

    @Test
    public void testGetCardPointForCorners() {
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(13);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(10);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);
        StartingCard start = Deck.getStartingCardsDeck().get(3);
        start.setFront(true);
        Objectives obj = Deck.getObjectiveCardsDeck().getFirst();
        main.setNumOfPlayers(2);
        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_1,plant_2,g});
        main.setPlayerTable(1,p);
        main.turnCardOnHand(1,2, true);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),2,1);
        assertEquals(0, main.getBoardPoints(1));
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),3,1);
        assertEquals(0, main.getBoardPoints(1));
        main.playCardAndUpdatePoints(2, PlayerTable.getStartingCardPos()+1,PlayerTable.getStartingCardPos()+1,2,1);
        assertEquals(4, main.getBoardPoints(1));
    }
    @Test
    public void testGetPoints(){
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(19);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(11);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(10);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        start.setFront(true);
        Objectives obj = Deck.getObjectiveCardsDeck().getFirst();
        main.setNumOfPlayers(2);
        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_1,plant_2,g});
        main.setPlayerTable(1,p);
        main.turnCardOnHand(1,2, true);
        main.turnCardOnHand(1,0, true);
        main.turnCardOnHand(1,1, true);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,1);
        System.out.println(Arrays.toString(main.getResourceCounter(1)));
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,1);
        System.out.println(Arrays.toString(main.getResourceCounter(1)));
        assertEquals(0, main.getBoardPoints(1));
        main.playCardAndUpdatePoints(2, PlayerTable.getStartingCardPos()-1,PlayerTable.getStartingCardPos()-1,0,1);
        System.out.println(Arrays.toString(main.getResourceCounter(1)));
        assertEquals(5, main.getBoardPoints(1));

    }

    @Test
    public void testGetCardPointForItems(){
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().getFirst();
        StartingCard start = Deck.getStartingCardsDeck().getFirst();
        start.switchFrontBack();
        Objectives obj = Deck.getObjectiveCardsDeck().getFirst();
        main.setNumOfPlayers(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(4);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{res_1, res_2, g});
        main.setPlayerTable(1, player);
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1, obj);
        main.turnCardOnHand(1,0, true);
        main.turnCardOnHand(1,1, true);
        main.turnCardOnHand(1,2, true);
        main.playCardAndUpdatePoints(1, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 1);
        assertEquals(0, main.getBoardPoints(1));
        main.playCardAndUpdatePoints(2, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 2, 1);
        assertEquals(2,player.getResourceCounter(4));
        assertEquals(2, main.getBoardPoints(1));
    }

    @Test
    public void testIsNotFront(){
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().getFirst();
        StartingCard start = Deck.getStartingCardsDeck().get(1);
        start.switchFrontBack();
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{g});
        main.setPlayerTable(1, player);
        Objectives obj = Deck.getObjectiveCardsDeck().getFirst();
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 1);
        assertEquals(0, main.getBoardPoints(1));
    }
    @Test
    public void testResourcesRequiredToString(){
        Deck.initDeck();
        GoldCard gold = Deck.getGoldCardsDeck().getFirst();
        assertEquals("\u001B[0;31mF\u001B[0m\u001B[0;31mF\u001B[0m\u001B[0;34mA\u001B[0m", gold.resourcesRequiredToString());
    }
    @Test
    public void testPointConditionToString(){
        Deck.initDeck();
        GoldCard gold = Deck.getGoldCardsDeck().get(9);
        assertEquals("", gold.pointConditionToString());
        GoldCard gold2 = Deck.getGoldCardsDeck().get(3);
        assertEquals("C", gold2.pointConditionToString());
        GoldCard gold3 = Deck.getGoldCardsDeck().get(0);
        assertEquals("q", gold3.pointConditionToString());
    }

}