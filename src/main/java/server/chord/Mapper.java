package server.chord;

import com.google.gson.*;
import server.model.Collection;
import server.util.CollectionComparator;

import static server.core.Server.d;

import java.io.Serializable;
import java.util.ArrayList;

public class Mapper implements MapReduceInterface, Serializable {
    public void map(String key, JsonObject value, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception {
        Collection c = d.jsonToCollection(value);
        String newKey = c.getSongTitle();
        if (file.contains("artist")) {
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

    public void reduce(String key, ArrayList valuesList, IDFSInterface context, ChordMessageInterface chordContext, String file) throws Exception {
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
