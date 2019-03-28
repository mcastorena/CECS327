package client.gui.MainDisplay;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import client.model.CollectionLightWeight;
import client.rpc.CECS327InputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * As part of the MVP-design pattern, this class represents the Model for a SongSearch
 */
public class SongSearchModel {

    /**
     * Retrieves the list of songs from the search
     *
     * @param is - InputStream
     * @return - List of songs
     * @throws IOException - Required for the InputStream
     */
    // Converts json from inputstream to list of songs related to the query
    public List<CollectionLightWeight> getResults(CECS327InputStream is) throws IOException {

        List<CollectionLightWeight> songList = new ArrayList<>();

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

        reader.beginArray();
        while (reader.hasNext()) {
            songList.add(gson.fromJson(reader, CollectionLightWeight.class));
        }
        reader.endArray();

        return songList;
    }
}
