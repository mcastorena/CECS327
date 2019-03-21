package client.gui.MainDisplay;

import client.model.Playlist;

/**
 * As part of the MVP-design pattern, this class represents the Model for the MainDisplay
 */
public class MainDisplayModel {

    /**
     * Playlist that is shown within the MainDisplay
     */
    private Playlist playlist;

    public void MainDisplayModel() {
    }

    //region Getters and Setters
    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(final Playlist playlist) {
        this.playlist = playlist;
    }
    //endregion
}
