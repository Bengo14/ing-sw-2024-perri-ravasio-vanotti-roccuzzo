package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;

public class PlayerTableDeserializer implements JsonDeserializer<PlayerTable> {
    @Override
    public PlayerTable deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        PlayerTable playerTable = new PlayerTable();
        playerTable.setNickname(jsonObject.get("nickName").getAsString());
        playerTable.setId(jsonObject.get("id").getAsInt());
        playerTable.setCanPlay(jsonObject.get("canPlay").getAsBoolean());
        playerTable.setResourceCounter(jsonArrayToInt(jsonObject.get("resourceCounter").getAsJsonArray()));
        ResourceCardDeserializer resDeserializer = new ResourceCardDeserializer("type");
        resDeserializer.registerCardType("ResourceCard", ResourceCard.class);
        resDeserializer.registerCardType("GoldCard", GoldCard.class);

        ObjectiveDeserializer objDeserializer = new ObjectiveDeserializer("type");
        objDeserializer.registerObjectiveType("ItemObjective", ItemObjective.class);
        objDeserializer.registerObjectiveType("ResourceObjective", ResourceObjective.class);
        objDeserializer.registerObjectiveType("LShapePatternObjective",LShapePatternObjective.class);
        objDeserializer.registerObjectiveType("DiagonalPatternObjective",DiagonalPatternObjective.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResourceCard.class, resDeserializer)
                .registerTypeAdapter(Objectives.class, objDeserializer)
                .create();
        Type listOfCards = new TypeToken<ResourceCard[]>() {}.getType();
        Type obj = new TypeToken<Objectives>() {}.getType();
        playerTable.setSecretObjective(gson.fromJson(jsonObject.get("secretObjective"), obj));
        playerTable.setCardsOnHand(gson.fromJson(jsonObject.get("cardsOnHand"), listOfCards));
        playerTable.setCardIdMatrix(jsonDeserializationContext.deserialize(jsonObject.get("cardIdMatrix"), int[][].class));
        playerTable.setCardSideMatrix(jsonDeserializationContext.deserialize(jsonObject.get("cardSideMatrix"), boolean[][].class));
        return playerTable;
    }

    private int[] jsonArrayToInt(JsonArray jsonArray) {
        int[] array = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = jsonArray.get(i).getAsInt();
        }
        return array;
    }
}
