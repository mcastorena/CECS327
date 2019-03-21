package client.gui.MusicPlayer;

import client.gui.Homepage.HomepagePresenter;
import client.gui.MainDisplay.MainDisplayPresenter;
import client.app.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import client.model.CollectionLightWeight;
import client.model.Playlist;
import client.rpc.CECS327InputStream;
import client.rpc.ProxyInterface;

import java.io.BufferedInputStream;
import java.util.ArrayList;


/**
 * As part of the MVP-design pattern, this class is the Presenter for the MusicPlayer
 */
public class MusicPlayerPresenter {

    /**
     * List of Playlists
     */
    ArrayList<CollectionLightWeight> playlist;

    /**
     * Current song selected for playing
     */
    CollectionLightWeight currentSong;

    /**
     * Presenter for the MainDisplay associated with this MusicPlayer
     */
    private MainDisplayPresenter mainDisplayPresenter;

    /**
     * TODO:
     */
    private Node view;

    /**
     * TODO:
     */
    private boolean isPlaying;

    /**
     * TODO:
     */
    private BasicPlayer myPlayer = new BasicPlayer();

    /**
     * Name of the song file to be played
     */
    private String songFile;

    /**
     * Proxy that the client is connected through
     */
    private ProxyInterface clientProxy;

    //region FXML components
    @FXML
    private Slider slider;
    @FXML
    private Group playButton;
    @FXML
    private Group previousButton;
    @FXML
    private Group nextButton;
    @FXML
    private Label songLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label albumLabel;
    //endregion


    /**
     * Constructor
     */
    public MusicPlayerPresenter() {
        this.playlist = new ArrayList<>();
    }

    /**
     * Required for JavaFX; variables annotated with @FXML are accessed here.
     */
    @FXML
    public void initialize() {
        try {
            isPlaying = false;

            //region Play button
            playButton.setOnMouseEntered(e -> App.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            playButton.setOnMouseExited(e -> App.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    togglePlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //endregion

            //region Next button
            nextButton.setOnMouseEntered(e -> App.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            nextButton.setOnMouseExited(e -> App.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    if (isPlaying) togglePlay();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (playlist != null && playlist.size() > 0 && currentSong != null) {
                    int songNum = findIndex(currentSong);
                    int size = playlist.size();
                    CollectionLightWeight nextSong = playlist.get((songNum + 1) % size);
                    currentSong = nextSong;
                    int nextID = (int) nextSong.getId();
                    setSongFile(Integer.toString(nextID), clientProxy);

                    songLabel.setText(nextSong.getSongTitle());
                    artistLabel.setText(nextSong.getArtistName());
                    albumLabel.setText(nextSong.getReleaseName());
                }
            });
            //endregion

            //region Previous button
            previousButton.setOnMouseEntered(e -> App.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            previousButton.setOnMouseExited(e -> App.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            previousButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    if (isPlaying) togglePlay();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (playlist != null && playlist.size() > 0 && currentSong != null) {
                    int songNum = findIndex(currentSong);
                    int size = playlist.size();
                    CollectionLightWeight prevSong = playlist.get(((songNum - 1) + size) % size);
                    int prevID = (int) prevSong.getId();
                    currentSong = prevSong;

                    setSongFile(Integer.toString(prevID), clientProxy);

                    songLabel.setText(prevSong.getSongTitle());
                    artistLabel.setText(prevSong.getArtistName());
                    albumLabel.setText(prevSong.getReleaseName());
                }
            });
            //endregion

            //region Slider
            slider.setValue(50);
            slider.setOnMouseEntered(e -> App.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            slider.setOnMouseExited(e -> App.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
        }

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    myPlayer.setGain((Double) newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * TODO:
     *
     * @param mainDisplayPresenter - TODO
     */
    public MusicPlayerPresenter(MainDisplayPresenter mainDisplayPresenter) {
        try {
            this.mainDisplayPresenter = mainDisplayPresenter;
            clientProxy = mainDisplayPresenter.getProxy();

            // Required for JavaFX to load the UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/MusicPlayer.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Toggles between playing and pausing the song.
     *
     * @throws Exception - Required for the BasicPlayer
     */
    private void togglePlay() throws Exception {
        if (!isPlaying) {
            if (!songFile.isEmpty() && myPlayer.getStatus() == BasicPlayer.PAUSED) {
                myPlayer.resume();
            } else {
                try {
                    myPlayer.play();
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }
            isPlaying = true;
        } else {
            try {
                myPlayer.pause();
                isPlaying = false;
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a song request and opens that request in the MusicPlayer.
     *
     * @param songFile - Name of the song/file to be played
     * @param proxy - Proxy that the client is connected through
     */
    private void setSongFile(String songFile, ProxyInterface proxy) {
        try {
            this.songFile = songFile;
            myPlayer.open(new BufferedInputStream(new CECS327InputStream(Long.valueOf(songFile), proxy)));
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a play request for a selected song and tries to set it for playing.
     *
     * @param sender - Homepage sending the request for play
     * @param song - Song selected for playing
     * @param playlist - Playlist containing the song to be played
     */
    public void receivePlaylistItemPlayRequest(HomepagePresenter sender, CollectionLightWeight song, Playlist playlist) {
        //this.playlist = playlist.getSongList();
        this.currentSong = song;

        songLabel.setText(song.getSongTitle());
        artistLabel.setText(song.getArtistName());
        albumLabel.setText(song.getReleaseName());

        try {
            int songId = (int) song.getId();
            String filename = Integer.toString(songId);
            setSongFile(Integer.toString(songId), clientProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an error to the user that the selected song is not playable.
     */
    private void displayNotPlayableError() {
        new Alert(Alert.AlertType.ERROR, "This song isn't in the playable library.", ButtonType.OK)
                .showAndWait();
    }

    /**
     * Gets the index of the song as it is in the playlist
     *
     * @param song - Song object
     * @return - Index of the song within the playlist; else -1
     */
    private int findIndex(CollectionLightWeight song) {
        // Null check
        if (playlist == null || playlist.size() < 1) {
            return -1;
        }

        int index = 0;
        for (CollectionLightWeight c : playlist) {
            if (c.getId() == song.getId())
                return index;
            index++;
        }
        return index;
    }

    public Node getView() {
        return view;
    }
}