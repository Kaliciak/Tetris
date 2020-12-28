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

    public static int normState(int state){
        state %= 4;
        state += 4;
        state %= 4;
        return state;
    }

    public boolean checkPositions(int from, int to){
        Shape test = getState(state);

        // 0>>1
        if(from == 0 && to == 1){
            if(test.moveByVector(0, 0) || test.moveByVector(-1, 0) || test.moveByVector(-1, 1) ||
                    test.moveByVector(0, -2) || test.moveByVector(-1, -2)){
                changeToShape(test);
                return true;
            }
        }

        // 1>>0
        else if(from == 1 && to == 0){
            if(test.moveByVector(0, 0) || test.moveByVector(1, 0) || test.moveByVector(1, -1) ||
                    test.moveByVector(0, 2) || test.moveByVector(1, 2)){
                changeToShape(test);
                return true;
            }
        }

        // 1>>2
        else if(from == 1 && to == 2){
            if(test.moveByVector(0, 0) || test.moveByVector(1, 0) || test.moveByVector(1, -1) ||
                    test.moveByVector(0, 2) || test.moveByVector(1, 2)){
                changeToShape(test);
                return true;
            }
        }

        // 2>>1
        else if(from == 2 && to == 1){
            if(test.moveByVector(0, 0) || test.moveByVector(-1, 0) || test.moveByVector(-1, 1) ||
                    test.moveByVector(0, -2) || test.moveByVector(-1, -2)){
                changeToShape(test);
                return true;
            }
        }

        // 2>>3
        else if(from == 2 && to == 3){
            if(test.moveByVector(0, 0) || test.moveByVector(1, 0) || test.moveByVector(1, 1) ||
                    test.moveByVector(0, -2) || test.moveByVector(1, -2)){
                changeToShape(test);
                return true;
            }
        }

        // 3>>2
        else if(from == 3 && to == 2){
            if(test.moveByVector(0, 0) || test.moveByVector(-1, 0) || test.moveByVector(-1, -1) ||
                    test.moveByVector(0, 2) || test.moveByVector(-1, 2)){
                changeToShape(test);
                return true;
            }
        }

        // 3>>0
        else if(from == 3 && to == 0){
            if(test.moveByVector(0, 0) || test.moveByVector(-1, 0) || test.moveByVector(-1, -1) ||
                    test.moveByVector(0, 2) || test.moveByVector(-1, 2)){
                changeToShape(test);
                return true;
            }
        }

        // 0>>3
        else if(from == 0 && to == 3){
            if(test.moveByVector(0, 0) || test.moveByVector(1, 0) || test.moveByVector(1, 1) ||
                    test.moveByVector(0, -2) || test.moveByVector(1, -2)){
                changeToShape(test);
                return true;
            }
        }

        return false;
    }

    // rotate clockwise
    public boolean rotateClock(){
        Shape result = getState(state + 1);
        if(result.checkPositions(state, result.state)){
            changeToShape(result);
            return true;
        }
        return false;
    }

    // rotate counter clockwise
    public boolean rotateCounterClock(){
        Shape result = getState(state - 1);
        if(result.checkPositions(state, result.state)){
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

    public Shape getGhost(){
        Shape result = getState(state);
        while(result.moveByVector(0, -1)){}
        return result;
    }
}
