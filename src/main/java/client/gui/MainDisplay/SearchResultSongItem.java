package client.gui.MainDisplay;

import client.app.App;
import client.data.CollectionFormat;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import client.model.CollectionLightWeight;


/**
 * Defines the appearance and characteristics Song as a Search Result
 */
public class SearchResultSongItem extends MainDisplayItem {

    public SearchResultSongItem(MainDisplayPresenter parent, CollectionLightWeight song) {
        super(parent, song);

        AnchorPane anchorPane = super.songPane;

        super.songPane.setOnDragDetected(e -> {
            App.setCursorStyle(Cursor.CLOSED_HAND);

            Dragboard dragboard = anchorPane.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.put(CollectionFormat.FORMAT, this.song);
            dragboard.setContent(content);
            e.consume();

            // Create a "ghost" image of the pane on drag
            dragboard.setDragView(
                    view.snapshot(
                            new SnapshotParameters(),
                            new WritableImage(
                                    anchorPane.widthProperty().intValue(),
                                    anchorPane.heightProperty().intValue())));
        });
    }
}