package client.gui.PlaylistPage;

import client.model.Playlist;

/**
 * Per the MVP design pattern, this class represents the Model for the PlaylistPage
 */
public class PlaylistPageModel {

    /**
     * Playlist associated with this object
     */
    Playlist playlist;

    //region Getters and Setters
    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(final Playlist playlist) {
        this.playlist = playlist;
    }
    //endregion
}
