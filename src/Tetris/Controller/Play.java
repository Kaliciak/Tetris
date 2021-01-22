package Tetris.Controller;

import Tetris.Model.Board;
import Tetris.Model.Shape;
import Tetris.Model.ShapeGenerator;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.*;

import static Tetris.Controller.Global.*;
import static Tetris.View.Show.*;


public class Play {
    // resize variables
    List<DoubleProperty> doublePropertyList = new ArrayList<>();
    List<StringExpression> stringExpressionList = new ArrayList<>();

    // game play variables
    Board board;
    ShapeGenerator shapeGenerator;
    Shape currentShape;
    Shape nextShape;
    Shape ghostShape;
    Shape holdShape;

    MediaPlayer player;
    MediaPlayer fall;
    Media fallMedia;
    MediaPlayer clearLineMP;
    Media clearLineMedia;

    volatile boolean stop = false;
    Thread gameThread;
    GraphicsContext gc;
    GraphicsContext nextGc;
    GraphicsContext holdGc;
    boolean begun = false;
    boolean changed = false;
    boolean paused = false;
    boolean muted = false;
    boolean permMuted = false;

    AnimationTimer animationTimer;

    int level = 1;
    long speed = secondNano;
    long lastFall = 0;
    long lockDelay = 0;
    long score = 0;
    int lines = 0;
    long moved = 0;
    boolean moveLeft = false;
    boolean moveRight = false;
    long movedLeft = 0;
    long movedRight = 0;

