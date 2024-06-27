package it.polimi.sw.gianpaolocugola47.network.rmi;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketServer;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the RMI server that manages the connection with the clients.
 * It also initializes the socket server.
 * It implements the VirtualServer interface and extends the UnicastRemoteObject class.
 * It also implements the Observer interface to receive updates from the model.
 */
@SuppressWarnings("ALL")
public class RMIServer extends UnicastRemoteObject implements VirtualServer, Observer {

    public static final int SERVER_PORT = 1234;
    private static RMIServer server;
    private final Controller controller;
    private final List<VirtualView> clients;
    private volatile boolean terminated = true;

    /**
     * This method returns the RMI server instance.
     * @return the RMI server instance
     */
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
    /**
     * This constructor creates a new RMI server with the given controller.
     * @param controller the controller of the game
     * @throws RemoteException if there is an error in the remote connection
     */
    private RMIServer(Controller controller) throws RemoteException {
        super(0);
        this.controller = controller;
        this.controller.addModelObserver(this);
        this.clients = new ArrayList<>();
        pingStart();
    }
    /**
     * This method starts the ping control thread.
     * It checks if the clients are still connected.
     * If a client is disconnected, it terminates the game.
     */
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
                   synchronized (this.clients) { //old code
                       try {
                           terminateGame(clients.indexOf(view)); // local id of dead client
                       } catch (RemoteException _) {}
                   }
               }
               try {
                   Thread.sleep(500);
               } catch (InterruptedException _) {}
           }
       }).start();
    }
     /**
      * This method terminates the game.
      */
    public void terminateGame() {
        this.terminated = true;
        synchronized (this.clients) {
            try {
                for (VirtualView view : clients)
                    if(view != null){
                        view.terminate();
                    }
            } catch (RemoteException ignored) {}
            resetGame();
        }
    }
    /**
     * This method terminates the game.
     * If the game is ended, it sends the game over message to the clients.
     * If some client has disconnected, it sends the terminate message to the clients.
     *
     * @param clientId the id of the client that has disconnected
     * @throws RemoteException if there is an error in the remote connection
     */
    private void terminateGame(int clientId) throws RemoteException {
        System.err.println("Terminating the game...");
        this.terminated = true;
        for (VirtualView view : clients)
            if (clients.indexOf(view) != clientId) // consider local id
                view.terminate();
        new Thread(() -> SocketServer.getServer().terminateGame()).start();
        resetGame();
    }
    /**
     * This method resets the game.
     * It resets the controller and the clients list.
     * It also adds the server as observer of the controller.
     * It is called when the game is terminated.
     */
    private void resetGame() {
        synchronized (this.controller) {
            controller.resetGame();
            controller.addModelObserver(this);
            controller.addModelObserver(SocketServer.getServer());
        }
        clients.clear();
    }

    /* --- methods of interface VirtualServer --- */

    /**
     * This method connects a client to the server.
     * If the connection is accepted, it adds the client to the clients list and increments the number of clients connected.ù
     * @param client the client to connect
     * @return the id of the client connected
     * @throws RemoteException if there is an error in the remote connection
     */
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

    /**
     * This method sets the number of players of the game.
     * It is called by the first client connected.
     * @param num the number of players
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        synchronized (this.controller) {
            this.controller.setNumOfPlayers(num);
        }
    }

    /**
     * This method adds a player to the game.
     * It is called by the clients connected.
     * It adds the player to the controller.
     * @param id the id of the player
     * @param nickname the nickname of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void addPlayer(int id, String nickname) throws RemoteException {
        synchronized (this.controller) {
            System.out.println(STR."Player \{nickname} added with id \{id}");
            this.controller.addPlayer(id, nickname);
        }
    }

    /**
     * This method draws a starting card.
     * It is called by the clients connected.
     * It draws a starting card from the controller.
     * @return the starting card drawn
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public StartingCard drawStartingCard() throws RemoteException {
        synchronized (controller) {
            return this.controller.drawStartingCard();
        }
    }

    /**
     * This method sets the starting card and draws the secret objectives.
     * It is called by the clients connected.
     * It sets the starting card and draws the secret objectives from the controller.
     * @param playerId the id of the player
     * @param card the starting card
     * @return the secret objectives drawn
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) throws RemoteException {
        synchronized (controller) {
            return this.controller.setStartingCardAndDrawObjectives(playerId, card);
        }
    }
    /**
     * This method sets the secret objective of a player.
     * It is called by the clients connected.
     * It sets the secret objective of a player from the controller.
     * @param playerId the id of the player
     * @param obj the secret objective
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void setSecretObjective(int playerId, Objectives obj) throws RemoteException {
        synchronized (controller) {
            this.controller.setSecretObjectiveAndUpdateView(playerId, obj);
        }
    }
    /**
     * This method starts the game from a file loaded.
     * It is called by the clients connected.
     * It starts the game from the controller.
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void startGameFromFile() throws RemoteException {
        synchronized (controller) {
            this.controller.startGameFromFile();
        }
    }

    /**
     * This method returns the playable positions of a player.
     * @param playerId the id of the player
     * @return the playable positions of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public boolean[][] getPlayablePositions(int playerId) throws RemoteException {
        synchronized (controller) {
            return this.controller.getPlayablePositions(playerId);
        }
    }

    @Override
    public ArrayList<int[]> getPlacingOrder(int id) throws RemoteException {
        synchronized (controller) {
            return this.controller.getPlacingOrder(id);
        }
    }

    /**
     * This method plays a card.
     * It is called by the clients connected.
     * It plays a card from the controller.
     * @param onHandCard the position of the card on hand
     * @param onTableCardX the x position of the card on the table
     * @param onTableCardY the y position of the card on the table
     * @param onTableCardCorner the corner of the card on the table
     * @param playerId the id of the player
     * @param isFront true if the card is played on the front side or false if it is played on the back side
     * @return true if the card is played, false otherwise
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) throws RemoteException {
        synchronized (controller) {
            return controller.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId, isFront);
        }
    }
    /**
     * This method draws a card.
     * It is called by the clients connected.
     * It draws a card from the controller.
     * @param position the position of the card to draw
     * @param playerId the id of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void drawCard(int position, int playerId) throws RemoteException {
        synchronized (controller) {
            controller.drawCard(position, playerId);
        }
    }
    /**
     * This method return the cards on hand of a player.
     * It is called by the clients connected.
     * It returns the cards on hand of a player from the controller.
     * @return the cards on hand of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public ResourceCard[][] getCardsOnHand() throws RemoteException {
        synchronized (controller) {
            return controller.getCardsOnHand();
        }
    }
    /**
     * This method return the cards on table of a player.
     * It is called by the clients connected.
     * It returns the cards on table of a player from the controller.
     * @param playerId the id of the player
     * @return the cards on table of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) throws RemoteException {
        synchronized (controller) {
            return controller.getPlacedCards(playerId);
        }
    }
    /**
     * This method return the secret objective of a player.
     * It is called by the clients connected.
     * It returns the secret objective of a player from the controller.
     * @param playerId the id of the player
     * @return the secret objective of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public Objectives getSecretObjective(int playerId) throws RemoteException {
        synchronized (controller) {
            return controller.getSecretObjective(playerId);
        }
    }

    /**
     * This method return the resource counter of a player.
     * It is called by the clients connected.
     * It returns the resource counter of a player from the controller.
     * @param playerId the id of the player
     * @return the resource counter of the player
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public int[] getResourceCounter(int playerId) throws RemoteException {
        synchronized (controller) {
            return controller.getResourceCounter(playerId);
        }
    }

    /**
     * This method sends a message to the client from another client.
     * It is called by the clients connected.
     * @param message the message to send
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                client.receiveMessage(message);
        }
        SocketServer.getServer().sendMessageFromRmi(message);
    }
    /**
     * This method sends a message to the client from a client with socket.
     * It is called by the clients connected.
     * @param message the message to send
     * @throws RemoteException if there is an error in the remote connection
     */
    public void sendMessageFromSocket(ChatMessage message) throws RemoteException {
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                client.receiveMessage(message);
        }
    }
    /**
     * This method sends a private message to the client from another client.
     * It is called by the clients connected.
     * @param message the message to send
     * @throws RemoteException if there is an error in the remote connection
     */
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
    /**
     * This method sends a private message to the client from a client with socket.
     * It is called by the clients connected.
     * @param message the message to send
     * @throws RemoteException if there is an error in the remote connection
     */
    public void sendPrivateMessageFromSocket(ChatMessage message) throws RemoteException {
        String [] nicknames = getNicknames();
        synchronized (this.clients) {
            for (VirtualView client : this.clients)
                if (nicknames[client.getId()].equals(message.getReceiver()))
                    client.receivePrivateMessage(message);
        }
    }
    /**
     * This method checks if the nickname is available.
     * It is called by the clients connected.
     * @param nickname the nickname to check
     * @return true if the nickname is available, false otherwise
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public boolean isNicknameAvailable(String nickname) throws RemoteException {
        String [] nicknames = getNicknames();
        for (String nick : nicknames)
            if (nick.equals(nickname))
                return false;
        return true;
    }
    /**
     * This method returns the nicknames of the clients connected.
     * It is called by the clients connected.
     * It returns the nicknames of the clients connected from the controller.
     * @return the nicknames of the clients connected
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public String[] getNicknames() throws RemoteException {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }
    /**
     * This method returns the number of players connected.
     * It is called by the clients connected.
     * It returns the number of players connected from the controller.
     * @return the number of players connected
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public int getNumOfPlayers() throws RemoteException {
        synchronized (controller) {
            return this.controller.getNumOfPlayers();
        }
    }

    /* --- methods of interface Observer --- */
    /**
     * This method starts the game.
     * It is called by the controller.
     * It starts the game for the clients connected.
     * @throws RemoteException if there is an error in the remote connection
     */
    @Override
    public void startGame() throws RemoteException {
        System.out.println("Game started");
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(VirtualView view : this.clients) {
                    try {
                        view.startGame(this.controller.isGameLoaded()); //to be sync?
                        System.out.println("Game started for client: " + view.getId());
                    } catch (RemoteException e) {
                        System.err.println("Failed to start game for client: " + view.getId());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method initializes the view.
     * It is called by the controller.
     * It initializes the view for the clients connected.
     * @param nicknames the nicknames of the clients connected
     * @param globalObjectives the global objectives of the game
     * @param cardsOnHand the cards on hand of the clients connected
     * @param cardsOnTable the cards on table of the clients connected
     */
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

    /**
     * This method updates the decks.
     * It is called by the controller.
     * It updates the decks for the clients connected.
     * @param resourceCardOnTop the resource card on top of the deck
     * @param goldCardOnTop the gold card on top of the deck
     * @param drawPos the position of the card to draw
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        if(view != null){
                            view.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
                        }
                } catch (RemoteException ignored) {}
            }
        }
    }

    /**
     * This method updates the points.
     * It is called by the controller.
     * It updates the points for the clients connected.
     * @param boardPoints the points of the board
     * @param globalPoints the global points
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        if(view != null){
                            view.updatePoints(boardPoints, globalPoints);
                        }
                } catch (RemoteException ignored) {}
            }
        }
    }

    /**
     * This method shows the turn of a player.
     * It is called by the controller.
     * It shows the turn of a player for the clients connected.
     * @param playerId the id of the player
     */
    @Override
    public void showTurn(int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        if(view != null) {
                            if(view.getId() == playerId)
                                view.setMyTurn();
                        }
                } catch (RemoteException ignored) {}
            }
        }
    }

    /**
     * This method shows the winner of the game.
     * It is called by the controller.
     * It shows the winner of the game for the clients connected.
     * @param winnerId the id of the winner
     */
    @Override
    public void showWinner(int winnerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : this.clients)
                        view.showWinner(winnerId);
                } catch (RemoteException ignored) {}
            }
        }
    }

    /**
     * This method is the main method of the RMI server.
     * It creates a new RMI server with the given controller and binds it to the registry.
     * It also initializes the socket server.
     * It is called when the server is started.
     */
    public static void startServer() {
        if(RMIServer.server == null) {
            String name = "VirtualServer";
            Controller controller = new Controller();
            String ip = "";
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
                System.setProperty("java.rmi.server.hostname", ip);
                System.setProperty("sun.rmi.transport.tcp.responseTimeout", "5000");
                System.setProperty("sun.rmi.transport.tcp.requestTimeout", "5000");
                System.setProperty("sun.rmi.transport.connectionTimeout", "5000");
                RMIServer.server = new RMIServer(controller);
                Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
                registry.rebind(name, RMIServer.server);
            } catch (RemoteException | UnknownHostException e) {
                e.printStackTrace();
            }
            System.err.println("Server ready ---> IP: " + ip + ", Port: " + SERVER_PORT);
            SocketServer.initSocketServer(controller);
        }
    }
}