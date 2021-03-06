package client.gui.MainDisplay;

import client.app.App;
import client.data.CollectionFormat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import client.model.CollectionLightWeight;

import java.io.IOException;

/**
 * This class represents the song that is currently selected to be played
 */
public class MainDisplayItem {
    /**
     * The parent view that contains this MainDisplayItem
     */
    protected Parent view;

    /**
     * The Presenter associated with this item
     */
    protected MainDisplayPresenter mainDisplayPresenter;

    /**
     * The song that is selected for display
     */
    protected CollectionLightWeight song;

    //region FXML components
    protected @FXML
    AnchorPane songPane;
    protected @FXML
    Label byLabel;
    protected @FXML
    Label onLabel;
    protected @FXML
    Label songTitleLabel;
    protected @FXML
    Label artistNameLabel;
    protected @FXML
    Label albumNameLabel;
    //endregion

    /**
     * Constructor
     *
     * @param mainDisplayPresenter - Presenter associated with this item
     * @param song                 - Song selected to be displayed
     */
    public MainDisplayItem(MainDisplayPresenter mainDisplayPresenter, CollectionLightWeight song) {
        try {
            this.mainDisplayPresenter = mainDisplayPresenter;
            this.song = song;

            // Loader required for JavaFX to set the .fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/MainDisplayItemAlt.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the FXML components and binds the set methods
     */
    public void initialize() {
        songTitleLabel.setText(song.getSongTitle());
        artistNameLabel.setText(song.getArtistName());
        albumNameLabel.setText(song.getReleaseName());

        songPane.setOnMouseEntered(e -> {
            App.getPrimaryStage()
                    .getScene()
                    .setCursor(Cursor.HAND);
            songPane.setStyle("-fx-background-color: #464646");
        });

        songPane.setOnMouseExited(e -> {
            App.getPrimaryStage()
                    .getScene()
                    .setCursor(Cursor.DEFAULT);
            songPane.setStyle("-fx-background-color: #333333");
        });

        songPane.setOnMouseClicked(e -> sendPlayRequest());

        // Set up drag-and-drop of a MainDisplayItem to a PlaylistItem
        songPane.setOnDragDetected(e -> {
            App.setCursorStyle(Cursor.CLOSED_HAND);

            Dragboard dragboard = songPane.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.put(CollectionFormat.FORMAT, this.song);
            dragboard.setContent(content);
            e.consume();

            // Create a "ghost" image of the pane on drag
            dragboard.setDragView(
                    view.snapshot(
                            new SnapshotParameters(),
                            new WritableImage(
                                    songPane.widthProperty().intValue(),
                                    songPane.heightProperty().intValue())));
        });
    }

    public Parent getView() {
        return view;
    }

    /**
     * Receives the user's request to play the `song` and sends that request to the associated MainDisplayPresenter
     */
    private void sendPlayRequest() {
        mainDisplayPresenter.receivePlayRequest(this, song);
    }

}
