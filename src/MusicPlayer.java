import javax.swing.*;

public class MusicPlayer extends JFrame {

    private static MusicPlayer mpInstance;

    //region Swing components
    private JPanel playerPane;
    private JPanel mainPanel;

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
    }


}
