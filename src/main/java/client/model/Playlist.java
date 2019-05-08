package client.model;

import java.util.ArrayList;

/**
 * Defines the characteristics of a Playlist
 */
public class Playlist {

    private String id;
    private String name;
    private ArrayList<CollectionLightWeight> songList;

    /**
     * Constructs a Playlist given an ID and a name
     *
     * @param id   - ID number to reference the Playlist
     * @param name - Name of the Playlist as given by the User
     */
    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
        this.songList = new ArrayList<>();
    }

    /**
     * Constructs a Playlist given a name
     *
     * @param name - Name of the Playlist
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

    public ArrayList<CollectionLightWeight> getSongList() {
        return this.songList;
    }
    //endregion

    /**
     * Adds a song to a Playlist
     *
     * @param song - Song to be added to the Playlist
     * @return - True if the song was successfully added
     */
    public boolean addToPlaylist(CollectionLightWeight song) {
        boolean added = false;

        if (song != null) {
            this.songList.add(song);
            added = true;
            System.out.printf("%s added to playlist\n", song.getSongTitle());
        }

        return added;
    }
}
