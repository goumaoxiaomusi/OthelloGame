package ss.othello.commonUtil;

import ss.othello.game.Mark;
import ss.othello.gui.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * This class is used to transfer data between client and server.
 */
public class Mission implements Serializable {

	private static final long serialVersionUID = 1L;
	private String protocol;

	private String username;

	private Player player;

	private Mark mark;

	private Decision decision;


	private ArrayList onlinePlayers;

	private String opponent;

	private Integer roomNumber;

	private String winner;

	private Scores scores;

	private String gameMode;

	public Scores getScores() {
		return scores;
	}

	public String getGameMode() {
		return gameMode;
	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	public void setScores(Scores scores) {
		this.scores = scores;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	public ArrayList getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(ArrayList onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}


	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}
}
