package model;

import java.io.Serializable;

// A container to hold all json objects related to the same release id
public class Collection implements Serializable {
    private static long serialVersionUID = 1L;

    private Release release;
    private Artist artist;
    private Song song;
    private long id;

    public Collection(Release release, Artist artist, Song song) {
        this.release = release;
        this.artist = artist;
        this.song = song;
        this.id = release.getId();
    }

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

    @Override
    public String toString() {
        return getArtistName();
    }

    public void serialize() {

    }
}