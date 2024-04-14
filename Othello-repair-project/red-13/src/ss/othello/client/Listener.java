package ss.othello.client;

import ss.othello.commonUtil.*;
import ss.othello.game.Mark;
import ss.othello.gui.*;
import ss.othello.server.ClientOnline;
import ss.othello.server.GameRoomsContainer;
//import ss.othello.gui.ChooseModeGUI;
//import ss.othello.gui.ChooseOpponentGUI;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to listen to the server
 */
public class Listener extends Thread {
	private static long lastCalledTime = 0;

	private Socket socket;

	private Player player = new Player();

	private String username;

	private String opponentName;

	private Integer roomNumer;


	private ChooseModeGUI chooseModeGUI;

	private ChooseOpponentGUI chooseOpponentGUI;


	private WaitingGUI waitingGUI;

	private PlayWithPlayerGUI playWithPlayerGUI;

	private PlayWithComputerGUI playWithComputerGUI;

	private ArrayList<String> askedPlayers = new ArrayList<>();

	public Listener(Socket socket) {
		this.socket = socket;
	}

	/**
	 * This method keeps running to listen to the server's protocol
	 */
	@Override
	public void run() {
		//keep reading the data from the server

		try {

			while (true) {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Mission response = (Mission) ois.readObject();
				System.out.println(response.getProtocol());

				if (response.getProtocol().equals(Protocol.LIST)) {
					//This if statement deal with the option of playing with random people online
					if (response.getGameMode().equals("random")) {

						if (response.getOnlinePlayers().isEmpty()) {
							JOptionPane.showMessageDialog(this.chooseModeGUI.getFrame(),
									"Sorry, no player is waiting to play, Please choose another mode");
						} else {
							if (!askedPlayers.isEmpty() && askedPlayers.size() == response.getOnlinePlayers().size()) {
								JOptionPane.showMessageDialog(this.chooseModeGUI.getFrame(),
										"Sorry, you already asked all the online players, Please choose another mode");
							}


							String opponent = null;
							for (int i = 0; i < response.getOnlinePlayers().size(); i++) {
								if (askedPlayers.isEmpty() || !askedPlayers.contains(response.getOnlinePlayers().get(i))) {
									opponent = (String) (response.getOnlinePlayers().get(i));
									this.askedPlayers.add(opponent);
									Mission mission = new Mission();
									mission.setGameMode("random");
									mission.setProtocol(Protocol.QUEUE);
									mission.setOpponent(opponent);
									ObjectOutputStream oos = new ObjectOutputStream(this.getSocket().getOutputStream());
									oos.writeObject(mission);
									this.waitingGUI = new WaitingGUI(username, opponent);
									this.chooseModeGUI.getFrame().dispose();
									break;
								}
							}
						}
					} else if (response.getGameMode().equals("specific")) {
						List<String> waitingPlayers = response.getOnlinePlayers();
						this.chooseOpponentGUI = new ChooseOpponentGUI(username, waitingPlayers);
					}

				}
				if (response.getProtocol().equals(Protocol.BUSY)) {
					JOptionPane.showMessageDialog(this.waitingGUI.getFrame(), "The player you chose is busy");
					this.waitingGUI.getFrame().dispose();
					new ChooseModeGUI(username);
					Util.removeUserFromWaitingGUI(this);

				}
				if (response.getProtocol().equals(Protocol.REJECT)) {
					if (response.getGameMode().equals("random")) {
						JOptionPane.showMessageDialog(this.waitingGUI.getFrame(), "The player " + response.getOpponent() + " rejected accepted you, please choose another opponent");
						Mission mission = new Mission();
						mission.setGameMode("random");
						mission.setProtocol(Protocol.LIST);
						ObjectOutputStream oos = new ObjectOutputStream(this.getSocket().getOutputStream());
						oos.writeObject(mission);
						this.chooseModeGUI = new ChooseModeGUI(username);
						this.waitingGUI.getFrame().dispose();
					} else if (response.getGameMode().equals("specific")) {
						JOptionPane.showMessageDialog(this.chooseOpponentGUI.getFrame(), "The player " + response.getOpponent() + " rejected accepted you, please choose another opponent");
						this.chooseOpponentGUI.getFrame().dispose();
						this.chooseModeGUI = new ChooseModeGUI(username);
					}

				}
				if (response.getProtocol().equals(Protocol.ERROR)) {
					JOptionPane.showMessageDialog(this.waitingGUI.getFrame(), "Error Occurred");
					this.waitingGUI.getFrame().dispose();
					new ChooseModeGUI(username);
					Util.removeUserFromWaitingGUI(this);

				}
				if (response.getProtocol().equals(Protocol.ACCEPT)) {
					this.opponentName = response.getOpponent();
					Mission mission = new Mission();
					mission.setProtocol(Protocol.NEWGAME);
					mission.setOpponent(this.opponentName);
					mission.setGameMode(response.getGameMode());
					mission.setRoomNumber(response.getRoomNumber());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					if (response.getGameMode().equals("random")) {
						JOptionPane.showMessageDialog(this.waitingGUI.getFrame(), "The player " + this.opponentName + " accepted you, starting a new game");
						oos.writeObject(mission);

					} else if (response.getGameMode().equals("specific")) {
						JOptionPane.showMessageDialog(this.chooseOpponentGUI.getFrame(), "The player " + this.opponentName + " accepted you, starting a new game");
						oos.writeObject(mission);
					}

				}
				if (response.getProtocol().equals(Protocol.NOTWAITING)) {
					JOptionPane.showMessageDialog(this.waitingGUI.getFrame(), "The player you chose is not waiting for a game");
					this.waitingGUI.getFrame().dispose();
					new ChooseModeGUI(username);

				}
				if (response.getProtocol().equals(Protocol.WAITING)) {
					this.waitingGUI = new WaitingGUI(username, response.getOpponent());
				}
				if (response.getProtocol().equals(Protocol.NEWGAME)) {
					if (response.getMark() == Mark.XX) {
						this.waitingGUI.getFrame().dispose();
						this.playWithPlayerGUI = new PlayWithPlayerGUI(username, response.getOpponent(), response.getMark(), response.getRoomNumber());
					} else if (response.getMark() == Mark.OO) {
						this.opponentName = response.getOpponent();
						this.setRoomNumer(response.getRoomNumber());
						if (response.getGameMode().equals("random")) {
							this.chooseModeGUI.getFrame().dispose();
							this.waitingGUI.getFrame().dispose();
						} else {
							this.chooseOpponentGUI.getFrame().dispose();
						}
						this.playWithPlayerGUI = new PlayWithPlayerGUI(username, opponentName, response.getMark(), response.getRoomNumber());
					} else if (response.getOpponent().equals("computer-easy-mode")) {
						this.opponentName = response.getOpponent();
						this.setRoomNumer(response.getRoomNumber());
						this.playWithComputerGUI = new PlayWithComputerGUI(username, "computer-easy-mode", Mark.XX, roomNumer);
						this.chooseModeGUI.getFrame().dispose();
					} else if (response.getOpponent().equals("computer-hard-mode")) {
						this.opponentName = response.getOpponent();
						this.setRoomNumer(response.getRoomNumber());
						this.playWithComputerGUI = new PlayWithComputerGUI(username, "computer-hard-mode", Mark.XX, roomNumer);
						this.chooseModeGUI.getFrame().dispose();
					}

				}
				if (response.getProtocol().equals(Protocol.REQUEST)) {
					int answer = JOptionPane.showConfirmDialog(this.waitingGUI.getFrame(),
							"Hi! " + username + ", do you want to play with " + response.getOpponent() + "?");
					if (answer == JOptionPane.YES_OPTION) {
						Util.removeUserFromWaitingGUI(this);
						this.opponentName = response.getOpponent();

						Mission mission = new Mission();
						mission.setProtocol(Protocol.NEWGAME);
						mission.setGameMode(response.getGameMode());
						mission.setOpponent(opponentName);
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(mission);
					} else {
						Mission mission = new Mission();
						mission.setProtocol(Protocol.REJECT);
						mission.setOpponent(response.getOpponent());
						mission.setGameMode(response.getGameMode());
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(mission);
					}


				}
				if (response.getProtocol().equals(Protocol.MOVE)) {
					if (response.getOpponent() == null) {
						this.getPlayWithPlayerGUI().updateBoard(response.getDecision().getBoard());
						if (response.getDecision().getNoPossibleMoves()) {
							JOptionPane.showMessageDialog(this.getPlayWithPlayerGUI().getFrame(), "No possible moves for you, turn passed to " + response.getDecision().getDecisionMaker());
							Mission mission1 = new Mission();
							mission1.setProtocol(Protocol.MOVE);
							Decision decision = new Decision();
							decision.setDecisionMaker(username);
							mission1.setRoomNumber(response.getRoomNumber());
							decision.setDecisionReceiver(response.getDecision().getDecisionMaker());
							decision.setBoard(response.getDecision().getBoard());
							decision.setIndexOfMove(-1);
							mission1.setDecision(decision);
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(mission1);

						} else {
							this.getPlayWithPlayerGUI().updateBoard(response.getDecision().getBoard());
						}

					} else if (response.getOpponent().equals("computer")) {
						if (response.getDecision().getNoPossibleMoves()) {
							this.getPlayWithComputerGUI().updateBoard(response.getDecision().getBoard());
							JOptionPane.showMessageDialog(this.getPlayWithComputerGUI().getFrame(), "No possible moves for you, turn passed to " + response.getDecision().getDecisionMaker());
							Mission mission1 = new Mission();
							mission1.setProtocol(Protocol.MOVE);
							Decision decision = new Decision();
							decision.setDecisionMaker(username);
							mission1.setRoomNumber(response.getRoomNumber());
							decision.setDecisionReceiver(response.getDecision().getDecisionMaker());
							decision.setBoard(response.getDecision().getBoard());
							decision.setIndexOfMove(-1);
							mission1.setDecision(decision);
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(mission1);
						} else {
							this.getPlayWithComputerGUI().updateBoard(response.getDecision().getBoard());
						}
					}

				}
				if (response.getProtocol().equals(Protocol.GAMEOVER)) {
					if (response.getOpponent() == null) {
						JOptionPane.showMessageDialog(this.getPlayWithPlayerGUI().getFrame(), "Game Over\n" +
								"Winner: " + response.getWinner());
						int answer = JOptionPane.showConfirmDialog(this.getPlayWithPlayerGUI().getFrame(), "Do you want to play another game?");
						if (answer == JOptionPane.YES_OPTION) {
							new ChooseModeGUI(username);
							this.getPlayWithPlayerGUI().getFrame().dispose();
						} else {
							this.getPlayWithPlayerGUI().getFrame().dispose();
							return;
						}
					} else if (response.getOpponent().equals("computer")) {
						JOptionPane.showMessageDialog(this.getPlayWithComputerGUI().getFrame(), "Game Over\n" +
								"Winner: " + response.getWinner());
						int answer = JOptionPane.showConfirmDialog(this.getPlayWithComputerGUI().getFrame(), "Do you want to play another game?");
						if (answer == JOptionPane.YES_OPTION) {
							new ChooseModeGUI(username);
							this.getPlayWithComputerGUI().getFrame().dispose();
						} else {
							this.getPlayWithComputerGUI().getFrame().dispose();
							return;
						}
					} else {
						JOptionPane.showMessageDialog(this.getPlayWithPlayerGUI().getFrame(), "Game Over\n" +
								"Winner: " + response.getWinner());
						int answer = JOptionPane.showConfirmDialog(this.getPlayWithPlayerGUI().getFrame(), "Do you want to play another game?");
						if (answer == JOptionPane.YES_OPTION) {
							new ChooseModeGUI(username);
							this.getPlayWithPlayerGUI().getFrame().dispose();
						} else {
							this.getPlayWithPlayerGUI().getFrame().dispose();
							return;
						}
					}
				}
			}

		} catch (IOException e) {
			if (this.chooseModeGUI != null) {
				JOptionPane.showMessageDialog(this.chooseModeGUI.getFrame(), "Server is down, please restart the game");
			} else if (this.waitingGUI != null) {
				JOptionPane.showMessageDialog(this.waitingGUI.getFrame(), "Server is down, please restart the game");
			} else if (this.chooseOpponentGUI != null) {
				JOptionPane.showMessageDialog(this.chooseOpponentGUI.getFrame(), "Server is down, please restart the game");
			} else if (this.playWithPlayerGUI != null) {
				JOptionPane.showMessageDialog(this.playWithPlayerGUI.getFrame(), "Server is down, please restart the game");
			} else if (this.playWithComputerGUI != null) {
				JOptionPane.showMessageDialog(this.playWithComputerGUI.getFrame(), "Server is down, please restart the game");
			}

			ListenerContainer.removeListener(username);
			if (GameRoomsContainer.getRoomNumber(username) != null) {
				GameRoomsContainer.removeRoom(GameRoomsContainer.getRoomNumber(username));
			}
			try {
				this.socket.close();
			} catch (IOException ex) {
				System.out.println("The server socket hasn't been closed");
			}
			System.exit(0);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}


	public Socket getSocket() {
		return socket;
	}


	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public void setChooseModeGUI(ChooseModeGUI chooseModeGUI) {
		this.chooseModeGUI = chooseModeGUI;
	}


	public PlayWithPlayerGUI getPlayWithPlayerGUI() {
		return playWithPlayerGUI;
	}

	public void setPlayWithPlayerGUI(PlayWithPlayerGUI playWithPlayerGUI) {
		this.playWithPlayerGUI = playWithPlayerGUI;
	}

	public PlayWithComputerGUI getPlayWithComputerGUI() {
		return playWithComputerGUI;
	}


	public void setRoomNumer(Integer roomNumer) {
		this.roomNumer = roomNumer;
	}
}


