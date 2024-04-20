package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.Deck;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements VirtualServer {

    public static final int SERVER_PORT = 1234;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private final Controller controller;
    private final List<VirtualView> clients = new ArrayList<>();
    private int numOfPlayers = -1;
    private volatile boolean terminated = false;

    public RMIServer(Controller controller) throws RemoteException {
        super(0);
        this.controller = controller;
        pingStart();
    }

    @Override
    public int connect(VirtualView client) throws RemoteException {
        synchronized (this.clients) {
            if(this.clients.size() == this.numOfPlayers || (numOfPlayers==-1 && !this.clients.isEmpty())) {
                System.err.println("Connection Refused");
                return -1;
            }
            else {
                System.out.println("New client connected");
                this.terminated = false;
                this.clients.add(client);
                return this.clients.indexOf(client);
            }
        }
    }
    private void pingStart() {
       new Thread(()->{
           while(true) {
               while (terminated)
                   Thread.onSpinWait();
               System.err.println("Ping control");
               VirtualView view = null;
               try {
                   synchronized (this.clients) {
                       for (VirtualView v : this.clients) {
                           view = v;
                           v.ping();
                       }
                   }
               } catch (RemoteException e) {
                   synchronized (this.clients) {
                       try {
                           terminateGame(clients.indexOf(view));
                       } catch (RemoteException ex) {
                           e.printStackTrace();
                       }
                   }
               }
               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }
    @Override
    public void terminateGame(int deadClientId) throws RemoteException {
        System.err.println("terminating the game...");
        this.terminated = true;
        synchronized (this.clients) {
            for (int i=0; i<clients.size(); i++)
                if(i!=deadClientId)
                    clients.get(i).terminate();
        }
        resetGame();
    }
    private void resetGame() {
        synchronized (this.controller) {
            controller.resetGame();
        }
        this.numOfPlayers = -1;
        synchronized (this.clients) {
            clients.clear();
        }
    }
    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        synchronized (this.controller) {
            this.controller.setNumOfPlayers(num);
        }
        this.numOfPlayers = num;
    }
    @Override
    public void addPlayer(int id, String nickname) throws RemoteException {
        synchronized (this.controller) {
            this.controller.addPlayer(id, nickname);
            System.out.println("Player "+nickname+" added with id "+id);
            if (this.controller.getNumOfPlayersCurrentlyAdded() == numOfPlayers) {
                this.controller.startGame();
                System.out.println("Game started");
                startGame();
            }
        }
    }
    private void startGame() throws RemoteException {
        synchronized (this.clients){
            for(VirtualView client : this.clients){
                client.startGame();
            }
        }
    }

    public static void main(String[] args) {

        String name = "VirtualServer";
        System.setProperty("java.rmi.server.hostname", SERVER_ADDRESS);
        try {
            VirtualServer stub = new RMIServer(new Controller());
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            registry.rebind(name, stub);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        System.err.println("Server ready ---> IP: "+SERVER_ADDRESS+", Port: "+SERVER_PORT);
    }
}
