package it.polimi.sw.gianpaolocugola47.rmi.rmiChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientChatManager extends UnicastRemoteObject implements ChatClient {
    private ChatServer server;
    private int id;
    private String nickname;

    protected ClientChatManager(ServerChatManager server, int id, String nickname) throws RemoteException {
        this.server = server;
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    @Override
    public int getId() throws RemoteException {
        return this.id;
    }

    public void receiveMessage(ChatMessage message) throws RemoteException {
        System.out.println("Client received public message: " + message.getMessage() + " from player: " + message.getSender());
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {
        System.err.println("Client received private message: " + message.getMessage() + " from player: " + message.getSender());
    }

    public void startClient() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ServerChatManager.SERVER_ADDRESS, ServerChatManager.SERVER_PORT);
        this.server = (ChatServer) registry.lookup("RMIChat");
        this.server.login(this);
        inputLoop();
    }

    void inputLoop() throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        ChatMessage message = new ChatMessage(nickname, id);
        while(true){
            String line = br.readLine();
            if(line.equals("exit")){
                this.server.logout(this);
                System.exit(0);
            }
            if(line.startsWith("@")){
                message.setPrivate(true);
                message.setReceiver(line.substring(1, line.indexOf(" ")));
                message.setMessage(line.substring(line.indexOf(" ")+1));
                this.server.sendPrivateMessage(message);
            } else {
                message.setPrivate(false);
                message.setMessage(line);
                this.server.sendMessage(message);
            }
        }
    }

    public static void main(String[] args){
        try{
            ServerChatManager server = new ServerChatManager();
            new ClientChatManager(server,0, "asdrubale").startClient();
        } catch(Exception e){
            System.out.println("Server down! Try again later.");
            e.printStackTrace();
        }
    }
}