    void startGame(){
        stop = false;
        changed = false;
        paused = false;
        holdShape = null;
        level = 0;
        levelUp();
        lastFall = 0;
        lockDelay = 0;
        score = 0;
        lines = 0;
        moved = 0;
        moveLeft = false;
        moveRight = false;
        movedLeft = 0;
        movedRight = 0;

        board = new Board();
        shapeGenerator = new ShapeGenerator(board);
        currentShape = shapeGenerator.getShape();
        ghostShape = currentShape.getGhost();
        nextShape = shapeGenerator.getShape();

        levelText.setText(String.valueOf(level));
        scoreText.setText(String.valueOf(score));
        linesText.setText(String.valueOf(lines));

        canvasText.setVisible(false);

        if(!begun){
            try {
                animationTimer = new AnimationTimer() {
                    long lastTick = 0;

                    public void handle(long now) {
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

                };
                animationTimer.start();
                gc.getCanvas().getParent().addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressed);
                gc.getCanvas().getParent().addEventFilter(KeyEvent.KEY_RELEASED, this::keyReleased);

                begun = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    void tick(long time){
        boolean zero = true;

        // dropping shape
        if(!stop && !paused){
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
        }

        showBoard(board, gc);
        showGhost(ghostShape, gc);
        showShape(currentShape, gc);
        showNext(nextShape, nextGc);
        showNext(holdShape, holdGc);
        if(paused){
            darken(gc);
        }


        // moving left and right
        if(!stop && !paused && moved > 0 && moveLeft && System.currentTimeMillis() - moved > secondMilli/5 && System.currentTimeMillis() - movedLeft > secondMilli/50) {
            currentShape.moveByVector(-1, 0);
            ghostShape = currentShape.getGhost();
            movedLeft = System.currentTimeMillis();
        }
        if(!stop && !paused && moved > 0 && moveRight && System.currentTimeMillis() - moved > secondMilli/5 && System.currentTimeMillis() - movedRight > secondMilli/50) {
            currentShape.moveByVector(1, 0);
            ghostShape = currentShape.getGhost();
            movedRight = System.currentTimeMillis();
        }
    }

    void keyPressed(KeyEvent key){
        if(!paused){
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
                    if(moved == 0 && !moveLeft){
                        currentShape.moveByVector(-1, 0);
                        ghostShape = currentShape.getGhost();
                        moved = System.currentTimeMillis();
                        moveLeft = true;

                    }
                    break;
                case RIGHT:
                    if(moved == 0 && !moveLeft){
                        currentShape.moveByVector(1, 0);
                        ghostShape = currentShape.getGhost();
                        moved = System.currentTimeMillis();
                        moveRight = true;

                    }
                    break;
                case SPACE:
                    addToScore((currentShape.ldY - ghostShape.ldY) * 2 * level);
                    putShape(ghostShape);
                    break;
                case P:
                    canvasText.setText("PAUSE");
                    canvasText.setVisible(true);
                    paused = !paused;

                    player.pause();
                    break;
                case M:
                    if(!permMuted){
                        if(!muted){
                            player.pause();
                        }
                        else {
                            player.play();
                        }
                        muted = !muted;
                    }
            }
        }

        // if game is paused
        else if(!stop && key.getCode() == KeyCode.P) {
//            animationTimer.start();
            paused = !paused;
            canvasText.setVisible(false);

            player.play();
        }

        // if game over
        if(stop && key.getCode() == KeyCode.R){
            startGame();
            if (!muted){
                player.play();  
            }
        }
    }

    void keyReleased(KeyEvent key){
        switch(key.getCode()){
            case LEFT:
                moveLeft = false;
                movedLeft = 0;
                moved = 0;
                break;
            case RIGHT:
                moveRight = false;
                movedRight = 0;
                moved = 0;
                break;
        }
    }

    void endGame(){
        stop = true;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        paused = !paused;
        canvasText.setText("GAME\nOVER");
        canvasText.setVisible(true);

        if(!muted){
            player.pause();
        }

        // high score
        try {
            File highScoreFile = new File("Resources/HighScore");
            if(highScoreFile.createNewFile()){
                FileWriter fileWriter = new FileWriter("Resources/HighScore");
                fileWriter.write(String.valueOf(score));
                fileWriter.close();
            }
            else{
                Scanner scanner = new Scanner(highScoreFile);
                long prevScore = scanner.nextLong();
                if(prevScore < score){
                    FileWriter fileWriter = new FileWriter("Resources/HighScore");
                    fileWriter.write(String.valueOf(score));
                    fileWriter.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void putShape(Shape shape){
        if (!muted) {
            try {
                fall.stop();
                fall.dispose();
            }catch (Exception e){}
            fall = new MediaPlayer(fallMedia);
            fall.play();

        }

        board.putShape(shape);
        if(shape.isAbove()){
            endGame();
//            startGame();
            return;
        }

        int clearedLines = board.clearLines();
        if(clearedLines > 0){
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

            if(!muted){
                try {
                    clearLineMP.stop();
                    clearLineMP.dispose();
                }catch (Exception e){}
                clearLineMP = new MediaPlayer(clearLineMedia);
                clearLineMP.play();
            }
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

    // resize functions
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

    void setRegionSize(Region region, double widthFactor, double heightFactor){
        DoubleProperty widthProperty = new SimpleDoubleProperty(10);
        DoubleProperty heightProperty = new SimpleDoubleProperty(10);
        doublePropertyList.add(widthProperty);
        doublePropertyList.add(heightProperty);

        widthProperty.bind(root.widthProperty().divide(widthFactor));
        heightProperty.bind(root.heightProperty().divide(heightFactor));

        region.prefWidthProperty().bind(widthProperty);
        region.prefHeightProperty().bind(heightProperty);
    }

    void setCanvasSize(Canvas canvas, double widthFactor, double heightFactor){
        DoubleProperty widthProperty = new SimpleDoubleProperty(10);
        DoubleProperty heightProperty = new SimpleDoubleProperty(10);
        doublePropertyList.add(widthProperty);
        doublePropertyList.add(heightProperty);

        widthProperty.bind(root.widthProperty().divide(widthFactor));
        heightProperty.bind(root.heightProperty().divide(heightFactor));

        canvas.widthProperty().bind(widthProperty);
        canvas.heightProperty().bind(heightProperty);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane root;

    @FXML
    private Canvas canvas;

    @FXML
    private Button gb;

    @FXML
    private Text scoreT;

    @FXML
    private Text scoreText;

    @FXML
    private Text levelT;

    @FXML
    private Text levelText;

    @FXML
    private Canvas nextCanvas;

    @FXML
    private Text nextT;

    @FXML
    private Canvas holdCanvas;

    @FXML
    private Text holdT;

    @FXML
    private Text linesT;

    @FXML
    private Text linesText;

    @FXML
    private Text canvasText;

    @FXML
    void goBack(ActionEvent event) {
        endGame();
        if(!muted){
            player.stop();
            try {
                fall.stop();
                fall.dispose();
            }catch (Exception e){}
            try {
                clearLineMP.stop();
                clearLineMP.dispose();
            }catch (Exception e){}
        }
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

        try {
            Media media = new Media(new File("Resources/Sound/Theme.mp3").toURI().toString());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();

            fallMedia = new Media(new File("Resources/Sound/Fall.mp3").toURI().toString());
            clearLineMedia = new Media(new File("Resources/Sound/ClearLine.mp3").toURI().toString());
        }catch (Exception e){
            permMuted = true;
            muted = true;
        }

        // resize
        setResize(100, Collections.singletonList(gb));
        setResize(55, Arrays.asList(nextT, holdT, scoreT, levelT, linesT, scoreText, levelText, linesText));
        setResize(30, Collections.singletonList(canvasText));

        setPlace(gb, 80, 80);
        setRegionSize(gb, 5, 50);

        setPlace(nextT, 25, (double)650/78);
        setPlace(holdT, 25, (double)650/204);
        setPlace(scoreT, 25, (double)650/334);
        setPlace(scoreText, 25, (double)650/361);
        setPlace(levelT, 25, (double)650/420);
        setPlace(levelText, 25, (double)650/447);
        setPlace(linesT, 25, (double)650/497);
        setPlace(linesText, 25, (double)650/524);

        setPlace(nextCanvas, 25, (double)650/92);
        setCanvasSize(nextCanvas, (double)500/80, (double)650/80);
        setPlace(holdCanvas, 25, (double)650/218);
        setCanvasSize(holdCanvas, (double)500/80, (double)650/80);
        setPlace(canvas, (double)500/180, (double)650/30);
        setCanvasSize(canvas, (double)500/300, (double)650/600);

        setPlace(canvasText, (double)500/180, (double)650/335);
        canvasText.wrappingWidthProperty().bind(root.widthProperty().divide((double)500/300));

//        stage.setResizable(false);
    }
}
