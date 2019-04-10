package server.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.chord.DFS;
import server.chord.RemoteInputFileStream;
import server.model.Collection;
import server.model.Profile;
import server.model.User;
import server.util.Deserializer;
import server.util.MusicJsonSplitter;
import server.util.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class represents the Music App's Server
 */
public class Server {

    /**
     * Port number to connect clients to
     */
    private static final int PORT_NUMBER = 2223;

    /**
     * Starting port for P2P clients
     */
    private static final int P2P_START_PORT = 2225;

    /**
     * Next port for the P2P client
     */
    public static int NEXT_PORT = P2P_START_PORT + 1;

    /**
     * Number of Nodes to be initialized
     */
    private static int INIT_NUM_NODES = 5;

    /**
     * Distributed File System
     */
    public static DFS dfs;

    /**
     * SearchResult information as a byte array
     */
    static byte[] byteSearchResult;

    /**
     * Playlists information as a byte array
     */
    static byte[] bytePlaylists;

    /**
     * Song and User deserializer for the Server
     */
    public static Deserializer deserializer;

    /**
     * List of active sessions
     */
    static List<User> currentSessions = new ArrayList<>();

    /**
     * List of Users
     */
    public static List<User> userList;

    /**
     * HashMap of User information; Key: username + user's password, Value: User object
     */
    public static HashMap<String, User> usersInfo = new HashMap<>();

    /**
     * List of Songs in the music database
     */
    static List<Collection> songList;

    /**
     * Server request cache
     */
    static Map<String, String> requestCache;

    /**
     * Updates userList and usersInfo after a new user registers
     */
    static void update() {
        songList = deserializer.getMusicDatabase();
        userList = deserializer.deserializeUsers();

        for (User u : userList) {
            if (usersInfo.containsValue(u)) {
                throw new IllegalStateException("Duplicate user found in usersInfo");
            }
            usersInfo.put(u.getUsername() + u.getPassword(), u);
        }
    }

    /**
     * Updates the song list by using the deserializer's getMusicDatabase method.
     */
    public static void updateSongList() {
        songList = deserializer.getMusicDatabase();
    }

    /**
     * Main method for starting the Server
     *
     * @param args N/A
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        dfs = new DFS(P2P_START_PORT);

        // Initialize chord with (n = INIT_NUM_NODES) nodes, need to sleep for the chord to stabalize
        for (int i = 0; i < INIT_NUM_NODES - 1; i++) {
            Thread.sleep(1000);
            DFS newdfs = new DFS(NEXT_PORT);
            NEXT_PORT++;
            newdfs.print();
            newdfs.join("127.0.0.1", P2P_START_PORT);
            newdfs.print();
//            Thread.sleep(1000);
        }
        Thread.sleep(2000);

        // Add user.json and mp3's to chord if they are not already there
        String metaFile = "users";
        String dfsList = dfs.lists();
        if (!dfsList.contains(metaFile)) {
            //Timer timer = new Timer();
            dfs.create("users");
            dfs.append("users", new RemoteInputFileStream(Server.class.getResource("/server/user.json").getPath()));
        }
        // Add music.json to the Chord if it's missing.
        String testfile = "musicChunks";
        if (!dfsList.contains(testfile)) {
            dfs.create(testfile);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            var chunks =
                    MusicJsonSplitter.getMusicJsonChunks(
                            Server.class.getResource("/server/music.json").getPath(),
                            100);

            System.out.println("Adding pages to music.json...");
            int i = 0;
            for (var chunk : chunks) {
                String jsonStr = null;
                try {
                    jsonStr = gson.toJson(chunk);
                    dfs.append(testfile, jsonStr);

                    System.out.println(String.format("Creating page [%deserializer/%deserializer]", ++i, chunks.size()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
            System.out.println("Done");
        }
//        metaFile = "300848.mp3";
//        if(!dfsList.contains(metaFile))
//        {
//            File dir = new File(Server.class.getResource("/server/music/").getPath());
//            File[] files = Objects.requireNonNull(dir.listFiles(), "ERROR: Attempt to listFiles() from Music folder " +
//                    "directory turned up NULL. Check to see that MUSIC_FOLDER points to a directory, not a file");
//            for (File file : files) {
//                if(file.getName() == metaFile) {
//                    dfs.create(file.getName());
//                    dfs.append(file.getName(), new RemoteInputFileStream(Server.class.getResource("/server/music/" + file.getName()).getPath()));
//                }
//            }
//        }

        requestCache = new HashMap<>();
        System.out.println("Deserializing music.json and user.json...");
        deserializer = new Deserializer();
        System.out.println("Done.");

        songList = deserializer.getMusicDatabase();
        userList = deserializer.deserializeUsers();

        for (User u : userList) {
            if (usersInfo.containsValue(u)) {
                throw new IllegalStateException("Duplicate user found in usersInfo");
            }
            usersInfo.put(u.getUsername() + u.getPassword(), u);
        }

        // Add a new user to user json, then update file in DFS
        System.out.println("Adding a new User \"April\" (without check for unique name) to user json...");
        var newUser = new User("April", "8", "2019");
        newUser.setUserProfile(new Profile());
        userList.add(newUser);
        new Serializer().updateUsersJson(userList);
        System.out.println("Done.");

        ServerCommunicationProtocol scp = new ServerCommunicationProtocol(PORT_NUMBER);
        scp.start();

////         DFS Interface
//        new Thread() {
//            public void run(){
//                try {
//                    new DFSCommand();
//                } catch(Exception e) {e.printStackTrace();}
//            }
//        }.start();
    }
}
