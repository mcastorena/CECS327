package client.Gui.MainDisplay;

import client.Gui.Homepage.HomepagePresenter;
import client.Gui.PlaylistList.PlaylistListPresenter;
import client.Gui.SearchBar.SearchBarPresenter;
import client.data.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import client.model.CollectionLightWeight;
import client.model.Playlist;
import client.rpc.CECS327InputStream;
import client.rpc.ProxyInterface;

import java.io.IOException;
import java.util.List;

public class MainDisplayPresenter {
    Parent view;

    MainDisplayModel mainDisplayModel;

    HomepagePresenter homepagePresenter;

    SongSearchModel songSearchModel;

    ProxyInterface clientProxy;

    @FXML
    ScrollPane displayScroller;

    @FXML
    VBox displayVBox;

    public MainDisplayPresenter(HomepagePresenter homepagePresenter) {
        mainDisplayModel = new MainDisplayModel();
        this.homepagePresenter = homepagePresenter;
        clientProxy = homepagePresenter.getProxy();
        songSearchModel = new SongSearchModel();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/client/ui/MainDisplay.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }

    public Parent getView() {
        return view;
    }

    public void receiveSearchText(SearchBarPresenter sender, String searchText) throws IOException {
        showResults(songSearchModel.getResults(new CECS327InputStream(searchText, clientProxy)));
    }

    // CHANGED THIS FOR SERVER/CLIENT
    public void showResults(List<CollectionLightWeight> searchResult) {
        displayVBox.getChildren().clear();

        for (CollectionLightWeight song : searchResult) {
            SearchResultSongItem displayItem =
                    new SearchResultSongItem(this, song);

            displayVBox.getChildren()
                    .add(displayItem
                            .getView());
        }
    }

    public void receivePlaylistSelection(PlaylistListPresenter sender, String selection) {
        try {
            Playlist playlist =
                    UserSession.getCurrentSession()
                            .getUserProfile()
                            .getPlaylist(selection);

            if (playlist == null)
                throw new NullPointerException("Playlist " + selection + " not found.");

            mainDisplayModel.setPlaylist(playlist);

            showPlaylist(playlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPlaylist(Playlist playlist) {
        displayVBox.getChildren().clear();
        for (CollectionLightWeight song : playlist.getSongList()) {
            MainDisplayItem displayItem =
                    new MainDisplayItem(this, song);


            displayVBox.getChildren()
                    .add(displayItem
                            .getView());
        }
    }

    public void receivePlayRequest(MainDisplayItem sender, CollectionLightWeight song) {
        homepagePresenter.receivePlaylistItemClick(this, song, mainDisplayModel.getPlaylist());
    }

    public void receivePlaylistItemClick(HomepagePresenter sender, Playlist obj) {
        mainDisplayModel.setPlaylist(obj);
        showPlaylist(obj);
    }

    public ProxyInterface getProxy() {
        return clientProxy;
    }
}
