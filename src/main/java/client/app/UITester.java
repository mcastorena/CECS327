package client.app;//package app;

import client.rpc.Proxy;
import client.rpc.ProxyInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UITester extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Stage primaryStage = new Stage();
        primaryStage.setWidth(1024);
        primaryStage.setHeight(576);
        primaryStage.setTitle("Music Player");
        primaryStage.setResizable(true);

        var proxy = new Proxy(2223);
        Controller controller = new Controller(primaryStage, proxy);
        controller.goToHomepage();

//        GridPane gp = new GridPane();
//        gp.add(controller.searchBarPresenter.getView(), 3, 0, 3, 1);
//        primaryStage.setScene(new Scene(gp));

        primaryStage.show();
    }
}
