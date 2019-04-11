package server.core;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.model.User;

import java.io.IOException;
import static server.core.Server.userList;
import server.util.Serializer;
import server.model.Profile;

/**
 * This class dispatches Registration information
 */
public class RegistrationDispatcher extends Dispatcher implements DispatcherService {

    /**
     * Fragment size
     */
    private static final int FRAGMENT_SIZE = 8192;

    /**
     * Return successful registration message if info is valid
     *
     * @param username the username from client
     * @param password the password from client
     */
    public String register(String username, String password) throws IOException {
        User user;

        user = Server.usersInfo.get(username + password);
        if (user != null) {

            // User has already been registered, return error
            return "User has already been registered, please try again.";
        } else {
            //Prepare new user entry
            JsonObject newEntry = new JsonObject();
            newEntry.addProperty("username", username);
            newEntry.addProperty("password", password);
            newEntry.addProperty("email", "test@test.com");
            JsonArray welcomePlaylist = new JsonArray();

            User newUser = new User(username, "test@test.com", password);

            // Add welcome playlist to new user
            JsonArray welcomePlaylistEntries = new JsonArray();
            welcomePlaylistEntries.add(41838);
            welcomePlaylistEntries.add(300848);
            JsonObject playListEntry = new JsonObject();
            playListEntry.add("Welcome", welcomePlaylistEntries);
            welcomePlaylist.add(playListEntry);
            newEntry.add("playlists", welcomePlaylist);


            newUser.setUserProfile(new Profile());
            userList.add(newUser);
            new Serializer().updateUsersJson(userList);
            System.out.println("Done");

            // Call server update
            Server.update();
            return "User: " + username + " has been successfully registered. You may now log in";
        }
    }
}