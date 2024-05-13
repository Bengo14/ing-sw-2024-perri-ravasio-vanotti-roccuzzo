package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.observer.Observer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Observer {

    public static final int SERVER_PORT = 8080;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private static SocketServer server;
    private final ServerSocket listenSocket;
    private final Controller controller;
    private final List<VirtualView> clients;
    private volatile boolean terminated = false;

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
    private void pingStart() {
        /*todo*/
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
                        } catch (IOException e) {
                            terminateGame(false, handler.getId()); // can it replace ping?
                        }
                    }).start();
                }
            }
        }
    }
    private void terminateGame(boolean gameOver, int clientId) {
        /*todo*/
    }
    protected void resetGame() {
        /*todo*/
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

    }

    @Override
    public void showWinner(int winnerId) {

    }

    @Override
    public void startGame() {

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