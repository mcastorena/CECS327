import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class mp3Player extends JFrame {
    private JButton playButton;
    private JButton skipButton;
    private JButton backButton;
    private JSlider volumeSlider;
    private JPanel contentPane;
    private JButton addPlaylistBtn;

    BasicPlayer myPlayer = new BasicPlayer();       // Creates basic player object to play music
    String mp3File;                     // Stores files location of mp3 to be played
    boolean isPlaying = false;          // Boolean stores whether a mp3 is currently being played or not
    double playerVolume;                // Stores player volume

    fileLocationInputWindow myInput;            // Input window for mp3 file location

    public mp3Player() {
        setContentPane(contentPane);                        // Setup window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mp3File == null) {           // Check to see if there is a song to be played, if not prompt user to play one
                    //getFileLocation();

                    JFileChooser myFileChooser = new JFileChooser();                // Create new JFileChooser object
                    int choice = myFileChooser.showOpenDialog(null);        // Show file chooser window with open option selected
                    if (choice == JFileChooser.APPROVE_OPTION) {                   // If user selects a file and hits open, then store selected filepath into string
                        mp3File = myFileChooser.getSelectedFile().getAbsolutePath();
                        System.out.println(mp3File);
                    }

                    try {
                        myPlayer.open(new File(mp3File));           //
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        System.out.println("File opened, trying to play");
                        myPlayer.play();
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                    isPlaying = true;                               // Set isPlaying flag to true

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
        addPlaylistBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create pop-up for creating a playlist
                System.out.println("Adding playlist");
            }
        });
    }

    private void getFileLocation() {
        myInput = new fileLocationInputWindow();        // Setup and display input prompt window
        myInput.pack();
        myInput.setVisible(true);
        myInput.setModal(true);

        mp3File = myInput.getFileLocation();            // Save and get file location input
    }

    // TODO: Finish this
    private void showPlaylists() {
        // Grab Playlists titles from User profile JSON
        // Display titles above "+ New Playlist"
    }

    private boolean createPlaylist() {
        // TODO: FINISH
        boolean created = false;
        String pID;
        String pName = "hello";

        if (!pName.equals("")) {
            pID = "";   // TODO: Automate somehow
            Playlist newPlaylist = new Playlist(pID, pName);

            System.out.println("Playlist is created");
            created = true;
        }

        return created;
    }

    private boolean deletePlaylist() {
        // TODO:
        boolean deleted = false;
        Playlist playlist = new Playlist("100", "Name");    // TODO: Change to user selected playlist

        // TODO: right-click and choose delete

        if (playlist != null) {
            playlist = null;    // Let garbage collection handle it
            deleted = true;
        }

        return deleted;
    }

    public static void main(String[] args) {
        mp3Player myPlayer = new mp3Player();
        myPlayer.pack();
        myPlayer.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        volumeSlider = new JSlider();
        contentPane.add(volumeSlider, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        skipButton = new JButton();
        skipButton.setText(">|");
        contentPane.add(skipButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("|<");
        contentPane.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playButton = new JButton();
        playButton.setText("►");
        contentPane.add(playButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
