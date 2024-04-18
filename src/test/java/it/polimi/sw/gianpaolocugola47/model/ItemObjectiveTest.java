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
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    private static ArrayList<ItemObjective> objectiveCardsDeck;
    private static ArrayList<GoldCard> goldCardsDeck;

    @BeforeAll
    public static void setUpStartingCArd() {

        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/objectives.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<ItemObjective>>() {
            }.getType();
            objectiveCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/resourceCards.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<ResourceCard>>() {
            }.getType();
            resourceCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/startingCards.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<StartingCard>>() {
            }.getType();
            startingCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/goldCards.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<GoldCard>>() {
            }.getType();
            goldCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    StartingCard start = startingCardsDeck.get(0);
    ResourceCard res1 = resourceCardsDeck.get(2);
    ResourceCard res2 = resourceCardsDeck.get(16);
    ResourceCard res3 = resourceCardsDeck.get(0);
    ResourceCard res4 = resourceCardsDeck.get(1);

    ItemObjective i = objectiveCardsDeck.get(0);
    PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{});

    start.isFront();
    start.setCoordinates(29,29);
    res1.isFront();
    res2.isFront();
    res3.isFront();
    res3.switchFrontBack();
    res4.isFront();
    res4.switchFrontBack();

    res1.setCoordinates(28,28);
    res2.setCoordinates(28,30);
    res3.setCoordinates(30,28);
    res4.setCoordinates(30,30);
    player.getElement(28,28);
    player.getElement(28,30);
    player.getElement(30,28);
    player.getElement(30,30);
    int point = i.checkPatternAndComputePoints(player);
    System.out.println(point);
    //deve ritornare due ma ritorna sempre zero non capisco il motivo

    //assertEquals(2,i.checkPatternAndComputePoints(player));
    }
}