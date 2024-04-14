package ss.othello.commonUtil;

import ss.othello.game.Mark;

import java.io.Serializable;
import java.util.List;

/**
 * It represents the move decision made by a player
 */
public class Decision implements Serializable {

	private static final long serialVersionUID = 1L;

	private String decisionMaker;

	private String decisionReceiver;
	private int indexOfMove;

	private boolean noPossibleMoves;


	private List<Mark> board;


	public String getDecisionMaker() {
		return decisionMaker;
	}

	public void setDecisionMaker(String decisionMaker) {
		this.decisionMaker = decisionMaker;
	}

	public String getDecisionReceiver() {
		return decisionReceiver;
	}

	public void setDecisionReceiver(String decisionReceiver) {
		this.decisionReceiver = decisionReceiver;
	}

	public int getIndexOfMove() {
		return indexOfMove;
	}

	public void setIndexOfMove(int indexOfMove) {
		this.indexOfMove = indexOfMove;
	}

	public boolean getNoPossibleMoves() {
		return noPossibleMoves;
	}

	public void setNoPossibleMoves(boolean noPossibleMoves) {
		this.noPossibleMoves = noPossibleMoves;
	}

	public List<Mark> getBoard() {
		return board;
	}

	public void setBoard(List<Mark> board) {
		this.board = board;
	}
}
