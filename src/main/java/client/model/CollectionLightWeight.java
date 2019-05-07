package client.model;

import java.io.Serializable;

/**
 * A custom container for Songs
 */
public class CollectionLightWeight implements Serializable {    // extends Collection

    /**
     * Artist of the song
     */
    private String artistName;

    /**
     * Name of the song
     */
    private String songName;

    /**
     * Release info of the song
     */
    private String releaseName;

    /**
     * long ID of the song
     */
    private long idNum;

    /**
     * Empty constructor
     */
    public CollectionLightWeight() {
    }

    /**
     * Full constructor
     *
     * @param id          - long ID of the song
     * @param songName    - Name of the song
     * @param artistName  - Artist of the song
     * @param releaseName - Release info
     */
    public CollectionLightWeight(long id, String songName, String artistName, String releaseName) {
        this.releaseName = releaseName;
        this.artistName = artistName;
        this.songName = songName;
        this.idNum = id;
    }

    //region Getters and Setters
    public String getReleaseName() {
        return releaseName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongTitle() {
        return songName;
    }

    public long getId() {
        return idNum;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setId(long id) {
        this.idNum = id;
    }
    //endregion

    public void serialize() {
    }
}

