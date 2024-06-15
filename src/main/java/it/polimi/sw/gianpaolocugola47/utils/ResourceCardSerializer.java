package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;

import java.lang.reflect.Type;

public class ResourceCardSerializer implements JsonSerializer<ResourceCard> {
    @Override
    public JsonElement serialize(ResourceCard resourceCard, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", resourceCard.getId());
        jsonObject.addProperty("points", resourceCard.getThisPoints());
        jsonObject.addProperty("imgPathFront", resourceCard.getFrontImgPath());
        jsonObject.addProperty("imgPathBack", resourceCard.getBackImgPath());
        jsonObject.addProperty("isFront", resourceCard.isFront());
        jsonObject.addProperty("isFlaggedForObjective", resourceCard.getIsFlaggedForObjective());
        jsonObject.addProperty("line", resourceCard.getLine());
        jsonObject.addProperty("column", resourceCard.getColumn());
        JsonElement resource = jsonSerializationContext.serialize(resourceCard.getResourceCentreBack());
        jsonObject.add("resourceCentreBack", resource);
        JsonElement corners = jsonSerializationContext.serialize(resourceCard.getVisibleCorners());
        jsonObject.add("corners", corners);
        jsonObject.addProperty("type", "ResourceCard");
        return jsonObject;
    }
}
