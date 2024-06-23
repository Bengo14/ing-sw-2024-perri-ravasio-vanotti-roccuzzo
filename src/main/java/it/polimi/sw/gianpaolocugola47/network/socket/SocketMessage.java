package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SocketMessage implements Serializable {

    private final List <Object> data;

    protected SocketMessage() {
        this.data = new ArrayList<Object>();
    }

    protected void addData(Object o) {
        this.data.add(o);
    }

    protected void doAction(SocketClient client) {
        switch ((String) data.removeFirst()) {

            case "setId" -> client.setIdAndRunCli((int) data.removeFirst());
            case "terminate" -> client.terminate();
            case "ping" -> client.ping();
            case "start" -> client.startGame((boolean) data.removeFirst());
            case "turn" -> client.setMyTurn();
            case "gameOver" -> client.gameOver();
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
                int[] boardPoints = (int[]) data.removeFirst();
                int[] globalPoints = (int[]) data.removeFirst();
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

            case "play" -> {
                client.playResponse = (boolean) data.removeFirst();
                client.setResponse();
            }

            case "getCardsOnHand" -> {
                client.getCardsOnHandResponse = (ResourceCard[][]) data.removeFirst();
                client.setResponse();
            }

            case "getPlacedCards" -> {
                client.getPlacedCardsResponse = (PlaceableCard[][]) data.removeFirst();
                client.setResponse();
            }

            case "getResourceCounter" -> {
                client.getResourceCounterResponse = (int[]) data.removeFirst();
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

            default -> System.err.println("[INVALID MESSAGE]");
        }
    }

    protected void doAction(SocketClientHandler handler) {

        switch ((String) data.removeFirst()) {

            case "ping" -> handler.setPingAck();

            case "numPlayers" -> handler.setNumOfPlayers((int) data.removeFirst());

            case "addPlayer" -> handler.addPlayer((int) data.removeFirst(), (String) data.removeFirst());

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

            default -> System.err.println("[INVALID MESSAGE]");
        }
    }
}
