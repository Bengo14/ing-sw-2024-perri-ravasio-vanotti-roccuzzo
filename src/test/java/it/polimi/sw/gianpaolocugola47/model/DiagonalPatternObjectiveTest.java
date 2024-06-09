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
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),3,1);
        main.playCardAndUpdatePoints(1,11,11,3,1);
        main.playCardAndUpdatePoints(2,12,12,3,1);
        assertEquals(2, obj.checkPatternAndComputePoints(player));

        DiagonalPatternObjective obj2 = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(8);
        ResourceCard fungi_1 = Deck.getResourceCardsDeck().get(10);
        ResourceCard fungi_2 = Deck.getResourceCardsDeck().get(13);
        ResourceCard fungi_3 = Deck.getResourceCardsDeck().get(14);
        ResourceCard fungi_4 = Deck.getResourceCardsDeck().get(19);
        main.getPlayerTable(1).setCardOnHandInTheEmptyPosition(fungi_1);
        main.getPlayerTable(1).setCardOnHandInTheEmptyPosition(fungi_2);
        main.getPlayerTable(1).setCardOnHandInTheEmptyPosition(fungi_3);
        main.setPlayerSecretObjective(1, obj2);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,1);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos()-1,PlayerTable.getStartingCardPos()+1,1,1);
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos()-2,PlayerTable.getStartingCardPos()+2,1,1);
        main.getPlayerTable(1).setCardOnHandInTheEmptyPosition(fungi_4);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos()-3,PlayerTable.getStartingCardPos()+3,1,1);
        assertEquals(2, obj2.checkPatternAndComputePoints(player));
    }

}
