package server.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents the Profile of a registered User
 * <p>
 * Access modifiers left out for now.
 */
public class Profile {

    /**
     * The User's Avatar
     */
    BufferedImage avatar;

    /**
     * HashMap of Playlists.
     * Key: Playlist name, Value: Playlist
     */
    HashMap<String, Playlist> playlists;

    /**
     * Constructor
     */
    public Profile() {
        avatar = null;
        playlists = new HashMap<>();
    }

    //region Getters and Setters
    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage img) {
        avatar = img;
    }

    public Playlist getPlaylist(String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("Playlist \"" + playlistName + "\" not found.");
            return null;
        }

        return playlists.get(playlistName);
    }

    public HashMap<String, Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(final HashMap<String, Playlist> playlists) {
        this.playlists = playlists;
    }
    //endregion

    /**
     * Adds a song to a Playlist
     *
     * @param song         Collection object representing the song.
     * @param playlistName Name of the Playlist to add the song to.
     */
    public void addSongToPlaylist(Collection song, String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("Profile.addSongToPlaylist() - Playlist \"" + playlistName + "\" not found.");
            return;
        }

        // No data checks performed
        playlists.get(playlistName).addToPlaylist(song);
    }

    /**
     * Adds a playlist to the profile's playlist collection.
     *
     * @param playlistName The name of the playlist.
     * @param playlist     The Playlist object to add.
     */
    public void addPlaylist(String playlistName, Playlist playlist) {
        if (playlists.containsKey(playlistName)) {
            System.out.println("Playlist not added: \"" + playlistName + "\" already exists.");
            return;
        }

        playlists.put(playlistName, playlist);
        System.out.println("\"" + playlistName + "\" added to playlists");
    }

    /**
     * Removes a playlist from the profile's playlist collection.
     *
     * @param playlistName Name of the Playlist to be removed
     */
    public void removePlaylist(String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            System.out.println("Playlist not removed: \"" + playlistName + "\" not found.");
            return;
        }

        playlists.remove(playlistName);
        System.out.println("\"" + playlistName + "\" removed from playlists.");
    }

    /**
     * Return the Playlists map as an iterable List of Playlists.
     *
     * @return List of Playlists
     */
    public List<Playlist> getIterablePlaylists() {
        return new ArrayList<>(playlists.values());
    }
}
