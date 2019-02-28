package Gui.PlaylistList;

import javafx.fxml.FXMLLoader;
import Gui.LoadableView;

public class PlaylistListView extends LoadableView {
    public PlaylistListView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/PlaylistList.fxml"));
    }
}
