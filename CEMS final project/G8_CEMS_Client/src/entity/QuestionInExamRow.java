package entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The class contains text fields of values that are set into the table
 * dynamically. The values can be updated in real time.
 * The class assists in updating the addition of questions from one table to
 * another and updating the score field for a question.
 *
 * @author Yuval Hayam
 *
 */
public class QuestionInExamRow {

	private final StringProperty questionID = new SimpleStringProperty();
	private final IntegerProperty score = new SimpleIntegerProperty();
	private final StringProperty question = new SimpleStringProperty();
	private QuestionInExam questionObject;

	/* constructor */
	public QuestionInExamRow(String questionID, Integer score, String question, QuestionInExam questionObj) {
		this.questionID.set(questionID);
		this.question.set(question);
		this.score.set(score);
		questionObject = questionObj;
	}

	public StringProperty questionIDProperty() {
		return questionID;
	}

	public final String getQuestionID() {
		return questionID.get();
	}

	public final void setQuestionID(String id) {
		questionID.set(id);
	}

	public StringProperty questionProperty() {
		return question;
	}

	public final String getQuestion() {
		return question.get();
	}

	public final void setQuestion(String q) {
		question.set(q);
	}

	public IntegerProperty scoreProperty() {
		return score;
	}

	public final Integer getScore() {
		return score.get();
	}

	public final void setScore(Integer s) {
		score.set(s);
		questionObject.setScore(score.intValue());
	}

	public QuestionInExam getQuestionObject() {
		return questionObject;
	}

	public void setQuestionObject(QuestionInExam questionObject) {
		this.questionObject = questionObject;
	}

}
