package ServerMain;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Collection;
import model.Playlist;
import model.Profile;
import model.User;
import utility.Serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class PlaylistDispatcher {
    static final int FRAGMENT_SIZE = 8192;

    public PlaylistDispatcher()
    {

    }

    /*
     * getSearchResultChunk: Gets a chunk of a given search result
     * @param fragment: The chunk corresponds to
     * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
     */
    public String getPlaylistsChunk(Long fragment) throws IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];

        System.out.println("PlaylistDispatcher is getting chunk");
        InputStream inputStream = new ByteArrayInputStream(server.bytePlaylists);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();

        // Encode in base64 so it can be transmitted
        return Base64.getEncoder().encodeToString(buf);
    }

    /*
     * getSize: Gets a size of the byte array
     * @param query: the search query from user
     */
    public Integer getPlaylistsSize(Integer userToken)
    {
        User currentSession = server.currentSessions.get(userToken);
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

        server.bytePlaylists = playlistListJA.toString().getBytes();
        return  server.bytePlaylists.length;
    }
}
//[
//        {
//        "first": [
//        {Song: {id:287650, songName:"name", artistName:"name", releaseName:"name"}},
//        {Song: {id:300822, songName:"name", artistName:"name", releaseName:"name"}},
//   Continue this format
//        300848,
//        41838,
//        514953,
//        611336
//        ]
//        },
//        {
//        "second": [
//        300848,
//        41838,
//        514953,
//        611336
//        ]
//        }
//]