package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.MainTable;

import java.lang.reflect.Type;

public class ControllerDeserializer implements JsonDeserializer<Controller> {
    public Controller deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Controller controller = new Controller();
        controller.setNumOfPlayers(jsonObject.get("numOfPlayers").getAsInt());
        controller.setClientsConnected(jsonObject.get("clientsConnected").getAsInt());
        controller.setPlayersAdded(jsonObject.get("playersAdded").getAsInt());
        controller.setStartingCardsAndObjAdded(jsonObject.get("startingCardsAndObjAdded").getAsInt());
        controller.setLastTurn(jsonObject.get("isLastTurn").getAsBoolean());
        controller.setCurrentPlayerId(jsonObject.get("currentPlayerId").getAsInt());
        MainTable mainTable = context.deserialize(jsonObject.get("mainTable"), MainTable.class);
        controller.setMainTable(mainTable);

        return controller;
    }
}
