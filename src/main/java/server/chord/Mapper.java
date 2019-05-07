package server.chord;

import com.google.gson.*;
import server.model.Collection;
import server.util.CollectionComparator;

import static server.core.Server.d;

import java.io.Serializable;
import java.util.ArrayList;

public class Mapper implements MapReduceInterface, Serializable {
    /**
     *
     * @param key - Index of value from it's PagesJson object
     * @param value - JsonObject from a page being mapped
     * @param context - DFS in which the mapreduce is being executed
     * @param chordContext - Chord in which the mapreduce is being executed. Used by IDFSInterface context
     * @param file - Name of file being mapped
     * @throws Exception
     */
    public void map(String key, JsonObject value, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception
    {
        Collection c = d.jsonToCollection(value);
        String newKey = c.getSongTitle();
        if(file.contains("artist"))
        {
            newKey = c.getArtistName();
        }

        JsonObject jo1 = new JsonObject();
        JsonObject releasejo = new JsonObject();
        JsonObject songJo = new JsonObject();
        JsonObject artistJo = new JsonObject();
        songJo.addProperty("title", c.getSongTitle());
        releasejo.addProperty("id", c.getId());
        releasejo.addProperty("name", c.getRelease().getName());
        artistJo.addProperty("name", c.getArtistName());

        jo1.add("release", releasejo);
        jo1.add("artist", artistJo);
        jo1.add("song", songJo);

        chordContext.emit(newKey, jo1, context, file);
    }

    /**
     * Runs reduce on a each entry in the TreeMap in the chord of the DFS
     * @param key - Key of map entry
     * @param valuesList - Value of entry in map
     * @param context - DFS in which reduce is being executed
     * @param chordContext - Chord in which reduce is being executed
     * @param file - Name of file being reduced
     * @throws Exception
     */
    public void reduce(String key, ArrayList valuesList, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception
    {
        //sort(values);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // Turn the jsonobjects in the arraylist into collections for easy sorting by value.
        valuesList.sort(new CollectionComparator(file));

        // Turn arraylist into jsonArray for emit
        String jsonString = gson.toJson(valuesList);

        JsonParser parser = new JsonParser();
        JsonArray valuesArray = parser.parse(jsonString).getAsJsonArray();

        chordContext.emit(key, valuesArray, context, file);
    }
}
