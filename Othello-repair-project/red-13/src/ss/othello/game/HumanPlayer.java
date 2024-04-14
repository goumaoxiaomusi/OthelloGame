package ss.othello.game;

import ss.othello.commonUtil.Player;

/**
 * This class is the human player class. It extends the abstract player class
 */
public class HumanPlayer extends AbstractPlayer  {


    private Mark mark;
    private String name;

    /**
     * Constructor of human player
     * @param mark
     * @param playerName
     */
    public HumanPlayer(Mark mark,String playerName) {
//        super(playerName,mark);
        this.mark = mark;
       this.name = playerName;
    }

    /**
     * the player determine what move it will take
     * @number the index of the move
     * @return
     */

    /**
     * the player determine what move it will take
     * @param number the index of the field that the player want to put the mark on
     * @param game
     * @return
     */
    public Move determineMove(String number, OthelloGame game) {
        return new OthelloMove(this, this.getMark(), Integer.parseInt(number));
    }

    /**
     * print the player in the form of player plus their marks
     * @return
     */
    public String toString() {
        if (this.getMark() == Mark.XX) {
            return "Player1 with mark X ";
        }
        return "Player2 with mark O ";
    }

    public Mark getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }
}
