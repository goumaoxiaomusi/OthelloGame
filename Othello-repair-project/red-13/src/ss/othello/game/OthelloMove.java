package ss.othello.game;

/**
 * This class is the move class for the othello game
 */
public class OthelloMove implements Move{

    private Player player;

    private Mark mark;

    private int index;

    public OthelloMove() {
    }

    /**
     * This is the constructor of the move. It pass in the player, mark and index of the move
     * @param player
     * @param mark
     * @param index
     */
    public OthelloMove(Player player, Mark mark, int index) {
        this.player = player;
        this.mark = mark;
        this.index = index;
    }

    /**
     * Get the index of a move
     * @return the index
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Get the player of a move
     *
     * @return
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the mark of a move
     * @return
     */
    @Override
    public Mark getMark() {
        return mark;
    }

    /**
     * set the player of a move
     * @param player
     */
    @Override
    public void setPlayer(AbstractPlayer player) {
        this.player = player;
    }

    /**
     * set the index of a move
     * @param index
     */
    @Override
    public void setIndex(int index) {
        this.index = index;

    }

    /**
     * set the mark of a move
     * @param mark
     */
    @Override
    public void setMark(Mark mark) {
        this.mark = mark;

    }

    /**
     * set the player of a move
     * @param player
     */
    public void setPlayer(HumanPlayer player) {
        this.player = player;
    }
}
