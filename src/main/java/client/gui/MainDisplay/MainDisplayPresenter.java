package client.gui.MainDisplay;

import client.gui.Homepage.HomepagePresenter;
import client.gui.PlaylistList.PlaylistListPresenter;
import client.gui.SearchBar.SearchBarPresenter;
import client.data.UserSession;
import client.model.SearchResult;
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

/**
 * As part of the MVP-design pattern, this class represents the Presenter for the MainDisplay
 */
public class MainDisplayPresenter {

    /**
     * The parent view that contains this MainDisplay
     */
    Parent view;

    /**
     * The Model for the MainDisplay
     */
    MainDisplayModel mainDisplayModel;

    /**
     * The Presenter for the Homepage
     */
    HomepagePresenter homepagePresenter;

    /**
     * The Model for the SongSearch
     */
    SongSearchModel songSearchModel;

    /**
     * The proxy that the client is connected through
     */
    ProxyInterface clientProxy;

    //region FXML components
    @FXML
    ScrollPane displayScroller;
    @FXML
    VBox displayVBox;
    //endregion

    /**
     * Constructor
     *
     * @param hp - Presenter for the Homepage associated with this MainDisplay
     */
    public MainDisplayPresenter(MainDisplayModel mdm, HomepagePresenter hp, SongSearchModel ssm) {

        mainDisplayModel = mdm;
        homepagePresenter = hp;
//        clientProxy = hp.getProxy();
        songSearchModel = ssm;

        try {
            // Loader required for JavaFX to set the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/client/ui/MainDisplayTabbed.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }

    /**
     * Receives a search as a String and sends it to the SongSearchModel's `getResults()`. The list returned is then
     * passed into another method for processing.
     *
     * @param sender     - SearchBar sending the search
     * @param searchText - String being searched
     * @throws IOException - Required for the created InputStream
     */
    public void receiveSearchText(SearchBarPresenter sender, String searchText) throws IOException {
        showResults(songSearchModel.getResults(new CECS327InputStream(searchText, clientProxy)));
    }

    /**
     * Shows the search results from a list of retrieved songs
     *
     * @param result - SearchResult containing query results for artist/songs
     */
    private void showResults(SearchResult result) {
        // Clear the previous state of the VBox
        displayVBox.getChildren().clear();

        List<CollectionLightWeight> searchResult = result.getSongResultList();

        // Create a new item for display for each song in `searchResult`
        for (CollectionLightWeight song : searchResult) {
            SearchResultSongItem displayItem =
                    new SearchResultSongItem(this, song);

            displayVBox.getChildren()
                    .add(displayItem
                            .getView());
        }
    }

    /**
     * Receives a click-selection for a Playlist from the PlaylistList and sends it to the MainDisplay
     *
     * @param sender    - PlaylistList sending the click-selection
     * @param selection - Title of the playlist being selected
     */
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

    /**
     * Clears the previous state of the playlist's VBox and recreates it. Called after adding/removing a Playlist
     *
     * @param playlist - Playlist to be shown
     */
    private void showPlaylist(Playlist playlist) {

        // Clear the VBox of songs
        displayVBox.getChildren().clear();

        // Create new items for each song in the playlist
        for (CollectionLightWeight song : playlist.getSongList()) {
            MainDisplayItem displayItem =
                    new MainDisplayItem(this, song);

            // Add the song to the VBox
            displayVBox.getChildren()
                    .add(displayItem.getView());
        }
    }

    /**
     * Receives a play request from the MainDisplay and sends it to the Homepage
     *
     * @param sender - MainDisplay sending the request for play
     * @param song   - Song that is being requested for play
     */
    public void receivePlayRequest(MainDisplayItem sender, CollectionLightWeight song) {
        homepagePresenter.receivePlaylistItemClick(this, song, mainDisplayModel.getPlaylist());
    }

    /**
     * Receives a click from the Homepage and sends it to the MainDisplay
     *
     * @param sender - Homepage sending the click
     * @param obj    - Playlist that was clicked and will be set for display
     */
    public void receivePlaylistItemClick(HomepagePresenter sender, Playlist obj) {
        mainDisplayModel.setPlaylist(obj);
        showPlaylist(obj);
    }

    //region Getters
    public Parent getView() {
        return view;
    }

    public ProxyInterface getProxy() {
        return clientProxy;
    }
    //endregion
}
