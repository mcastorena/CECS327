package client.Gui.PlaylistList;

import client.Gui.LoadableView;
import javafx.fxml.FXMLLoader;

public class PlaylistListView extends LoadableView {
    public PlaylistListView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/PlaylistList.fxml"));
    }
}
