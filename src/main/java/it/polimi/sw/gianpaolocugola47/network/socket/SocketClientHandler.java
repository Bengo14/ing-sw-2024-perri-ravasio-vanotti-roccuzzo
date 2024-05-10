package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.rmi.RemoteException;

public class SocketClientHandler implements VirtualView {
    private final Controller controller;
    private final SocketServer socketServer;
    private final BufferedReader input;
    private final VirtualView view;

    public SocketClientHandler(Controller controller, SocketServer socketServer, BufferedReader input, BufferedWriter output) {
        this.controller = controller;
        this.socketServer = socketServer;
        this.input = input;
        this.view = new SocketClientProxy(output);
    }

    public void runVirtualView() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                //case "connect" -> {}
                //default -> System.err.println("[INVALID MESSAGE]");
            }
        }
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

    /*@Override
    public void showValue(Integer number) {
        synchronized (this.view) {
            this.view.showValue(number);
        }
    }*/

}

