package entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Entity class - define Student in the CEMS system. 
 * The class and its fields were included in the diagrams. 
 * The class is used to store data information about each teacher that logged in the system.
 * The class inherits from the User class.
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class Student extends User implements Serializable {

	private float studentAvg;
	private ArrayList<Course> courses;
	/* constructor */	
	public Student(int id, String password, String firstName, String lastName, String email, UserType userType,
			float studentAvg) {
		super(id, password, firstName, lastName, email, userType);
		this.studentAvg = studentAvg;
		courses = new ArrayList<Course>();
	}
	/* constructor */	
	public Student(User userData, float studentAvg, ArrayList<Course> courses) {
		super(userData.getId(), userData.getPassword(), userData.getFirstName(), userData.getLastName(), userData.getEmail(), userData.getUserType());
		this.studentAvg = studentAvg;
		this.courses = courses;
	}
	/* constructor */	
	public Student(int id,UserType user) {
		super(id,user);
	}
	/*get StudentAvg field*/
	public float getStudentAvg() {
		return studentAvg;
	}
	/*set StudentAvg field*/
	public void setStudentAvg(float studentAvg) {
		this.studentAvg = studentAvg;
	}
	/* toString to print message in server log */
	@Override
	public String toString() {
		return "Student [studentID=" + this.getId() + ", studentAvg=" + studentAvg + "]";
	}
	/*get ArrayList of Courses belong to student*/
	public ArrayList<Course> getCourses() {
		return courses;
	}
	/*set ArrayList of Courses belong to student*/
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

}
