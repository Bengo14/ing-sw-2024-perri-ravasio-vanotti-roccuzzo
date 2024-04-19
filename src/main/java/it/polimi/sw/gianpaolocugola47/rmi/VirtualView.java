package it.polimi.sw.gianpaolocugola47.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {
    /*todo metodi remoti della view, invocati dal server*/

    public void terminate() throws RemoteException;
    public void ping() throws RemoteException;
    public void startGame() throws RemoteException;

}
