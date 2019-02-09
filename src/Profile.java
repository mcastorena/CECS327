import java.awt.image.BufferedImage;
import java.util.*;

// Access modifiers left out for now...
public class Profile {
    BufferedImage avatar;
    HashMap<String,Playlist> playlists; // key: playlist name | value: the playlist

    Profile() {
        avatar = null;
        playlists = new HashMap<>();
    }

    BufferedImage getAvatar() { return avatar; }

    /**
     * Sets the user's profile pic.
     * @param img The BufferedImage to set as an avatar.
     */
    void setAvatar(BufferedImage img) { avatar = img; }

    /**
     * Retrieve a playlist from the user's collection.
     * @param playlistName The name of the playlist.
     * @return The Playlist object, or null if it doesn't exist.
     */
    Playlist getPlaylist(String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("Playlist \"" + playlistName + "\" not found.");
            return null;
        }

        return playlists.get(playlistName);
    }

    public void addSongToPlaylist(Collection song, String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("Profile.addSongToPlaylist() - Playlist \"" + playlistName + "\" not found.");
            return;
        }

        // No data checks performed
        playlists.get(playlistName).addToPlaylist(song);
    }

    public HashMap<String, Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(final HashMap<String, Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * Adds a playlist to the profile's playlist collection.
     * @param playlistName The name of the playlist.
     * @param playlist The Playlist object to add.
     */
    void addPlaylist(String playlistName, Playlist playlist) {
        if (playlists.containsKey(playlistName)) {
            System.out.println("Playlist not added: \"" + playlistName + "\" already exists.");
            return;
        }

        playlists.put(playlistName, playlist);
        System.out.println("\"" + playlistName + "\" added to playlists");
    }

    /**
     * Removes a playlist from the profile's playlist collection.
     * @param playlistName
     */
    void removePlaylist(String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("Playlist not removed: \"" + playlistName + "\" not found.");
            return;
        }

        playlists.remove(playlistName);
        System.out.println("\"" + playlistName + "\" removed from playlists.");
    }

    /**
     * Return the Playlists map as an iterable List of Playlists.
     * @return
     */
    List<Playlist> getIterablePlaylists() {
        return new ArrayList<>(playlists.values());
    }
}
