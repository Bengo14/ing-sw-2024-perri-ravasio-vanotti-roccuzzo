package it.polimi.sw.gianpaolocugola47.network.rmi;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.cli.CLI;
import it.polimi.sw.gianpaolocugola47.view.gui.ViewGui;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * This class represents the client in the RMI network.
 */
public class RMIClient extends UnicastRemoteObject implements VirtualView, Client {

    private final VirtualServer server;
    private volatile boolean terminate = false;
    private View view;
    private int id;
    private boolean isCliChosen = false;
    private int numOfPlayers = 0;
    private String nickname = "";
    private boolean isMyTurn = false;
    /**
     * Constructor of the class.
     * @param server the server to connect to.
     * @throws RemoteException if there are issues with the connection.
     */
    private RMIClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }
    /**
     * This method is used to connect the client to the server, it also runs the CLI.
     * @throws RemoteException if there are issues with the connection.
     */
    private void run() throws RemoteException {
        try {
            this.id = this.server.connect(this);
            if(id == -1) {
                System.err.println("Connection refused: match already started/full or number of players not set...");
                System.exit(0);
            }
            System.err.println("Client connected with id: "+id);
            getNumOfPlayers();
            terminationCheckerStart();

            try {
                runCli();
            } catch (IOException e) {
                System.out.println("Oops! Issues with input found. Terminating the game...");
                terminateLocal();
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
    }

    /**
     * This method is used to start the termination checker.
     */
    private void terminationCheckerStart() {
        new Thread(()->{
            while (!terminate) {
                Thread.onSpinWait();
            }
            System.exit(1);
        }).start();
    }
    /**
     * This method is used to run the CLI.
     * It asks the user to choose the interface, the number of players and the nickname.
     * @throws IOException if there are issues with the input.
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
        while(!this.server.isNicknameAvailable(tempNick)
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
     * This method is used to terminate the client, it calls the local terminate method.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void terminate() throws RemoteException {
        terminateLocal();
    }
    /**
     * This method is used to ping the server.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void ping() throws RemoteException {
        // do nothing, liveness check only
    }
    /**
     * This method is used to start the game, it creates the CLI or the GUI.
     * @param isLoaded if the game is already loaded.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void startGame(boolean isLoaded) throws RemoteException {

        if(isCliChosen) {
            try{
                this.view = new CLI(this,isLoaded);
                new Thread(() -> view.start()).start();
            }catch(Exception e){
                System.err.println("Error in starting the CLI. Shutting down.");
                terminateLocal();
            }
        }
        else {
            try{
                this.view = new ViewGui(isLoaded);
                this.view.setClient(this);
                new Thread(() -> Platform.startup(() -> view.start())).start();
            }catch(Exception e){
                System.err.println("Error in starting the GUI. Shutting down.");
                terminateLocal();
            }
        }
    }
    /**
     * This method is used to call the gameOver method of the view.
     *
     */
    @Override
    public void gameOver() throws RemoteException {
        this.view.gameOver();
    }
    /**
     * This method is used to create the view.
     * @param nicknames the nicknames of the players.
     * @param globalObjectives the global objectives.
     * @param cardsOnHand the cards on hand.
     * @param cardsOnTable the cards on table.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws RemoteException {
        this.view.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
    }

    /**
     * This method is used to update the decks
     * @param resourceCardOnTop the resource card on top.
     * @param goldCardOnTop the gold card on top.
     * @param drawPos the draw position.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) throws RemoteException {
        this.view.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
    }

    /**
     * This method is used to update the points.
     * @param boardPoints the board points.
     * @param globalPoints the global points.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) throws RemoteException {
        this.view.updatePoints(boardPoints, globalPoints);
    }
    /**
     * This method is used to set the turn of the player.
     * It calls the showTurn method of the view.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void setMyTurn() throws RemoteException {
        this.isMyTurn = true;
        view.showTurn();
    }
    /**
     * This method is used to show the winner.
     * It calls the showWinner method of the view.
     * @param id the id of the winner.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void showWinner(int id) throws RemoteException {
        this.view.showWinner(id);
    }

    /**
     * This method is used to get the id of the client.
     * @return the id of the client.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public int getId() throws RemoteException {
        return this.id;
    }
    /**
     * This method is used to receive a message.
     * It calls the receiveMessage method of the view.
     * @param message the message received.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void receiveMessage(ChatMessage message) throws RemoteException {
        synchronized (view) {
            view.receiveMessage(message);
        }
    }
    /**
     * This method is used to receive a private message.
     * It calls the receiveMessage method of the view.
     * @param message the message received.
     * @throws RemoteException if there are issues with the connection.
     */
    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {
        message.setPrivate(true);
        synchronized (view) {
            view.receiveMessage(message);
        }
    }

    /* --- methods of interface Client --- */
    /**
     * This method is used to terminate the client locally.
     * It sets the terminate variable to true.
     */
    @Override
    public void terminateLocal() {
        System.err.println("\nTerminating the game...");
        this.terminate = true;
    }

    /**
     * This method draws a starting card.
     * It calls the drawStartingCard method of the server.
     * @return the starting card drawn.
     */
    @Override
    public StartingCard drawStartingCard() {
        try {
            synchronized (server) {
                return server.drawStartingCard();
            }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }

    /**
     * This method is used to set the starting card and draw the secret objectives to choice.
     * @return the secret objectives drawn.
     * It calls the setStartingCardAndDrawObjectives method of the server.
     */
    @Override
    public Objectives[] setStartingCardAndDrawObjectives() {
        Objectives[] objectives = null;
        try {
            synchronized (server) {
                objectives = server.setStartingCardAndDrawObjectives(this.id, this.view.getStartingCard());
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
        return objectives;
    }
    /**
     * This method is used to set the secret objective.
     * It calls the setSecretObjective method of the server.
     */
    @Override
    public void setSecretObjective() {
        try {
            synchronized (server) {
                server.setSecretObjective(this.id, this.view.getSecretObjective());
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    /**
     * This method is used to start the game from a file.
     * It calls the startGameFromFile method of the server.
     */
    @Override
    public void startGameFromFile() {
        try {
            synchronized (server) {
                server.startGameFromFile();
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    /**
     * This method is used to get the playable positions on the board.
     * @return the playable positions.
     * It calls the getPlayablePositions method of the server.
     */
    @Override
    public boolean[][] getPlayablePositions() {
        try {
           synchronized (server) {
               return server.getPlayablePositions(this.id);
           }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    /**
     * This method is used to get the secret objective of the player.
     * It calls the getSecretObjective method of the server.
     * @return the secret objective of the player.
     */
    @Override
    public Objectives getSecretObjective() {
        try {
            synchronized (server) {
                return server.getSecretObjective(this.id);
            }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    /**
     * This method plays a card.
     * It calls the playCard method of the server.
     * @param onHandCard the card on hand to play.
     * @param onTableCardX the x position of the card on the table on which to play the card.
     * @param onTableCardY the y position of the card on the table on which to play the card.
     * @param onTableCardCorner the corner of the card on the table on which to play the card.
     * @param isFront if the card is played on the front.
     * @return true if the card is played, false otherwise.
     */
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, boolean isFront) {
        try {
            synchronized (server) {
                return server.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, this.id, isFront);
            }
        } catch (RemoteException e) {
            terminateLocal();
            return false;
        }
    }
    /**
     * This method is used to draw a card.
     * It calls the drawCard method of the server.
     * It sets the isMyTurn variable to false.
     * It calls the showTurn method of the view.
     * @param position the position of the card to draw.
     */
    @Override
    public void drawCard(int position) {
        try {
            synchronized (server) {
                server.drawCard(position, this.id);
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
        this.isMyTurn = false;
        view.showTurn();
    }
    /**
     * This method is used to get the cards on hand.
     * It calls the getCardsOnHand method of the server.
     * @return the cards on hand.
     */
    @Override
    public ResourceCard[][] getCardsOnHand() {
        try {
            synchronized (server) {
                return server.getCardsOnHand();
            }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    /**
     * This method is used to get the cards on table.
     * It calls the getCardsOnTable method of the server.
     * @return the cards on table.
     */
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        try {
            synchronized (server) {
                return server.getPlacedCards(playerId);
            }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    /**
     * This method is used to get the resource counter of the player.
     * It calls the getResourceCounter method of the server.
     * @param playerId the id of the player.
     * @return the resource counter of the player.
     */
    @Override
    public int[] getResourceCounter(int playerId) {
        try {
            synchronized (server) {
                return server.getResourceCounter(playerId);
            }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }

    /**
     * This method is used to get the id of the local player.
     * @return the id of the local player.
     */
    @Override
    public int getIdLocal() {return this.id;}
    /**
     * This method is used to get the nickname of the local player.
     * @return the nickname of the local player.
     */
    @Override
    public String getNicknameLocal() {return this.nickname;}

    /**
     * This method is used to get the nicknames of the players.
     * It calls the getNicknames method of the server.
     * @return the nicknames of the players.
     */
    @Override
    public String[] getNicknames() {
        try {
            synchronized (server) {
                return server.getNicknames();
            }
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }

    /**
     * This method is used to send a message.
     * It calls the sendMessage method of the server.
     * @param msg the message to send.
     */
    @Override
    public void sendMessage(ChatMessage msg) {
        try {
            synchronized (server) {
                server.sendMessage(msg);
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    /**
     * This method is used to send a private message.
     * It calls the sendPrivateMessage method of the server.
     * @param msg the message to send.
     */
    @Override
    public void sendPrivateMessage(ChatMessage msg) {
        try {
            synchronized (server) {
                server.sendPrivateMessage(msg);
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
    }

    /**
     * This method is used to get the value of the isMyTurn variable.
     * @return the value of the isMyTurn variable.
     */
    @Override
    public boolean isItMyTurn() {
        return isMyTurn;
    }
    /**
     * This method is used to get the number of players.
     * It calls the getNumOfPlayers method of the server and sets the numOfPlayers variable.
     */
    private void getNumOfPlayers() {
        try {
            synchronized (server) {
                numOfPlayers = server.getNumOfPlayers();
            }
        } catch (RemoteException e) {
            terminateLocal();
        }
    }

    /**
     * This method is used to set the ip and the port of the server and connect to it.
     * It asks the user to insert the ip and the port of the server.
     * If the connection is successful, it creates a new RMIClient and runs it.
     */
    public static void connectToServer(){
        int port = 0;
        String ip;
        boolean done = false;
        System.out.println("Insert the server IP: ");
        Scanner scan = new Scanner(System.in);
        ip = scan.next();
        while(!done){
            System.out.println("Insert the server port: ");
            String command = scan.next();
            try {
                port = Integer.parseInt(command);
                done=true;
            } catch (NumberFormatException e ){
                System.out.println(e.getMessage() + " try again");
            }
        }
        try {
            Registry registry = LocateRegistry.getRegistry(ip,port);
            VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
            new RMIClient(server).run();
        } catch(RemoteException | NotBoundException e) {
            System.err.println(e.getMessage() + "\nServer is not up yet... Try again later.");
            connectToServer();
        }
    }
    /**
     * Main method of the class.
     * It calls the connectToServer method.
     * @param args the arguments of the main method.
     */
    public static void main(String[] args) {
        connectToServer();
    }

}