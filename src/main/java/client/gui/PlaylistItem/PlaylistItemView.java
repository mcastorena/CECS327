package client.gui.PlaylistItem;

import client.gui.LoadableView;
import javafx.fxml.FXMLLoader;

/**
 * Part of the MVP design pattern, this class represents the View of the PlaylistItem
 */
public class PlaylistItemView extends LoadableView {

    /**
     * Constructor
     */
    public PlaylistItemView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/PlaylistItem.fxml"));
    }
}
