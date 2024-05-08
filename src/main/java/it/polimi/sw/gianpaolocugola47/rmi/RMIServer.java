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
        this.controller.addModelObserver(this);
        this.clients = new ArrayList<>();
        pingStart();
    }

    @Override
    public int connect(VirtualView client) throws RemoteException {
        synchronized (this.clients) {
            synchronized (this.controller) {
                if (controller.getClientsConnected() == controller.getNumOfPlayers() || (controller.getNumOfPlayers() == -1 && controller.getClientsConnected() > 0)) {
                    System.err.println("Connection Refused");
                    return -1;
                } else {
                    System.out.println("New client connected");
                    this.terminated = false;
                    this.clients.add(client);
                    this.controller.addClientConnected();
                    return this.controller.getClientsConnected() - 1;
                }
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
                           terminateGame(false, clients.indexOf(view)); // local id of dead client
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
            if(gameOver) { // the game is ended
                for (VirtualView view : clients)
                    if (view.getId() != clientId) // consider real id
                        view.gameOver();
            } else { // some client has disconnected
                for (VirtualView view : clients)
                    if (clients.indexOf(view) != clientId) // consider local id
                        view.terminate();
                /*todo call socket terminateGame (and reset)*/
            }
        }
        resetGame();
    }
    private void resetGame() {
        synchronized (this.controller) {
            controller.resetGame();
        }
        synchronized (this.clients) {
            clients.clear();
        }
    }
    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        synchronized (this.controller) {
            this.controller.setNumOfPlayers(num);
        }
    }
    @Override
    public void addPlayer(int id, String nickname) throws RemoteException {
        synchronized (this.controller) {
            System.out.println("Player "+nickname+" added with id "+id);
            this.controller.addPlayer(id, nickname);
            /*todo*/
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
    public boolean[][] getPlayablePositions(int playerId) throws RemoteException {
        synchronized (controller) {
            return this.controller.getPlayablePositions(playerId);
        }
    }
    /*todo add methods to interface*/
    @Override
    public void login() throws RemoteException {
        /*todo*/ // what is it meant for?
    }

    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        System.out.println("Received public message");
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                client.receiveMessage(message);
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage message) throws RemoteException {
        System.out.println("Received private message");
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                if (client.getNickname().equals(message.getReceiver()))
                    client.receivePrivateMessage(message);
        }
    }

    @Override
    public boolean isNicknameAvailable(String nickname, int id) throws RemoteException {
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                if (client.getNickname().equals(nickname) && client.getId() != id)
                    return false;
            return true;
        }
    }

    @Override
    public String[] getNicknames() throws RemoteException {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }

    // --- methods of interface Observer ---
    @Override
    public void startGame() {
        System.out.println("Game started");
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        view.startGame();
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[][] cardsOnHand, ResourceCard[] cardsOnTable) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        view.initView(nicknames, globalObjectives, cardsOnHand[view.getId()], cardsOnTable);
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        view.updateDecks(resourceCardOnTop, goldCardOnTop);
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        view.updatePoints(boardPoints, globalPoints);
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void showTurn(int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        if(view.getId() == playerId)
                            view.showTurn();
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void showWinner(int winnerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        if(view.getId() == winnerId)
                            view.showWinner();
                    terminateGame(true, winnerId);
                } catch (RemoteException ignored) {}
            }
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
