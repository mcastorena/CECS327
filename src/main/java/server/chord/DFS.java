package server.chord;
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import server.core.Server;
import static server.core.Server.d;

import java.io.InputStream;


/* JSON Format

{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
} 
*/


public class DFS
{
    Date date = new Date();
    Long metadata;
    

    public class PagesJson
    {
        // guid = md5(filename+pagenumber)
        Long guid;
        Long size;

        Long creationTS;
        Long readTS;
        Long writeTS;

        int referenceCount;


        public PagesJson(Long g, Long s)
        {
            this.guid = g;
            this.size = s;
            this.referenceCount = 0;

            // Set timestamps
            this.creationTS = date.getTime();
            this.readTS = date.getTime();
            this.writeTS = date.getTime();
        }
        // getters

        /**
         * Returns the object's GUID
         * @return Object's GUID as Long
         */
        public Long getGUID(){
            return this.guid;
        }

        /**
         * Return object's size
         * @return Object's size as LONG
         */
        public Long getSize(){
            return this.size;
        }

        // setters

        /**
         * Set object's GUID
         * @param g - Long GUID created using md5 function
         */
        public void setGUID(Long g){
            this.guid = g;
        }

        /**
         * Set object's size
         * @param s - Long size that reflects the page's physical size on the disk
         */
        public void setSize(Long s){
            this.size = s;
        }

    };

    public class FileJson 
    {
        String name;
        Long   size;

        Long creationTS;
        Long readTS;
        Long writeTS;

        int numberOfPages;
        int referenceCount;

        ArrayList<PagesJson> pages;
        public FileJson(String n, Long s)
        {
         this.name = n;
         this.size = s;
         this.numberOfPages = 0;
         this.referenceCount = 0;
         pages = new ArrayList<PagesJson>();

         // Set timestamps
         this.creationTS = date.getTime();
         this.readTS = date.getTime();
         this.writeTS = date.getTime();
        }
        // getters

        /**
         * Return object name
         * @return Object's name
         */
        public String getName(){
            return this.name;
        }

        /**
         * Return object's siZe
         * @return Return the sum of the object's Pages's sizes
         */
        public Long getSize(){
            return this.size;
        }
        // setters

        /**
         * Set object's name
         * @param n - string to be set as object's name
         */
        public void setName(String n){
            this.name = n;
        }

        /**
         * Set object's size, called everytime a page is added
         * @param s
         */
        public void setSize(Long s){
            this.size = s;
        }

        /**
         * Update object's number of pages by getting it's pages array size
         */
        public void updateNumPages(){
            this.numberOfPages = pages.size();
        }

        /**
         * Increment object's reference count when object is in use
         */
        public void incrementRef(){
            this.referenceCount++;
        }

        /**
         * Decrement object's reference count when object is no longer in use
         */
        public void decrementRef(){
            this.referenceCount--;
        }

        /**
         * Increment object and it's page's refCount when in use
         * @param pageIndex - pageIndex for page in use
         */
        public void incrementRef(int pageIndex){
            this.referenceCount++;
            pages.get(pageIndex).referenceCount++;
        }

        /**
         *  Decrement object and it's page's refCount when in use
         * @param pageIndex - pageIndex for page in use
         */
        public void decrementRef(int pageIndex){
            this.referenceCount--;
            this.pages.get(pageIndex).referenceCount--;
        }

        /**
         * Add's a page this object. Update numberOfPages and object's size
         * @param p - page to be added
         * @param size - size of the page to be added
         */
        public void addPage(PagesJson p, Long size){
            this.pages.add(p);
            this.numberOfPages++;
            this.size+= size;
        }
    };
    
    public class FilesJson 
    {
         List<FileJson> file;
         public FilesJson() 
         {
            file = new ArrayList<FileJson>();
         }
        // getters

        /**
         * Return object's file list
         * @return - file - file list
         */
        public List<FileJson> getFileList(){
             return this.file;
        }
        // setters


        /**
         * Add a file to the files
         * @param f - file to be added
         */
        public void addFile(FileJson f){
             this.file.add(f);
        }
    };
    
    
    int port;
    Chord  chord;


