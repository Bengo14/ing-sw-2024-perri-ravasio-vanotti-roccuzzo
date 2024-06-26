package it.polimi.sw.gianpaolocugola47.view.cli;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static it.polimi.sw.gianpaolocugola47.model.Items.*;
import static it.polimi.sw.gianpaolocugola47.model.Resources.*;

// import java.io.IOException;

/**
 * This class handles the view part of the game, more specifically it handles the Command Line Interface (CLI).
 * It contains methods needed to render elements of the game (such as the board or cards) and to communicate with the model and the controller.
 * It implements the interface View, which itself defines crucial methods needed for the correct functioning of the CLI.
 */

public class CLI implements View {

    private Client client;
    private final String ANSI_RESET = "\033[0m";
    private final CLIController cliController;
    private boolean isChatOpen;
    private boolean isLoaded;
    private final ArrayList<ChatMessage> chatBuffer = new ArrayList<>();
    private boolean isGameFinished;

    /**
     * Constructor method for this class. cliController handles data-related game saved locally, more specifically:
     * -board cards that can be picked up
     * -cards on top of the deck
     * -a local copy of the player table, bar the placedCard matrix which is retrieved from the server
     * -global and board points counter
     * -the nicknames of the players currently present in the match
     * @param isLoaded : true if the game is loaded from a previous save, false otherwise.
     */
    public CLI(Client client, boolean isLoaded) {
        this.isLoaded = isLoaded;
        this.client = client;
        this.cliController = new CLIController(new PlayerTable(client.getIdLocal()));
        this.isGameFinished = false;
    }

    public CLI() {
        this.cliController = null;
        this.client = null;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * This method is called when the game starts. It clears the console and prints the game logo.
     * It then calls the commandHandler method, which is responsible for handling the input from the user;
     * this command is crucial to handle the correct flow of the game.
     */
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

    /**
     * This method is called when the user wants to use the chat, be it for sending or receiving messages.
     * Once called, the last 50> unread messages (saved in the 'chatBuffer' array list) are displayed.
     * @param br: BufferedReader; needed to take user's input.
     * @param startingState: boolean attribute; it checks whether the user has entered the chat while it was its turn or not;
     *                       used to close the chat if the turn is switched while chat is open.
     */
    private void chatInputLoop(BufferedReader br, boolean startingState) throws IOException {
        System.out.println("Chat service is on!\nType /listPlayers to see who your opponents are.\nStart a message with '@' to send a private message.\nType /closeChat to close the chat.");
        ChatMessage message = new ChatMessage(cliController.getNickname(), cliController.getId());
        System.out.println("\nLast 50 unread messages: ");
        for(ChatMessage chatMessage : chatBuffer){
            if(!chatMessage.isPrivate())
                System.out.println(chatMessage.getSender() + ": " + chatMessage.getMessage());
            else System.out.println(chatMessage.getSender() + ": psst, " + chatMessage.getMessage());
        }
        while(isChatOpen) {
            if(client.isItMyTurn() != startingState){
                this.isChatOpen = false;
                this.chatBuffer.clear();
                System.out.println("Closing chat, it's your turn.");
            }
            else{
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
                } else if (line.equals("/listPlayers")) {
                    System.out.println("Here's a list of all the players in the lobby: ");
                    for(String nickname : cliController.getNicknames()) {
                        if(nickname.equals(this.cliController.getNickname()))
                            System.err.println(nickname + " (you)");
                        else
                            System.out.println(nickname);
                    }
                } else if(line.equals("/closeChat")) {
                    System.out.println("Chat is closing down!");
                    this.isChatOpen = false;
                    this.chatBuffer.clear();
                } else {
                    message.setPrivate(false);
                    message.setMessage(line);
                    this.client.sendMessage(message);
                }
            }
        }
    }

