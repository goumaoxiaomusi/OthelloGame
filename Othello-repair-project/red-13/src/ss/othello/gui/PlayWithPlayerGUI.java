package ss.othello.gui;

import ss.othello.client.ClientService;
import ss.othello.client.Listener;
import ss.othello.client.ListenerContainer;
import ss.othello.commonUtil.Decision;
import ss.othello.commonUtil.Mission;
import ss.othello.commonUtil.Protocol;
import ss.othello.commonUtil.Util;
import ss.othello.game.Mark;
import ss.othello.server.GameRoomsContainer;
import ss.othello.server.OthelloServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayWithPlayerGUI {

	public static final int DIM = 8;

	private List<JButton> buttons = new ArrayList<>();

	private Listener clientListener;

	private String player;

	private String opponent;

	private Mark mark;

	private Integer roomNumber;

	private JLabel blackScore;

	private JLabel whiteScore;

	private JPanel mainPanel;

	private JFrame frame;


	public PlayWithPlayerGUI(String player, String opponent, Mark mark, Integer roomNumber) {
		//pass in the corresponding client service
		this.clientListener = ListenerContainer.getListener(player);
		this.clientListener.setPlayWithPlayerGUI(this);
		this.player =player;
		this.roomNumber = roomNumber;
		this.mark = mark;
		this.opponent = opponent;

		initialGUISetUp();

		//TODO: need to prompt a dialog indicating which one win



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
							mission.setRoomNumber(roomNumber);
							Decision decision = new Decision();
							decision.setDecisionMaker(player);
							decision.setDecisionReceiver(opponent);
							decision.setIndexOfMove(clickedButtonIndex);
							mission.setDecision(decision);
							ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
							oos.writeObject(mission);
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
				if (answer == JOptionPane.YES_OPTION) {
					try {
						Util.updateRank(opponent, "red-13/file/nameAndScore");
						Mission mission = new Mission();
						mission.setProtocol(Protocol.GAMEOVER);
						mission.setWinner(opponent);
						mission.setOpponent(opponent);
						ObjectOutputStream oos1 = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
						oos1.writeObject(mission);
						JOptionPane.showMessageDialog(frame, "Game Over\n" +"The Winner is: " + opponent);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
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
					JOptionPane.showMessageDialog(frame, Util.getRank());
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
				if (answer == JOptionPane.YES_OPTION) {
					try {

						Util.updateRank(opponent, "red-13/file/nameAndScore");
						Mission mission = new Mission();
						mission.setProtocol(Protocol.GAMEOVER);
						mission.setWinner(opponent);
						mission.setOpponent(opponent);
						ObjectOutputStream oos1 = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
						oos1.writeObject(mission);
						JOptionPane.showMessageDialog(frame, "Game Over\n" +"The Winner is: " + opponent);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
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

		JLabel blackPlayer = new JLabel(player);
		blackPlayer.setForeground(Color.BLACK);
		blackPlayer.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel space1 = new JLabel("         ");
		space1.setFont(new Font("Tahoma", Font.BOLD, 30));


		JLabel space2 = new JLabel("         ");
		space2.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel space3 = new JLabel("         ");
		space3.setFont(new Font("Tahoma", Font.BOLD, 30));

		JLabel whitePlayer = new JLabel(opponent);
		whitePlayer.setFont(new Font("Tahoma", Font.BOLD, 30));
		whitePlayer.setForeground(Color.WHITE);

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

	public void setBlackScore(Integer blackScore) {
		this.blackScore.setText("Black   x  "+blackScore.toString());
	}

	public void setWhiteScore(Integer whiteScore) {
		this.blackScore.setText("White   x  "+whiteScore.toString());
	}
	public List<JButton> getButtons() {
		return buttons;
	}

	public void setButtons(List<JButton> buttons) {
		this.buttons = buttons;
	}

	public Listener getClientListener() {
		return clientListener;
	}

	public void setClientListener(Listener clientListener) {
		this.clientListener = clientListener;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public JLabel getBlackScore() {
		return blackScore;
	}

	public void setBlackScore(JLabel blackScore) {
		this.blackScore = blackScore;
	}

	public JLabel getWhiteScore() {
		return whiteScore;
	}

	public void setWhiteScore(JLabel whiteScore) {
		this.whiteScore = whiteScore;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
