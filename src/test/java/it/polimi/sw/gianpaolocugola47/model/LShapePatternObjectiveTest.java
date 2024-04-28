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

class LShapePatternObjectiveTest {


    @Test
    public void testConstructor() {
        LShapePatternObjective obj = new LShapePatternObjective(3, "imgPathFront", "imgPathBack", "topLeft", Resources.PLANT, Resources.INSECTS);
        assertEquals(3, obj.getPoints());
        assertEquals("imgPathFront", obj.getImgPathFront());
        assertEquals("imgPathBack", obj.getImgPathBack());

    }
    @Test
    public void testCheckPatternAndComputePoints(){
        MainTable main = new MainTable();

        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        ResourceCard insect = Deck.getResourceCardsDeck().get(2);
        LShapePatternObjective obj = (LShapePatternObjective) Deck.getObjectiveCardsDeck().get(15);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{plant_1, plant_2, insect});
        main.setPlayerTable(1,player);
        main.setPlayerSecretObjective(1,obj);
        start.switchFrontBack();
        main.setPlayerStartingCard(1,start);
        main.playCardAndUpdatePoints(0,29,29,0,1);
        main.playCardAndUpdatePoints(1,29,29,2,1);
        main.playCardAndUpdatePoints(2,30,28,2,1);
        System.out.println(obj.checkPatternAndComputePoints(player));
        assertEquals(3, obj.checkPatternAndComputePoints(player));
    }
    @Test
    public void testIsSecondaryResourceMatchedAndNotFlagged(){


    }



 }

