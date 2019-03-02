import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class SearchResultDispatcher {
    static final int FRAGMENT_SIZE = 8192;
    private String query;
    public SearchResultDispatcher(String query)
    {
        this.query = query;
    }

    /*
     * getSearchResultChunk: Gets a chunk of a given search result
     * @param key: Song ID. Each song has a unique ID
     * @param fragment: The chunk corresponds to
     * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getSearchResultChunk(Long key, Long fragment) throws FileNotFoundException, IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];
        File file = new File(System.getProperty("user.dir") + "\\src\\" +key);
        System.out.println("SearchResultDispatcherhas found file: "+key+"\tStatus: "+file.exists());
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();
        // Encode in base64 so it can be transmitted
        return Base64.getEncoder().encodeToString(buf);
    }

    /*
     * getFileSize: Gets a size of the file
     * @param key: Song ID. Each song has a unique ID
     */
    public Integer getFileSize(Long key) throws FileNotFoundException, IOException
    {
        File file = new File(System.getProperty("user.dir") + "\\src\\" +key);
        Integer total =  (int)file.length();

        return total;
    }
}
