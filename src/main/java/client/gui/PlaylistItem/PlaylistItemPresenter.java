package client.gui.PlaylistItem;

import client.gui.PlaylistList.PlaylistListPresenter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import client.model.Playlist;

import java.io.IOException;


/**
 * Following the MVP design pattern, this class represents the Presenter for the PlaylistItem
 */
public class PlaylistItemPresenter {

    /**
     * Parent container that contains this object
     */
    private Parent view;

    /**
     * Model for this PlaylistItem
     */
    private PlaylistItemModel playlistItemModel;

    //region FXML components
    @FXML
    Pane playlistItemPane;
    @FXML
    Text playlistNameText;
    //endregion

    /**
     * Constructor
     */
    public PlaylistItemPresenter() {
        try {

            // Required for JavaFX
            FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            loader.setLocation(getClass().getResource("/client/ui/PlaylistItem.fxml"));
            view = loader.load();

            playlistItemModel = new PlaylistItemModel();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called post-constructor & required by JavaFX
     */
    @FXML
    public void initialize() {
        playlistItemPane.setOnMouseEntered(e -> {
            playlistItemPane.setStyle("-fx-background-color: #464646");
        });
        playlistItemPane.setOnMouseExited(e -> {
            playlistItemPane.setStyle("-fx-background-color: #333333");
        });
    }

    /**
     * Sets the playlist within the Model
     *
     * @param sender   - Presenter sending the Playlist
     * @param playlist - Playlist object being set
     */
    public void receivePlaylist(PlaylistListPresenter sender, Playlist playlist) {
        playlistItemModel.setPlaylist(playlist);
    }

    public Parent getView() {
        return view;
    }
}
