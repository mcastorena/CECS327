package client.gui.PlaylistList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import client.data.UserSession;
import client.model.CollectionLightWeight;
import client.model.Playlist;
import client.model.Profile;
import client.rpc.CECS327InputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Per the MVP design pattern, this class represents the Model for a PlaylistList
 */
public class PlaylistListModel {

    private HashMap<String, Playlist> playlists = new HashMap<>();

    /* Getter */
    public HashMap<String, Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Fills the playlists hashmap and associates it with a UserSession
     *
     * @param is - InputStream of JSON data
     * @throws IOException - Required for the InputStream
     */
    /* Setter */
    public void setPlaylists(CECS327InputStream is) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String playlistName = reader.nextName();
            Playlist playlist = new Playlist(playlistName);

            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                reader.nextName();
                playlist.addToPlaylist(gson.fromJson(reader, CollectionLightWeight.class));
                reader.endObject();
            }
            reader.endArray();
            reader.endObject();

            if (playlist != null) playlists.put(playlistName, playlist);

        }
        reader.endArray();
        UserSession.getCurrentSession().setUserProfile(new Profile(playlists));
    }

    /**
     * Adds the Playlist to the HashMap of all Playlists
     *
     * @param playlist - Playlist object to be added
     */
    void addPlaylist(Playlist playlist) {
        if (playlist != null) playlists.put(playlist.getName(), playlist);
        // TODO: Tell server to add a new playlist
    }

    /**
     * Deletes the Playlist from the HashMap of all Playlists
     *
     * @param playlist - Playlist object to be deleted
     */
    void deletePlaylist(Playlist playlist) {
        playlists.remove(playlist.getName());
        // TODO: Tell server to delete the Playlist
    }
}
