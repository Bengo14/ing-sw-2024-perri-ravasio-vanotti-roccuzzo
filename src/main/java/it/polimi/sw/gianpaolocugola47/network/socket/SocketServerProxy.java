package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.*;

/**
 * This class is a proxy for the server, it is used to send messages to the client
 * through the socket connection.
 */
@SuppressWarnings("ALL")
public class SocketServerProxy implements VirtualServer {

    private final ObjectOutputStream output;
    /**
     * Constructor for the class, it takes an output stream as parameter.
     * @param output the output stream to write the messages to.
     * @throws IOException if an I/O error occurs.
     */
    public SocketServerProxy(OutputStream output) throws IOException {
        this.output = new ObjectOutputStream(output);
    }

    /**
     * This method is used to acknowledge a ping from the client. It creates a new SocketMessage,
     * adds the string "ping" to the message data, and then sends this message to the client through the output stream.
     */
    public void pingAck() {
        SocketMessage message = new SocketMessage();
        message.addData("ping");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is intended to establish a connection with a client. However, in the current implementation,
     * it is not used and simply returns 0.
     *
     * @param client The client to connect to.
     * @return Always returns 0 as the method is not implemented.
     */
    @Override
    public int connect(VirtualView client) {
        // not used here
        return 0;
    }

    /**
     * This method sets the number of players. It creates a new SocketMessage,
     * adds the string "numPlayers" and the provided number of players to the message data,
     * and then sends this message to the client through the output stream.
     * @param num The number of players to be set.
     */
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
    /**
     * This method is used to add a player to the game. It creates a new SocketMessage,
     * adds the string "addPlayer", the player's id and nickname to the message data,
     * and then sends this message to the client through the output stream.
     * @param id The id of the player to be added.
     * @param nickname The nickname of the player to be added.
     */
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

    /**
     * This method is used to draw a starting card. It creates a new SocketMessage,
     * adds the string "drawStarting" to the message data,
     * and then sends this message to the client through the output stream.
     * @return Always returns null.
     */
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
    /**
     * This method is used to draw an objective card. It creates a new SocketMessage,
     * adds the string "setStarting" the playerId and the startingCard to the message data,
     * then sends this message to the client through the output stream.
     * @return Always returns an empty array of Objectives.
     */
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
    /**
     * This method is used to set a secret objective. It creates a new SocketMessage,
     * adds the string "setObj" the playerId and the objective to the message data,
     * then sends this message to the client through the output stream.
     * @param playerId The id of the player to set the objective for.
     * @param obj The objective to set.
     */
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
    /**
     * This method is used to start the game from file. It creates a new SocketMessage,
     * adds the string "startFromFile" to the message data,
     * then sends this message to the client through the output stream.
     */
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

    /**
     * This method is used to play a card. It creates a new SocketMessage,
     * adds the string "play", the onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId and isFront to the message data,
     * then sends this message to the client through the output stream.
     * @param onHandCard the card to play.
     * @param onTableCardX the x coordinate of the card on the table.
     * @param onTableCardY the y coordinate of the card on the table.
     * @param onTableCardCorner the corner of the card on the table.
     * @param playerId the id of the player playing the card.
     * @param isFront whether the card is played front or back.
     * @return Always returns false.
     */
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
    /**
     * This method is used to draw a card. It creates a new SocketMessage,
     * adds the string "draw", the position and playerId to the message data,
     * then sends this message to the client through the output stream.
     * @param position the position to draw the card from.
     * @param playerId the id of the player drawing the card.
     */
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

    /***
     * This method is used to get the cards on hand. It creates a new SocketMessage,
     * adds the string "getCardsOnHand" to the message data,
     * then sends this message to the client through the output stream.
     * @return Always returns the first element of an array of ResourceCard.
     */
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

    /**
     * This method is used to get the placed cards. It creates a new SocketMessage,
     * adds the string "getPlacedCards" and the playerId to the message data,
     * then sends this message to the client through the output stream.
     * @param playerId the id of the player to get the placed cards for.
     * @return Always returns the first element of an array of PlaceableCard.
     */
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

    /**
     * This method is used to get the secret objective. It creates a new SocketMessage,
     * adds the string "getSecretObj" and the playerId to the message data,
     * then sends this message to the client through the output stream.
     * @param playerId the id of the player to get the secret objective for.
     * @return Always returns null.
     */
    @Override
    public Objectives getSecretObjective(int playerId) {
        SocketMessage message = new SocketMessage();
        message.addData("getSecretObj");
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to get the resource counter. It creates a new SocketMessage,
     * adds the string "getResourceCounter" and the playerId to the message data,
     * then sends this message to the client through the output stream.
     * @param playerId the id of the player to get the resource counter for.
     * @return Always returns an empty array of integers.
     */
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
    /**
     * This method is used to send a private message. It creates a new SocketMessage,
     * adds the string "privateMessage" and the message to the message data,
     * then sends this message to the client through the output stream.
     * @param msg the message to send.
     */
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

    /**
     * This method is used to verify if a nickname is available. It creates a new SocketMessage,
     * adds the string "nickAvailable" and the nickname to the message data,
     * then sends this message to the client through the output stream.
     * @param nickname the nickname to verify.
     * @return Always returns false.
     */
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
    /**
     * This method is used to get the nicknames. It creates a new SocketMessage,
     * adds the string "getNick" to the message data,
     * then sends this message to the client through the output stream.
     * @return Always returns an empty array of strings.
     */
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
    /**
     * This method is used to get the number of players. It creates a new SocketMessage,
     * adds the string "getNumPlayers" to the message data,
     * then sends this message to the client through the output stream.
     * @return Always returns 0.
     */
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

    /**
     * This method is used to get the playable positions. It creates a new SocketMessage,
     * adds the string "getPlayPos" and the playerId to the message data,
     * then sends this message to the client through the output stream.
     * @param playerId the id of the player to get the playable positions for.
     * @return Always returns an empty array of booleans.
     */
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