    /**
     * initializes view.
     * @param nicknames: array of nicknames of the players in the match.
     * @param globalObjectives: array of global objectives of the match.
     * @param cardsOnHand: array of initial cards in the hand of the player.
     * @param cardsOnTable: array of initial drawable cards present on the table.
     */
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        this.cliController.initView(globalObjectives, cardsOnHand, cardsOnTable, nicknames);
    }

    /**
     * Updates decks.
     * @param resourceCardOnTop: resource card on top of the resource deck.
     * @param goldCardOnTop: gold card on top of the gold deck.
     * @param drawPos: board position of the card that was drawn from the table.
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        this.cliController.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
    }

    /**
     * Updates point counters.
     * @param boardPoints: array of updated board points.
     * @param globalPoints: array of updated global points.
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        this.cliController.updatePoints(boardPoints, globalPoints);
    }

    /**
     * Gets starting card from CLIController.
     * @return : starting card of the player.
     */
    @Override
    public StartingCard getStartingCard() {
        return cliController.getStartingCard();
    }

    /**
     * Gets the secret objective of the player.
     * @return : secret objective of the player.
     */
    @Override
    public Objectives getSecretObjective() {
        return cliController.getSecretObjective();
    }

    /**
     * Prints whether it's player's turn or not.
     */
    @Override
    public void showTurn() {
        if(client.isItMyTurn())
            System.out.println("It's your turn! Type /help to see all the available commands.");
        else
            System.out.println("Your turn is done now! Type /help to see all the available commands");
    }

    /**
     * Receives a message sent from the chat. If the chat is open, the message is displayed on the console; otherwise,
     * it is saved in a buffer and eventually shown when the chat is open.
     * @param message: object ChatMessage; it contains the body of the message as well as sender and receiver information
     *               and whether the message is private or not.
     */
    @Override
    public void receiveMessage(ChatMessage message) {
        if(isChatOpen){
            if(!message.getSender().equals(this.cliController.getNickname())){
                if(!message.isPrivate())
                    System.out.println(message.getSender() + ": " + message.getMessage());
                else System.out.println(message.getSender() + ": psst, " + message.getMessage());
            }
        } else {
            chatBuffer.add(message);
            if(chatBuffer.size() > 50){
                chatBuffer.removeFirst();
            }
        }
    }

    /**
     * Shows the winner of the game.
     * @param id: winner's id.
     */
    @Override
    public void showWinner(int id) {
        if(id == this.client.getIdLocal())
            System.out.println("Congratulations! You won the game!");
        else
            System.out.println("Player " + this.cliController.getNicknames()[id] + " won the game!");
        System.out.println("Looking forward to play with you again :-)");
        isGameFinished = true;
    }

    /**
     * Gets the placedCard matrix of a given player from the server.
     * @param id: int; id of the player whose board I want to retrieve.
     * @return : the placed card matrix of given player.
     */
    protected PlaceableCard[][] getPlacedCards(int id) {
        return client.getPlacedCards(id);
    }

    /**
     * Prints the given resource card.
     */
    @SuppressWarnings("ALL")
    public void printResourceCard(ResourceCard resourceCard) {
        String colour = resourceCard.getResourceCentreBack().getAsciiEscape();
        Corner[] corners = resourceCard.getCorners();
        System.out.println(colour + "+-------+" + ANSI_RESET);
        if(resourceCard.getIsFront()){
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

    /**
     * Prints the given gold card.
     */
    @SuppressWarnings("ALL")
    public void printGoldCard(GoldCard goldCard) {
        String color = goldCard.getResourceCentreBack().getAsciiEscape();
        Corner[] corners = goldCard.getCorners();
        System.out.println(color + "+-------+" + ANSI_RESET);
        if(goldCard.getIsFront()){
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
        if(goldCard.getIsFront())
            System.out.println("Resources required to play this card: " + goldCard.resourcesRequiredToString());
    }

    /**
     * Prints the given starting card.
     */
    @SuppressWarnings("ALL")
    public void printStartingCard(StartingCard startingCard) {
        Corner[] corners = startingCard.getCorners();
        System.out.println("+-------+");
        if(startingCard.getIsFront()){
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

    /**
     * Prints the given objective card.
     */
    @SuppressWarnings("ALL")
    public void printObjectiveCard(Objectives objective) {
        System.out.println("Objective card: "+ objective.getDescription());
    }

    /**
     * Prints the resource counter.
     */
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

    /**
     * Prints the board of a given player. Empty rows are trimmed for the sake of readability.
     * @param id : id of the player whose board needs to be printed.
     */
    public void printPlayerBoardCompactCard(int id){
        boolean printableRow;
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            printableRow = false;
            PlaceableCard[] row = this.getPlacedCards(id)[i];
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
                            case StartingCard _ -> System.out.print("S");
                            default -> {}
                        }
                    }
                    else
                        System.out.print(" ");
                }
                System.out.print("\n");
            }
        }
    }

    @SuppressWarnings("ALL")
    /**
     * Prints the leaderboard, comprehensive of both board and global points.
     */
    public void printPoints()  {
        int[] boardPoints = this.cliController.getBoardPoints();
        int[] globalPoints = this.cliController.getGlobalPoints();
        String[] nicknames = this.cliController.getNicknames();
        System.out.println("LEADERBOARD");
        try{
            for(int i = 0; i < nicknames.length; i++){
                System.out.println(nicknames[i] + " || Board points: " + boardPoints[i] + " || Global points: " + globalPoints[i]);
                if(i < nicknames.length - 1)
                    System.out.println("--------------------");
                else
                    System.out.println("");
            }
        } catch(NullPointerException e){
            for(int i = 0; i < nicknames.length; i++){
                System.out.println(nicknames[i] + " || Board points: " + 0 + " || Global points: " + 0);
                if(i < nicknames.length - 1)
                    System.out.println("--------------------");
                else
                    System.out.println("");
            }
        }
    }

    /**
     * This method handles user's input &amp; commands. The bulk of the game's logic is contained in this method. More methods needed to display
     * important information (such as the leaderboard, the player's board, the cards in hand, the objectives, etc.) are called here
     * when the user send correct commands, which can be displayed by typing "/help".
     */
    public void commandHandler() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if(!isLoaded){
            try{
                setupPhase(br);
            }catch(InterruptedException e){
                System.err.println("Interrupted");
            }
        } else {
            System.out.println("Game loaded successfully!");
            System.out.println("Type /help to see all the available commands.");
            client.startGameFromFile();
            this.cliController.getLocalPlayerTable().setStartingCard((StartingCard) client.getPlacedCards(client.getIdLocal())[PlayerTable.STARTING_CARD_POS][PlayerTable.STARTING_CARD_POS]);
            this.cliController.getLocalPlayerTable().setSecretObjective(client.getSecretObjective());
        }
        while(!isGameFinished){
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
                            /showOwnBoard: show the player board
                            /showPlayerBoard [nickname]: show the board of a specific player
                            /showAvailablePositions: show available positions to place a card
                            /showObjectives: print personal & shared objective card
                            /placeCard [xCoord] [yCoord] [cardInHand {0-2}] [{front/back}]: place a card on the board
                            /openChat: opens chat
                            """);
                    case "/showHandCards" -> showHandCards();
                    case "/showOccupiedPositions" -> showOccupiedPositions();
                    case "/showOwnBoard" -> printPlayerBoardCompactCard(this.client.getIdLocal());
                    case "/showObjectives" -> showObjectives();
                    case "/showAvailablePositions" -> showAvailablePositions();
                    case "/openChat" -> chatHandler(br, client.isItMyTurn());
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
                            }
                        } else if (command.startsWith("/showPlayerBoard")) {
                            showPlayerBoard(command);
                        }
                        else
                            System.out.println("Command couldn't be recognized, please try again.");
                    }
                }
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
                            /showOwnBoard: show player's personal board
                            /showPlayerBoard [nickname]: show the board of a specific player
                            /showObjectives: print personal & shared objective card
                            /openChat: opens chat
                            """);
                    case "/showHandCards" -> showHandCards();
                    case "/showOccupiedPositions" -> showOccupiedPositions();
                    case "/showOwnBoard" -> printPlayerBoardCompactCard(this.client.getIdLocal());
                    case "/showObjectives" -> showObjectives();
                    case "/openChat" -> chatHandler(br, client.isItMyTurn());
                    default -> {
                        if(command.startsWith("/showCardAt"))
                            showCardAt(command);
                        else if(command.startsWith("/showPlayerBoard"))
                            showPlayerBoard(command);
                        else
                            System.out.println("Command couldn't be recognized, please try again.");
                    }
                }
            }
        }
    }

    /**
     * Calls the 'chatInputLoop' method, which is responsible for handling the chat and modifies the isChatOpen parameter
     * to reflect whether the chat is open or not.
     * @param br: BufferedReader; needed to take user's input.
     * @param startingState: boolean attribute; it checks whether the user has entered the chat while it was its turn or not;
     *                       used to close the chat if the turn is switched while chat is open.
     */
    private void chatHandler(BufferedReader br, boolean startingState) {
        this.isChatOpen = true;
        try{
            chatInputLoop(br, startingState);
            this.isChatOpen = false;
        } catch(IOException e){
            System.err.println("An error occurred while reading the input.");
        }
    }

    /**
     * Checks the validity of "/showPlayerBoard" command and prints the board of the player whose nickname is passed as a parameter.
     * This method checks whether command's arguments are correct in length (=2) and content (player's nickname).
     * @param command input command given by the user. Parameters are (in order): the command itself and the nickname of the player whose board needs to be printed.
     */
    private void showPlayerBoard(String command) {
        String[] nickname = command.split(" ");
        if(nickname.length != 2)
            System.out.println("Invalid command, try again.");
        else{
            for(int k = 0; k < this.cliController.getNicknames().length; k++){
                if(this.cliController.getNicknames()[k].equals(nickname[1])){
                    System.out.println("Player " + this.cliController.getNicknames()[k] +  "'s board: ");
                    printPlayerBoardCompactCard(k);
                    return;
                }
            }
            System.out.println("The player you typed in is not in the game.");
        }
    }

    /**
     * Prints the objectives (both global and personal) of the player.
     */
    private void showObjectives() {
        System.out.println("SECRET PERSONAL OBJECTIVE");
        this.printObjectiveCard(this.cliController.getLocalPlayerTable().getSecretObjective());
        System.out.println("SHARED GLOBAL OBJECTIVES");
        this.printObjectiveCard(this.cliController.getObjectives()[0]);
        this.printObjectiveCard(this.cliController.getObjectives()[1]);
    }

    /**
     * Prints the cards in hand of the player, both front and back.
     */
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

    /**
     * Handles the setup phase of the match: the player draws a starting card and two objectives, then chooses which ones to keep.
     * @param br: BufferedReader; needed to take user's input.
     */
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
        System.out.println("Setup phase completed! Waiting for the other players to pick their cards and play their first hand.");
        Thread.sleep(2000);
        for (int i = 0; i < 50; i++) System.out.println();
    }

    /**
     * Converts a String to a number and checks if it is positive number.
     * @param number: String; the number to be converted.
     * @return : boolean; true if the number is positive, false otherwise or if the String analyzed can't be parsed to int.
     */
    private boolean isAPositiveNumber(String number){
        try{
            Integer.parseInt(number);
            return Integer.parseInt(number) >= 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * Checks the validity of the "/placeCard" command in terms of numbers of arguments (= 5) and their correctness.
     * The latter is defined by the following rules:
     * -coordinates must be available;
     * -the hand position must be valid;
     * -the card is either "front" or "back" aligned.
     * If the command is valid and the card can be put in the designated position, the card is placed on the board.
     * @param command String; input command given by the user. Arguments are (in order): the command itself, the x and y coordinates, the card to be placed and the side of the card.
     * @return : boolean; true if the card is placed successfully, false otherwise.
     */
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
                    int corner = cliController.getCorner(x, y, this.getPlacedCards(client.getIdLocal()));
                    if(corner == -1){
                        System.out.println("The position you typed in is not valid, try again.");
                        return false;
                    }
                    else{
                        this.cliController.setCardSide(card, coordinates[4].equals("front"));
                        return playCard(card, coords[0], coords[1], corner , coordinates[4].equals("front"));
                    }
                }
            } else {
                System.out.println("The parameters you typed in are not numbers, try again.");
            }
        }
        return false;
    }

    /**
     * Checks whether the card is placeable in the coordinates specified.
     * @param hand: int; the card to be placed.
     * @param x: int; x coordinate, tied to the corresponding row of the board card over which my card is placed.
     * @param y: int; y coordinate, tied to the corresponding column of the board card over which my card is placed.
     * @param corner: int; the corner of the board card over which my card is placed.
     * @param isFront: boolean; true if the card is placed front, false otherwise.
     * @return : boolean; true if the card is placed successfully, false otherwise.
     */
    private boolean playCard(int hand, int x, int y, int corner, boolean isFront) {
        return client.playCard(hand, x, y, corner, isFront);
    }

    /**
     * Returns the playable positions of the board
     * @return : boolean[][]; the matrix of the board, where true means the position is available, where false means it is not.
     */
    private boolean[][] getPlayablePositions() {
        return client.getPlayablePositions();
    }

    /**
     * Checks the validity of the "/showCardAt" command in terms of number of arguments (=3) and their correctness.
     * The latter is defined by the following rule: the coordinates must be of a position on the board already occupied by a card.
     * @param command String; input command given by the user. Arguments 1 and 2 are the x and y coordinates of the card to be shown, while 0 is the command itself.
     */
    private void showCardAt(String command){
        String[] coordinates = command.split(" ");
        if(coordinates.length != 3) System.out.println("Invalid command, try again.");
        else{
            if(isAPositiveNumber(coordinates[1]) && isAPositiveNumber(coordinates[2]) && Integer.parseInt(coordinates[1]) < PlayerTable.getMatrixDimension() && Integer.parseInt(coordinates[2]) < PlayerTable.getMatrixDimension()){
                if(this.getPlacedCards(client.getIdLocal())[Integer.parseInt(coordinates[1])][Integer.parseInt(coordinates[2])] != null){
                    PlaceableCard card = this.getPlacedCards(client.getIdLocal())[Integer.parseInt(coordinates[1])][Integer.parseInt(coordinates[2])];
                    if(card instanceof GoldCard)
                        this.printGoldCard((GoldCard) card);
                    else if(card instanceof ResourceCard)
                        this.printResourceCard((ResourceCard) card);
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

    /**
     * Prints the coordinates of the board where a card may be legally placed.
     */
    private void showAvailablePositions(){
        System.out.println("You may place your card at one of the following sets of coordinates: ");
        for(Integer[] coordinates : this.cliController.getAvailablePositions(getPlayablePositions())){
            System.out.println("[" + coordinates[0] + " " + coordinates[1]+ "]");
        }
    }

    /**
     * Prints the cards on the table that can be drawn by the player.
     */
    private void showCardsOnTable(){
        System.out.println("________________________\nBOARD CARDS");
        for(ResourceCard card: this.cliController.getCardsOnTable()){
            if(!card.getIsFront())
                card.switchFrontBack();
            if(card instanceof GoldCard)
                this.printGoldCard((GoldCard) card);
            else
                this.printResourceCard(card);
        }
    }

    /**
     * Prints the cards on top of the decks.
     */
    private void showDeckCards(){
        System.out.println("________________________\nRESOURCE DECK CARDS");
        this.printResourceCard(this.cliController.getResourceCardOnTop());
        System.out.println("________________________\nGOLD DECK CARDS");
        this.printGoldCard(this.cliController.getGoldCardOnTop());
    }

    /**
     * Draws a card from the deck or the board and updates them.
     * @param choice: String; hand position of the card to be drawn.
     */
    private void drawCard(String choice){
        this.client.drawCard(Integer.parseInt(choice));
        this.cliController.updateDecksAndBoard(Integer.parseInt(choice));
    }

    /**
     * Prints the coordinates of the board where a card is already present.
     */

    private void showOccupiedPositions() {
        System.out.println("The following positions are already occupied by a card: ");
        for(Integer[] i : this.cliController.getOccupiedPositions(this.getPlacedCards(client.getIdLocal())))
            System.out.println("[" + i[0] + " " + i[1] + "]");
    }
}