package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * This class represents the handler of a client connected to the server via socket.
 * It implements both the VirtualView and VirtualServer interfaces, so it can receive messages from the server and send messages to the client.
 * It also has a reference to the controller and the socket server, so it can interact with them.
 */
public class SocketClientHandler implements VirtualView, VirtualServer {
    private final Controller controller;
    private final SocketServer socketServer;
    protected final SocketClientProxy client;
    private final Socket socket;
    protected int id;
    protected boolean pingAck = true;

    /**
     * Constructor of the class.
     * @param controller the controller of the game
     * @param socketServer the socket server
     * @param socket the socket of the client
     * @throws IOException if an I/O error occurs when creating the output stream
     */
    public SocketClientHandler(Controller controller, SocketServer socketServer, Socket socket) throws IOException {
        this.controller = controller;
        this.socketServer = socketServer;
        this.socket = socket;
        this.client = new SocketClientProxy(socket.getOutputStream());
    }

    /**
     * This method is called when the client is connected to the server.
     * It reads the messages sent by the client and calls the corresponding methods.
     * @throws IOException if an I/O error occurs when creating the input stream
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    public void runVirtualView() throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        while (true) {
            SocketMessage message = (SocketMessage) ois.readObject();
            message.doAction(this);
        }
    }

    /**
     * This method sets the id of the client.
     * @param id the id of the client
     */
    protected void setId(int id) {
        this.id = id;
        this.client.setId(id);
    }

    /**
     * This method returns the pingAck variable.
     * @return the value of the pingAck variable
     */
    protected synchronized boolean getPingAck() {
         boolean ping = pingAck;
         pingAck = false;
         return ping;
    }
    /**
     * This method sets the pingAck variable to true.
     */
    protected synchronized void setPingAck() {
        pingAck = true;
    }

    /* methods of interface VirtualView */

    /**
     * This method terminates the client.
     * It is synchronized to avoid conflicts between threads.
     */
    @Override
    public void terminate() {
        synchronized (client) {
            this.client.terminate();
        }
    }
    /**
     * This method sends a ping message to the client.
     * It is synchronized to avoid conflicts between threads.
     */
    @Override
    public void ping() {
        synchronized (client) {
            this.client.ping();
        }
    }

    /**
     * This method starts the game.
     * It is synchronized to avoid conflicts between threads.
     * @param isLoaded true if the game is loaded, false otherwise
     */
    @Override
    public void startGame(boolean isLoaded) {
        synchronized (client) {
            this.client.startGame(isLoaded);
        }
    }
    /**
     * This method sets the turn of the client.
     * It is synchronized to avoid conflicts between threads.
     */
    @Override
    public void setMyTurn() {
        synchronized (client) {
            this.client.setMyTurn();
        }
    }
    /**
     * This method calls the game over method of the client.
     * It is synchronized to avoid conflicts between threads.
     */
    @Override
    public void gameOver() {
        synchronized (client) {
            this.client.gameOver();
        }
    }
    /**
     * This method calls the show winner method of the client.
     * It is synchronized to avoid conflicts between threads.
     * @param id the id of the winner
     */
    @Override
    public void showWinner(int id) {
        synchronized (client) {
            this.client.showWinner(id);
        }
    }

    /**
     * This method returns the id of the client.
     * @return the id of the client
     */
    @Override
    public int getId() {
        return this.id; // id saved in local so socket communication is not needed
    }
    /**
     * This method calls the receiveMessage method of the client.
     * It is synchronized to avoid conflicts between threads.
     * @param message the message to receive
     */
    @Override
    public void receiveMessage(ChatMessage message) {
        synchronized (client) {
            client.receiveMessage(message);
        }
    }
    /**
     * This method calls the receivePrivateMessage method of the client.
     * It is synchronized to avoid conflicts between threads.
     * @param message the private message to receive
     */
    @Override
    public void receivePrivateMessage(ChatMessage message) {
        synchronized (client) {
            client.receivePrivateMessage(message);
        }
    }

