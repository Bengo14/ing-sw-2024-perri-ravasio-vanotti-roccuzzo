package it.polimi.sw.gianpaolocugola47.utils;
import com.google.gson.*;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom deserializer for the ResourceCard class.
 * Used to deserialize the JSON representation of a resource card into the correct subclass of ResourceCard.
 */
public class ResourceCardDeserializer implements JsonDeserializer<PlaceableCard>{
    private final String cardTypeElementName;
    private final Gson gson;
    private final Map<String, Class<? extends ResourceCard>> cardTypeRegistry;
    //three types: ResourceCard, StartingCard, GoldCard

    /**
     * Constructor. Initializes Gson and a map of card types.
     * @param cardTypeElementName : name of the subtype element in the JSON representation of a resource card.
     */
    public ResourceCardDeserializer(String cardTypeElementName) {
        this.cardTypeElementName = cardTypeElementName;
        this.gson = new Gson();
        this.cardTypeRegistry = new HashMap<>();
    }

    /**
     * Registers a new card type in the map.
     * @param cardTypeName : name of the card type.
     * @param cardType : class of the card type.
     */
    public void registerCardType(String cardTypeName, Class<? extends ResourceCard> cardType) {
        cardTypeRegistry.put(cardTypeName, cardType);
    }

    /**
     * Deserializes the JSON representation of a resource card into the correct subclass of ResourceCard.
     * Used by the GsonBuilder.
     */
    public ResourceCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject cardObject = jsonElement.getAsJsonObject();
        JsonElement cardTypeElement = cardObject.get(cardTypeElementName);
        Class<? extends ResourceCard> cardType = cardTypeRegistry.get(cardTypeElement.getAsString());
        return gson.fromJson(cardObject, cardType);
    }
}
