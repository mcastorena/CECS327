package server.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;
import server.chord.RemoteInputFileStream;
import server.model.Collection;
import server.model.*;

import java.io.*;
import java.util.*;

import static server.core.Server.dfs;

/**
 * This class deserializes both the Music and User JSON into POJOs.
 *
 * Contains data structures to hold:
 * 1. the song info database (musicDatabase),
 * 2. songs able to be played from the music folder (playableSongs),
 * 3. a lookup of song information by release ID, and
 * 4. the user's set of owned mp3s (from the music folder)
 */
public class Deserializer {

    /**
     * Logger for the Deserializer, declared static and final
     */
    private static final Logger LOGGER = Logger.getLogger(Deserializer.class);
    //public static final URL MUSIC_STREAM = Deserializer.class.getResource("/server/music.json");
    //public static final URL USER_STREAM = Deserializer.class.getResource("/server/user.json");
    //public static final String MUSIC_FOLDER = Deserializer.class.getResource("/server/music/").getPath();

    /**
     * A list of all songs (from the Music JSON).
     */
    private List<Collection> musicDatabase;

    /**
     * A container of references for PLAYABLE songs (i.e. the song's mp3 file exists in the music directory).
     */
    private HashMap<Integer, String> playableSongs; // key: song release ID, value: mp3 file name

    /**
     * A dictionary of the user's music library (as Collection objects).
     */
    private HashMap<Integer, Collection> userLibrary; // key: song release ID, value: corresponding Collection object

    /**
     * The song release IDs present in the music directory. Used for quick lookup of available music.
     */
    private HashSet<Integer> ownedIDs;

    /**
     * Initializes the Deserializer and loads the music files from the filesystem.
     */
    public Deserializer() {
        userLibrary = new HashMap<>();
        //ownedIDs = new HashSet<>();

        musicDatabase = deserializeSongsFromJson();
        //loadOwnedMusicIDs();
        initUserLibrary();
    }

//    /**
//     * Loads the music mp3 files that exist on the computer.
//     */
//    private void loadOwnedMusicIDs() {
//        File dir = new File(MUSIC_FOLDER.toString());
//        File[] files = Objects.requireNonNull(dir.listFiles(), "ERROR: Attempt to listFiles() from Music folder " +
//                "directory turned up NULL. Check to see that MUSIC_FOLDER points to a directory, not a file");
//        for (File file : files) {
//            String filename = trimMp3Extension(file.getName());
//
//            if (filename.matches("^\\deserializer{5,}$"))
//                ownedIDs.add(Integer.parseInt(filename));
//            else
//                throw new IllegalStateException("Invalid file format; Music file must be an mp3 with > 4 character");
//        }
//
//    }

//    private String trimMp3Extension(String filename) {
//        if(filename.substring(filename.length()-3).equals("mp3"))
//            return filename.substring(0, filename.length() - 4);
//        // TODO: technically fails if some file has any other extension, since it doesnt trim it
//        return filename;
//    }

