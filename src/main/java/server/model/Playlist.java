package server.model;

import java.util.ArrayList;

/**
 * This class represents User's Playlists
 */
public class Playlist {

    /**
     * ID of the Playlist
     */
    private String id;

    /**
     * Name of the Playlist
     */
    private String name;

    /**
     * List of Songs in the Playlist
     */
    private ArrayList<Collection> songList;

    /**
     * Default constructor for creating a new playlist. Instantiates a new ArrayList for songList.
     *
     * @param id   ID number to reference the Playlist
     * @param name Name of the Playlist as given by the User
     */
    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
        this.songList = new ArrayList<>();
    }

    /**
     * Constructor #2
     *
     * @param name Name of the Playlist
     */
    public Playlist(String name) {
        this.id = "001";
        this.name = name;
        this.songList = new ArrayList<>();
    }

    //region Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Collection> getSongList() {
        return this.songList;
    }
    //endregion

    /**
     * Adds a Song, represented by a Collection, to a Playlist
     *
     * @param song Song being added
     * @return true if the song was added, otherwise false
     */
    public boolean addToPlaylist(Collection song) {    // TODO: Change to a Song POJO
        boolean added = false;

        if (song != null) {
            this.songList.add(song);
            added = true;
        }

        return added;
    }
}
