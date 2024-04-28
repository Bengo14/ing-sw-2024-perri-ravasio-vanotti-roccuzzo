package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    @Test
    public void testConstructorAndGetterResourceCard() {
        Deck.initDeck();
        ResourceCard res = new ResourceCard("back", "front", 1, Resources.PLANT);// create the card in front position
        assertNotNull(res);
        assertEquals(Resources.PLANT, res.getResourceCentreBack());
        //assertEquals(0, res.getThisPoints());
        //res.switchFrontBack();
        assertTrue(res.isFront());
        System.out.println(res.getThisPoints());
        assertEquals(1, res.getThisPoints());
    }
    @Test
    public void testUpdateResourceCounterAndGetPoints() {
        MainTable main = new MainTable();
        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(4);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        ResourceCard res = Deck.getResourceCardsDeck().get(2);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1,plant_2,res});
        main.setPlayerTable(1,player);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0,29,29,1,1);
        plant_2.switchFrontBack();
        main.playCardAndUpdatePoints(1,29,29,3,1);
        res.switchFrontBack();
        main.playCardAndUpdatePoints(2,29,29,0,1);
        System.out.println(obj.checkPatternAndComputePoints(player));
        assertEquals(2,obj.checkPatternAndComputePoints(player));
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
        main.playCardAndUpdatePoints(0,29,29,0,1);
        assertEquals(0,res.getThisPoints());
        assertEquals(0,res.getPoints(player));
    }

}