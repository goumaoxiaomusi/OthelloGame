package ss.othello.game;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;


public class OthelloBoardTest {

    private Board board;

    @BeforeEach
    public void setUp(){
        board = new Board();
    }

    @Test
    public void testSetup(){
        assertEquals(Mark.OO, board.getField(27));
        assertEquals(Mark.OO, board.getField(36));
        assertEquals(Mark.XX, board.getField(28));
        assertEquals(Mark.XX, board.getField(35));
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
            if(i== 27 || i==36 || i==28 || i==35){
                continue;
            }
            indices.add(i);
        }
        for(Integer index :indices){
            assertEquals(Mark.EMPTY, board.getField(index));
        }
    }

    @Test
    public void testReset(){
        board.setField(13, Mark.OO);
        board.setField(Board.DIM+Board.DIM, Mark.XX);
        board.reset();
        assertEquals(Mark.EMPTY, board.getField(13));
        assertEquals(Mark.OO, board.getField(27));
        assertEquals(Mark.OO, board.getField(36));
        assertEquals(Mark.XX, board.getField(28));
        assertEquals(Mark.XX, board.getField(35));
        assertEquals(Mark.EMPTY, board.getField(Board.DIM+Board.DIM));
    }

    @Test
    public void testIndex(){
        int index = 0;
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                assertEquals(index, board.index(i, j));
                index += 1;
            }
        }
        assertEquals(0, board.index(0, 0));
        assertEquals(11, board.index(1, 3));
        assertEquals(37, board.index(4, 5));
        assertEquals(52, board.index(6, 4));
    }

    @Test
    public void testCheckRowFromLeft(){
        board.setField(0,Mark.OO);
        board.setField(1,Mark.XX);
        board.setField(2,Mark.XX);
        board.setField(3,Mark.XX);
        assertTrue(board.checkRowFromLeft(Mark.XX).contains(26));
        assertTrue(board.checkRowFromLeft(Mark.OO).contains(34));

        /*
            |    |    |    |    |    |    |          0 |  1 |  2 |  3 |  4 |  5 |  6 |  7
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |          8 |  9 | 10 | 11 | 12 | 13 | 14 | 15
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |         16 | 17 | 18 | 19 | 20 | 21 | 22 | 23
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |  O |  X |    |    |         24 | 25 | 26 | 27 | 28 | 29 | 30 | 31
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |  X |  O |    |    |         32 | 33 | 34 | 35 | 36 | 37 | 38 | 39
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |         40 | 41 | 42 | 43 | 44 | 45 | 46 | 47
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |         48 | 49 | 50 | 51 | 52 | 53 | 54 | 55
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
          X |  O |    |    |  X |  O |  O |         56 | 57 | 58 | 59 | 60 | 61 | 62 | 63
         */
        board.setField(60, Mark.XX);
        board.setField(62, Mark.OO);
        board.setField(61, Mark.OO);

        board.setField(56, Mark.XX);
        board.setField(57, Mark.OO);
        assertTrue(board.checkRowFromLeft(Mark.XX).contains(26));
        assertTrue(board.checkRowFromLeft(Mark.OO).contains(34));
        assertTrue(board.checkRowFromLeft(Mark.OO).contains(59));

    }

    @Test
    public void testCheckRowFromRight(){
        /*
            |    |    |    |    |    |    |          0 |  1 |  2 |  3 |  4 |  5 |  6 |  7
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |          8 |  9 | 10 | 11 | 12 | 13 | 14 | 15
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |  O |  X |    |    |  O |  O |  X      16 | 17 | 18 | 19 | 20 | 21 | 22 | 23
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |  O |  X |    |    |         24 | 25 | 26 | 27 | 28 | 29 | 30 | 31
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |  X |  O |    |    |         32 | 33 | 34 | 35 | 36 | 37 | 38 | 39
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |         40 | 41 | 42 | 43 | 44 | 45 | 46 | 47
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |         48 | 49 | 50 | 51 | 52 | 53 | 54 | 55
        ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
            |    |    |    |    |    |    |         56 | 57 | 58 | 59 | 60 | 61 | 62 | 63
         */
        board.setField(23, Mark.XX);
        board.setField(22, Mark.OO);
        board.setField(21, Mark.OO);

        board.setField(18, Mark.XX);
        board.setField(17, Mark.OO);
        assertTrue(board.checkRowFromRight(Mark.XX).contains(37));
        assertTrue(board.checkRowFromRight(Mark.OO).contains(19));
        assertTrue(board.checkRowFromRight(Mark.OO).contains(29));


    }

    @Test
    public void testCheckDown(){
        board.setField(58, Mark.XX);
        board.setField(50, Mark.OO);

        board.setField(34, Mark.XX);
        board.setField(26, Mark.OO);
        board.setField(18, Mark.OO);

        assertTrue(board.checkDown(Mark.XX).size() == 3);
        assertTrue(board.checkDown(Mark.XX).contains(19));
        assertTrue(board.checkDown(Mark.XX).contains(10));
        assertTrue(board.checkDown(Mark.XX).contains(42));

        assertFalse(board.checkDown(Mark.OO).isEmpty());
        assertTrue(board.checkDown(Mark.OO).size() == 1);
        assertTrue(board.checkDown(Mark.OO).contains(20));

        board.reset();

        board.setField(31, Mark.OO);
        board.setField(39, Mark.XX);

        board.setField(42, Mark.XX);

        board.setField(13, Mark.XX);
        board.setField(21, Mark.OO);
        board.setField(29, Mark.OO);
        board.setField(37, Mark.OO);
        board.setField(45, Mark.OO);
        board.setField(53, Mark.OO);
        board.setField(61, Mark.OO);

        assertTrue(board.checkDown(Mark.XX).size() == 2);
        assertTrue(board.checkDown(Mark.XX).contains(19));
        assertTrue(board.checkDown(Mark.XX).contains(23));

        assertTrue(board.checkDown(Mark.OO).size() == 2);
        assertTrue(board.checkDown(Mark.OO).contains(5));
        assertTrue(board.checkDown(Mark.OO).contains(20));
    }

    @Test
    public void testCheckUp(){
        board.setField(2, Mark.XX);
        board.setField(10, Mark.XX);

        board.setField(55, Mark.OO);
        board.setField(47, Mark.OO);
        board.setField(39, Mark.XX);


        board.setField(0, Mark.XX);
        board.setField(8, Mark.OO);
        board.setField(16, Mark.OO);

        assertTrue(board.checkUp(Mark.XX).size() == 3);
        assertTrue(board.checkUp(Mark.XX).contains(24));
        assertTrue(board.checkUp(Mark.XX).contains(63));
        assertTrue(board.checkUp(Mark.XX).contains(44));

        assertTrue(board.checkUp(Mark.OO).size() == 1);
        assertTrue(board.checkUp(Mark.OO).contains(43));
    }



    @Test
    public void testEmptyFieldsLeftDiagonal(){
        board.setField(40, Mark.XX);
        board.setField(49, Mark.OO);
        board.setField(58, Mark.XX);

        board.setField(40, Mark.EMPTY);
        board.setField(58, Mark.EMPTY);


    }


    @Test
    public void testCheckLeftDiagonalDown(){        /*
        |    |    |    |    |    |    |          0 |  1 |  2 |  3 |  4 |  5 |  6 |  7
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |  X |          8 |  9 | 10 | 11 | 12 | 13 | 14 | 15
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |    |    |    |    |  O      16 | 17 | 18 | 19 | 20 | 21 | 22 | 23
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |  X |    |  O |  X |    |  X |         24 | 25 | 26 | 27 | 28 | 29 | 30 | 31
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |  X |  O |    |    |  O      32 | 33 | 34 | 35 | 36 | 37 | 38 | 39
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |  X |    |         40 | 41 | 42 | 43 | 44 | 45 | 46 | 47
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |  X |    |  X |    |    |    |         48 | 49 | 50 | 51 | 52 | 53 | 54 | 55
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |    |  O |    |    |         56 | 57 | 58 | 59 | 60 | 61 | 62 | 63
         */
        board.setField(25,Mark.XX);
        board.setField(34,Mark.OO);

        board.setField(51,Mark.XX);
        board.setField(60,Mark.OO);

        board.setField(49,Mark.XX);
        board.setField(58,Mark.OO);

        board.setField(18,Mark.OO);
        board.setField(45,Mark.XX);

        board.setField(14,Mark.XX);
        board.setField(23,Mark.OO);

        board.setField(39,Mark.OO);
        board.setField(30,Mark.XX);



        assertTrue(board.checkLeftDiagonalDown(Mark.OO).contains(40));
        assertTrue(board.checkLeftDiagonalDown(Mark.OO).contains(42));
        assertTrue(board.checkLeftDiagonalDown(Mark.OO).contains(16));
        assertTrue(board.checkLeftDiagonalDown(Mark.OO).contains(21));
        assertTrue(board.checkLeftDiagonalDown(Mark.OO).contains(5));

        assertTrue(board.checkLeftDiagonalDown(Mark.XX).contains(9));

    }

    @Test
    public void testCheckLeftDiagonalUp(){
        /*
        |    |    |    |    |    |    |          0 |  1 |  2 |  3 |  4 |  5 |  6 |  7
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |  X |          8 |  9 | 10 | 11 | 12 | 13 | 14 | 15
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |    |    |    |    |  O      16 | 17 | 18 | 19 | 20 | 21 | 22 | 23
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |  X |    |  O |  X |    |  X |         24 | 25 | 26 | 27 | 28 | 29 | 30 | 31
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |  X |  O |    |    |  O      32 | 33 | 34 | 35 | 36 | 37 | 38 | 39
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |  X |    |         40 | 41 | 42 | 43 | 44 | 45 | 46 | 47
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |  X |    |  X |    |    |    |         48 | 49 | 50 | 51 | 52 | 53 | 54 | 55
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |    |  O |    |    |         56 | 57 | 58 | 59 | 60 | 61 | 62 | 63
         */
        board.setField(25,Mark.XX);
        board.setField(34,Mark.OO);

        board.setField(51,Mark.XX);
        board.setField(60,Mark.OO);

        board.setField(49,Mark.XX);
        board.setField(58,Mark.OO);

        board.setField(18,Mark.OO);
        board.setField(45,Mark.XX);

        board.setField(14,Mark.XX);
        board.setField(23,Mark.OO);

        board.setField(39,Mark.OO);
        board.setField(30,Mark.XX);

        assertTrue(board.checkLeftDiagonalUp(Mark.OO).contains(54));

        assertTrue(board.checkLeftDiagonalUp(Mark.XX).contains(43));

    }



    @Test
    public void testCheckRightDiagonalDown(){
        /*
        |    |    |    |    |    |    |          0 |  1 |  2 |  3 |  4 |  5 |  6 |  7
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |  X |          8 |  9 | 10 | 11 | 12 | 13 | 14 | 15
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |    |    |    |    |  O      16 | 17 | 18 | 19 | 20 | 21 | 22 | 23
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |  X |    |  O |  X |    |  X |         24 | 25 | 26 | 27 | 28 | 29 | 30 | 31
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |  X |  O |    |    |  O      32 | 33 | 34 | 35 | 36 | 37 | 38 | 39
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |  X |    |         40 | 41 | 42 | 43 | 44 | 45 | 46 | 47
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |  X |    |  X |    |    |    |         48 | 49 | 50 | 51 | 52 | 53 | 54 | 55
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |  O |    |  O |    |    |         56 | 57 | 58 | 59 | 60 | 61 | 62 | 63
         */
        board.setField(25,Mark.XX);
        board.setField(34,Mark.OO);

        board.setField(51,Mark.XX);
        board.setField(60,Mark.OO);

        board.setField(49,Mark.XX);
        board.setField(58,Mark.OO);

        board.setField(18,Mark.OO);
        board.setField(45,Mark.XX);

        board.setField(14,Mark.XX);
        board.setField(23,Mark.OO);

        board.setField(39,Mark.OO);
        board.setField(30,Mark.XX);


        assertTrue(board.checkRightDiagonalDown(Mark.XX).contains(11));
        assertTrue(board.checkRightDiagonalDown(Mark.OO).contains(44));

    }

    @Test
    public void testCheckRightDiagonalUp(){
        /*
        |    |    |    |    |    |    |          0 |  1 |  2 |  3 |  4 |  5 |  6 |  7
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |  O |          8 |  9 | 10 | 11 | 12 | 13 | 14 | 15
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |  O |  O |    |    |  X      16 | 17 | 18 | 19 | 20 | 21 | 22 | 23
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |  O |  O |  X |    |         24 | 25 | 26 | 27 | 28 | 29 | 30 | 31
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |  X |  O |  O |    |         32 | 33 | 34 | 35 | 36 | 37 | 38 | 39
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |  X |  O      40 | 41 | 42 | 43 | 44 | 45 | 46 | 47
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |    |         48 | 49 | 50 | 51 | 52 | 53 | 54 | 55
    ----+----+----+----+----+----+----+----    ----+----+----+----+----+----+----+----
        |    |    |    |    |    |    |         56 | 57 | 58 | 59 | 60 | 61 | 62 | 63
         */
        board.setField(29,Mark.XX);
        board.setField(47,Mark.OO);
        board.setField(20,Mark.OO);

        board.setField(14,Mark.OO);
        board.setField(23,Mark.XX);

        board.setField(19,Mark.OO);
        board.setField(28,Mark.OO);
        board.setField(37,Mark.OO);
        board.setField(46,Mark.XX);

        assertTrue(board.checkRightDiagonalUp(Mark.XX).contains(43));
        assertTrue(board.checkRightDiagonalUp(Mark.OO).contains(42));
    }

















































}
