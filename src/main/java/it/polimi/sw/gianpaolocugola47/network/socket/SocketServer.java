package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Observer {

    public static final int SERVER_PORT = 8080;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private static SocketServer server;
    private final ServerSocket listenSocket;
    private final Controller controller;
    private final List<SocketClientHandler> clients;
    private volatile boolean terminated = true;

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

    public SocketServer(ServerSocket listenSocket, Controller controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
        this.controller.addModelObserver(this);
        this.clients = new ArrayList<>();
    }

    private void run() throws IOException {

        pingStart();
        Socket clientSocket;

        while ((clientSocket = this.listenSocket.accept()) != null) { // accept() waits until a client joins

            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            SocketClientHandler handler = new SocketClientHandler(this.controller, this, new BufferedReader(socketRx), new BufferedWriter(socketTx));

            connect(handler);
        }
    }

    private void connect(SocketClientHandler handler) {

        synchronized (this.clients) {
            synchronized (this.controller) {

                if (controller.getClientsConnected() == controller.getNumOfPlayers() || (controller.getNumOfPlayers() == -1 && controller.getClientsConnected() > 0)) {
                    System.err.println("Connection Refused");
                    handler.setId(-1);
                } else {

                    System.out.println("New client connected");
                    this.terminated = false;
                    this.clients.add(handler);
                    this.controller.addClientConnected();
                    handler.setId(controller.getClientsConnected() - 1);

                    new Thread(() -> {
                        try {
                            handler.runVirtualView();
                        } catch (IOException _) {}
                    }).start();
                }
            }
        }
    }

    private void pingStart() {

        new Thread(()->{
            while(true) {
                while (terminated)
                    Thread.onSpinWait();

                synchronized (this.clients) {
                    for (SocketClientHandler handler : this.clients)
                        handler.ping();

                    try {
                        clients.wait(100);
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

    public void terminateGame() { // terminates all clients
        this.terminated = true;
        synchronized (this.clients) {
            for (SocketClientHandler handler : clients)
                handler.terminate();
            resetGame();
        }
    }

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

    protected void resetGame() {
        clients.clear();
    }

    /* methods of interface Observer */

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[][] cardsOnHand, ResourceCard[] cardsOnTable) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.initView(nicknames, globalObjectives, cardsOnHand[handler.getId()], cardsOnTable);
            }
        }
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.updateDecks(resourceCardOnTop, goldCardOnTop);
            }
        }
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.updatePoints(boardPoints, globalPoints);
            }
        }
    }

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

    @Override
    public void showWinner(int winnerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    if(handler.getId() == winnerId)
                        handler.showWinner();
                terminateGame(true, winnerId);
            }
        }
    }

    @Override
    public void startGame() {
        System.out.println("Game started");
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                for(SocketClientHandler handler : this.clients)
                    handler.startGame();
            }
        }
    }

    protected void sendMessage(ChatMessage message) {
        System.out.println("Received public message");
        synchronized (this.clients) {
            for (SocketClientHandler handler : this.clients)
                handler.receiveMessage(message);
        }
    }

    protected void sendPrivateMessage(ChatMessage message) {
        System.out.println("Received private message");
        String [] nicknames = getNicknames();
        synchronized (this.clients) {
            for (SocketClientHandler handler : this.clients)
                if (nicknames[handler.getId()].equals(message.getReceiver()))
                    handler.receivePrivateMessage(message);
        }
    }

    protected String[] getNicknames()  {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }

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