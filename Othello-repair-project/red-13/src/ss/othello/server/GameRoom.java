package ss.othello.server;

import ss.othello.game.Mark;
import ss.othello.game.Move;
import ss.othello.game.OthelloGame;

/**
 * This class represents a game room.
 */
public class GameRoom {


    private Integer roomNumber;


    private ClientHandler XXPlayerClienthandler;


    private String XXPlayerName;


    private ClientHandler OOPlayerClienthandler;

    private String OOPlayerName;


    private OthelloGame game;


    private boolean full = false;

    /**
     * Constructor of the game room
     * @param XXPlayerName
     * @param XXPlayerClienthandler
     */
    public GameRoom(String XXPlayerName, ClientHandler XXPlayerClienthandler) {
        this.XXPlayerClienthandler = XXPlayerClienthandler;
        this.XXPlayerName = XXPlayerName;
        this.game = new OthelloGame();
        this.game.setXXPlayer(this.XXPlayerName);
        this.game.setTurn(this.game.getXXPlayer());
		this.roomNumber = GameRoomsContainer.getRoomNumberCounter();
        this.XXPlayerClienthandler.setGameRoomNumber(this.roomNumber);
		GameRoomsContainer.addRoom(this.roomNumber, this);
	}


    /**
     * This method add the second player to the game room
     * @param player2Name
     * @param player2
     */
    public void addSecondPlayer(String player2Name, ClientHandler player2) {
            this.OOPlayerClienthandler = player2;
            this.OOPlayerName = player2Name;
            if(player2Name.equals("computer-easy-mode") || player2Name.equals("computer-hard-mode")) {
                this.game.setPlayer2AsComputer(this.OOPlayerName);
            }else{
                this.game.setPlayer2(this.OOPlayerName);
            }
            full = true;
			player2.setGameRoomNumber(this.roomNumber);
    }


    public boolean isFull() {
        return full;
    }

    /**
     * This get the room name of the game
     *
     * @return the name of this game room
     */
    public Integer getRoomNumber() {
        return roomNumber;
    }


    /**
     * This method get the Othello game that the client is playing
     * @return the Othello game in this room
     */
    public OthelloGame getGame() {
        return game;
    }


    public void displayMove(Move move) {
		//TODO: display move on the screen

    }

	public void announceWinner() {
		//TODO
	}

	public void announceDisconnection() {

	}



    /**
     * Set the name of this game room
     * @param roomNumber the name of this game room
     */
    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Get the name of the first player
     * @return the name of the first player
     */
    public String getXXPlayerName() {
        return XXPlayerName;
    }

    /**
     * Get the name of the second player
     * @return the name of the second player
     */
    public String getOOPlayerName() {
        return OOPlayerName;
    }

    public String getTheOtherPlayer(String playerName){
        if(playerName.equals(XXPlayerName)){
            return OOPlayerName;
        }
        else return XXPlayerName;
    }


}
