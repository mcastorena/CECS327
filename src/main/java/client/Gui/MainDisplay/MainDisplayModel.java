package client.Gui.MainDisplay;

import client.model.Playlist;

public class MainDisplayModel {
    private Playlist playlist;

    public void MainDisplayModel() {
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(final Playlist playlist) {
        this.playlist = playlist;
    }
}
