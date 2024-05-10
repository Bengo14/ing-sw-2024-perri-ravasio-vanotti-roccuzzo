package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.observer.Observer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Observer {
    private final ServerSocket listenSocket;
    private final Controller controller;
    private final List<SocketClientHandler> clients;

    public SocketServer(ServerSocket listenSocket, Controller controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
        this.clients = new ArrayList<>();
    }

    public void run() throws IOException {

        Socket clientSocket;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            SocketClientHandler handler = new SocketClientHandler(this.controller, this, new BufferedReader(socketRx), new BufferedWriter(socketTx));

            clients.add(handler);
            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
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

    }

    @Override
    public void showWinner(int winnerId) {

    }

    @Override
    public void startGame() {

    }

    public static void initSocketServer(Controller controller) {

        String host = "localhost";
        int port = 8080;

        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress endpoint = new InetSocketAddress(host, port);
            serverSocket.bind(endpoint);
            System.out.println("Socket Server ready ---> IP: "+host+", Port: "+port);

            new SocketServer(serverSocket, controller).run();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}