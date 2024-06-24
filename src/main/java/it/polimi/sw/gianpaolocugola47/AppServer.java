package it.polimi.sw.gianpaolocugola47;

import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
/**
 * This class is the launcher of the server side of the application
 */
public class AppServer {
    /**
     * This method is the main server method, it starts the server
     */
    public static void main(String[] args) {
        RMIServer.startServer();
    }
}
