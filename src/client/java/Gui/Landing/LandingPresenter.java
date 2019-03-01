package Gui.Landing;

import app.Main;
import data.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import Gui.Homepage.HomepagePresenter;
import model.User;

import java.io.IOException;

// TODO: Implement a Presenter Interface that includes common methods (i.e. switchToScene())
public class LandingPresenter {
    private LandingModel landingModel;
    private Parent view;
    private LandingService landingService;

    // Communication between landing and homepage
    private HomepagePresenter homepagePresenter;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    public LandingPresenter() {
        try {
            landingModel = new LandingModel();
            landingService = LandingService.getInstance();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/Landing.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        usernameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitLogin();
            }
        });

        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitLogin();
            }
        });

        loginButton.setOnMouseClicked(e -> submitLogin());

        registerButton.setOnMouseClicked(e -> register());
    }

    private void submitLogin() {
//        landingModel.setUsernameInput(usernameField.getText());
//        landingModel.setPasswordInput(passwordField.getText());
        landingModel.setUsernameInput("user");
        landingModel.setPasswordInput("pass");

        boolean isAuthorized =
                landingService.authorizeUser(landingModel.getUsernameInput(), landingModel.getPasswordInput());

        if (isAuthorized) {
            User user = landingService.getCurrentSession();
            UserSession.setCurrentSession(user);
            homepagePresenter = new HomepagePresenter();
            switchToHomepage();
        }
        else {
            displayError();
        }
    }

    public void register() {
        Stage registerWindow = new Stage();

        register r = new register(registerWindow);
        Scene s = new Scene(r.getView());
        registerWindow.setScene(s);
        registerWindow.show();
    }

//    private boolean validateSubmission(User user) {
//        // to-do: actual validation
//        return user != null;
//    }

    // TODO: Abstract away into some sort of Stage/Screen manager.
    // TODO: Need to handle null userList? User should technically be authed already though.
    private void switchToHomepage() {
        homepagePresenter.showDefaultPage();
    }

    public Parent getView() {
//        return landingView.getView();
        return view;
    }

    private void displayError() {
        new Alert(Alert.AlertType.ERROR, "Invalid usernameField/passwordField.", ButtonType.OK)
                .showAndWait();
    }

    public void showLandingPage() {
        Main.getPrimaryStage().setScene(new Scene(view));
    }
}