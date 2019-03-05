package utility;

import com.google.gson.*;
import model.Collection;
import model.Playlist;
import model.Profile;
import model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Serializes Users into JSON objects
 **/
public class Serializer {
    /**
     * Serializes a Playlist into JSON format.
     *
     * @param p - A Playlist object.
     * @return A JSON array of integers.
     */
    public static JsonElement serialize(final Playlist p) {
        JsonArray ids = new JsonArray();
        for (Collection c : p.getSongList())
            ids.add((int) c.getId());

        JsonObject playlistJO = new JsonObject();
        playlistJO.add(p.getName(), ids);
        return playlistJO;
    }

    /**
     * Serializes a User into JSON format.
     *
     * @param u - A User object
     * @return A JSON user object (see 'userList.json')
     */
    public JsonElement serialize(final User u) {
        JsonObject userJO = new JsonObject();

        userJO.addProperty("username", u.getUsername());
        userJO.addProperty("password", u.getPassword());
        userJO.addProperty("email", u.getEmail());

        JsonArray playlistJA = new JsonArray();
        for (Playlist p : u.getUserProfile().getIterablePlaylists())
            playlistJA.add(serialize(p));

        userJO.add("playlists", playlistJA);
        return userJO;
    }

    // Does nothing for now.
    private JsonElement serialize(final Profile p, final Type Profile, final JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    public void updateUsersJson(HashMap<String, User> users) throws IOException {
        try (PrintWriter writer =
                new PrintWriter(
                        new File(
                                getClass().getResource("/appdata/user.json")
                                        .getPath()))) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            // unnamed "userList" array
            JsonArray usersJA = new JsonArray();
            for (User user : users.values()) {
                JsonObject userJO = (JsonObject) serialize(user);
                usersJA.add(userJO);
            }

            // named "userList" array
            JsonObject usersJAO = new JsonObject();
            usersJAO.add("userList", usersJA);

            gson.toJson(usersJAO, writer);
        }
    }

    public void updateUsersJson(List<User> users) throws IOException {
        try (PrintWriter writer =
                new PrintWriter(
                        new File(
                                getClass().getResource("/appdata/user.json")
                                        .getPath()))) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            // unnamed "userList" array
            JsonArray usersJA = new JsonArray();
            for (User user : users) {
                JsonObject userJO = (JsonObject) serialize(user);
                usersJA.add(userJO);
            }

            // named "userList" array
            JsonObject usersJAO = new JsonObject();
            usersJAO.add("userList", usersJA);

            gson.toJson(usersJAO, writer);
        }
    }

    public static byte[] serialize(Collection collection) throws IOException {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos)){
            out.writeObject(collection);
            out.flush();
            return bos.toByteArray();
        }
    }
}
