package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.rmi.RMIClient;

import java.io.IOException;

public class CLI {

    private final RMIClient client;

    public CLI(RMIClient client){
        this.client = client;
    }
    public void start() {
        for (int i = 0; i < 50; i++) System.out.println();
        System.out.flush();
        System.out.println("---- The game CODEX NATURALIS starts! ----\ncoming soon...");
        /*todo*/
    }
}
