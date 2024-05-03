package it.polimi.sw.gianpaolocugola47.socket;
import  it.polimi.sw.gianpaolocugola47.controller.AdderController;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    final ServerSocket listenSocket;
    final AdderController controller;
    final List<ClientHandler> clients = new ArrayList<>();

    public Server(ServerSocket listenSocket, AdderController controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    public void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandler handler = new ClientHandler(this.controller, this, new BufferedReader(socketRx), new BufferedWriter(socketTx));

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

    public void broadcastUpdate(Integer value) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showValue(value);
            }
        }
    }


    //    public static void main(String[] args) throws IOException {
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
//
//        ServerSocket listenSocket = new ServerSocket(port);
//
//        new Server(listenSocket, new AdderController());
//    }
//    public static void main(String[] args) {
//        if (args.length < 2) {
//            System.err.println("Usage: java Server <host> <port>");
//            System.exit(1);
//        }
//
//        int port;
//        try {
//            port = Integer.parseInt(args[1]);
//        } catch (NumberFormatException e) {
//            System.err.println("Error: Invalid port number.");
//            System.exit(1);
//            return;
//        }
//
//        try {
//            ServerSocket listenSocket = new ServerSocket(port);
//            Server server = new Server(listenSocket, new AdderController());
//            server.runServer();
//        } catch (IOException e) {
//            System.err.println("Error: Unable to start server: " + e.getMessage());
//            System.exit(1);
//        }
//    }
    public static void main(String[] args) {

        String host = "localhost";
        int port = 8080;

        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress endpoint = new InetSocketAddress(host, port);
            serverSocket.bind(endpoint);

            System.out.println("Server ready ---> IP: "+host+", Port: "+port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // gestisci la connessione con il client in un nuovo thread o in un executor
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}