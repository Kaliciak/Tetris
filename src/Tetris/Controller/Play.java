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
    Shape ghostShape;
    Shape holdShape;

    volatile boolean stop = false;
    Thread gameThread;
    GraphicsContext gc;
    GraphicsContext nextGc;
    GraphicsContext holdGc;
    boolean begun = false;
    boolean changed = false;

    int level = 1;
    long speed = secondNano;
    long lastFall = 0;
    long lockDelay = 0;
    long score = 0;
    int lines = 0;

    void startGame(){
        stop = false;
        changed = false;
        holdShape = null;
        level = 0;
        levelUp();
        lastFall = 0;
        lockDelay = 0;
        score = 0;
        lines = 0;

        board = new Board();
        shapeGenerator = new ShapeGenerator(board);
        currentShape = shapeGenerator.getShape();
        ghostShape = currentShape.getGhost();
        nextShape = shapeGenerator.getShape();

        levelText.setText(String.valueOf(level));
        scoreText.setText(String.valueOf(score));
        linesText.setText(String.valueOf(lines));

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
        boolean zero = true;

        lastFall += time;
        if(lastFall > speed){
            if(!currentShape.moveByVector(0, -1)){
                lockDelay += time;
                if(lockDelay > secondNano/2){
                    putShape(currentShape);
                }
                else{
                    zero = false;
                }
            }
            if(zero){
                lastFall = 0;
            }
        }
        showBoard(board, gc);
        showGhost(ghostShape, gc);
        showShape(currentShape, gc);
        showNext(nextShape, nextGc);
        showNext(holdShape, holdGc);
    }

    public void keyPressed(KeyEvent key){
        switch (key.getCode()){
            case UP:
            case X:
                currentShape.rotateClock();
                ghostShape = currentShape.getGhost();
                break;
            case Z:
                currentShape.rotateCounterClock();
                ghostShape = currentShape.getGhost();
                break;
            case C:
                if(!changed){
                    currentShape = hold(currentShape);
                    if(currentShape == null){
                        getNewShape();
                    }
                    else {
                        ghostShape = currentShape.getGhost();
                    }
                    changed = true;
                }
                break;
            case DOWN:
                if(!currentShape.moveByVector(0, -1)){
                    putShape(currentShape);
                }
                else{
                    addToScore(level);
                }
                lastFall = 0;
                break;
            case LEFT:
                currentShape.moveByVector(-1, 0);
                ghostShape = currentShape.getGhost();
                break;
            case RIGHT:
                currentShape.moveByVector(1, 0);
                ghostShape = currentShape.getGhost();
                break;
            case SPACE:
                addToScore((currentShape.ldY - ghostShape.ldY) * 2 * level);
                putShape(ghostShape);
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

    void putShape(Shape shape){
        board.putShape(shape);
        if(shape.isAbove()){
            startGame();
            return;
        }

        int clearedLines = board.clearLines();
        addToLines(clearedLines);
        switch (clearedLines){
            case 1:
                addToScore(100 * level);
                break;
            case 2:
                addToScore(300 * level);
                break;
            case 3:
                addToScore(500 * level);
                break;
            case 4:
                addToScore(800 * level);
                break;
        }
        if(lines >= 10*level){
            levelUp();
        }
        getNewShape();
        lockDelay = 0;
    }

    void getNewShape(){
        currentShape = nextShape;
        ghostShape = currentShape.getGhost();
        nextShape = shapeGenerator.getShape();
        lastFall = 0;
        changed = false;
    }

    void levelUp(){
        level ++;
        double temp = Math.pow((0.8 - ((level - 1) * 0.007)), level - 1);
        speed = (long) (temp * secondNano);
        levelText.setText(String.valueOf(level));
    }

    Shape hold(Shape shape){
        Shape result = holdShape;
        try {
            holdShape = shape.getClass().getConstructor(Board.class, int.class).newInstance(board, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    void addToScore(int x){
        score += x;
        scoreText.setText(String.valueOf(score));
    }

    void addToLines(int x){
        lines += x;
        linesText.setText(String.valueOf(lines));
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
    private Canvas holdCanvas;

    @FXML
    private Text scoreText;

    @FXML
    private Text levelText;

    @FXML
    private Text linesText;

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
        holdGc = holdCanvas.getGraphicsContext2D();

        gc.getCanvas().getParent().requestFocus();

        gameThread = new Thread(this::startGame);
        gameThread.start();
    }
}
