package server.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.model.Collection;
import server.model.Playlist;
import server.model.Profile;
import server.model.User;
import server.util.Serializer;
import static server.core.Server.dfs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;

/**
 * This class represents a specialized Dispatcher for Playlists
 */
public class PlaylistDispatcher extends Dispatcher implements DispatcherService {

    /**
     * Fragment size; required by Dispatcher interface
     */
    private static final int FRAGMENT_SIZE = 44100;

    /**
     * Default constructor
     */
    public PlaylistDispatcher() {
    }

    /**
     * Gets a chunk of a given search result
     *
     * @param fragment The chunk corresponds to
     *                 [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getPlaylistsChunk(Long fragment) throws IOException {
        byte buf[] = new byte[FRAGMENT_SIZE];

        System.out.println("PlaylistDispatcher is getting chunk");
        InputStream inputStream = new ByteArrayInputStream(Server.bytePlaylists);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();

        // Encode in base64 so it can be transmitted
        return Base64.getEncoder().encodeToString(buf);
    }

    /**
     * Gets a size of the byte array
     *
     * @param userToken The unique token of the user
     */
    public Integer getPlaylistsSize(Integer userToken) {
        User currentSession = Server.currentSessions.get(userToken);
        Profile userProfile = currentSession.getUserProfile();

        JsonArray playlistListJA = new JsonArray();
        for (Playlist p : userProfile.getIterablePlaylists()) {
            JsonObject playlistJO = new JsonObject();
            JsonArray singlePlaylist = new JsonArray();
            for (Collection c : p.getSongList()) {
                JsonObject songJO = new JsonObject();
                JsonObject singleSongElement = new JsonObject();
                songJO.addProperty("idNum", c.getId());
                songJO.addProperty("songName", c.getSongTitle());
                songJO.addProperty("artistName", c.getArtistName());
                songJO.addProperty("releaseName", c.getRelease().getName());
                singleSongElement.add("song", songJO);
                singlePlaylist.add(singleSongElement);
            }
            playlistJO.add(p.getName(), singlePlaylist);
            playlistListJA.add(playlistJO);
        }


        System.out.println(playlistListJA.toString());

        Server.bytePlaylists = playlistListJA.toString().getBytes();
        return Server.bytePlaylists.length;
    }

    /**
     * Informs the Server to delete the Playlist
     *
     * @param userToken    User being accessed
     * @param playlistName Name of Playlist to be deleted
     * @return Acknowledgement of Playlist deletion
     * @throws IOException
     */
    public String deletePlaylist(Integer userToken, String playlistName) throws IOException {
        Serializer s = new Serializer();
        User currentSession = Server.currentSessions.get(userToken);
        currentSession.getUserProfile().removePlaylist(playlistName);
        s.updateUsersJson(Server.userList);

        JsonObject ackMessage = new JsonObject();
        ackMessage.addProperty("Ack:", "Playlist \"" + playlistName + "\" deleted");
        // TODO: need encoding
        return ackMessage.toString();
    }

    /**
     * Informs the Server to add a Song to a Playlist
     *
     * @param userToken    User being referenced
     * @param playlistName Name of the playlist to be modified
     * @param songID       ID of the song being added
     * @return Acknowledgment that the song has been added
     * @throws IOException
     */
    public String addSongToPlaylist(Integer userToken, String playlistName, Long songID, String songName) throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        Serializer s = new Serializer();
        User currentSession = Server.currentSessions.get(userToken);

        // Add playlist to profile if not already added
        currentSession.getUserProfile().addPlaylist(playlistName, new Playlist(playlistName));

        // Add the song to the playlist as a Collection
        String song = songName.substring(1,songName.length() - 1);
        JsonArray ja = dfs.search(song);
        JsonArray songArray = ja.get(0).getAsJsonArray();
        Collection c = null;
        try {
            for(Object o : songArray)
            {
                JsonObject jo = gson.toJsonTree(o).getAsJsonObject();
                c = Server.d.jsonToCollectionLightWeight(jo);
                if(c.getId() == songID)
                    break;
            }

            currentSession.getUserProfile().getPlaylist(playlistName).addToPlaylist(c);
            s.updateUsersJson(Server.userList);

            JsonObject ackMessage = new JsonObject();
            ackMessage.addProperty("Ack:", "Song added to " + playlistName);
            // TODO: need encoding
            return ackMessage.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return "Add Song to " + playlistName + " FAILED!" ;

    }
}
