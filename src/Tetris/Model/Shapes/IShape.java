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
        state %= 4;
        this.state = state;
        this.blocks = getState(state).blocks;
    }

    @Override
    public Shape getState(int state) {
        Shape result = new IShape(board);
        state %= 4;
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
}
