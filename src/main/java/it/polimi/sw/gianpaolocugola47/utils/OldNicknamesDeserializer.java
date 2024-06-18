package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Custom deserializer for the player's nicknames on a previous save file.
 * Used to deserialize the JSON representation of the old nicknames into an ArrayList of Strings.
 * Said nicknames are read from the "controllerStatus" file.
 */
public class OldNicknamesDeserializer implements JsonDeserializer<ArrayList<String>> {
    /**
     * Deserializes the JSON representation of the old nicknames into an ArrayList of Strings.
     * Used by the GsonBuilder.
     */
    @Override
    public ArrayList<String> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return getNicknames(jsonObject.getAsJsonArray("oldNicknames"));
    }

    /**
     * Extracts the nicknames from the JSON representation.
     * @param jsonArray : JSON array containing the nicknames.
     * @return : ArrayList of Strings containing the nicknames.
     */
    private ArrayList<String> getNicknames(JsonArray jsonArray) {
        ArrayList<String> nicknames = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            nicknames.add(jsonElement.getAsString());
        }
        return nicknames;
    }
}
