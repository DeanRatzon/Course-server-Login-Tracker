
/** User class represents a user in the Course online members program (Maman 16 Question B)
 * 
 * @author Dean Ratzon
 * @version 17/01/19
 */

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

	private String username;
	private InetAddress ip;
	private int port;
	private boolean logged;
	private String logTime;

	/**
	 * Constructs a user with given parameters
	 * 
	 * @param username
	 *            represents the user's username
	 * @param ip
	 *            represents user's ip address
	 * @param port
	 *            represents the user's port address
	 * @param logged
	 *            represents the login state of the user
	 */
	public User(String username, InetAddress ip, int port, boolean logged) {
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.logged = logged;
		logTime = new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date());
	}

	/**
	 * Constructs a user with a given username
	 * 
	 * @param username
	 *            represents the user's username
	 */
	public User(String username) {
		this.username = username;
		this.ip = null;
		this.port = 0;
		this.logged = false;
		this.logTime = null;
	}

	/**
	 * Gets the user's username
	 * 
	 * @return the user's username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the user's ip address
	 * 
	 * @return the user's ip address
	 */
	public InetAddress getIP() {
		return ip;
	}

	/**
	 * Gets the user's port
	 * 
	 * @return the user's port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Gets the current login state of the user
	 * 
	 * @return true if logged , false otherwise
	 */
	public boolean getLogged() {
		return logged;
	}

	/**
	 * Gets this user's logging time
	 * 
	 * @return this user's logging time
	 */
	public String getLogTime() {
		return logTime;
	}

	/**
	 * Sets the user's ip addres
	 * 
	 * @param ip
	 *            represents the user's ip addres to be set
	 */
	public void setIP(InetAddress ip) {
		this.ip = ip;

	}

	/**
	 * Sets the user's port
	 * 
	 * @param port
	 *            represents the user's port to be set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Sets the user to logged or logged out
	 * 
	 * @param logged
	 *            represents the user's login state to be set
	 */
	public void setLogged(boolean logged) {
		this.logged = logged;
	}

}
