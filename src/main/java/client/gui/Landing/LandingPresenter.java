package client.gui.Landing;

import client.app.Controller;
import client.gui.Homepage.HomepagePresenter;
import client.app.App;
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
import client.rpc.ProxyInterface;

import java.io.IOException;

/**
 * As part of the MVP-design pattern, this class represents the Presenter for the Landing
 */
public class LandingPresenter {
    private Controller controller;

    /**
     * Parent FXMLLoader
     */
    private Parent view;

    /**
     * Model, per the MVP-design pattern
     */
    private LandingModel landingModel;

    /**
     * TODO:
     */
    private LandingService landingService;

    /**
     * Proxy that the client is connected through
     */
    private ProxyInterface clientProxy;

    /**
     * Reference for the Homepage
     */
    private HomepagePresenter homepagePresenter;

    //region FXML components
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    //endregion

    /**
     * Constructor
     *
     * @param proxy - Proxy that the client is connected through
     */
    public LandingPresenter(Controller controller, ProxyInterface proxy, LandingModel lm, LandingService ls) {
        this.controller = controller;
        clientProxy = proxy;

        try {
            // Set the model and service
            landingModel = lm;
            landingService = ls;

            // Loader required for JavaFX to set the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/client/ui/Landing.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Binds the actions to be taken for the username and password fields, and login and register buttons
     */
    public void initialize() {
        usernameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitLoginRequest();
            }
        });

        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitLoginRequest();
            }
        });

        loginButton.setOnMouseClicked(e -> submitLoginRequest());
        registerButton.setOnMouseClicked(e -> register());
    }

    /**
     * Grabs the username and password entered and verifies them through the LandingService
     */
    private void submitLogin() {
        landingModel.setUsernameInput(usernameField.getText());
        landingModel.setPasswordInput(passwordField.getText());

        // Initialize the user as an unauthorized user at first
        boolean isAuthorized = false;
        try {
            isAuthorized = landingService.authorizeUser(landingModel.getUsernameInput(), landingModel.getPasswordInput());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            displayError();
        }

        if (isAuthorized) {
            controller.goToHomepage();
        } else {
            displayError();
        }
    }

    /**
     * Presents the register window to the user
     */
    public void register() {
        Stage registerWindow = new Stage();

        register r = new register(registerWindow, clientProxy);
        Scene s = new Scene(r.getView());
        registerWindow.setScene(s);
        registerWindow.show();
    }

    // TODO: Abstract away into some sort of Stage/Screen manager.
    // TODO: Need to handle null userList? User should technically be authed already though.
    private void switchToHomepage() {
        homepagePresenter.showDefaultPage();
    }

    /**
     * Displays an error to the user if they've entered an invalid username or password
     */
    private void displayError() {
        new Alert(Alert.AlertType.ERROR, "Invalid username and/or password.", ButtonType.OK)
                .showAndWait();
    }

    public void showLandingPage() {
        App.getPrimaryStage().setScene(new Scene(view));
    }

    //region Getters and Setters
    public Parent getView() {
        return view;
    }
    //endregion

    private void submitLoginRequest() {
        try {
            controller.receiveLoginRequest(this, usernameField.getText(), passwordField.getText());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
