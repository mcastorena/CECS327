package Gui.Landing;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rpc.ProxyInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class register {
    private Stage stage;
    private Parent view;

    private ProxyInterface proxy;
    @FXML
    private TextField user;
    @FXML
    private TextField pass;
    @FXML
    private Button signupButton;
    @FXML
    private Text registerText;

    public register(Stage stage, ProxyInterface proxy) {
        try {
            this.proxy = proxy;
            this.stage = stage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Registration.fxml"));
            loader.setController(this);
            view = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        signupButton.setOnMouseClicked(e -> {
            LandingService ls = LandingService.getInstance(proxy);

            // Get username and password
            String username = user.getText();
            String password = pass.getText();

            // Create HashMap to hold parameters
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);

            // Get and display response and clear entry forms
            JsonObject response = this.proxy.syncExecution("register", params);
            String responsePayload = response.get("ret").getAsString();
            Font myFont = registerText.getFont();
            registerText.setFont(new Font(myFont.getName(), myFont.getSize()/2));
            registerText.setText(responsePayload);
            user.clear();
            pass.clear();


        });
    }

    public Parent getView() {
        return view;
    }
}
