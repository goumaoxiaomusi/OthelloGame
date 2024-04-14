package ss.othello.commonUtil;

import ss.othello.client.ClientService;
import ss.othello.client.Listener;
import ss.othello.client.ListenerContainer;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

/**
 * This class is stores all the commonly shared methods that can be used
 */
public class Util {

	/**
	 * This method update the rank record of the player
	 * @param playerName
	 * @param filePath
	 * @throws IOException
	 */
	public static void updateRank(String playerName, String filePath) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));
		properties.setProperty(playerName, Integer.toString(Integer.parseInt(properties.getProperty(playerName)) +1));
		properties.store(new FileOutputStream(filePath), null);
	}

	/**
	 * This method is used to get the rank of the player
	 * @return
	 * @throws IOException
	 */
	public static String getRank() throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("red-13/file/nameAndScore"));
		return properties.toString();
	}

	/**
	 * This method is used to add a new player to the rank
	 * @param playerName
	 * @param filePath
	 * @throws IOException
	 */
	public static void addNewPlayerToRank(String playerName, String filePath) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));
		properties.setProperty(playerName, "0");
		properties.store(new FileOutputStream(filePath), null);
	}

	/**
	 * This method is used to read the password of the player from the system file
	 * @param playerName
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readPasswords(String playerName, String filePath) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));
		return properties.getProperty(playerName);
	}

	/**
	 * This method is used to hash the password of the player fromm the system file
	 * @param password password entered by the player
	 * @return
	 */
	public static String hashPassword(String password) {
		//the password is salted with the word "Othello" before hashing
		try {
			String saltedPassword = "Othello" + password;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(saltedPassword.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is used to check if the password entered by the player is correct
	 * @param playerName
	 * @param password
	 * @param filePath where the password is stored
	 * @return true means the password and username combination entered by the user is correct
	 * @throws IOException
	 */
	public static boolean checkPassword(String playerName, String password, String filePath) throws IOException {
		return hashPassword(password).equals(readPasswords(playerName, filePath));
	}

	/**
	 * This method is used to add a new user to the system file
	 * @param playerName
	 * @param password
	 * @param filePath
	 * @throws IOException
	 */
	public static void addUserAndPassword(String playerName, String password, String filePath) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));
		properties.setProperty(playerName, hashPassword(password));
		properties.store(new FileOutputStream(filePath), null);
	}

	/**
	 * This method is used to check if the player exists in the system file, this if for creating a new account
	 * @param playerName
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean playerExists(String playerName, String filePath) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));
		return properties.containsKey(playerName);
	}

	/**
	 * This method is used to check if the player exists in the system file, this if there is then send a login to the server
	 * @param username
	 * @param clientListener
	 * @throws IOException
	 */
	public static void sendExitToServer(String username, Listener clientListener) throws IOException {
		Mission mission = new Mission();
		mission.setProtocol(Protocol.EXIT);
		ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
		oos.writeObject(mission);
		ListenerContainer.removeListener(username);
	}

	/**
	 * This method is used to remove a player from the waiting list
	 * @param clientListener
	 * @throws IOException
	 */
	public static void removeUserFromWaitingGUI(Listener clientListener) throws IOException {
		Mission mission = new Mission();
		mission.setProtocol(Protocol.NOTWAITING);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
		objectOutputStream.writeObject(mission);
	}



}

