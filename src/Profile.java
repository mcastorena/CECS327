import java.util.HashMap;

// Access modifiers left out for now...
public class Profile {
    String username;
    HashMap<String, Playlist> playlists; // key: playlist name | value: the playlist

    Profile(String username) {
        this.username = username;
    }

    Profile(String username, HashMap<String, Playlist> playlists) {
        this.username = username;
        this.playlists = playlists;
    }

    /**
     * Adds a playlist to the profile's playlist collection.
     *
     * @param playlistName
     * @param playlist
     */
    void addPlaylist(String playlistName, Playlist playlist) {
        if (playlists.containsKey(playlistName)) {
            System.out.println("Playlist not added: \"" + playlistName + "\" already exists.");
            return;
        }

        playlists.put(playlistName, playlist);
        System.out.println("\"" + playlistName + "\" added to " + username + "'s playlists");
    }

}
