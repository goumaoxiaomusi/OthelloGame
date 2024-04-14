package ss.othello.server;

/**
 * This class is the server frame that is used to start an Othello server.
 */
public class ServerFrame {
    /**
     * starts the server thread.
     * @param args
     */
    public static void main(String[] args) {
        Thread thread = new Thread(new OthelloServer());
        thread.start();
    }
}
