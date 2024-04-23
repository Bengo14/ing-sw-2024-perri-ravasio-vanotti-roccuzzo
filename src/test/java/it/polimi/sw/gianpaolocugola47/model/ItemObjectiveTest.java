package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
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
//        MainTable main = new MainTable();
//        main.setNumOfPlayers(2);
//        StartingCard start = Deck.getStartingCardsDeck().get(0);
//        ResourceCard res1 = Deck.getResourceCardsDeck().get(2);
//        ResourceCard res2 = Deck.getResourceCardsDeck().get(16);
//        ResourceCard res3 = Deck.getResourceCardsDeck().get(0);
//        ResourceCard res4 = Deck.getResourceCardsDeck().get(1);
//        ItemObjective i = (ItemObjective) Deck.getObjectiveCardsDeck().get(0);
//        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{});
//        main.setPlayerTable(1,player);
//        main.setPlayerStartingCard(1,start);
//        player.setStartingCard(start);
//        main.setPlayerSecretObjective(1,i);
//        main.playCardAndUpdatePoints(0,29,29,1,1);
//        main.playCardAndUpdatePoints(1,29,29,3,1);
//        player.setCardOnHandInTheEmptyPosition(res4);
//        main.playCardAndUpdatePoints(2,29,29,0,1);








    }
}