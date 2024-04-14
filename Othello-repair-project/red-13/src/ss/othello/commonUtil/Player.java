package ss.othello.commonUtil;

import java.io.Serializable;

/**
 * It represents the information of a player
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private String playerName;
	private String pwd;

	private String opponent;


	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
}
