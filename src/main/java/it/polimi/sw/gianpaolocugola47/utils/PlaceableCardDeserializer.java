package it.polimi.sw.gianpaolocugola47.utils;
import com.google.gson.*;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;
import java.util.Map;

public class PlaceableCardDeserializer {
    private final String objectiveTypeElementName;
    private final Gson gson;
    private final Map<String, Class<? extends PlaceableCard>> cardTypeRegistry;

    public PlaceableCardDeserializer(String objectiveTypeElementName, Gson gson, Map<String, Class<? extends PlaceableCard>> cardTypeRegistry) {
        this.objectiveTypeElementName = objectiveTypeElementName;
        this.gson = gson;
        this.cardTypeRegistry = cardTypeRegistry;
    }

    public void registerCardType(String cardTypeName, Class<? extends PlaceableCard> cardType) {
        cardTypeRegistry.put(cardTypeName, cardType);
    }

    public PlaceableCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject cardObject = jsonElement.getAsJsonObject();
        JsonElement cardTypeElement = cardObject.get(objectiveTypeElementName);
        Class<? extends PlaceableCard> cardType = cardTypeRegistry.get(cardTypeElement.getAsString());
        return gson.fromJson(cardObject, cardType);
    }
}
