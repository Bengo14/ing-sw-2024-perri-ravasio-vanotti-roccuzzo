package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import it.polimi.sw.gianpaolocugola47.model.Objectives;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom deserializer for the Objectives class.
 * Used to deserialize the JSON representation of an objective into the correct subclass of Objectives.
 */
public class ObjectiveDeserializer implements JsonDeserializer<Objectives> {

    private final String objectiveTypeElementName;
    private final Gson gson;
    private final Map<String, Class<? extends Objectives>> objectiveTypeRegistry;

    /**
     * Constructor. Initializes Gson and a map of objective types.
     * @param objectiveTypeElementName: name of the subtype element in the JSON representation of an objective.
     */
    public ObjectiveDeserializer(String objectiveTypeElementName) {
        this.objectiveTypeElementName = objectiveTypeElementName;
        this.gson = new Gson();
        this.objectiveTypeRegistry = new HashMap<>();
    }

    /**
     * Registers a new objective type in the map.
     * @param objectiveTypeName : name of the objective type.
     * @param objectiveType : class of the objective type.
     */
    public void registerObjectiveType(String objectiveTypeName, Class<? extends Objectives> objectiveType) {
        objectiveTypeRegistry.put(objectiveTypeName, objectiveType);
    }

    /**
     * Deserializes the JSON representation of an objective into the correct subclass of Objectives.
     * Used by the GsonBuilder.
     */
    @Override
    public Objectives deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject objectiveObject = jsonElement.getAsJsonObject();
        JsonElement objectiveTypeElement = objectiveObject.get(objectiveTypeElementName);

        Class<? extends Objectives> objectiveType = objectiveTypeRegistry.get(objectiveTypeElement.getAsString());
        return gson.fromJson(objectiveObject, objectiveType);
    }
}