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


    // rotate clockwise
    public boolean rotateClock(){
        //todo: 5 positions
        Shape result = getState(state + 1);
        if(result.isProper()){
            changeToShape(result);
            return true;
        }
        return false;
    }

    public abstract Shape getState(int state);

    public void stateAssigns(int state, int ldX, int ldY){
        this.state = state;
        this.ldX = ldX;
        this.ldY = ldY;
    }

    public void changeToShape(Shape shape){
        color = shape.color;
        blocks = shape.blocks;
        state = shape.state;
        ldX = shape.ldX;
        ldY = shape.ldY;
    }

    public boolean isAbove(){
        for(Block[] bl: blocks){
            for(Block block: bl){
                if(!block.empty){
                    // if above upper line
                    if(block.y >= board.numberOfVisibleRows){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isProper(){
        for(Block[] bl: blocks){
            for(Block block: bl){
                if(!block.empty){
                    // if crosses left or right border
                    if(block.x < 0 || block.x >= board.numberOfColumns){
                        return false;
                    }
                    // if below bottom line
                    if(block.y < 0){
                        return false;
                    }
                    // if there is already block
                    if(!board.blocks[block.x][block.y].empty){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean moveByVector(int x, int y){
        Shape result = getState(state);
        result.ldX += x;
        result.ldY += y;
        for(Block[] bl: result.blocks){
            for(Block block: bl){
                block.x += x;
                block.y += y;
            }
        }

        if(result.isProper()){
            changeToShape(result);
            return true;
        }
        return false;
    }
}
