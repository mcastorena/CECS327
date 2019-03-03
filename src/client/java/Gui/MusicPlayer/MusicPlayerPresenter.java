package Gui.MusicPlayer;

import app.Main;
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

import Gui.Homepage.HomepagePresenter;
import Gui.MainDisplay.MainDisplayPresenter;
import model.Collection;
import model.Playlist;
import rpc.CECS327InputStream;
import rpc.ProxyInterface;

import java.io.IOException;
import java.util.ArrayList;


public class MusicPlayerPresenter {
    ArrayList<Collection> playlist;
    Collection currentSong;

    private MainDisplayPresenter mainDisplayPresenter;
    private Node view;
    private boolean isPlaying;
    private BasicPlayer myPlayer = new BasicPlayer();
    private String songFile;
//    private Image pause;
//    private Image play;

    @FXML private Slider slider;
    @FXML private Group playButton;
    @FXML private Group previousButton;
    @FXML private Group nextButton;

//    @FXML
//    private ImageView playImage;

    @FXML private Label songLabel;
    @FXML private Label artistLabel;
    @FXML private Label albumLabel;

    public MusicPlayerPresenter() {
        this.playlist = new ArrayList<>();
    }

    private ProxyInterface clientProxy;

    @FXML
    public void initialize() {
        try {
            isPlaying = false;

            playButton.setOnMouseEntered(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            playButton.setOnMouseExited(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try { togglePlay(); }
                catch (Exception e) { e.printStackTrace(); }
            });

            nextButton.setOnMouseEntered(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            nextButton.setOnMouseExited(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    if (isPlaying) togglePlay();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (playlist != null && playlist.size() > 0 && currentSong != null) {
                    int songNum = findIndex(currentSong);
                    int size = playlist.size();
                    Collection nextSong = playlist.get( (songNum+1)%size );
                    currentSong = nextSong;
                    int nextID = (int)nextSong.getId();
                    //setSongFile(mp3FileName(nextID));
                    setSongFile(Integer.toString(nextID), clientProxy);

                    songLabel.setText(nextSong.getSongTitle());
                    artistLabel.setText(nextSong.getArtistName());
                    albumLabel.setText(nextSong.getRelease().getName());
                }

             });

            previousButton.setOnMouseEntered(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            previousButton.setOnMouseExited(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
            previousButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    if (isPlaying) togglePlay();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (playlist != null && playlist.size() > 0 && currentSong != null) {
                    int songNum = findIndex(currentSong);
                    int size = playlist.size();
                    Collection prevSong = playlist.get( ((songNum-1)+size)%size);
                    int prevID = (int)prevSong.getId();
                    currentSong = prevSong;

                    //setSongFile(mp3FileName(prevID));
                    setSongFile(Integer.toString(prevID), clientProxy);

                    songLabel.setText(prevSong.getSongTitle());
                    artistLabel.setText(prevSong.getArtistName());
                    albumLabel.setText(prevSong.getRelease().getName());
                }

            });

            slider.setValue(50);
            slider.setOnMouseEntered(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.HAND));
            slider.setOnMouseExited(e -> Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT));
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

    public MusicPlayerPresenter(MainDisplayPresenter mainDisplayPresenter) {
        try {
            this.mainDisplayPresenter = mainDisplayPresenter;
            clientProxy = mainDisplayPresenter.getProxy();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MusicPlayer.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void togglePlay() throws Exception {
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
//            playImage.setImage(pause);
        } else {
            try {
                myPlayer.pause();
//                playImage.setImage(play);
                isPlaying = false;
                return;
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSongFile(String songFile, ProxyInterface proxy) {
        try {
            this.songFile = songFile;
            myPlayer.open(new CECS327InputStream(Long.valueOf(songFile), proxy));
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public Node getView() {
        return view;
    }

    // play song
    public void receivePlaylistItemPlayRequest(HomepagePresenter sender, Collection song, Playlist playlist) {
        this.playlist = playlist.getSongList();
        this.currentSong = song;

        songLabel.setText(song.getSongTitle());
        artistLabel.setText(song.getArtistName());
        albumLabel.setText(song.getRelease().getName());
        try {
            int songId = (int)song.getId();
            String filename = Integer.toString(songId);
            setSongFile(Integer.toString(songId), clientProxy);
//            HashMap<Integer,String> playableSongs = Resources.getPlayableSongs();
            //HashSet<Integer> ownedIDs = Resources.getOwnedIDs();
            //if (ownedIDs.contains(songId)) {
//                String filename = "./music/" + songId + ".mp3";
                //String filename = mp3FileName(songId);
                //File file = new File(filename);
                //if (file.exists()) {

//                    setSongFile(filename);
//                    togglePlay();
//                }
//                else
//                    displayNotPlayableError();
//            }
//            else {
//                displayNotPlayableError();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayNotPlayableError() {
        new Alert(Alert.AlertType.ERROR, "This song isn't in the playable library.", ButtonType.OK)
                .showAndWait();
    }

//    private String mp3FileName(int songId) {
//        //return getClass().getResource("/music/" + songId + ".mp3").getPath();
//        return getClass().getResource("/music/" + songId).getPath(); //CHANGE TO SERVER
//    }

    private int findIndex(Collection song) {
        if (playlist == null || playlist.size() < 1) {
            return -1;
        }

        int index = 0;
        for (Collection c : playlist) {
            if (c.getId() == song.getId())
                return index;
            index++;
        }
        return index;
    }
}