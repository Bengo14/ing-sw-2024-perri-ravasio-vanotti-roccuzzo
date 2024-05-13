package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import static it.polimi.sw.gianpaolocugola47.model.Items.*;
import static it.polimi.sw.gianpaolocugola47.model.Resources.*;

//import java.io.IOException;


public class CLI {

    private final RMIClient client;
    private final String ANSI_RESET = "\033[0m";

    public CLI(RMIClient client) {
        this.client = client;
    }
    public CLI() {
        this.client = null;
    }
    public void start() {
        for (int i = 0; i < 50; i++) System.out.println();
        System.out.flush();
        System.out.println("---- The game CODEX NATURALIS starts! ----\ncoming soon...");
        /*todo*/
    }

    @SuppressWarnings("ALL")
    public void printResourceCard(ResourceCard resourceCard) {
        String colour = resourceCard.getResourceCentreBack().getAsciiEscape();
        Corner[] corners = resourceCard.getCorners();
        System.out.println(colour + "+-------+" + ANSI_RESET);
        if(resourceCard.isFront()){
            System.out.println(colour + "|" + ANSI_RESET + corners[0].getCornerType() + "  " + resourceCard.getThisPoints() + "  " + corners[1].getCornerType() + colour + "|" + ANSI_RESET);
            System.out.println(colour + "|       |" + ANSI_RESET);
            System.out.println(colour + "|" + ANSI_RESET + corners[2].getCornerType() + "     " + corners[3].getCornerType() + colour + "|" + ANSI_RESET);
        }
        else{
            System.out.println(colour + "|" + ANSI_RESET + corners[4].getCornerType() + "     " + corners[5].getCornerType() + colour + "|" + ANSI_RESET);
            System.out.println(colour + "|   " + ANSI_RESET + resourceCard.resourceCentreBackToString() + colour + "   |" + ANSI_RESET);
            System.out.println(colour + "|" + ANSI_RESET + corners[6].getCornerType() + "     " + corners[7].getCornerType() + colour + "|" + ANSI_RESET);
        }
        System.out.println(colour + "+-------+" + ANSI_RESET);
    }

    @SuppressWarnings("ALL")
    public void printGoldCard(GoldCard goldCard) {
        String color = goldCard.getResourceCentreBack().getAsciiEscape();
        Corner[] corners = goldCard.getCorners();
        System.out.println(color + "+-------+" + ANSI_RESET);
        if(goldCard.isFront()){
            if(goldCard.pointConditionToString().equals("N/A"))
                System.out.println(color + "|" + ANSI_RESET + corners[0].getCornerType() + "  " + goldCard.getThisPoints() + "  " + corners[1].getCornerType() + color + "|" + ANSI_RESET);
            else
                System.out.println(color + "|" + ANSI_RESET + corners[0].getCornerType() + " " + goldCard.getThisPoints() + "|" + goldCard.pointConditionToString() + " " + corners[1].getCornerType() + color + "|" + ANSI_RESET);
            System.out.println(color + "|       |" + ANSI_RESET);
            System.out.println(color + "|" + ANSI_RESET + corners[2].getCornerType() + "     " + corners[3].getCornerType() + color + "|" + ANSI_RESET);
        }
        else{
            System.out.println(color + "|" + ANSI_RESET + corners[4].getCornerType() + "     " + corners[5].getCornerType() + color + "|" + ANSI_RESET);
            System.out.println(color + "|   " + ANSI_RESET + goldCard.resourceCentreBackToString() + color + "   |" + ANSI_RESET);
            System.out.println(color + "|" + ANSI_RESET + corners[6].getCornerType() + "     " + corners[7].getCornerType() + color + "|" + ANSI_RESET);
        }
        System.out.println(color + "+-------+" + ANSI_RESET);
        System.out.println("Resources required to play this card: " + goldCard.resourcesRequiredToString());
    }

    @SuppressWarnings("ALL")
    public void printStartingCard(StartingCard startingCard) {
        Corner[] corners = startingCard.getCorners();
        System.out.println("+-------+");
        if(startingCard.isFront()){
            System.out.println("|" + corners[0].getCornerType() + "     " + corners[1].getCornerType() + "|");
            System.out.println("|       |");
            System.out.println("|" + corners[2].getCornerType() + "     " + corners[3].getCornerType() + "|");
        }
        else{
            System.out.println("|" + corners[4].getCornerType() + "     " + corners[5].getCornerType() + "|");
            System.out.println("|" + startingCard.resourcesCentreBackToString() + "|");
            System.out.println("|" + corners[6].getCornerType() + "     " + corners[7].getCornerType() + "|");
        }
        System.out.println("+-------+");
    }

    @SuppressWarnings("ALL")
    public void printObjectiveCard(Objectives objective) {
        System.out.println("Objective card: "+ objective.getDescription());
    }

