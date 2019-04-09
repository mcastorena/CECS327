package server.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.model.Collection;
import server.model.Playlist;
import server.model.Profile;
import server.model.User;
import server.util.Serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class PlaylistDispatcher extends Dispatcher implements DispatcherService {
    private static final int FRAGMENT_SIZE = 44100;

    public PlaylistDispatcher()
    {

    }

    /**
     * getSearchResultChunk: Gets a chunk of a given search result
     * @param fragment: The chunk corresponds to
     * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getPlaylistsChunk(Long fragment) throws IOException
    {
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
     * getPlaylistsSize: Gets a size of the byte array
     * @param userToken: The unique token of the user
     */
    public Integer getPlaylistsSize(Integer userToken)
    {
        User currentSession = Server.currentSessions.get(userToken);
        Profile userProfile = currentSession.getUserProfile();

        JsonArray playlistListJA = new JsonArray();
        for(Playlist p : userProfile.getIterablePlaylists()) {
            JsonObject playlistJO = new JsonObject();
            JsonArray singlePlaylist = new JsonArray();
            for(Collection c : p.getSongList())
            {
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
        return  Server.bytePlaylists.length;
    }

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

    public String addSongToPlaylist(Integer userToken, String playlistName, Long songID) throws IOException {
        Serializer s = new Serializer();
        User currentSession = Server.currentSessions.get(userToken);

        // Add playlist to profile if not already added
        currentSession.getUserProfile().addPlaylist(playlistName, new Playlist(playlistName));

        // Add the song to the playlist as a Collection
        currentSession.getUserProfile().getPlaylist(playlistName).addToPlaylist(Server.d.getUserLibrary().get(Math.toIntExact(songID)));
        s.updateUsersJson(Server.userList);

        JsonObject ackMessage = new JsonObject();
        ackMessage.addProperty("Ack:", "Song added to " + playlistName);
        // TODO: need encoding
        return ackMessage.toString();
    }
}
