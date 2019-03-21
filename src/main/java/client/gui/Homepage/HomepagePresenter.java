package client.gui.Homepage;

import client.gui.MainDisplay.MainDisplayPresenter;
import client.gui.MusicPlayer.MusicPlayerPresenter;
import client.gui.PlaylistList.PlaylistListPresenter;
import client.gui.SearchBar.SearchBarPresenter;
import client.app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import client.model.CollectionLightWeight;
import client.model.Playlist;
import client.rpc.ProxyInterface;

import java.io.IOException;

/**
 * As part of the MVP-design pattern, this class represents the Presenter for the Homepage
 */
public class HomepagePresenter {

    /**
     * The parent view that contains this HomepagePresenter
     */
    private Parent view;

    /**
     * The Proxy that the client is connected through
     */
    private ProxyInterface clientProxy;

    //region Presenters
    private MainDisplayPresenter mainDisplayPresenter;
    private PlaylistListPresenter playlistListPresenter;
    private SearchBarPresenter searchBarPresenter;
    private MusicPlayerPresenter musicPlayerPresenter;
    //endregion

    //region FXML components
    @FXML
    private GridPane gridPane;
    //endregion

    /**
     * Public constructor
     *
     * @param proxy - Proxy that the client is connecting through
     */
    public HomepagePresenter(ProxyInterface proxy) {
        clientProxy = proxy;

        try {
            // Instantiate the object's associated Presenters
            mainDisplayPresenter = new MainDisplayPresenter(this);
            playlistListPresenter = new PlaylistListPresenter(this.mainDisplayPresenter, this);
            searchBarPresenter = new SearchBarPresenter(mainDisplayPresenter);
            musicPlayerPresenter = new MusicPlayerPresenter(this.mainDisplayPresenter);

            // Loader required for JavaFX to set the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/client/ui/HomepageAlt.fxml"));
            loader.setController(this);
            view = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Note: you can only modify javafx objects loaded from FXML (i.e. adding elements)
     * after calling FXMLLoader.load(). This is invoked when calling any of the xView constructors.
     */
    public void initialize() {
        // TODO: Fix this with login dispatcher
        //Text profileText = new Text(UserSession.getCurrentSession().getUsername());
        //profileText.requestFocus(); // doesn't work
        //gridPane.add(profileText, 0, 0);
        gridPane.add(searchBarPresenter.getView(), 3, 0, 3, 1);
        gridPane.add(playlistListPresenter.getView(), 0, 2, 2, 4);
        gridPane.add(mainDisplayPresenter.getView(), 3, 2, 3, 3);
        gridPane.add(musicPlayerPresenter.getView(), 2, 6);
    }

    public void showDefaultPage() {
        Scene scene = new Scene(view);
        App.getPrimaryStage().setScene(scene);
    }

    /**
     * Receives a click from the MainDisplay and sends it to the associated MusicPlayer.
     *
     * @param sender - Presenter sending the click
     * @param song - Song that was clicked
     * @param playlist - Playlist associated with the song clicked
     */
    public void receivePlaylistItemClick(MainDisplayPresenter sender, CollectionLightWeight song, Playlist playlist) {
        musicPlayerPresenter.receivePlaylistItemPlayRequest(this, song, playlist);
    }

    /**
     * Receives a click from the PlaylistList and sends it to the associated MainDisplay.
     *
     * @param sender - PlaylistList sending the click
     * @param obj - Playlist that was clicked
     */
    public void receivePlaylistItemClick(PlaylistListPresenter sender, Playlist obj) {
        mainDisplayPresenter.receivePlaylistItemClick(this, obj);
    }

    /**
     * Gets the proxy that the client is connected through
     *
     * @return - Proxy that the client is connected through
     */
    public ProxyInterface getProxy() {
        return clientProxy;
    }
}