package server.core;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static server.core.Server.dfs;

/**
 * SongDispatcher is the core responsible for obtaining the songs
 *
 * @author Oscar Morales-Ponce
 * @version 0.15
 * @since 02-11-2019
 */
public class SongDispatcher extends Dispatcher implements DispatcherService {

    /**
     * Size of each fragment
     */
    private static final int FRAGMENT_SIZE = 44100;

//    private static final String MUSIC_FILE_PATH;
//    static {
//        String tmp = null;
//        try {
//            tmp =
//                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("server/music.json")).getPath();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        MUSIC_FILE_PATH = tmp;
//    }

    /**
     * getSongChunk: Gets a chunk of a given song
     *
     * @param key      Song ID. Each song has a unique ID
     * @param fragment The chunk corresponds to
     *                 [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getSongChunk(Long key, Long fragment) throws IOException {
        byte buf[] = new byte[FRAGMENT_SIZE];

// The RemoteInputFileStream will work now that it has been fixed, so leaving this here for now.
//        RemoteInputFileStream rifs = null;
//        try {
//            rifs = dfs.read(key+".mp3", 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        rifs.connect();
//        InputStream inputStream = rifs;
//
//        inputStream.skip(fragment * FRAGMENT_SIZE);
//        inputStream.read(buf);
//        inputStream.close();

        try {
            // Need to dehardcode the pageNumber if we end up splitting the mp3 files into multiple pages.
            buf = dfs.read(key + ".mp3", 0, FRAGMENT_SIZE * Math.toIntExact(fragment), FRAGMENT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(buf);
    }

    /**
     * getFileSize: Gets a size of the file
     *
     * @param key Song ID. Each song has a unique ID
     */
    public Integer getFileSize(Long key) {
//        File file = new File(MUSIC_FILE_PATH + File.separator + key + ".mp3");
//        return (int)file.length();
        try {
            return dfs.getFileSize(key + ".mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
