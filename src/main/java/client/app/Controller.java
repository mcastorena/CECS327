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

public class Controller {
    Stage stage; // The primary window of our app.

    ProxyInterface proxy;
    LandingPresenter landingPresenter;
    HomepagePresenter homepagePresenter;
    MainDisplayPresenter mainDisplayPresenter;
    PlaylistListPresenter playlistListPresenter;
    SearchBarPresenter searchBarPresenter;
    MusicPlayerPresenter musicPlayerPresenter;

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
                                    new SongSearchModel()
                                );

        playlistListPresenter = new PlaylistListPresenter(
                                    mainDisplayPresenter,
                                    homepagePresenter
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

    public void receiveLoginRequest(LandingPresenter sender, String username, String password) throws IllegalAccessException {
        try {
            if (LandingService.getInstance(proxy)
                            .authorizeUser(username, password)) {
                goToHomepage();
            }
            else {
                throw new IllegalAccessException("Invalid login info.");
            }
        } catch (IOException e) {
            System.out.println("Error authorizing user.");
            e.printStackTrace();
        }
    }

    public void goToHomepage() {
        stage.setScene(new Scene(homepagePresenter.getView()));
    }
}
