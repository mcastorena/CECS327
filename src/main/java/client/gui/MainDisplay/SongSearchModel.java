package client.gui.MainDisplay;

import client.model.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import client.model.CollectionLightWeight;
import client.rpc.CECS327InputStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * As part of the MVP-design pattern, this class represents the Model for a SongSearch
 */
public class SongSearchModel {

    /**
     * Retrieves the list of songs/artists from the search
     *
     * @param is - InputStream
     * @return - List of songs
     * @throws IOException - Required for the InputStream
     */
    // Converts json from inputstream to list of songs related to the query
    public SearchResult getResults(CECS327InputStream is) throws IOException {

        List<CollectionLightWeight> songList = new ArrayList<>();
        List<CollectionLightWeight> artistSongList = new ArrayList<>();

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

        try {
            reader.beginArray();
            reader.beginArray();
            while (reader.hasNext()) {
                songList.add(gson.fromJson(reader, CollectionLightWeight.class));
            }
            reader.endArray();
            reader.beginArray();
            while (reader.hasNext()) {
                artistSongList.add(gson.fromJson(reader, CollectionLightWeight.class));
            }
            reader.endArray();
        } catch (EOFException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        return new SearchResult(songList, artistSongList);
    }
}
