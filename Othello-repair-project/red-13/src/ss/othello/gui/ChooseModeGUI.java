package ss.othello.gui;

import ss.othello.client.ClientService;
import ss.othello.client.Listener;
import ss.othello.client.ListenerContainer;
import ss.othello.commonUtil.Mission;
import ss.othello.commonUtil.Protocol;
import ss.othello.commonUtil.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to set up the GUI for the choose mode page
 */

public class ChooseModeGUI implements GUI, Serializable {

	private static final long serialVersionUID = 1L;

	private JFrame frame;


	private String username;

	private JPanel panel;

	private Listener clientListener;

	private ChooseModeGUI chooseModeGUI;

	private ArrayList<String> waitingUsers;

	/**
	 * This method is used to set up the GUI for the choose mode page
	 * @param username
	 */
	public ChooseModeGUI(String username) {
		this.username = username;
		this.chooseModeGUI = this;
		this.clientListener = ListenerContainer.getListener(username);
		this.clientListener.setChooseModeGUI(this);
		JFrame frame = new JFrame("Choose Play Mode-" + username);
		this.frame = frame;
		initialGUI();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Util.sendExitToServer(username, clientListener);
					ListenerContainer.removeListener(username);
					frame.dispose();
					System.exit(0);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		setWaitToBeInvitedButton(this.panel);
		setUpPlayWithPeopleButton(this.panel);
		setPlayWithRandomPeopleButton(this.panel);
		setPlayWithComputerEasy(this.panel);
		setPlayWithComputerHard(this.panel);
		setUpExit(this.panel);

	}

	/**
	 * This method is used to set up the initial design of the GUI for the choose mode page
	 */
	public void initialGUI() {
		frame.setVisible(true);
		frame.setBounds(100, 100, 800, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		JPanel startup = new JPanel();
		this.panel = startup;
		startup.setBounds(100, 100, 600, 800);
		startup.setLayout(null);
		frame.setContentPane(startup);
	}

	/**
	 * This method is used to set up the waited to be invited button
	 * @param panel
	 */
	public void setWaitToBeInvitedButton(JPanel panel){
		JButton buttonWait = new JButton("Wait to be Invited");
		buttonWait.setBounds(250, 30, 250, 70);
		buttonWait.setBackground(Color.white);
		panel.add(buttonWait);

		buttonWait.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					System.out.println("The player choose to wait");
					System.out.println(clientListener);
					ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
					Mission mission = new Mission();
					mission.setProtocol(Protocol.WAITING);
					oos.writeObject(mission);
					frame.dispose();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

	}

	/**
	 * This method is used to set up the PlayWithPeople button
	 * @param panel
	 */
	public void setUpPlayWithPeopleButton(JPanel panel) {
		JButton buttonPlayWithPeople = new JButton("Select an Opponent to Play");
		buttonPlayWithPeople.setBounds(250, 120, 250, 70);
		buttonPlayWithPeople.setBackground(new Color(116,206,78));
		panel.add(buttonPlayWithPeople);
		buttonPlayWithPeople.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mission mission = new Mission();
				mission.setProtocol(Protocol.LIST);
				mission.setGameMode("specific");
				try {
					ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
					oos.writeObject(mission);
					frame.dispose();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
	}

	/**
	 * This method is used to set up the PlayWithRandomPeople button
	 * @param panel
	 */
	public void setPlayWithRandomPeopleButton(JPanel panel) {
		JButton buttonPlayWithRandomPeople = new JButton("Randomly Match an Opponent");
		buttonPlayWithRandomPeople.setBounds(250, 210, 250, 70);
		buttonPlayWithRandomPeople.setBackground(new Color(101,185,66));
		panel.add(buttonPlayWithRandomPeople);
		buttonPlayWithRandomPeople.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Mission mission = new Mission();
					mission.setGameMode("random");
					mission.setProtocol(Protocol.LIST);
					ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
					oos.writeObject(mission);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}

			}
		});
	}


	/**
	 * This method is used to set up the PlayWithComputerEasy button
	 * @param panel
	 */
	public void setPlayWithComputerEasy(JPanel panel) {
		JButton buttonPlayWithComputerEasy = new JButton("Play with computer(Easy Mode)");
		buttonPlayWithComputerEasy.setBounds(250, 300, 250, 70);
		buttonPlayWithComputerEasy.setBackground(new Color(83,151,54));
		buttonPlayWithComputerEasy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				try {
					Mission mission = new Mission();
					mission.setProtocol(Protocol.QUEUE);
					mission.setGameMode("computer-easy-mode");
					mission.setUsername(username);
					ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
					oos.writeObject(mission);

				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		panel.add(buttonPlayWithComputerEasy);

	}

	/**
	 * This method is used to set up the PlayWithComputerHard button
	 * @param panel
	 */
	public void setPlayWithComputerHard(JPanel panel) {
		JButton buttonPlayWithComputerHard = new JButton("Play with computer(Hard Mode)");
		buttonPlayWithComputerHard.setBounds(250, 390, 250, 70);
		buttonPlayWithComputerHard.setBackground(new Color(64,115,42));
		buttonPlayWithComputerHard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				try {
					Mission mission = new Mission();
					mission.setProtocol(Protocol.QUEUE);
					mission.setGameMode("computer-hard-mode");
					mission.setUsername(username);
					ObjectOutputStream oos = new ObjectOutputStream(clientListener.getSocket().getOutputStream());
					oos.writeObject(mission);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		panel.add(buttonPlayWithComputerHard);
	}

	/**
	 * This method is used to set up the Exit button
	 * @param panel
	 */
	public void setUpExit(JPanel panel) {
		JButton buttonExit = new JButton("Exit");
		buttonExit.setBounds(250, 480, 250, 70);
		buttonExit.setBackground(new Color(0,0,0));
		buttonExit.setForeground(new Color(255,255,255));
		panel.add(buttonExit);

		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exit");
				System.exit(0);
			}
		});
	}


	public JFrame getFrame() {
		return frame;
	}



	@Override
	public String getUserName() {
		return this.username;
	}

	@Override
	public void setUserName(String userName) {
		this.username = userName;
	}
}

