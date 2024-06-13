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
    private volatile boolean terminated = true;

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
            resetGame();
        }
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
            new Thread(() -> SocketServer.getServer().terminateGame()).start();
        }
        resetGame();
    }
    private void resetGame() {
        synchronized (this.controller) {
            controller.resetGame();
            controller.addModelObserver(this);
            controller.addModelObserver(SocketServer.getServer());
        }
        clients.clear();
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
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) throws RemoteException {
        synchronized (controller) {
            return controller.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId, isFront);
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
    public void sendMessage(ChatMessage message) throws RemoteException {
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                client.receiveMessage(message);
        }
        SocketServer.getServer().sendMessageFromRmi(message);
    }
    public void sendMessageFromSocket(ChatMessage message) throws RemoteException {
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                client.receiveMessage(message);
        }
    }
    @Override
    public void sendPrivateMessage(ChatMessage message) throws RemoteException {
        String [] nicknames = getNicknames();
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                if (nicknames[client.getId()].equals(message.getReceiver()))
                    client.receivePrivateMessage(message);
        }
        SocketServer.getServer().sendPrivateMessageFromRmi(message);
    }
    public void sendPrivateMessageFromSocket(ChatMessage message) throws RemoteException {
        String [] nicknames = getNicknames();
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                if (nicknames[client.getId()].equals(message.getReceiver()))
                    client.receivePrivateMessage(message);
        }
    }

    @Override
    public boolean isNicknameAvailable(String nickname) throws RemoteException {
        String [] nicknames = getNicknames();
        for (String nick : nicknames)
            if (nick.equals(nickname))
                return false;
        return true;
    }

    @Override
    public String[] getNicknames() throws RemoteException {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }

    @Override
    public int getNumOfPlayers() throws RemoteException {
        synchronized (controller) {
            return this.controller.getNumOfPlayers();
        }
    }

    /* --- methods of interface Observer --- */

    @Override
    public void startGame() throws RemoteException {
        System.out.println("Game started");
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(VirtualView view : this.clients) {
                    try {
                        view.startGame();
                        System.out.println("Game started for client: " + view.getId());
                    } catch (RemoteException e) {
                        System.err.println("Failed to start game for client: " + view.getId());
                        e.printStackTrace();
                    }
                }
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
                } catch (RemoteException _) {}
            }
        }
    }
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        view.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
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
                            view.setMyTurn();
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
                        view.showWinner(winnerId);
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
