package ss.othello.game;

import java.util.*;

/**
 * This class represents the game Othello.
 */
public class OthelloGame implements Game {

    private Board board;

    private AbstractPlayer XXPlayer;

    private AbstractPlayer OOPlayer;

    private AbstractPlayer turn;

    /**
     * Constructor of the game
     */
    public OthelloGame(AbstractPlayer XXPlayer, AbstractPlayer OOPlayer) {
        this.XXPlayer = XXPlayer;
        this.OOPlayer = OOPlayer;
        this.board = new Board();
    }


    public OthelloGame() {
        this.board = new Board();
    }

    /**
     * check whose turn it is
     *
     * @return the player who is supposed to make the next move
     */
    @Override
    public AbstractPlayer getTurn() {
        return turn;
    }


    /**
     * Get a list of all possible moves on the board for a player
     *
     * @param mark represents the player
     * @return a list of possible moves
     */
    @Override
    public List<Integer> getValidMoves(Mark mark) {

        List<Integer> allMoves = new ArrayList<>();
        allMoves.addAll(board.checkUp(mark));
        allMoves.addAll(board.checkDown(mark));
        allMoves.addAll(board.checkRowFromLeft(mark));
        allMoves.addAll(board.checkRowFromRight(mark));
        allMoves.addAll(board.checkRightDiagonalUp(mark));
        allMoves.addAll(board.checkRightDiagonalDown(mark));
        allMoves.addAll(board.checkLeftDiagonalUp(mark));
        allMoves.addAll(board.checkLeftDiagonalDown(mark));

        Set<Integer> moves = new HashSet<>(allMoves);
        List<Integer> allMoves1 = new ArrayList<>(moves);
        Collections.sort(allMoves1);

        return allMoves1;
    }

    /**
     * Determine whether a move made by a player is a valid move
     *
     * @param move made by a player
     * @return true if the move is valid; false if the move is not valid
     */
    @Override
    public boolean isValidMove(Move move) {
        List<Integer> validMoves = getValidMoves(move.getMark());
        if (validMoves.contains(move.getIndex()) && getTurn() == move.getPlayer()) {
            return true;
        }
        return false;
    }

    /**
     * Player makes a valid move.
     *
     * @param move
     */
    @Override
    public void doMove(Move move) {
        if (!(isValidMove(move))) {
            System.out.println("This is not a valid move! You lost your chance of making the move!");
            if (move.getPlayer() == XXPlayer) {
                setTurn(OOPlayer);
            } else {
                setTurn(XXPlayer);
            }
            return;
        }

        if (getValidMoves(move.getMark()).isEmpty()) {
            System.out.println("There is no valid moves for you! Your turn is skipped");
            if (move.getPlayer() == XXPlayer) {
                setTurn(OOPlayer);
            } else {
                setTurn(XXPlayer);
            }
            return;
        }

        board.flipDisc(move);
        this.board.setField(move.getIndex(), move.getMark());
        if (move.getPlayer() == XXPlayer) {
            setTurn(OOPlayer);
        } else {
            setTurn(XXPlayer);
        }


    }

    /**
     * Make a move for a human player
     * @param humanPlayer
     * @param move
     */
    public void doMove(String humanPlayer, Move move) {
        board.flipDisc(move);
        this.board.setField(move.getIndex(), move.getMark());
        setTurn(getPlayer(humanPlayer));
        System.out.println(move.getPlayer()+":");
        System.out.println(this.board);
    }

    /**
     * Get the player object by the name of the player and mmake a move
     * @param player a human player
     * @param indexOfMove the index clicked by the user
     * @return the board in the current state
     */
    public List<Mark> doMove(String player, Integer indexOfMove){
        Move move = new OthelloMove();
        move.setPlayer(getPlayer(player));
        move.setIndex(indexOfMove);
        move.setMark(getPlayer(player).getMark());

        board.flipDisc(move);
        this.board.setField(move.getIndex(), move.getPlayer().getMark());
        if (move.getPlayer() == XXPlayer) {
            setTurn(OOPlayer);
        } else {
            setTurn(XXPlayer);
        }
        System.out.println(player+":");
        System.out.println(this.board);
        return board.getFields();

    }

