package Tetris.Controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Tetris.Game;


public class Global {
    public static final long secondNano = 1000000000;

    public static Stage stage;

    public static void replaceSceneContent(String fxml){
        System.out.println(fxml);
        try {
            Parent page = (Parent) FXMLLoader.load(Game.class.getResource(fxml), null, new JavaFXBuilderFactory());
            Scene scene = stage.getScene();
            stage.getScene().setRoot(page);
            stage.sizeToScene();
            page.requestFocus();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
