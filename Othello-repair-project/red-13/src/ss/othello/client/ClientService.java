package ss.othello.client;

import ss.othello.commonUtil.*;
import ss.othello.game.Mark;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to handle the client's connection to the server.
 */
public class ClientService {
	private Player player = new Player();

	private Socket socket;


	public ClientService() {
	}

	/**
	 * This method is used to establish the connection with the server.
	 *
	 * @return true or false to indicate whether the connection is established.
	 */
	public boolean establishConnectionWithServer() {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 44444);
			this.socket = socket;
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Mission helloMessage = new Mission();
			helloMessage.setProtocol(Protocol.HELLO);
			oos.writeObject(helloMessage);

			//read the message object sent from the server
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Mission helloBack = (Mission) ois.readObject();
			if (helloBack.getProtocol().equals(Protocol.HELLO)) {
				System.out.println("Connection established");

				return true;
			} else {
				return false;
			}
		} catch (UnknownHostException e) {
			System.out.println("Unknown host");
		} catch (IOException e) {
			return false;
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found");
		}
		return false;
	}

	/**
	 * This method is used to send the player's decision to the server.
	 *
	 * @param playerName the name of user entered in the GUI login page.
	 * @param password   the password of user entered in the GUI login page.
	 * @return the protocol of the response from the server.
	 */
	public String checkCredentialWithServer(String playerName, String password) {

		try {
			Mission mission = new Mission();
			mission.setProtocol(Protocol.LOGIN);
			Player player = new Player();
			player.setPlayerName(playerName);
			player.setPwd(password);
			mission.setPlayer(player);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mission);

			//read the message object sent from the server
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Mission response = (Mission) ois.readObject();

			switch (response.getProtocol()) {
				case Protocol.LOGIN:
					this.player = player;
					Listener listener = new Listener(socket);
					listener.setName(player.getPlayerName());
					listener.setUsername(player.getPlayerName());
					ListenerContainer.addListener(player.getPlayerName(), listener);
					listener.start();
					return Protocol.LOGIN;
				case Protocol.WRONGPASSWORD:
					return Protocol.WRONGPASSWORD;
				case Protocol.NEWPLAYER:
					this.player = player;
					Listener listener1 = new Listener(socket);
					listener1.setName(player.getPlayerName());
					listener1.setUsername(player.getPlayerName());
					ListenerContainer.addListener(player.getPlayerName(), listener1);
					listener1.start();
					return Protocol.NEWPLAYER;
				case Protocol.ALREADAYLOGGEDIN:
					return Protocol.ALREADAYLOGGEDIN;
			}
			return null;

		} catch (IOException e) {
			System.out.println("Server is not responding");
			if (ListenerContainer.getListener(playerName) != null) {
				ListenerContainer.removeListener(playerName);
			}
			return Protocol.SERVERISDOWN;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

	}


	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}


