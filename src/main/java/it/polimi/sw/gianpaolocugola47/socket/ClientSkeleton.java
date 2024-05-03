package it.polimi.sw.gianpaolocugola47.socket;

import it.polimi.sw.gianpaolocugola47.messages.Message;
import it.polimi.sw.gianpaolocugola47.messages.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

//gestisce la comunicazione tra client e server

public class ClientSkeleton implements Runnable{

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private Lobby lobby;
    private boolean inLobby;
    private Server server;
    private String nick = null;



    public String getNick() {
        return nick;
    }


    public void setNick(String nick) {
        this.nick = nick;
    }





    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
    }


  //costruttore
    public ClientSkeleton(Socket socket, Server server) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RemoteException("Can't create OutputStream: " + e.getMessage());
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Can't create InputStream: " + e.getMessage());
        }
        this.inLobby=false;
        this.server=server;
    }


    //metodo per inviare messaggi
    public void sendMessage(Message m) throws RemoteException {
        try {
            oos.writeObject(m);
            oos.reset();
            oos.flush();
            //server.Print(m,"LOBBY");
        } catch (IOException e) {
            throw new RemoteException("Can't send message: " + e.getMessage());
        }
    }


    //gestisce la comunicazione tra client e server durante il gioco
    public void run(){
        /*TODO*/
    }

    //aggiunge il client alla lobby
    public void addLobby(Lobby lobby){
        this.lobby = lobby;
        this.inLobby=true;
    }

}

