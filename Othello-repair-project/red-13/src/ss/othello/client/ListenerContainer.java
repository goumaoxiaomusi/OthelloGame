package ss.othello.client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to store all the listeners of the clients
 */
public class ListenerContainer {
	//i put all the client listeners in a container for future management
	//the String is the username of the client, the Listener is the listener of the client
	private static ConcurrentHashMap<String, Listener> listeners = new ConcurrentHashMap<>();

	/**
	 * This method is used to add a listener to the container
	 *
	 * @param username
	 * @param listener
	 */
	public static void addListener(String username, Listener listener) {
		listeners.put(username, listener);
	}

	/**
	 * This method is used to get a listener from the container
	 *
	 * @param username
	 * @return
	 */
	public static Listener getListener(String username) {
		return listeners.get(username);
	}

	/**
	 * remove a listener from the container
	 *
	 * @param username
	 */
	public static void removeListener(String username) {
		listeners.remove(username);
	}

	/**
	 * This method is used to get all the listeners in the container
	 *
	 * @return
	 */
	public static ConcurrentHashMap<String, Listener> getListeners() {
		return listeners;
	}
}
