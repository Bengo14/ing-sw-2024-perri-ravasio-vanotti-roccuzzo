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

public class SocketClient implements VirtualView, Client {
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
    boolean nickAvailableResponse;
    String[] nicknamesResponse;
    StartingCard drawStartingCardResponse;
    Objectives[] setStartingResponse;
    boolean playResponse;
    ResourceCard[][] getCardsOnHandResponse;
    PlaceableCard[][] getPlacedCardsResponse;
    int[] getResourceCounterResponse;
    boolean[][] getPlayPosResponse;
    private Objectives getSecretObjectiveResponse;

    protected SocketClient(Socket socket) throws IOException {
        this.server = new SocketServerProxy(socket.getOutputStream());
        this.socket = socket;
    }

    public void run() {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (IOException | ClassNotFoundException e) {
                terminate();
            }
        }).start();
    }

    private void runVirtualServer() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        while (true) {
            SocketMessage message = (SocketMessage) ois.readObject();
            message.doAction(this);
        }
    }

    protected void setResponse() {
        this.response = true;
    }

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

    @Override
    public void terminate() {
        System.err.println("\nTerminating the game...");
        System.exit(1);
    }

    @Override
    public void ping() {
        synchronized (server) {
            this.server.pingAck();
        }
    }

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

    @Override
    public void setMyTurn() {
        this.isMyTurn = true;
        view.showTurn();
    }

    @Override
    public void gameOver() {
        this.view.gameOver();
    }

    @Override
    public void showWinner(int id) {
        this.view.showWinner(id);
    }

    @Override
    public int getId() { // not used here
        return getIdLocal();
    }

    @Override
    public void receiveMessage(ChatMessage message) {
        synchronized (view) {
            view.receiveMessage(message);
        }
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) {
        message.setPrivate(true);
        synchronized (view) {
            view.receiveMessage(message);
        }
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        this.view.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        this.view.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        this.view.updatePoints(boardPoints, globalPoints);
    }

    /* --- methods of interface Client --- */

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

    @Override
    public void setSecretObjective() {
        synchronized (server) {
            server.setSecretObjective(this.id, this.view.getSecretObjective());
        }
    }

    @Override
    public void startGameFromFile() {
        synchronized (server) {
            server.startGameFromFile();
        }
    }

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

    @Override
    public void drawCard(int position) {
        synchronized (server) {
            server.drawCard(position, this.id);
        }
        this.isMyTurn = false;
        view.showTurn();
    }

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

    @Override
    public void sendMessage(ChatMessage msg) {
        synchronized (server) {
            server.sendMessage(msg);
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage msg) {
        synchronized (server) {
            server.sendPrivateMessage(msg);
        }
    }

    @Override
    public int getIdLocal() {
        return this.id;
    }

    @Override
    public String getNicknameLocal() {
        return this.nickname;
    }

    @Override
    public boolean isItMyTurn() {
        return isMyTurn;
    }

    @Override
    public void terminateLocal() {
        terminate();
    }

    private void getNumOfPlayers() {
        synchronized (server) {
            server.getNumOfPlayers();
        }
    }

    private boolean isNicknameAvailable(String nickname) {
        synchronized (server) {
            server.isNicknameAvailable(nickname);
        }
        while(!response)
            Thread.onSpinWait();

        response = false;
        return nickAvailableResponse;
    }

    public static void connectToServer() {
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
            Socket socket = new Socket(ip, port);
            new SocketClient(socket).run();

        } catch (IOException e) {
            System.err.println(e.getMessage() + "\nServer is not up yet... Try again later.");
            connectToServer();
        }
    }

    public static void main(String[] args) {
        connectToServer();
    }
}