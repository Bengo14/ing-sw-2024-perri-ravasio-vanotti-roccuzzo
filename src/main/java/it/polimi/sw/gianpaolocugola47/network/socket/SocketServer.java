package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the server side of the socket connection.
 * It is responsible for accepting new clients and managing the connection with them.
 * It also implements the Observer interface to receive updates from the controller and send them to the clients.
 */
public class SocketServer implements Observer {

    public static final int SERVER_PORT = 12345;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private static SocketServer server;
    private final ServerSocket listenSocket;
    private final Controller controller;
    private final List<SocketClientHandler> clients;
    private volatile boolean terminated = true;

    /**
     * This method returns the instance of the SocketServer class.
     * @return the instance of the SocketServer class
     */
    public static SocketServer getServer() {
        if (SocketServer.server == null) {
            try {
                SocketServer.server = new SocketServer(new ServerSocket(), new Controller());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SocketServer.server;
    }

    /**
     * This constructor creates a new SocketServer instance.
     * @param listenSocket the server socket
     * @param controller the controller
     */
    public SocketServer(ServerSocket listenSocket, Controller controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
        this.controller.addModelObserver(this);
        this.clients = new ArrayList<>();
    }
    /**
     * This method starts the server and waits for new clients to connect.
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    private void run() throws IOException {

        pingStart();
        Socket clientSocket;

        while ((clientSocket = this.listenSocket.accept()) != null) { // accept() waits until a client joins

            SocketClientHandler handler = new SocketClientHandler(this.controller, this, clientSocket);
            connect(handler);
        }
    }
    /**
     * This method connects a new client to the server.
     * @param handler the client handler
     */
    private void connect(SocketClientHandler handler) {

        synchronized (this.clients) {
            synchronized (this.controller) {

                if (controller.getClientsConnected() == controller.getNumOfPlayers() || (controller.getNumOfPlayers() == -1 && controller.getClientsConnected() > 0)) {
                    System.err.println("Connection Refused");
                    handler.setId(-1);
                } else {

                    System.out.println("New client connected");
                    this.controller.addClientConnected();

                    new Thread(() -> {
                        try {
                            handler.runVirtualView();
                        } catch (IOException | ClassNotFoundException _) {}
                    }).start();

                    handler.setId(controller.getClientsConnected() - 1);
                    this.clients.add(handler);
                    this.terminated = false;
                }
            }
        }
    }
    /**
     * This method starts the ping mechanism.
     * It sends a ping message to all clients every 500ms and waits for 500ms for the response.
     */
    private void pingStart() {

        new Thread(()->{
            while(true) {
                while (terminated)
                    Thread.onSpinWait();

                synchronized (this.clients) {
                    for (SocketClientHandler handler : this.clients)
                        handler.ping();

                    try {
                        clients.wait(500);
                    } catch (InterruptedException _) {}

                    for (SocketClientHandler handler : this.clients)
                        if (!handler.getPingAck()) {
                            terminateGame(false, clients.indexOf(handler));
                            break;
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
     * It sends a terminate message to all clients and resets the game.
     */
    public void terminateGame() { // terminates all clients
        this.terminated = true;
        synchronized (this.clients) {
            for (SocketClientHandler handler : clients)
                handler.terminate();
            resetGame();
        }
    }
    /**
     * This method terminates the game.
     * It sends a terminate message to all clients except the one with the specified id and resets the game.
     * @param gameOver true if the game is ended, false if some client has disconnected
     * @param clientId the id of the client that has disconnected
     */
    private void terminateGame(boolean gameOver, int clientId) {
        System.err.println("terminating the game...");
        this.terminated = true;

        if(gameOver) { // the game is ended
            for (SocketClientHandler handler : clients)
                if (handler.getId() != clientId) // consider real id
                    handler.gameOver();
        } else { // some client has disconnected
            for (SocketClientHandler handler : clients)
                if (clients.indexOf(handler) != clientId) // consider local id
                    handler.terminate();
            new Thread(() -> RMIServer.getServer().terminateGame()).start();
        }
        resetGame();
    }
    /**
     * This method resets the game.
     * It clears the list of clients.
     */
    protected void resetGame() {
        clients.clear();
    }

    /* methods of interface Observer */
    /**
     * This method initializes the view of the clients.
     * @param nicknames the nicknames of the players
     * @param globalObjectives the global objectives
     * @param cardsOnHand the cards on hand of the players
     * @param cardsOnTable the cards on table
     */
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[][] cardsOnHand, ResourceCard[] cardsOnTable) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.initView(nicknames, globalObjectives, cardsOnHand[handler.getId()], cardsOnTable);
            }
        }
    }

    /**
     * This method updates the decks of the clients.
     * It sends the resource card on top of the deck, the gold card on top of the deck and the position of the card to draw.
     * @param resourceCardOnTop the resource card on top of the deck
     * @param goldCardOnTop the gold card on top of the deck
     * @param drawPos the position of the card to draw
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
            }
        }
    }
    /**
     * This method updates the points of the clients.
     * It sends the points of the board and the global points.
     * @param boardPoints the points of the board
     * @param globalPoints the global points
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.updatePoints(boardPoints, globalPoints);
            }
        }
    }

    /**
     * This method show the turn of the player with the specified id.
     * @param playerId the id of the player
     */
    @Override
    public void showTurn(int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    if(handler.getId() == playerId)
                        handler.setMyTurn();
            }
        }
    }

    /**
     * This method show the winner of the game.
     * It sends the id of the winner.
     * @param winnerId the id of the winner
     */
    @Override
    public void showWinner(int winnerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.showWinner(winnerId);
                terminateGame(true, winnerId);
            }
        }
    }

