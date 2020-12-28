package Tetris.Model.Shapes;

import Tetris.Model.Block;
import Tetris.Model.Board;
import Tetris.Model.Shape;
import javafx.scene.paint.Color;

/*

XX
XX

*/
public class OShape extends Shape {

    OShape(Board board){
        super(board);
        color = Color.rgb(249,250,26);
        blocks = new Block[3][3];
        ldX = 4;
        ldY = 20;
    }

    public OShape(Board board, int state) {
        this(board);
        this.state = normState(state);
        this.blocks = getState(state).blocks;
    }

    @Override
    public Shape getState(int state) {
        Shape result = new OShape(board);
        state = normState(state);
        result.stateAssigns(state, ldX, ldY);
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                result.blocks[i][j] = new Block(board, ldX + i,  ldY + j);
            }
        }
        for(int i = 1; i < 3; i ++){
            for(int j = 1; j < 3; j ++){
                result.addBlock(i, j);
            }
        }
        return result;
    }

    @Override
    public boolean checkPositions(int from, int to) {
        return isProper();
    }
}
