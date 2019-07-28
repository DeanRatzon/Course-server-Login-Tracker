
/** This class used to read the Usernames file to be used in the Course online members program (Maman 16 Question B)
 * 
 * @author Dean Ratzon
 * @version 17/01/19
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ReadFile {

	private ArrayList<User> users = new ArrayList<User>();

	/**
	 * Reads the file and adds the users readed to the users list
	 * 
	 */
	public ReadFile() {

		try {
			Scanner input = new Scanner(new File("usernames.txt"));
			while (input.hasNext()) {
				String username = input.nextLine();
				
				users.add(new User(username));
			}
			input.close();

		} catch (java.io.FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File Not Found, Shutting down.");
			System.exit(0);
		}

	}

/**  Gets the username of the i user in the users list
 * 
 * @param i represents the index of the user in the list
 * @return the users username
 */
	public String getUserName(int i) {
		return users.get(i).getUsername();
	}
	
	/** Gets the users list size
	 * 
	 * @return the size of the users list
	 */
	public int getUserListSize()	{
		return users.size();
	}


}
