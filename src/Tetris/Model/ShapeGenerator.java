package Tetris.Model;

import Tetris.Model.Shapes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShapeGenerator {
    List<Class<? extends  Shape>> shapeList = Arrays.asList(IShape.class, JShape.class, LShape.class, OShape.class, SShape.class, TShape.class, ZShape.class);
    List<Shape> shapes = new ArrayList<>();
    Board board;

    public ShapeGenerator(Board board){
        this.board = board;
    }

    public Shape getShape(){
        if(shapes.isEmpty()){
            generateShapes();
        }
        Shape result = shapes.get(0);
        shapes.remove(0);
        return result;
    }

    void generateShapes(){
        Collections.shuffle(shapeList);
        for(Class<? extends Shape> shapeClass: shapeList){
            try{
                shapes.add(shapeClass.getConstructor(Board.class, int.class).newInstance(board, 0));
            }catch (Exception e){
                System.out.println("No such shape");
            }
        }
    }
}
