package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
import it.polimi.sw.gianpaolocugola47.socket.SocketServer;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements VirtualServer, Observer {

    public static final int SERVER_PORT = 1234;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private static RMIServer server;
    private final Controller controller;
    private final List<VirtualView> clients;
    private int numOfPlayers = -1;
    private volatile boolean terminated = false;

    public static RMIServer getServer() {
        if (RMIServer.server == null) {
            try {
                RMIServer.server = new RMIServer(new Controller());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return RMIServer.server;
    }

    private RMIServer(Controller controller) throws RemoteException {
        super(0);
        this.controller = controller;
        this.clients = new ArrayList<>();
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
               //System.err.println("Ping control");
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
                           terminateGame(false, clients.indexOf(view));
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
    private void terminateGame(boolean gameOver, int clientId) throws RemoteException {
        System.err.println("terminating the game...");
        this.terminated = true;

        synchronized (this.clients) {
            if(gameOver) {
                for (int i=0; i<clients.size(); i++)
                    if (i != clientId)
                        clients.get(i).gameOver();
            } else {
                for (int i = 0; i < clients.size(); i++)
                    if (i != clientId)
                        clients.get(i).terminate();
            }
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
                System.out.println("Game started");
                startGame();
            }
        }
    }
    private void startGame() throws RemoteException {
        synchronized (this.clients) {
            for(VirtualView client : this.clients) {
                client.startGame();
            }
        }
    }
    @Override
    public StartingCard drawStartingCard() throws RemoteException {
        synchronized (controller) {
            return this.controller.drawStartingCard();
        }
    }
    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) throws RemoteException {
        synchronized (controller) {
            return this.controller.setStartingCardAndDrawObjectives(playerId, card);
        }
    }
    @Override
    public void setSecretObjective(int playerId, Objectives obj) throws RemoteException {
        synchronized (controller) {
            this.controller.setSecretObjectiveAndUpdateView(playerId, obj);
        }
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[][] cardsOnHand, ResourceCard[] cardsOnTable) {

    }
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {

    }
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {

    }
    @Override
    public void showTurn(int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(playerId).showTurn();
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void showWinner(int winner) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(winner).showWinner();
                    terminateGame(true, winner);
                } catch (RemoteException ignored) {}
            }
        }
    }

    public boolean[][] getPlayablePositions(int playerId) {
        synchronized (controller) {
            return this.controller.getPlayablePositions(playerId);
        }
    }

    public void login() throws RemoteException {}

    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        System.out.println("Server received public message: " + message.getMessage());
        for (VirtualView client : this.clients) {
            client.receiveMessage(message);
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage message) throws RemoteException {
        System.out.println("Server received private message: " + message.getMessage());
        for (VirtualView client : this.clients) {
            if(client.getNickname().equals(message.getReceiver())){
                client.receivePrivateMessage(message);
            }
        }
    }

    @Override
    public boolean isNicknameAvailable(String nickname, int id) throws RemoteException {
        for(VirtualView client: this.clients)
            if(client.getNickname().equals(nickname) && clients.indexOf(client) != id)
                return false;
        return true;
    }

    @Override
    public String[] getNicknames() throws RemoteException {
        synchronized (controller) {
            return controller.getNicknames();
        }
    }

    public static void main(String[] args) {

        String name = "VirtualServer";
        System.setProperty("java.rmi.server.hostname", SERVER_ADDRESS);
        Controller controller = new Controller();
        try {
            RMIServer.server = new RMIServer(controller);
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            registry.rebind(name, RMIServer.server);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready ---> IP: "+SERVER_ADDRESS+", Port: "+SERVER_PORT);
        SocketServer.initSocketServer(controller);
    }
}
