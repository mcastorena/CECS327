package client.gui.MainDisplay;

import client.gui.Homepage.HomepagePresenter;
import client.gui.PlaylistList.PlaylistListPresenter;
import client.gui.SearchBar.SearchBarPresenter;
import client.data.UserSession;
import client.model.SearchResult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import client.model.CollectionLightWeight;
import client.model.Playlist;
import client.rpc.CECS327InputStream;
import client.rpc.ProxyInterface;
import server.model.Collection;

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
//    @FXML
//    ScrollPane displayScroller;
//    @FXML
//    VBox displayVBox;
    @FXML
    TabPane displayTabPane;
    @FXML
    Tab songTab;
    @FXML
    Tab artistTab;
    @FXML
    Tab playlistTab;
    @FXML
    VBox bySongDisplayVBox;
    @FXML
    VBox byArtistDisplayVBox;
    @FXML
    VBox playlistDisplayVBox;
    @FXML
    ScrollPane byArtistDisplayScroller;
    @FXML
    ScrollPane bySongDisplayScroller;
    @FXML
    ScrollPane playlistDisplayScroller;
    //endregion

    /**
     * Constructor
     *
     * @param hp - Presenter for the Homepage associated with this MainDisplay
     */
    public MainDisplayPresenter(MainDisplayModel mdm, HomepagePresenter hp, SongSearchModel ssm, ProxyInterface proxy) {

        mainDisplayModel = mdm;
        homepagePresenter = hp;
        clientProxy = proxy;
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
        // Disable all display tabs until the user does something.
        songTab.setDisable(true);
        artistTab.setDisable(true);
        playlistTab.setDisable(true);

        // Fit scroll pane sizes to parent
        bySongDisplayScroller.setFitToHeight(true);
        bySongDisplayScroller.setFitToWidth(true);
        byArtistDisplayScroller.setFitToHeight(true);
        byArtistDisplayScroller.setFitToWidth(true);
        playlistDisplayScroller.setFitToHeight(true);
        playlistDisplayScroller.setFitToWidth(true);

        // Bind the VBox sizes to parent (scroll panes)
        bySongDisplayVBox.prefWidthProperty().bind(bySongDisplayScroller.prefViewportWidthProperty());
        bySongDisplayVBox.prefHeightProperty().bind(bySongDisplayScroller.prefViewportHeightProperty());
        byArtistDisplayVBox.prefWidthProperty().bind(byArtistDisplayScroller.prefViewportWidthProperty());
        byArtistDisplayVBox.prefHeightProperty().bind(byArtistDisplayScroller.prefViewportHeightProperty());
        playlistDisplayVBox.prefWidthProperty().bind(playlistDisplayScroller.prefViewportWidthProperty());
        playlistDisplayVBox.prefHeightProperty().bind(playlistDisplayScroller.prefViewportHeightProperty());
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
//    private void showResults(SearchResult result) {
//        // Clear the previous state of the VBox
//        displayVBox.getChildren().clear();
//
//        List<CollectionLightWeight> searchResult = result.getSongResultList();
//
//        // Create a new item for display for each song in `searchResult`
//        for (CollectionLightWeight song : searchResult) {
//            SearchResultSongItem displayItem =
//                    new SearchResultSongItem(this, song);
//
//            displayVBox.getChildren()
//                    .add(displayItem
//                            .getView());
//        }
//    }

    /**
     * Updates the display(s) with new search results.
     * @param result The SearchResult containing a list of songs and a list of songs sorted by artist.
     */
    private void showResults(SearchResult result) {
        // Bring focus to song tab
        displayTabPane.getSelectionModel().select(songTab);

        updateBySongDisplay(result.getSongResultList());
        updateByArtistDisplay(result.getArtistResultList());

    }

    /**
     * Refreshes the 'By Song' results display.
     * @param songResultsList The new list of results, sorted by song name
     */
    private void updateBySongDisplay(List<CollectionLightWeight> songResultsList) {
        // Enable the display tabs
        songTab.setDisable(false);

        bySongDisplayVBox.getChildren().clear();

        for (CollectionLightWeight song : songResultsList) {
            var displayItem = new MainDisplayItem(this, song);
            // Stretch the display item horizontally to fill the width of the display view
            displayItem.songPane.prefWidthProperty().bind(bySongDisplayVBox.widthProperty());

            bySongDisplayVBox.getChildren()
                    .add(displayItem.getView());
        }
    }

    /**
     * Refreshes the 'By Artist' results display. Songs are grouped by artist name.
     * @param artistResultsList The new list of results, sorted by artist name
     */
    private void updateByArtistDisplay(List<CollectionLightWeight> artistResultsList) {
        // Enable the artist tab
        artistTab.setDisable(false);

        byArtistDisplayVBox.getChildren().clear();

        for (int i = 0; i < artistResultsList.size(); i++) {
            var song = artistResultsList.get(i);

            String currentArtist = song.getArtistName();
            String prevArtist = (i > 0 ? artistResultsList.get(i-1).getArtistName() : "");

            // Group songs by artist
            // Check if previous song's artist was the same
            if ( i == 0 || !currentArtist.equals(prevArtist) )
                byArtistDisplayVBox.getChildren().add(new ArtistLabel(currentArtist).getView());

            var displayItem = new MainDisplayItem2(this, song);
            // Stretch item UI to fit display width
            displayItem.songPane.prefWidthProperty().bind(byArtistDisplayVBox.widthProperty());

            byArtistDisplayVBox.getChildren()
                    .add(displayItem.getView());
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
        playlistTab.setDisable(false);
        playlistTab.setText(playlist.getName());
        // Bring focus to playlist tab
        displayTabPane.getSelectionModel().select(playlistTab);

        playlistDisplayVBox.getChildren().clear();
        // Create new items for each song in the playlist
        for (CollectionLightWeight song : playlist.getSongList()) {
            MainDisplayItem displayItem = new MainDisplayItem(this, song);

            // Stretch the display item horizontally to fill the width of the display view
            displayItem.songPane.prefWidthProperty().bind(bySongDisplayVBox.widthProperty());

            // Add the song to the VBox
            playlistDisplayVBox.getChildren()
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
