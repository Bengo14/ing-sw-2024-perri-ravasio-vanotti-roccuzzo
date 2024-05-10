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
    final PrintWriter output;

    public SocketClientProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
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

   /* public void showValue(Integer number) {
        output.println("update");
        output.println(number);
        output.flush();
    }*/
}
