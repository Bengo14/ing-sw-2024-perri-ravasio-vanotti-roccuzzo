package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.sw.gianpaolocugola47.model.DiagonalPatternObjective;

import java.lang.reflect.Type;

public class DiagonalPatternObjectiveSerializer implements JsonSerializer<DiagonalPatternObjective> {
    @Override
    public JsonElement serialize(DiagonalPatternObjective diagonalPatternObjective, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description",diagonalPatternObjective.getDescription());
        jsonObject.addProperty("id", diagonalPatternObjective.getId());
        jsonObject.addProperty("points", diagonalPatternObjective.getPoints());
        jsonObject.addProperty("imgPathFront", diagonalPatternObjective.getImgPathFront());
        jsonObject.addProperty("imgPathBack", diagonalPatternObjective.getImgPathBack());
        JsonElement orientation = jsonSerializationContext.serialize(diagonalPatternObjective.isAscending());
        JsonElement res = jsonSerializationContext.serialize(diagonalPatternObjective.getResource());
        jsonObject.add("isAscending", orientation);
        jsonObject.add("resource", res);
        jsonObject.addProperty("type", "DiagonalPatternObjective");
        return jsonObject;
    }
}
