package ss.othello.game;

import java.sql.SQLOutput;
import java.util.HashMap;

public class Main {

    /**
     * This class is used to test the board manually
     * @param args
     */
    public static void main(String[] args) {
        Board board = new Board();

        board.setField(21, Mark.XX);




        System.out.println(board);
//        System.out.println(board.emptyFieldsInRightDiagonal());
//
        System.out.println(board.checkRightDiagonalUp(Mark.XX));
        System.out.println(board.checkRightDiagonalUp(Mark.OO));


    }

}