package Tetris.Model.Shapes;

import Tetris.Model.Block;
import Tetris.Model.Board;
import Tetris.Model.Shape;
import javafx.scene.paint.Color;

/*

XXXX

*/

public class IShape extends Shape {

    public IShape(Board board){
        super(board);
        color = Color.rgb(124,215,250);
        blocks = new Block[4][4];
        ldX = 3;
        ldY = 18;
    }

    public IShape(Board board, int state) {
        this(board);
        this.state = normState(state);
        this.blocks = getState(state).blocks;
    }

    @Override
    public Shape getState(int state) {
        Shape result = new IShape(board);
        state = normState(state);
        result.stateAssigns(state, ldX, ldY);
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 4; j ++){
                result.blocks[i][j] = new Block(board, ldX + i,  ldY + j);
            }
        }
        switch (state){
            case 0:
                result.addBlock(0, 2);
                result.addBlock(1, 2);
                result.addBlock(2, 2);
                result.addBlock(3, 2);
                return result;
            case 1:
                result.addBlock(2, 0);
                result.addBlock(2, 1);
                result.addBlock(2, 2);
                result.addBlock(2, 3);
                return result;
            case 2:
                result.addBlock(0, 1);
                result.addBlock(1, 1);
                result.addBlock(2, 1);
                result.addBlock(3, 1);
                return result;
            case 3:
                result.addBlock(1, 0);
                result.addBlock(1, 1);
                result.addBlock(1, 2);
                result.addBlock(1, 3);
                return result;
        }
        return null;
    }

    @Override
    public boolean checkPositions(int from, int to) {
        Shape test = getState(state);

        // 0>>1
        if(from == 0 && to == 1){
            if(test.moveByVector(0, 0) || test.moveByVector(-2, 0) || test.moveByVector(1, 0) ||
                    test.moveByVector(-2, -1) || test.moveByVector(1, 2)){
                changeToShape(test);
                return true;
            }
        }

        // 1>>0
        else if(from == 1 && to == 0){
            if(test.moveByVector(0, 0) || test.moveByVector(2, 0) || test.moveByVector(-1, 0) ||
                    test.moveByVector(2, 1) || test.moveByVector(-1, -2)){
                changeToShape(test);
                return true;
            }
        }

        // 1>>2
        else if(from == 1 && to == 2){
            if(test.moveByVector(0, 0) || test.moveByVector(-1, 0) || test.moveByVector(2, 0) ||
                    test.moveByVector(-1, 2) || test.moveByVector(2, -1)){
                changeToShape(test);
                return true;
            }
        }

        // 2>>1
        else if(from == 2 && to == 1){
            if(test.moveByVector(0, 0) || test.moveByVector(1, 0) || test.moveByVector(-2, 0) ||
                    test.moveByVector(1, -2) || test.moveByVector(-2, 1)){
                changeToShape(test);
                return true;
            }
        }

        // 2>>3
        else if(from == 2 && to == 3){
            if(test.moveByVector(0, 0) || test.moveByVector(2, 0) || test.moveByVector(-1, 0) ||
                    test.moveByVector(2, 1) || test.moveByVector(-1, -2)){
                changeToShape(test);
                return true;
            }
        }

        // 3>>2
        else if(from == 3 && to == 2){
            if(test.moveByVector(0, 0) || test.moveByVector(-2, 0) || test.moveByVector(1, 0) ||
                    test.moveByVector(-2, -1) || test.moveByVector(1, 2)){
                changeToShape(test);
                return true;
            }
        }

        // 3>>0
        else if(from == 3 && to == 0){
            if(test.moveByVector(0, 0) || test.moveByVector(1, 0) || test.moveByVector(-2, 0) ||
                    test.moveByVector(1, -2) || test.moveByVector(-2, 1)){
                changeToShape(test);
                return true;
            }
        }

        // 0>>3
        else if(from == 0 && to == 3){
            if(test.moveByVector(0, 0) || test.moveByVector(-1, 0) || test.moveByVector(2, 0) ||
                    test.moveByVector(-1, 2) || test.moveByVector(2, -1)){
                changeToShape(test);
                return true;
            }
        }

        return false;
    }
}
