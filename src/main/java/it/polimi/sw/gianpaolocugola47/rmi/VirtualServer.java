package it.polimi.sw.gianpaolocugola47.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    public int connect(VirtualView client) throws RemoteException;
    public void terminateGame(int deadClientId) throws RemoteException;
    public void setNumOfPlayers(int num) throws RemoteException;
    public void addPlayer(int id, String nickname) throws RemoteException;

    //todo metodi remoti server, invocati dal client
}
