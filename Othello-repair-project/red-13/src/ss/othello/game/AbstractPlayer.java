package ss.othello.game;

/**
 * This is the abstract class of the player. It contains the name and mark of the player.
 */
public abstract class AbstractPlayer implements Player {

    private String name;

    private Mark mark;

    /**
     * Set the name of the player
     * @param name
     */
    public AbstractPlayer(String name) {
        this.name = name;
    }

    /**
     * This is the constructor of the player
     */
    public AbstractPlayer() {
    }

    /**
     * A player determines their moves
     * @return
     */
    public abstract Move determineMove(String number, OthelloGame game);


    @Override
    public String toString() {
        return "Player " + name;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public String getName() {
        return this.name;
    }

}
