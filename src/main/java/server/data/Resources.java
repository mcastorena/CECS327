package server.data;

import server.model.Collection;

import java.util.HashMap;
import java.util.List;

/**
 * TODO: Currently not used
 */
public class Resources {

    /**
     * A list of ALL songs (from the Music JSON).
     */
    private static List<Collection> musicDatabase;

    /**
     * A container of references for PLAYABLE songs
     * (i.e. the song's mp3 file exists in the music directory).
     */
    private static HashMap<Integer, String> playableSongs; // key: song release ID, value: mp3 file name

    /**
     * A dictionary of the user's music library (as Collection objects).
     */
    private static HashMap<Integer, Collection> userLibrary; // key: song release ID, value: corresponding Collection object

    //region Getters and Setters
    public static List<Collection> getMusicDatabase() {
        return musicDatabase;
    }

    public static void setMusicDatabase(final List<Collection> musicDatabase) {
        Resources.musicDatabase = musicDatabase;
    }

    public static HashMap<Integer, String> getPlayableSongs() {
        return playableSongs;
    }

    public static void setPlayableSongs(final HashMap<Integer, String> playableSongs) {
        Resources.playableSongs = playableSongs;
    }

    public static HashMap<Integer, Collection> getUserLibrary() {
        return userLibrary;
    }

    public static void setUserLibrary(final HashMap<Integer, Collection> userLibrary) {
        Resources.userLibrary = userLibrary;
    }
    //endregion
}
