package Tetris.View;

import Tetris.Model.Block;
import Tetris.Model.Board;
import Tetris.Model.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static Tetris.Controller.Global.*;

public class Show {

    public static void showBoard(Board board, GraphicsContext gc){

        gc.setFill(Color.rgb(60, 60, 60));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for(int i = 0; i < numberOfColumns ; i ++){
            for(int j = 0; j < numberOfVisibleRows; j ++){
                showBlock(board.blocks[i][j], gc);
            }
        }
    }

    public static void showBlock(Block block, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / numberOfColumns;
        double height = gc.getCanvas().getHeight() / numberOfVisibleRows;
        if(block.empty){
            gc.setFill(Color.BLACK);
        }
        else{
            gc.setFill(block.color);
        }
        gc.fillRect(block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
    }

    public static void showShape(Shape shape, GraphicsContext gc){
        for(Block[] bl: shape.blocks){
            for(Block block: bl){
                showBlock(block, gc);
            }
        }
    }
}
