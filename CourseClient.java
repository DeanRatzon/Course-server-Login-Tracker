
/** User Side of the Course online members program using UDP Unicasts (Maman 16 Question B)
 * 
 * @author Dean Ratzon
 * @version 17/01/19
 */
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CourseClient extends JFrame implements ActionListener {
	private JTextField textField;
	private JScrollPane jScrollPanel;
	private JTextArea textArea;
	private JLabel usernameLabel;
	private JPanel usernameInput;
	private byte[] serverBuffer, clientBuffer;
	private DatagramSocket client;
	private JButton connect;
	private JButton disconnect;
	private JPanel buttons;
	private boolean login = false;
	private InetAddress host;
	private int hostPort;

	/**
	 * Starts the user GUI and receives the host address and port from the command
	 * line
	 * 
	 * @param args
	 *            represents the command line arguments received for the server info
	 */
	public static void main(String[] args) {

		String hostIP = "localhost";
		int hPort;
		if (args.length > 0) {
			hostIP = args[0];
			hPort = Integer.parseInt(args[1]);
			CourseClient userClient = new CourseClient(hostIP, hPort);
		} else {
			System.out.println(
					"Error: Command line arguments for host ip address and port are needed in order to connect to the server");
		}

	}

	/**
	 * Constructs the user GUI and communicate with the server
	 * 
	 * @param hostIP
	 *            represents the server ip address
	 * @param hPort
	 *            represents the server port
	 */
	public CourseClient(String hostIP, int hPort) {
		this.setSize(400, 400);
		this.setTitle("User");

		// Disconnect on frame close (if connected) and exit
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (login) {
					String message = "-" + textField.getText();
					clientBuffer = message.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(clientBuffer, clientBuffer.length, host, hostPort);
					try {
						client.send(sendPacket);
						System.exit(1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Username input panel
		usernameInput = new JPanel(new FlowLayout());
		usernameLabel = new JLabel("Username:");
		usernameInput.add(usernameLabel);
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(150, 20));
		usernameInput.add(textField);
		this.add(usernameInput, BorderLayout.NORTH);
		textArea = new JTextArea();
		textArea.setEditable(false);
		jScrollPanel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(jScrollPanel, BorderLayout.CENTER);

		// Buttons panel
		buttons = new JPanel();
		connect = new JButton("Connect");
		disconnect = new JButton("Disconnected");
		disconnect.setBackground(Color.red);
		disconnect.setEnabled(false);
		buttons.add(connect);
		buttons.add(disconnect);
		this.add(buttons, BorderLayout.SOUTH);
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			host = InetAddress.getByName(hostIP);
			hostPort = hPort;

			client = new DatagramSocket();

			serverBuffer = new byte[256];
			clientBuffer = new byte[256];

			while (true) {

				// Receive a message
				DatagramPacket datapack = new DatagramPacket(serverBuffer, serverBuffer.length);
				client.receive(datapack);
				String msg = new String(datapack.getData(), datapack.getOffset(), datapack.getLength());

				// Connected
				if (msg.substring(0, 4).equals("Feed")) {
					textField.setEditable(false);
					login = true;
					connect.setText("Connected");
					connect.setBackground(Color.green);
					connect.setEnabled(false);
					disconnect.setEnabled(true);
					disconnect.setText("Disconnect");
					disconnect.setBackground(null);
					textArea.append("\n" + new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date()) + "   : " + msg);

				}
				// Disconnect if its a log out message directed at this user
				else if (msg.equals(textField.getText() + " Logged out")) {
					login = false;
					textArea.append("\n" + new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date()) + "   : "
							+ textField.getText() + " Logged out");
					textField.setEditable(true);
					connect.setEnabled(true);
					connect.setText("Connect");
					connect.setBackground(null);
					disconnect.setText("Disconnected");
					disconnect.setBackground(Color.RED);
					disconnect.setEnabled(false);

				}
				// Wrong Username input
				else if (msg.equals("WRONG USERNAME")) {
					textArea.append("\nWrong username, please try again");
				}

				// Username Already logged in
				else if (msg.equals("+" + textField.getText() + " is already logged in")) {
					textArea.append("\n" + new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date()) + "   : "
							+ msg.substring(1));

				} else {
					textArea.append("\n" + new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date()) + "   : " + msg);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Action listener for the connect and disconnect buttons (logging in and out)
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == connect) {
				textArea.setText("");
				if (textField.getText().isEmpty() || textField.getText() == null
						|| String.valueOf(textField.getText().charAt(0)).equals("-")
						|| String.valueOf(textField.getText().charAt(0)).equals("+")) {
					textArea.append("\nUnauthorized charaters used in the Username field");
				} else {
					String username = textField.getText();
					// send join request
					username = "+" + username;
					clientBuffer = username.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(clientBuffer, clientBuffer.length, host, hostPort);
					client.send(sendPacket);
				}
			} else if (e.getSource() == disconnect) {

				String message = "-" + textField.getText();
				clientBuffer = message.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(clientBuffer, clientBuffer.length, host, hostPort);
				client.send(sendPacket);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}