    /**
     * Creates a md5 hash from string input
     * @param objectName - name of the object to be hashed
     * @return md5 hash in Long format
     */
    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
                e.printStackTrace();
                
        }
        return 0;
    }
    
    
    
    public DFS(int port) throws Exception
    {
        
        
        this.port = port;
        this.metadata = md5("Metadata");
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+ File.separator +"repository"+ File.separator));
        Files.createDirectories(Paths.get(guid+ File.separator +"tmp"+ File.separator));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                chord.leave();
            }
        });
        
    }


    /**
     * Join the chord
     * @param Ip - IP for the chord to be joined
     * @param port - port to join
     * @throws Exception
     */
    public void join(String Ip, int port) throws Exception
    {
        chord.joinRing(Ip, port);
        chord.print();
    }


    /**
     * Leave the chord
     * @throws Exception
     */
    public void leave() throws Exception
    {        
       chord.leave();
    }

    /**
     * Print the status of the peer in the chord
     * @throws Exception
     */
    public void print() throws Exception
    {
        chord.print();
    }

    /**
     * Read the metadata in the chord
     * @return FilesJson object containing all "files", or null if an exception is encountered
     * @throws Exception
     */
    public FilesJson readMetaData() throws Exception
    {
        FilesJson filesJson = null;
        long guid = md5("Metadata");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Try to read metadata if it does not exist create a new physical file for it
        try {
            //System.out.println("Read metadata starting");
            ChordMessageInterface peer = chord.locateSuccessor(guid);

            int len = 1024<<9;
            var bytes = peer.get(guid,0,len);

            String metadataraw = new String(bytes);

            Scanner scan = new Scanner(metadataraw);
            scan.useDelimiter("\\A");
            var reader = new JsonReader(new StringReader(metadataraw));
            reader.setLenient(true);
            filesJson= gson.fromJson(reader, FilesJson.class);
        }
        catch (Exception ex)
        {
            File metadata = new File(this.chord.prefix+guid);       // Create file object with filepath
            metadata.createNewFile();                                         // Create the physical file

            // Create initial data for metadata
            filesJson = new FilesJson();
            filesJson.addFile(new FileJson("Metadata", Long.valueOf(0)));   // Add metadata entry

            // Write data to metadata file
            writeMetaData(filesJson);

        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        return filesJson;
    }

    /**
     * Write the metadata back to the cord
     * @param filesJson - Object that will form the new metadata to be written
     * @throws Exception
     */
    public void writeMetaData(FilesJson filesJson) throws Exception
    {
        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        peer.put(guid, gson.toJson(filesJson));
    }

    /**
     * Changes the name of a file in the system
     * @param oldName Name of file to be edited
     * @param newName New name of file
     * @throws Exception
     */
    public void move(String oldName, String newName) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find and edit file
        for(int i = 0; i < metadata.file.size(); i++){
            if(metadata.file.get(i).getName().equals(oldName)){
                System.out.println("Move file found!\n");
                metadata.file.get(i).incrementRef();                    // Increment referenceCount
                writeMetaData(metadata);                                // Update metadata with new reference count
                metadata.file.get(i).name = newName;                    // Change old file name to newName
                metadata.file.get(i).writeTS = date.getTime();          // Update write timestamp
                metadata.file.get(i).decrementRef();                    // Decrement referenceCount
                writeMetaData(metadata);                                // Update metadata
                break;
            }
        }
    }


    /**
     * List the files in the chord
     * @return string containing the names of all files in the chord
     * @throws Exception
     */
    public String lists() throws Exception
    {
        StringBuilder listOfFiles = new StringBuilder("\nFiles:\n");                        // Initialize string to hold file names

        List<FileJson> myFiles = readMetaData().file;               // Get our list of files
        for(int i = 0; i < myFiles.size(); i++){
            listOfFiles.append(myFiles.get(i).name + "\n");              // Append each file name
        }
        listOfFiles.append("\n");
        return listOfFiles.toString();
    }

