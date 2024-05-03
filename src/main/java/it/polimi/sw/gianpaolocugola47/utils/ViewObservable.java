package it.polimi.sw.gianpaolocugola47.utils;

import it.polimi.sw.gianpaolocugola47.messages.Message;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class ViewObservable {

    private final List<ViewListener> listeners = new ArrayList<>();

    //aggiunge un observer alla view
    public void addObserver (ViewListener viewListener){
        listeners.add(viewListener);
    }


    //notifica tutti gli osservatori con un messaggio
    public void notify(Message m){
        for(ViewListener listener : listeners){
            try {
                listener.sendMessage(m);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
