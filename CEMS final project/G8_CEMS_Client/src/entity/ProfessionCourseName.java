package entity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The class contains a ProfessionID field and a HashMap for displaying
 * the name of the profession and the unique number that represents it
 * 
 * @author Yuval Hayam
 *
 */
@SuppressWarnings("serial")
public class ProfessionCourseName implements Serializable {

	String ProfessionID;
	HashMap<String, String> courses = new HashMap<String, String>();

	// <courseID,course name>
	/* get ProfessionID */
	public String getProfessionID() {
		return ProfessionID;
	}

	/* set ProfessionID */
	public void setProfessionID(String professionID) {
		ProfessionID = professionID;
	}

	/* get HashMap courseID,course name> of Courses */
	public HashMap<String, String> getCourses() {
		return courses;
	}

	/* set HashMap courseID,course name> of Courses */
	public void setCourses(HashMap<String, String> courses) {
		this.courses = courses;
	}

}
