package ss.othello.gui;

import ss.othello.client.ClientService;
import ss.othello.commonUtil.Protocol;
import ss.othello.commonUtil.Util;
import ss.othello.server.OthelloServer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;

/**
 * This class is used to create a start page GUI when a user is waiting for other players to  invite him/her to play.
 */
public class StartPageGUI {

	private String name;

	private String password;

	private JFrame frame;

	private ClientService clientService;


	public static void main(String[] args) {
		new StartPageGUI();
	}

	/**
	 * Create the GUI and initialize the function of buttons in the GUI
	 */
	public StartPageGUI(){
		JFrame frame = new JFrame();
		frame.setTitle("Start Page");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 800, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.BLACK));
		panel.setLayout(null);
		panel.setLocation(0,0);
		frame.setContentPane(panel);

		JLabel coverLabel = new JLabel();
		coverLabel.setIcon(new ImageIcon("red-13/img/cover.png"));
		coverLabel.setBounds(300, -150, 800, 600);

		//set up a label to prompt user to enter their name
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameLabel.setBounds(150, 300, 100, 50);

		//set up a text field for user to enter their name
		final JFormattedTextField nameField = new JFormattedTextField();
		nameField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameField.setBounds(250, 300, 300, 50);
		nameField.setForeground(Color.BLACK);

		//set up a label to prompt user to enter their password
		JLabel pwdLabel = new JLabel("Password:");
		pwdLabel.setForeground(Color.BLACK);
		pwdLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pwdLabel.setBounds(150, 380, 100, 50);

		//set up a text field for user to enter their password
		final JPasswordField pwdField = new JPasswordField();
		pwdField.setEchoChar('*');
		pwdField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pwdField.setBounds(250, 380, 300, 50);
		pwdField.setForeground(Color.BLACK);

		//set up a button for user to submit their name and password
		JButton submitButton = new JButton("Enter Game");
		submitButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		submitButton.setBounds(300, 450, 200, 50);
		submitButton.setForeground(Color.BLACK);

		panel.add(coverLabel);
		panel.add(nameLabel);
		panel.add(nameField);
		panel.add(pwdLabel);
		panel.add(pwdField);
		panel.add(submitButton);

		this.frame = frame;
		frame.setVisible(true);

		JOptionPane.showMessageDialog(this.frame, "Trying to Connect with the Server, Please Wait");
		clientService = new ClientService();

		if(clientService.establishConnectionWithServer()) {
			JOptionPane.showMessageDialog(this.frame, "Connection Established, You can now play the game");

		} else {
				JOptionPane.showMessageDialog(this.frame, "Connection Failed, Please Restart the Game");
				System.exit(0);
			}



		//set up the key listener for the button
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				name = nameField.getText();
				name = name.trim();
				name = name.toUpperCase();
				password = String.valueOf(pwdField.getPassword());
				String result = clientService.checkCredentialWithServer(name, password);
				switch(result){
					case Protocol.NEWPLAYER:
						JOptionPane.showMessageDialog(frame, "Created an Account! Welcome!");
						frame.dispose();
						new ChooseModeGUI(name);
						try {
							Util.addNewPlayerToRank(name,"red-13/file/nameAndScore");
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
						break;
					case Protocol.LOGIN:
						JOptionPane.showMessageDialog(frame, "Welcome Back!");
						frame.dispose();
						new ChooseModeGUI(name);
						System.out.println("Login:"+name);
						break;
					case Protocol.WRONGPASSWORD:
						JOptionPane.showMessageDialog(frame, "Wrong User or Password!");
						frame.dispose();
						new StartPageGUI();
						break;
					case Protocol.ALREADAYLOGGEDIN:
						JOptionPane.showMessageDialog(frame, "You are already logged in!");
						frame.dispose();
						new StartPageGUI();
						break;
					case Protocol.SERVERISDOWN:
						JOptionPane.showMessageDialog(frame, "Sorry, the server disconnected, please try again later!");
						System.exit(0);
						break;
				}
			}

		});

	}



	public ClientService getClientService() {
		return clientService;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
