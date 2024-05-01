package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.model.*;
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
            runCli();
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

    private void runCli() throws RemoteException {

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
        }
        else {
            System.out.println("A game is already starting, you connected to the server with id: "+this.id);
            System.out.print("Insert your nickname: ");
        }
        this.nickname = scan.next();
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
    public void showWinner(){
        System.out.println("\nYou won the game!");
        this.terminate = true;
    }
    @Override
    public void startGame() throws RemoteException {
        if(isCliChosen) {
            this.cli = new CLI(this);
            this.cli.start();
        }
        else {
            /*todo GUI*/
        }
    }
    @Override
    public void secretObjectivesDrawn(Objectives[] obj) throws RemoteException {
        /*todo update cli/gui*/
    }
    @Override
    public void startingCardDrawn(StartingCard card) throws RemoteException{
        /*todo*/
    }
    @Override
    public void showTurn(){
        /*todo*/
    }
    @Override
    public void setViewFixedParams(String[] nicknames, Objectives[] globalObj, Objectives secretObj) throws RemoteException {
        /*todo*/
    }
    @Override
    public void updateView(ResourceCard[] cardsOnHand, PlaceableCard[][] placedCards) throws RemoteException {
        /*todo*/
    }
    @Override
    public void updateView(int[] boardPoints, int[] globalPoints, ResourceCard[] cardsOnTable) throws RemoteException {
        /*todo*/
    }
    @Override
    public void showPlayablePositions(boolean [][] matrix) throws RemoteException {
        /*todo*/
    }
    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    @Override
    public int getId() throws RemoteException {
        return this.id;
    }

    public void receiveMessage(ChatMessage message) throws RemoteException {
        System.out.println("Client received public message: " + message.getMessage() + " from player: " + message.getSender());
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {
        System.err.println("Client received private message: " + message.getMessage() + " from player: " + message.getSender());
    }

    public void inputLoop() throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        ChatMessage message = new ChatMessage(nickname, id);
        //noinspection InfiniteLoopStatement
        while(true){
            String line = br.readLine();
            if(line.startsWith("@")){
                try{
                    message.setPrivate(true);
                    message.setReceiver(line.substring(1, line.indexOf(" ")));
                    message.setMessage(line.substring(line.indexOf(" ")+1));
                    this.server.sendPrivateMessage(message);
                } catch (StringIndexOutOfBoundsException e){
                    System.err.println("Invalid input, try again...");
                }
            } else {
                message.setPrivate(false);
                message.setMessage(line);
                this.server.sendMessage(message);
            }
        }
    }

    public static void main(String[] args){

        /*todo scelta tecnologia di rete da usare*/
        try {
            Registry registry = LocateRegistry.getRegistry(RMIServer.SERVER_ADDRESS, RMIServer.SERVER_PORT);
            VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
            new RMIClient(server).run();
        } catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
    }
}
