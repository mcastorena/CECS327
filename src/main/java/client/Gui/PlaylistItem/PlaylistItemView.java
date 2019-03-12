package client.Gui.PlaylistItem;

import client.Gui.LoadableView;
import javafx.fxml.FXMLLoader;

public class PlaylistItemView extends LoadableView {
    public PlaylistItemView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/PlaylistItem.fxml"));
    }
}
