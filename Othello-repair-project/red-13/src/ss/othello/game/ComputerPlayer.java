package ss.othello.game;

/**
 * This class represents a computer player.
 */
public class ComputerPlayer extends AbstractPlayer {
    private final AI ai;

    /**
     * This is the constructor of the computer player
     * @param name
     * @param ai
     */
    public ComputerPlayer(String name, AI ai) {
        super(name);
        this.ai = ai;
    }

    /**
     * A player determines their moves
     * @param number
     * @param game
     * @return
     */
    @Override
    public Move determineMove(String number, OthelloGame game) {
        Move move = this.ai.determinMove(number,game);
        move.setPlayer(this);
        move.setMark(this.getMark());
        move.setMark(getMark());
        return move;
    }

    @Override
    public void setMark(Mark mark) {
        super.setMark(mark);
    }

    public Mark getMark() {
        return ai.getMark();
    }




}
