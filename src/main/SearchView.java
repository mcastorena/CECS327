package main;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import main.java.model.*;
import main.java.utility.Search;

// Displays search bar and results, also plays the song when selected
public class SearchView extends JFrame {
    private JList<Object> list1;
    private JList<Object> list2;

    private JPanel searchListPane;
    private JTextField textField1;
    private JButton SEARCHButton;
    private JPanel searchPanel;

    private ListSelectionListener newSongListener;
    private ListSelectionListener newArtistListener;
    private ActionListener searchActionListener;

    private mp3Player mp3 = mp3Player.getInstance();

    // For displaying/playing songs after an artist is selected
    private boolean useArtistSongList;
    List<Collection> artistsSongs = new ArrayList<>();

    // The entire json collection
    List<Collection> collection;

    SearchView(List<Collection> collection) {
        setContentPane(searchListPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MP3 Player");

        textField1.setText("friend"); // For testing: initialize the search for "friend" (1st 6 songs play)

        this.collection = collection;

        searchActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = textField1.getText();

                // Remove old listeners, needed for > 1 search
                list1.removeListSelectionListener(newSongListener);
                list2.removeListSelectionListener(newArtistListener);

                // Clear out the artists songs if list2 has been looked at previously
                artistsSongs.clear();
                useArtistSongList = false;

                search(searchQuery);
            }
        };

        // Add listener to Search Button and Enter button
        SEARCHButton.addActionListener(searchActionListener);
        textField1.addActionListener(searchActionListener);
    }

    private void search(String searchQuery) {
        SearchResult sResult = Search.search(searchQuery, collection);

        // Retrieve song and artists search results
        List<Collection> songResultList = sResult.getSongResultList();
        List<Collection> artistResultList = sResult.getArtistResultList();

        // Set the results into each list
        list1.setListData(songResultList.toArray());
        list2.setListData(artistResultList.toArray());

        // Needed for multiline cell
        list1.setCellRenderer(new SearchListCellRenderer());

        // When a song is selected
        newSongListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                    int inx = list1.getSelectedIndex();

                    // If viewing a group of artist songs then retrieve song from artistsSongs, otherwise retrieve from songResultList
                    String mp3File = "musicFiles/" + (useArtistSongList ? artistsSongs : songResultList).get(inx).getId() + ".mp3";

                    // Play selected song if not already
                    if (!mp3.getCurrentSong().equals(mp3File))
                        mp3.changeSong(mp3File);

                    System.out.println("Play: " + mp3File);
                    System.out.println(list1.getSelectedValue());
                }
            }
        };
        list1.addListSelectionListener(newSongListener);

        // When an artist is selected
        newArtistListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    list1.removeListSelectionListener(newSongListener);
                    int aIndex = list2.getSelectedIndex();

                    artistsSongs = new ArrayList<>();
                    for (Collection currentCollection : collection) {
                        if (currentCollection.getArtistName().equals(artistResultList.get(aIndex).getArtistName()))
                            artistsSongs.add(currentCollection);
                    }

                    // Replace songs in list1 with the songs of the selected artist
                    list1.setListData(artistsSongs.toArray());
                    list1.addListSelectionListener(newSongListener);

                    // Now use the artistsSongs array to retrieve the song for playing
                    useArtistSongList = true;
                }
            }
        };
        list2.addListSelectionListener(newArtistListener);

        mp3.getSkipButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int inx = list1.getSelectedIndex();
                list1.setSelectedIndex(inx+1);
            }
        });

        mp3.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int inx = list1.getSelectedIndex();
                list1.setSelectedIndex(inx-1);
            }
        });
    }
}
