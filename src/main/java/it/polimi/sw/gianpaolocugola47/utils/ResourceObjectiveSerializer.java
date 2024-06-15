package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.ResourceObjective;

import java.lang.reflect.Type;

public class ResourceObjectiveSerializer implements JsonSerializer<ResourceObjective> {
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
