package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {

    @Test
    public void testConstructorAndGetterResourceCard() {
        Deck.initDeck();
        ResourceCard resourceCard = new ResourceCard("backPath", "frontPath", 3,Resources.PLANT);
        ResourceCard res = Deck.getResourceCardsDeck().get(28);
        res.setFront(true);
        assertNotNull(res);
        assertEquals(Resources.ANIMAL, res.getResourceCentreBack());
        assertTrue(res.getIsFront());
        assertEquals(1, res.getThisPoints());
    }

    @Test
    public void testUpdateResourceCounterAndGetPoints() {
        MainTable main = new MainTable();
        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(4);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(17);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(18);
        ResourceCard res = Deck.getResourceCardsDeck().get(4);
        plant_1.switchFrontBack();
        plant_2.switchFrontBack();

        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1,plant_2,res});
        main.setPlayerTable(1,player);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,1);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),3,1);
        res.switchFrontBack();
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,1);
        assertEquals(2,main.getBoardPoints(1));
    }

    @Test
    public void testElse(){
        Deck.initDeck();
        ResourceCard res = Deck.drawCardFromResourceDeck();
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(4);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{res});
        main.setPlayerTable(1,player);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,1);
        assertEquals(0,res.getThisPoints());
        assertEquals(0,res.getPoints(player));
    }

}