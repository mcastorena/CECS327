package client.gui.Landing;

import client.gui.LoadableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * As part of the MVP-design pattern, this class represents the View for the Landing
 */
public class LandingView extends LoadableView {

    /**
     * Constructor
     */
    public LandingView() {
        loader = new FXMLLoader(getClass().getResource("ui/Landing.fxml"));
    }

    /**
     * Displays an error to the user if their username or password is not valid
     */
    protected void displayError() {
        new Alert(Alert.AlertType.ERROR, "Invalid username/password.", ButtonType.OK)
                .showAndWait();
    }
}
