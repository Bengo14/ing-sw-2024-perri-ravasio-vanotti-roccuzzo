package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.LShapePatternObjective;

import java.lang.reflect.Type;

public class LShapePatternObjectiveSerializer implements JsonSerializer<LShapePatternObjective> {
    @Override
    public JsonElement serialize(LShapePatternObjective lShapePatternObjective, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description",lShapePatternObjective.getDescription());
        jsonObject.addProperty("id", lShapePatternObjective.getId());
        jsonObject.addProperty("points", lShapePatternObjective.getPoints());
        jsonObject.addProperty("imgPathFront", lShapePatternObjective.getImgPathFront());
        jsonObject.addProperty("imgPathBack", lShapePatternObjective.getImgPathBack());
        JsonElement orientation = jsonSerializationContext.serialize(lShapePatternObjective.getOrientation());
        JsonElement mainRes = jsonSerializationContext.serialize(lShapePatternObjective.getMainResource());
        JsonElement secRes = jsonSerializationContext.serialize(lShapePatternObjective.getSecondaryResource());
        jsonObject.add("orientation", orientation);
        jsonObject.add("mainResource", mainRes);
        jsonObject.add("secondaryResource", secRes);
        jsonObject.addProperty("type", "LShapePatternObjective");
        return jsonObject;
    }
}
