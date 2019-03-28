package client.gui.PlaylistPage;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import client.model.Playlist;

/**
 * Per the MVP design pattern, this class represents the Presenter for  the PlaylistPage
 */
public class PlaylistPagePresenter {

    /**
     * MVP connection to the Model
     */
    private PlaylistPageModel playlistPageModel;

    /**
     * Required by JavaFX
     */
    @FXML
    public void initialize() {
        playlistPageModel.setPlaylist(null);
    }

    /**
     * TODO:
     *
     * @param playlist - Playlist being set in the model
     */
    public void receivePlaylists(Playlist playlist) {
        playlistPageModel.setPlaylist(playlist);
    }

    /* Getter */
    public Parent getView() {
        return null;
    }
}
