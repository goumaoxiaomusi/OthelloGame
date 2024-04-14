package ss.othello.server;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents all logged-in client in the form of concurrent HashMap.
 * The key refers to the name of the client.
 * The value is the corresponding clientHandler thread.
 */
public class ClientOnline {
	private static ConcurrentHashMap<String, ClientHandler> pool = new ConcurrentHashMap<>();

	private static ArrayList<String> waitingUsers = new ArrayList<>();

	public static void addClientHandler(String userName, ClientHandler clientHandler) {
		pool.put(userName, clientHandler);
	}

	public static ClientHandler getClientHandler(String userName) {
		return pool.get(userName);
	}


	public static ArrayList<String> returnOnlineUsers() {
		Set<String> clients = pool.keySet();
		ArrayList<String> onlineUsers = new ArrayList<>();
		for (String client : clients) {
			onlineUsers.add(client);
		}
		return onlineUsers;
	}


	/**
	 * Remove a client and its corresponding clientHandler thread form the pool
	 * @param clientName the name of the client
	 */
	/*@ requires pool.contains(clientName);
    ensures pool.size() == \old(pool.size()) -1;
     */
	public static void removeClient(String clientName) {
		Set<String> clients = pool.keySet();
		for (String client : clients) {
			if (clientName.equals(client)) {
				pool.remove(client);
			}
		}
	}

	public static void addAnWaitingUser(String userName){
		waitingUsers.add(userName);
	}

	public static void removeAnWaitingUser(String userName){
		waitingUsers.remove(userName);
	}

	public static ArrayList<String> getWaitingUsers() {
		return waitingUsers;
	}
}
