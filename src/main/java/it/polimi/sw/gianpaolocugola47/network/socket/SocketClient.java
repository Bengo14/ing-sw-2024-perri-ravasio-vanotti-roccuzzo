package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.cli.CLI;
import it.polimi.sw.gianpaolocugola47.view.gui.ViewGui;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class represents the client side of the socket connection.
 */
public class SocketClient implements VirtualView, Client {

    private static final int PORT = 8080;
    private final SocketServerProxy server;
    private final Socket socket;
    private int id;
    private View view;
    private boolean isCliChosen = false;
    protected int numOfPlayers = 0;
    private String nickname = "";
    private boolean isMyTurn = false;
    private volatile boolean response = false;

    /* --- attributes for socket responses --- */
    protected boolean nickAvailableResponse;
    protected String[] nicknamesResponse;
    protected StartingCard drawStartingCardResponse;
    protected Objectives[] setStartingResponse;
    protected boolean playResponse;
    protected ResourceCard[][] getCardsOnHandResponse;
    protected PlaceableCard[][] getPlacedCardsResponse;
    protected int[] getResourceCounterResponse;
    protected boolean[][] getPlayPosResponse;
    protected Objectives getSecretObjectiveResponse;

    /**
     * Constructor of the class.
     * @param socket the socket to connect to the server
     * @throws IOException if an I/O error occurs when creating the output stream
     */
    protected SocketClient(Socket socket) throws IOException {
        this.server = new SocketServerProxy(socket.getOutputStream());
        this.socket = socket;
    }
    /**
     * This method starts the client side of the connection.
     * It creates a new thread to run the virtual server.
     * The virtual server reads the messages sent by the server and executes the corresponding actions.
     */
    public void run() {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (IOException | ClassNotFoundException e) {
                terminate();
            }
        }).start();
    }

    /**
     * This method runs the virtual server.
     * It reads the messages sent by the server and executes the corresponding actions.
     * The method is blocking, so it will wait for the server to send a message.
     * @throws IOException if an I/O error occurs when creating the input stream
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    private void runVirtualServer() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        while (true) {
            SocketMessage message = (SocketMessage) ois.readObject();
            message.doAction(this);
        }
    }

    /**
     * This method sets the response attribute to true.
     */
    protected void setResponse() {
        this.response = true;
    }

    /**
     * This method sets the id attribute to the given id and runs the CLI.
     * @param id the id to set
     */
    protected void setIdAndRunCli(int id) {

        if(id == -1) {
            System.err.println("Connection refused: match already started/full or number of players not set...");
            System.exit(1);
        }

        this.id = id;
        System.err.println("Client connected with id: " + id);
        getNumOfPlayers();

        new Thread(()->{
            try {
                runCli();
            } catch (IOException e) {
                terminate();
            }
        }).start();
    }
    /**
     * This method runs the CLI.
     * It asks the user to choose the interface and the number of players.
     * It also asks the user to insert the nickname.
     * @throws IOException if an I/O error occurs when reading the input
     */

    private void runCli() throws IOException {

        String tempNick;
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        System.out.println("Welcome to Codex Naturalis!");
        System.out.println("Just a little patience, choose the interface: \n1 = CLI\n2 = GUI");
        Scanner scan = new Scanner(System.in);
        String command = scan.next();

        while (!command.equals("1") && !command.equals("2")) {
            System.out.print("Invalid input, try again: ");
            command = scan.next();
        }
        if(command.equals("1"))
            this.isCliChosen = true;

        if(this.id == 0) {
            System.out.print("You are the first to connect, so choose the number of players (2-4): ");
            command = scan.next();
            while (!command.equals("2") && !command.equals("3") && !command.equals("4")) {
                System.out.print("Invalid input, try again: ");
                command = scan.next();
            }
            this.numOfPlayers = Integer.parseInt(command);
            synchronized (server) {
                this.server.setNumOfPlayers(numOfPlayers);
            }
        }
        else {
            System.out.println("A game is already starting, you connected to the server with id: " + this.id);

        }
        System.out.print("Insert your nickname: ");
        tempNick = br.readLine();
        if(tempNick.contains(" ")) {
            tempNick = tempNick.replace(" ","_");
        }
        while(!isNicknameAvailable(tempNick)
                || tempNick.isEmpty()) {
            System.out.print("Nickname invalid or already taken, try again: ");
            System.out.println(tempNick);
            tempNick = br.readLine();
            if(tempNick.contains(" ")) {
                tempNick = tempNick.replace(" ","_");
            }
        }
        System.out.println("You joined the game! Waiting for other players...");
        this.nickname = tempNick;
        synchronized (server) {
            this.server.addPlayer(this.id, this.nickname);
        }
    }

    /* --- methods of interface VirtualView --- */

    /**
     * This method terminates the game.
     * It prints a message and exits the program.
     * It is called when the server sends a message to terminate the game.
     */
    @Override
    public void terminate() {
        System.err.println("\nTerminating the game...");
        System.exit(1);
    }

    /**
     * This method pings the server.
     * It is called when the server sends a message to ping.
     * It sends an ack to the server.
     */
    @Override
    public void ping() {
        synchronized (server) {
            this.server.pingAck();
        }
    }

    /**
     * This method starts the game.
     * It initializes the deck and starts the CLI or the GUI.
     * It is called when the server sends a message to start the game.
     * @param isLoaded true if the game is loaded, false otherwise
     */
    @Override
    public void startGame(boolean isLoaded) {
        Deck.initDeck(); //init Deck's hashMap to retrieve cards by id

        if(isCliChosen) {
            try{
                this.view = new CLI(this,isLoaded);
                new Thread(() -> view.start()).start();
            }catch (Exception e){
                System.err.println("Error in starting CLI. Shutting down.");
                System.exit(1);
            }
        }
        else {
            try{
                this.view = new ViewGui(isLoaded);
                this.view.setClient(this);
                new Thread(() -> Platform.startup(() -> view.start())).start();
            }catch(Exception e){
                System.err.println("Error in starting GUI. Shutting down.");
                System.exit(1);
            }
        }
    }

    /**
     * This method sets the turn attribute to true and shows the turn.
     * It is called when the server sends a message to set the turn.
     * It also shows the turn on the CLI or the GUI.
     */
    @Override
    public void setMyTurn() {
        this.isMyTurn = true;
        view.showTurn();
    }

    /**
     * This method shows the winner.
     * It is called when the server sends a message to show the winner.
     * It shows the winner on the CLI or the GUI.
     * @param id the id of the winner
     */
    @Override
    public void showWinner(int id) {
        this.view.showWinner(id);
    }

    /**
     * This method returns the id of the client.
     * It is called when the server sends a message to get the id.
     * @return the id of the client
     */
    @Override
    public int getId() { // not used here
        return getIdLocal();
    }

    /**
     * This method receives a message and shows it.
     * It is called when the server sends a message to receive a message.
     * It shows the message on the CLI or the GUI.
     * @param message the message to receive
     */
    @Override
    public void receiveMessage(ChatMessage message) {
        synchronized (view) {
            view.receiveMessage(message);
        }
    }

    /**
     * This method receives a private message and shows it.
     * It is called when the server sends a message to receive a private message.
     * It shows the private message on the CLI or the GUI.
     * @param message the private message to receive
     */
    @Override
    public void receivePrivateMessage(ChatMessage message) {
        message.setPrivate(true);
        synchronized (view) {
            view.receiveMessage(message);
        }
    }

    /**
     * This method initializes the view.
     * It is called when the server sends a message to initialize the view.
     * It initializes the view with the given parameters.
     * @param nicknames the nicknames of the players
     * @param globalObjectives the global objectives
     * @param cardsOnHand the cards on hand
     * @param cardsOnTable the cards on table
     */
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        this.view.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
    }
    /**
     * This method updates the decks.
     * It is called when the server sends a message to update the decks.
     * It updates the decks with the given parameters.
     * @param resourceCardOnTop the resource card on top
     * @param goldCardOnTop the gold card on top
     * @param drawPos the draw position
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        this.view.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
    }
    /**
     * This method updates the points.
     * It is called when the server sends a message to update the points.
     * It updates the points with the given parameters.
     * @param boardPoints the board points
     * @param globalPoints the global points
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        this.view.updatePoints(boardPoints, globalPoints);
    }

    /* --- methods of interface Client --- */

    /**
     * This method draws a starting card.
     * It is called when the server sends a message to draw a starting card.
     * It draws a starting card and returns it.
     * @return the starting card drawn
     */
    @Override
    public StartingCard drawStartingCard() {
        synchronized (server) {
            server.drawStartingCard();
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return drawStartingCardResponse;
    }

    /**
     * This method sets the starting card and draws the objectives.
     * It is called when the server sends a message to set the starting card and draw the objectives.
     * It sets the starting card and draws the objectives.
     * @return the objectives drawn
     */
    @Override
    public Objectives[] setStartingCardAndDrawObjectives() {
        synchronized (server) {
            server.setStartingCardAndDrawObjectives(this.id, this.view.getStartingCard());
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return setStartingResponse;
    }
    /**
     * This method sets the secret objective.
     * It is called when the server sends a message to set the secret objective.
     */
    @Override
    public void setSecretObjective() {
        synchronized (server) {
            server.setSecretObjective(this.id, this.view.getSecretObjective());
        }
    }

    /**
     * This method starts the game from a file previously saved.
     */
    @Override
    public void startGameFromFile() {
        synchronized (server) {
            server.startGameFromFile();
        }
    }

    /**
     * This method returns the playable positions.
     * It is called when the server sends a message to get the playable positions.
     * It returns the playable positions.
     * @return the playable positions
     */
    @Override
    public boolean[][] getPlayablePositions() {
        synchronized (server) {
            server.getPlayablePositions(this.id);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return getPlayPosResponse;
    }
    /**
     * This method returns the secret objective.
     * It is called when the server sends a message to get the secret objective.
     * It returns the secret objective.
     * @return the secret objective
     */
    @Override
    public Objectives getSecretObjective() {
        synchronized (server) {
            server.getSecretObjective(this.id);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return getSecretObjectiveResponse;
    }
    /**
     * This method plays a card.
     * It is called when the server sends a message to play a card.
     * It plays a card and returns true if the card is played, false otherwise.
     * @param onHandCard the card on hand
     * @param onTableCardX the card on table x
     * @param onTableCardY the card on table y
     * @param onTableCardCorner the card on table corner
     * @param isFront true if the card is front, false otherwise
     * @return true if the card is played, false otherwise
     */
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, boolean isFront) {
        synchronized (server) {
            server.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, this.id, isFront);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return playResponse;
    }

    /**
     * This method draws a card.
     * It is called when the server sends a message to draw a card.
     * It draws a card.
     * @param position the position to draw the card
     */
    @Override
    public void drawCard(int position) {
        synchronized (server) {
            server.drawCard(position, this.id);
        }
        this.isMyTurn = false;
        view.showTurn();
    }

    /**
     * This method returns the cards on hand.
     * It is called when the server sends a message to get the cards on hand.
     * It returns the cards on hand.
     * @return the cards on hand
     */
    @Override
    public ResourceCard[][] getCardsOnHand() {
        synchronized (server) {
            server.getCardsOnHand();
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return getCardsOnHandResponse;
    }
    /**
     * This method returns the placed cards.
     * It is called when the server sends a message to get the placed cards.
     * It returns the placed cards.
     * @param playerId the id of the player
     * @return the placed cards
     */
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        synchronized (server) {
            server.getPlacedCards(playerId);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return getPlacedCardsResponse;
    }

    /**
     * This method returns the resource counter.
     * It is called when the server sends a message to get the resource counter.
     * It returns the resource counter.
     * @param playerId the id of the player
     * @return the resource counter
     */
    @Override
    public int[] getResourceCounter(int playerId) {
        synchronized (server) {
            server.getResourceCounter(playerId);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return getResourceCounterResponse;
    }
    /**
     * This method returns the nicknames.
     * It is called when the server sends a message to get the nicknames.
     * It returns the nicknames.
     * @return the nicknames
     */
    @Override
    public String[] getNicknames() {
        synchronized (server) {
            server.getNicknames();
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return nicknamesResponse;
    }

    /**
     * This method sends a message.
     * @param msg the message to send
     */
    @Override
    public void sendMessage(ChatMessage msg) {
        synchronized (server) {
            server.sendMessage(msg);
        }
    }

    /**
     * This method sends a private message.
     * @param msg the private message to send
     */
    @Override
    public void sendPrivateMessage(ChatMessage msg) {
        synchronized (server) {
            server.sendPrivateMessage(msg);
        }
    }
    /**
     * This method returns the id of the client locally.
     * @return the id of the client
     */
    @Override
    public int getIdLocal() {
        return this.id;
    }

    /**
     * This method returns the nickname of the client locally.
     * @return the nickname of the client
     */
    @Override
    public String getNicknameLocal() {
        return this.nickname;
    }

    /**
     * This method returns true if it is the turn of the client, false otherwise.
     * @return true if it is the turn of the client, false otherwise
     */
    @Override
    public boolean isItMyTurn() {
        return isMyTurn;
    }
    /**
     * This method terminates the client locally.
     */
    @Override
    public void terminateLocal() {
        terminate();
    }

    /**
     * This method gets the number of players.
     */
    private void getNumOfPlayers() {
        synchronized (server) {
            server.getNumOfPlayers();
        }
    }
    /**
     * This method checks if the nickname is available.
     * @param nickname the nickname to check
     * @return true if the nickname is available, false otherwise
     */
    private boolean isNicknameAvailable(String nickname) {
        synchronized (server) {
            server.isNicknameAvailable(nickname);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return nickAvailableResponse;
    }
    /**
     * This method connects to the server.
     * It asks the user to insert the server IP and port.
     * It creates a new socket and runs the client.
     * If the connection fails, it asks the user to try again.
     */
    public static void connectToServer() {

        String ip;
        boolean done = false;
        System.out.println("Insert the server IP: ");
        Scanner scan = new Scanner(System.in);
        ip = scan.next();

        try {
            Socket socket = new Socket(ip, PORT);
            new SocketClient(socket).run();

        } catch (IOException e) {
            System.err.println(e.getMessage() + "\nServer is not up yet... Try again later.");
            connectToServer();
        }
    }
}