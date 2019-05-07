package server.chord;
/**
* Chord implements Chord P2P
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   03-3-2019
*/

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;
import java.io.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.jshell.execution.RemoteExecutionControl;
import server.chord.ChordMessageInterface;
import server.chord.RemoteInputFileStream;
import server.model.Collection;

import static server.core.Server.d;

/**
 * Chord extends from UnicastRemoteObject to support RMI.
 * It implements the ChordMessageInterface
 *
 */
public class Chord extends UnicastRemoteObject implements ChordMessageInterface
{
    // Numbers of fingers
    public static final int M = 7;

     // rmi registry for lookup the remote objects.
    Registry registry;
    // Successor peeer
    ChordMessageInterface successor;
    // Predecessor peeer
    ChordMessageInterface predecessor;
    // array of fingers
    ChordMessageInterface[] finger;
    // it is used to keep the fingers updated
    int nextFinger;
    // GUID
    long guid;

    int chordSize = 0;

    // path prefix
    String prefix;

    HashMap<Long, TreeMap<String, ArrayList>> myPageMap = new HashMap<>();



/**
 * Constructor of the Chord.
 * <p>
 * The function is used to debug if the ring is correctly formed
 * </p>
  *
 * @param  port it is the port where it listen. If the port is being used
 * for another process, it throw RemoteException.
 * @param guid the global unique id of the peer.
 */
    public Chord(int port, long guid) throws RemoteException {
        int j;
        // Initialize the variables
        prefix = "." + File.separator + guid + File.separator + "repository"+ File.separator;      // Local repository for each peer
	    finger = new ChordMessageInterface[M];
        for (j=0;j<M; j++){
	       finger[j] = null;
     	}
        this.guid = guid;
	    nextFinger = 0;
        predecessor = null;
        successor = this;
        Timer timer = new Timer();
        // It sets the timer to self stabilize the chord when nodes leave or join
        timer.scheduleAtFixedRate(new TimerTask() {
	    @Override
	    public void run() {
            stabilize();
            fixFingers();
            checkSuccessor();
            checkPredecessor();
            }
        }, 1000, 1000);   // Executes every second
        try{
            // create the registry and bind the name and object.
            System.out.println(guid + " is starting RMI at port="+port);
            registry = LocateRegistry.createRegistry( port );
            registry.rebind("Chord", this);
        }
        catch(RemoteException e){
	       throw e;
        }
    }



/**
 * return true if the key is in the open interval (key1, key2)
 */
    public Boolean isKeyInOpenInterval(long key, long key1, long key2)
    {
      if (key1 < key2)
          return (key > key1 && key < key2);
      else
          return (key > key1 || key < key2);
    }

    public int getChordSize() throws RemoteException{
        return chordSize;
    }


/**
 *return true if the key is in the semi-open interval (key1, key2]
 */
    public Boolean isKeyInSemiCloseInterval(long key, long key1, long key2)
    {
      return isKeyInOpenInterval(key, key1, key2) || key == key2;
    }

/**
 *  put a file in the repository
  * @param guidObject  GUID of the object to store
  * @param stream  File to store
 */
    public void put(long guidObject, RemoteInputFileStream stream) throws RemoteException {
        stream.connect();
        try {
          String fileName = prefix + guidObject;
          FileOutputStream output = new FileOutputStream(fileName);
          while (stream.available() > 0)
              output.write(stream.read());
          output.close();
      }
      catch (IOException e) {
          System.out.println(e);
      }
    }

