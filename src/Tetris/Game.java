package Tetris;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

import static Tetris.Controller.Global.ratio;
import static Tetris.Controller.Global.stage;

public class Game extends Application {
    public static void main(String[] args) {
//        System.out.println("java.version: " + System.getProperty("java.version"));
//        System.out.println("javafx.version: " + System.getProperty("javafx.version"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainMenu.fxml"));
        primaryStage.setTitle("Tetris");
        primaryStage.setResizable(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int height = gd.getDisplayMode().getHeight();
        double windowHeight = (double)height/1.5;

        Scene scene = new Scene(root, windowHeight * ratio, windowHeight);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(200 * ratio);
        DoubleBinding w = stage.widthProperty().divide(ratio);
        stage.minHeightProperty().bind(w);
        stage.maxHeightProperty().bind(w);
        primaryStage.show();
//        root.requestFocus();

    }
}