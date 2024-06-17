package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;

public class PlayerTableSerializer implements JsonSerializer<PlayerTable> {
    private final Gson gson;
    public PlayerTableSerializer() {
        RuntimeTypeAdapterFactory<ResourceCard> resourceCardAdapterFactory = RuntimeTypeAdapterFactory.of(ResourceCard.class, "type")
                .registerSubtype(ResourceCard.class, "ResourceCard")
                .registerSubtype(GoldCard.class, "GoldCard");
        RuntimeTypeAdapterFactory<Objectives> objectivesAdapterFactory = RuntimeTypeAdapterFactory.of(Objectives.class, "type")
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
    public JsonElement serialize(PlayerTable playerTable, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickName", playerTable.getNickName());
        jsonObject.addProperty("id", playerTable.getId());
        jsonObject.addProperty("canPlay", playerTable.isCanPlay());
        JsonElement resCount = jsonSerializationContext.serialize(playerTable.getResourceCounter());
        jsonObject.add("resourceCounter", resCount);
        JsonElement obj = gson.toJsonTree(playerTable.getSecretObjective(), new TypeToken<Objectives>(){}.getType());
        jsonObject.add("secretObjective", obj);
        JsonElement cards = gson.toJsonTree(playerTable.getCardsOnHand(), new TypeToken<ResourceCard[]>(){}.getType());
        jsonObject.add("cardsOnHand", cards);
        JsonElement starting = jsonSerializationContext.serialize(playerTable.getStartingCard());
        jsonObject.add("startingCard", starting);
        JsonElement board = jsonSerializationContext.serialize(playerTable.getCardIdMatrix());
        jsonObject.add("cardIdMatrix", board);
        JsonElement sides = jsonSerializationContext.serialize(playerTable.getCardSideMatrix());
        jsonObject.add("cardSideMatrix", sides);
        return jsonObject;
    }
}
