import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class deserializes both the Music and User JSON into POJOs.
 * Contains data structures to hold:
 *  1. the song info database (musicDatabase),
 *  2. songs able to be played from the music folder (playableSongs),
 *  3. a lookup of song information by release ID, and
 *  4. the user's set of owned mp3s (from the music folder)
 */
public class Deserializer {
    /**
     * A list of ALL songs (from the Music JSON).
     */
    List<Collection> musicDatabase;

    /**
     * A container of references for PLAYABLE songs
     * (i.e. the song's mp3 file exists in the music directory).
     */
    HashMap<Integer,String> playableSongs; // key: song release ID, value: mp3 file name

    /**
     * A dictionary of the user's music library (as Collection objects).
     */
    HashMap<Integer,Collection> userLibrary; // key: song release ID, value: corresponding Collection object

    /**
     * The song release IDs present in the music directory.
     * Used for quick lookup of available music.
     */
    HashSet<Integer> ownedIDs;

    public Deserializer() throws IOException {
        playableSongs = new HashMap<>();
        userLibrary = new HashMap<>();
        ownedIDs = new HashSet<>();

        loadOwnedMusicIDs();
        findPlayableSongs();
    }

    // Loads the song IDs of OWNED music into a Set for lookup.
    private void loadOwnedMusicIDs() {
        File dir = new File("./music");
        for (File file : dir.listFiles()) {
            String filename = file.getName();

            if (filename.length() < 5)
                throw new ArrayIndexOutOfBoundsException("Deserializer.loadOwnedMusicIDs() - File name is too short.");

            // Trim the ".mp3" extension from file name
            String mp3Name = filename.substring(0, filename.length()-4);
            System.out.println(mp3Name);

            if (mp3Name.matches("[\\d]+"))
                ownedIDs.add(Integer.parseInt(mp3Name));
            else
                throw new NumberFormatException("Error: mp3 name isn't a song ID");
        }
    }

    public HashSet<Integer> getOwnedIDs () {
        return ownedIDs;
    }

    public HashMap<Integer, String> getPlayableSongs() {
        return playableSongs;
    }

    /**
     * Returns a dictionary of song information.
     * Key: song release ID
     * Value: the song info as a Collection
     * @return
     */
    public HashMap<Integer, Collection> getUserLibrary() {
        return userLibrary;
    }

    /**
     * Returns and stores a dictionary of songs that are playable
     * (i.e. song mp3 files that exist in the music directory).
     * @return A dictionary of existing songs.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public HashMap<Integer,String> findPlayableSongs() throws IOException {
        HashMap<Integer,String> playableSongs = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("music.json"));
             JsonReader jsonReader = new JsonReader(br)) {

            Gson gson = new Gson();

            List<Collection> collection = new ArrayList<>();

            // Clear the first "[" in the json file
            jsonReader.beginArray();

            // Parse the json file
            while (jsonReader.hasNext()) {
                Release release;
                Artist artist;
                Song song;

                // If the artist has already been parsed, don't add new object.
                boolean oldArtist = false;

                // Clear the first "{" for each json object
                jsonReader.beginObject();

                jsonReader.nextName();
                release = gson.fromJson(jsonReader, Release.class);

                jsonReader.nextName();
                artist = gson.fromJson(jsonReader, Artist.class);

                jsonReader.nextName();
                song = gson.fromJson(jsonReader, Song.class);

                jsonReader.endObject();

                // Check if artist was previously parsed
                for (int i = 0; i < collection.size(); i++) {
                    Artist artistInCollection = collection.get(i).getArtist();

                    if (artistInCollection.getName().equals(artist.getName())) {
                        oldArtist = true;
                        collection.add(new Collection(release, artistInCollection, song));
                        break;
                    }
                }
                if (!oldArtist) {
                    Collection newCollection = new Collection(release, artist, song);

                    /** Add the Collection to a list and a dictionary **/
                    collection.add(newCollection);

                    if (ownedIDs.contains( (int)newCollection.getId() ))
                        userLibrary.put((int)newCollection.getId(), newCollection);
                }
            }
            jsonReader.endArray();
        }

        this.playableSongs = playableSongs;
        return playableSongs;
    }

    /**
     * Deserializes a single Playlist from the "playlists" json array.
     * A helper function for deserializeUsers().
     * @param reader - The parent method's JsonReader
     * @return
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

        if (playableSongs == null)
            throw new NullPointerException("\"UsersDeserializer.deserializePlaylists() - HashMap playableSongs.\n");

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
     * Parses the User JSON file and returns a list of User objects.
     * @return
     * @throws IOException
     */
    public List<User> deserializeUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try {
            FileReader fr = new FileReader("user.json");
            JsonReader jr = new JsonReader(fr);

            jr.beginObject(); // file start '{'

            jr.nextName(); // "users"
            jr.beginArray(); // "users" '['

            /** Parse each user object in "users" **/
            while (jr.hasNext()) {
                jr.beginObject(); // user object '{'

                 jr.nextName(); // key: "username"
                String username = jr.nextString(); // value: "user"

                jr.nextName(); // key: "password"
                String password = jr.nextString(); // value: "pass"

                jr.nextName(); // key: "email"
                String email = jr.nextString(); // value: "example@email.com"

                Profile profile = new Profile();

                /** Parse the users's playlists and add to user Profile **/
                jr.nextName(); // "playlists"
                jr.beginArray(); // "playlists" '['
                while (jr.hasNext()) {
                    Playlist p = deserializePlaylist(jr);


                    profile.addPlaylist(p.getName(), p);
                }
                jr.endArray(); // "playlists" ']'


                /** Load the user and add to list **/
                User user = new User(username, email, password);
                user.setUserProfile(profile);
                users.add(user);

                jr.endObject(); // user object '}'
            }
            jr.endArray(); // "users" ']'
            jr.endObject(); // file end
        }
        finally {
            return users;
        }
    }
}