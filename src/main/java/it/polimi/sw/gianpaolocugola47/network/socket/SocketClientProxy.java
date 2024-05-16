package it.polimi.sw.gianpaolocugola47.network.socket;


import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class SocketClientProxy implements VirtualView {
    private final PrintWriter output;

    public SocketClientProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    public void setId(int id) {
        output.println("setId");
        output.println(id);
        output.flush();
    }

    @Override
    public void terminate() {
        output.println("terminate");
        output.flush();
    }
    @Override
    public void ping() {
        output.println("ping");
        output.flush();
    }
    @Override
    public void startGame() throws RemoteException {

    }
    @Override
    public void setMyTurn() throws RemoteException {

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

    @Override
    public void setNotMyTurn() throws RemoteException {

    }
}
