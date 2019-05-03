package server.chord;

import com.google.gson.*;
import server.model.Collection;
import server.util.CollectionComparator;

import static server.core.Server.d;

import java.io.Serializable;
import java.util.ArrayList;

public class Mapper implements MapReduceInterface, Serializable {
    public void map(String key, JsonObject value, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception
    {
        //let newKey be the song title in value
//        JsonArray release =(JsonArray) value.get("release");
//        String newKey = release.get(1).getAsString();
        //let newValue be a subset of value
        // The new values can have the items of interest - Song title, year of release, duration, artist and album
//        JsonElement newValue = release.get(1);              // New value is songtitle
        //JsonObject newValue = (JsonObject) release.get(1);
//        context.emit(newKey, newValue, file);


        // TODO: Open up to artists as well.
        Collection c = d.jsonToCollection(value);
        String newKey = c.getSongTitle();

        JsonObject jo1 = new JsonObject();
        JsonObject releasejo = new JsonObject();
        JsonObject songJo = new JsonObject();
        JsonObject artistJo = new JsonObject();
        songJo.addProperty("title", c.getSongTitle());
        releasejo.addProperty("id", c.getId());
        artistJo.addProperty("name", c.getArtistName());

        jo1.add("release", releasejo);
        jo1.add("artist", artistJo);
        jo1.add("song", songJo);

        // Using the whole jsonobject as the value for now. Needs to at least store all data shown on UI and the release id.
        chordContext.emit(newKey, jo1, context, file);
    }

    public void reduce(String key, ArrayList valuesList, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception
    {
        //sort(values);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // Turn JsonArray into Java Arraylist
        //ArrayList valuesList = gson.fromJson(values, ArrayList.class);

        // Turn the jsonobjects in the arraylist into collections for easy sorting.
        valuesList.sort(new CollectionComparator());

        // Turn arraylist back into jsonArray for emit
        String jsonString = gson.toJson(valuesList);

        JsonParser parser = new JsonParser();
        JsonArray valuesArray = parser.parse(jsonString).getAsJsonArray();

        chordContext.emit(key, valuesArray, context, file);
    }
}
