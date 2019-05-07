package client.gui.MainDisplay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Define the characteristics of an Artist Label
 */
public class ArtistLabel {

    /**
     * Parent view containing the Artist Label
     */
    private Parent view;

    @FXML
    BorderPane borderPane;

    @FXML
    Label artistLabel;

    String artistName;

    public ArtistLabel(String artistName) {
        try {
            this.artistName = artistName;
            // Loader required for JavaFX to set the .fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/ArtistLabel.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        artistLabel.setText(artistName);
    }

    public Parent getView() {
        return view;
    }
}