    @SuppressWarnings("ALL")
    public void printResourceCounter(int[] resourceCounter) {
        System.out.print("[" + ANIMAL.getAsciiEscape() + ANIMAL.getSymbol() + ANSI_RESET + " " +
                FUNGI.getAsciiEscape() + FUNGI.getSymbol() + ANSI_RESET + " " +
                PLANT.getAsciiEscape() + PLANT.getSymbol() + ANSI_RESET + " " +
                INSECTS.getAsciiEscape() + INSECTS.getSymbol() + ANSI_RESET + "]");
        System.out.println("[" + QUILL.getSymbol() + " " + INKWELL.getSymbol() + " " + MANUSCRIPT.getSymbol() + "]");
        System.out.print("[" + resourceCounter[0] + " " + resourceCounter[1] + " " + resourceCounter[2] + " " + resourceCounter[3] + "]");
        System.out.println("[" + resourceCounter[4] + " " + resourceCounter[5] + " " + resourceCounter[6] + "]");
    }

    /*resulting print is likely to be confusing, suggest using the compact method*/
    public void printPlayerBoardExtendedCard(PlayerTable playerTable){
        boolean printableRow = false;
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++){
            PlaceableCard[] row = playerTable.getPlacedCards()[i];
            for (PlaceableCard placeableCard : row)
                if (placeableCard != null) {
                    printableRow = true;
                    break;
                }
            if(printableRow){
                printableRow = false;
                for(PlaceableCard placeableCard : row){
                    if(placeableCard != null){
                        switch (placeableCard) {
                            case ResourceCard resourceCard when !(placeableCard instanceof GoldCard) ->
                                    printResourceCard(resourceCard);
                            case GoldCard goldCard -> printGoldCard(goldCard);
                            case StartingCard startingCard -> printStartingCard(startingCard);
                            default -> {
                            }
                        }
                    }
                    else
                        /*todo*/;
                }
            }
        }
    }

    public void printPlayerBoardCompactCard(PlayerTable playerTable){
        boolean printableRow = false;
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            PlaceableCard[] row = playerTable.getPlacedCards()[i];
            for (PlaceableCard placeableCard : row)
                if (placeableCard != null) {
                    printableRow = true;
                    break;
                }
            if(printableRow){
                printableRow = false;
                for(PlaceableCard placeableCard : row){
                    if(placeableCard != null){
                        switch (placeableCard) {
                            case ResourceCard resourceCard when !(placeableCard instanceof GoldCard) ->
                                    System.out.print(resourceCard.getResourceCentreBack().getAsciiEscape() + "R" + ANSI_RESET);
                            case GoldCard goldCard ->
                                    System.out.print(goldCard.getResourceCentreBack().getAsciiEscape() + "G" + ANSI_RESET);
                            case StartingCard startingCard -> System.out.print("S");
                            default -> {
                            }
                        }
                    }
                    else
                        System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }

    @SuppressWarnings("ALL")
    public void printPoints() throws RemoteException {
        System.out.println("BoardPoints: " + this.client.getBoardPoints()+"|| GlobalPoints: " + this.client.getGlobalPoints());
    }

    public void commandHandler() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            printPoints();
            //printResourceCounter(...);
            //printPlayerBoardCompactCard(...);
            String command = br.readLine();
            if(command.equals("/help")){
                System.out.println("""
                        Commands available:
                        /help: show this message
                        /showCard [xCoord] [yCoord]: show the board
                        /showCardInHand [cardInHand]: show a card in hand
                        /placeCard [xCoord] [yCoord] [cardInHand]: place a card on the board""");
            }
            else if(command.startsWith("/showCard")){
                String[] coordinates = command.split(" ");
                if(coordinates.length != 3) System.out.println("Invalid command, try again.");
                else{
                    try{
                        int x = Integer.parseInt(coordinates[1]);
                        int y = Integer.parseInt(coordinates[2]);
                    } catch(NumberFormatException e){
                        System.out.println("The parameters you typed in are not numbers, try again.");
                    }
                    //getCard method
                }
            }
            else if(command.equals("/showCardInHand")){
                String[] cardInHand = command.split(" ");
                if(cardInHand.length != 2) System.out.println("Invalid command, try again.");
                else{
                    try{
                        int card = Integer.parseInt(cardInHand[1]);
                        if(card != 0 && card != 1)
                            System.out.println("The parameter you typed in is not a valid hand position, try again.");
                    } catch(NumberFormatException e){
                        System.out.println("The parameter you typed in is not a number, try again.");
                    }
                    //getCardInHand method
                }
            }
            else if(command.startsWith("/placeCard")){
                String[] coordinates = command.split(" ");
                if(coordinates.length != 4) System.out.println("Invalid command, try again.");
                else{
                    try{
                        int x = Integer.parseInt(coordinates[1]);
                        int y = Integer.parseInt(coordinates[2]);
                        int card = Integer.parseInt(coordinates[3]);
                        if(card != 0 && card != 1)
                            System.out.println("The parameter you typed in is not a valid hand position, try again.");
                    } catch(NumberFormatException e){
                        System.out.println("The parameters you typed in are not numbers, try again.");
                    }
                    //placeCard method
                }
            }
            else if(command.equals("/exit")) break;
            else System.out.println("Command not recognized");
        }
    }
}
