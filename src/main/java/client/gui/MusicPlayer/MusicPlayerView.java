package client.gui.MusicPlayer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * As part of the MVP-design pattern, this class represents the View for the MusicPlayer
 */
public class MusicPlayerView {

    /**
     * Parent view for the MusicPlayer
     */
    private Parent view;

    /**
     * JavaFX loader to load the UI
     */
    private FXMLLoader loader;

    /**
     * Constructor
     */
    public MusicPlayerView() {
        loader = new FXMLLoader(getClass().getResource("../../../res/ui/MusicPlayer.fxml"));
    }

    /**
     * Load the Music Player's view
     *
     * @return -
     */
    public Parent loadView() {
        try {
            return loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the object's FXMLLoader
     *
     * @return - Instantiated FXMLLoader
     */
    public FXMLLoader getLoader() {
        return loader;
    }
}
