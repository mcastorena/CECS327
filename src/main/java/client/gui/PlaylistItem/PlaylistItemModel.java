package client.gui.PlaylistItem;

import client.model.Playlist;

public class PlaylistItemModel {
    Playlist playlist;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(final Playlist playlist) {
        this.playlist = playlist;
    }
}
