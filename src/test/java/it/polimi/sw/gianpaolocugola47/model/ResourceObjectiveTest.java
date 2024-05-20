package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceObjectiveTest {

    @Test
    public void testConstructorPlant(){
        Deck.initDeck();
        ResourceObjective plant = (ResourceObjective) Deck.getObjectiveCardsDeck().get(4);
        assertEquals(Resources.PLANT,plant.getResource());
        assertNotNull(plant);
    }
    @Test
    public void testConstructorInsect(){
        Deck.initDeck();
        ResourceObjective insect = (ResourceObjective) Deck.getObjectiveCardsDeck().get(5);
        assertEquals(Resources.INSECTS,insect.getResource());
        assertNotNull(insect);
    }

    @Test
    public void testConstructorFungi(){
        Deck.initDeck();
        ResourceObjective fungi = (ResourceObjective) Deck.getObjectiveCardsDeck().get(6);
        assertEquals(Resources.FUNGI,fungi.getResource());
        assertNotNull(fungi);
    }
    @Test
    public void testConstructorAnimal(){
        Deck.initDeck();
        ResourceObjective animal = (ResourceObjective) Deck.getObjectiveCardsDeck().get(7);
        assertEquals(Resources.ANIMAL,animal.getResource());
        assertNotNull(animal);
        ResourceObjective obj = new ResourceObjective("front","back",Resources.ANIMAL);
    }


    @Test
    public void checkPatternAndComputePoints() {
        MainTable main = new MainTable();
        Deck.initDeck();
        main.setNumOfPlayers(2);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(4);
//        ResourceObjective plant = (ResourceObjective) obj;
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_3 = Deck.getResourceCardsDeck().get(12);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1,plant_2,plant_3});
        main.setPlayerTable(1,player);
        main.setPlayerStartingCard(1,start);
        System.out.println(player.getResourceCounter(2)+"Starting card\n");
        main.setPlayerSecretObjective(1,obj);
        main.playCardAndUpdatePoints(0,29,29,0,1);
        //verify if the card is played
        assertNotNull(player.getPlacedCard(28,28));
        System.out.println(player.getResourceCounter(2)+" First card\n");
        main.playCardAndUpdatePoints(1,29,29,1,1);
        assertNotNull(player.getPlacedCard(28,30));
        System.out.println(player.getResourceCounter(2)+" Second card\n");
        main.playCardAndUpdatePoints(2,28,30,3,1);
        System.out.println(player.getResourceCounter(0)+"\n");
        System.out.println(player.getResourceCounter(1)+"\n");
        System.out.println(player.getResourceCounter(2)+" Third card\n");
        System.out.println(player.getResourceCounter(3)+"\n");
        System.out.println(player.getResourceCounter(4)+"\n");
        System.out.println(player.getResourceCounter(5)+"\n");
        System.out.println(player.getResourceCounter(6)+" End element\n");


        System.out.println(obj.checkPatternAndComputePoints(player)+"\n");
        assertEquals(2,obj.checkPatternAndComputePoints(player));

   }
}