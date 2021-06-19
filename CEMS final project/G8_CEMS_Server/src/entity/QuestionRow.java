package entity;

import java.io.Serializable;

/**
 * The class contains text fields of values that are entered into various tables
 * in the project such as the question table of a test presented for 
 * editing / a table of all the questions that exist in the system for viewing by the
 * principal.
 * 
 * @author Nadav
 * @author Yadin
 *
 */
@SuppressWarnings("serial")
public class QuestionRow implements Serializable {

	private String QuestionID;
	private String profession;
	private String Question;
	private int studentAnswer;
	private int correct;
	
	/*get StudentAnswer field*/
	public int getStudentAnswer() {
		return studentAnswer;
	}
	/*set StudentAnswer field*/
	public void setStudentAnswer(int studentAnswer) {
		this.studentAnswer = studentAnswer;
	}
	/*get Correct field*/
	public int getCorrect() {
		return correct;
	}
	/*set Correct field*/
	public void setCorrect(int correct) {
		this.correct = correct;
	}
	/*get QuestionID field*/
	public String getQuestionID() {
		return QuestionID;
	}
	/*set QuestionID field*/
	public void setQuestionID(String QuestionID) {
		this.QuestionID = QuestionID;
	}
	public QuestionRow() {
	}
	/*get Profession field*/
	public String getProfession() {
		return profession;
	}
	/*set Profession field*/
	public void setProfession(String profession) {
		this.profession = profession;
	}
	/*get Question field*/
	public String getQuestion() {
		return Question;
	}
	/*set Question field*/
	public void setQuestion(String question) {
		Question = question;
	}

}
