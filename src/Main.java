import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static MusicPlayer musicPlayer;
    private static mp3Player instance;

    public static void main(String[] args) {

        SearchView searchView;
        User loggedInUser;  // TODO: Reference later

        loggedInUser = testData();  // TODO: Authentic user from Sign-In


        // TODO: If not a user, DON'T LET THEM IN, i.e., re-prompt for credentials
        if (loggedInUser != null) {

            // Authentic user -> grab their Playlists
            Profile userProfile = new Profile();
            HashMap<String, Playlist> usersPLs;

            try {
                FileReader fr = new FileReader("user.json");
                JsonReader jr = new JsonReader(fr);
                String usersField, usernameField, username, passwordField, password, emailField, email,
                        playlistsField, playlistTitle, songID;

                jr.beginObject();   // parse first '{'
                usersField = jr.nextName(); // parse key "users"
                jr.beginArray();    // parse '[' following "users" : array of user objects
                jr.beginObject();   // parse '{' for the first obj in the "users" array
                usernameField = jr.nextName();  // key: "username"
                username = jr.nextString();     // value: "user"
                passwordField = jr.nextName();  // key: "password"
                password = jr.nextString();     // value: "pass"
                emailField = jr.nextName();     // key: "email"
                email = jr.nextString();        // value: "example@email.com"
                playlistsField = jr.nextName(); // key for the array of "playlists"
                jr.beginArray();    // parse '[' of the "playlists" array

                while (jr.hasNext()) {
                    jr.beginObject();   // parse '{'
                    playlistTitle = jr.nextName();
                    userProfile.addPlaylist(playlistTitle, new Playlist(playlistTitle));
                    System.out.println(playlistTitle);

                    jr.beginArray();    // parse '['
                    while (jr.hasNext()) {
                        songID = jr.nextString();    // each song ID in the playlist array
                        userProfile.getPlaylist(playlistTitle).addToPlaylist(songID);
                    }
                    System.out.println();
                    jr.endArray();  // parse ']' to close the playlist array
                    jr.endObject(); // parse '}' to close the "playlist" object in "playlists"
                }
                jr.endArray();  // Close playlists array
                jr.endObject(); // Close current user obj
                jr.endArray();  // Close users array
                jr.endObject(); // Marks the end of the JSON file

            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO: Show titles of playlists in `playlistsPane`
            usersPLs = userProfile.playlists;       // Get the users playlists
            System.out.println(usersPLs.keySet());  // get the playlists titles
            Object[] titles = usersPLs.keySet().toArray();  // Turn to an array
//            for (Object t : titles) {
//                System.out.println(t.toString());   // Get the title as a String
//            }


            //region Process JSON file
            Gson gson = new Gson();
            List<Collection> collection = new ArrayList<>();

            try {
                BufferedReader br = new BufferedReader(new FileReader("music.json"));
                JsonReader jsonReader = new JsonReader(br);

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
                        collection.add(new Collection(release, artist, song));
                    }
                }
                jsonReader.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //endregion JSON Processing

            searchView = new SearchView(collection);
            searchView.pack();
            searchView.setVisible(true);

            // Open player
            mp3Player myPlayer = mp3Player.getInstance();
            myPlayer.pack();
            myPlayer.showPlaylists(titles);
            myPlayer.setVisible(true);

            //region GUI Upgrade?
//        musicPlayer = MusicPlayer.getInstance();
//        musicPlayer.setCollection(collection);
//
//        musicPlayer.pack();
//        musicPlayer.setVisible(true);
            //endregion?
        }
    }

    public static User testData() {
        User testUser = new User("user", "example@email.com", "pass");

        return testUser;
    }
}