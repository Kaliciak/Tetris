package Tetris.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static Tetris.Controller.Global.replaceSceneContent;
import static Tetris.Controller.Global.stage;

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
    private Text highScoreText;

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

        try {
            File highScoreFile = new File("Resources/HighScore");
            if(highScoreFile.createNewFile()){
                highScoreText.setText("0");
                FileWriter fileWriter = new FileWriter("Resources/HighScore");
                fileWriter.write("0");
                fileWriter.close();
            }
            else{
                Scanner scanner = new Scanner(highScoreFile);
                long score = scanner.nextLong();
                highScoreText.setText(String.valueOf(score));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
