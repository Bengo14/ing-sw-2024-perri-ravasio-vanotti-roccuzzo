package it.polimi.sw.gianpaolocugola47.view.cli;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static it.polimi.sw.gianpaolocugola47.model.Items.*;
import static it.polimi.sw.gianpaolocugola47.model.Resources.*;

//import java.io.IOException;


public class CLI implements View {

    private Client client;
    private final String ANSI_RESET = "\033[0m";
    private final CLIController cliController;

    public CLI(Client client) {
        this.client = client;
        this.cliController = new CLIController(new PlayerTable(client.getIdLocal()));
    }
    public CLI() {
        this.cliController = null;
        this.client = null;
    }
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    public void start() {
        for (int i = 0; i < 50; i++) System.out.println();
        System.out.println("""
                                                    ▄██████╗ ▄████▄  ██████▄   ███████╗██╗   ██╗     ███╗   ██╗  ▄██▄╗  ████████╗██╗   ██╗█████▄╗    ▄██▄╗  ██╗     ██╗███████╗
                                                    ██╔════╝██╔═══██╗██╔═══██╗ ██╔════╝╚██╗ ██╔╝     ████╗  ██║▄█▀╝ ▀█▄╗╚══██╔══╝██║   ██║██╔══██╗ ▄█▀╝ ▀█▄╗██║     ██║██╔════╝
                                                    ██║     ██║   ██║██║    ██╗█████╗   ╚████╔╝      ██╔██╗ ██║██▄▄▄▄██║   ██║   ██║   ██║█████▀ ╝ ██▄▄▄▄██║██║     ██║███████╗
                                                    ██║     ██║   ██║██║   ██╔╝██╔══╝   ██╔═██╗      ██║╚██╗██║██▀▀▀▀██║   ██║   ██║   ██║██╔ ▀█▄╗ ██▀▀▀▀██║██║     ██║╚════██║
                                                    ▀██████╗ ▀█████╔╝██████╔═╝ ███████╗██╔╝ ╚██╗     ██║ ╚████║██║   ██║   ██║    ██████╔╝██║  ╚██╗██║   ██║███████╗██║███████║
                                                     ╚═════╝  ╚════╝ ╚═════╝   ╚══════╝╚═╝   ╚═╝     ╚═╝  ╚═══╝╚═╝   ╚═╝   ╚═╝    ╚═════╝ ╚═╝   ╚═╝╚═╝   ╚═╝╚══════╝╚═╝╚══════╝
                """);
        System.out.flush();
        this.cliController.setNickname(client.getNicknameLocal());
        try{
            commandHandler();
        } catch(IOException e){
            System.err.println("An error occurred while reading the input.");
        }
    }

    private void openChat() {
        new Thread(() -> {
            try {
                chatInputLoop();
            } catch (IOException e) {
                client.terminateLocal();
            }
        }).start();
    }

    private void chatInputLoop() throws IOException {

        System.out.println("Chat service is on!\nType --listPlayers to see who your opponents are.\nStart a message with '@' to send a private message.");
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        ChatMessage message = new ChatMessage(cliController.getNickname(), cliController.getId());

        while(true) {
            String line = br.readLine();
            if (line.startsWith("@")) {
                try {
                    message.setPrivate(true);
                    message.setReceiver(line.substring(1, line.indexOf(" ")));
                    message.setMessage(line.substring(line.indexOf(" ") + 1));
                    this.client.sendPrivateMessage(message);
                } catch (StringIndexOutOfBoundsException e) {
                    System.err.println("Invalid input, try again...");
                }
            } else if (line.equals("--listPlayers")) {
                System.out.println("Here's a list of all the players in the lobby: ");
                for(String nickname : cliController.getNicknames()) {
                    if(nickname.equals(this.cliController.getNickname()))
                        System.err.println(nickname + " (you)");
                    else
                        System.out.println(nickname);
                }
            } else {
                message.setPrivate(false);
                message.setMessage(line);
                this.client.sendMessage(message);
            }
        }
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        this.cliController.initView(globalObjectives, cardsOnHand, cardsOnTable, nicknames);
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        this.cliController.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        this.cliController.updatePoints(boardPoints, globalPoints);
    }

    @Override
    public StartingCard getStartingCard() {
        return cliController.getStartingCard();
    }

