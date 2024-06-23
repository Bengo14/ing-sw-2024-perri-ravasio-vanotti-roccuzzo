package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.*;

public class SocketServerProxy implements VirtualServer {

    private final ObjectOutputStream output;

    public SocketServerProxy(OutputStream output) throws IOException {
        this.output = new ObjectOutputStream(output);
    }

    public void pingAck() {
        SocketMessage message = new SocketMessage();
        message.addData("ping");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int connect(VirtualView client) {
        // not used here
        return 0;
    }

    @Override
    public void setNumOfPlayers(int num) {
        SocketMessage message = new SocketMessage();
        message.addData("numPlayers");
        message.addData(num);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPlayer(int id, String nickname) {
        SocketMessage message = new SocketMessage();
        message.addData("addPlayer");
        message.addData(id);
        message.addData(nickname);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StartingCard drawStartingCard() {
        SocketMessage message = new SocketMessage();
        message.addData("drawStarting");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        SocketMessage message = new SocketMessage();
        message.addData("setStarting");
        message.addData(card);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Objectives[0];
    }

    @Override
    public void setSecretObjective(int playerId, Objectives obj) {
        SocketMessage message = new SocketMessage();
        message.addData("setObj");
        message.addData(obj);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startGameFromFile() {
        SocketMessage message = new SocketMessage();
        message.addData("startFromFile");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) {
        SocketMessage message = new SocketMessage();
        message.addData("play");
        message.addData(onHandCard);
        message.addData(onTableCardX);
        message.addData(onTableCardY);
        message.addData(onTableCardCorner);
        message.addData(isFront);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void drawCard(int position, int playerId) {
        SocketMessage message = new SocketMessage();
        message.addData("draw");
        message.addData(position);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResourceCard[][] getCardsOnHand() {
        SocketMessage message = new SocketMessage();
        message.addData("getCardsOnHand");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResourceCard[0][];
    }

    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        SocketMessage message = new SocketMessage();
        message.addData("getPlacedCards");
        message.addData(playerId);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PlaceableCard[0][];
    }

    @Override
    public Objectives getSecretObjective(int playerId) {
        SocketMessage message = new SocketMessage();
        message.addData("getSecretObj");
        message.addData(playerId);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int[] getResourceCounter(int playerId) {
        SocketMessage message = new SocketMessage();
        message.addData("getResourceCounter");
        message.addData(playerId);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    @Override
    public void sendMessage(ChatMessage msg) {
        SocketMessage message = new SocketMessage();
        message.addData("message");
        message.addData(msg);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage msg) {
        SocketMessage message = new SocketMessage();
        message.addData("privateMessage");
        message.addData(msg);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNicknameAvailable(String nickname) {
        SocketMessage message = new SocketMessage();
        message.addData("nickAvailable");
        message.addData(nickname);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String[] getNicknames() {
        SocketMessage message = new SocketMessage();
        message.addData("getNick");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public int getNumOfPlayers() {
        SocketMessage message = new SocketMessage();
        message.addData("getNumPlayers");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean[][] getPlayablePositions(int playerId) {
        SocketMessage message = new SocketMessage();
        message.addData("getPlayPos");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new boolean[0][];
    }
}