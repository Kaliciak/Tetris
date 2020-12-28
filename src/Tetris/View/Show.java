package Tetris.View;

import Tetris.Model.Block;
import Tetris.Model.Board;
import Tetris.Model.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Show {

    public static void showBoard(Board board, GraphicsContext gc){

        gc.setFill(Color.rgb(60, 60, 60));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for(int i = 0; i < board.numberOfColumns ; i ++){
            for(int j = 0; j < board.numberOfVisibleRows; j ++){
                showBlock(board.blocks[i][j], gc);
            }
        }
    }

    public static void showBlock(Block block, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / block.board.numberOfColumns;
        double height = gc.getCanvas().getHeight() / block.board.numberOfVisibleRows;
        if(block.empty){
            gc.setFill(Color.BLACK);
        }
        else{
            gc.setFill(block.color);
        }
        gc.fillRect(block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
    }

    public static void showGhostBlock(Block block, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / block.board.numberOfColumns;
        double height = gc.getCanvas().getHeight() / block.board.numberOfVisibleRows;
        if(block.empty){
            gc.setFill(Color.BLACK);
        }
        else{
            gc.setFill(block.color);
        }
        gc.fillRect(block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
        gc.setFill(Color.BLACK);
        gc.fillRect(block.x * width + 2, gc.getCanvas().getHeight() - (block.y + 1) * height + 2, width - 5, height - 5);
        gc.setFill(new Color(block.color.getRed(), block.color.getGreen(), block.color.getBlue(), 0.5));
        gc.fillRect(block.x * width + 2, gc.getCanvas().getHeight() - (block.y + 1) * height + 2, width - 5, height - 5);
    }

    public static void showShape(Shape shape, GraphicsContext gc){
        for(Block[] bl: shape.blocks){
            for(Block block: bl){
                if(!block.empty){
                    showBlock(block, gc);
                }
            }
        }
    }

    public static void showGhost(Shape shape, GraphicsContext gc){
        for(Block[] bl: shape.blocks){
            for(Block block: bl){
                if(!block.empty){
                    showGhostBlock(block, gc);
                }
            }
        }
    }

    public static void showNext(Shape shape, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / 4;
        double height = gc.getCanvas().getHeight() / 4;

        gc.setFill(Color.rgb(60, 60, 60));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for(int i = 0; i < 4 ; i ++){
            for(int j = 0; j < 4; j ++){
                try {
                    if(shape != null && shape.blocks[i][j].empty){
                        gc.setFill(Color.BLACK);
                    }
                    else {
                        gc.setFill(shape.blocks[i][j].color);
                    }
                }catch (Exception e){
                    gc.setFill(Color.BLACK);
                }
                gc.fillRect(i * width, gc.getCanvas().getHeight() - (j + 1) * height, width - 1, height - 1);
            }
        }
    }
}