    /**
 *  put a text in guidObject
 * @param guidObject  GUID of the object to store
 * @param text text to store
 */
    public void put(long guidObject, String text) throws RemoteException {
        try {
          String fileName = prefix + guidObject;
          FileOutputStream output = new FileOutputStream(fileName);
          output.write(text.getBytes());
          output.close();
      }
      catch (IOException e) {
          System.out.println(e);
      }
    }
/**
 * return guidObject
  * @param guidObject  GUID of the object to return
 */
    public RemoteInputFileStream get(long guidObject) throws RemoteException {
        RemoteInputFileStream file = null;
        try {
            file = new RemoteInputFileStream(prefix + guidObject);
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return file;
    }


/**
 * return len bytes of guidObject from offset
  * @param guidObject  GUID of the object to return
 */
    public byte[] get(long guidObject, long offset, int len) throws RemoteException {
        byte[] buf = new byte[len];

        try {
            File file = new File(prefix + guidObject);
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.skip(offset);
            inputStream.read(buf);
            inputStream.close();
        } catch (IOException e)
        {
            throw(new RemoteException(String.format("File %s does not exists", prefix+guidObject)));
        }
        return buf;
    }


/**
 * deletes a file with guidObject from the repository
 * @param guidObject  GUID of the object to delete
 */
    public void delete(long guidObject) throws RemoteException {
        File file = new File(prefix + guidObject);
        file.delete();
    }

/**
 * returns the id of the peer
 */
    public long getId() throws RemoteException {
        return guid;
    }

/**
 * It is used to detect that the peer is still alive
 * <p>
 * return true
 */
    public boolean isAlive() throws RemoteException {
	    return true;
    }

/**
 * return the predecessor
 * <p>
 * return the Chord Interface of the predecessor
 */
    public ChordMessageInterface getPredecessor() throws RemoteException {
	    return predecessor;
    }

    /**
     * return the predecessor
     * <p>
     * return the Chord Interface of the predecessor
     */
    public ChordMessageInterface getSuccessor() throws RemoteException {
        return successor;
    }

/**
 * locates the successor of key
 * <p>
 * @param key
 * return the Chord Interface of the successor of key
 */
    public ChordMessageInterface locateSuccessor(long key) throws RemoteException {
	    if (key == guid)
            throw new IllegalArgumentException("Key must be distinct that  " + guid);
	    if (successor.getId() != guid)
	    {
	      if (isKeyInSemiCloseInterval(key, guid, successor.getId()))
	        return successor;
	      ChordMessageInterface j = closestPrecedingNode(key);

          if (j == null)
	        return null;
	      return j.locateSuccessor(key);
        }
        return successor;
    }

/**
 * Returns the closest preceding node for the key
 * <p>
 * @param key
 * return the Chord Interface of the closet preceding node
 */
    public ChordMessageInterface closestPrecedingNode(long key) throws RemoteException {
        if(key != guid) {
            int i = M - 1;
            while (i >= 0) {
                try{

                    // It verifies from the largest interval
                    if(finger[i] != null && isKeyInSemiCloseInterval(finger[i].getId(), guid, key)) {
                        if(finger[i].getId() != key)
                            return finger[i];
                        else {
                            return successor;
                        }
                    }
                }
                catch(Exception e)
                {
                    // Skip ;
                }
                i--;
            }
        }
        return successor;
    }

/**
 * It joins the ring in the peer (ip,port). The peer must exist
 * <p>
 * @param ip of the peer
 * @param port of the peer
 */
    public void joinRing(String ip, int port)  throws RemoteException {
        try{
            System.out.println("Get Registry to joining ring");
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ChordMessageInterface chord = (ChordMessageInterface)(registry.lookup("Chord"));
            predecessor = null;
            successor = chord.locateSuccessor(this.getId());
            System.out.println("Joining ring");
        }
        catch(RemoteException | NotBoundException e){
            successor = this;
        }
    }

    /**
 * It joins the ring in the peer ChordMessageInterface. The peer must exist
 * <p>
 * @param s is the successor
 */
    public void joinRing(ChordMessageInterface s)  throws RemoteException {
            predecessor = null;
            successor = s;
    }

/**
 *  If the successor fails, it tries to handle the failure using the
 * first finger available
 * <p>
 */
    public void findingNextSuccessor()
    {
        int i;
        successor = this;
        for (i = 0;  i< M; i++)
        {
            try
            {
                if (finger[i].isAlive())
                {
                    successor = finger[i];
                }
            }
            catch(RemoteException | NullPointerException e)
            {
                finger[i] = null;
            }
        }
    }

/**
 * Stabilizes the chord
 * <p>
 * It verifies if the peer is in the right interval. If it is not
 * in the interval, it corrects the interval
 * This method executed by the timer.
 */
    public void stabilize() {
      try {
          if (successor != null)
          {
              ChordMessageInterface x = successor.getPredecessor();

              // It verifies if the predecessor is in the correct interval
              // x.getId() != this.getId() is used for for the trivial case
              // where only one peer exists
              if (x != null && x.getId() != this.getId() && isKeyInOpenInterval(x.getId(), this.getId(), successor.getId()))
              {
                  successor = x;
              }
              // The if statament is to handle the trivial case where only
              // one peer exists
              if (successor.getId() != getId())
              {
                  // We notified the successor that there It verifies if the predecessor is in the correct interval
                  successor.notify(this);
              }
          }
      } catch(RemoteException | NullPointerException e1) {
          findingNextSuccessor();

      }
    }

/**
 * A node notifies that it has a new predecessor j. It also moves all the
 * files that the predecessor must handle
 * </p>
 * @param j the new predecessor
 */
    public void notify(ChordMessageInterface j) throws RemoteException {
         if (predecessor == null || (predecessor != null
                    && isKeyInOpenInterval(j.getId(), predecessor.getId(), guid)))
             predecessor = j;
            try {
                File folder = new File(prefix);
                // It reads all the files in repository
                File[] files = folder.listFiles();

                for (File file : files) {
                    try{
                        long guidObject = Long.valueOf(file.getName());
                        // If the guidObject is less than the new predecessor
                        if(!isKeyInSemiCloseInterval(guidObject, j.getId(), getId())) {
                            predecessor.put(guidObject, new RemoteInputFileStream(file.getPath(), true));

                        }
                    }
                    catch (NumberFormatException e)
                    {
                        //skip;
                    }
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                //happens sometimes when a new file is added during the loop
            } catch (IOException e) {
            e.printStackTrace();
        }

    }

/**
 * Fixes the fingers
 * <p>
 * Every time that is executed is fixing the Finger nextFinger.
 * This method executed by the timer.
 */
    public void fixFingers() {

        long id= guid;

        try {
            if (successor != null && successor.getId() != getId())
            {
                // The finger is at distance 2^(nextFinger) of this.getId()
                // We use a shift to the left to perform the operation
                long nextId = this.getId() + (1<< (nextFinger+1));
                finger[nextFinger] = locateSuccessor(nextId);

                // The same process cannot be a finger
                if (finger[nextFinger].getId() == guid)
                    finger[nextFinger] = null;
                else
                    nextFinger = (nextFinger + 1) % M;
            }
            else{
                if (successor != null)
                {
                    finger[nextFinger] = null;
                    nextFinger = (nextFinger + 1) % M;
                }
            }
        }
        catch(RemoteException | NullPointerException e){
             //System.out.println(e.message());
             e.printStackTrace();
        }
    }

 /**
 * It checks if the predecessor is still alive.
 * <p>
 * It checks if the predecessor is still alive. If the predecessor
 * is not present it sets its predecessor to null. This method executed
 * by the timer.
 */
    public void checkPredecessor() {
      try {
          if (predecessor != null && !predecessor.isAlive())
              predecessor = null;
      }
      catch(RemoteException e)
      {
          predecessor = null;
      }
    }

       /**
 * It checks if the successor is still alive.
 * <p>
 * It checks if the successor is still alive. If the succesor
 * is not present it joins its successor. This method executed
 * by the timer.
 */
    public void checkSuccessor() {
      try {
          successor.isAlive();
      }
      catch(Exception e)
      {
            successor = null;
      }


         if (successor == null)
         {
                try{
                      for (int i=1; i<M; i++)
                      {
                        joinRing(finger[i]);
                        break;
                      }
                }catch(Exception e)
                {

                }
        }
    }

/**
 * Leaves the ring. It move all files to the successor
 */
    public void leave()  {
        if (predecessor == null) return;
        try
        {
            ChordMessageInterface suc = successor;
            if (suc != null && suc.getId() != getId())
            {
                successor = null;
                predecessor.joinRing(suc);
                predecessor = null;

                try {
                    File folder = new File(prefix);
                    // It reads all the files in repository
                    File[] files = folder.listFiles();

                    for (File file : files) {
                        try{
                            long guidObject = Long.valueOf(file.getName());
                            suc.put(guidObject, new RemoteInputFileStream(file.getPath()));
                            //file.delete();
                        }
                        catch (Exception   e)
                        {
                            //skip;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    //happens sometimes when a new file is added during the loop
                }
            }
        }
        catch (RemoteException e)
        {

        }
    }

/**
 * Prints the successor, predecessor and fingers if they are not null.
 * <p>
 * The function is used to debug if the ring is correctly formed
 * </p>
 */
    public String print()
    {
        int i;
        try {
            String out = "";
            if (successor != null)
                out = "successor "+ successor.getId();
            if (predecessor != null)
                out = out + "\npredecessor "+ predecessor.getId();
            for (i=0; i<M; i++)
            {
                try {
                    if (finger[i] != null)
                        out = out + "\nFinger "+ i + " " + finger[i].getId();
                } catch(NullPointerException e)
                {
                    System.out.println("Cannot retrive id of the finger " + i);
                }
            }
            return out;
        }
        catch(RemoteException e){
	       System.out.println("Cannot retrive id of successor or predecessor");
        }
        return null;
    }


    public String getPrefix()
    {
        return prefix;
    }
    /**
     *
     * @param source - GUID of the chord peer that initiates onChordSize
     * @param n - number of nodes counted, init 1
     * @return n - the number of nodes in the chord
     */
    public void onChordSize(Long source, int n) throws RemoteException {

        if(source != this.guid){
            this.successor.onChordSize(source, ++n);
        }
        else {
            this.chordSize = n;
        }
        // When source == this.guid then all nodes in the chord have been counted

    }

    /**
     * Store tree in the page in order
     * @param page - Page receiving TreeMap Data
     */
    public void bulk(DFS.PagesJson page, String file) throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try {
            long pageGuid = page.getGUID();
            if(myPageMap.get(pageGuid) != null) {
                    put(pageGuid, gson.toJson(myPageMap.get(pageGuid)));
                    myPageMap.remove(pageGuid);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: Chord.bulk ");
            e.printStackTrace();

        }
    }

    /**
     * Runs map for mapreduce
     * @param pageGuid - Page being mapped
     * @param mapper - Mapper object performing the mapping
     * @param coordinator - DFS coordinating the mapreduce (this)
     * @param file - Name of the file being mapped
     * @throws Exception
     */
    public void mapContext(long pageGuid, MapReduceInterface mapper, IDFSInterface coordinator, String file) throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        RemoteInputFileStream rifs = get(pageGuid);
        rifs.connect();
        JsonArray page = gson.fromJson(new JsonReader(new InputStreamReader(rifs)), JsonArray.class);
        for(int i = 0; i < page.size(); i++){
            int index = i;
            JsonObject value = (JsonObject) page.get(index);
            mapper.map(Integer.toString(index), value, coordinator, this, file);
        }
        coordinator.onPageComplete(file);
    }


    /**
     * Runs reduce for mapreduce
     * @param pageGuid - GUID for page being reduced
     * @param reducer - Mapper object performing the reduce
     * @param coordinator - DFS coordinating the reduce (this)
     * @param file - Name of the file being reduced
     * @throws IOException
     */
    public void reduceContext(long pageGuid, MapReduceInterface reducer, DFS coordinator, String file) throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();


        try {
            RemoteInputFileStream rifs = get(pageGuid);
            rifs.connect();
            TreeMap<String, ArrayList> page = gson.fromJson(new JsonReader(new InputStreamReader(rifs)), TreeMap.class);
            if(page != null) {
                Set<Map.Entry<String, ArrayList>> entrySet = page.entrySet();


                for (Map.Entry<String, ArrayList> entry : entrySet) {
                    reducer.reduce(entry.getKey(), entry.getValue(), coordinator, this, file);
                }
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        coordinator.onPageComplete(file);
    }

    @Override
    public void emit(String key, JsonElement value, IDFSInterface context, String file) throws Exception {
        key = key.toUpperCase();

        DFS.FilesJson metadata = context.readMetaData();

        DFS.FileJson fj = null;
        for(int i = metadata.file.size() - 1; i >= 0 ; i--){
            fj = metadata.file.get(i);
            if(fj.getName().equals(file)){
                break;
            }
        }
        for(int i = 0; i < fj.numberOfPages - 1; i++) {
            DFS.PagesJson page1 = fj.pages.get(i);
            DFS.PagesJson page2 = fj.pages.get(i + 1);

            String indexString = new String(context.getIndex());

            int keyLetter1;
            int keyLetter2;

            try {
                keyLetter1 = indexString.indexOf(key.charAt(0));
                keyLetter2 = indexString.indexOf(key.charAt(1));
            } catch (StringIndexOutOfBoundsException e) {
                break;
            }


            int pg1Letter1 = indexString.indexOf(page1.lowerBoundInterval.charAt(0));
            int pg2Letter1 = indexString.indexOf(page2.lowerBoundInterval.charAt(0));
            int pg1Letter2 = indexString.indexOf(page1.lowerBoundInterval.charAt(1));
            int pg2Letter2 = indexString.indexOf(page2.lowerBoundInterval.charAt(1));

            if ((keyLetter1 == pg1Letter1 && (keyLetter2 >= pg1Letter2 || key.charAt(1) == ' ')) ||
                    (keyLetter1 == pg2Letter1 && keyLetter2 < pg2Letter2) ||
                    (keyLetter1 > pg1Letter1 && keyLetter1 < pg2Letter1))
            {
                ChordMessageInterface peer = locateSuccessor(page1.getGUID());
                peer.addKeyValue(key, value.toString(), file, page1.getGUID());
                break;
            } else if (i == fj.numberOfPages - 2) {
                ChordMessageInterface peer = locateSuccessor(page2.getGUID());
                peer.addKeyValue(key, value.toString(), file, page2.getGUID());
            }
        }

    }

//    /**
//     * Adds key, value pair to TreeMap data structure
//     * @param key - Key value in string format
//     * @param value - Content of entry being mapped (data)
//     */
    public void addKeyValue(String key, String valueString, String filename, long guid) throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonParser parser = new JsonParser();

        JsonElement value = null;
        if(filename.endsWith(".map"))
            value = parser.parse(valueString).getAsJsonObject();
        else
            value = parser.parse(valueString).getAsJsonArray();

        TreeMap<String, ArrayList> myMap = null;
        try {
            myMap = myPageMap.get(guid);
        } catch(Exception e)
        {
          e.printStackTrace();
        }

        if(myMap == null)
        {
            myPageMap.put(guid, new TreeMap<>());
            myMap = myPageMap.get(guid);
        }

        if(filename.endsWith(".map")) {
            if (!myMap.containsKey(key)) {                            // If key is not in map, add an entry
                ArrayList tmpList = new ArrayList();
                myMap.put(key, tmpList);
            }
            myMap.get(key).add(value);                              // Add value to map
        }
        else
        {
            ArrayList values = gson.fromJson(value, ArrayList.class);
            myMap.put(key, values);
        }

    }


}
