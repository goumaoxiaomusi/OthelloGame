package ss.othello.game;


public interface Move {

    /**
     * get the index of the move
     * @return
     */
    public int getIndex();

    /**
     * Get the player that makes the move
     * @return
     */
    public Player getPlayer();

    /**
     * get the mark of the player
     * @return
     */
    public Mark getMark();

    public void setPlayer(AbstractPlayer player);

    /**
     * set the index of a move
     * @param index
     */
    public void setIndex(int index);

    /**
     * set the mark of a move
     * @param mark
     */
    public void setMark(Mark mark);


}
