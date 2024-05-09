package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.CLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualView {

    private final VirtualServer server;
    private int id;
    private volatile boolean terminate = false;
    private CLI cli;
    private boolean isCliChosen;
    private int numOfPlayers;
    private String nickname;
    private boolean isMyTurn = false;
    private StartingCard startingCard;
    private Objectives secretObjective;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnHand;
    private ResourceCard[] cardsOnTable;
    private GoldCard goldCardOnTop;
    private ResourceCard resourceCardOnTop;
    private int globalPoints;
    private int boardPoints;

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
            try{
                runCli();
            } catch (IOException e) {
                System.out.println("Oops! Issues with input found. Terminating game.");
                terminate();
            }
        } catch (RemoteException e) {
            terminate();
        }
    }
    private void terminationCheckerStart(){
        new Thread(()->{
            while (!terminate) {
                Thread.onSpinWait();
            }
            System.exit(0);
        }).start();
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
            while(!this.server.isNicknameAvailable(this.nickname,this.id)
                    || this.nickname.isEmpty() || this.nickname == null) {
                System.out.print("Nickname invalid or already taken, try again: ");
                System.out.println(this.nickname);
                this.nickname = br.readLine();
                if(nickname.contains(" ")) {
                    this.nickname = nickname.replace(" ","_");
                }
            }
        }
        System.out.println("You joined the game! Waiting for other players...");
        this.server.addPlayer(this.id, nickname);
    }

    @Override
    public void terminate() throws RemoteException{
        System.err.println("\nTerminating the game, because something went wrong...");
        this.terminate = true;
    }
    @Override
    public void ping() throws RemoteException{
        //do nothing, liveness check only
    }
    @Override
    public void gameOver() {
        System.err.println("\nGame Over!");
        this.terminate = true;
    }
    @Override
    public void startGame() throws RemoteException {
        if(isCliChosen) {
            this.cli = new CLI(this);
            this.cli.start();
            openChat();
        }
        else {
            /*todo GUI*/
        }
    }
    public void openChat() {
        new Thread(() -> {
            try {
                chatInputLoop(); /* todo open new cli window */
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public void chatInputLoop() throws IOException {
        System.out.println("Chat service is on!\nType --listPlayers to see who your opponents are.\nStart a message with '@' to send a private message.");
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        ChatMessage message = new ChatMessage(nickname, id);
        //noinspection InfiniteLoopStatement
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
    public void drawStartingCard() {
        // richiede la carta al server
        try {
            startingCard = server.drawStartingCard();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("You have drawn: " + startingCard);
    }
    public void setStartingCardAndDrawObjectives() {
        // richiede al server di impostare la carta iniziale e di pescare gli obiettivi
        Objectives[] objectives;
        try {
            objectives = server.setStartingCardAndDrawObjectives(this.id,startingCard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Choose the objective you want to keep: ");
        for (Objectives obj : objectives) {
            System.out.println(obj);
        }
        Scanner scan = new Scanner(System.in);
        int secretObjective = scan.nextInt();
    }
    public void setSecretObjective(){
        //set the objective chosen by the player in setStartingCardAndDrawObjectives
        try {
            server.setSecretObjective(this.id, secretObjective);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean[][] getPlayablePositions(){
        //richiede al server le posizioni giocabili
        boolean[][] playablePositions;
        try {
           playablePositions = server.getPlayablePositions(this.id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return playablePositions;
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
        System.out.println("\nIt's your turn!");

    }
    @Override
    public void showWinner(){
        System.out.println("\nYou won the game!");
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

    public int getGlobalPoints() throws RemoteException {
        return this.globalPoints;
    }
    public int getBoardPoints() throws RemoteException {
        return this.boardPoints;
    }

    @Override
    public void receiveMessage(ChatMessage message) throws RemoteException {
        System.out.println(message.getSender() + ": " + message.getMessage());
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {
        System.err.println(message.getSender() + ": psst, " + message.getMessage());
    }

    public static void main(String[] args) {

        /*todo scelta tecnologia di rete da usare*/
        try {
            Registry registry = LocateRegistry.getRegistry(RMIServer.SERVER_ADDRESS, RMIServer.SERVER_PORT);
            VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
            new RMIClient(server).run();
        } catch(RemoteException | NotBoundException e){
            System.err.println("Server is not up yet. Try again later.");
            e.printStackTrace();
        }
    }
}
