package Tetris.Model;

public class Board {
    public final int numberOfRows = 24;
    public final int numberOfVisibleRows = 20;
    public final int numberOfColumns = 10;

    // 22 rows, 10 columns, (0,0) -- left down
    public Block[][] blocks = new Block[numberOfColumns][numberOfRows];

    public Board(){
        for(int i = 0; i < numberOfColumns; i ++){
            for(int j = 0; j < numberOfRows; j ++){
                blocks[i][j] = new Block(this, i, j);
            }
        }
    }

    public void putBlock(Block block){
        try {
            blocks[block.x][block.y] = block;
        }catch (Exception e){
            System.out.println(block.x + " " +block.y);
        }
    }

    public void putShape(Shape shape){
        for(Block[] bl: shape.blocks){
            for(Block block: bl){
                if(!block.empty){
                    putBlock(block);
                }
            }
        }
    }

    public int clearLines(){
        int[] down = new int[numberOfRows];
        int lines = 0;

        // looking for full lines
        for(int i = 0; i < numberOfRows; i ++){
            boolean full = true;
            for(int j = 0; j < numberOfColumns; j ++){
                if(blocks[j][i].empty){
                    full = false;
                    break;
                }
            }

            if(full){
                lines ++;
                down[i] = 0;
            }
            else {
                down[i] = lines;
            }
        }

        // clearing lines
        if(lines > 0){
            for(int i = 0; i < numberOfRows; i ++){
                if(down[i] > 0){
                    for(int j = 0; j < numberOfColumns; j ++){
                        blocks[j][i].moveTo(blocks[j][i - down[i]]);
                    }
                }
            }
        }

        return lines;
    }
}
