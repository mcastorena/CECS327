package Gui.PlaylistList;

import Gui.Homepage.HomepagePresenter;
import Gui.MainDisplay.MainDisplayPresenter;
import Gui.PlaylistItem.PlaylistItem;
import app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import model.Playlist;
import rpc.CECS327InputStream;

import java.io.IOException;

public class PlaylistListPresenter {
    private MainDisplayPresenter mainDisplayPresenter;

    private PlaylistListModel playlistListModel;
    //    private PlaylistListView playlistListView;
    private Parent view;

    private HomepagePresenter homepagePresenter;

//    @FXML private ScrollPane playlistsScroller;

//    @FXML private ListView<String> playlistsListView;

//    @FXML private VBox list;

//    @FXML private TextField newPlaylistTextField;

//    @FXML private Button createButton;

    @FXML
    VBox playlistPanel;
    @FXML
    Group addButton;

    public PlaylistListPresenter(MainDisplayPresenter mainDisplayPresenter, HomepagePresenter homepagePresenter) {
        try {
            this.mainDisplayPresenter = mainDisplayPresenter;
            this.homepagePresenter = homepagePresenter;

            playlistListModel = new PlaylistListModel();
//            playlistListModel.setPlaylists(
//                    UserSession.getCurrentSession()
//                            .getUserProfile()
//                            .getPlaylists());
            playlistListModel.setPlaylists(new CECS327InputStream(Main.userToken, homepagePresenter.getProxy()));

//            FXMLLoader loader = new FXMLLoader(MainDisplayPresenter.class.getResource("ui/PlaylistListAlt.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/PlaylistListAlt.fxml"));
//            loader.setLocation();
            loader.setController(this);
            view = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        playlistListModel = new PlaylistListModel();
//        playlistListModel.setPlaylists(
//                UserSession.getCurrentSession()
//                        .getUserProfile()
//                        .getIterablePlaylists());


//        playlistListView = new PlaylistListView();

    }

    public void setHomepage(HomepagePresenter homepagePresenter) {
        this.homepagePresenter = homepagePresenter;
    }


    @FXML
    public void initialize() {
        addButton.setOnMouseEntered(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
        addButton.setOnMouseExited(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
        addButton.setOnMouseClicked(e -> showCreatePlaylistWindow());
//        playlistsScroller.setStyle("-fx-background-color:transparent;");
//        createButton.setOnMouseClicked(e -> createPlaylist());
        renderPlaylists();
//        user = HomepagePresenter.getUser();
//        playlists = HomepagePresenter.getPlaylists();

//        initData();
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
//            n.setOnM
//            n.setOn

            playlistPanel.getChildren().add(n);
        }


//        ArrayList<Playlist> playlists = new ArrayList<>(playlistListModel.getPlaylists());
//        ArrayList<String> playlistNames = new ArrayList<>();
//        for (Playlist playlist : playlists) {
//            playlistNames.add(playlist.getName());
//        }
//        // Display the playlist names in the ListView
//        ObservableList<String> obsPlaylists = FXCollections.observableArrayList(playlistNames);
//        playlistsListView.setItems(obsPlaylists);
//        // Selecting playlist name pulls up the playlist in the main display
//        playlistsListView.getSelectionModel()
//                .selectedItemProperty()
//                .addListener(new ChangeListener<String>() {
//                    @Override
//                    public void changed(final ObservableValue<? extends String> observable,
//                                        final String oldValue, final String newValue) {
//                        String selection = playlistsListView.getSelectionModel().getSelectedItem();
//                        sendPlaylistSelectionToMainDisplay(selection);
//                    }
//        });

    }

    public void sendPlaylist(Playlist p) {
//        homepagePresenter.receivePlaylistItemClick(this, p.getSongList());;
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
        // To-do: add the playlist to user
        Playlist newPlaylist = new Playlist(playlistName);

        playlistListModel.addPlaylist(newPlaylist);
//        playlistPanel.getChildren()
//                    .add(new PlaylistItem(this, newPlaylist)
//                                .getView());
        renderPlaylists();
    }

    /**
     * Receives a message from a PlaylistItem to delete the associated Playlist object
     *
     * @param sender - PlaylistItem sending the message
     * @param obj - Playlist to be delete
     */
    public void receivePlaylistItemDeleteClick(PlaylistItem sender, Playlist obj) {
        playlistListModel.deletePlaylist(obj);
        renderPlaylists();
    }

    public void receiveSongAdd(PlaylistItem sender) {
        renderPlaylists();
    }

    private void showCreatePlaylistWindow() {
        CreatePlaylistWindow cpw = new CreatePlaylistWindow(this);
        cpw.show();
    }
}
