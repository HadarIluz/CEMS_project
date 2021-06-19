package entity;

import java.io.Serializable;
import java.sql.Time;

/**
 * Entity class define Active Exam in the CEMS system. 
 * The department included in our diagrams except for one field which is: extraTime, which used to
 * save value and add to the calculation of the time left to solve an active exam 
 * that student performs after receiving approval for extra time from the principal.
 * 
 * @author Hadar Iluz
 */
public class ActiveExam implements Serializable {
	private static final long serialVersionUID = 1L;

	private Exam exam;
	private String examCode;
	private int timeAllotedForTest;
	private String activeExamType; // {manual / computerized}
	private Time startTime;
	private Time endTimeToTakeExam;
	private int extraTime;

	/* constructor */
	public ActiveExam(Exam exam) {
		this.exam = exam;
	}

	/* constructor */
	public ActiveExam(Time startTime, Exam exam, String examCode) {
		this.startTime = startTime;
		this.exam = exam;
		this.examCode = examCode;
	}

	/* constructor */
	public ActiveExam(Time startTime, Exam exam, String examCode, String activeExamType, int timeAllotedForTest) {
		this.startTime = startTime;
		this.exam = exam;
		this.examCode = examCode;
		this.activeExamType = activeExamType;
		this.timeAllotedForTest = timeAllotedForTest;
	}

	/* constructor */
	public ActiveExam(Time time, Time endTimeToTakeExam, String examCode) {
		this.startTime = time;
		this.endTimeToTakeExam = endTimeToTakeExam;
		this.examCode = examCode;
	}

	/* constructor */
	public ActiveExam(Exam exam, Time startTime) {
		this.startTime = startTime;
		this.exam = exam;
	}

	/* get TimeAllotedForTest field */
	public int getTimeAllotedForTest() {
		return timeAllotedForTest;
	}

	/* set TimeAllotedForTest field and parser string to integer */
	public void setTimeAllotedForTest(String timeOfExam) {
		this.timeAllotedForTest = Integer.parseInt(timeOfExam);
	}

	/* get tartTime field */
	public Time getStartTime() {
		return startTime;
	}

	/* set startTime field */
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	/* get Exam object */
	public Exam getExam() {
		return exam;
	}

	/* set Exam object */
	public void setExam(Exam exam) {
		this.exam = exam;
	}

	/* get ExamCode field */
	public String getExamCode() {
		return examCode;
	}

	/* set ExamCode field */
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	/* set getActiveExamType filed */
	public String getActiveExamType() {
		return activeExamType;
	}

	/* set getActiveExamType filed */
	public void setActiveExamType(String activeExamType) {
		this.activeExamType = activeExamType;
	}

	/* get Time object of EndTimeToTakeExam filed */
	public Time getEndTimeToTakeExam() {
		return endTimeToTakeExam;
	}

	/* set Time object of EndTimeToTakeExam filed */
	public void setEndTimeToTakeExam(Time endTimeToTakeExam) {
		this.endTimeToTakeExam = endTimeToTakeExam;
	}

	/* get ExtraTime filed */
	public int getExtraTime() {
		return extraTime;
	}

	/* set ExtraTime filed */
	public void setExtraTime(int extraTime) {
		this.extraTime = extraTime;
	}

}
