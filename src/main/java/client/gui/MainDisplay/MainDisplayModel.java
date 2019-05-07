package client.gui.MainDisplay;

import client.model.CollectionLightWeight;
import client.model.Playlist;

import java.util.List;

/**
 * As part of the MVP-design pattern, this class represents the Model for the MainDisplay
 */
public class MainDisplayModel {

    /**
     * Playlist that is shown within the MainDisplay
     */
    private Playlist playlist;

    private List<CollectionLightWeight> bySongList;
    private List<CollectionLightWeight> byArtistList;

    public void MainDisplayModel() {
    }

    //region Getters and Setters
    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(final Playlist playlist) {
        this.playlist = playlist;
    }

    public List<CollectionLightWeight> getBySongList() {
        return bySongList;
    }

    public void setBySongList(final List<CollectionLightWeight> bySongList) {
        this.bySongList = bySongList;
    }

    public List<CollectionLightWeight> getByArtistList() {
        return byArtistList;
    }

    public void setByArtistList(final List<CollectionLightWeight> byArtistList) {
        this.byArtistList = byArtistList;
    }

    //endregion
}
