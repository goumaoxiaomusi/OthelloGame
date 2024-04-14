

package ss.othello.server;

import ss.othello.commonUtil.*;
import ss.othello.game.AbstractPlayer;
import ss.othello.game.Mark;
import ss.othello.game.OthelloGame;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * This class is responsible for handling the client requests
 * and sending the responses back to the client.
 */
public class ClientHandler extends Thread {

	private Socket socket;

	private String clientName;


	private Integer gameRoomNumber;


	/**
	 * Constructor
	 * @param socket
	 */
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	/**
	 * this method keeps running and listening to the client requests
	 */
	public void run() {
		while (true) {
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Mission response = (Mission) ois.readObject();


				if (response.getProtocol().equals(Protocol.EXIT)) {
					ClientOnline.removeClient(clientName);
					OthelloServer.removeSocketFromMap(clientName);
					System.out.println("Client " + clientName + " has disconnected");
					this.socket.close();
					return;
				}

				if (response.getProtocol().equals(Protocol.LIST)) {
					retrieveOnlineUsers(response);
				}

				if (response.getProtocol().equals(Protocol.QUEUE)) {
					dealWithQUEUE(response);
				}

				if (response.getProtocol().equals(Protocol.REJECT)) {
					dealWithREJECT(response);
				}

				if (response.getProtocol().equals(Protocol.ACCEPT)) {
					dealWithACCEPT(response);
				}

				if (response.getProtocol().equals(Protocol.WAITING)) {
					System.out.println("Server received WAITING");
					dealWithWAITING(response);
				}


				if(response.getProtocol().equals(Protocol.GAMEOVER)) {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					ObjectOutputStream oos2 = new ObjectOutputStream(OthelloServer.getSocketFromMap(response.getOpponent()).getOutputStream());
					Mission mission = new Mission();
					mission.setProtocol(Protocol.GAMEOVER);
					mission.setWinner(response.getOpponent());
					oos.writeObject(mission);
					oos2.writeObject(mission);
				}

				if (response.getProtocol().equals(Protocol.NOTWAITING)) {
					ClientOnline.removeAnWaitingUser(clientName);
					System.out.println(ClientOnline.getWaitingUsers());
				}

				if (response.getProtocol().equals(Protocol.NEWGAME)) {
					dealWithNEWGAME(response);

				}

				if (response.getProtocol().equals(Protocol.MOVE)) {
					System.out.println("Server received MOVE");
					System.out.println(response.getRoomNumber());
					Integer gameRoomNumber = response.getRoomNumber();
					GameRoom gameRoom = GameRoomsContainer.getRoom(gameRoomNumber);
					OthelloGame game = gameRoom.getGame();
					if (response.getDecision().getDecisionReceiver().equals("computer-hard-mode") || response.getDecision().getDecisionReceiver().equals("computer-easy-mode")) {
						System.out.println("Computer move executed");
						computerMove(response.getDecision().getDecisionMaker(), response.getDecision().getIndexOfMove(),gameRoom, game);
					} else {
						for (int i = 0; i < game.getBoard().getFields().size(); i++) {
							if (game.getBoard().getField(i) == Mark.PP) {
								game.getBoard().setField(i, Mark.EMPTY);
							}
						}
						List<Mark> result = null;
						Mission mission1 = new Mission();
						mission1.setProtocol(Protocol.MOVE);
						if(response.getDecision().getIndexOfMove() != -1){
							result = game.doMove(clientName, response.getDecision().getIndexOfMove());
							Decision decision = new Decision();
							decision.setDecisionMaker(clientName);
							decision.setDecisionReceiver(response.getDecision().getDecisionReceiver());
							decision.setBoard(result);
							mission1.setDecision(decision);
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(mission1);
						}else{
							result = response.getDecision().getBoard();
						}

						List<Mark> result1 = null;
						Decision decision1 = new Decision();
						decision1.setDecisionMaker(clientName);
						decision1.setDecisionReceiver(response.getDecision().getDecisionReceiver());
						if((game.getValidMoves(game.getPlayer(response.getDecision().getDecisionReceiver()).getMark())).isEmpty()
								&& response.getDecision().getIndexOfMove() == -1){
							GameIsOver(gameRoom,response.getDecision().getDecisionReceiver());
						}else if ((game.getValidMoves(game.getPlayer(response.getDecision().getDecisionReceiver()).getMark())).isEmpty()) {
							result1 = result;
							decision1.setNoPossibleMoves(true);
							Socket opponentsSocket = OthelloServer.getSocketFromMap(response.getDecision().getDecisionReceiver());
							decision1.setBoard(result1);
							mission1.setDecision(decision1);
							mission1.setRoomNumber(gameRoomNumber);
							ObjectOutputStream oos1 = new ObjectOutputStream(opponentsSocket.getOutputStream());
							oos1.writeObject(mission1);

						} else {
							result1 = game.addPossibleMoves(response.getDecision().getDecisionReceiver(), result);
							decision1.setNoPossibleMoves(false);
							Socket opponentsSocket = OthelloServer.getSocketFromMap(response.getDecision().getDecisionReceiver());
							decision1.setBoard(result1);
							mission1.setDecision(decision1);
							ObjectOutputStream oos1 = new ObjectOutputStream(opponentsSocket.getOutputStream());
							oos1.writeObject(mission1);
						}

					}


				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				try {
					socket.close();
					ClientOnline.removeClient(clientName);
					if(ClientOnline.getWaitingUsers().contains(clientName)){
						ClientOnline.removeAnWaitingUser(clientName);
					}
					if(GameRoomsContainer.getRoomNumber(clientName) != null){
						GameRoomsContainer.removeRoom(GameRoomsContainer.getRoomNumber(clientName));
					}
					this.interrupt();
					return;
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}


		}
	}

	/**
	 * This method deals with the MOVE protocol
	 * @param player the name of the player who made the move
	 * @param indexOfMove made by the player
	 * @param gameRoom the gameRoom in which the move is made
	 * @param game the game in which the move is made
	 * @throws IOException
	 */
	public void computerMove(String player, Integer indexOfMove, GameRoom gameRoom, OthelloGame game) throws IOException {
		for (int i = 0; i < game.getBoard().getFields().size(); i++) {
			if (game.getBoard().getField(i) == Mark.PP) {
				game.getBoard().setField(i, Mark.EMPTY);
			}
		}
		if(indexOfMove != -1){
			gameRoom.getGame().doMove(player, indexOfMove);
		}

		List<Integer> validMovesComputer = game.getValidMoves(Mark.OO);
		if (!validMovesComputer.isEmpty()) {
			AbstractPlayer AIplayer = game.getOOPlayer();
			gameRoom.getGame().doMove(clientName, AIplayer.determineMove("0", gameRoom.getGame()));
			List<Integer> validMovesPlayer = game.getValidMoves(Mark.XX);
			for (int i = 0; i < validMovesPlayer.size(); i++) {
				game.getBoard().setField(validMovesPlayer.get(i), Mark.PP);
			}

			Mission mission = new Mission();
			mission.setProtocol(Protocol.MOVE);
			Decision decision = new Decision();
			decision.setDecisionMaker(AIplayer.getName());
			decision.setDecisionReceiver(clientName);
			decision.setBoard(game.getBoard().getFields());
			if(validMovesPlayer.isEmpty()){
				decision.setNoPossibleMoves(true);
			}
			mission.setDecision(decision);
			mission.setOpponent("computer");
			mission.setRoomNumber(gameRoom.getRoomNumber());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);
		} else if(game.getValidMoves(Mark.XX).isEmpty() && validMovesComputer.isEmpty()){
			System.out.println("Game is over SENT");
			GameIsOver(gameRoom, "computer");
		}

	}

