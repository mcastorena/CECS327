package ServerMain;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Collection;
import model.SearchResult;
import utility.Search;


import java.io.*;
import java.util.Base64;
import java.util.List;


public class SearchResultDispatcher {
    static final int FRAGMENT_SIZE = 8192;
//    private String query;

    public SearchResultDispatcher()
    {

    }

    /*
     * getSearchResultChunk: Gets a chunk of a given search result
     * @param fragment: The chunk corresponds to
     * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getSearchResultChunk(Long fragment) throws IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];

        System.out.println("SearchResultDispatcher is getting chunk");
        InputStream inputStream = new ByteArrayInputStream(server.byteSearchResult);
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
        SearchResult searchResult = Search.search(query, server.songList);
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

        server.byteSearchResult = sResult.toString().getBytes();
        return  server.byteSearchResult.length;
    }
}
