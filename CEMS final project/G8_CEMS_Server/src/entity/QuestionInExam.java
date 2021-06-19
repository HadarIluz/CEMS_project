package entity;

import java.io.Serializable;

/**
 * The class and its fields were included in the diagrams. 
 * The class used to store data for information on a question that belongs to a specific exam. 
 * It maintains fields of scoring of the question in this exam, exam`s
 * details and question`s details.
 * 
 * @author Yuval Hayam
 * @author lior karish
 *
 */
@SuppressWarnings("serial")
public class QuestionInExam implements Serializable {

	private int score;
	private Question question;
	private Exam exam;
	
	private String questionID;
	private String questionDescription;

	/* constructor */
	public QuestionInExam(int score, Question question, Exam exam) {
		super();
		this.score = score;
		this.question = question;
		this.exam = exam;
	}
	/* constructor */
	public QuestionInExam(int score, Question question) {
		super();
		this.score = score;
		this.question = question;
	}
	/*get score field*/
	public int getScore() {
		return score;
	}
	/*set score field*/
	public void setScore(int score) {
		this.score = score;
	}
	/*get Question object to field*/
	public Question getQuestion() {
		return question;
	}
	/*set Question object to field*/
	public void setQuestion(Question question) {
		this.question = question;
	}
	/*get Exam object to field*/
	public Exam getExam() {
		return exam;
	}
	/*set Exam object to field*/
	public void setExam(Exam exam) {
		this.exam = exam;
	}
	/*---Getters and setters for display data in table view---*/
	/*get QuestionDescription */
	public String getQuestionDescription() {
		return questionDescription;
	}
	/*set QuestionDescription */
	public void setQuestionDescription() {
		questionDescription = question.getQuestion();
	}
	/*get QuestionID*/
	public String getQuestionID() {
		return questionID;
	}
	/*set QuestionID*/
	public void setQuestionID() {
		questionID=question.getQuestionID();
	}
	
}
