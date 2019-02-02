import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Displays search bar and results, also plays the song when selected
public class SearchView extends JFrame{
    private JList<Object> list1;
    private JList<Object> list2;
    private JPanel searchListPane;
    private JTextField textField1;
    private JButton SEARCHButton;
    private JPanel searchPanel;
    private ListSelectionListener newListener;
    private mp3Player mp3 = mp3Player.getInstance();

    SearchView(List<Collection> collection)
    {
        setContentPane(searchListPane);
        textField1.setText("friend"); // For testing: initialize the search for "friend" (1st 6 songs play)

        SEARCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = textField1.getText();

                // Remove old listener, needed for > 1 search
                list1.removeListSelectionListener(newListener);
                search(searchQuery, collection);
            }
        });
    }

    private void search(String searchQuery, List<Collection> collection)
    {
        SearchResult sResult = Search.search(searchQuery, collection);

        List<Collection> songResultList = sResult.getSongResultList();
        List<Collection> artistResultList = sResult.getArtistResultList();

        list1.setListData(songResultList.toArray());
        list2.setListData(artistResultList.toArray());

        list1.setCellRenderer(new SearchListCellRenderer());

        newListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                    int inx = list1.getSelectedIndex();
                    String mp3File = "musicFiles/" + songResultList.get(inx).getId() + ".mp3";

                    if (!mp3.getCurrentSong().equals(mp3File)) {
                        mp3.changeSong(mp3File);
                    }
                    System.out.println("Play: " + mp3File);
                    System.out.println(list1.getSelectedValue());
                }
            }
        };
        list1.addListSelectionListener(newListener);

        // TODO: Add event when artist is selected
//        list2.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//
//            }
//        });
    }

}
