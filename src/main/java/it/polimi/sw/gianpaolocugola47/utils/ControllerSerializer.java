package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.MainTable;

import java.lang.reflect.Type;

/**
 * This class is used to serialize a Controller object into a JSON string.
 * It is used by the Gson library to convert a Controller object into a JSON string.
 * json fields are 1:1 on the Controller object fields; the MainTable object is serialized using its own serializer.
 * It also adds a nicknames field not present in the controller class, used to check if the server needs to load a previous game.
 */
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
        MainTableSerializer mainTableSerializer = new MainTableSerializer();
        JsonElement mainTable = mainTableSerializer.serialize(controller.getMainTable(), MainTable.class, jsonSerializationContext);
        jsonObject.add("mainTable", mainTable);
        return jsonObject;
    }
}
