package it.polimi.sw.gianpaolocugola47.socket;
//app che lancia il server
public class AppServer {

    //lancia il server
    public static void main(String[] args) {
        Server server = new Server(12345);
        server.run();
    }
}
