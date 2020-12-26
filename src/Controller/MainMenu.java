package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenu {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button but;

    @FXML
    void butt(ActionEvent event) {
        System.out.println("BUTTON PRESSED");
    }

    @FXML
    void initialize() {
        assert but != null : "fx:id=\"but\" was not injected: check your FXML file 'MainMenu.fxml'.";

        System.out.println("INIT");

    }
}
