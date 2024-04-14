package ss.othello.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used to create a HardAI player.
 */
public class HardAI implements AI{

    public final String name = "Hard";
    private final Mark mark;

    public HardAI(Mark mark) {
        this.mark = mark;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * A player determines their moves
     * @param number
     * @param game
     * @return
     */
    @Override
    public Move determinMove(String number, OthelloGame game) {
        Board board;
        Move move = new OthelloMove();
        int max = 0;
        int temp;
        int index = 0;
        List<Integer> validMoves = game.getValidMoves(Mark.OO);
        List<Integer> listOfScores = new ArrayList<>();
        List<Integer> listOfIndexes = new ArrayList<>();
        List<Board> listOfBoards = new ArrayList<>();
        Player player;
        Random random = new Random();
        player = game.getOOPlayer();
        if (!validMoves.isEmpty()) {
            for (int i = 0; i < validMoves.size(); i++) {
                listOfBoards.add(game.getBoard().deepCopy());
            }
            for (int i = 0; i < validMoves.size(); i++) {
//            listOfBoards.get(i).setField(validMoves.get(i), getMark());
                listOfBoards.get(i).flipDisc(new OthelloMove(player, getMark(), validMoves.get(i)));
                listOfScores.add(listOfBoards.get(i).countScore(getMark()));
//            System.out.println(listOfBoards.get(i).countScore(getMark()));
            }

            for (int i = 0; i < listOfScores.size(); i++) {
                temp = listOfScores.get(i);
                if (temp > max) {
                    max = temp;
                    index = validMoves.get(i);
//                System.out.println("Max " + index);
//                System.out.println("Index " + index);
                }
            }

            move.setIndex(index);
            move.setMark(getMark());
            return move;
        }
        else {
            index = random.nextInt(64);
            move.setIndex((Integer) index);
            move.setMark(getMark());
            return move;
        }
    }

    @Override
    public Mark getMark() {
        return mark;
    }
}
