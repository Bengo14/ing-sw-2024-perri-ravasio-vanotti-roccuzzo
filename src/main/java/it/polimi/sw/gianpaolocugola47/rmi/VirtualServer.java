package it.polimi.sw.gianpaolocugola47.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    public void connect(VirtualView client) throws RemoteException;

    public void reset() throws RemoteException;

    //todo metodi remoti server, invocati dal client
}
