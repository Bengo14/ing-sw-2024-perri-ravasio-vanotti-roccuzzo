package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OldNicknamesDeserializer implements JsonDeserializer<ArrayList<String>> {
    @Override
    public ArrayList<String> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return getNicknames(jsonObject.getAsJsonArray("oldNicknames"));
    }

    private ArrayList<String> getNicknames(JsonArray jsonArray) {
        ArrayList<String> nicknames = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            nicknames.add(jsonElement.getAsString());
        }
        return nicknames;
    }
}
