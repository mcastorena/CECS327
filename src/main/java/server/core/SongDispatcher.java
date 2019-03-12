package server.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

/**
* SongDispatcher is the core responsible for obtaining the songs
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/
public class SongDispatcher extends Dispatcher implements DispatcherService
{
    private static final int FRAGMENT_SIZE = 8192;

    private static final String MUSIC_FILE_PATH;
    static {
        String tmp = null;
        try {
            tmp =
                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("server/music")).getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MUSIC_FILE_PATH = tmp;
    }

    /**
    * getSongChunk: Gets a chunk of a given song
    * @param key: Song ID. Each song has a unique ID 
    * @param fragment: The chunk corresponds to 
    * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
    */
    public String getSongChunk(Long key, Long fragment) throws IOException {
        byte buf[] = new byte[FRAGMENT_SIZE];
        File file = new File(MUSIC_FILE_PATH + File.separator + key + ".mp3");
        System.out.println("SongDispatcher has found file: " + key + "\tStatus: " + file.exists());
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();

        return Base64.getEncoder().encodeToString(buf);
    }
    
    /**
    * getFileSize: Gets a size of the file
    * @param key: Song ID. Each song has a unique ID 
     */
    public Integer getFileSize(Long key) {
        File file = new File(MUSIC_FILE_PATH + File.separator + key + ".mp3");
        return (int)file.length();
    }
    
}
