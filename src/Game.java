import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {
    public static void main(String args[]) {
        System.out.println("XD");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainMenu.fxml"));
        primaryStage.setTitle("Tetris");
        Scene  scene = new Scene(root, 500, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}