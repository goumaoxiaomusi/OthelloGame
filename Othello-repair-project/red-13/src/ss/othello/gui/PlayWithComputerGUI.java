package ss.othello.gui;

import ss.othello.client.ClientService;
import ss.othello.client.Listener;
import ss.othello.client.ListenerContainer;
import ss.othello.commonUtil.*;
import ss.othello.game.Mark;
import ss.othello.server.GameRoomsContainer;
import ss.othello.server.OthelloServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayWithComputerGUI {
	public static final int DIM = 8;

	private List<JButton> buttons = new ArrayList<>();

	private String player;

	private String opponent;

	private Mark mark;

	private Listener clientListener;

	private Integer roomNumber;

	private JPanel mainPanel;

	private JFrame frame;



	public PlayWithComputerGUI(String player, String opponent,Mark mark,Integer roomNumber) {
		System.out.println("PlayWithComputerGUI"+roomNumber);
		//pass in the corresponding client service
		this.clientListener = ListenerContainer.getListener(player);
		this.opponent = opponent;
		this.player =player;
		this.roomNumber = roomNumber;
		this.mark = mark;

		initialGUISetUp();

	}



	public void updateBoard(List<Mark> board) {

		int i = 0;
		for(; i < board.size(); i++) {
			if(board.get(i) == Mark.XX) {
				this.buttons.get(i).setIcon(new ImageIcon("red-13/img/black.png"));
			}
			else if(board.get(i) == Mark.OO) {
				this.buttons.get(i).setIcon(new ImageIcon("red-13/img/white.png"));
			}else if(board.get(i)==Mark.PP){
				this.buttons.get(i).setIcon(new ImageIcon("red-13/img/possible.png"));
			}else {
				this.buttons.get(i).setIcon(null);
			}

		}

	}

	public void setUpButtons(JFrame frame, JPanel mainPanel, List<JButton> buttons){
		int i = 0;
		for(; i < DIM*DIM; i++) {
			JButton button = new JButton();
			button.setOpaque(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(true);
			mainPanel.add(button);
			buttons.add(i, button);

			//only allow users to click on the possible moves
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if(button.getIcon() == null ||button.getIcon().toString().contains("black")
							|| button.getIcon().toString().contains("white")) {
						JOptionPane.showMessageDialog(frame, "This is an invalid move!");}
					else if(button.getIcon().toString().contains("possible")) {
						try {
							int clickedButtonIndex = buttons.indexOf(button);
							Mission mission = new Mission();
							mission.setProtocol(Protocol.MOVE);
							Decision decision = new Decision();
							decision.setDecisionMaker(player);
							decision.setDecisionReceiver(opponent);
							mission.setRoomNumber(roomNumber);
							decision.setIndexOfMove(clickedButtonIndex);
							mission.setDecision(decision);
							ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
							oos.writeObject(mission);
							System.out.println("Sent the move over");
						} catch (IOException ex) {
							System.out.println("Failed to send the move over");
						}

					}
				}
			});

		}
	}

	public void initialGUISetUp(){

		//set up the root frame
		JFrame frame = new JFrame("Play with Player-Room No."+roomNumber+"-"+this.player);
		this.frame = frame;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 800, 600);
		frame.setBackground(Color.DARK_GRAY);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(frame, "If you leave you will lose, Are you sure you want to exit the game?", "Exit", JOptionPane.YES_NO_OPTION);
				if(answer == JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(frame, "Game Over\n" +"The Winner is: computer " );
					GameRoomsContainer.removeRoom(roomNumber);
					ListenerContainer.removeListener(player);
					frame.dispose();
					System.exit(0);
				}
			}
		});

		//create a layered pane to hold the chess board + chess pieces
		JLayeredPane layeredPane = new JLayeredPane();

		frame.add(layeredPane);
		frame.setVisible(true);

		//create a chess board image
		ImageIcon image = new ImageIcon("red-13/img/board.jpg");


		//create a startup panel
		JPanel startup = new JPanel();
		startup.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		startup.setLayout(null);
		JLabel board = new JLabel(image);
		board.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		startup.add(board);
		layeredPane.add(startup, JLayeredPane.DEFAULT_LAYER);



		//set up a palette layer to hold the bottom buttons
		JPanel bottomButtons = new JPanel();
		bottomButtons.setOpaque(false);
		bottomButtons.setBounds(0,500,800,600);
		bottomButtons.setLayout(null);
		layeredPane.add(bottomButtons,JLayeredPane.PALETTE_LAYER);


		JButton rank = new JButton("Rank");
		rank.setBounds(0,0, 200,70);
		rank.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					JOptionPane.showMessageDialog(frame, "The name and score: \n" + Util.getRank());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});


		JButton rule = new JButton("Othello Rules");
		rule.setBounds(200,0, 200,70);
		rule.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(frame,"The rules are:\n" +"The black disc holder plays first");
			}
		});



		JButton giveUp = new JButton("Give Up");
		giveUp.setBounds(400,0, 200,70);
		giveUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int answer = JOptionPane.showConfirmDialog(frame, "If you leave you will lose, Are you sure you want to exit the game?", "Exit", JOptionPane.YES_NO_OPTION);
				if(answer == JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(frame, "Game Over\n" +"The Winner is: computer " );
					GameRoomsContainer.removeRoom(roomNumber);
					ListenerContainer.removeListener(player);
					frame.dispose();
					System.exit(0);
				}
			}
		});

		bottomButtons.add(rule);
		bottomButtons.add(rank);
		bottomButtons.add(giveUp);

		//set up a palette layer to hold the score and buttons
		JPanel scoresAndNames = new JPanel();
		scoresAndNames.setOpaque(false);
		scoresAndNames.setBounds(510,0,300,500);
		scoresAndNames.setLayout(new BoxLayout(scoresAndNames, BoxLayout.Y_AXIS));
		layeredPane.add(scoresAndNames,JLayeredPane.PALETTE_LAYER);

		JLabel space = new JLabel("         ");
		space.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel blackPlayer = new JLabel();

		blackPlayer.setForeground(Color.BLACK);
		blackPlayer.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel space1 = new JLabel("         ");
		space1.setFont(new Font("Tahoma", Font.BOLD, 30));


		JLabel space2 = new JLabel("         ");
		space2.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel space3 = new JLabel("         ");
		space3.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel whitePlayer = new JLabel();
		whitePlayer.setFont(new Font("Tahoma", Font.BOLD, 30));
		whitePlayer.setForeground(Color.WHITE);
		if(this.mark == Mark.XX){
			blackPlayer.setText(player);
			whitePlayer.setText(opponent);
		}else{
			blackPlayer.setText(opponent);
			whitePlayer.setText(player);
		}

		JLabel space4 = new JLabel("         ");
		space4.setFont(new Font("Tahoma", Font.BOLD, 30));


		scoresAndNames.add(space);
		scoresAndNames.add(blackPlayer);
		scoresAndNames.add(space1);
		scoresAndNames.add(space2);
		scoresAndNames.add(space3);
		scoresAndNames.add(whitePlayer);
		scoresAndNames.add(space4);


		//set up a panel to hold the buttons of chess pieces
		JPanel mainPanel = new JPanel();
		this.mainPanel = mainPanel;
		mainPanel.setOpaque(false);
		mainPanel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		mainPanel.setLayout(new GridLayout(DIM, DIM));



		//create DIM*DIM transparent button and store them in this board
		setUpButtons(frame, mainPanel, buttons);

		setUpInitialBoard();
		layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);




	}

	/**
	 * Set up the buttons of chess pieces and the board
	 */
	public void setUpInitialBoard() {
		//set up the initial chess pieces
		this.buttons.get((DIM/2-1)*DIM+(DIM/2-1)).setIcon(new ImageIcon("red-13/img/white.png"));


		this.buttons.get((DIM/2)*DIM+DIM/2).setIcon(new ImageIcon("red-13/img/white.png"));


		this.buttons.get((DIM/2-1)*DIM+DIM/2).setIcon(new ImageIcon("red-13/img/black.png"));
		this.buttons.get((DIM/2)*DIM+(DIM/2-1)).setIcon(new ImageIcon("red-13/img/black.png"));


		if(this.mark == Mark.XX){
			this.buttons.get((DIM/2-2)*DIM+(DIM/2-1)).setIcon(new ImageIcon("red-13/img/possible.png"));
			this.buttons.get((44)).setIcon(new ImageIcon("red-13/img/possible.png"));
			this.buttons.get(37).setIcon(new ImageIcon("red-13/img/possible.png"));
			this.buttons.get(26).setIcon(new ImageIcon("red-13/img/possible.png"));

		}
	}






	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}



	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}



	public JFrame getFrame() {
		return frame;
	}


}
