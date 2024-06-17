package it.polimi.sw.gianpaolocugola47.utils;
import com.google.gson.*;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ResourceCardDeserializer implements JsonDeserializer<PlaceableCard>{
    private final String cardTypeElementName;
    private final Gson gson;
    private final Map<String, Class<? extends ResourceCard>> cardTypeRegistry;
    //three types: ResourceCard, StartingCard, GoldCard
    public ResourceCardDeserializer(String cardTypeElementName) {
        this.cardTypeElementName = cardTypeElementName;
        this.gson = new Gson();
        this.cardTypeRegistry = new HashMap<>();
    }

    public void registerCardType(String cardTypeName, Class<? extends ResourceCard> cardType) {
        cardTypeRegistry.put(cardTypeName, cardType);
    }

    public ResourceCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject cardObject = jsonElement.getAsJsonObject();
        JsonElement cardTypeElement = cardObject.get(cardTypeElementName);
        Class<? extends ResourceCard> cardType = cardTypeRegistry.get(cardTypeElement.getAsString());
        return gson.fromJson(cardObject, cardType);
    }
}
