package ss.othello.game;

import java.util.List;

public interface Game {

    /**
     * Get the turn of  players
     * @return
     */
    AbstractPlayer getTurn();

    /**
     * Get a list of valid moves
     * @param mark
     * @return a list of valid moves on the board
     */
    List getValidMoves(Mark mark);

    /**
     * Check if a move is a valid move
     * @param move
     * @return
     */
    boolean isValidMove(Move move);

    /**
     * Do a move
     * @param move
     */
    void doMove(Move move);

    /**
     * Check if the game is over
     * @return
     */
    boolean isGameOver();

    /**
     * get the winner of the game
     * @return
     */
    AbstractPlayer getWinner();

    /**
     * Get the board
     * @return
     */
    Board getBoard();




}
