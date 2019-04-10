package server.model;

import java.util.List;

/**
 * This class represents the result of a Song search and is used to obtain information to be displayed.
 */
public class SearchResult {

    /**
     * List of Songs
     */
    List<Collection> songs;

    /**
     * List of Artists
     */
    List<Collection> artists;

    /**
     * Constructor
     *
     * @param songs   List of Collection objects representing Songs
     * @param artists List of Collection objects representing Artists
     */
    public SearchResult(List<Collection> songs, List<Collection> artists) {
        this.songs = songs;
        this.artists = artists;
    }

    //region Getters and Setters
    public List<Collection> getSongResultList() {
        return songs;
    }

    public List<Collection> getArtistResultList() {
        return artists;
    }

    public String getSongTitle(int index) {
        return songs.get(index).getSongTitle();
    }

    public String getArtistForSong(int index) {
        return songs.get(index).getArtistName();
    }

    public String getArtistName(int index) {
        return artists.get(index).getArtistName();
    }

    public int getSongResultSize() {
        return songs.size();
    }

    public int getArtistResultSize() {
        return artists.size();
    }

    public List<Collection> getSongs() {
        return songs;
    }

    public List<Collection> getArtists() {
        return artists;
    }
    //endregion

    @Override
    public String toString() {
        return "SearchResult{" +
                "songs=" + songs +
                ",\n artists=" + artists +
                '}';
    }
}
