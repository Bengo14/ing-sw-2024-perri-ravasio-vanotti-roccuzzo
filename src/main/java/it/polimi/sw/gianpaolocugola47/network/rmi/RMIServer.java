package it.polimi.sw.gianpaolocugola47.network.rmi;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketServer;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
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
                       } catch (RemoteException _) {}
                   }
               }
               try {
                   Thread.sleep(500);
               } catch (InterruptedException _) {}
           }
       }).start();
    }

    public void terminateGame() {
        this.terminated = true;
        synchronized (this.clients) {
            try {
                for (VirtualView view : clients)
                    view.terminate();
            } catch (RemoteException ignored) {}
        }
        resetGame();
    }
    private void terminateGame(boolean gameOver, int clientId) throws RemoteException {
        System.err.println("terminating the game...");
        this.terminated = true;

        if(gameOver) { // the game is ended
            for (VirtualView view : clients)
                if (view.getId() != clientId) // consider real id
                    view.gameOver();
        } else { // some client has disconnected
            for (VirtualView view : clients)
                if (clients.indexOf(view) != clientId) // consider local id
                    view.terminate();
            SocketServer.getServer().terminateGame();
        }
        resetGame();
    }
    private void resetGame() {
        synchronized (this.controller) {
            controller.resetGame();
            controller.addModelObserver(this);
            controller.addModelObserver(SocketServer.getServer());
        }
        synchronized (this.clients) {
            clients.clear();
        }
    }

    /* --- methods of interface VirtualServer --- */

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
    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        synchronized (this.controller) {
            this.controller.setNumOfPlayers(num);
        }
    }
    @Override
    public void addPlayer(int id, String nickname) throws RemoteException {
        synchronized (this.controller) {
            System.out.println(STR."Player \{nickname} added with id \{id}");
            this.controller.addPlayer(id, nickname);
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
    @Override
    public void turnCardOnHand(int playerId, int position) throws RemoteException {
        synchronized (controller) {
            controller.turnCardOnHand(playerId, position);
        }
    }
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId) throws RemoteException {
        synchronized (controller) {
            return controller.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId);
        }
    }
    @Override
    public void drawCard(int position, int playerId) throws RemoteException {
        synchronized (controller) {
            controller.drawCard(position, playerId);
        }
    }
    @Override
    public ResourceCard[][] getCardsOnHand() throws RemoteException {
        synchronized (controller) {
            return controller.getCardsOnHand();
        }
    }
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) throws RemoteException {
        synchronized (controller) {
            return controller.getPlacedCards(playerId);
        }
    }
    @Override
    public int[] getResourceCounter(int playerId) throws RemoteException {
        synchronized (controller) {
            return controller.getResourceCounter(playerId);
        }
    }

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

    /* --- methods of interface Observer --- */

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

        if(RMIServer.server == null) {

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
            System.err.println("Server ready ---> IP: " + SERVER_ADDRESS + ", Port: " + SERVER_PORT);
            SocketServer.initSocketServer(controller);
        }
    }
}
