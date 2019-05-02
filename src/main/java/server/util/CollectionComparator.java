package server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import server.model.Collection;
import static server.core.Server.d;

import java.util.Comparator;

public class CollectionComparator implements Comparator<LinkedTreeMap> {
    /**
     *
     * Compares two collections by song title. Used for sorting.
     */
    @Override
    public int compare(LinkedTreeMap t1, LinkedTreeMap t2) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonObject jo1 = gson.toJsonTree(t1).getAsJsonObject();
        JsonObject jo2 = gson.toJsonTree(t2).getAsJsonObject();

        Collection c1 = d.jsonToCollection(jo1);
        Collection c2 = d.jsonToCollection(jo2);

        String song1 = c1.getSongTitle();
        String song2 = c2.getSongTitle();

        return song1.compareToIgnoreCase(song2);
    }

}
