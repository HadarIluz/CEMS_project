package entity;

import java.io.Serializable;

/**
 * The department maintains data for approval and updating of a student's exam
 * score. The department was not included in our diagrams and was added to save:
 * the unique number of the exam, the student's ID, the student's exam score on
 * the exam, and the reason for the update if updated.
 * 
 * @author Matar Asaf
 *
 */
@SuppressWarnings("serial")
public class UpdateScoreRequest implements Serializable {

	String ExamID;
	String StudentID;
	int updatedScore;
	String ReasonOfUpdate;

	/* get ExamID filed */
	public String getExamID() {
		return ExamID;
	}

	/* set ExamID filed */
	public void setExamID(String examID) {
		ExamID = examID;
	}

	/* get StudentID filed */
	public String getStudentID() {
		return StudentID;
	}

	/* set StudentID filed */
	public void setStudentID(String studentID) {
		StudentID = studentID;
	}

	/* get UpdatedScore filed */
	public int getUpdatedScore() {
		return updatedScore;
	}

	/* set UpdatedScore field */
	public void setUpdatedScore(int updatedScore) {
		this.updatedScore = updatedScore;
	}

	/* get ReasonOfUpdate field */
	public String getReasonOfUpdate() {
		return ReasonOfUpdate;
	}

	/* set ReasonOfUpdate field */
	public void setReasonOfUpdate(String reasonOfUpdate) {
		ReasonOfUpdate = reasonOfUpdate;
	}

}
