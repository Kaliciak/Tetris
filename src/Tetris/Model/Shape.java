package Tetris.Model;


import javafx.scene.paint.Color;

public abstract class Shape {
    public Color color;
    public Block[][] blocks;
    public Board board;
    public int state = 1;
    public int ldX, ldY;

    protected Shape(Board board){
        this.board = board;
    }

    public Shape(Board board, int state){
        this(board);
        state %= 4;
        this.state = state;
        this.blocks = getState(state).blocks;
    }

    public void addBlock(int x, int y){
        blocks[x][y] = new Block(board, ldX + x, ldY + y, false, color);
    }

    //todo: rotating and moving

    public Shape rotateClock(){
        return getState(state + 1);
    }

    public abstract Shape getState(int state);

    public void stateAssigns(int state, int ldX, int ldY){
        this.state = state;
        this.ldX = ldX;
        this.ldY = ldY;
    }

    public void moveDown(){
        ldY --;
        for(Block[] bl: blocks){
            for(Block block: bl){
                block.y --;
            }
        }
    }

    public void moveLeft(){
        ldX --;
        for(Block[] bl: blocks){
            for(Block block: bl){
                block.x --;
            }
        }
    }

    public void moveRight(){
        ldX ++;
        for(Block[] bl: blocks){
            for(Block block: bl){
                block.x ++;
            }
        }
    }
}
