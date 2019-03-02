package Gui.Homepage;

import Gui.PlaylistList.PlaylistListPresenter;
import app.Main;
import data.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import Gui.MusicPlayer.MusicPlayerPresenter;
import Gui.SearchBar.SearchBarPresenter;
import Gui.MainDisplay.MainDisplayPresenter;
import model.Collection;
import model.Playlist;
import model.SearchResult;
import model.User;
import rpc.Proxy;
import rpc.ProxyInterface;

import java.io.IOException;

public class HomepagePresenter {
//    private HomepageModel homepageModel;

    private MainDisplayPresenter mainDisplayPresenter;
    private PlaylistListPresenter playlistListPresenter;
    private SearchBarPresenter searchBarPresenter;

    private MusicPlayerPresenter musicPlayerPresenter;

//    private PlaylistPagePresenter playlistPagePresenter;

    private Parent view;

    private ProxyInterface clientProxy;

//    private PlaylistListView playlistListView;
//    private SearchBarView searchBarView;
//    private static musicPlayer;

//    private static List<Playlist> playlists;

    @FXML
    private GridPane gridPane;

    public HomepagePresenter(ProxyInterface proxy) {
        clientProxy = proxy;
        try {
//            homepageModel = new HomepageModel();
//            homepageModel.setUser(UserSession.getCurrentSession());

            mainDisplayPresenter = new MainDisplayPresenter(this);
            playlistListPresenter = new PlaylistListPresenter(this.mainDisplayPresenter, this);
            searchBarPresenter = new SearchBarPresenter(mainDisplayPresenter);
            musicPlayerPresenter = new MusicPlayerPresenter(this.mainDisplayPresenter);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/HomepageAlt.fxml"));
            loader.setController(this);
            view = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Note: you can only modify javafx objects loaded from FXML (i.e. adding elements)
     *  after calling FXMLLoader.load(). This is invoked when calling any of the xView constructors.
     */
    public void initialize() {
        Text profileText = new Text(UserSession.getCurrentSession().getUsername());
        profileText.requestFocus(); // doesn't work
        gridPane.add(profileText, 0, 0);
        gridPane.add(searchBarPresenter.getView(), 3, 0, 3, 1);
        gridPane.add(playlistListPresenter.getView(), 0, 2, 2, 4);
        gridPane.add(mainDisplayPresenter.getView(), 3,2, 3, 3);
        gridPane.add(musicPlayerPresenter.getView(), 2,6);
    }

//    public HomepagePresenter()  {
//        homepageView = new HomepageView();
//        playlistListView = new PlaylistListView();
//        playlists = new ArrayList<>();
//        playlists.addAll(user.getUserProfile().getPlaylists().values());

//        PlaylistListView pllView = new PlaylistListView();
//        playlistListView = pllView.loadView();
//        PlaylistListPresenter pllPresenter = pllView.getLoader().<PlaylistListPresenter>getController();
//        System.out.println(user);
//        System.out.println(playlists);
//        pllPresenter.initData(user, playlists);

//        searchBarView = new SearchBarView();
//        musicPlayer = new MusicPlayerView().loadView();
//    }

//    public void initData(User user) {
//        this.user = user;
//    }

//    public static User getUser() {
//        return user;
//    }

//    public static void setUser(User user1) {
//        user = user1;
//    }

//    public static List<Playlist> getPlaylists() {
//        return playlists;
//    }

//    public static void addPlaylist(Playlist playlist) {
//        playlists.add(playlist);
//    }

//    public static Node getSearchBarView() {
//        return searchBarView;
//    }

//    public static Node getPlaylistListView() {
//        return playlistListView;
//    }

//    public static Node getMusicPlayer() {
//        return musicPlayer;
//    }

    public void showDefaultPage() {
//        homepageView = new HomepageView();
        Scene scene = new Scene(view);
        Main.getPrimaryStage().setScene(scene);
    }

    public void displayDefaultHomepage() {
//        Scene scene = new Scene(homepageView.loadView());
//        Main.getPrimaryStage().setScene(scene);
//        Main.getPrimaryStage().show();
    }

//    public void displayPlaylistPage() {
//        gridPane.add(playlistPagePresenter.getView(), 1, 0, 1, 2);
//    }

    public void receivePlaylistItemClick(MainDisplayPresenter sender, Collection song, Playlist playlist) {
        musicPlayerPresenter.receivePlaylistItemPlayRequest(this, song, playlist);
    }

    public void receiveSearchResults(SearchBarPresenter sender, SearchResult results) {

    }

    public void receivePlaylistItemClick(PlaylistListPresenter sender, Playlist obj) {
        mainDisplayPresenter.receivePlaylistItemClick(this, obj);
    }

    public ProxyInterface getProxy()
    {
        return clientProxy;
    }
}
