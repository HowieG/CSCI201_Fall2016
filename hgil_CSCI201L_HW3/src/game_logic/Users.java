package game_logic;

import java.io.Serializable;
import java.util.HashMap;

public class Users implements Serializable
{
	private static final long serialVersionUID = 1L;

	HashMap<String, String> userMap;

	public Users()
	{
		userMap = new HashMap<String, String>();
	}

	public void addUser(String username, String password)
	{
		userMap.put(username, password);
	}

	public boolean containsUser(String username)
	{
		return userMap.containsKey(username);
	}

	public boolean checkPassword(String enteredUsername, String enteredPassword)
	{
		if (userMap.containsKey(enteredUsername))
		{
			if (userMap.get(enteredUsername).equals(enteredPassword))
			{
				return true;
			}
		}

		return false;
	}

	public HashMap<String, String> getUserMap()
	{
		return userMap;
	}

}
