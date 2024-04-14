package ss.othello.game;

/**
 * This interface contains all the protocols that are used in the game.
 */
public interface AI {

    public  String getName();

    /**
     * A player determines their moves
     * @param number
     * @param game
     * @return
     */
    public Move determinMove(String number,OthelloGame game);

    public Mark getMark();
    }
