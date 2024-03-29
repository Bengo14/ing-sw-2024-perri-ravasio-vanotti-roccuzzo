package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import it.polimi.sw.gianpaolocugola47.model.Objectives;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ObjectiveDeserializer implements JsonDeserializer<Objectives> {
    private String objectiveTypeElementName;
    private Gson gson;
    private Map<String, Class<? extends Objectives>> objectiveTypeRegistry;

    public ObjectiveDeserializer(String objectiveTypeElementName) {
        this.objectiveTypeElementName = objectiveTypeElementName;
        this.gson = new Gson();
        this.objectiveTypeRegistry = new HashMap<>();
    }

    public void registerObjectiveType(String objectiveTypeName, Class<? extends Objectives> objectiveType) {
        objectiveTypeRegistry.put(objectiveTypeName, objectiveType);
    }

    @Override
    public Objectives deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject objectiveObject = jsonElement.getAsJsonObject();
        JsonElement objectiveTypeElement = objectiveObject.get(objectiveTypeElementName);

        Class<? extends Objectives> objectiveType = objectiveTypeRegistry.get(objectiveTypeElement.getAsString());
        return gson.fromJson(objectiveObject, objectiveType);
    }
}
