package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.MainTable;

import java.lang.reflect.Type;

public class MainTableSerializer implements JsonSerializer<MainTable> {
    @Override
    public JsonElement serialize(MainTable mainTable, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonElement cardsOnTable = jsonSerializationContext.serialize(mainTable.getCardsOnTable());
        jsonObject.add("cardsOnTable", cardsOnTable);
        JsonElement globalObjectives = jsonSerializationContext.serialize(mainTable.getGlobalObjectives());
        jsonObject.add("globalObjectives", globalObjectives);
        JsonElement boardPoints = jsonSerializationContext.serialize(mainTable.getBoardPoints());
        jsonObject.add("boardPoints", boardPoints);
        JsonElement globalPoints = jsonSerializationContext.serialize(mainTable.getGlobalPoints());
        jsonObject.add("globalPoints", globalPoints);
        jsonObject.addProperty("numOfPlayers", mainTable.getNumOfPlayers());
        return jsonObject;
    }
}