    /**
     * Add possible move for the opponent and send it back to the srver
     * @param player
     * @param board
     * @return a board field after adding the possible moves
     */
    public List<Mark> addPossibleMoves(String player, List<Mark> board){
        this.board.setFields(board);
        List<Integer> possibleMoves = getValidMoves(getPlayer(player).getMark());
        for(int i = 0; i < possibleMoves.size(); i++){
            this.board.setField(possibleMoves.get(i), Mark.PP);
        }
        return this.board.getFields();

    }


    /**
     * Check if the game is over
     *
     * @return true if it is over; return false if it is not
     */
    @Override
    public boolean isGameOver() {

        if (getValidMoves(XXPlayer.getMark()).isEmpty() && getValidMoves(OOPlayer.getMark()).isEmpty()){
            return true;
        }

        return false;
    }

    /**
     * Get the winner when the game is over
     *
     * @return the winner who have the highest score
     */
    @Override
    public AbstractPlayer getWinner() {
        if (this.isGameOver()) {
            if (this.board.isWinner(Mark.XX)) {
                if (XXPlayer.getMark() == Mark.XX) {
                    return XXPlayer;
                } else {
                    return OOPlayer;
                }

            } else {
                if (XXPlayer.getMark() == Mark.OO) {
                    return XXPlayer;
                } else {
                    return OOPlayer;
                }
            }
        }
        return null;
    }

    /**
     * Get the board of this game
     *
     * @return
     */
    @Override
    public Board getBoard() {
        return this.board;
    }

    /**
     * Get the first player
     *
     * @return
     */

    public AbstractPlayer
    getXXPlayer() {
        return this.XXPlayer;
    }

    /**
     * Get the second player
     *
     * @return
     */

    public AbstractPlayer getOOPlayer() {
        return this.OOPlayer;
    }

    // Sets the board to an existing board this is meant to be used for the AI algorithm.
    public void setBoard(Board board) {
        this.board = board;
    }
    public void setXXPlayer(AbstractPlayer player1Name) {
//        this.player1 = new HumanPlayer(Mark.XX, player1Name);
        this.XXPlayer = player1Name;
    }

    /**
     * Set the first player
     * @param playerName
     */
    public void setXXPlayer(String playerName) {
        AbstractPlayer XXPlayer = new HumanPlayer(Mark.XX,playerName);
        this.XXPlayer = XXPlayer;
        this.XXPlayer.setMark(Mark.XX);
    }


    /**
     * Set the second player as a computer
     * @param playerName
     */
    public void setPlayer2(String playerName){
        AbstractPlayer player2 = new HumanPlayer(Mark.OO,playerName);
        this.OOPlayer = player2;
        this.OOPlayer.setMark(Mark.OO);
    }

    /**
     * Set the second player as a computer
     * @param aiMode
     */
    public void setPlayer2AsComputer(String aiMode){
        AbstractPlayer player2;
        if(aiMode.equals("computer-easy-mode")) {
            player2 = new ComputerPlayer(aiMode, new EasyAI(Mark.OO));
        } else{
            player2 = new ComputerPlayer(aiMode, new HardAI(Mark.OO));
        }
        this.OOPlayer = player2;
    }


    /**
     * Get the player object by the name of the player
     * @param playerName
     * @return the player
     */
    public AbstractPlayer getPlayer(String playerName) {
        if(playerName.equals(this.XXPlayer.getName())) {
            return this.XXPlayer;
        } else {
            return this.OOPlayer;
        }
    }

    /**
     *Set the turn of the player
     * @param turn
     */
    public void setTurn(AbstractPlayer turn) {
        this.turn = turn;
    }




}
