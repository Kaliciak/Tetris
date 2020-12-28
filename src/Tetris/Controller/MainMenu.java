package Tetris.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import static Tetris.Controller.Global.*;

public class MainMenu {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Play;

    @FXML
    private Button Exit;

    @FXML
    void exitGame(ActionEvent event) {
        stage.close();
    }

    @FXML
    void play(ActionEvent event) {
        replaceSceneContent("/FXML/Play.fxml");
    }

    @FXML
    void initialize() {
        assert Play != null : "fx:id=\"Play\" was not injected: check your FXML file 'MainMenu.fxml'.";

    }
}
