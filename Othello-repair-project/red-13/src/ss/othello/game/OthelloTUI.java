package ss.othello.game;

import java.util.Scanner;

public class OthelloTUI {
    /**
     * This TUI is set up to test the game manually.
     */
    private OthelloGame game;

    public static void main(String[] args) {
        OthelloTUI tui = new OthelloTUI();
        tui.run();
        System.out.println("The Game is Over!");
    }

    /**
     * The constructor of the TUI generate a new game
     */
    public OthelloTUI() {
        AI easyAI = new EasyAI(Mark.OO);
        AI hardAI = new HardAI(Mark.XX);
//        this.game = new OthelloGame();
//        this.game = new OthelloGame(new HumanPlayer(Mark.XX,"yujun"),new HumanPlayer(Mark.OO,"marios"));
        this.game = new OthelloGame(new ComputerPlayer("yujun",hardAI),new ComputerPlayer("Marios",easyAI));

//        this.game = new OthelloGame(new ComputerPlayer("yujun",hardAI),new HumanPlayer(Mark.OO,"marios"));
//        this.game.setPlayer1("yujun");
//        this.game.setPlayer2("marios");
        this.game.setTurn(this.game.getXXPlayer());
//        this.game.getBoard().setField(42, Mark.OO);
//        this.game.getBoard().setField(49, Mark.OO);
        System.out.println(this.game.getBoard());
    }

    /**
     * The run method keep looping and prompt players to enter their moves
     */
    public void run() {


        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (this.game.isGameOver()) {
                System.out.println("The winner is: " + this.game.getWinner());
                return;
            }

            System.out.println(this.game.getValidMoves(Mark.XX));
//            String movePlayer1 = scanner.nextLine();
            Move move = this.game.getXXPlayer().determineMove("movePlayer1", this.game);
            this.game.doMove(move);


            System.out.println(this.game.getValidMoves(Mark.OO));
//            String movePlayer2 = scanner.nextLine();
            Move move2 = this.game.getOOPlayer().determineMove("movePlayer2", this.game);
            this.game.doMove(move2);
        }
    }

}
