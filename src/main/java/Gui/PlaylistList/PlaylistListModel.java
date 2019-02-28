package Gui.PlaylistList;

import model.Playlist;

import java.util.HashMap;
import java.util.List;

public class PlaylistListModel {
    HashMap<String,Playlist> playlists;

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

    public void setPlaylists(final HashMap<String, Playlist> playlists) {
        this.playlists = playlists;
    }

    protected void addPlaylist(Playlist p) {
        if (p != null) playlists.put(p.getName(), p);
    }

    protected void removePlaylist(Playlist p) {
        playlists.remove(p.getName());
    }
}