    /**
     * This method starts the game.
     * It sends a message to the clients to start the game.
     * It also sends the information if the game is loaded.
     */
    @Override
    public void startGame() {
        System.out.println("Game started");
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.startGame(this.controller.isGameLoaded()); //to be sync?
            }
        }
    }

    /**
     * This method sends a message to the clients.
     * @param message the message to send
     */
    public void sendMessage(ChatMessage message) {
        synchronized (this.clients) {
            for (SocketClientHandler handler : this.clients)
                handler.receiveMessage(message);
        }
        try {
            RMIServer.getServer().sendMessageFromSocket(message);
        } catch (RemoteException _) {}
    }

    /**
     * This method sends a message to the clients from an RMI client.
     * @param message the message to send
     */
    public void sendMessageFromRmi(ChatMessage message) {
        synchronized (this.clients) {
            for (SocketClientHandler handler : this.clients)
                handler.receiveMessage(message);
        }
    }
    /**
     * This method sends a private message to the clients.
     * @param message the message to send
     */
    public void sendPrivateMessage(ChatMessage message) {
        String [] nicknames = getNicknames();
        synchronized (this.clients) {
            for (SocketClientHandler handler : this.clients)
                if (nicknames[handler.getId()].equals(message.getReceiver()))
                    handler.receivePrivateMessage(message);
        }
        try {
            RMIServer.getServer().sendPrivateMessageFromSocket(message);
        } catch (RemoteException _) {}
    }
    /**
     * This method sends a private message to the clients from an RMI client.
     * @param message the message to send
     */
    public void sendPrivateMessageFromRmi(ChatMessage message) {
        String [] nicknames = getNicknames();
        synchronized (this.clients) {
            for (SocketClientHandler handler : this.clients)
                if (nicknames[handler.getId()].equals(message.getReceiver()))
                    handler.receivePrivateMessage(message);
        }
    }

    /**
     * This method returns the nicknames of the players.
     * @return the nicknames of the players
     */
    protected String[] getNicknames()  {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }

    /**
     * This method initializes the socket server.
     * @param controller the controller
     */
    public static void initSocketServer(Controller controller) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress endpoint = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
            serverSocket.bind(endpoint);
            System.out.println("Socket Server ready ---> IP: "+SERVER_ADDRESS+", Port: "+SERVER_PORT);

            SocketServer.server = new SocketServer(serverSocket, controller);
            SocketServer.server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}