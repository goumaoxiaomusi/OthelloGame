package ss.othello.commonUtil;

import java.io.Serializable;

public class Scores implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer XXscore;
	private Integer OOscore;


	public void setXXscore(Integer XXscore) {
		this.XXscore = XXscore;
	}


	public void setOOscore(Integer OOscore) {
		this.OOscore = OOscore;
	}
}
