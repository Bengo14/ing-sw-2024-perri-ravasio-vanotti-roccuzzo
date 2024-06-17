package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;

public class MainTableDeserializer implements JsonDeserializer<MainTable> {

    @Override
    public MainTable deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        MainTable mainTable = new MainTable();
        mainTable.setNumOfPlayers(jsonObject.get("numOfPlayers").getAsInt());
        mainTable.setEndGame(jsonObject.get("endGame").getAsBoolean());
        mainTable.setGlobalPoints(jsonArrayToInt(jsonObject.get("globalPoints").getAsJsonArray()));
        mainTable.setBoardPoints(jsonArrayToInt(jsonObject.get("boardPoints").getAsJsonArray()));

        ResourceCardDeserializer resDeserializer = new ResourceCardDeserializer("type");
        resDeserializer.registerCardType("ResourceCard", ResourceCard.class);
        resDeserializer.registerCardType("GoldCard", GoldCard.class);

        ObjectiveDeserializer objDeserializer = new ObjectiveDeserializer("type");
        objDeserializer.registerObjectiveType("ItemObjective", ItemObjective.class);
        objDeserializer.registerObjectiveType("ResourceObjective",ResourceObjective.class);
        objDeserializer.registerObjectiveType("LShapePatternObjective",LShapePatternObjective.class);
        objDeserializer.registerObjectiveType("DiagonalPatternObjective",DiagonalPatternObjective.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResourceCard.class, resDeserializer)
                .registerTypeAdapter(Objectives.class, objDeserializer)
                .create();
        Type listOfCards = new TypeToken<ResourceCard[]>() {}.getType();
        Type listOfObj = new TypeToken<Objectives[]>() {}.getType();

        mainTable.setGlobalObjectives(gson.fromJson(jsonObject.get("globalObjectives"), listOfObj));
        mainTable.setCardsOnTable(gson.fromJson(jsonObject.get("cardsOnTable"), listOfCards));
        return mainTable;
    }

    private int[] jsonArrayToInt(JsonArray jsonArray) {
        int[] array = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = jsonArray.get(i).getAsInt();
        }
        return array;
    }
}
