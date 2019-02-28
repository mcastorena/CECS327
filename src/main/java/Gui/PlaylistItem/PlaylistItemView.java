package Gui.PlaylistItem;

import javafx.fxml.FXMLLoader;

import Gui.LoadableView;

public class PlaylistItemView extends LoadableView {
    public PlaylistItemView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/PlaylistItem.fxml"));
    }
}
