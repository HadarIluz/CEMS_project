package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The department and all its fields were included in the diagrams. The
 * department is used to store data for an exam a student has done or a private
 * information created by a teacher. We've added additional fields to the
 * columns that will include exam details in various tables that display exam details.
 * 
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class Exam implements Serializable {

	private String examID;
	private Profession profession;
	private Course course;
	private int timeOfExam;
	private ArrayList<Question> questions;
	private HashMap<String, Integer> questionScores; // mapping the questionID(string) to score(integer)
	private String commentForTeacher;
	private String commentForStudents;
	private Teacher author;
	@SuppressWarnings("unused")
	private String ProfessionName;  
	private String activeExamType; // {manual \ computerized}
	private ArrayList<QuestionInExam> examQuestionsWithScores;
	private ExamStatus examStatus;

	/* constructor */
	public Exam() {
		super();
	}
	/* constructor */
	public Exam(String examID) {
		this.examID = examID;
	}
	/* constructor */
	public Exam(String examID, Profession profession, int timeOfExam) {
		this.examID = examID;
		this.profession = profession;
		this.timeOfExam = timeOfExam;
	}
	/* constructor */
	public Exam(Profession profession, Course course, int timeOfExam) {
		this.examID = "";
		this.profession = profession;
		this.timeOfExam = timeOfExam;
		this.course = course;
	}
	/* constructor */
	public Exam(String examID, Profession profession, Course course, int timeOfExam) {
		super();
		this.examID = examID;
		this.profession = profession;
		this.course = course;
		this.timeOfExam = timeOfExam;
		this.course = course;
	}
	/* constructor */
	public Exam(Course course) {
		this.course = course;
	}
	/* constructor */
	public Exam(String examID, Profession profession, Course course, int timeOfExam, ArrayList<Question> questions,
			HashMap<String, Integer> questionScores, String commentForTeacher, String commentForStudents,
			Teacher author) {
		this.examID = examID;
		this.profession = profession;
		this.course = course;
		this.timeOfExam = timeOfExam;
		this.questions = questions;
		this.questionScores = questionScores;
		this.commentForTeacher = commentForTeacher;
		this.commentForStudents = commentForStudents;
		this.author = author;
	}
	/*get ArrayList of ExamQuestionsWithScores data*/
	public ArrayList<QuestionInExam> getExamQuestionsWithScores() {
		return examQuestionsWithScores;
	}
	/*set ArrayList of ExamQuestionsWithScores data*/
	public void setExamQuestionsWithScores(ArrayList<QuestionInExam> examQuestionsWithScores) {
		this.examQuestionsWithScores = examQuestionsWithScores;
	}
	/*get getProfessionID field that saved in Profession entity of table view*/
	public String getProfessionName() {
		return profession.getProfessionID();
	}
	/*get Profession field*/
	public Profession getProfession() {
		return profession;
	}
	/*set Profession object to field*/
	public void setProfession(Profession profession) {
		this.profession = profession;
	}
	/*get Course object field*/
	public Course getCourse() {
		return course;
	}
	/*set Course object to field*/
	public void setCourse(Course course) {
		this.course = course;
	}
	/*get TimeOfExam field*/
	public int getTimeOfExam() {
		return timeOfExam;
	}
	/*set TimeOfExam field*/
	public void setTimeOfExam(int timeOfExam) {
		this.timeOfExam = timeOfExam;
	}
	/*get ArrayList of getQuestions data*/
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	/*set ArrayList of getQuestions data*/
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	/*get HashMap<String, Integer> of QuestionScores with key: questionID*/
	public HashMap<String, Integer> getQuestionScores() {
		return questionScores;
	}
	/*set HashMap<String, Integer> of QuestionScores with key: questionID*/
	public void setQuestionScores(HashMap<String, Integer> questionScores) {
		this.questionScores = questionScores;
	}
	/*get CommentForTeacher filed*/
	public String getCommentForTeacher() {
		return commentForTeacher;
	}
	/*set CommentForTeacher filed*/
	public void setCommentForTeacher(String commentForTeacher) {
		this.commentForTeacher = commentForTeacher;
	}
	/*get CommentForStudents filed*/
	public String getCommentForStudents() {
		return commentForStudents;
	}
	/*set CommentForStudents filed*/
	public void setCommentForStudents(String commentForStudents) {
		this.commentForStudents = commentForStudents;
	}
	/*get Author of exam of Teacher type object*/
	public Teacher getAuthor() {
		return author;
	}
	/*set Author of exam of Teacher type object*/
	public void setAuthor(Teacher author) {
		this.author = author;
	}
	/*get ExamID filed*/
	public String getExamID() {
		return examID;
	}
	/*set ExamID filed*/
	public void setExamID(String examID) {
		this.examID = examID;
	}
	/*get ExamStatus filed in enum class to differentiate between active and inactive tests */
	public ExamStatus getExamStatus() {
		return examStatus;
	}
	/*set ExamStatus filed in enum class to differentiate between active and inactive tests */
	public void setExamStatus(ExamStatus examStatus) {
		this.examStatus = examStatus;
	}
	/*get CourseID field that saved in Course entity of table view*/
	public String getCourseID() {
		return this.getCourse().getCourseID();
	}
	/*get ActiveExamType field*/
	public String getActiveExamType() {
		return activeExamType;
	}
	/*set ActiveExamType field*/
	public void setActiveExamType(String activeExamType) {
		this.activeExamType = activeExamType;
	}
	public void setProfessionName(String professionName) {
		ProfessionName = professionName;
	}

}
