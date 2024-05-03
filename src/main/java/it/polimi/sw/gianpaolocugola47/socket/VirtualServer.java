package it.polimi.sw.gianpaolocugola47.socket;


public interface VirtualServer {
    public void connect(VirtualView client);

    public void add(Integer number);

    public void reset();
}
