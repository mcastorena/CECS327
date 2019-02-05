import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// mp3Player is a Singleton because we only want 1 song to play at a time.
public class mp3Player extends JFrame {

    private static mp3Player instance;
    private static MusicPlayer musicPlayer;
    static User loggedInUser;  // TODO: Reference later
    static Profile userProfile;

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
    private ListSelectionListener selectedSong;
    private JButton addPlaylistBtn;

    // Right-click Playlist pane controls
    private JPopupMenu playlistOptions = new JPopupMenu();                                     // Set up playlist list rightclick pop-up menu
    private JMenuItem deletePlaylistButton = new JMenuItem("Delete playlist");           // Set up delete playlist button
    private String delete = "";                                                               // Holds playlist title to delete

    // TODO: For future placement of songs, i.e., SearchView
    private JScrollPane songsPane;
    private JList<Object> songsList;
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

        // TODO: Make deleting playlist an option that appears when you right-click an item

        addPlaylistBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // TODO: Create pop-up for creating a playlist

                System.out.println("Adding playlist");
            }
        });

        selectedSong = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String plTitle = playlistList.getSelectedValue().toString();    // Title of selected playlist
                    Playlist playlist = userProfile.getPlaylist(plTitle);           // Playlist object
                    ArrayList<String> songs = playlist.getSongList();               // Song list
                    songsList.setListData(songs.toArray());
                }
            }
        };

        playlistList.addListSelectionListener(selectedSong);
        songsList.addListSelectionListener(selectedSong);

        playlistOptions.add(deletePlaylistButton);                                  // Add delete playlist button to pop-up menu

        playlistList.addMouseListener( new MouseAdapter()                           // Add right-click action listener to playlist list
        {
            public void mousePressed(MouseEvent e)
            {
                if ( SwingUtilities.isRightMouseButton(e) )
                {
                    JList list = (JList)e.getSource();
                    int row = list.locationToIndex(e.getPoint());
                    list.setSelectedIndex(row);
                    delete = list.getModel().getElementAt(row).toString();          // Set delete strign as selected playlist title
                    playlistOptions.show(e.getComponent(), e.getX(), e.getY());     // Show pop-up menu
                }
            }

        });

        deletePlaylistButton.addActionListener(new ActionListener() {           // Add action listener to delete button

            public void actionPerformed(ActionEvent e) {
                System.out.println("Deleting: "+ delete);
                userProfile.removePlaylist(delete);
                showPlaylists(userProfile.playlists.keySet().toArray());
            }
        });



    }

    public static mp3Player getInstance() {
        if (instance == null) {
            instance = new mp3Player();
        }

        return instance;
    }


    public JButton getSkipButton()
    {
        return skipButton;
    }

    public JButton getBackButton()
    {
        return backButton;
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

    /**
     * Lists the User's already created playlists
     *
     * @param playlistTitles - Array of playlist titles pulled from `user.json`
     */
    public void showPlaylists(Object[] playlistTitles) {
        playlistList.setListData(playlistTitles);
    }

    /**
     * Called when the "Create" button is clicked
     *
     * @param pName - Name of the playlist to be created
     * @return - true if the playlist was created
     */
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

    public static void main(String[] args) {

        SearchView searchView;
        loggedInUser = testData();  // TODO: Authentic user from Sign-In

        // TODO: If not a user, DON'T LET THEM IN, i.e., re-prompt for credentials
        if (loggedInUser != null) {

            // Authentic user -> grab their Playlists
            userProfile = new Profile();
            HashMap<String, Playlist> usersPLs;

            //region Process user.JSON
            try {
                FileReader fr = new FileReader("user.json");
                JsonReader jr = new JsonReader(fr);
                String usersField, usernameField, username, passwordField, password, emailField, email,
                        playlistsField, playlistTitle, songID;

                jr.beginObject();   // parse first '{'
                usersField = jr.nextName(); // parse key "users"
                jr.beginArray();    // parse '[' following "users" : array of user objects
                jr.beginObject();   // parse '{' for the first obj in the "users" array
                usernameField = jr.nextName();  // key: "username"
                username = jr.nextString();     // value: "user"
                passwordField = jr.nextName();  // key: "password"
                password = jr.nextString();     // value: "pass"
                emailField = jr.nextName();     // key: "email"
                email = jr.nextString();        // value: "example@email.com"
                playlistsField = jr.nextName(); // key for the array of "playlists"
                jr.beginArray();    // parse '[' of the "playlists" array

                while (jr.hasNext()) {
                    jr.beginObject();   // parse '{'
                    playlistTitle = jr.nextName();

                    userProfile.addPlaylist(playlistTitle, new Playlist(playlistTitle));

                    System.out.println(playlistTitle);

                    jr.beginArray();    // parse '['
                    while (jr.hasNext()) {
                        songID = jr.nextString();    // each song ID in the playlist array
                        userProfile.getPlaylist(playlistTitle).addToPlaylist(songID);
                    }

                    System.out.println();
                    jr.endArray();  // parse ']' to close the playlist array
                    jr.endObject(); // parse '}' to close the "playlist" object in "playlists"
                }
                jr.endArray();  // Close playlists array
                jr.endObject(); // Close current user obj
                jr.endArray();  // Close users array
                jr.endObject(); // Marks the end of the JSON file

            } catch (IOException e) {
                e.printStackTrace();
            }
            //endregion

            usersPLs = userProfile.playlists;       // Get the users playlists as a HashMap
            System.out.println(usersPLs.keySet());  // get the playlists titles
            Object[] titles = usersPLs.keySet().toArray();  // Turn keyset to an Array[]


            //region Process music.JSON file
            Gson gson = new Gson();
            List<Collection> collection = new ArrayList<>();

            try {
                BufferedReader br = new BufferedReader(new FileReader("music.json"));
                JsonReader jsonReader = new JsonReader(br);

                // Clear the first "[" in the json file
                jsonReader.beginArray();

                // Parse the json file
                while (jsonReader.hasNext()) {
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
                    for (int i = 0; i < collection.size(); i++) {
                        Artist artistInCollection = collection.get(i).getArtist();

                        if (artistInCollection.getName().equals(artist.getName())) {
                            oldArtist = true;
                            collection.add(new Collection(release, artistInCollection, song));
                            break;
                        }
                    }
                    if (!oldArtist) {
                        collection.add(new Collection(release, artist, song));
                    }
                }
                jsonReader.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //endregion JSON Processing

            searchView = new SearchView(collection);
            searchView.pack();
            searchView.setVisible(true);

            // Open player
            mp3Player myPlayer = mp3Player.getInstance();
            myPlayer.pack();
            myPlayer.showPlaylists(titles); // Show playlist titles
            myPlayer.setVisible(true);

            //region GUI Upgrade?
//        musicPlayer = MusicPlayer.getInstance();
//        musicPlayer.setCollection(collection);
//
//        musicPlayer.pack();
//        musicPlayer.setVisible(true);
            //endregion?
        }
    }

    public static User testData() {
        User testUser = new User("user", "example@email.com", "pass");

        return testUser;
    }
}
