package Tetris.Controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.*;

import static Tetris.Controller.Global.replaceSceneContent;
import static Tetris.Controller.Global.stage;

public class MainMenu {
    List<DoubleProperty> doublePropertyList = new ArrayList<>();
    List<StringExpression> stringExpressionList = new ArrayList<>();

    @FXML
    private AnchorPane root;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Play;

    @FXML
    private Button Exit;

    @FXML
    private Text hstxt;

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

    void setResize(double factor, List<? extends Node> list){
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(root.widthProperty().add(root.heightProperty()).divide(factor));
        StringExpression binding = Bindings.concat("-fx-font-size: ", fontSize.asString(), ";");
        doublePropertyList.add(fontSize);
        stringExpressionList.add(binding);
        for(Node node: list){
            node.styleProperty().bind(binding);
        }
    }

    void setPlace(Node node, double xFactor, double yFactor){
        DoubleProperty xProperty = new SimpleDoubleProperty(10);
        DoubleProperty yProperty = new SimpleDoubleProperty(10);
        doublePropertyList.add(xProperty);
        doublePropertyList.add(yProperty);

        xProperty.bind(root.widthProperty().divide(xFactor));
        yProperty.bind(root.heightProperty().divide(yFactor));

        node.layoutXProperty().bindBidirectional(xProperty);
        node.layoutYProperty().bindBidirectional(yProperty);
    }

    void setSize(Region region, double widthFactor, double heightFactor){
        DoubleProperty widthProperty = new SimpleDoubleProperty(10);
        DoubleProperty heightProperty = new SimpleDoubleProperty(10);
        doublePropertyList.add(widthProperty);
        doublePropertyList.add(heightProperty);

        widthProperty.bind(root.widthProperty().divide(widthFactor));
        heightProperty.bind(root.heightProperty().divide(heightFactor));

        region.prefWidthProperty().bind(widthProperty);
        region.prefHeightProperty().bind(heightProperty);
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

        // resize text
        setResize(40, Arrays.asList(highScoreText, hstxt));
        setResize(30, Arrays.asList(Play, Exit));

        setPlace(Play, 6.5, 2.6);
        setSize(Play, (double)13/9, 7);
        setPlace(Exit, 6.5, 1.65);
        setSize(Exit, (double)13/9, 7);
        setPlace(highScoreText, Double.POSITIVE_INFINITY, 1.07);
        highScoreText.wrappingWidthProperty().bind(root.widthProperty());
        setPlace(hstxt, Double.POSITIVE_INFINITY, 1.14);
        hstxt.wrappingWidthProperty().bind(root.widthProperty());

        Play.requestFocus();
//        stage.setResizable(false);
    }
}
