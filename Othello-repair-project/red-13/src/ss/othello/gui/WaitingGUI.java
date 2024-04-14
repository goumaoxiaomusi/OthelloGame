package ss.othello.gui;

import ss.othello.client.ClientService;
import ss.othello.client.Listener;
import ss.othello.client.ListenerContainer;
import ss.othello.commonUtil.Util;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLOutput;

/**
 * This class is used to create a waiting GUI when a user is waiting for other players to  invite him/her to play.
 */
public class WaitingGUI implements GUI {

	private ClientService clientService;

	private JFrame frame;

	private String opponentName;

	private String username;

	private Listener clientListener;

	private WaitingGUI waitingGUI;

	/**
	 * Create the GUI and initialize the function of buttons in the GUI
	 * @param username
	 * @param opponentName
	 */
	public WaitingGUI(String username, String opponentName) {
		this.waitingGUI = this;
		System.out.println("WaitingGUI is created");
		this.username = username;
		this.opponentName = opponentName;
		this.clientListener = ListenerContainer.getListener(username);
		JFrame frame = new JFrame();
		this.frame = frame;
		this.frame.setVisible(true);
		frame.setTitle("Waiting for Connection-" + username);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Util.removeUserFromWaitingGUI(clientListener);
					Util.sendExitToServer(username, clientListener);
					ListenerContainer.removeListener(username);
					frame.dispose();
					System.exit(0);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		frame.setBounds(100, 100, 800, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.BLACK));
		panel.setLayout(null);
		frame.setContentPane(panel);

		JLabel nameLabel = new JLabel("Waiting for connection...");
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameLabel.setBounds(300, 300, 300, 50);
		panel.add(nameLabel);
		frame.setVisible(true);

		JButton cancelButton = new JButton("Cancel Waiting");
		cancelButton.setBounds(300, 400, 200, 50);
		panel.add(cancelButton);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Util.removeUserFromWaitingGUI(clientListener);
					frame.dispose();
					new ChooseModeGUI(username);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}

			}
		});

	}

	public JFrame getFrame() {
		return frame;
	}

	@Override
	public String getUserName() {
		return null;
	}

	@Override
	public void setUserName(String userName) {

	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

