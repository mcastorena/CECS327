import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// mp3Player is a Singleton because we only want 1 song to play at a time.
public class mp3Player extends JFrame {
    private JButton playButton;
    private JButton skipButton;
    private JButton backButton;
    private JSlider volumeSlider;
    private JPanel contentPane;
    private static mp3Player instance;

    private BasicPlayer myPlayer = new BasicPlayer();       // Creates basic player object to play music
    private String mp3File;                     // Stores files location of mp3 to be played
    private boolean isPlaying = false;          // Boolean stores whether a mp3 is currently being played or not
    private double playerVolume;                // Stores player volume

    fileLocationInputWindow myInput;            // Input window for mp3 file location

    private mp3Player() {
        setContentPane(contentPane);                        // Setup window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MP3 Player");

        //volumeSlider.setMaximum(6);                         //set max gain
        //volumeSlider.setMinimum(0);                         //set min gain


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
                        isPlaying = true;
                    } catch (BasicPlayerException e1) {
                        e1.printStackTrace();
                    }
                    isPlaying = true;                               // Set isPlaying flag to true
                    playButton.setText("||");                       // Change play button to pause button
                    return;
                }
                if (isPlaying) {                                      // If a song is currently playing, pause the song
                    try {
                        myPlayer.pause();                           // Pause the song
                        isPlaying = false;                          // Set isPlaying flag to false
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
                        return;
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
    }

    public static mp3Player getInstance()
    {
        if(instance == null)
        {
            instance = new mp3Player();
        }
        return instance;
    }

    public void changeSong(String mp3File)
    {
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

    public String getCurrentSong()
    {
        if(mp3File != null)
        {
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

    public static void main(String[] args) {

        // Process JSON file
        Gson gson = new Gson();
        List<Collection> collection = new ArrayList<>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader("music.json"));
            JsonReader jsonReader = new JsonReader(br);

            // Clear the first "[" in the json file
            jsonReader.beginArray();

            // Parse the json file
            while(jsonReader.hasNext())
            {
                Release release;
                Artist artist;
                Song song;

                // If the artist has already been parsed, don't add new object.
                boolean oldArtist = false;

                // Clear the first "{" for each json object
                jsonReader.beginObject();

                jsonReader.nextName();
                release = gson.fromJson(jsonReader, Release.class);

                jsonReader.nextName();
                artist = gson.fromJson(jsonReader, Artist.class);

                jsonReader.nextName();
                song = gson.fromJson(jsonReader, Song.class);

                jsonReader.endObject();

                // Check if artist was previously parsed
                for(int i = 0; i < collection.size(); i++)
                {
                    Artist artistInCollection = collection.get(i).getArtist();
                    if (artistInCollection.getName().equals(artist.getName()))
                    {
                        oldArtist = true;
                        collection.add(new Collection(release, artistInCollection, song));
                        break;
                    }
                }
                if(!oldArtist)
                {
                    collection.add(new Collection(release, artist, song));
                }
            }
            jsonReader.endArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        } // End Json Processing


        SearchView searchView = new SearchView(collection);
        searchView.pack();
        searchView.setVisible(true);

        // Open mp3
        mp3Player myPlayer = mp3Player.getInstance();
        myPlayer.pack();
        myPlayer.setVisible(true);


    }

}
