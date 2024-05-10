package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClient implements VirtualView {
    final BufferedReader input;
    final SocketServerProxy server;

    protected SocketClient(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.server = new SocketServerProxy(output);
    }

    public void run() throws RemoteException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    private void runVirtualServer() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                /*todo*/
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli() throws RemoteException {
        /*todo*/
    }

    @Override
    public void terminate() throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void startGame() throws RemoteException {

    }

    @Override
    public void showTurn() throws RemoteException {

    }

    @Override
    public void gameOver() throws RemoteException {

    }

    @Override
    public void showWinner() throws RemoteException {

    }

    @Override
    public String getNickname() throws RemoteException {
        return null;
    }

    @Override
    public int getId() throws RemoteException {
        return 0;
    }

    @Override
    public void receiveMessage(ChatMessage message) throws RemoteException {

    }

    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {

    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws RemoteException {

    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) throws RemoteException {

    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) throws RemoteException {

    }
    public static void main(String[] args) {

        int port= 8080;
        String host = "localhost";

        try {
            Socket serverSocket = new Socket(host, port);
            InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

            new SocketClient(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
        } catch (IOException e) {
            System.err.println("Error: Unable to connect to server: " + e.getMessage());
            System.exit(1);
        }
    }

}
