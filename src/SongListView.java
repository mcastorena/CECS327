import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class SongListView extends JFrame{
    private JList list1;
    private JList list2;
    private JPanel searchListPane;

    public SongListView(SearchResult sResult)
    {
        setContentPane(searchListPane);

        List<Collection> songResultList = sResult.getSongResultList();
        List<Collection> artistResultList = sResult.getArtistResultList();

        list1.setListData(songResultList.toArray());
        list2.setListData(artistResultList.toArray());

        list1.setSelectionMode(0);
        list1.setCellRenderer(new SearchListCellRenderer());
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int inx = e.getFirstIndex();
                String mp3File = songResultList.get(inx).getId() + ".mp3";
                mp3Player mp3player = new mp3Player(mp3File);
            }
        });

//        list2.addListSelectionListener(new ListSelectionListener()){
//
//            }


    }

}
