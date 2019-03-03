package Gui.Landing;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import Gui.LoadableView;

public class LandingView extends LoadableView {
    public LandingView() {
        loader = new FXMLLoader(getClass().getResource("ui/Landing.fxml"));
    }

    protected void displayError() {
        new Alert(Alert.AlertType.ERROR, "Invalid username/password.", ButtonType.OK)
                .showAndWait();
    }
}