    /**
     * Returns and stores a dictionary of songs that are playable
     * (i.e. song mp3 files that exist in the music directory).
     *
     * @return A dictionary of existing songs.
     * @throws FileNotFoundException
     * @throws IOException
     */
    private List<Collection> deserializeSongsFromJson() {
        List<Collection> songs = new ArrayList<>();

        try {
            Gson gson = new Gson();
            //BufferedReader br = new BufferedReader(new InputStreamReader(MUSIC_STREAM.openStream()));
            String dfsList = dfs.lists();
            Scanner dfsFileScan = new Scanner(dfsList);
            while (dfsFileScan.hasNext()) {
                String fileName = dfsFileScan.next();

                if(fileName.equalsIgnoreCase("music")) {
                    System.out.println(fileName);

                    int pageNumber = 0;
                    byte bytes[];
                    JsonArray bigArr = new JsonArray();
                    // Piece together the music.json from each page (as a byte array) in DFS
                    while ((bytes = dfs.read(fileName, pageNumber, 23 - 2392)) != null) {
                        String jsonStr = new String(bytes);
                        JsonArray smallArr = gson.fromJson(new JsonReader(new StringReader(jsonStr)), JsonArray.class);

                        for (var element : smallArr) {
                            bigArr.add(element);
                        }

                        pageNumber++;
                    }
                    for (JsonElement jsonElement : bigArr) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Release release = gson.fromJson(jsonObject.get("release"), Release.class);
                        Artist artist = gson.fromJson(jsonObject.get("artist"), Artist.class);
                        Song song = gson.fromJson(jsonObject.get("song"), Song.class);

                        songs.add(new Collection(release, artist, song));
//                        if (!songSet.contains(release.getName())) {
//                            songSet.add(release.getName());
//                        } else {
//                            System.out.printf("Duplicate song name '%s' found\n", release.getName());
//                            System.out.print("");
//                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR: deserializeSongsFromJson >> " + e);
            e.printStackTrace();
        } finally {
            return songs;
        }
    }

    /**
     * Initializes the userLibrary using the IDs of the songs in ownedIDs.
     */
    private void initUserLibrary() {
        for (Collection c : musicDatabase) {
            //if (ownedIDs.contains((int) c.getId())) {
            userLibrary.put((int) c.getId(), c);
            //}
        }
    }

    /**
     * Parses the User JSON file and returns a list of User objects.
     *
     * @return List containing User objects
     * @throws IOException
     */
    public List<User> deserializeUsers() {
        List<User> users = new ArrayList<>();
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(USER_STREAM.openStream()));
            RemoteInputFileStream rifs = dfs.read("users", 0);
            rifs.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) rifs));
            JsonReader jr = new JsonReader(br);

            jr.beginObject(); // file start '{'

            jr.nextName(); // "userList"
            jr.beginArray(); // "userList" '['

            /* Parse each user object in "userList" */
            while (jr.hasNext()) {
                jr.beginObject(); // user object '{'

                jr.nextName(); // key: "username"
                String username = jr.nextString(); // value: "user"

                jr.nextName(); // key: "password"
                String password = jr.nextString(); // value: "pass"

                jr.nextName(); // key: "email"
                String email = jr.nextString(); // value: "example@email.com"

                Profile profile = new Profile();

                /* Parse the userList's playlists and add to user Profile */
                jr.nextName(); // "playlists"
                jr.beginArray(); // "playlists" '['
                while (jr.hasNext()) {
                    Playlist p = deserializePlaylist(jr);


                    profile.addPlaylist(p.getName(), p);
                }
                jr.endArray(); // "playlists" ']'


                /* Load the user and add to list */
                User user = new User(username, email, password);
                user.setUserProfile(profile);
                users.add(user);

                jr.endObject(); // user object '}'
            }
            jr.endArray(); // "userList" ']'
            jr.endObject(); // file end
        } catch (Exception e) {
            LOGGER.error("Error while deserializing users: ", e);
        }

        return users;
    }

    /**
     * Deserializes a single Playlist from the "playlists" JSON array.
     * A helper function for the deserializeUsers function.
     *
     * @param reader The parent method's JsonReader
     * @return A deserialized Playlist object
     * @throws IOException
     */
    private Playlist deserializePlaylist(JsonReader reader) throws IOException {
        reader.beginObject(); // Playlist object '{'
        String playlistName = reader.nextName();
        reader.beginArray(); // "playlists" array '['

        // Read each ID in the playlist
        ArrayList<Integer> songIDs = new ArrayList<>();
        while (reader.hasNext())
            songIDs.add(reader.nextInt());

        reader.endArray(); // "playlists" ']'
        reader.endObject(); // Playlist object '}'

        Playlist playlist = new Playlist(playlistName);

        // Add each Collection referenced (as an ID) in the playlist JsonArray to a Playlist.
        for (int id : songIDs) {
            // Retrieve Collection from a pre-loaded HashMap
            Collection c = userLibrary.get(id);

            if (c != null)
                playlist.addToPlaylist(c);
            else
                System.out.println("Deserializer.deserializePlaylist() - Failed to add Collection ID: " + id + " to Playlist");
        }
        return playlist;
    }

    /**
     * Returns the song information in the Music JSON as an ArrayList.
     *
     * @return A List of songs, as Collections, from the music database.
     */
    public List<Collection> getMusicDatabase() {
        return musicDatabase;
    }

    /**
     * Returns a dictionary of the user's
     * owned mp3s in the music directory.
     *
     * @return A HashSet of the User's owned MP3's.
     */
    public HashSet<Integer> getOwnedIDs() {
        return ownedIDs;
    }

    /**
     * Returns a dictionary of songs that CAN be played
     * (the mp3 files exist in the music directory).
     * Key: song release ID
     * Value: the mp3 file name (without '.mp3' extension)
     *
     * @return A HashMap of songs that the User can play.
     */
    public HashMap<Integer, String> getPlayableSongs() {
        return playableSongs;
    }

    /**
     * Returns a dictionary of song information.
     * Key: song release ID
     * Value: the song info as a Collection
     *
     * @return A HashMap of songs in the User's Library
     */
    public HashMap<Integer, Collection> getUserLibrary() {
        return userLibrary;
    }

    /**
     * Updates the music database; called when a file is added to DFS.
     */
    public void updateMusicOnFileAdd()
    {
        musicDatabase = deserializeSongsFromJson();
        initUserLibrary();
    }

}