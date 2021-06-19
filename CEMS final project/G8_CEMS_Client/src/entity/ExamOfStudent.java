package entity;

import java.io.Serializable;
import java.util.HashMap;;

/**
 * The class and all of its fields are included in our diagrams except for one
 * field which is: reasonOfSubmit, used to keep record of the reason for
 * submission for a student test in the system
 * 
 * @author Hadar Iluz
 * @author lior karish
 *
 */
@SuppressWarnings("serial")
public class ExamOfStudent implements Serializable {

	private ActiveExam activeExam;
	private Student student;
	private int score;
	private int totalTime;
	private HashMap<QuestionInExam, Integer> questionsAndAnswers;
	private String examType;
	private ReasonOfSubmit reasonOfSubmit;
	
	/* constructor */
	public ExamOfStudent(ActiveExam exam, Student student, int score) {
		super();
		this.activeExam = exam;
		this.student = student;
		this.score = score;
	}
	/* constructor */
	public ExamOfStudent(ActiveExam exam, Student student) {
		this.activeExam = exam;
		this.student = student;
	}
	
	/*get TotalTime field*/
	public int getTotalTime() {
		return totalTime;
	}
	
	/*set TotalTime field*/
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	
	/*get HashMap<QuestionInExam, Integer> of QuestionScores with key: questionID*/
	public HashMap<QuestionInExam, Integer> getQuestionsAndAnswers() {
		return questionsAndAnswers;
	}
	
	/*get HashMap<QuestionInExam, Integer> of QuestionScores with key: questionID*/
	public void setQuestionsAndAnswers(HashMap<QuestionInExam, Integer> questionsAndAnswers) {
		this.questionsAndAnswers = questionsAndAnswers;
	}
	
	/*get ActiveExam object to field*/
	public ActiveExam getActiveExam() {
		return activeExam;
	}
	
	/*set ActiveExam object to field*/
	public void setActiveExam(ActiveExam exam) {
		this.activeExam = exam;
	}
	
	/*get Student object to field*/
	public Student getStudent() {
		return student;
	}
	
	/*set Student object to field*/
	public void setStudent(Student student) {
		this.student = student;
	}
	
	/*set Score field*/
	public void setScore(int score) {
		this.score = score;
	}
	
	/*get Score field*/
	public int getScore() {
		return score;
	}
	
	/*get getReasonOfSubmit filed in enum class to differentiate between Reasons for submission for exam documentation*/
	public ReasonOfSubmit getReasonOfSubmit() {
		return reasonOfSubmit;
	}
	
	/*set getReasonOfSubmit filed in enum class to differentiate between Reasons for submission for exam documentation*/
	public void setReasonOfSubmit(ReasonOfSubmit reasonOfSubmit) {
		this.reasonOfSubmit = reasonOfSubmit;
	}
	
	/*get ExamType field*/
	public String getExamType() {
		return examType;
	}
	
	/*set ExamType field*/
	public void setExamType(String examType) {
		this.examType = examType;
	}

}
