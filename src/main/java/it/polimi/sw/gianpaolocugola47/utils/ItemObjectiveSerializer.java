package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.ItemObjective;

import java.lang.reflect.Type;

/**
 * This class is used to serialize an ItemObjective object into a JSON string.
 * It is used by the Gson library to convert an ItemObjective object into a JSON string.
 * json fields are 1:1 on the ItemObjective object fields with an additional "type" added.
 * The latter is crucial when I need to deserialize an objective deck from a JSON string, since
 * ItemObjective is a subclass of Objective.
 */
public class ItemObjectiveSerializer implements JsonSerializer<ItemObjective> {

    /**
     * Serializes an ItemObjective object into a JSON string.
     * Used by the Gson builder.
     */
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