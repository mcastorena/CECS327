package server.core;

import com.google.gson.JsonArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static server.core.Server.dfs;


public class SearchResultDispatcher extends Dispatcher implements DispatcherService {

    /**
     * Size of each fragment
     */
    static final int FRAGMENT_SIZE = 44100;

    /**
     * Gets a chunk of a given search result
     *
     * @param fragment The chunk corresponds to
     *                 [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getSearchResultChunk(Long fragment) throws IOException {
        byte buf[] = new byte[FRAGMENT_SIZE];

        System.out.println("SearchResultDispatcher is getting chunk");
        InputStream inputStream = new ByteArrayInputStream(Server.byteSearchResult);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();

        // Encode in base64 so it can be transmitted
        return Base64.getEncoder().encodeToString(buf);
    }

    /**
     * Gets the size of a SearchResult
     *
     * @param query Song being searched
     * @return Size of the SearchResult as a byte array, as an Integer
     */
    public Integer getSize(String query) throws Exception {

        // sResult returns as a JsonArray holding 2 JsonArray's, the first is the song search results/ 2nd is artist search results
        JsonArray sResult = dfs.search(query);

        if (sResult == null) {
            return 0;
        }

        System.out.println(sResult.toString());

        Server.byteSearchResult = sResult.toString().getBytes();
        return Server.byteSearchResult.length;
    }
}
