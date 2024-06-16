package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.controller.Controller;

import java.lang.reflect.Type;

public class ControllerSerializer implements JsonSerializer<Controller> {
    @Override
    public JsonElement serialize(Controller controller, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        //technically already present in mainTable; useful to have as the first parameter since the server has to check
        //old nicknames to check if match is resuming.
        JsonElement nicknames = jsonSerializationContext.serialize(controller.getMainTable().getNicknames());
        jsonObject.add("oldNicknames", nicknames);
        jsonObject.addProperty("currentPlayerId", controller.getCurrentPlayerId());
        jsonObject.addProperty("clientsConnected", controller.getClientsConnected());
        jsonObject.addProperty("numOfPlayers", controller.getNumOfPlayers());
        jsonObject.addProperty("playersAdded", controller.getPlayersAdded());
        jsonObject.addProperty("startingCardsAndObjAdded", controller.getStartingCardsAndObjAdded());
        jsonObject.addProperty("isLastTurn", controller.isLastTurn());
        //JsonElement mainTable = jsonSerializationContext.serialize(controller.getMainTable());
        //jsonObject.add("mainTable", mainTable);
        return jsonObject;
    }
}
