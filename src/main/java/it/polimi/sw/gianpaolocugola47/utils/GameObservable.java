package it.polimi.sw.gianpaolocugola47.utils;

import it.polimi.sw.gianpaolocugola47.messages.Message;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

//classe astratta che definisce tutto ciò che un observable game dovrebbe permettere
public abstract class GameObservable {

    private final List<GameListener> listeners = new ArrayList<>();


    //metodo che permette di aggiungere un observer ad un game
    public void addObserver(GameListener gameListener){
        listeners.add(gameListener);
    }


    //metodo che notifica tutti gli osservatori con un messaggio
    public void notifyObservers(Message message) throws RemoteException {
        for(GameListener gameListener : listeners){
            gameListener.update(message);
        }
    }
}


