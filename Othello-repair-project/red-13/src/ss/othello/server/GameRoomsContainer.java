package ss.othello.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a container for all the game rooms.
 */
public class GameRoomsContainer {

	private static ConcurrentHashMap<Integer, GameRoom> container = new ConcurrentHashMap<>();

	private static Integer roomNumberCounter = 0;

	/**
	 * This method adds a room to the container
	 *
	 * @param roomNumber
	 * @param room
	 */
	public static void addRoom(Integer roomNumber, GameRoom room) {
		container.put(roomNumber, room);
		roomNumberCounter++;
	}

	/**
	 * This method gets a room from the container
	 *
	 * @param roomNumber
	 * @return a room
	 */
	public static GameRoom getRoom(Integer roomNumber) {
		return container.get(roomNumber);
	}

	/**
	 * This method gets the room number of a player
	 *
	 * @param playerName
	 * @return a room number
	 */
	public static Integer getRoomNumber(String playerName) {
		Set<Integer> roomNumbers = container.keySet();
		for (Integer room : roomNumbers) {
			if ((container.get(room).getXXPlayerName().equals(playerName)) && !(container.get(room).isFull())) {
				return room;
			}
			if (container.get(room).getOOPlayerName().equals(playerName) && !(container.get(room).isFull())) {
				return room;
			}
		}
		return null;
	}

	//TODO: might need it later
	public static String getTheOtherUserName(String playerName, Integer roomNumber) {
		if (GameRoomsContainer.getRoom(roomNumber).getOOPlayerName().equals(playerName)) {
			return GameRoomsContainer.getRoom(roomNumber).getXXPlayerName();
		} else {
			return GameRoomsContainer.getRoom(roomNumber).getOOPlayerName();
		}
	}

	/**
	 * This method removes a room from the container
	 *
	 * @param roomNumber
	 */
	public static void removeRoom(Integer roomNumber) {
		container.remove(roomNumber);
		roomNumberCounter--;
	}

	/**
	 * This method gets the number of rooms in the container
	 * @return
	 */
	public static Integer getRoomNumberCounter() {
		return roomNumberCounter;
	}

}
