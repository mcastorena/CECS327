package client.gui.MainDisplay;

import client.gui.LoadableView;
import javafx.fxml.FXMLLoader;

public class MainDisplayView extends LoadableView {
    public MainDisplayView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/MainDisplayAlt.fxml"));
    }
}
