package Gui.MainDisplay;

import app.Main;
import data.CollectionFormat;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import model.Collection;
import utility.Serializer;

public class SearchResultSongItem extends MainDisplayItem {

    public SearchResultSongItem(MainDisplayPresenter parent, Collection song) {
        super(parent, song);

        AnchorPane p = super.songPane;

        super.songPane.setOnDragDetected(e -> {
            Main.setCursorStyle(Cursor.CLOSED_HAND);

            Dragboard dragboard = p.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.put(CollectionFormat.FORMAT, this.song);
            dragboard.setContent(content);
            e.consume();

            // Create a "ghost" image of the pane on drag
            dragboard.setDragView(
                    view.snapshot(
                        new SnapshotParameters(),
                        new WritableImage(
                                p.widthProperty().intValue(),
                                p.heightProperty().intValue())));
        });

//        AnchorPane s = super.songPane;
//
//        ContextMenu contextMenu = new ContextMenu();
//        MenuItem addToPlaylistOption = new MenuItem("Add to playlist");
//        addToPlaylistOption.setOnAction(e -> {
//
//        });
//        contextMenu.getItems().add(addToPlaylistOption);
//
//        s.setOnContextMenuRequested(e -> {
//            contextMenu.show(s, e.getScreenX(), e.getScreenY());
//        });
    }

//    private void showSongAddWindow() {
//        SongAddWindow saw = new SongAddWindow();
//        saw.show();
//    }
}
