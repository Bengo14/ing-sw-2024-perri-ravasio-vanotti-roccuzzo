package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LShapePatternObjectiveTest {


    @Test
    public void testConstructor() {
        LShapePatternObjective obj = new LShapePatternObjective(3, "imgPathFront", "imgPathBack", "topLeft", Resources.PLANT, Resources.INSECTS);
        assertEquals(3, obj.getPoints());
        assertEquals("imgPathFront", obj.getImgPathFront());
        assertEquals("imgPathBack", obj.getImgPathBack());

    }
    @Test
    public void testCheckPatternAndComputePointsBottomLeft(){
        MainTable main = new MainTable();

        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(10);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);
        ResourceCard insect = Deck.getResourceCardsDeck().get(30);
        LShapePatternObjective obj = (LShapePatternObjective) Deck.getObjectiveCardsDeck().get(5);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{plant_1, plant_2, insect});
        main.setPlayerTable(1,player);
        main.setPlayerSecretObjective(1,obj);
        start.switchFrontBack();
        main.setPlayerStartingCard(1,start);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,1);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),2,1);
        main.playCardAndUpdatePoints(2,11,9,2,1);
        System.out.println(obj.checkPatternAndComputePoints(player));
        assertEquals(3, obj.checkPatternAndComputePoints(player));
    }

    @Test
    public void testCheckPatternAndComputePointsTopRight(){
        MainTable main = new MainTable();

        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        ResourceCard animal_1 = Deck.getResourceCardsDeck().get(20);
        ResourceCard animal_2 = Deck.getResourceCardsDeck().get(21);
        ResourceCard fungi = Deck.getResourceCardsDeck().get(0);
        LShapePatternObjective obj = (LShapePatternObjective) Deck.getObjectiveCardsDeck().get(6);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{animal_1, animal_2, fungi});
        main.setPlayerTable(1,player);
        main.setPlayerSecretObjective(1,obj);
        start.switchFrontBack();
        main.setPlayerStartingCard(1,start);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,1);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),2,1);
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos()-1,PlayerTable.getStartingCardPos()-1,1,1);
        System.out.println(obj.checkPatternAndComputePoints(player));
        assertEquals(3, obj.checkPatternAndComputePoints(player));
    }
 }