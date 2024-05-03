package it.polimi.sw.gianpaolocugola47.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server implements Runnable {
    private ServerSocket serverSocket;
    int port;

    private List<Lobby> lobbies = new ArrayList<>();
    private List<ClientSkeleton> queue = new ArrayList<>();


    public Server(int port) {
        this.port = port;
    }

    //inizializza e avvia il server
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server address: " + serverSocket.getInetAddress());
            System.out.println("\nSystem port: " + port);
        } catch (IOException ex) {
            System.out.println("Error initializing serverSocket.\n" + ex.getClass().getSimpleName() +
                    ": " + ex.getMessage());
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connection accepted from: " + socket.getInetAddress());
                ClientSkeleton clientSkeleton = new ClientSkeleton(socket, this);
                Thread thread = new Thread(clientSkeleton);
                thread.start();
                queue.add(clientSkeleton);
//                if (lobbies.size() == 0) {
//                    clientSkeleton.sendMessage(new MessageLobby("NO"));
//                } else {
//                    clientSkeleton.sendMessage(new MessageLobby("YES", lobbies));
//                }
            } catch (IOException e) {
                System.out.println(("Connection ended.\n" + e.getClass().getSimpleName() + ": " + e.getMessage()));
            }
        }
    }

    public void connectLobby(Message m, ClientSkeleton client) throws RemoteException {
    switch (m.getMessageType()) {
            /*TODO: implementare il caso in cui il client vuole connettersi ad una lobby*/
    }
    }



    public void quit(){
        /*TODO: implementare il metodo che RIMUOVE UN GIOCATORE DALLA LOBBY*/
    }



    public void Print() {
        /*TODO: implementare il metodo che STAMPA UN MESSAGGIO*/
    }


}