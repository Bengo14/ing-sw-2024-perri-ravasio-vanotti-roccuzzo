package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.*;
import java.util.Arrays;

/**
 * This class is a proxy for the client, it is used to send messages to the client through the socket.
 */
public class SocketClientProxy implements VirtualView {

    private final ObjectOutputStream output;
    /**
     * Constructor for the SocketClientProxy class.
     * @param output the output stream to send messages to the client.
     * @throws IOException if an I/O error occurs.
     */
    public SocketClientProxy(OutputStream output) throws IOException {
        this.output = new ObjectOutputStream(output);
    }

    /**
     * This method is used to set the ID of the client. It creates a new SocketMessage,
     * adds the string "setId" and the provided ID to the message data, and then sends this
     * message to the client through the output stream.
     * @param id The ID to be set for the client.
     */
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

    /**
     * This method is used to send a message to the client. It creates a new SocketMessage,
     * adds the string "terminate" to the message data, and then sends
     * this message to the client through the output stream.
     */
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

    /**
     * This method is used to send a message to the client. It creates a new SocketMessage,
     * adds the string "ping" to the message data, and then sends this message to the client
     * through the output stream.
     * This is used to check if the client is still connected.
     */
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

    /**
     * This method is used to send a message to the client. It creates a new SocketMessage,
     * adds the string "startGame" and the provided isLoaded to the message data, and then sends
     * this message to the client through the output stream.
     * @param isLoaded A boolean value that indicates if the game is loaded.
     */
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

    /**
     * This method is used to send a message to the client. It creates a new SocketMessage,
     * adds the string "setMyTurn" to the message data, and then sends this message to the client
     * through the output stream.
     */
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

    /**
     * This method is used to send a message to the client. It creates a new SocketMessage,
     * adds the string "gameOver" to the message data, and then sends this
     * message to the client through the output stream.
     */
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

    /**
     * This method is used to send a message to the client. It creates a new SocketMessage,
     * adds the string "showWinner" and the provided id to the message data, and then sends
     * this message to the client through the output stream.
     * @param id The ID of the winner.
     */
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

    /**
     * This method is used to get the ID of the client.
     * @return -1, as the ID is not used here.
     */
    @Override
    public int getId() {
        return -1; // not used here
    }

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "message" and the provided msg to the message data, and then sends this
     * message to the client through the output stream.
     * @param msg The message to be sent to the client.
     */
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
    /**
     * This method is used to receive a private message from the client. It creates a new SocketMessage,
     * adds the string "privateMessage" and the provided msg to the message data, and then sends this
     * message to the client through the output stream.
     * @param msg The private message to be sent to the client.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "init" and the provided nicknames, globalObjectives, cardsOnHand, and cardsOnTable
     * to the message data, and then sends this message to the client through the output stream.
     * @param nicknames the nicknames of the players.
     * @param globalObjectives the global objectives of the game.
     * @param cardsOnHand the cards on the hand of the player.
     * @param cardsOnTable the cards on the table of the player.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "decks" and the provided resourceCardOnTop, goldCardOnTop, and drawPos
     * to the message data, and then sends this message to the client through the output stream.
     * @param resourceCardOnTop the resource card on top of the deck.
     * @param goldCardOnTop the gold card on top of the deck.
     * @param drawPos the position of the card to be drawn.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "points" and the provided boardPoints and globalPoints to the message data,
     * and then sends this message to the client through the output stream.
     * @param boardPoints the points of the player on the board.
     * @param globalPoints the global points of the player.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "drawStarting" and the provided card to the message data, and then sends
     * this message to the client through the output stream.
     * @param card the starting card to be drawn.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "setStarting" and the provided obj to the message data, and then sends
     * this message to the client through the output stream.
     * @param obj the objectives to be set.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * add the string "play" and the provided bool to the message data, and then sends this
     * message to the client through the output stream.
     * @param bool the boolean value to be set.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * add the string "getCardsOnHand" and the provided cardsOnHand to the message data, and then sends this
     * message to the client through the output stream.
     * @param cardsOnHand the cards on the hand of the player.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "getPlacedCards" and the provided placedCards to the message data, and then sends this
     * message to the client through the output stream.
     * @param placedCards the cards placed on the table.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "getResourceCounter" and the provided resourceCounter to the message data, and then sends this
     * message to the client through the output stream.
     * @param resourceCounter the resource counter of the player.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the sting "nickAvailable" and the provided bool to the message data, and then sends this
     * message to the client through the output stream.
     * @param bool the boolean value to be set.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "getNick" and the provided nicknames to the message data, and then sends this
     * message to the client through the output stream.
     * @param nicknames the nicknames of the players.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "getNumPlayers" and the provided numOfPlayers to the message data, and then sends this
     * message to the client through the output stream.
     * @param numOfPlayers the number of players in the game.
     */
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

    /**
     * This method is used to receive a message from the client. It creates a new SocketMessage,
     * adds the string "getPlayPos" and the provided playablePositions to the message data, and then sends this
     * message to the client through the output stream.
     * @param playablePositions the playable positions on the board.
     */
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