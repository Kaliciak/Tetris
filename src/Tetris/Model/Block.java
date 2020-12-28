package Tetris.Model;

import javafx.scene.paint.Color;

public class Block {
    public boolean empty = true;
    public Color color = new Color(0,0,0,1);
    public int x, y;
    public Board board;

    public Block(Board board, int x, int y){
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public Block(Board board, int x, int y, boolean empty, Color color){
        this.board = board;
        this.x = x;
        this.y = y;
        this.empty = empty;
        this.color = color;
    }

    public void moveTo(Block block){
        block.empty = this.empty;
        block.color = this.color;

        this.empty = true;
    }

}
