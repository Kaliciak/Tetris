package Tetris.Model;

import static Tetris.Controller.Global.*;

public class Board {

    // 22 rows, 10 columns, (0,0) -- left down
    public Block[][] blocks = new Block[numberOfColumns][numberOfRows];

    public Board(){
        for(int i = 0; i < numberOfColumns; i ++){
            for(int j = 0; j < numberOfRows; j ++){
                blocks[i][j] = new Block(this, i, j);
            }
        }
    }
}
