import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersDeserializer {
    private Playlist deserializePlaylist(JsonReader reader) throws IOException {
        reader.beginObject(); // Playlist object '{'
        String playlistName = reader.nextName();
        reader.beginArray(); // "playlists" array '['

        ArrayList<Integer> songIDs = new ArrayList<>();
        while (reader.hasNext())
            songIDs.add(reader.nextInt());

        reader.endArray(); // "playlists" '['
        reader.endObject(); // Playlist object '}'

        Playlist playlist = new Playlist(playlistName);
        // To-do: load song data into a HashMap
        HashMap<Integer, Collection> songmap = new HashMap<>();
        for (int id : songIDs) {
            if (songmap.containsKey(id)) {
                Collection c = songmap.get(id);
                if (c == null)
                    throw new NullPointerException("UsersDeserializer.load() - Song not found in library HashMap.\n");

                playlist.addToPlaylist(c);
            }
        }
        return playlist;
    }

    public List<User> deserialize() throws IOException {
        List<User> users = new ArrayList<>();
        FileReader fr = new FileReader("user.json");
        JsonReader jr = new JsonReader(fr);

        jr.beginObject(); // file start '{'

        String usersField = jr.nextName();
        jr.beginArray(); // users array

        while (jr.hasNext()) {
            jr.beginObject(); // user object '{'
            String usernameField = jr.nextName(); // key: "username"
            String username = jr.nextString(); // value: "user"
            String passwordField = jr.nextName(); // key: "password"
            String password = jr.nextString(); // value: "pass"
            String emailField = jr.nextName(); // key: "email"
            String email = jr.nextString(); // value: "example@email.com"

            Profile profile = new Profile();

            String playlistsField = jr.nextName(); // key for the array of "playlists"
            jr.beginArray(); // "playlists" '['
            while (jr.hasNext()) {
                Playlist p = deserializePlaylist(jr);
                profile.addPlaylist(p.getName(), p);
            }
            jr.endArray(); // "playlists" ']'

            User user = new User(username, email, password);
            user.setUserProfile(profile);
            users.add(user);

            jr.endObject(); // user object '}'
        }
        jr.endArray(); // "users" ']'
        jr.endObject(); // file end

        return users;
    }
}