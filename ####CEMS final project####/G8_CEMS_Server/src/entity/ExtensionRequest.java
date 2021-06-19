package entity;

import java.io.Serializable;

/**
 * The class contains fields that includes details about a request for extra
 * time of an active test sent from the teacher to the principal.
 * 
 * @author Matar Asaf
 *
 */

@SuppressWarnings("serial")
//Entity class - define Extension Request in the CEMS system.
public class ExtensionRequest implements Serializable {
	private ActiveExam activeExam;
	private String additionalTime;
	private String reqReason;

	/* constructor */
	public ExtensionRequest(ActiveExam activeExam, String additionalTime, String reqReason) {
		this.activeExam = activeExam;
		this.additionalTime = additionalTime;
		this.reqReason = reqReason;
	}

	/* constructor */
	public ExtensionRequest(ActiveExam activeExam) {
		this.activeExam = activeExam;
	}

	/* get ActiveExam object to field */
	public ActiveExam getActiveExam() {
		return activeExam;
	}

	/* set ActiveExam field */
	public void setActiveExam(ActiveExam activeExam) {
		this.activeExam = activeExam;
	}

	/* get AdditionalTime field */
	public String getAdditionalTime() {
		return additionalTime;
	}

	/* set AdditionalTime to field */
	public void setAdditionalTime(String additionalTime) {
		this.additionalTime = additionalTime;
	}

	/* set Reason field */
	public void setReason(String reqReason) {
		this.reqReason = reqReason;
	}

	/* get Reason field */
	public String getReason() {
		return reqReason;
	}

}
