package Gui.PlaylistItem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import Gui.PlaylistList.PlaylistListPresenter;

import model.Playlist;

import java.io.IOException;


public class PlaylistItemPresenter {
//    private Playlist playlist;
    private PlaylistItemModel playlistItemModel;
//    private PlaylistItemView playlistItemView;
    private Parent view;

//    @FXML private Text text;

//    @FXML private TextFlow listItem;

    @FXML Pane playlistItemPane;
    @FXML Text playlistNameText;

    public PlaylistItemPresenter() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(this);

            // Load the playlist
            loader.setLocation(getClass().getResource("/ui/PlaylistItem.fxml"));
            view = loader.load();

            playlistItemModel = new PlaylistItemModel();

//        playlistItemView = new PlaylistItemView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        playlistItemPane.setOnMouseEntered(e -> {
            playlistItemPane.setStyle("-fx-background-color: #464646");
        });
        playlistItemPane.setOnMouseExited(e -> {
            playlistItemPane.setStyle("-fx-background-color: #333333");
        });

//        text.textProperty().set(playlistItemModel.getPlaylist().getName());
//        homepagePresenter = new HomepagePresenter();

//        listItem.setOnMouseClicked(e -> {
            // Generate the playlist page and send to homepage
//            homepagePresenter.displayPlaylistPage();
//        });
//        listItem.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
//            displaySongs();
//        });
    }

    public void receivePlaylist(PlaylistListPresenter sender, Playlist playlist) {
        playlistItemModel.setPlaylist(playlist);
//        text = new Text(playlistItemModel.getPlaylist().getName());
//        text.textProperty().set();
//        displaySongs();
    }

    public Parent getView() {
        return view;
    }

//    public void setPlaylist(Playlist playlist) {
//        this.playlist = playlist;
//        text.textProperty().set(playlist.getName());
//    }

//    public void displaySongs() {
//        SearchBarView sbLoader = new SearchBarView();
//        Parent sbView = sbLoader.getView();
//        SearchBarPresenter sbPresenter = new SearchBarPresenter();
//        sbPresenter.renderPlaylist(playlistItemModel.getPlaylist());
//    }
}
