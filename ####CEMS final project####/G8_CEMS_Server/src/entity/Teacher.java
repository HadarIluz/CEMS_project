package entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Entity class - define Teacher in the CEMS system. 
 * The class and its fields were included in the diagrams. 
 * The class is used to store data information about each teacher that logged in the system.
 * The class inherits from the User class.
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class Teacher extends User implements Serializable {

	private ArrayList<Profession> professions;
	
	public Teacher(int id, String password, String fullName, String lastName, String email, UserType userType,
			ArrayList<Profession> professions) {
		super(id, password, fullName, lastName, email, userType);
		professions = new ArrayList<Profession>();
	}
	/* constructor */	
	public Teacher(int id,UserType user) {
		super(id,user);
	}
	/* constructor */	
	public Teacher(User userData, ArrayList<Profession> professions) {
		super(userData.getId(), userData.getPassword(), userData.getFirstName(), userData.getLastName(), userData.getEmail(), userData.getUserType());
		this.professions = professions;
	}
	/*get ArrayList of Profession belong to teacher*/
	public ArrayList<Profession> getProfessions() {
		return professions;
	}
	/*get ArrayList of Profession belong to teacher*/
	public void setProfessions(ArrayList<Profession> professions) {
		this.professions = professions;
	}
	
	
	
}
