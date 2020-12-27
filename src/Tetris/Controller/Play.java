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

import java.net.URL;
import java.util.ResourceBundle;

import static Tetris.Controller.Global.replaceSceneContent;
import static Tetris.Controller.Global.secondNano;
import static Tetris.View.Show.showBoard;
import static Tetris.View.Show.showShape;


public class Play {

    Board board;
    ShapeGenerator shapeGenerator;
    Shape currentShape;
    Shape nextShape;

    volatile boolean stop = false;
    Thread gameThread;
    GraphicsContext gc;

    int level = 1;
    long speed = secondNano;
    long lastFall = 0;

    void levelUp(){
        level ++;
        double temp = Math.pow((0.8 - ((level - 1) * 0.007)), level - 1);
        speed = (long) (temp * secondNano);
    }

    void startGame(){
        level = 0;
        levelUp();
        lastFall = 0;

        board = new Board();
        shapeGenerator = new ShapeGenerator(board);
        currentShape = shapeGenerator.getShape();
        nextShape = shapeGenerator.getShape();

        try{
            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if(stop){
                        return;
                    }
                    if (lastTick == 0) {
                        long time = now - lastTick;
                        lastTick = now;
                        return;
                    }

                    // 60 fps
                    if (now - lastTick > (secondNano/60)) {
                        long time = now - lastTick;
                        lastTick = now;
                        tick(time);
                    }
                }

            }.start();

            gc.getCanvas().getParent().addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressed);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void tick(long time){
        lastFall += time;
        if(lastFall > speed){
            currentShape.moveDown();
            lastFall = 0;
            if(currentShape.ldY<-1){
                currentShape = nextShape;
                nextShape = shapeGenerator.getShape();
            }
        }
        showBoard(board, gc);
        showShape(currentShape, gc);
    }

    public void keyPressed(KeyEvent key){
        switch (key.getCode()){
            case UP:
                currentShape = currentShape.rotateClock();
                break;
            case DOWN:
                currentShape.moveDown();
                lastFall = 0;
                break;
            case LEFT:
                currentShape.moveLeft();
                break;
            case RIGHT:
                currentShape.moveRight();
        }
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
    void goBack(ActionEvent event) {
        stop = true;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        replaceSceneContent("/FXML/MainMenu.fxml");
    }

    @FXML
    void initialize() {
        assert gb != null : "fx:id=\"gb\" was not injected: check your FXML file 'Play.fxml'.";

        gc = canvas.getGraphicsContext2D();

        gc.getCanvas().getParent().requestFocus();

        gameThread = new Thread(this::startGame);
        gameThread.start();
    }
}
