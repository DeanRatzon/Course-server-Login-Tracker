
/** Server Side of the Course online members program using UDP Unicasts (Maman 16 Question B)
 * 
 * @author Dean Ratzon
 * @version 17/01/19
 */

import java.io.IOException;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;

public class CourseServer extends JFrame {
	private byte[] serverBuffer, clientBuffer;
	private DatagramSocket client, server;
	private static ArrayList<User> users = new ArrayList<User>();

	/**
	 * Main method, Creates a server object in order to initiallize the server
	 * 
	 *
	 */
	public static void main(String[] args) {
		CourseServer server = new CourseServer();
	}

	/**
	 * Initiallizes the server, listen to coming requests and communicate with the
	 * users
	 * 
	 */
	public CourseServer() {

		// Read users from file and copy them to the server's users list
		ReadFile file = new ReadFile();
		for (int i = 0; i < file.getUserListSize(); i++) {
			users.add(new User(file.getUserName(i)));
		}

		serverBuffer = new byte[256];
		clientBuffer = new byte[256];

		try {

			client = new DatagramSocket();
			server = new DatagramSocket(3333);
			String message;
			String receivedName;
			int userIndex = -1;
			InetAddress userAddress;
			int userPort;

			while (true) {

				// Receive user input
				DatagramPacket receivePacket = new DatagramPacket(clientBuffer, clientBuffer.length);
				server.receive(receivePacket);
				userAddress = receivePacket.getAddress();
				userPort = receivePacket.getPort();
				receivedName = new String(receivePacket.getData()).substring(0, receivePacket.getLength());

				userIndex = -1;
				DatagramPacket sendPacket;
				String name = receivedName.substring(1);

				// Check if its a login request
				if (String.valueOf(receivedName.charAt(0)).equals("+")) {

					// Find the user
					for (int j = 0; j < users.size(); j++) {
						if (name.equals(users.get(j).getUsername()) && (!users.get(j).getLogged())) {

							// Connect and store user's index, groupAddress, groupPort and set to online
							users.set(j, new User(name, userAddress, userPort, true));
							userIndex = j;

							// Tell the user that just connected who is currently online
							message = "Feed:\n";
							for (int k = 0; k < users.size(); k++) {
								if (users.get(k).getLogged()) {
									message += "\n" + users.get(k).getLogTime() + "   : " + (users.get(k).getUsername()
											+ " is Logged in from ip: " + users.get(k).getIP());
								}
							}
							serverBuffer = message.getBytes();
							sendPacket = new DatagramPacket(serverBuffer, serverBuffer.length,
									users.get(userIndex).getIP(), users.get(userIndex).getPort());
							client.send(sendPacket);

							// Tell everyone online who just connected
							message = (users.get(userIndex).getUsername() + " is Logged in from ip: "
									+ users.get(userIndex).getIP());
							for (int i = 0; i < users.size(); i++) {
								if (users.get(i).getLogged() && i != j) {
									serverBuffer = message.getBytes();
									sendPacket = new DatagramPacket(serverBuffer, serverBuffer.length,
											users.get(i).getIP(), users.get(i).getPort());
									client.send(sendPacket);
								}
							}
						}

						// User is already logged in
						else if (name.equals(users.get(j).getUsername()) && (users.get(j).getLogged())) {
							userIndex = j;
							message = receivedName + " is already logged in";
							serverBuffer = message.getBytes();
							sendPacket = new DatagramPacket(serverBuffer, serverBuffer.length,
									receivePacket.getAddress(), receivePacket.getPort());
							client.send(sendPacket);

						}
						// User not found / Wrong input
						else if (userIndex == -1 && j == users.size() - 1) {
							message = "WRONG USERNAME";
							serverBuffer = message.getBytes();
							sendPacket = new DatagramPacket(serverBuffer, serverBuffer.length, userAddress, userPort);
							client.send(sendPacket);
						}
					}
				}

				// Disconnect
				else if (String.valueOf(receivedName.charAt(0)).equals("-")) {
					for (int j = 0; j < users.size(); j++) {
						if (name.equals(users.get(j).getUsername()) && (users.get(j).getLogged())) {
							userIndex = j;
							message = (users.get(j).getUsername() + " Logged out");

							// Send everyone online a message saying who disconnected
							for (int i = 0; i < users.size(); i++) {
								if (users.get(i).getLogged()) {
									serverBuffer = message.getBytes();
									sendPacket = new DatagramPacket(serverBuffer, serverBuffer.length,
											users.get(i).getIP(), users.get(i).getPort());
									client.send(sendPacket);
								}
							}
							users.get(j).setLogged(false);
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}