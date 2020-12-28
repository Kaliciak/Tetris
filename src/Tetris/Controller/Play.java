package Tetris.Controller;

import Tetris.Model.Board;
import Tetris.Model.Shape;
import Tetris.Model.ShapeGenerator;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static Tetris.Controller.Global.replaceSceneContent;
import static Tetris.Controller.Global.secondNano;
import static Tetris.View.Show.*;


public class Play {

    Board board;
    ShapeGenerator shapeGenerator;
    Shape currentShape;
    Shape nextShape;

    volatile boolean stop = false;
    Thread gameThread;
    GraphicsContext gc;
    GraphicsContext nextGc;
    boolean begun = false;

    int level = 1;
    long speed = secondNano;
    long lastFall = 0;
    long score = 0;

    void startGame(){
        stop = false;
        level = 0;
        levelUp();
        lastFall = 0;
        score = 0;

        board = new Board();
        shapeGenerator = new ShapeGenerator(board);
        currentShape = shapeGenerator.getShape();
        nextShape = shapeGenerator.getShape();

        levelText.setText(String.valueOf(level));
        scoreText.setText(String.valueOf(score));

        if(!begun){
            try {
                new AnimationTimer() {
                    long lastTick = 0;

                    public void handle(long now) {
                        if (stop) {
                            return;
                        }
                        if (lastTick == 0) {
                            long time = now - lastTick;
                            lastTick = now;
                            return;
                        }

                        // 60 fps
                        if (now - lastTick > (secondNano / 60)) {
                            long time = now - lastTick;
                            lastTick = now;
                            tick(time);
                        }
                    }

                }.start();
                gc.getCanvas().getParent().addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressed);

                begun = true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    void tick(long time){
        lastFall += time;
        if(lastFall > speed){
            if(!currentShape.moveByVector(0, -1)){
                putShape();
            }
            lastFall = 0;
        }
        showBoard(board, gc);
        showShape(currentShape, gc);
        showNext(nextShape, nextGc);
    }

    public void keyPressed(KeyEvent key){
        switch (key.getCode()){
            case UP:
                currentShape.rotateClock();
                break;
            case DOWN:
                if(!currentShape.moveByVector(0, -1)){
                    putShape();
                }
                lastFall = 0;
                break;
            case LEFT:
                currentShape.moveByVector(-1, 0);
                break;
            case RIGHT:
                currentShape.moveByVector(1, 0);
        }
    }

    void endGame(){
        stop = true;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void putShape(){
        board.putShape(currentShape);
        lastFall = 0;
        if(currentShape.isAbove()){
            startGame();
            return;
        }
        currentShape = nextShape;
        nextShape = shapeGenerator.getShape();
        score += board.clearLines();

        scoreText.setText(String.valueOf(score));
    }

    void levelUp(){
        level ++;
        double temp = Math.pow((0.8 - ((level - 1) * 0.007)), level - 1);
        speed = (long) (temp * secondNano);
        levelText.setText(String.valueOf(level));
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button gb;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas nextCanvas;

    @FXML
    private Text scoreText;

    @FXML
    private Text levelText;

    @FXML
    void goBack(ActionEvent event) {
        endGame();
        replaceSceneContent("/FXML/MainMenu.fxml");
    }

    @FXML
    void initialize() {
        assert gb != null : "fx:id=\"gb\" was not injected: check your FXML file 'Play.fxml'.";

        gc = canvas.getGraphicsContext2D();
        nextGc = nextCanvas.getGraphicsContext2D();

        gc.getCanvas().getParent().requestFocus();

        gameThread = new Thread(this::startGame);
        gameThread.start();
    }
}