    /**
     * This method calls the initView method of the client.
     * It is synchronized to avoid conflicts between threads.
     * @param nicknames the nicknames of the players
     * @param globalObjectives the global objectives of the game
     * @param cardsOnHand the cards on hand of the player
     * @param cardsOnTable the cards on table of the player
     */
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        synchronized (client) {
            client.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
        }
    }

    /**
     * This method calls the updateDecks method of the client.
     * It is synchronized to avoid conflicts between threads.
     * @param resourceCardOnTop the resource card on top of the deck
     * @param goldCardOnTop the gold card on top of the deck
     * @param drawPos the position of the draw
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        synchronized (client) {
            client.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
        }
    }

    /**
     * This method calls the updatePoints method of the client.
     * It is synchronized to avoid conflicts between threads.
     * @param boardPoints the points of the board
     * @param globalPoints the global points of the player
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints)  {
        synchronized (client) {
            client.updatePoints(boardPoints, globalPoints);
        }
    }

    /* methods of interface VirtualServer */

    /**
     * This method connects the client to the server.
     * @param client the client to connect
     * @return -1
     * @throws RemoteException if the connection fails
     */
    @Override
    public int connect(VirtualView client) throws RemoteException {
        return -1; // not used here
    }

    /**
     * This method calls the setNumOfPlayers method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param num the number of players
     */
    @Override
    public void setNumOfPlayers(int num) {
        synchronized (controller) {
            this.controller.setNumOfPlayers(num);
        }
    }
    /**
     * This method calls the addPlayer method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param id the id of the player
     * @param nickname the nickname of the player
     */
    @Override
    public void addPlayer(int id, String nickname) {
        synchronized (controller) {
            System.out.println(STR."Player \{nickname} added with id \{id}");
            this.controller.addPlayer(id, nickname);
        }
    }

    /**
     * This method calls the drawStartingCard method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @return the starting card drawn
     */
    @Override
    public StartingCard drawStartingCard() {
        synchronized (controller) {
            return this.controller.drawStartingCard();
        }
    }

    /**
     * This method calls the setStartingCardAndDrawObjectives method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param playerId the id of the player
     * @param card the starting card of the player
     * @return the objectives drawn
     */
    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        synchronized (controller) {
            return this.controller.setStartingCardAndDrawObjectives(playerId, card);
        }
    }
    /**
     * This method calls the setSecretObjectiveAndUpdateView method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param playerId the id of the player
     * @param obj the secret objective of the player
     */
    @Override
    public void setSecretObjective(int playerId, Objectives obj) {
        synchronized (controller) {
            this.controller.setSecretObjectiveAndUpdateView(playerId, obj);
        }
    }

    /**
     * This method calls the startGameFromFile method of the controller.
     * It is synchronized to avoid conflicts between threads.
     */
    @Override
    public void startGameFromFile() {
        synchronized (controller) {
            this.controller.startGameFromFile();
        }
    }

    /**
     * This method calls the getPlayablePositions method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param playerId the id of the player
     * @return the playable positions of the player
     */
    @Override
    public boolean[][] getPlayablePositions(int playerId) {
        synchronized (controller) {
            return this.controller.getPlayablePositions(playerId);
        }
    }

    /**
     * This method calls the playCard method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param onHandCard the position of the card on hand
     * @param onTableCardX the x position of the card on table
     * @param onTableCardY the y position of the card on table
     * @param onTableCardCorner the corner of the card on table
     * @param playerId the id of the player
     * @param isFront true if the card is played on the front, false otherwise
     * @return true if the card is played, false otherwise
     */
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) {
        synchronized (controller) {
            return controller.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId, isFront);
        }
    }

    /**
     * This method calls the drawCard method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param position the position of the card
     * @param playerId the id of the player
     */
    @Override
    public void drawCard(int position, int playerId) {
        synchronized (controller) {
            controller.drawCard(position, playerId);
        }
    }

    /**
     * This method calls the getCardsOnHand method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @return the cards on hand of the player
     */
    @Override
    public ResourceCard[][] getCardsOnHand() {
        synchronized (controller) {
            return controller.getCardsOnHand();
        }
    }

    /**
     * This method calls the getPlacedCards method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param playerId the id of the player
     * @return the placed cards of the player
     */
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        synchronized (controller) {
            return controller.getPlacedCards(playerId);
        }
    }

    /**
     * This method calls the getSecretObjective method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param playerId the id of the player
     * @return the secret objective of the player
     */
    @Override
    public Objectives getSecretObjective(int playerId) {
        synchronized (controller) {
            return controller.getSecretObjective(playerId);
        }
    }

    /**
     * This method calls the getResourceCounter method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @param playerId the id of the player
     * @return the resource counter of the player
     */
    @Override
    public int[] getResourceCounter(int playerId)  {
        synchronized (controller) {
            return controller.getResourceCounter(playerId);
        }
    }

    /**
     * This method calls the sendResource method of the socket server.
     * It is synchronized to avoid conflicts between threads.
     * @param message the message to send
     */
    @Override
    public void sendMessage(ChatMessage message)  {
        synchronized (this.socketServer) {
            this.socketServer.sendMessage(message);
        }
    }
    /**
     * This method calls the sendPrivateMessage method of the socket server.
     * It is synchronized to avoid conflicts between threads.
     * @param message the private message to send
     */
    @Override
    public void sendPrivateMessage(ChatMessage message)  {
        synchronized (this.socketServer) {
            this.socketServer.sendPrivateMessage(message);
        }
    }

    /**
     * This method verifies if the nickname is available.
     * @param nickname the nickname to verify
     * @return true if the nickname is available, false otherwise
     */
    @Override
    public boolean isNicknameAvailable(String nickname)  {
        String [] nicknames = getNicknames();
        for (String nick : nicknames)
            if (nick.equals(nickname))
                return false;
        return true;
    }
    /**
     * This method calls the getNicknames method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @return the nicknames of the players
     */
    @Override
    public String[] getNicknames()  {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }
    /**
     * This method calls the getNumOfPlayers method of the controller.
     * It is synchronized to avoid conflicts between threads.
     * @return the number of players
     */
    @Override
    public int getNumOfPlayers() {
        synchronized (controller) {
            return this.controller.getNumOfPlayers();
        }
    }
}