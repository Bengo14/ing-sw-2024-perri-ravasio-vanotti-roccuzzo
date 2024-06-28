package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message that can be sent through a socket connection.
 * It contains a list of objects that represent the data of the message.
 * The first object of the list is always a string that represents the action that the message wants to perform.
 */
public class SocketMessage implements Serializable {

    private final List <Object> data;
    /**
     * Constructor of the class.
     */
    protected SocketMessage() {
        this.data = new ArrayList<>();
    }
    /**
     * This method adds an object to the list of data.
     * @param o the object to be added.
     */
    protected void addData(Object o) {
        this.data.add(o);
    }

    /**
     * This method calls the correct method of the client based on the action that the message wants to perform.
     * @param client the client that receives the message.
     */
    protected void doAction(SocketClient client) {
        switch ((String) data.removeFirst()) {

            case "setId" -> client.setIdAndRunCli((int) data.removeFirst());
            case "terminate" -> client.terminate();
            case "ping" -> client.ping();
            case "start" -> client.startGame((boolean) data.removeFirst());
            case "turn" -> client.setMyTurn();
            case "winner" -> client.showWinner((int) data.removeFirst());

            case "getNumPlayers" -> client.numOfPlayers = (int) data.removeFirst();

            case "message" -> {
                ChatMessage message = (ChatMessage) data.removeFirst();
                client.receiveMessage(message);
            }

            case "privateMessage" -> {
                ChatMessage message = (ChatMessage) data.removeFirst();
                client.receivePrivateMessage(message);
            }

            case "init" -> {
                String[] nicknames = (String[]) data.removeFirst();
                Objectives[] globalObjectives = (Objectives[]) data.removeFirst();
                ResourceCard[] cardsOnHand = (ResourceCard[]) data.removeFirst();
                ResourceCard[] cardsOnTable = (ResourceCard[]) data.removeFirst();
                client.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
            }

            case "decks" -> {
                ResourceCard resourceCardOnTop = (ResourceCard) data.removeFirst();
                GoldCard goldCardOnTop = (GoldCard) data.removeFirst();
                client.updateDecks(resourceCardOnTop, goldCardOnTop, (int) data.removeFirst()); // cards and drawPos
            }

            case "points" -> {
                int[] boardPoints = new int[client.numOfPlayers];
                int[] globalPoints = new int[client.numOfPlayers];
                for (int i = 0; i < boardPoints.length; i++)
                    boardPoints[i] = (int) data.removeFirst();
                for (int i = 0; i < globalPoints.length; i++)
                    globalPoints[i] = (int) data.removeFirst();
                client.updatePoints(boardPoints, globalPoints);
            }

            case "drawStarting" -> {
                client.drawStartingCardResponse = (StartingCard) data.removeFirst();
                client.setResponse();
            }

            case "setStarting" -> {
                client.setStartingResponse = (Objectives[]) data.removeFirst();
                client.setResponse();
            }

            case "getSecretObj" -> {
                client.getSecretObjectiveResponse = (Objectives) data.removeFirst();
                client.setResponse();
            }

            case "play" -> {
                client.playResponse = (boolean) data.removeFirst();
                client.setResponse();
            }

            case "getCardsOnHand" -> {
                ResourceCard[][] rc = new ResourceCard[(int)data.removeFirst()][3];
                for (int i = 0; i < rc.length; i++)
                    for (int j = 0; j < 3; j++)
                        rc[i][j] = (ResourceCard) data.removeFirst();
                client.getCardsOnHandResponse = rc;
                client.setResponse();
            }

            case "getPlacedCards" -> {
                PlaceableCard[][] pc = new PlaceableCard[PlayerTable.getMatrixDimension()][PlayerTable.getMatrixDimension()];
                for (int i = 0; i < PlayerTable.getMatrixDimension(); i++)
                    for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                        pc[i][j] = (PlaceableCard) data.removeFirst();
                        if(pc[i][j] != null)
                            pc[i][j].setFront((boolean) data.removeFirst());
                    }
                client.getPlacedCardsResponse = pc;
                client.setResponse();
            }

            case "getResourceCounter" -> {
                int[] resourceCounter = new int[7];
                for (int i = 0; i < 7; i++)
                    resourceCounter[i] = (int) data.removeFirst();
                client.getResourceCounterResponse = resourceCounter;
                client.setResponse();
            }

            case "getPlayPos" -> {
                client.getPlayPosResponse = (boolean[][]) data.removeFirst();
                client.setResponse();
            }

            case "nickAvailable" -> {
                client.nickAvailableResponse = (boolean) data.removeFirst();
                client.setResponse();
            }

            case "getNick" -> {
                client.nicknamesResponse = (String[]) data.removeFirst();
                client.setResponse();
            }

            case "getPlacingOrder" -> {
                ArrayList<int[]> placingOrder = new ArrayList<>();
                while (!data.isEmpty()) {
                    int[] row = new int[2];
                    row[0] = (int) data.removeFirst();
                    row[1] = (int) data.removeFirst();
                    placingOrder.add(row);
                }
                client.getPlacingOrderResponse = placingOrder;
                client.setResponse();
            }

            default -> System.err.println("[INVALID MESSAGE]");
        }
    }
    /**
     * This method calls the correct method of the handler based on the action that the message wants to perform.
     * @param handler the handler that receives the message.
     */
    protected void doAction(SocketClientHandler handler) {

        switch ((String) data.removeFirst()) {

            case "ping" -> handler.setPingAck();

            case "numPlayers" -> handler.setNumOfPlayers((int) data.removeFirst());

            case "addPlayer" -> handler.addPlayer(handler.id, (String) data.removeFirst());

            case "drawStarting" -> {
                synchronized (handler.client) {
                    handler.client.drawStartingCardResponse(handler.drawStartingCard());
                }
            }

            case "setStarting" -> {
                StartingCard card = (StartingCard) data.removeFirst();
                synchronized (handler.client) {
                    handler.client.setStartingCardAndDrawObjectivesResponse(handler.setStartingCardAndDrawObjectives(handler.id, card));
                }
            }

            case "setObj" -> {
                Objectives obj = (Objectives) data.removeFirst();
                handler.setSecretObjective(handler.id, obj);
            }

            case "startFromFile" -> handler.startGameFromFile();

            case "getSecretObj" -> handler.client.getSecretObjectiveResponse(handler.getSecretObjective(handler.id));

            case "play" -> {
                synchronized (handler.client) {
                    handler.client.playCardResponse(handler.playCard((int)data.removeFirst(), (int)data.removeFirst(), (int)data.removeFirst(), (int)data.removeFirst(), handler.id, (boolean)data.removeFirst()));
                }
            }

            case "draw" -> handler.drawCard((int) data.removeFirst(), handler.id);

            case "getCardsOnHand" -> {
                synchronized (handler.client) {
                    handler.client.getCardsOnHandResponse(handler.getCardsOnHand());
                }
            }

            case "getPlacedCards" -> {
                synchronized (handler.client) {
                    handler.client.getPlacedCardsResponse(handler.getPlacedCards((int) data.removeFirst()));
                }
            }

            case "getResourceCounter" -> {
                synchronized (handler.client) {
                    handler.client.getResourceCounterResponse(handler.getResourceCounter((int) data.removeFirst()));
                }
            }

            case "message" -> {
                ChatMessage message = (ChatMessage) data.removeFirst();
                handler.sendMessage(message);
            }

            case "privateMessage" -> {
                ChatMessage message = (ChatMessage) data.removeFirst();
                handler.sendPrivateMessage(message);
            }

            case "nickAvailable" -> {
                synchronized (handler.client) {
                    handler.client.isNicknameAvailableResponse(handler.isNicknameAvailable((String) data.removeFirst()));
                }
            }

            case "getNick" -> {
                synchronized (handler.client) {
                    handler.client.getNicknamesResponse(handler.getNicknames());
                }
            }

            case "getNumPlayers" -> {
                synchronized (handler.client) {
                    handler.client.getNumOfPlayersResponse(handler.getNumOfPlayers());
                }
            }

            case "getPlayPos" -> {
                synchronized (handler.client) {
                    handler.client.getPlayablePositionsResponse(handler.getPlayablePositions(handler.id));
                }
            }

            case "getPlacingOrder" -> {
                synchronized (handler.client) {
                    handler.client.getPlacingOrderResponse(handler.getPlacingOrder((int) data.removeFirst()));
                }
            }

            default -> System.err.println("[INVALID MESSAGE]");
        }
    }
}
