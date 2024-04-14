package ss.othello.gui;

import ss.othello.client.ClientService;
import ss.othello.client.Listener;
import ss.othello.client.ListenerContainer;
import ss.othello.commonUtil.Mission;
import ss.othello.commonUtil.Protocol;
import ss.othello.commonUtil.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * This class is used to set up the GUI for the choose opponent page
 */
public class ChooseOpponentGUI {
	private static long lastCalledTime = 0;

	private Listener clientListener;

	private String username;
	private JFrame frame;

	private java.util.List<String> waitingUsers;

	public ChooseOpponentGUI(String username, java.util.List waiting) throws IOException, ClassNotFoundException {
		System.out.println("listner:" + ListenerContainer.getListener(username));
		this.clientListener = ListenerContainer.getListener(username);
		this.waitingUsers = waiting;
		this.username = username;
		JFrame frame = new JFrame();
		this.frame = frame;

		frame.setTitle("Choose Your Opponent-" + username);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Util.removeUserFromWaitingGUI(clientListener);
					Util.sendExitToServer(username,clientListener);
					clientListener.getSocket().close();
					System.exit(0);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});


		JPanel panel = new JPanel();
		panel.setLayout(null);
		frame.setContentPane(panel);

		//set up a label to prompt user to enter their name
		JLabel nameLabel = new JLabel("Choose Opponent From The List");
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameLabel.setBounds(300, 50, 300, 50);
		panel.add(nameLabel);

		//set up a combo box for user to choose opponent
		JComboBox<String> opponentList = new JComboBox<String>();
		opponentList.setFont(new Font("Tahoma", Font.PLAIN, 20));
		opponentList.setBounds(250, 100, 300, 50);
		opponentList.setForeground(Color.BLACK);
		for (int i = 0; i < waiting.size(); i++) {
			opponentList.addItem((String) waiting.get(i));
		}
		panel.add(opponentList);

		JButton buttonPlay = new JButton("Play With This Opponent");
		buttonPlay.setBounds(300, 150, 200, 80);
		panel.add(buttonPlay);


		JButton buttonRank = new JButton("Ranking List");
		buttonRank.setBounds(300, 350, 200, 80);
		panel.add(buttonRank);

		JButton buttonGoBack = new JButton("Go Back");
		buttonGoBack.setBounds(300, 450, 200, 80);
		panel.add(buttonGoBack);


		buttonPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String chosenOpponent = (String) opponentList.getSelectedItem();
				System.out.println("The player choose: " + chosenOpponent);
				if (chosenOpponent == null) {
					JOptionPane.showMessageDialog(frame, "Please Choose an Opponent");
				} else {
					try {
						Mission mission = new Mission();
						mission.setProtocol(Protocol.QUEUE);
						mission.setOpponent(chosenOpponent);
						mission.setGameMode("specific");
						ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
						oos.writeObject(mission);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		});

		buttonGoBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new ChooseModeGUI(username);
				frame.dispose();
			}
		});

		buttonRank.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					JOptionPane.showMessageDialog(frame, "The name and score: \n" + Util.getRank());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

	}




	public JFrame getFrame() {
		return frame;
	}


}

