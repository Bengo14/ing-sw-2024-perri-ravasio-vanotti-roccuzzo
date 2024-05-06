package it.polimi.sw.gianpaolocugola47.socket;


public interface VirtualSocketServer {
    public void connect(VirtualSocketView client);

    public void add(Integer number);

    public void reset();
}
