package server.model;

import java.io.Serializable;

/**
 * This class represents a container that holds relevant information about a Song
 */
public class Collection implements Serializable {

    /**
     * Serial Version UID
     */
    private static long serialVersionUID = 1L;

    /**
     * Release info about the song
     */
    private Release release;

    /**
     * Artist of the song
     */
    private Artist artist;

    /**
     * Song being referenced
     */
    private Song song;

    /**
     * ID of the Song
     */
    private long id;

    /**
     * Default constructor
     *
     * @param release Release info for the song
     * @param artist Artist of the song
     * @param song Song being referenced
     */
    public Collection(Release release, Artist artist, Song song) {
        this.release = release;
        this.artist = artist;
        this.song = song;
        this.id = release.getId();
    }

    /**
     * Constructor ?
     *
     * @param id ID of the Song
     */
    public Collection(long id) {
        this.id = id;
    }

    //region Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public String getSongTitle() {
        return song.getTitle();
    }

    public String getArtistName() {
        return artist.getName();
    }
    //endregion

    @Override
    public String toString() {
        return getArtistName();
    }
}