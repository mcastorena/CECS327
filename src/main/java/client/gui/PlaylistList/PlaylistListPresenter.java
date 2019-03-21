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

    private HomepagePresenter homepagePresenter;
    private MainDisplayPresenter mainDisplayPresenter;
    private Parent view;
    private PlaylistListModel playlistListModel;

    @FXML
    VBox playlistPanel;
    @FXML
    Group addButton;

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

    public void setHomepage(HomepagePresenter homepagePresenter) {
        this.homepagePresenter = homepagePresenter;
    }


    @FXML
    public void initialize() {
        addButton.setOnMouseEntered(e -> App.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        addButton.setOnMouseExited(e -> App.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        addButton.setOnMouseClicked(e -> showCreatePlaylistWindow());
        renderPlaylists();
    }

    public void initData() {
        renderPlaylists();
    }

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

    public void sendPlaylist(Playlist p) {
    }

    public Node getView() {
        return view;
    }

    private void sendPlaylistSelectionToMainDisplay(String playlistName) {
        this.mainDisplayPresenter.receivePlaylistSelection(this, playlistName);
    }

    public void receivePlaylistItemClick(PlaylistItem sender, Playlist obj) {
        homepagePresenter.receivePlaylistItemClick(this, obj);
    }

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

    public void receiveSongAdd(PlaylistItem sender) {
        renderPlaylists();
    }

    private void showCreatePlaylistWindow() {
        CreatePlaylistWindow cpw = new CreatePlaylistWindow(this);
        cpw.show();
    }

    public ProxyInterface getProxy() {
        return homepagePresenter.getProxy();
    }
}