package Gui.MainDisplay;

import model.Collection;
import model.SearchResult;

import java.util.List;

public class SongSearchModel {
    List<Collection> musicDatabase;
    SearchResult results;

    public List<Collection> getMusicDatabase() {
        return musicDatabase;
    }

    public void setMusicDatabase(final List<Collection> musicDatabase) {
        this.musicDatabase = musicDatabase;
    }

    public SearchResult getResults() {
        return results;
    }

    public void setResults(final SearchResult results) {
        this.results = results;
    }
}
