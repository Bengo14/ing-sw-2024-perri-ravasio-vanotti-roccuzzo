package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.GoldCard;

import java.lang.reflect.Type;

public class GoldCardSerializer implements JsonSerializer<GoldCard> {
    @Override
    public JsonElement serialize(GoldCard goldCard, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", goldCard.getId());
        jsonObject.addProperty("points", goldCard.getThisPoints());
        jsonObject.addProperty("imgPathFront", goldCard.getFrontImgPath());
        jsonObject.addProperty("imgPathBack", goldCard.getBackImgPath());
        jsonObject.addProperty("isFront", goldCard.isFront());
        jsonObject.addProperty("isFlaggedForObjective", goldCard.getIsFlaggedForObjective());
        jsonObject.addProperty("line", goldCard.getLine());
        jsonObject.addProperty("column", goldCard.getColumn());
        jsonObject.addProperty("pointsForCorners", goldCard.isPointsForCorners());
        jsonObject.addProperty("pointsForItems", goldCard.isPointsForItems());
        JsonElement resource = jsonSerializationContext.serialize(goldCard.getResourceCentreBack());
        jsonObject.add("resourceCentreBack", resource);
        JsonElement corners = jsonSerializationContext.serialize(goldCard.getVisibleCorners());
        jsonObject.add("corners", corners);
        JsonElement resReq = jsonSerializationContext.serialize(goldCard.getResourcesRequired());
        jsonObject.add("resourcesRequired", resReq);
        JsonElement itemReq = jsonSerializationContext.serialize(goldCard.getItemRequired());
        jsonObject.add("itemRequired", itemReq);
        jsonObject.addProperty("type", "GoldCard");
        return jsonObject;
    }
}
