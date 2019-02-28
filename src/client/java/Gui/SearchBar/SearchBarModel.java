package Gui.SearchBar;

import model.Collection;

import java.util.List;

public class SearchBarModel {
    List<Collection> musicDatabase;

    public List<Collection> getMusicDatabase() {
        return musicDatabase;
    }

    public void setMusicDatabase(final List<Collection> musicDatabase) {
        this.musicDatabase = musicDatabase;
    }
}