/**
 * create an empty file 
  *
 * @param fileName Name of the file
 */
    public void create(String fileName) throws Exception
    {
        // Create new file
        FileJson newFile = new FileJson(fileName, (long) 0);

        // Read Metadata
        FilesJson metadata = readMetaData();

        // Add new file to metadata
        metadata.file.add(newFile);

        // Write Metadata
        writeMetaData(metadata);

        System.out.println("File:\t"+fileName+" created!\n");
    }
    
/**
 * delete file 
  *
 * @param fileName Name of the file
 */
    public void delete(String fileName) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find and delete file
        boolean find = false;
        for(int i = 0; i < metadata.file.size(); i++){
            if(metadata.file.get(i).getName().equals(fileName)){

                // Delete physical pages for file from chord
                for(int j = 0; j < metadata.file.get(i).pages.size(); j++){
                    ChordMessageInterface peer = chord.locateSuccessor(metadata.file.get(i).pages.get(j).getGUID());        // Locate node where page is held
                    peer.delete(metadata.file.get(i).pages.get(j).getGUID());                       // Delete page
                }

                // Remove file from metadata
                metadata.file.remove(i);
                find = true;
            }
        }

        // Write Metadata if file was found, else return
        if(find){
            System.out.println("File:\t"+fileName+" deleted!\n");
            writeMetaData(metadata);
        }else return;

    }
    
/**
 * Read block pageNumber of fileName 
  *
 * @param fileName Name of the file
 * @param pageNumber number of block. 
 */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file
        boolean find = false;
        FileJson myFile = null;
        int fileIndex = 0;
        for(int i = 0; i < metadata.file.size(); i++){
            if(metadata.file.get(i).getName().equals(fileName)){
                fileIndex = i;
                myFile = metadata.file.get(i);
                if(myFile.numberOfPages == 0 || pageNumber >= myFile.numberOfPages){
                    return null;
                }

                metadata.file.get(i).incrementRef(pageNumber);                              // Increment file and page refCount
                metadata.file.get(i).readTS = new Date().getTime();                              // Update file read timestamp
                metadata.file.get(i).pages.get(pageNumber).readTS = new Date().getTime();         // Update page read timestamp
                writeMetaData(metadata);                                                    // Update metadata with refCount and timestamps
                find = true;
                break;
            }
        }

        // If file was found return page, else return null
        if(find){
            PagesJson myPage = myFile.pages.get(pageNumber);
            metadata.file.get(fileIndex).decrementRef(pageNumber);                          // Decrement refCount
            writeMetaData(metadata);                                                        // Update metadata for read and refCount
            ChordMessageInterface peer = chord.locateSuccessor(myPage.getGUID());
            //System.out.println(peer.getId());
            return peer.get(myPage.guid);
        }else return null;
    }

