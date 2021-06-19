package entity;

import java.io.Serializable;

/**
 * Entity class - define user in the CEMS system. 
 * The class and its fields were included in the diagrams. The class is used to store data for
 * information about each user in the system.
 * 
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class User implements Serializable {

	private int id;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int isLogged = 0;
	private UserType userType;

	/* constructor */
	public User(int id, String password, String firstName, String lastName, String email, UserType userType) {
		super();
		this.id = id;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userType = userType;
	}

	/* constructor */
	public User(int id, String password) {
		this.id = id;
		this.password = password;
	}

	/* constructor */
	public User(int id, UserType userType) {
		this.id = id;
		this.userType = userType;
	}

	/* get Id field */
	public int getId() {
		return id;
	}

	/* set Id field */
	public void setId(int id) {
		this.id = id;
	}

	/* get Password field */
	public String getPassword() {
		return password;
	}

	/* set Password field */
	public void setPassword(String password) {
		this.password = password;
	}

	/* get FirstName field */
	public String getFirstName() {
		return firstName;
	}

	/* set FirstName field */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/* get LastName field */
	public String getLastName() {
		return lastName;
	}

	/* get LastName field */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/* get Email field */
	public String getEmail() {
		return email;
	}

	/* set Email field */
	public void setEmail(String email) {
		this.email = email;
	}

	/* get Logged status {0,1} */
	public int isLogged() {
		return isLogged;
	}

	/* set Logged status {0,1} */
	public void setLogged(int isLogged) {
		this.isLogged = isLogged;
	}

	/* toString to print message in server log */
	@Override
	public String toString() {
		return "User: " + userType + " isLogged=" + isLogged;
	}

	/* get UserType field */
	public UserType getUserType() {
		return userType;
	}

	/* set UserType field */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

}
