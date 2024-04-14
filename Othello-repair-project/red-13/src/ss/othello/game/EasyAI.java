package ss.othello.game;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used to create an EasyAI player.
 */
public class EasyAI implements AI{

    private final static String name = "Easy";
    private final Mark mark;

    public EasyAI(Mark mark) {
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
    public Move determinMove(String number,OthelloGame game) {
        //obtain the list of all valid moves of the game
        List moves = new ArrayList<>(game.getValidMoves(getMark()));
        Random random = new Random();
        int index;
        //create a new move
        Move move = new OthelloMove();
        while (true) {
            if (moves.size() != 0) {
                 index = random.nextInt(moves.size());
                if (game.getBoard().isEmptyField((Integer )moves.get(index))) {
                    move.setIndex((Integer) moves.get(index));
                    move.setMark(getMark());
                    return move;
                }
            }
            else {
                // When there is no valid move available
                index = random.nextInt(64);
                move.setIndex((Integer) index);
                move.setMark(getMark());
                return move;
            }
            }
    }

    @Override
    public Mark getMark() {
        return this.mark;
    }
}
