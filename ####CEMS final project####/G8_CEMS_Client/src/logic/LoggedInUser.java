package logic;

import entity.User;

/**
 * The class ensures the creation of a single user object in the system using
 * Singleton. In addition ensures that a user will log in at any given time from
 * a single account.
 * 
 * @author Yuval Hayam
 *
 */
public class LoggedInUser {
	private static LoggedInUser instance = null;
	private final User user;
	
	/*constructor*/
	private LoggedInUser(User user) {
		this.user = user;
	}

	/**
	 * @param user that logs in
	 * @return the singleton instance of the logged in user
	 * if the instance is null, will create a new one, by the singleton design pattern
	 */
	public static LoggedInUser getInstance(User user) {
		if (instance == null) {
			instance = new LoggedInUser(user);
		}
		return instance;
	}

	
	/**
	 * @return the logged in user if the singleton object is not null
	 */
	public User getUser() {
		if (instance != null) {
			return user;
		}
		return null;
	}

	
	/**
	 * in order to "logoff" the user, this method will put null in the singleton instance
	 */
	public void logOff() {
		instance = null;
	}

}
