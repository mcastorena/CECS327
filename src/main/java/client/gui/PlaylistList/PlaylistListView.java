package client.gui.PlaylistList;

import client.gui.LoadableView;
import javafx.fxml.FXMLLoader;

/**
 * Per the MVP design pattern, this class represents the View of the PlaylistList
 */
public class PlaylistListView extends LoadableView {

    /**
     * Constructor
     */
    public PlaylistListView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/PlaylistList.fxml"));
    }
}
