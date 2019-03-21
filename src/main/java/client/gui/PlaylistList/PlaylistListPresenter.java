package client.gui.PlaylistList;

import client.gui.Homepage.HomepagePresenter;
import client.gui.MainDisplay.MainDisplayPresenter;
import client.gui.PlaylistItem.PlaylistItem;
import client.app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import client.model.Playlist;
import client.rpc.CECS327InputStream;
import client.rpc.ProxyInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlaylistListPresenter {

    /**
     * Parent node
     */
    private Parent view;

    /**
     * MVP connection to the Homepage
     */
    private HomepagePresenter homepagePresenter;

    /**
     * MVP connection to the MainDisplay
     */
    private MainDisplayPresenter mainDisplayPresenter;

    /**
     * MVP connection to the PlaylistList
     */
    private PlaylistListModel playlistListModel;

    //region FXML components
    @FXML
    VBox playlistPanel;
    @FXML
    Group addButton;
    //endregion

    /**
     * Constructor
     *
     * @param mainDisplayPresenter - MVP connection to the MainDisplay
     * @param homepagePresenter    - MVP connection to the Homepage
     */
    public PlaylistListPresenter(MainDisplayPresenter mainDisplayPresenter, HomepagePresenter homepagePresenter) {
        try {
            this.mainDisplayPresenter = mainDisplayPresenter;
            this.homepagePresenter = homepagePresenter;

            playlistListModel = new PlaylistListModel();
            playlistListModel.setPlaylists(new CECS327InputStream(App.userToken, homepagePresenter.getProxy()));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/PlaylistListAlt.fxml"));
            loader.setController(this);
            view = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Required by JavaFX for accessing @FXML components
     */
    @FXML
    public void initialize() {
        addButton.setOnMouseEntered(e -> App.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        addButton.setOnMouseExited(e -> App.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        addButton.setOnMouseClicked(e -> showCreatePlaylistWindow());
        renderPlaylists();
    }

    /**
     * Re-initialize playlists data
     */
    public void initData() {
        renderPlaylists();
    }

    /**
     * Clears the previous state of the playlist panel and recreates it with the newly added data
     */
    // TODO: Clean up, generalize, abstract to interface
    // What this code does is instantiate a loader for a fxml component. Then, it uses that loader
    // to instantiate the component. The loader itself is tied to the component. Because of that,
    // it can use the component's controller to modify it indirectly.
    private void renderPlaylists() {
        playlistPanel.getChildren().clear();

        for (Playlist playlist : playlistListModel.getPlaylists().values()) {
            Node n = new PlaylistItem(this, playlist).getView();
            playlistPanel.getChildren().add(n);
        }
    }

    /**
     * TODO:
     *
     * @param p - Playlist being sent
     */
    public void sendPlaylist(Playlist p) {
    }

    /**
     * Takes the name of the playlist selected and sends it to the associated MainDisplayPresenter
     *
     * @param playlistName - Name of playlist selected
     */
    private void sendPlaylistSelectionToMainDisplay(String playlistName) {
        this.mainDisplayPresenter.receivePlaylistSelection(this, playlistName);
    }

    /**
     * Takes the Playlist selected and sends it to the associated HomepagePresenter
     *
     * @param sender - PlaylistItem sending the click
     * @param obj    - Playlist selected
     */
    public void receivePlaylistItemClick(PlaylistItem sender, Playlist obj) {
        homepagePresenter.receivePlaylistItemClick(this, obj);
    }

    /**
     * Receives a playlist title, creates a new Playlist with that title, adds it to the PlaylistListModel, and
     * re-renders the playlists
     *
     * @param sender       - Create playlist window
     * @param playlistName - Name of playlist to be created
     */
    public void receivePlaylistCreateClick(CreatePlaylistWindow sender, String playlistName) {
        // TODO: add the playlist to user
        Playlist newPlaylist = new Playlist(playlistName);

        playlistListModel.addPlaylist(newPlaylist);
        renderPlaylists();
    }

    /**
     * Receives a message from a PlaylistItem to delete the associated Playlist object
     *
     * @param sender - PlaylistItem sending the message
     * @param obj    - Playlist to be delete
     */
    public void receivePlaylistItemDeleteClick(PlaylistItem sender, Playlist obj) {
        playlistListModel.deletePlaylist(obj);
        renderPlaylists();

        // Delete playlist on server
        Map<String, String> params = new HashMap<>();
        params.put("userToken", Integer.toString(App.userToken));
        params.put("playlistName", obj.getName());
        homepagePresenter.getProxy().asyncExecution("deletePlaylist", params);
    }

    /**
     * TODO:
     *
     * @param sender - PlaylistItem
     */
    public void receiveSongAdd(PlaylistItem sender) {
        renderPlaylists();
    }

    /**
     * Creates and displays to the user a create playlist window
     */
    private void showCreatePlaylistWindow() {
        CreatePlaylistWindow cpw = new CreatePlaylistWindow(this);
        cpw.show();
    }

    //region Getters and Setters
    public Node getView() {
        return view;
    }

    public ProxyInterface getProxy() {
        return homepagePresenter.getProxy();
    }

    public void setHomepage(HomepagePresenter homepagePresenter) {
        this.homepagePresenter = homepagePresenter;
    }
    //endregion
}
