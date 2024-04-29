package it.polimi.sw.gianpaolocugola47.model;


import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;


class DiagonalPatternObjectiveTest {


    @Test
    public void testConstructorFungi() {
        Deck.initDeck();
        //create the card from json file
        DiagonalPatternObjective f = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(8);
        assertNotNull(f);
        assertEquals(true, f.isAscending());
        assertEquals(Resources.FUNGI, f.getResource());
    }
    @Test
    public void testConstructorAnimal() {
        Deck.initDeck();

        //create the card from json file
        DiagonalPatternObjective a = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(9);
        assertNotNull(a);
        assertEquals(true, a.isAscending());
        assertEquals(Resources.ANIMAL, a.getResource());
    }
    @Test
    public void testConstructorPlant() {
        Deck.initDeck();

        //create the card from json file
        DiagonalPatternObjective p = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(10);
        assertNotNull(p);
        assertEquals(false, p.isAscending());
        assertEquals(Resources.PLANT, p.getResource());
    }
    @Test
    public void testConstructor() {
        DiagonalPatternObjective obj = new DiagonalPatternObjective(2, "imgPathFront", "imgPathBack", false, Resources.INSECTS);
        assertNotNull(obj);
        assertEquals(false, obj.isAscending());
        assertEquals(Resources.INSECTS, obj.getResource());
    }
    @Test
    public void isResourceMatchedAndNotFlagged() {
        Deck.initDeck();

        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(11);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{});



    }
    @Test
    void testIsResourceMatchedAndNotFlagged() {
        Deck.initDeck();

        // Test when the resource matches and the card is not flagged
        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(11);
        ResourceCard resourceCard = Deck.getResourceCardsDeck().get(2);
        assertTrue(obj.isResourceMatchedAndNotFlagged(resourceCard));

        // Test when the resource does not match
        ResourceCard differentResourceCard = new ResourceCard("back", "front", 1, Resources.ANIMAL);
        assertFalse(obj.isResourceMatchedAndNotFlagged(differentResourceCard));

        // Test when the card is flagged
        resourceCard.setFlaggedForObjective(true);
        assertFalse(obj.isResourceMatchedAndNotFlagged(resourceCard));
    }

    @Test
    public void testCheckPatternAndComputePoints(){
        MainTable main = new MainTable();
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(10);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_3 = Deck.getResourceCardsDeck().get(12);
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1, plant_2, plant_3});
        main.setPlayerTable(1, player);
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1, obj);
        main.playCardAndUpdatePoints(0,29,29,1,1);
        main.playCardAndUpdatePoints(1,28,30,3,1);
        main.playCardAndUpdatePoints(2,29,31,3,1);
        System.out.println(obj.checkPatternAndComputePoints(player));
        assertEquals(2, obj.checkPatternAndComputePoints(player));
    }

}
