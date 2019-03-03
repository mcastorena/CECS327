package Gui.MainDisplay;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import model.Collection;
import model.CollectionLightWeight;
//import model.SearchResult;
import rpc.CECS327InputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SongSearchModel {
    List<Collection> musicDatabase;
    //SearchResult results;

    public List<Collection> getMusicDatabase() {
        return musicDatabase;
    }

    public void setMusicDatabase(final List<Collection> musicDatabase) {
        this.musicDatabase = musicDatabase;
    }

//    public SearchResult getResults() {
//        return results;
//    }
    public List<CollectionLightWeight> getResults(CECS327InputStream is) throws IOException {
        List<CollectionLightWeight> songList = new ArrayList<>();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        reader.beginArray();
        while(reader.hasNext())
        {
            //reader.beginObject();
            songList.add(gson.fromJson(reader, CollectionLightWeight.class));
            //reader.endObject();
        }
        reader.endArray();
        //return results;
        return songList;
    }

//    public void setResults(final SearchResult results) {
//        this.results = results;
//    }
}
