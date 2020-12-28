package Tetris.Model.Shapes;

import Tetris.Model.Block;
import Tetris.Model.Board;
import Tetris.Model.Shape;
import javafx.scene.paint.Color;

public class TShape extends Shape {

    public TShape(Board board){
        super(board);
        color = Color.rgb(187,24,165);
        blocks = new Block[3][3];
        ldX = 3;
        ldY = 19;
    }

    public TShape(Board board, int state) {
        this(board);
        this.state = normState(state);
        this.blocks = getState(state).blocks;
    }

    @Override
    public Shape getState(int state) {
        Shape result = new TShape(board);
        state = normState(state);
        result.stateAssigns(state, ldX, ldY);
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                result.blocks[i][j] = new Block(board, ldX + i,  ldY + j);
            }
        }
        switch (state){
            case 0:
                result.addBlock(0, 1);
                result.addBlock(1, 1);
                result.addBlock(1, 2);
                result.addBlock(2, 1);
                return result;
            case 1:
                result.addBlock(1, 0);
                result.addBlock(1, 1);
                result.addBlock(1, 2);
                result.addBlock(2, 1);
                return result;
            case 2:
                result.addBlock(0, 1);
                result.addBlock(1, 0);
                result.addBlock(1, 1);
                result.addBlock(2, 1);
                return result;
            case 3:
                result.addBlock(0, 1);
                result.addBlock(1, 0);
                result.addBlock(1, 1);
                result.addBlock(1, 2);
                return result;

        }
        return null;
    }
}
