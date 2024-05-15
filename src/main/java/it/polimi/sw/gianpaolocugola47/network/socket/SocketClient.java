package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.CLI;
import it.polimi.sw.gianpaolocugola47.view.GUI;
import it.polimi.sw.gianpaolocugola47.view.View;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClient implements VirtualView, Client {
    private final BufferedReader input;
    private final SocketServerProxy server;
    private int id;
    private View view;
    private boolean isCliChosen = false;
    private int numOfPlayers = 0;
    private String nickname = "";
    private boolean isMyTurn = false;

    protected SocketClient(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.server = new SocketServerProxy(output);
    }

    public void run() throws RemoteException {

        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (IOException e) {
                terminate();
            }
        }).start();
    }

    private void runVirtualServer() throws IOException {
        String line;

        while (true) {
            line = input.readLine();

            switch (line) {
                case "setId" -> setId(Integer.parseInt(input.readLine()));
                case "terminate" -> terminate();
                case "ping" -> ping();
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void setId(int id) {

        if(id == -1) {
            System.err.println("Connection refused: match already started/full or number of players not set...");
            System.exit(1);
        }

        this.id = id;
        System.err.println("Client connected with id: " + id);

        new Thread(()->{
            try {
                runCli();
            } catch (IOException e) {
                terminate();
            }
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
    public void terminate() {
        System.err.println("\nTerminating the game, because something went wrong...");
        System.exit(1);
    }

    @Override
    public void ping() {
        this.server.pingAck();
    }

    @Override
    public void startGame() {
        if(isCliChosen) {
            this.view = new CLI(this);
            new Thread(() -> view.start()).start();
        }
        else {
            this.view = new GUI(this);
            new Thread(() -> view.start()).start();
        }
    }

    @Override
    public void showTurn() {
        this.isMyTurn = true;
    }

    @Override
    public void gameOver() {
        /*todo*/
        terminate();
    }

    @Override
    public void showWinner() {
        /*todo*/
        terminate();
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void receiveMessage(ChatMessage message) {

    }

    @Override
    public void receivePrivateMessage(ChatMessage message) {

    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {

    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {

    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {

    }

    /* --- methods of interface Client --- */

    @Override
    public StartingCard drawStartingCard() {

        return null;
    }

    @Override
    public Objectives[] setStartingCardAndDrawObjectives() {
        return null;
    }

    @Override
    public void setSecretObjective() {

    }

    @Override
    public boolean[][] getPlayablePositions() {
        return new boolean[0][];
    }

    @Override
    public void turnCardOnHand(int position) {

    }

    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner) {
        return false;
    }

    @Override
    public void drawCard(int position) {

    }

    @Override
    public ResourceCard[][] getCardsOnHand() {
        return new ResourceCard[0][];
    }

    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        return new PlaceableCard[0][];
    }

    @Override
    public int[] getResourceCounter(int playerId) {
        return new int[0];
    }

    @Override
    public int getGlobalPoints() {
        return 0;
    }

    @Override
    public int getIdLocal() {
        return 0;
    }

    @Override
    public String getNicknameLocal() {
        return null;
    }

    @Override
    public String[] getNicknames() {
        return new String[0];
    }

    @Override
    public void sendMessage(ChatMessage msg) {

    }

    @Override
    public void sendPrivateMessage(ChatMessage msg) {

    }

    @Override
    public int getBoardPoints() {
        return 0;
    }

    @Override
    public void terminateLocal() {
        terminate();
    }

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(SocketServer.SERVER_ADDRESS, SocketServer.SERVER_PORT);
            InputStreamReader socketRx = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(socket.getOutputStream());

            new SocketClient(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();

        } catch (IOException e) {
            System.err.println(e.getMessage() + "\nServer is not up yet... Try again later.");
            System.exit(1);
        }
    }
}
