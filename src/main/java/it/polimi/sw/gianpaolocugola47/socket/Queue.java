package it.polimi.sw.gianpaolocugola47.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue { private final BlockingQueue<Integer> updateQueue = new LinkedBlockingQueue<>();
    private final List<VirtualView> exampleClientList = new ArrayList<>();

    public void addClient(VirtualView client) {
        synchronized (this.exampleClientList) {
            this.exampleClientList.add(client);
        }
    }

    public void queueUpdate(Integer value) throws InterruptedException {
        this.updateQueue.put(value);
    }

    public void startUpdateThread() {
        new Thread(this::runBroadcastLoop).start();
    }

    public void runBroadcastLoop() {
        while(true) {
            try {
                Integer update = this.updateQueue.take();
                synchronized (this.exampleClientList) {
                    for (var client : this.exampleClientList) {
                        client.showValue(update);
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("channel closed");
                break;
            }
        }
    }
}


