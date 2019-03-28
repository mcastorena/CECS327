package client.gui.MainDisplay;

import client.gui.LoadableView;
import javafx.fxml.FXMLLoader;

/**
 * As part of the MVP-design pattern, this class represents the View for the MainDisplay
 */
public class MainDisplayView extends LoadableView {
    public MainDisplayView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/MainDisplayAlt.fxml"));
    }
}
