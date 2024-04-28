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
        MainTable main = new MainTable();
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(2);
        res_1.switchFrontBack();
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(24);
        res_2.switchFrontBack();
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(19);
        res_3.switchFrontBack();
        Objectives obj = Deck.getObjectiveCardsDeck().get(3);
        ItemObjective item = (ItemObjective) obj;
        PlayerTable player = new PlayerTable(0,"name",new ResourceCard[]{res_1,res_2,res_3});
        main.setNumOfPlayers(2);
        main.setPlayerTable(0,player);
        main.setPlayerStartingCard(0,start);
        main.setPlayerSecretObjective(0,obj);
        main.playCardAndUpdatePoints(0,29,29,0,0);
        main.playCardAndUpdatePoints(1,29,29,1,0);
        main.playCardAndUpdatePoints(2,29,29,2,0);
        System.out.println(obj.checkPatternAndComputePoints(player));
        assertEquals(3,obj.checkPatternAndComputePoints(player));


    }
}