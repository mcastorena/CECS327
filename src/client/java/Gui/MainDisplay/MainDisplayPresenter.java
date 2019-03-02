package Gui.MainDisplay;

import Gui.PlaylistList.PlaylistListPresenter;
import Gui.SearchBar.SearchBarPresenter;
import data.Resources;
import data.UserSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

import Gui.Homepage.HomepagePresenter;
import model.Collection;
import model.Playlist;
import model.SearchResult;
import rpc.ProxyInterface;
import utility.Search;


public class MainDisplayPresenter {
    Parent view;

    MainDisplayModel mainDisplayModel;

    HomepagePresenter homepagePresenter;

    SongSearchModel songSearchModel;

    ProxyInterface clientProxy;

    @FXML
    ScrollPane displayScroller;

//    @FXML
//    ListView displayListView;

    @FXML
    VBox displayVBox;

    public MainDisplayPresenter(HomepagePresenter homepagePresenter) {
        mainDisplayModel = new MainDisplayModel();
        this.homepagePresenter = homepagePresenter;
        clientProxy = homepagePresenter.getProxy();
        songSearchModel = new SongSearchModel();
        songSearchModel.setMusicDatabase(Resources.getMusicDatabase());

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/MainDisplay.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
//        mainDisplayList = new ListView();
    }

    public Parent getView() {
        return view;
    }

    public void receiveSearchText(SearchBarPresenter sender, String searchText) {
        SearchResult searchResult = Search.search(searchText, songSearchModel.getMusicDatabase());
        songSearchModel.setResults(searchResult);
        showResults(songSearchModel.getResults());
    }

    public void receiveSearchResults(HomepagePresenter sender, ObservableList<Collection> results) {

        ListView mainDisplayList = new ListView(results);

//        m_listView.prefWidth(100);
//        m_listView.setMaxWidth(100);
        mainDisplayList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Collection>() {
                    public void changed(
                            ObservableValue<? extends Collection> observable,
                            Collection c, Collection ca) {

                        // play music
                    }
                });

//        display = new VBox(mainDisplayList);
//        display.getChildren().add(mainDisplayList);
    }

    public void showResults(SearchResult searchResult) {
        displayVBox.getChildren().clear();
        for (Collection song : searchResult.getSongResultList()) {
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
        for (Collection song : playlist.getSongList()) {
            MainDisplayItem displayItem =
                    new MainDisplayItem(this, song);


            displayVBox.getChildren()
                    .add(displayItem
                            .getView());
        }
    }

    public void receivePlayRequest(MainDisplayItem sender, Collection song) {
        homepagePresenter.receivePlaylistItemClick(this, song, mainDisplayModel.getPlaylist());
    }

    public void receivePlaylistItemClick(HomepagePresenter sender, Playlist obj) {
        mainDisplayModel.setPlaylist(obj);
        showPlaylist(obj);
    }

    public ProxyInterface getProxy()
    {
        return clientProxy;
    }
}
