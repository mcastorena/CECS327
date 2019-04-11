package server.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.model.Collection;
import server.model.SearchResult;
import server.util.Search;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;


public class SearchResultDispatcher extends Dispatcher implements DispatcherService {
    static final int FRAGMENT_SIZE = 44100;

    /**
     * getSearchResultChunk: Gets a chunk of a given search result
     * @param fragment: The chunk corresponds to
     * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getSearchResultChunk(Long fragment) throws IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];

        System.out.println("SearchResultDispatcher is getting chunk");
        InputStream inputStream = new ByteArrayInputStream(Server.byteSearchResult);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();

        // Encode in base64 so it can be transmitted
        return Base64.getEncoder().encodeToString(buf);
    }

    /*
     * getSize: Gets a size of the byte array
     * @param query: the search query from user
     */
    public Integer getSize(String query)
    {
        //this.query = query;
        SearchResult searchResult = Search.search(query, Server.songList);
        List<Collection> songResults = searchResult.getSongResultList();

        // Convert search result to json
        JsonArray sResult = new JsonArray();

        for (Collection c : songResults)
        {
            JsonObject singleSong = new JsonObject();
            singleSong.addProperty("idNum", c.getId());
            singleSong.addProperty("songName", c.getSongTitle());
            singleSong.addProperty("artistName", c.getArtistName());
            singleSong.addProperty("releaseName", c.getRelease().getName());
            sResult.add(singleSong);
        }
        System.out.println(sResult.toString());

        Server.byteSearchResult = sResult.toString().getBytes();
        return  Server.byteSearchResult.length;
    }
}