//    /**
//     * Retrieves a byte array representation of the queried page.
//     * @param filename Name of the file in the Metadata list.
//     * @param pageNumber The page to retrieve.
//     * @param unused_variable Unused.
//     * @return The page as a byte array.
//     * @throws Exception
//     */
    public byte[] read(String filename, int pageNumber, int unused_variable) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file

        var file = find(metadata.getFileList(), filename);
        if (file != null) {
            if(file.numberOfPages == 0 || pageNumber >= file.numberOfPages){
                return null;
            }

            var timestamp = new Date().getTime();
            var page = file.pages.get(pageNumber);
            file.incrementRef();

            file.readTS = timestamp;
            page.readTS = timestamp;

            var peer = chord.locateSuccessor(page.getGUID());

            writeMetaData(metadata);
            return peer.get(page.getGUID(), 0, 1024 << 9);
        }
        return null;
    }

    /**
     * Read the first page for a file
     * @param fileName Name of the file
     * @return First block of a file
     * @throws Exception
     */
    public RemoteInputFileStream head(String fileName) throws Exception
    {
        return read(fileName, 0);
    }

    /**
     * Read the last page for a file
     * @param fileName Name of the file
     * @return Last block of a file
     * @throws Exception
     */
    public RemoteInputFileStream tail(String fileName) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file
        boolean find = false;
        FileJson myFile = null;
        int tailPage = 0;
        int fileIndex = 0;
        for(int i = 0; i < metadata.file.size(); i++){
            if(metadata.file.get(i).getName().equals(fileName)){
                fileIndex = i;
                myFile = metadata.file.get(i);
                if(myFile.numberOfPages == 0){
                    return null;
                }
                tailPage = myFile.pages.size()-1;
                metadata.file.get(i).readTS = new Date().getTime();                              // Update file read timestamp
                metadata.file.get(i).pages.get(tailPage).readTS = new Date().getTime();             // Update page read timestamp
                metadata.file.get(i).incrementRef(tailPage);                                // Increment file and page referenceCount
                writeMetaData(metadata);                                                    // Update metadata with new RefCount and timestamps
                find = true;
                break;
            }
        }

        // If file was found return page, else return null
        if(find){
            PagesJson myPage = myFile.pages.get(tailPage);
            metadata.file.get(fileIndex).decrementRef(tailPage);                            // Decrement reference count
            writeMetaData(metadata);                                                        // Update metadata for read and refCount
            ChordMessageInterface peer = chord.locateSuccessor(myPage.getGUID());
            return peer.get(myPage.guid);
        }else return null;
    }
    
 /**
 * Add a page to the file                
  *
 * @param fileName Name of the file
 * @param data RemoteInputStream. 
 */
    public void append(String fileName, RemoteInputFileStream data) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file
        boolean find = false;
        int newPageIndex = 0;
        int fileIndex = 0;
        Long pageGUID = Long.valueOf(0);
        for(int i = 0; i < metadata.file.size(); i++){
            if(metadata.file.get(i).getName().equals(fileName)){
                fileIndex = i;
                metadata.file.get(i).incrementRef();                                            // Increment file refCount
                writeMetaData(metadata);                                                        // Write updated metadata
                newPageIndex = metadata.file.get(i).pages.size();
                pageGUID = md5(fileName+newPageIndex);
                metadata.file.get(i).addPage(new PagesJson(pageGUID, (long) data.total), (long) data.total);     // Add new page entry to file and update filesize
                metadata.file.get(i).writeTS = date.getTime();              // Update file write timestamp
                find = true;
                break;
            }
        }

        // If file was found append data and add to chord, else return
        if(find){
            //Find closest successor node and place data
            metadata.file.get(fileIndex).decrementRef();                                    // Decrement refcount
            ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
            writeMetaData(metadata);                                                        // Update metadata for write and refCount
            peer.put(pageGUID, data);

            if(fileName.contains("music")) {
                Thread.sleep(2000);
                d.updateMusicOnFileAdd();
                Server.updateSongList();
                System.out.println("Append Complete");
            }
            else
                System.out.println("Append Complete");
        }else return;
    }

    /**
     * Appends a string marked by single quotes (') as a page to the target file
     * @param filename The file to add a page to.
     * @param text The text to append.
     * @throws Exception
     */
    public void append(String filename, String text) throws Exception {
        FilesJson metadata = readMetaData();

        FileJson target = find(metadata.getFileList(), filename);
        if (target != null) {
            long timestamp = new Date().getTime();
            long pageGUID = md5(filename + timestamp);
            long text_len = (long)text.getBytes("UTF-8").length;

            PagesJson page = new PagesJson(pageGUID, text_len);

            target.writeTS = timestamp;                 // Update write timestamp
            target.addPage(page, text_len);     // Add a new page to the file
            target.incrementRef();

            ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
            peer.put(pageGUID, text);                   // Store the file on the Chord

            writeMetaData(metadata);
            target.decrementRef();
        }
        else {
            System.out.println("File '" + filename + "' not found.");
        }
    }

    /**
     * Helper method to find a filename in the Metadata.
     * @param collection Metadata's file list
     * @param filename The file filename
     * @return
     */
    private FileJson find(List<FileJson> collection, String filename) {
        for (var item : collection) {
            if (item.name.equals(filename)) {
                return item;
            }
        }
        return null;
    }
}


