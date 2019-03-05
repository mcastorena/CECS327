package Gui.PlaylistList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import data.UserSession;
import model.CollectionLightWeight;
import model.Playlist;
import model.Profile;
import rpc.CECS327InputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public class PlaylistListModel {
    HashMap<String,Playlist> playlists = new HashMap<>();

//    List<Playlist> playlists;

//    public List<Playlist> getPlaylists() {
//        return playlists;
//    }

//    protected void setPlaylists(final List<Playlist> playlists) {
//        this.playlists = playlists;
//    }


    public HashMap<String, Playlist> getPlaylists() {
        return playlists;
    }

//    public void setPlaylists(final HashMap<String, Playlist> playlists) {
//        this.playlists = playlists;
//    }
public void setPlaylists(CECS327InputStream is) throws IOException {
    Gson gson = new Gson();
    JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

    reader.beginArray();
    while(reader.hasNext())
    {
        reader.beginObject();
        String playlistName = reader.nextName();
        Playlist p = new Playlist(playlistName);
        reader.beginArray();
        while(reader.hasNext())
        {
            reader.beginObject();
            reader.nextName();
            p.addToPlaylist(gson.fromJson(reader, CollectionLightWeight.class));
            reader.endObject();
        }
        reader.endArray();
        reader.endObject();
        if (p != null) playlists.put(playlistName, p);

    }
    reader.endArray();
    UserSession.getCurrentSession().setUserProfile(new Profile(playlists));
}

    protected void addPlaylist(Playlist p) {
        if (p != null) playlists.put(p.getName(), p);
    }

    protected void removePlaylist(Playlist p) {
        playlists.remove(p.getName());
    }
}
