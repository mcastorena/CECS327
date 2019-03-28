package client.gui.SearchBar;

import client.gui.MainDisplay.MainDisplayPresenter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;

/**
 * Per the MVP design pattern, this class represents the Presenter for the SearchBar
 */
public class SearchBarPresenter {

    /**
     * Parent node
     */
    private Parent view;

    /**
     * MVP connection to the MainDisplay
     */
    private MainDisplayPresenter mainDisplayPresenter;

    @FXML
    TextField searchField;

    /**
     * Constructor
     *
     * @param mainDisplayPresenter - MVP connection to the MainDisplay
     */
    public SearchBarPresenter(MainDisplayPresenter mainDisplayPresenter) {

        try {
            this.mainDisplayPresenter = mainDisplayPresenter;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/SearchBar.fxml"));
            loader.setController(this);
            view = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Required by JavaFX
     */
    @FXML
    public void initialize() {
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onSearchSubmit();
            }
        });
    }

    // TODO: Consolidate these two methods??

    /**
     * Takes the search text and submits it to the MainDisplay
     */
    private void onSearchSubmit() {
        sendResultsToMainDisplay(searchField.getText());
    }

    private void sendResultsToMainDisplay(String searchText) {
        try {
            this.mainDisplayPresenter.receiveSearchText(this, searchField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getView() {
        return view;
    }
}
