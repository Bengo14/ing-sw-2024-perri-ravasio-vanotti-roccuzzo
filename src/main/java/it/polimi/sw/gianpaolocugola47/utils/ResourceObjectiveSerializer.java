package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.ResourceObjective;

import java.lang.reflect.Type;

/**
 * Custom serializer for the ResourceObjective class.
 * Used to serialize a ResourceObjective object into a JSON representation.
 * json fields are 1:1 on the ResourceObjective object fields with an additional "type" added.
 * The latter is crucial when I need to deserialize an objective deck from a JSON string, since
 * ResourceObjective is a subclass of Objective.
 */
public class ResourceObjectiveSerializer implements JsonSerializer<ResourceObjective> {
    /**
     * Serializes a ResourceObjective object into a JSON representation.
     * Used by the GsonBuilder.
     */
    @Override
    public JsonElement serialize(ResourceObjective resourceObjective, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description",resourceObjective.getDescription());
        jsonObject.addProperty("id", resourceObjective.getId());
        jsonObject.addProperty("points", resourceObjective.getPoints());
        jsonObject.addProperty("imgPathFront", resourceObjective.getImgPathFront());
        jsonObject.addProperty("imgPathBack", resourceObjective.getImgPathBack());
        JsonElement resource = jsonSerializationContext.serialize(resourceObjective.getResource());
        jsonObject.add("itemsRequired", resource);
        jsonObject.addProperty("type", "ResourceObjective");
        return jsonObject;
    }
}
