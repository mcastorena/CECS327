package server.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegistrationDispatcher extends Dispatcher implements DispatcherService {
    private static final int FRAGMENT_SIZE = 8192;

    /**
     * register: return successful registration message if info is valid
     *
     * @param username: the username from client
     * @param password: the password from client
     */
    public String register(String username, String password) throws IOException {
        User user = Server.usersInfo.get(username + password);
        if (user != null) {
            // User has already been registered, return error
            return "User has already been registered, please try again.";
        } else {
            // Get user.json filepath
            URL userURL = RegistrationDispatcher.class.getResource("/server/user.json");
            Path path = Paths.get(URI.create(userURL.getPath()));

            // Read in current Json file
            String userJSON = new String(Files.readAllBytes(path), "UTF-8");
            JsonObject userFile = new Gson().fromJson(userJSON, JsonObject.class);

            // Get userlist
            JsonArray userList = userFile.get("userList").getAsJsonArray();

            //Prepare new user entry
            JsonObject newEntry = new JsonObject();
            newEntry.addProperty("username", username);
            newEntry.addProperty("password", password);
            newEntry.addProperty("email", "test@test.com");
            JsonArray welcomePlaylist = new JsonArray();
            // Add welcome playlist to new user
            JsonArray welcomePlaylistEntries = new JsonArray();
            welcomePlaylistEntries.add(41838);
            welcomePlaylistEntries.add(300848);
            JsonObject playListEntry = new JsonObject();
            playListEntry.add("Welcome", welcomePlaylistEntries);
            welcomePlaylist.add(playListEntry);
            newEntry.add("playlists", welcomePlaylist);
            //Add new user to userList
            userList.add(newEntry);
            // Put new userList into userFile JSON object
            userFile.add("userList", userList);

            // Write to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                FileWriter file = new FileWriter(String.valueOf(path));
                gson.toJson(userFile, file);
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Call server update
            Server.update();
            return "User: " + username + " has been successfully registered. You may now log in";
        }
    }
}