package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.rmi.RMIClient;

import java.io.IOException;

public class CLI {

    private final RMIClient client;

    public CLI(RMIClient client) {
        this.client = client;
    }
    public CLI(){this.client = null;}
    public void start() {
        for (int i = 0; i < 50; i++) System.out.println();
        System.out.flush();
        System.out.println("---- The game CODEX NATURALIS starts! ----\ncoming soon...");
        System.out.println("Chat service is on!\nType --listPlayers to see who your opponents are.\nStart a message with '@' to send a private message.");
        openChat();
        /*todo*/
    }

    private void openChat() {
        new Thread(() -> {
            while (true) {
                try {
                    client.inputLoop();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
