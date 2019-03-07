package Gui.SearchBar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import Gui.MainDisplay.MainDisplayPresenter;

import java.io.IOException;

public class SearchBarPresenter {
    //    private SearchBarView searchBarView;
    private Parent view;
    private MainDisplayPresenter mainDisplayPresenter;

//    @FXML
//    Button submitButton;

//    @FXML
//    TextInputControl text;

//    @FXML
//    VBox list;

    @FXML
    TextField searchField;


    public SearchBarPresenter(MainDisplayPresenter mainDisplayPresenter) {
        try {
            this.mainDisplayPresenter = mainDisplayPresenter;

//            searchBarModel = new SearchBarModel();
//            searchBarModel.setMusicDatabase(Resources.getMusicDatabase());
//        searchBarView = new SearchBarView();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SearchBar.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
//        submitButton.setOnMouseClicked(e -> onSearchSubmit());
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onSearchSubmit();
            }
        });
    }

    public void onSearchSubmit() {
//        SearchResult results = Search.search(searchField.getText(), searchBarModel.getMusicDatabase());
//        renderResults(results);
//        System.out.println("Text inputted and enter key pressed.");
        sendResultsToMainDisplay(searchField.getText());
    }

    public Parent getView() {
        return view;
    }

    private void sendResultsToMainDisplay(String searchText) {
        try {
            this.mainDisplayPresenter.receiveSearchText(this, searchField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void renderResults(SearchResult results) {
//        renderList(results.getSongResultList());
//    }

//    public void renderList(List<Collection> results) {
//        list.getChildren().clear();
//        results.forEach(result -> {
//            SearchBarResultPresenter sbrPresenter = new SearchBarResultPresenter(result);
//            list.getChildren().add(sbrPresenter.getView());
//        });
//    }

//    public void renderPlaylist(Playlist playlist) {
//        renderList(playlist.getSongList());
//    }
}
