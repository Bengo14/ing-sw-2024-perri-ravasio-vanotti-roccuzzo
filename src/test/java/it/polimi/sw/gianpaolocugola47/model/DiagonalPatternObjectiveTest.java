package it.polimi.sw.gianpaolocugola47.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiagonalPatternObjectiveTest {
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
