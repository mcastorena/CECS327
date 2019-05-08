package server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import server.model.Collection;

import static server.core.Server.d;

import java.util.Comparator;

/**
 * Custom comparator for Collection objects
 */
public class CollectionComparator implements Comparator<LinkedTreeMap> {

    String file;

    /**
     * Constructor
     *
     * @param file
     */
    public CollectionComparator(String file) {
        super();
        this.file = file;
    }

    /**
     * Compares two collections. Used for sorting values.
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

        // Sort values by song title for artistInvertedIndex.
        if (file.contains("artist")) {
            String song1 = c1.getSongTitle();
            String song2 = c2.getSongTitle();

            return song1.compareToIgnoreCase(song2);
        } else {
            // Sort values by artist name for musicInvertedIndex.
            String artist1 = c1.getArtistName();
            String artist2 = c2.getArtistName();

            return artist1.compareToIgnoreCase(artist2);
        }
    }

}
