package entity;

import java.io.Serializable;

/**
 * Entity class - define Course in the CEMS system.
 * The department and all its fields were included in the diagrams.
 * The class is used to store data for a course that an associate to student or course that teacher teaching.
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class Course implements Serializable {
	private String courseID;
	private String courseName;
	private Profession profession;
	
	/* constructor */
	public Course(String courseID, Profession profession) {
		super();
		this.courseID = courseID;
		this.profession = profession;
	}
	/* constructor */
	public Course(String courseID, String courseName, Profession profession) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.profession = profession;
	}
	/* constructor */
	public Course(String courseID) {
		this.courseID = courseID;
	}
	/*set CourseID field*/
	public void setCourseID(String CourseID) {
		this.courseID = CourseID;
	}
	/*get CourseID field*/
	public String getCourseID() {
		return courseID;
	}
	/*get CourseName field*/
	public String getCourseName() {
		return courseName;
	}
	/*get getProfessionID object*/
	public Profession getProfessionID() {
		return profession;
	}
	/*set getProfessionID object*/
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}