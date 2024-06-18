package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;

public class MainTableSerializer implements JsonSerializer<MainTable> {
    private final Gson gson;
    public MainTableSerializer() {
        RuntimeTypeAdapterFactory<ResourceCard> resourceCardAdapterFactory = RuntimeTypeAdapterFactory.of(ResourceCard.class, "type")
                .registerSubtype(ResourceCard.class, "ResourceCard")
                .registerSubtype(GoldCard.class, "GoldCard");
        RuntimeTypeAdapterFactory<Objectives> objectivesAdapterFactory = RuntimeTypeAdapterFactory.of(Objectives.class, "type")
                .registerSubtype(Objectives.class, "Objectives")
                .registerSubtype(ItemObjective.class, "ItemObjective")
                .registerSubtype(ResourceObjective.class, "ResourceObjective")
                .registerSubtype(LShapePatternObjective.class, "LShapePatternObjective")
                .registerSubtype(DiagonalPatternObjective.class, "DiagonalPatternObjective");
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(resourceCardAdapterFactory)
                .registerTypeAdapterFactory(objectivesAdapterFactory)
                .create();
    }
    @Override
    public JsonElement serialize(MainTable mainTable, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonElement globalObjectives = gson.toJsonTree(mainTable.getGlobalObjectives(), new TypeToken<Objectives[]>(){}.getType());
        jsonObject.add("globalObjectives", globalObjectives);
        JsonElement cardsOnTable = gson.toJsonTree(mainTable.getCardsOnTable(), new TypeToken<ResourceCard[]>(){}.getType());
        jsonObject.add("cardsOnTable", cardsOnTable);
        JsonElement boardPoints = jsonSerializationContext.serialize(mainTable.getBoardPoints());
        jsonObject.add("boardPoints", boardPoints);
        JsonElement globalPoints = jsonSerializationContext.serialize(mainTable.getGlobalPoints());
        jsonObject.add("globalPoints", globalPoints);
        jsonObject.addProperty("numOfPlayers", mainTable.getNumOfPlayers());
        jsonObject.addProperty("endGame", mainTable.isEndGame());
        return jsonObject;
    }
}
