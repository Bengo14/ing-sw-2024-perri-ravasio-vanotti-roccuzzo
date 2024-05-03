package it.polimi.sw.gianpaolocugola47.utils;

import it.polimi.sw.gianpaolocugola47.messages.Message;

import java.rmi.RemoteException;

public interface GameListener {
    //questo metodo gestirà la ricezione dei messaggi dal server al clientSkeleton
    void update(Message m) throws RemoteException;
}
