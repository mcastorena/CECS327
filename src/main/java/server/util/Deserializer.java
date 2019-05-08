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
 * <p>
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

    /**
     * A container of references for PLAYABLE songs (i.e. the song's mp3 file exists in the music directory).
     */
    private HashMap<Integer, String> playableSongs; // key: song release ID, value: mp3 file name

    /**
     * A dictionary of the user's music library (as Collection objects).
     */
    private HashMap<Integer, Collection> userPlaylistLibrary; // key: song release ID, value: corresponding Collection object

    /**
     * The song release IDs present in the music directory. Used for quick lookup of available music.
     */
    private HashSet<Integer> ownedIDs;

    /**
     * Initializes the Deserializer and loads the music files from the filesystem.
     */
    public Deserializer() {
        userPlaylistLibrary = new HashMap<>();
    }

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

                if (fileName.equalsIgnoreCase("music")) {
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
     * @param reader - The parent method's JsonReader
     * @return A deserialized Playlist object
     * @throws IOException
     */
    private Playlist deserializePlaylist(JsonReader reader) throws IOException {
        reader.beginObject(); // Playlist object '{'
        String playlistName = reader.nextName();
        reader.beginArray(); // "playlists" array '['

        // Read each ID in the playlist
        ArrayList<Collection> songs = new ArrayList<>();
        while (reader.hasNext()) {
            songs.add(deserializePlaylistItem(reader));

        }

        reader.endArray(); // "playlists" ']'
        reader.endObject(); // Playlist object '}'

        Playlist playlist = new Playlist(playlistName);

        // Add each Collection referenced (as an ID) in the playlist JsonArray to a Playlist.
        for (Collection song : songs) {
            // Retrieve Collection from a pre-loaded HashMap

            if (song != null) {
                userPlaylistLibrary.put(Math.toIntExact(song.getId()), song);
                playlist.addToPlaylist(song);
            } else
                System.out.println("Deserializer.deserializePlaylist() - Failed to add Collection ID: " + song.getId() + " to Playlist");
        }
        return playlist;
    }

    /**
     * Deserializes the JSON object Song into a Collection
     *
     * @param r - JSON Reader that contains the Song
     * @return Song as a Collection
     * @throws IOException
     */
    public Collection deserializePlaylistItem(JsonReader r) throws IOException {
        r.beginObject();

        Release release = new Release();
        r.nextName(); // release
        r.beginObject();
        r.nextName(); // id
        release.setId(r.nextLong());
        r.nextName(); // name
        release.setName(r.nextString());
        r.endObject();

        Artist artist = new Artist();
        r.nextName(); // artist
        r.beginObject();
        r.nextName(); // name
        artist.setName(r.nextString());
        r.endObject();

        Song song = new Song();
        r.nextName(); // song
        r.beginObject();
        r.nextName(); // name
        song.setTitle(r.nextString());
        r.endObject();

        r.endObject();

        return new Collection(release, artist, song);
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
    public HashMap<Integer, Collection> getUserPlaylistLibrary() {
        return userPlaylistLibrary;
    }

    /**
     * Get collection (song/artist/release) from a single json object.
     *
     * @param value A json object representing a single song/artist/release.
     * @return A Collection representing the json object.
     */
    public Collection jsonToCollection(JsonObject value) {

        try {
            Gson gson = new Gson();

            Release release = gson.fromJson(value.get("release"), Release.class);
            Artist artist = gson.fromJson(value.get("artist"), Artist.class);
            Song song = gson.fromJson(value.get("song"), Song.class);

            return new Collection(release, artist, song);
        } catch (Exception e) {
            LOGGER.error("ERROR: jsonToCollection >> " + e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a JSON object to a Collection
     *
     * @param value - JSON Object to be converted
     * @return Song within the JSON as a Collection
     */
    public Collection jsonToCollectionLightWeight(JsonObject value) {

        try {
            Long idNum = value.get("idNum").getAsLong();
            String songName = value.get("songName").getAsString();
            String artistName = value.get("artistName").getAsString();
            String releaseName = value.get("releaseName").getAsString();

            Release release = new Release();
            release.setName(releaseName);
            release.setId(idNum);

            Song song = new Song();
            song.setTitle(songName);

            Artist artist = new Artist();
            artist.setName(artistName);

            return new Collection(release, artist, song);

        } catch (Exception e) {
            LOGGER.error("ERROR: jsonToCollectionLightWeight >> " + e);
            e.printStackTrace();
        }

        return null;
    }
}