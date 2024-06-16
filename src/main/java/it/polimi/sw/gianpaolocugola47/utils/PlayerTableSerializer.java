package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;

import java.lang.reflect.Type;

public class PlayerTableSerializer implements JsonSerializer<PlayerTable> {
    @Override
    public JsonElement serialize(PlayerTable playerTable, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", playerTable.getNickName());
        jsonObject.addProperty("id", playerTable.getId());
        jsonObject.addProperty("canPlay", playerTable.isCanPlay());
        JsonElement resCount = jsonSerializationContext.serialize(playerTable.getResourceCounter());
        jsonObject.add("resourceCounter", resCount);

        return jsonObject;
    }
}
