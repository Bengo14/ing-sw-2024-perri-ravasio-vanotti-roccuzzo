package it.polimi.sw.gianpaolocugola47.network.rmi;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.CLI;
import it.polimi.sw.gianpaolocugola47.view.GUI;
import it.polimi.sw.gianpaolocugola47.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualView, Client {

    private final VirtualServer server;
    private volatile boolean terminate = false;
    private View view;
    private int id;
    private boolean isCliChosen = false;
    private int numOfPlayers = 0;
    private String nickname = "";
    private boolean isMyTurn = false;

    /* todo attributes to be moved to view below */
    private StartingCard startingCard;
    private Objectives secretObjective;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnHand;
    private ResourceCard[] cardsOnTable;
    private GoldCard goldCardOnTop;
    private ResourceCard resourceCardOnTop;
    private int globalPoints = 0;
    private int boardPoints = 0;

    private RMIClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException {
        try {
            this.id = this.server.connect(this);
            if(id == -1) {
                System.err.println("Connection refused: match already started/full or number of players not set...");
                System.exit(0);
            }
            System.err.println("Client connected with id: "+id);
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
    private void terminationCheckerStart() {
        new Thread(()->{
            while (!terminate) {
                Thread.onSpinWait();
            }
            System.exit(0);
        }).start();
    }

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
            this.server.setNumOfPlayers(numOfPlayers);
        }
        else {
            System.out.println("A game is already starting, you connected to the server with id: " + this.id);

        }
        System.out.print("Insert your nickname: ");
        tempNick = br.readLine();
        if(tempNick.contains(" ")) {
            tempNick = tempNick.replace(" ","_");
        }
        while(!this.server.isNicknameAvailable(tempNick, this.id)
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
        this.server.addPlayer(this.id, this.nickname);
    }

    /* --- methods of interface VirtualView --- */

    @Override
    public void terminate() throws RemoteException {
        terminateLocal();
    }
    @Override
    public void ping() throws RemoteException {
        //do nothing, liveness check only
    }
    @Override
    public void gameOver() {
        System.err.println("\nGame Over!");
        this.terminate = true;
    }
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws RemoteException {
        this.nickname = nicknames[id];
        this.objectives = globalObjectives;
        this.cardsOnHand = cardsOnHand;
        this.cardsOnTable = cardsOnTable;
    }
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) throws RemoteException {
        this.resourceCardOnTop = resourceCardOnTop;
        this.goldCardOnTop = goldCardOnTop;
    }
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) throws RemoteException {
        this.boardPoints = boardPoints[id];
        this.globalPoints = globalPoints[id];
    }
    @Override
    public void showTurn() {
        this.isMyTurn = true;
        //System.out.println("\nIt's your turn!");
        /*todo*/
    }
    @Override
    public void showWinner() {
        //System.out.println("\nYou won the game!");
        this.terminate = true;
    }
    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }
    @Override
    public int getId() throws RemoteException {
        return this.id;
    }

    @Override
    public void receiveMessage(ChatMessage message) throws RemoteException {
        System.out.println(message.getSender() + ": " + message.getMessage());
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {
        System.err.println(message.getSender() + ": psst, " + message.getMessage());
    }
    @Override
    public void startGame() throws RemoteException {
        if(isCliChosen) {
            this.view = new CLI(this);
            new Thread(() -> view.start()).start();
        }
        else {
            this.view = new GUI(this);
            new Thread(() -> view.start()).start();
        }
    }

    /* --- methods of interface Client --- */

    @Override
    public void terminateLocal() {
        System.err.println("\nTerminating the game, because something went wrong...");
        this.terminate = true;
    }
    @Override
    public StartingCard drawStartingCard() {
        try {
            return server.drawStartingCard();
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    @Override
    public Objectives[] setStartingCardAndDrawObjectives() {
        Objectives[] objectives = null;
        try {
            objectives = server.setStartingCardAndDrawObjectives(this.id, this.startingCard);
        } catch (RemoteException e) {
            terminateLocal();
        }
        return objectives;
    }
    @Override
    public void setSecretObjective() {
        try {
            server.setSecretObjective(this.id, this.secretObjective);
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    @Override
    public boolean[][] getPlayablePositions() {
        try {
           return server.getPlayablePositions(this.id);
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    @Override
    public void turnCardOnHand(int position) {
        try {
            server.turnCardOnHand(this.id, position);
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner) {
        try {
            return server.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, this.id);
        } catch (RemoteException e) {
            terminateLocal();
            return false;
        }
    }
    @Override
    public void drawCard(int position) {
        try {
            server.drawCard(position, this.id);
            this.isMyTurn = false;
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    @Override
    public ResourceCard[][] getCardsOnHand() {
        try {
            return server.getCardsOnHand();
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        try {
            return server.getPlacedCards(playerId);
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    @Override
    public int[] getResourceCounter(int playerId) {
        try {
            return server.getResourceCounter(playerId);
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    @Override
    public int getGlobalPoints() {
        return this.globalPoints;
    }
    @Override
    public int getBoardPoints() {
        return this.boardPoints;
    }
    @Override
    public int getIdLocal() {return this.id;}
    @Override
    public String getNicknameLocal() {return this.nickname;}
    @Override
    public String[] getNicknames() {
        try {
            return server.getNicknames();
        } catch (RemoteException e) {
            terminateLocal();
            return null;
        }
    }
    @Override
    public void sendMessage(ChatMessage msg) {
        try {
            server.sendMessage(msg);
        } catch (RemoteException e) {
            terminateLocal();
        }
    }
    @Override
    public void sendPrivateMessage(ChatMessage msg) {
        try {
            server.sendPrivateMessage(msg);
        } catch (RemoteException e) {
            terminateLocal();
        }
    }


    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry(RMIServer.SERVER_ADDRESS, RMIServer.SERVER_PORT);
            VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
            new RMIClient(server).run();
        } catch(RemoteException | NotBoundException e) {
            System.err.println(e.getMessage() + "\nServer is not up yet... Try again later.");
            System.exit(1);
        }
    }
}
