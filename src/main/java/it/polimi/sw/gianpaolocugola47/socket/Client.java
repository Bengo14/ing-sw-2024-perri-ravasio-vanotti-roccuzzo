package it.polimi.sw.gianpaolocugola47.socket;

import it.polimi.sw.gianpaolocugola47.messages.Message;
import it.polimi.sw.gianpaolocugola47.utils.ViewListener;
import it.polimi.sw.gianpaolocugola47.view.handler;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client extends ViewListener {
    private final handler viewH;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    private int port;
    private String ip;


    public Client(handler viewH) {
        this.viewH = viewH;
    }
    //verifica se il client è connesso
    public boolean register (int port,String ip){
        this.port=port;
        this.ip=ip;
        try {
            this.socket = new Socket(this.ip, this.port);
        } catch (Exception e) {
            viewH.wrongData();
            return false;
        }

        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            viewH.wrongData();
            return false;
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            viewH.wrongData();
            return false;

        }
        return true;
    }


    public void sendMessage(Message message) throws RemoteException {
       /*TODO:implementare l'invio di messaggi al server*/
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            throw new RemoteException("Can't send message to server: " + e.getMessage());
        }
    }


    public void receive() throws RemoteException {
       /*TODO:implementare la ricezione di messaggi dal server*/
        try {
            Message mes = (Message) ois.readObject();
            //viewH.update(mes);non capisco il problema
        } catch (IOException | ClassNotFoundException e) {
            throw new RemoteException("Can't receive message from server: " + e.getMessage());
        }
    }


    //permette di avviare il client
    public void start(int port, String ip) {
        boolean valid = register(port,ip);
        if (valid) {

            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            receive();
                        } catch (RemoteException e) {
                            System.err.println("Error in thread while receiving message: " + e.getMessage());
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                System.err.println("Error in thread while closing connection: " + ex.getMessage());
                            }
                            System.exit(1);
                        }
                    }
                }

            }.start();
            viewH.go();}
    }


    //inizializza la connessione
    public void run(){
        viewH.askServer();
    }
}

