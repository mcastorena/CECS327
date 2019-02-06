import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MusicPlayer extends JFrame {

    private static MusicPlayer mpInstance;  // Single instance of Music Player
    private List<Collection> collection;    // Sets the players collection of songs
    private String songFile;                // Stores file location of song to be played
    private boolean isPlaying = false;      // Stores whether a song is playing

    private BasicPlayer myPlayer = new BasicPlayer();

    //region Swing components
    private JPanel playerPane;

    //region Main Panel
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchBtn;
    private JList<Collection> songsList;
    private JList<Collection> artistsList;
    //endregion

    //region Control Panel
    private JPanel controlPanel;
    private JButton playBtn;
    private JSlider volumeSlider;
    private JButton nextBtn;
    private JButton prevBtn;
    //endregion

    //region Left Scroll Pane
    private JScrollPane leftScrollPane;
    private JList<Collection> playlistTitles;
    //endregion

    //region Left Fixed Panel
    private JPanel leftFixedPanel;
    private JButton newPlaylistBtn;
    //endregion

    //endregion

    public static MusicPlayer getInstance() {

        if (mpInstance == null) {
            mpInstance = new MusicPlayer();
        }

        return mpInstance;
    }

    /**
     * Public constructor
     */
    private MusicPlayer() {
        setContentPane(playerPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MP3 Player");

        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (songFile == null) {
                    JFileChooser fileChooser = new JFileChooser();
                    int choice = fileChooser.showOpenDialog(null);

                    if (choice == JFileChooser.APPROVE_OPTION) {
                        songFile = fileChooser.getSelectedFile().getAbsolutePath();
                        System.out.println(songFile);
                    }

                    try {
                        myPlayer.open(new File(songFile));
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        System.out.println("File opened - trying to play");
                        myPlayer.play();
                        isPlaying = true;
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }

                    isPlaying = true;
                    playBtn.setText("||");  // Change play sign to pause
                    return;
                }

                if (isPlaying) {
                    try {
                        myPlayer.pause();
                        isPlaying = false;
                        return;
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                }

                if (!isPlaying && !songFile.isEmpty()) {
                    try {
                        myPlayer.resume();
                        isPlaying = true;
                        playBtn.setText("||");
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }
}
