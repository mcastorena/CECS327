package client.app;

import client.gui.Homepage.HomepagePresenter;
import client.gui.Landing.LandingModel;
import client.gui.Landing.LandingPresenter;
import client.gui.Landing.LandingService;
import client.gui.MainDisplay.MainDisplayModel;
import client.gui.MainDisplay.MainDisplayPresenter;
import client.gui.MainDisplay.SongSearchModel;
import client.gui.MusicPlayer.MusicPlayerPresenter;
import client.gui.PlaylistList.PlaylistListPresenter;
import client.gui.SearchBar.SearchBarPresenter;
import client.rpc.ProxyInterface;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller used for connecting with the Application
 */
public class Controller {

    /**
     * Primary window of application
     */
    Stage stage;

    /**
     * Proxy used for connection
     */
    ProxyInterface proxy;

    /**
     * Presenter for the Landing page
     */
    LandingPresenter landingPresenter;

    /**
     * Presenter for the Homepage
     */
    HomepagePresenter homepagePresenter;

    /**
     * Presenter for the Main Display
     */
    MainDisplayPresenter mainDisplayPresenter;

    /**
     * Presenter for the List of Playlists
     */
    PlaylistListPresenter playlistListPresenter;

    /**
     * Presenter for the Searchbar
     */
    SearchBarPresenter searchBarPresenter;

    /**
     * Presenter for the Music Player
     */
    MusicPlayerPresenter musicPlayerPresenter;

    /**
     * Constructor using a supplied JavaFX stage and Proxy
     *
     * @param stage - JavaFX Stage
     * @param proxy - Proxy
     */
    public Controller(Stage stage, ProxyInterface proxy) {
        this.proxy = proxy;

        landingPresenter = new LandingPresenter(
                this,
                proxy,
                new LandingModel(),
                LandingService.getInstance(proxy)
        );

        mainDisplayPresenter = new MainDisplayPresenter(
                new MainDisplayModel(),
                homepagePresenter,
                new SongSearchModel(),
                proxy
        );

        playlistListPresenter = new PlaylistListPresenter(
                mainDisplayPresenter,
                homepagePresenter,
                proxy
        );

        searchBarPresenter = new SearchBarPresenter(mainDisplayPresenter);
        musicPlayerPresenter = new MusicPlayerPresenter(mainDisplayPresenter);
        homepagePresenter = new HomepagePresenter(
                proxy,
                mainDisplayPresenter,
                playlistListPresenter,
                searchBarPresenter,
                musicPlayerPresenter
        );

        this.stage = stage;
        this.stage.setScene(new Scene(landingPresenter.getView()));
    }

    /**
     * Receive and attempt to authorize a user via a login request
     *
     * @param sender   - JavaFX element sending the request
     * @param username - Supplied username
     * @param password - Supplied password
     * @throws IllegalAccessException
     */
    public void receiveLoginRequest(LandingPresenter sender, String username, String password) throws IllegalAccessException {
        try {
            if (LandingService.getInstance(proxy)
                    .authorizeUser(username, password)) {
                playlistListPresenter.loadPlaylists();
                goToHomepage();
            } else {
                throw new IllegalAccessException("Invalid login info.");
            }
        } catch (IOException e) {
            System.out.println("Error authorizing user.");
            e.printStackTrace();
        }
    }

    /**
     * Inform the Application to set the Stage to the Homepage
     */
    public void goToHomepage() {
        stage.setScene(new Scene(homepagePresenter.getView()));
    }


}
