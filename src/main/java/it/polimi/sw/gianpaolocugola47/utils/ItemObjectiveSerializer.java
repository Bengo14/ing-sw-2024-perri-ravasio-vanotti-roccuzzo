package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.ItemObjective;

import java.lang.reflect.Type;

public class ItemObjectiveSerializer implements JsonSerializer<ItemObjective> {
    @Override
    public JsonElement serialize(ItemObjective itemObjective, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description",itemObjective.getDescription());
        jsonObject.addProperty("id", itemObjective.getId());
        jsonObject.addProperty("points", itemObjective.getPoints());
        jsonObject.addProperty("imgPathFront", itemObjective.getImgPathFront());
        jsonObject.addProperty("imgPathBack", itemObjective.getImgPathBack());
        JsonElement itemsRequired = jsonSerializationContext.serialize(itemObjective.getItemsRequired());
        jsonObject.add("itemsRequired", itemsRequired);
        jsonObject.addProperty("type", "ItemObjective");
        return jsonObject;
    }
}
