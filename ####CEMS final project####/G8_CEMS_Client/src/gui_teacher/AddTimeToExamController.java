package gui_teacher;

import client.CEMSClient;
import client.ClientUI;
import entity.ActiveExam;
import entity.Exam;
import entity.ExtensionRequest;
import gui_cems.GuiCommon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.RequestToServer;

/**
 * This class includes a function where a teacher submits to the principal a
 * request for extra time for an exam. the request is added to the table of
 * extension requests in the database and is awaiting a response from the principal as to
 * whether the request was received or denied.
 *
 * @author Matar Asaf
 *
 */

public class AddTimeToExamController extends GuiCommon {

	@FXML
	private Button btnSubmitTimeExtentionRequest;

	@FXML
	private Label lblNoteInMinutes;

	@FXML
	private TextField textAdditionalTime;

	@FXML
	private TextField textReqReason;

	@FXML
	private TextField textExamCode;

	@FXML
	private TextField textExamID;

	public static ActiveExam activeExam;
	public static Exam exam;

	/**
	 * The method checks that all the conditions for submit extension request have
	 * been approved and verifies the details entered by the teacher. Verifies that
	 * there is an active exam with the details of the test that the teacher has
	 * entered and checks that there is no request that has not yet been answered
	 * for this test.
	 * 
	 * @param event that occurs when clicking on 'Submit' button
	 */
	@FXML
	void btnSubmitTimeExtentionRequest(ActionEvent event) {
		String examID = textExamID.getText();
		String examCode = textExamCode.getText();
		String additionalTime = textAdditionalTime.getText();
		String reqReason = textReqReason.getText();
		ActiveExam activeExamInSystem;

		// Check that all fields that must be filled are filled.
		if (textExamID.getText().trim().isEmpty()) {
			popUp("Please fill the ExamID Field");
		} else if (textExamCode.getText().trim().isEmpty()) {
			popUp("Please fill the Exam Code Field");
		} else if (textAdditionalTime.getText().trim().isEmpty()) {
			popUp("Please fill the Additional time Field");
		} else if (textReqReason.getText().trim().isEmpty()) {
			popUp("Please fill the Request reason Field");
		}
		// in case fields not empty checks if exist activeExam with this details in DB
		else {
			if (examID.length() == 6 && isOnlyDigits(examID) && examCode.length() == 4
					&& isOnlyDigits(additionalTime)) {
				exam = new Exam(examID);
				activeExam = new ActiveExam(exam);
				// set in 'Serializable' class my request from server.
				RequestToServer req = new RequestToServer("addTimeToExam");
				req.setRequestData(activeExam);
				ClientUI.cems.accept(req); // sent server pk(exam) to DB in order to checks if activeExam exist or not.
				// activeExam does not exist in cems system.
				if (CEMSClient.responseFromServer.getStatusMsg().getStatus().equals("ACTIVE EXAM NOT FOUND")) {
					System.out.println("press on submit button and server returns: -->ACTIVE EXAM NOT FOUND");
					popUp("This activeExam doesn`t exist in CEMS system.");
				}
				// handle case that activeExam found and checks examCode.
				else {
					activeExamInSystem = (ActiveExam) CEMSClient.responseFromServer.getResponseData();
					// the exam code entered does not match the set exam code
					if (examCode.equals(activeExamInSystem.getExamCode()) == false)
						popUp("The examCode insert is incorrect. Please try again.");
					// the exam code entered correctly.
					else if (Integer.parseInt(additionalTime) <= 0)
						// When additional time is not a positive number.
						popUp("The additional time must be positive.");
					else {
						ExtensionRequest newExtensionReq = new ExtensionRequest(activeExam, additionalTime, reqReason);
						RequestToServer extReq = new RequestToServer("createNewExtensionRequest");
						extReq.setRequestData(newExtensionReq);
						ClientUI.cems.accept(extReq);
						textAdditionalTime.clear();
						textReqReason.clear();
						textExamCode.clear();
						textExamID.clear();
						// When there is already a request for a test that has not yet been answered by
						// the principal then the new request is not sent to the principal.
						if (CEMSClient.responseFromServer.getStatusMsg().getStatus()
								.equals("EXTENSION REQUEST DIDN'T CREATED")) {
							System.out.println(
									"press on submit button and server returns: -->EXTENSION REQUEST DIDN'T CREATED");
							popUp("There is an open request to this exam,\ncan't open another rquest untill principal close current request.");

						} else { // the extension sent to the principal.
							popUp("The request to add time for the exam was sent to the principal.\nPlease wait for approval.");
						}
					}
				}
			} else
				popUp("At least one of the fields was not entered as required.\nExam ID number: 6 digits\nExam code number: 4 digits or letters\nExtra time for the exam: A positive number");
		}
	}

}
