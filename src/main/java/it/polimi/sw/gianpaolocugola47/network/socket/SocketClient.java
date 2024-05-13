package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
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
    private final VirtualServer server;
    private volatile boolean terminate = false;
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
            } catch (Exception e) {
                terminate();
            }
        }).start();

        terminationCheckerStart();
        try {
            runCli();
        } catch (IOException e) {
            System.out.println("Oops! Issues with input found. Terminating the game...");
            terminate();
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

    private void runVirtualServer() throws IOException {
        String line;
        while ((line = input.readLine()) != null) {
            switch (line) {
                case "setId" -> this.setId(Integer.parseInt(input.readLine()));
                /*todo*/
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void setId(int id) {
        if(id == -1) {
            System.err.println("Connection refused: match already started/full or number of players not set...");
            System.exit(0);
        }
        this.id = id;
        System.err.println("Client connected with id: " + id);
    }

    private void runCli() throws IOException {

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
            System.out.print("Okay, now insert your nickname: ");
            this.nickname = br.readLine();
            while(nickname.isEmpty() || nickname == null){
                System.out.print("Invalid nickname, try again: ");
                this.nickname = scan.next();
            }
            if(nickname.contains(" ")) {
                this.nickname = nickname.replace(" ","_");
            }
        }
        else {
            System.out.println("A game is already starting, you connected to the server with id: " + this.id);
            System.out.print("Insert your nickname: ");
            this.nickname = br.readLine();
            if(nickname.contains(" ")) {
                this.nickname = nickname.replace(" ","_");
            }
            while(!this.server.isNicknameAvailable(this.nickname, this.id)
                    || this.nickname.isEmpty()) {
                System.out.print("Nickname invalid or already taken, try again: ");
                System.out.println(this.nickname);
                this.nickname = br.readLine();
                if(nickname.contains(" ")) {
                    this.nickname = nickname.replace(" ","_");
                }
            }
        }
        System.out.println("You joined the game! Waiting for other players...");
        this.server.addPlayer(this.id, this.nickname);
    }

    private void openChat() {
        new Thread(() -> {
            try {
                chatInputLoop();
            } catch (IOException e) {
                terminate();
            }
        }).start();
    }
    public void chatInputLoop() throws IOException {

        System.out.println("Chat service is on!\nType --listPlayers to see who your opponents are.\nStart a message with '@' to send a private message.");
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        ChatMessage message = new ChatMessage(nickname, id);

        while(true) {
            String line = br.readLine();
            if (line.startsWith("@")) {
                try {
                    message.setPrivate(true);
                    message.setReceiver(line.substring(1, line.indexOf(" ")));
                    message.setMessage(line.substring(line.indexOf(" ") + 1));
                    this.server.sendPrivateMessage(message);
                } catch (StringIndexOutOfBoundsException e) {
                    System.err.println("Invalid input, try again...");
                }
            } else if (line.equals("--listPlayers")) {
                this.server.getNicknames();
                System.out.println("Here's a list of all the players in the lobby: ");
                for(String nickname : this.server.getNicknames()){
                    if(nickname.equals(this.nickname))
                        System.err.println(nickname + " (you)");
                    else
                        System.out.println(nickname);
                }
            } else {
                message.setPrivate(false);
                message.setMessage(line);
                this.server.sendMessage(message);
            }
        }
    }

    /* --- methods of interface VirtualView --- */

    @Override
    public void terminate() {
        System.err.println("\nTerminating the game, because something went wrong...");
        this.terminate = true;
    }

    @Override
    public void ping() {
        /*todo*/
    }

    @Override
    public void startGame() throws RemoteException {
        if(isCliChosen) {
            this.view = new CLI(this);
            view.start();
        }
        else {
            this.view = new GUI(this);
            view.start();
        }
        openChat();
    }

    @Override
    public void showTurn() {

    }

    @Override
    public void gameOver() {

    }

    @Override
    public void showWinner() {

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
    public void drawStartingCard() {

    }

    @Override
    public void setStartingCardAndDrawObjectives() {

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
    public int getBoardPoints() {
        return 0;
    }

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(SocketServer.SERVER_ADDRESS, SocketServer.SERVER_PORT);
            InputStreamReader socketRx = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(socket.getOutputStream());

            new SocketClient(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();

        } catch (IOException e) {
            System.err.println("Error: Unable to connect to server: " + e.getMessage());
            System.exit(1);
        }
    }
}
