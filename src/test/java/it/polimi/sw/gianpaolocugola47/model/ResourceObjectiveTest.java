package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceObjectiveTest {

    @Test
    public void testConstructorPlant(){
        Deck.initDeck();
        ResourceObjective plant = (ResourceObjective) Deck.getObjectiveCardsDeck().get(9);
        assertEquals(Resources.PLANT,plant.getResource());
        assertNotNull(plant);
    }

    @Test
    public void testConstructorInsect(){
        Deck.initDeck();
        ResourceObjective insect = (ResourceObjective) Deck.getObjectiveCardsDeck().get(11);
        assertEquals(Resources.INSECTS,insect.getResource());
        assertNotNull(insect);
    }

    @Test
    public void testConstructorFungi(){
        Deck.initDeck();
        ResourceObjective fungi = (ResourceObjective) Deck.getObjectiveCardsDeck().get(8);
        assertEquals(Resources.FUNGI,fungi.getResource());
        assertNotNull(fungi);
    }

    @Test
    public void testConstructorAnimal(){
        Deck.initDeck();
        ResourceObjective animal = (ResourceObjective) Deck.getObjectiveCardsDeck().get(10);
        assertEquals(Resources.ANIMAL,animal.getResource());
        assertNotNull(animal);
        ResourceObjective obj = new ResourceObjective("front","back",Resources.ANIMAL);
    }

    @Test
    public void checkPatternAndComputePoints() {
        MainTable main = new MainTable();
        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(1);
        Objectives obj = Deck.getObjectiveCardsDeck().get(11);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(32);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(35);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(36);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{res_1, res_2,res_3});
        main.setPlayerTable(1,player);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
        res_1.setFront(true);
        res_2.setFront(true);
        res_3.setFront(true);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,1);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,1);
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),3,1);
        assertEquals(2,obj.checkPatternAndComputePoints(player));
   }

}