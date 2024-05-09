package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.rmi.RMIClient;

import java.util.Scanner;

//import java.io.IOException;


public class CLI {

    private final RMIClient client;
    private final String ANSI_RESET = "\033[0m";

    public CLI(RMIClient client) {
        this.client = client;
    }
    public CLI(){
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

    public void printObjectiveCard(Objectives objective) {
        System.out.println("Objective card: "+ objective.getDescription());
    }

    public void printPlayerBoard(PlayerTable playerTable){
        /*todo*/
    }

}
