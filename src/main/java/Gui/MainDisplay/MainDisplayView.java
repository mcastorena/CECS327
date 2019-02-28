package Gui.MainDisplay;

import javafx.fxml.FXMLLoader;
import Gui.LoadableView;

public class MainDisplayView extends LoadableView {
    public MainDisplayView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/MainDisplayAlt.fxml"));
    }
}
