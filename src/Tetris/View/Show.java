package Tetris.View;

import Tetris.Model.Block;
import Tetris.Model.Board;
import Tetris.Model.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Show {
    public static Image blockImage = new Image("file:Resources/Images/Block.png");
    public static Image ghostImage = new Image("file:Resources/Images/GhostBlock.png");

    public static void showBoard(Board board, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / board.numberOfColumns;
        double height = gc.getCanvas().getHeight() / board.numberOfVisibleRows;

        gc.setFill(Color.rgb(60, 60, 60));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for(int i = 0; i < board.numberOfColumns ; i ++){
            for(int j = 0; j < board.numberOfVisibleRows; j ++){
                gc.setFill(Color.BLACK);
                gc.fillRect(i * width, gc.getCanvas().getHeight() - (j + 1) * height, width - 1, height - 1);
                showBlock(board.blocks[i][j], gc);
            }
        }
    }

    public static void showBlock(Block block, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / block.board.numberOfColumns;
        double height = gc.getCanvas().getHeight() / block.board.numberOfVisibleRows;
        if(!block.empty){
            gc.drawImage(blockImage,block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
            gc.setFill(block.color);
            BlendMode prev = gc.getGlobalBlendMode();
            gc.setGlobalBlendMode(BlendMode.MULTIPLY);
            gc.fillRect(block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
            gc.setGlobalBlendMode(prev);
        }
    }

    public static void showGhostBlock(Block block, GraphicsContext gc){
        double width = gc.getCanvas().getWidth() / block.board.numberOfColumns;
        double height = gc.getCanvas().getHeight() / block.board.numberOfVisibleRows;
        if(!block.empty){
            gc.drawImage(ghostImage,block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
            gc.setFill(block.color);
            BlendMode prev = gc.getGlobalBlendMode();
            gc.setGlobalBlendMode(BlendMode.MULTIPLY);
            gc.fillRect(block.x * width, gc.getCanvas().getHeight() - (block.y + 1) * height, width - 1, height - 1);
            gc.setGlobalBlendMode(prev);
        }
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

        gc.setFill(Color.rgb(0,0,0));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for(int i = 0; i < 4 ; i ++){
            for(int j = 0; j < 4; j ++){
                try {
                    Block block = shape.blocks[i][j];
                    gc.drawImage(blockImage,i * width, gc.getCanvas().getHeight() - (j + 1) * height, width - 1, height - 1);
                    gc.setFill(block.color);
                    BlendMode prev = gc.getGlobalBlendMode();
                    gc.setGlobalBlendMode(BlendMode.MULTIPLY);
                    gc.fillRect(i * width, gc.getCanvas().getHeight() - (j + 1) * height, width - 1, height - 1);
                    gc.setGlobalBlendMode(prev);
                }catch (Exception e){
                    continue;
                }
            }
        }
    }
}
