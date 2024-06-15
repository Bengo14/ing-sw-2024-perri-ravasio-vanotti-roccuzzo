package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;

import java.lang.reflect.Type;

public class StartingCardSerializer implements JsonSerializer<StartingCard> {
    @Override
    public JsonElement serialize(StartingCard startingCard, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", startingCard.getId());
        jsonObject.addProperty("imgPathFront", startingCard.getFrontImgPath());
        jsonObject.addProperty("imgPathBack", startingCard.getBackImgPath());
        jsonObject.addProperty("isFront", startingCard.isFront());
        jsonObject.addProperty("isFlaggedForObjective", startingCard.getIsFlaggedForObjective());
        jsonObject.addProperty("line", startingCard.getLine());
        jsonObject.addProperty("column", startingCard.getColumn());
        JsonElement corners = jsonSerializationContext.serialize(startingCard.getVisibleCorners());
        jsonObject.add("corners", corners);
        JsonElement resCb = jsonSerializationContext.serialize(startingCard.getResourcesCentreBack());
        jsonObject.add("resourcesCentreBack", resCb);
        jsonObject.addProperty("type", "StartingCard");
        return jsonObject;
    }
}
