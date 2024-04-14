package ss.othello.commonUtil;

/**
 * This interface contains all the protocols that are used in the game.
 */
public interface Protocol {
	String HELLO = "HELLO";

	String SERVERISDOWN = "SERVERISDOWN";

	String LOGIN = "LOGIN";
	String ALREADAYLOGGEDIN = "ALREADAYLOGGEDIN";
	String WRONGPASSWORD = "WRONGPASSWORD";
	String NEWPLAYER = "NEWPLAYER";
	String LIST = "LIST";

	String QUEUE = "QUEUE";

	String REQUEST = "REQUEST";

	String NOTWAITING = "NOTWAITING";

	String ACCEPT = "ACCEPT";

	String REJECT = "REJECT";

	String BUSY = "BUSY";

	String WAITING = "WAITING";


	String NEWGAME = "NEWGAME";


	String MOVE = "MOVE";


	String GAMEOVER = "GAMEOVER";


	String ERROR = "ERROR";

	String EXIT = "EXIT";
}
