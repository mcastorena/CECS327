package server.util;

import com.google.gson.*;
import server.model.Collection;
import server.model.Playlist;
import server.model.Profile;
import server.model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static server.core.Server.dfs;

/**
 * Serializes Users into JSON objects.
 */
public class Serializer {

    /**
     * Serializes a Playlist into JSON format.
     *
     * @param p A Playlist object.
     * @return A JSON array of integers.
     */
    public static JsonElement serialize(final Playlist p) {
        JsonArray ids = new JsonArray();
        JsonObject playlistJO = new JsonObject();

        for (Collection c : p.getSongList())
            ids.add((int) c.getId());

        playlistJO.add(p.getName(), ids);

        return playlistJO;
    }

    /**
     * Serializes a User into JSON format.
     *
     * @param u A User object
     * @return A JSON user object (see 'userList.json')
     */
    public JsonElement serialize(final User u) {
        JsonObject userJO = new JsonObject();
        JsonArray playlistJA = new JsonArray();
        List<Playlist> playlists = u.getUserProfile()
                .getIterablePlaylists();

        userJO.addProperty("username", u.getUsername());
        userJO.addProperty("password", u.getPassword());
        userJO.addProperty("email", u.getEmail());

        if (playlists != null) {
            for (Playlist p : playlists)
                playlistJA.add(serialize(p));
        }
        userJO.add("playlists", playlistJA);

        return userJO;
    }

    // TODO: Does nothing for now.
    private JsonElement serialize(final Profile p, final Type Profile, final JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    /**
     * Updates the user.json file.
     *
     * @param users HashMap of all registered Users
     * @throws IOException
     */
    public void updateUsersJson(HashMap<String, User> users) throws IOException {
        try (PrintWriter writer =
                     new PrintWriter(
                             new File(
                                     getClass().getResource("/server/user.json")
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

    /**
     * Updates the user.json file.
     *
     * @param users List of all registered Users
     * @throws IOException
     */
    public void updateUsersJson(List<User> users) throws IOException {
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

        String jsonStr = gson.toJson(usersJAO);

        // Delete and recreate a file since modifying multiple pages is tricky
        try {
            dfs.delete("users");
            dfs.create("users");
            dfs.append("users", jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO:
     *
     * @param collection A Song
     * @return The Collection object as a byte-array
     * @throws IOException
     */
    public static byte[] serialize(Collection collection) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(collection);
            out.flush();

            return bos.toByteArray();
        }
    }

    /**
     * TODO: Implement and test...and figure out what this does
     *
     * @param object
     * @param size
     * @return
     */
    public static byte[] serialize(Object object, int size) {
        byte[] stream = null;

        try {
            var baos = new ByteArrayOutputStream();
            var oos = new ObjectOutputStream(baos);

            oos.writeObject(object);
            stream = Arrays.copyOfRange(baos.toByteArray(), 0, size);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }
}
