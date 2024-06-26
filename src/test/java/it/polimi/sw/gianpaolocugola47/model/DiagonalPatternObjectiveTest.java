package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiagonalPatternObjectiveTest {

    @Test
    public void testConstructor() {
        DiagonalPatternObjective obj = new DiagonalPatternObjective(2, "imgPathFront", "imgPathBack", false, Resources.INSECTS);
        assertNotNull(obj);
        assertFalse(obj.isAscending());
        assertEquals(Resources.INSECTS, obj.getResource());
    }

    @Test
    void testIsResourceMatchedAndNotFlagged() {
        Deck.initDeck();
        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().getFirst();
        // Test when everything matches
        ResourceCard resourceCard = Deck.getResourceCardsDeck().getFirst();
        assertTrue(obj.isResourceMatchedAndNotFlagged(resourceCard));
        // Test when the Resource does not match
        ResourceCard resourceCard_2 = Deck.getResourceCardsDeck().get(10);
        assertFalse(obj.isResourceMatchedAndNotFlagged(resourceCard_2));
        // Test when the card is flagged
        resourceCard.setFlaggedForObjective(true);
        assertFalse(obj.isResourceMatchedAndNotFlagged(resourceCard));
        // Test when card is null
        assertFalse(obj.isResourceMatchedAndNotFlagged(null));
    }

    @Test
    public void testCheckPatternAndComputePoints(){
        MainTable main = new MainTable();
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().getFirst();
        // testing Ascending
        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().getFirst();
        ResourceCard fungi_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard fungi_2 = Deck.getResourceCardsDeck().get(1);
        ResourceCard fungi_3 = Deck.getResourceCardsDeck().get(2);
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{fungi_1, fungi_2, fungi_3});
        main.setPlayerTable(1, player);
        main.setPlayerStartingCard(1, start);
        main.setPlayerSecretObjective(1, obj);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,1);
        assertEquals(0, obj.checkPatternAndComputePoints(player));
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos()-1,PlayerTable.getStartingCardPos()+1,1,1);
        assertEquals(0, obj.checkPatternAndComputePoints(player));
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos()-2,PlayerTable.getStartingCardPos()+2,1,1);
        assertEquals(2, obj.checkPatternAndComputePoints(player));

        //testing Descending
        DiagonalPatternObjective obj_2 = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(1);
        player.setSecretObjective(obj_2);
        ResourceCard fungi_4 = Deck.getResourceCardsDeck().get(3);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(10);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);
        ResourceCard plant_3 = Deck.getResourceCardsDeck().get(12);
        player.setCardsOnHand(new ResourceCard[]{fungi_4, plant_2, plant_3});
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),2,1);
        assertEquals(2, obj.checkPatternAndComputePoints(player));
        player.setCardOnHandInTheEmptyPosition(plant_1);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos()+1,PlayerTable.getStartingCardPos()-1,2,1);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos()+2,PlayerTable.getStartingCardPos()-2,3,1);
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos()+3,PlayerTable.getStartingCardPos()-1,3,1);
        assertEquals(2, obj_2.checkPatternAndComputePoints(player));
    }

}