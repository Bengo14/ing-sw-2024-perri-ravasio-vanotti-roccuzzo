package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ItemObjectiveTest {

    @Test
    public void testConstructorAndGetterItemObjective() {
        ArrayList<Items> items = new ArrayList<>();
        items.add(Items.QUILL);
        ItemObjective i = new ItemObjective(5, "imgPathFront", "imgPathBack", items);
        assertNotNull(i);
        assertEquals(5, i.getPoints());
        assertEquals("imgPathFront", i.getImgPathFront());
        assertEquals("imgPathBack", i.getImgPathBack());
        assertEquals(items, i.getItemsRequired());
    }

    @Test
    public void testCheckPatternAndComputePoints(){
        MainTable main = new MainTable();
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(3);
        Objectives obj_1 = Deck.getObjectiveCardsDeck().get(12);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(4);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(15);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(35);
        res_1.switchFrontBack();
        res_2.switchFrontBack();
        res_3.switchFrontBack();
        ItemObjective item = (ItemObjective) obj_1;
        PlayerTable player = new PlayerTable(0,"name",new ResourceCard[]{res_1,res_2,res_3});
        main.setNumOfPlayers(2);
        main.setPlayerTable(0,player);
        main.setPlayerStartingCard(0,start);
        main.setPlayerSecretObjective(0,obj_1);
        main.playCardAndUpdatePoints(0,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),0,0);
        main.playCardAndUpdatePoints(1,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),1,0);
        main.playCardAndUpdatePoints(2,PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos(),2,0);
        assertEquals(3,obj_1.checkPatternAndComputePoints(player));
    }

}