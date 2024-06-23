package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.*;
import java.util.Arrays;

public class SocketClientProxy implements VirtualView {

    private final ObjectOutputStream output;

    public SocketClientProxy(OutputStream output) throws IOException {
        this.output = new ObjectOutputStream(output);
    }

    protected void setId(int id) {
        SocketMessage message = new SocketMessage();
        message.addData("setId");
        message.addData(id);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminate() {
        SocketMessage message = new SocketMessage();
        message.addData("terminate");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ping() {
        SocketMessage message = new SocketMessage();
        message.addData("ping");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startGame(boolean isLoaded) {
        SocketMessage message = new SocketMessage();
        message.addData("start");
        message.addData(isLoaded);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMyTurn() {
        SocketMessage message = new SocketMessage();
        message.addData("turn");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameOver() {
        SocketMessage message = new SocketMessage();
        message.addData("gameOver");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showWinner(int id) {
        SocketMessage message = new SocketMessage();
        message.addData("winner");
        message.addData(id);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getId() {
        return -1; // not used here
    }

    @Override
    public void receiveMessage(ChatMessage msg) {
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
    public void receivePrivateMessage(ChatMessage msg) {
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
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        SocketMessage message = new SocketMessage();
        message.addData("init");
        message.addData(nicknames);
        message.addData(globalObjectives);
        message.addData(cardsOnHand);
        message.addData(cardsOnTable);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        SocketMessage message = new SocketMessage();
        message.addData("decks");
        message.addData(resourceCardOnTop);
        message.addData(goldCardOnTop);
        message.addData(drawPos);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        SocketMessage message = new SocketMessage();
        message.addData("points");
        message.addData(boardPoints);
        message.addData(globalPoints);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void drawStartingCardResponse(StartingCard card) {
        SocketMessage message = new SocketMessage();
        message.addData("drawStarting");
        message.addData(card);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setStartingCardAndDrawObjectivesResponse(Objectives[] obj) {
        SocketMessage message = new SocketMessage();
        message.addData("setStarting");
        message.addData(obj);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void playCardResponse(boolean bool) {
        SocketMessage message = new SocketMessage();
        message.addData("play");
        message.addData(bool);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getCardsOnHandResponse(ResourceCard[][] cardsOnHand) {
        SocketMessage message = new SocketMessage();
        message.addData("getCardsOnHand");
        message.addData(cardsOnHand);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getPlacedCardsResponse(PlaceableCard[][] placedCards) {
        SocketMessage message = new SocketMessage();
        message.addData("getPlacedCards");
        message.addData(placedCards);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getResourceCounterResponse(int[] resourceCounter) {
        SocketMessage message = new SocketMessage();
        message.addData("getResourceCounter");
        message.addData(resourceCounter);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void isNicknameAvailableResponse(boolean bool) {
        SocketMessage message = new SocketMessage();
        message.addData("nickAvailable");
        message.addData(bool);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getNicknamesResponse(String[] nicknames) {
        SocketMessage message = new SocketMessage();
        message.addData("getNick");
        message.addData(nicknames);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getNumOfPlayersResponse(int numOfPlayers) {
        SocketMessage message = new SocketMessage();
        message.addData("getNumPlayers");
        message.addData(numOfPlayers);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getPlayablePositionsResponse(boolean[][] playablePositions) {
        SocketMessage message = new SocketMessage();
        message.addData("getPlayPos");
        message.addData(playablePositions);
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}