	/**
	 * This method deals with the GAMEOVER protocol
	 * @param gameRoom the gameRoom in which the game is over
	 * @param opponent the opponent of the player who made the move
	 * @throws IOException
	 */
	public void GameIsOver(GameRoom gameRoom, String opponent) throws IOException {
		if(opponent.equals("computer")){
			Mission mission = new Mission();
			if(gameRoom.getGame().getWinner().getName().equals(clientName)){
				Util.updateRank(gameRoom.getGame().getWinner().getName(), "red-13/file/nameAndScore");
			}
			Scores scores = new Scores();
			scores.setXXscore(gameRoom.getGame().getBoard().countScore(Mark.XX));
			scores.setOOscore(gameRoom.getGame().getBoard().countScore(Mark.OO));
			mission.setProtocol(Protocol.GAMEOVER);
			mission.setWinner(gameRoom.getGame().getWinner().getName());
			mission.setOpponent("computer");
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);
		}else{
			Mission mission = new Mission();
			Scores scores = new Scores();
			scores.setXXscore(gameRoom.getGame().getBoard().countScore(Mark.XX));
			scores.setOOscore(gameRoom.getGame().getBoard().countScore(Mark.OO));
			Util.updateRank(gameRoom.getGame().getWinner().getName(), "red-13/file/nameAndScore");

			mission.setProtocol(Protocol.GAMEOVER);
			mission.setScores(scores);
			mission.setWinner(gameRoom.getGame().getWinner().getName());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectOutputStream oos1 = new ObjectOutputStream(OthelloServer.getSocketFromMap(opponent).getOutputStream());
			oos.writeObject(mission);
			oos1.writeObject(mission);
		}

	}

	/**
	 * This method deals with the QUEUE protocol
	 * @param response the mission received from the client
	 * @throws IOException
	 */
	public void dealWithQUEUE(Mission response) throws IOException {
		String opponent = response.getOpponent();
		String gameMode = response.getGameMode();
		System.out.println(clientName + "gameMode" +response.getProtocol());
		Mission mission = new Mission();
		if (gameMode.equals("computer-easy-mode")) {
			GameRoom gameRoom = new GameRoom(response.getUsername(), ClientOnline.getClientHandler(response.getUsername()));
			this.gameRoomNumber = gameRoom.getRoomNumber();
			gameRoom.addSecondPlayer("computer-easy-mode", this);
			GameRoomsContainer.addRoom(gameRoomNumber, gameRoom);


			mission.setProtocol(Protocol.NEWGAME);
			mission.setOpponent("computer-easy-mode");
			mission.setRoomNumber(gameRoomNumber);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);

		} else if (gameMode.equals("computer-hard-mode")) {
			GameRoom gameRoom = new GameRoom(response.getUsername(), ClientOnline.getClientHandler(response.getUsername()));
			this.gameRoomNumber = gameRoom.getRoomNumber();
			gameRoom.addSecondPlayer("computer-hard-mode", this);
			GameRoomsContainer.addRoom(gameRoomNumber, gameRoom);


			mission.setProtocol(Protocol.NEWGAME);
			mission.setOpponent("computer-hard-mode");
			mission.setRoomNumber(gameRoomNumber);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);

		} else if (verifyPlayerIsNotPlaying(opponent) && verifyPlayerIsWaiting(opponent)) {
			System.out.println(clientName + "sent REQUEST to " + opponent);
			Socket opponentsSocket = OthelloServer.getSocketFromMap(opponent);
			mission.setProtocol(Protocol.REQUEST);
			mission.setOpponent(clientName);
			mission.setGameMode(gameMode);
			ObjectOutputStream oos = new ObjectOutputStream(opponentsSocket.getOutputStream());
			oos.writeObject(mission);
		} else if (verifyPlayerIsNotPlaying(opponent) && !verifyPlayerIsWaiting(opponent)) {
			mission.setOpponent(opponent);
			mission.setProtocol(Protocol.NOTWAITING);
			mission.setGameMode(gameMode);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);
		} else if (!verifyPlayerIsNotPlaying(opponent)) {
			mission.setProtocol(Protocol.BUSY);
			mission.setOpponent(opponent);
			mission.setGameMode(gameMode);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);
		}

	}

	/**
	 * This method deals with the REQUEST protocol
	 * @param response the mission received from the client
	 * @throws IOException
	 */
	public void dealWithREJECT(Mission response) throws IOException {
		Socket opponentsSocket = OthelloServer.getSocketFromMap(response.getOpponent());
		Mission mission = new Mission();
		mission.setProtocol(Protocol.REJECT);
		mission.setGameMode(response.getGameMode());
		mission.setOpponent(clientName);
		ObjectOutputStream oos = new ObjectOutputStream(opponentsSocket.getOutputStream());
		oos.writeObject(mission);
		System.out.println(clientName + " sent REJECT to " + response.getOpponent());
	}

	/**
	 * This method deals with the ACCEPT protocol
	 * @param response the mission received from the client
	 * @throws IOException
	 */
	public void dealWithACCEPT(Mission response) throws IOException {
		Socket opponentsSocket = OthelloServer.getSocketFromMap(response.getOpponent());
		Mission mission = new Mission();
		mission.setProtocol(Protocol.ACCEPT);
		mission.setOpponent(clientName);
		mission.setGameMode(response.getGameMode());
		mission.setRoomNumber(response.getRoomNumber());
		ObjectOutputStream oos = new ObjectOutputStream(opponentsSocket.getOutputStream());
		oos.writeObject(mission);
	}

	/**
	 * This method deals with the WAITING protocol
	 * @param response the mission received from the client
	 * @throws IOException
	 */
	public void dealWithWAITING(Mission response) throws IOException {
		ClientOnline.addAnWaitingUser(clientName);
		Mission mission = new Mission();
		mission.setProtocol(Protocol.WAITING);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(mission);
		System.out.println("SERVER SENT WAITING");
	}

	/**
	 * This method deals with the NEWGAME protocol
	 * @param response the mission received from the client
	 * @throws IOException
	 */
	public void dealWithNEWGAME(Mission response) throws IOException {
		Mission mission = new Mission();
		mission.setProtocol(Protocol.NEWGAME);
		mission.setGameMode(response.getGameMode());
		System.out.println(clientName + "gameMode" +response.getProtocol());
		mission.setOpponent(response.getOpponent());
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

		if (response.getRoomNumber() == null) {
			GameRoom gameRoom = new GameRoom(clientName, this);
			this.gameRoomNumber = gameRoom.getRoomNumber();
			GameRoomsContainer.addRoom(gameRoomNumber, gameRoom);
			mission.setRoomNumber(this.gameRoomNumber);
			mission.setMark(Mark.XX);

			Mission mission1 = new Mission();
			mission1.setProtocol(Protocol.ACCEPT);
			mission1.setOpponent(clientName);
			mission1.setRoomNumber(gameRoomNumber);
			mission1.setGameMode(response.getGameMode());
			Socket opponentsSocket = OthelloServer.getSocketFromMap(response.getOpponent());
			ObjectOutputStream oos1 = new ObjectOutputStream(opponentsSocket.getOutputStream());
			oos1.writeObject(mission1);
		} else {
			this.gameRoomNumber = response.getRoomNumber();
			mission.setGameMode(response.getGameMode());
			GameRoom gameRoom1 = GameRoomsContainer.getRoom(this.gameRoomNumber);
			gameRoom1.addSecondPlayer(clientName, this);
			mission.setRoomNumber(this.gameRoomNumber);
			mission.setMark(Mark.OO);
		}

		oos.writeObject(mission);
	}

	/**
	 * this methods get all the users that are in the waiting list
	 * @param response the mission received from the client
	 * @throws IOException
	 */
	public void retrieveOnlineUsers(Mission response) throws IOException {
		Mission mission = new Mission();
		mission.setProtocol(Protocol.LIST);
		mission.setOpponent(response.getOpponent());
		mission.setGameMode(response.getGameMode());
		mission.setOnlinePlayers(ClientOnline.getWaitingUsers());
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(mission);
	}

	/**
	 * This method checks whether  a player is in a game room
	 * @param opponentName the name of the opponent
	 * @return true if this player is in a game room
	 */
	public boolean verifyPlayerIsNotPlaying(String opponentName) {
		if (GameRoomsContainer.getRoomNumber(opponentName) == null) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks whether a player is in the waiting list
	 * @param opponentName the name of the opponent that we want to check
	 * @return true if this player is in the waiting list
	 * @throws IOException if the connection is lost
	 */
	public boolean verifyPlayerIsWaiting(String opponentName) throws IOException {
		if (!(ClientOnline.getWaitingUsers().contains(opponentName))) {
			return false;
		}
		return true;
	}


	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	public void setGameRoomNumber(Integer gameRoomNumber) {
		this.gameRoomNumber = gameRoomNumber;
	}
}

