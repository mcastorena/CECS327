import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Serializes Users into JSON objects **/
class Serializer {
    /**
     * Serializes a Playlist into JSON format.
     * @param p - A Playlist object.
     * @return A JSON array of integers.
     */
    public JsonElement serialize(final Playlist p) {
        JsonArray ids = new JsonArray();
        for (Collection c : p.getSongList())
            ids.add( (int)c.getId() );

        JsonObject playlistJO = new JsonObject();
        playlistJO.add(p.getName(), ids);
        return playlistJO;
    }

    /**
     * Serializes a User into JSON format.
     * @param u - A User object
     * @return A JSON user object (see 'users.json')
     */
    public JsonElement serialize(final User u) {
        JsonObject userJO = new JsonObject();

        userJO.addProperty("username", u.getUsername());
        userJO.addProperty("password", u.getPassword());
        userJO.addProperty("email", u.getEmail());

        JsonArray playlistJA = new JsonArray();
        for (Playlist p : u.getUserProfile().getIterablePlaylists())
            playlistJA.add( serialize(p) );

        userJO.add("playlists", playlistJA);
        return userJO;
    }

    // Does nothing for now.
    private JsonElement serialize(final Profile p, final Type Profile, final JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    public void updateUsersJson(HashMap<String,User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("user.json"))) {

            Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

            // unnamed "users" array
            JsonArray usersJA = new JsonArray();
            for (User user : users.values()) {
                JsonObject userJO = (JsonObject)serialize(user);
                usersJA.add(userJO);
            }

            // named "users" array
            JsonObject usersJAO = new JsonObject();
            usersJAO.add("users", usersJA);

            gson.toJson(usersJAO, bw);
        }
    }

    public void updateUsersJson(List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("user.json"))) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            // unnamed "users" array
            JsonArray usersJA = new JsonArray();
            for (User user : users) {
                JsonObject userJO = (JsonObject)serialize(user);
                usersJA.add(userJO);
            }

            // named "users" array
            JsonObject usersJAO = new JsonObject();
            usersJAO.add("users", usersJA);

            gson.toJson(usersJAO, bw);
        }
    }
}
