import java.util.List;

// Contains list of collections for song/artists
// Can use this to obtain information to be displayed
public class SearchResult {
    List<Collection> songs;
    List<Collection> artists;

    public SearchResult(List<Collection> songs, List<Collection> artists) {
        this.songs = songs;
        this.artists = artists;
    }

    // TODO: Add setters/getters for Lists songs/artists?

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

    public int getArtistResultSize()
    {
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
