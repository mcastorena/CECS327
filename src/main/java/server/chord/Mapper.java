package server.chord;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

public class Mapper implements MapReduceInterface {
    public void map(String key, JsonObject value, DFS context, String file) throws IOException
    {
        //let newKey be the song title in value
        JsonArray release =(JsonArray) value.get("release");
        String newKey = release.get(1).getAsString();
        //let newValue be a subset of value
        // The new values can have the items of interest - Song title, year of release, duration, artist and album
        JsonElement newValue = release.get(1);              // New value is songtitle
        //JsonObject newValue = (JsonObject) release.get(1);
        context.emit(newKey, newValue, file);
    }

    public void reduce(String key, JsonObject values, DFS context, String file) throws IOException
    {
        //sort(values);
        context.emit(key, values, file);
    }
}