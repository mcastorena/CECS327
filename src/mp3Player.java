import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

// mp3Player is a Singleton because we only want 1 song to play at a time.
public class mp3Player extends JFrame {

    private static mp3Player instance;

    //region Swing components
    private JPanel contentPane;

    // Player controls
    private JButton backButton;
    private JButton playButton;
    private JButton skipButton;
    private JSlider volumeSlider;

    // Playlist controls
    private JScrollPane playlistsPane;
    private JList<Object> playlistList;
    private JButton addPlaylistBtn;

    // TODO: For future placement of songs, i.e., SearchView
    private JScrollPane songsPane;
    private JList songsList;
    //endregion

    private BasicPlayer myPlayer = new BasicPlayer();       // Creates basic player object to play music
    private String mp3File;                     // Stores files location of mp3 to be played
    private boolean isPlaying = false;          // Boolean stores whether a mp3 is currently being played or not
    private double playerVolume;                // Stores player volume

    fileLocationInputWindow myInput;            // Input window for mp3 file location

    private List<Collection> playlistTitles;

    private mp3Player() {
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MP3 Player");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mp3File == null) {           // Check to see if there is a song to be played, if not prompt user to play one
                    //getFileLocation();

                    JFileChooser myFileChooser = new JFileChooser();            // Create new JFileChooser object
                    int choice = myFileChooser.showOpenDialog(null);    // Show file chooser window with open option selected

                    if (choice == JFileChooser.APPROVE_OPTION) {                // If user selects a file and hits open, then store selected filepath into string
                        mp3File = myFileChooser.getSelectedFile().getAbsolutePath();
                        System.out.println(mp3File);
                    }

                    try {
                        myPlayer.open(new File(mp3File));
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        System.out.println("File opened, trying to play");
                        myPlayer.play();
                        isPlaying = true;
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }

                    isPlaying = true;           // Set isPlaying flag to true
                    playButton.setText("||");   // Change play button to pause button

                    return;
                }

                if (isPlaying) {                // If a song is currently playing, pause the song
                    try {
                        myPlayer.pause();       // Pause the song
                        isPlaying = false;      // Set isPlaying flag to false
                        return;
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                }

                if (!isPlaying && !mp3File.isEmpty()) {                  //If a song is paused, play it again
                    try {
                        myPlayer.resume();
                        isPlaying = true;
                        playButton.setText("||");
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        volumeSlider.addChangeListener(new ChangeListener() {                   //sets mp3Player volume
            @Override
            public void stateChanged(ChangeEvent e) {
                playerVolume = volumeSlider.getValue();                         // Get volumeSlider's value
                playerVolume = (playerVolume / 100);                            // BasicPlayer's setGain() accepts input from 0.0 - 1.0
                System.out.println("Volume slider value:\t" + playerVolume);

                if (isPlaying) {                                                // If a song is playing
                    try {
                        myPlayer.setGain(playerVolume);                         // Set the volume
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        volumeSlider.addChangeListener(new ChangeListener() {                   //sets mp3Player volume
            @Override
            public void stateChanged(ChangeEvent e) {
                playerVolume = volumeSlider.getValue();
                playerVolume = (playerVolume / 100) * 3.02;
                System.out.println("Volume slider value:\t" + playerVolume);

                try {
                    myPlayer.setGain(playerVolume);
                } catch (BasicPlayerException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // TODO: Make deleting playlist an option that appears when you right-click an item

        addPlaylistBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create pop-up for creating a playlist
                System.out.println("Adding playlist");
            }
        });
    }

    public static mp3Player getInstance() {
        if (instance == null) {
            instance = new mp3Player();
        }

        return instance;
    }

    public void changeSong(String mp3File) {
        try {
            myPlayer.stop();
            isPlaying = false;

            this.mp3File = mp3File;
            myPlayer.open(new File(mp3File));
            myPlayer.play();
            isPlaying = true;
            playButton.setText("||");
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentSong() {
        if (mp3File != null) {
            return mp3File;
        }

        return "";
    }

    private void getFileLocation() {
        myInput = new fileLocationInputWindow();        // Setup and display input prompt window
        myInput.pack();
        myInput.setVisible(true);
        myInput.setModal(true);

        mp3File = myInput.getFileLocation();            // Save and get file location input
    }

    // TODO: Finish this
    public void showPlaylists(Object[] playlistTitles) {
        // Grab Playlists titles from User profile JSON
        // Display titles above "+ New Playlist"
        playlistList.setListData(playlistTitles);

        // TODO: playlistTitles = user.playlists or whatever
        // TODO: playlistList.setListData(playlistTitles.toArray());

    }

    private boolean createPlaylist(String pName) {
        // TODO: FINISH
        boolean created = false;
        String pID;

        if (!pName.equals("")) {
            pID = "";   // TODO: Automate somehow
            Playlist newPlaylist = new Playlist(pID, pName);

            // TODO: Also add to `user.json`

            System.out.println("Playlist is created");
            created = true;
        }

        return created;
    }

    private boolean deletePlaylist(Playlist playlist) {

        boolean deleted = false;

        // TODO: right-click and choose delete

        if (playlist != null) {
            playlist = null;    // Let garbage collection handle it

            // TODO: Remove from `user.json`

            deleted = true;
        }

        return deleted;
    }
}
