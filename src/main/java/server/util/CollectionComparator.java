package server.util;

import com.google.gson.JsonObject;
import server.model.Collection;
import static server.core.Server.d;

import java.util.Comparator;

public class CollectionComparator implements Comparator<JsonObject> {
    /**
     *
     * Compares two collections by song title. Used for sorting.
     */
    @Override
    public int compare(JsonObject jo1, JsonObject jo2) {
        Collection c1 = d.jsonToCollection(jo1);
        Collection c2 = d.jsonToCollection(jo2);

        String song1 = c1.getSongTitle();
        String song2 = c2.getSongTitle();

        return song1.compareToIgnoreCase(song2);
    }

}
