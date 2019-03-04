package ServerMain; /**
* SongDispatcher is the main responsable for obtaining the songs 
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.io.*;
import java.util.Base64;


 public class SongDispatcher
{
    static final int FRAGMENT_SIZE = 8192;
    public SongDispatcher()
    {
        
    }
    
    /* 
    * getSongChunk: Gets a chunk of a given song
    * @param key: Song ID. Each song has a unique ID 
    * @param fragment: The chunk corresponds to 
    * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
    */
    public String getSongChunk(Long key, Long fragment) throws FileNotFoundException, IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("music").getPath() + "\\" + key + ".mp3");
        System.out.println("SongDispatcher has found file: "+key+"\tStatus: "+file.exists());
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
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("music").getPath() + "\\" + key + ".mp3");
        Integer total =  (int)file.length();

        return total;
    }
    
}