    @Override
    public Objectives getSecretObjective() {
        return cliController.getSecretObjective();
    }

    private int[] getGlobalPoints() {
        return cliController.getGlobalPoints();
    }

    private int[] getBoardPoints() {
        return cliController.getBoardPoints();
    }

    @Override
    public void showTurn() {
        if(client.isItMyTurn())
            System.out.println("It's your turn!");
        else
            System.out.println("It's not your turn, wait for the other players to finish.");
    }

    @Override
    public void receiveMessage(ChatMessage message) {
        if(!message.isPrivate())
            System.out.println(message.getSender() + ": " + message.getMessage());
        else System.out.println(message.getSender() + ": psst, " + message.getMessage());
    }

    @Override
    public void gameOver() {
        /*todo*/
    }

    @Override
    public void showWinner() {
        /*todo*/
    }

    protected PlaceableCard[][] getPlacedCards(int id) {
        return client.getPlacedCards(id);
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
        if(goldCard.isFront())
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

    public void printPlayerBoardCompactCard(){ //to be fixed!!
        boolean printableRow;
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            printableRow = false;
            PlaceableCard[] row = this.getPlacedCards(client.getIdLocal())[i];
            for (PlaceableCard placeableCard : row)
                if (placeableCard != null) {
                    printableRow = true;
                    break;
                }
            if(printableRow){
                for(PlaceableCard placeableCard : row){
                    if(placeableCard != null){
                        switch (placeableCard) {
                            case ResourceCard resourceCard when !(placeableCard instanceof GoldCard) ->
                                    System.out.print(resourceCard.getResourceCentreBack().getAsciiEscape() + "R" + ANSI_RESET);
                            case GoldCard goldCard ->
                                    System.out.print(goldCard.getResourceCentreBack().getAsciiEscape() + "G" + ANSI_RESET);
                            case StartingCard startingCard -> System.out.print("S");
                            default -> {}
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
    public void printPoints()  {
        int[] boardPoints = this.cliController.getBoardPoints();
        int[] globalPoints = this.cliController.getGlobalPoints();
        String[] nicknames = this.cliController.getNicknames();
        try{
            System.out.println("LEADERBOARD");
            for(int i = 0; i < nicknames.length; i++){
                System.out.println(nicknames[i] + " || Board points: " + boardPoints[i] + " || Global points: " + globalPoints[i]);
                if(i < nicknames.length - 1)
                    System.out.println("--------------------");
                else
                    System.out.println("");
            }
        } catch(NullPointerException e){
            System.out.println("No points to show yet.");
        }
    }

    public void commandHandler() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            setupPhase(br);
        }catch(InterruptedException e){
            System.err.println("Interrupted");
        }
        while(true){
            if(client.isItMyTurn()){
                printPoints();
                printResourceCounter(client.getResourceCounter(client.getIdLocal()));
                String command = br.readLine();
                switch (command) {
                    case "/help" -> System.out.println("""
                            Commands available:
                            /help: show this message
                            /showCardAt [xCoord] [yCoord]: show a card in a given position
                            /showHandCards: show both cards in hand
                            /showOccupiedPositions: show positions where a card is already present
                            /showBoard: show the player board
                            /showAvailablePositions: show available positions to place a card
                            /showObjectives: print personal & shared objective card
                            /placeCard [xCoord] [yCoord] [cardInHand {0-2}] [{front/back}]: place a card on the board
                            """);
                    case "/showHandCards" -> showHandCards();
                    case "/showOccupiedPositions" -> showOccupiedPositions();
                    case "/showBoard" -> printPlayerBoardCompactCard();
                    case "/showObjectives" -> showObjectives();
                    case "/showAvailablePositions" -> showAvailablePositions();
                    default -> {
                        if(command.startsWith("/showCardAt")){
                            showCardAt(command);
                        }
                        else if(command.startsWith("/placeCard")){
                            if(placeCard(command))
                            {
                                System.out.println("Card placed successfully!");
                                System.out.println("Now you can draw your card {0-5}: ");
                                showCardsOnTable();
                                showDeckCards();
                                String choice;
                                do{
                                    choice = br.readLine();
                                    if(choice.equals("0") || choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4") || choice.equals("5"))
                                        break;
                                    else
                                        System.out.println("Invalid card choice, must be between 0 and 5. Try again.");
                                } while(true);
                                drawCard(choice);
                                System.out.println("Your turn is done now!");
                            }
                        }
                        else
                            System.out.println("Command couldn't be recognized, please try again.");
                    }
                }
                /*checkIfGameHasEnded*/
            }
            if(!client.isItMyTurn()){
                String command = br.readLine();
                printPoints();
                switch (command) {
                    case "/help" -> System.out.println("""
                            Commands available:
                            /help: show this message
                            /showCardAt [xCoord] [yCoord]: show a card in a given position
                            /showHandCards: show both cards in hand
                            /showOccupiedPositions: show positions where a card is already present
                            /showBoard: show the player board
                            /showObjectives: print personal & shared objective card
                            /openChat: opens chat. Type '@' to send a private message
                            """);
                    case "/showHandCards" -> showHandCards();
                    case "/showOccupiedPositions" -> showOccupiedPositions();
                    case "/showBoard" -> printPlayerBoardCompactCard();
                    case "/showObjectives" -> showObjectives();
                    case "/openChat" -> System.out.flush(); /*todo*/
                    default -> {
                        if(command.startsWith("/showCardAt"))
                            showCardAt(command);
                        else
                            System.out.println("Command couldn't be recognized, please try again.");
                    }
                }
            }
        }
    }

    private void showObjectives() {
        System.out.println("SECRET PERSONAL OBJECTIVE");
        this.printObjectiveCard(this.cliController.getLocalPlayerTable().getSecretObjective());
        System.out.println("SHARED GLOBAL OBJECTIVES");
        this.printObjectiveCard(this.cliController.getObjectives()[0]);
        this.printObjectiveCard(this.cliController.getObjectives()[1]);
    }

    private void showHandCards() {
        int i = 0;
        for(ResourceCard card: this.cliController.getLocalPlayerTable().getCardsOnHand()){
            System.out.println("Card #" + i );
            if(card instanceof GoldCard)
                this.printGoldCard((GoldCard) card);
            else
                this.printResourceCard(card);
            card.switchFrontBack();
            if(card instanceof GoldCard)
                this.printGoldCard((GoldCard) card);
            else
                this.printResourceCard(card);
            System.out.println("________________________");
            i++;
        }
    }

    public void setupPhase(BufferedReader br) throws IOException, InterruptedException {
        StartingCard selectedStartingCard = client.drawStartingCard();
        String command;
        System.out.println("You drew the following starting card: ");
        this.printStartingCard(selectedStartingCard);
        selectedStartingCard.switchFrontBack();
        this.printStartingCard(selectedStartingCard);
        selectedStartingCard.switchFrontBack();
        System.out.println("Pick the side you want to start with (1-2): ");
        do{
            command = br.readLine();
            if(command.equals("2")){
                selectedStartingCard.switchFrontBack();
                this.cliController.getLocalPlayerTable().setStartingCard(selectedStartingCard);
            }
            else if(command.equals("1"))
                this.cliController.getLocalPlayerTable().setStartingCard(selectedStartingCard);
            else
                System.out.println("Invalid command, try again.");
        }while(!command.equals("1") && !command.equals("2"));
        Objectives[] objectives = client.setStartingCardAndDrawObjectives();
        System.out.println("You drew the following objectives: ");
        for(Objectives objective : objectives)
            this.printObjectiveCard(objective);
        System.out.println("Pick the objective you want to keep (1-2): ");
        do {
            command = br.readLine();
            if (command.equals("2"))
                this.cliController.getLocalPlayerTable().setSecretObjective(objectives[1]);
            else if (command.equals("1"))
                this.cliController.getLocalPlayerTable().setSecretObjective(objectives[0]);
            else
                System.out.println("Invalid command, try again.");
        }while(!command.equals("1") && !command.equals("2"));
        client.setSecretObjective();
        System.out.println("Setup phase completed! Waiting for the other players to pick their cards.");
        Thread.sleep(1000);
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private boolean isAPositiveNumber(String number){
        try{
            Integer.parseInt(number);
            return Integer.parseInt(number) >= 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean placeCard(String command){
        String[] coordinates = command.split(" ");
        if(coordinates.length != 5){
            System.out.println("Invalid command, try again.");
            return false;
        }
        else{
            if(isAPositiveNumber(coordinates[1]) && isAPositiveNumber(coordinates[2]) && isAPositiveNumber(coordinates[3]) && Integer.parseInt(coordinates[1]) < PlayerTable.getMatrixDimension() && Integer.parseInt(coordinates[2]) < PlayerTable.getMatrixDimension()){
                int x = Integer.parseInt(coordinates[1]);
                int y = Integer.parseInt(coordinates[2]);
                int card = Integer.parseInt(coordinates[3]);
                if(card != 0 && card != 1 && card != 2)
                    System.out.println("The parameter you typed in is not a valid hand position, try again.");
                else if(!coordinates[4].equals("front") && !coordinates[4].equals("back"))
                    System.out.println("The parameter you typed in is not a valid card side, try again.");
                else if(!this.cliController.checkIfValidPosition(x,y,getPlayablePositions()))
                    System.out.println("The x/y coordinates you typed in are not available. Use /showAvailablePositions to see where you may place your cards.");
                else{
                    int[] coords = this.cliController.getCardCoords(x,y,this.getPlacedCards(client.getIdLocal()));
                    this.cliController.setCardSide(card, coordinates[4].equals("front"));
                    return playCard(card, coords[0], coords[1], cliController.getCorner(x, y, this.getPlacedCards(client.getIdLocal())), coordinates[4].equals("front"));
                }
            } else {
                System.out.println("The parameters you typed in are not numbers, try again.");
            }
        }
        return false;
    }

    private boolean playCard(int hand, int x, int y, int corner, boolean isFront) {
        return client.playCard(hand, x, y, corner, isFront);
    }
    private boolean[][] getPlayablePositions() {
        return client.getPlayablePositions();
    }

    private void showCardAt(String command){
        String[] coordinates = command.split(" ");
        if(coordinates.length != 3) System.out.println("Invalid command, try again.");
        else{
            if(isAPositiveNumber(coordinates[1]) && isAPositiveNumber(coordinates[2]) && Integer.parseInt(coordinates[1]) < PlayerTable.getMatrixDimension() && Integer.parseInt(coordinates[2]) < PlayerTable.getMatrixDimension()){
                if(this.getPlacedCards(client.getIdLocal())[Integer.parseInt(coordinates[1])][Integer.parseInt(coordinates[2])] != null){
                    PlaceableCard card = this.getPlacedCards(client.getIdLocal())[Integer.parseInt(coordinates[1])][Integer.parseInt(coordinates[2])];
                    if(card instanceof ResourceCard)
                        this.printResourceCard((ResourceCard) card);
                    else if(card instanceof GoldCard)
                        this.printGoldCard((GoldCard) card);
                    else
                        this.printStartingCard((StartingCard) card);
                }
                else
                    System.out.println("There is no card in the position you typed in.");
            } else {
                System.out.println("The parameters you typed in are not numbers, try again.");
            }
        }
    }
    private void showAvailablePositions(){
        System.out.println("You may place your card at one of the following sets of coordinates: ");
        for(Integer[] coordinates : this.cliController.getAvailablePositions(getPlayablePositions())){
            System.out.println("[" + coordinates[0] + " " + coordinates[1]+ "]");
        }
    }

    private void showCardsOnTable(){
        System.out.println("________________________\nBOARD CARDS");
        for(ResourceCard card: this.cliController.getCardsOnTable()){
            if(!card.isFront())
                card.switchFrontBack();
            if(card instanceof GoldCard)
                this.printGoldCard((GoldCard) card);
            else
                this.printResourceCard(card);
        }
    }

    private void showDeckCards(){
        System.out.println("________________________\nRESOURCE DECK CARDS");
        this.printResourceCard(this.cliController.getResourceCardOnTop());
        System.out.println("________________________\nGOLD DECK CARDS");
        this.printGoldCard(this.cliController.getGoldCardOnTop());
    }
    private void drawCard(String choice){
        this.client.drawCard(Integer.parseInt(choice));
        this.cliController.updateDecksAndBoard(Integer.parseInt(choice));
    }

    private void showOccupiedPositions() {
        System.out.println("The following positions are already occupied by a card: ");
        for(Integer[] i : this.cliController.getOccupiedPositions(this.getPlacedCards(client.getIdLocal())))
            System.out.println("[" + i[0] + " " + i[1] + "]");
    }
}