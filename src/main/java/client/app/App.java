package client.app;

import client.gui.Landing.LandingPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import client.rpc.Proxy;
import client.rpc.ProxyInterface;

import java.io.IOException;

/**
 * The main application that runs the client-side GUI
 */
public class App extends Application {

    //region Static variables
    /**
     * JavaFX Stage
     */
    private static Stage primaryStage;

    /**
     * JavaFX Scene
     */
    private static Scene primaryScene;

    /**
     * Integer token to identify the User
     */
    public static int userToken;
    //endregion

    /**
     * JavaFX root layout which holds the scene
     */
    private AnchorPane rootLayout;

    /**
     * Port that the Client makes requests to the Server through
     */
    private int portNumber = 2223;

    /**
     * Proxy that the Client communicates with the Server through
     */
    private ProxyInterface clientProxy = new Proxy(portNumber);

    @Override
    public void start(Stage stage) throws Exception {
        //Deserializer d = new Deserializer();
        //Resources.setOwnedIDs(d.getOwnedIDs());
        //Resources.setMusicDatabase(d.deserializeMusicLibrary());

        primaryStage = stage; // does nothing atm
        primaryStage.setWidth(1024);
        primaryStage.setHeight(576);
        primaryStage.setTitle("Music Player");
        primaryStage.setResizable(true);

        Controller c = new Controller(primaryStage, clientProxy);
//        c.goToHomepage();
        c.stage.show();

        primaryStage.setOnCloseRequest(e -> {
//            try {
//                new Serializer().updateUsersJson(LandingService.getInstance(clientProxy).userList());
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            } finally {
//                System.exit(1);
//            }
//        });
            // TODO: Update user json with dispatcher
            System.exit(0);
        });
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Set a specific cursor style when the user's mouse pointer is within the GUI
     *
     * @param cursorStyle - Cursor being set
     */
    public static void setCursorStyle(Cursor cursorStyle) {
        primaryStage.getScene().setCursor(cursorStyle);
    }

    public Scene getPrimaryScene() {
        return primaryScene;
    }

    public void setPrimaryScene(final Scene primaryScene) {
        this.primaryScene = primaryScene;
    }

    /**
     * Initializes the root layout when the application is started
     */
    private void initRootLayout() {
        try {
            // Load root layout from FXML file.
            FXMLLoader loader = new FXMLLoader();
//           loader.setLocation(App.class.getResource("gui/Landing/Landing.fxml"));
            rootLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
