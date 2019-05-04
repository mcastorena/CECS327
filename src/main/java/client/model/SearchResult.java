package client.model;

import java.util.ArrayList;
import java.util.List;

// Contains list of collections for song/artists
// Can use this to obtain information to be displayed
public class SearchResult {

    List<CollectionLightWeight> songs;
    List<CollectionLightWeight> artists;

    public SearchResult(List<CollectionLightWeight> songs, List<CollectionLightWeight> artists) {
        this.songs = songs;
        this.artists = artists;
    }

    public SearchResult()
    {
    }

    public List<CollectionLightWeight> getSongResultList() {
        return songs;
    }

    public List<CollectionLightWeight> getArtistResultList() {
        return artists;
    }

    public void setSongs(List<CollectionLightWeight> songs) {
        this.songs = songs;
    }

    public void setArtists(List<CollectionLightWeight> artists) {
        this.artists = artists;
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


    @Override
    public String toString() {
        return "SearchResult{" +
                "songs=" + songs +
                ",\n artists=" + artists +
                '}';
    }
}
