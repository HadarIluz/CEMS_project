package entity;

import java.io.Serializable;

/**
 * The department and its fields were included in the diagrams. The class is
 * used to store data for information about a question created in the system.
 * Additional fields of: correctAns, StdAns have been added to create a
 * statistical report
 * 
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class Question implements Serializable {

	private String questionID;
	private String question;
	private String description;
	private String[] answers;
	private int correctAnswerIndex;
	private Profession profession;
	private Teacher teacher;
	private String correctAns, StdAns;

	/* constructor */
	public Question() {
		this.questionID = "";
	}

	/* constructor */
	public Question(String questionID) {
		this.questionID = questionID;
	}

	/* constructor */
	public Question(String questionID, String question, String description, String[] answers, int correctAnswerIndex,
			Profession profession, Teacher teacher) {
		super();
		this.questionID = questionID;
		this.question = question;
		this.description = description;
		this.answers = answers;
		this.correctAnswerIndex = correctAnswerIndex;
		this.profession = profession;
		this.teacher = teacher;
	}

	/* constructor */
	public Question(String questionID, String question, String[] answers, int correctAnswerIndex, String description) {
		super();
		this.questionID = questionID;
		this.question = question;
		this.description = description;
		this.answers = answers;
		this.correctAnswerIndex = correctAnswerIndex;
	}

	/* get CorrectAns field */
	public String getCorrectAns() {
		return correctAns;
	}

	/* set CorrectAns field */
	public void setCorrectAns(String correctAns) {
		this.correctAns = correctAns;
	}

	/* get StdAns field */
	public String getStdAns() {
		return StdAns;
	}

	/* set StdAns field */
	public void setStdAns(String stdAns) {
		StdAns = stdAns;
	}

	/* set QuestionID field */
	public String getQuestionID() {
		return questionID;
	}

	/* get QuestionID field */
	public String getQuestion() {
		return question;
	}

	/* set Question field */
	public void setQuestion(String question) {
		this.question = question;
	}

	/* get Description field */
	public String getDescription() {
		return description;
	}

	/* set Description field */
	public void setDescription(String description) {
		this.description = description;
	}

	/* get array list of Answers */
	public String[] getAnswers() {
		return answers;
	}

	/* set array list of Answers */
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	/* get CorrectAnswerIndex field */
	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	/* set CorrectAnswerIndex field */
	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}

	/* get Profession object to field */
	public Profession getProfession() {
		return profession;
	}

	/* set Profession object to field */
	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	/* set QuestionID field */
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	/* get Teacher object to field */
	public Teacher getTeacher() {
		return teacher;
	}

	/* set Teacher object to field */
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
