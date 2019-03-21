package client.gui.PlaylistItem;

import client.model.Playlist;

/**
 * As part of the MVP-design pattern, this class represents the Model of the PlaylistItem
 */
public class PlaylistItemModel {
    /**
     * Linked Playlist object
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
