package client.gui.PlaylistItem;

import client.gui.PlaylistList.DeletePlaylistWindow;
import client.gui.PlaylistList.PlaylistListPresenter;
import client.app.App;
import client.data.CollectionFormat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import client.model.CollectionLightWeight;
import client.model.Playlist;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PlaylistItem {

    /**
     * Parent view
     */
    private Parent view;

    /**
     * Playlist represented on this Item being shown
     */
    private Playlist playlist;

    /**
     * Presenter for the PlaylistList that this item is within
     */
    private PlaylistListPresenter parent;

    //region FXML components
    @FXML
    Pane playlistItemPane;
    @FXML
    Text playlistNameText;
    @FXML
    StackPane deletePane;
    //endregion

    /**
     * Constructor
     *
     * @param parent   - PlaylistListPresenter
     * @param playlist - Playlist this Item represents
     */
    public PlaylistItem(PlaylistListPresenter parent, Playlist playlist) {
        try {
            this.parent = parent;
            this.playlist = playlist;

            // Load the delete button
            deletePane = new FXMLLoader(getClass().getResource("/client/ui/DeleteButton.fxml")).load();

            // Load the playlist item pane
            FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            loader.setLocation(getClass().getResource("/client/ui/PlaylistItem.fxml"));
            view = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Required for this object to access @FXML components
     */
    @FXML
    public void initialize() {
        playlistNameText.setText(playlist.getName());

        //region Delete pane
        deletePane.setVisible(false);
        deletePane.setPickOnBounds(true);
        deletePane.relocate(140, 30);
        deletePane.setOnMouseEntered(f -> {
            App.getPrimaryStage()
                    .getScene()
                    .setCursor(Cursor.HAND);
        });
        deletePane.setOnMouseExited(f -> {
            App.getPrimaryStage()
                    .getScene()
                    .setCursor(Cursor.DEFAULT);
        });
        deletePane.setOnMouseClicked(f -> showDeletePlaylistWindow());
        //endregion

        //region PlaylistItemPane
        playlistItemPane.getChildren().add(deletePane);

        // Set up event handling of MainDisplayItem dragged and dropped onto PlaylistItem
        playlistItemPane.setOnDragOver(e -> {
            Dragboard dragboard = e.getDragboard();
            if (e.getGestureSource() instanceof AnchorPane &&
                    dragboard.hasContent(CollectionFormat.FORMAT)) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });
        playlistItemPane.setOnDragDropped(e -> {
            boolean dragCompleted = false;
            Dragboard dragboard = e.getDragboard();

            if (dragboard.hasContent(CollectionFormat.FORMAT)) {
                CollectionLightWeight song = (CollectionLightWeight) dragboard.getContent(CollectionFormat.FORMAT);
                playlist.addToPlaylist(song);

                Map<String, String> params = new HashMap<>();
                params.put("userToken", Integer.toString(App.userToken));
                params.put("playlistName", playlist.getName());
                params.put("song", Long.toString(song.getId()));
                params.put("songName", song.getSongTitle());
                parent.getProxy().asyncExecution("addSongToPlaylist", params);

                dragCompleted = true;
                sendDragComplete();
            }
            App.setCursorStyle(Cursor.DEFAULT);
            e.setDropCompleted(dragCompleted);
            e.consume();

        });
        playlistItemPane.setOnMouseEntered(e -> {
            playlistItemPane.setStyle("-fx-background-color: #464646");
            deletePane.setVisible(true);
        });
        playlistItemPane.setOnMouseExited(e -> {
            playlistItemPane.setStyle("-fx-background-color: #333333");
            deletePane.setVisible(false);
        });
        playlistItemPane.setOnMouseClicked(e -> sendClick());
        //endregion
    }

    /**
     * Gets the user's click on a playlist and sends it to the Parent view
     */
    private void sendClick() {
        parent.receivePlaylistItemClick(this, playlist);
    }

    /**
     * TODO:
     *
     * @param sender - Object sending the message to delete the playlist
     */
    public void receivePlaylistDeleteClick(DeletePlaylistWindow sender) {
        sendDeleteClick();
    }

    /**
     * Informs the parent, a PlaylistListPresenter object, to delete the playlist associated with this PlaylistItem
     */
    private void sendDeleteClick() {
        parent.receivePlaylistItemDeleteClick(this, playlist);
    }

    /**
     * TODO:
     */
    private void showDeletePlaylistWindow() {
        DeletePlaylistWindow dpw = new DeletePlaylistWindow(this);
        dpw.show();
    }

    /**
     * TODO:
     */
    private void sendDragComplete() {
        parent.receiveSongAdd(this);
    }

    /**
     * @return - object's Parent view
     */
    public Parent getView() {
        return view;
    }
}
