package gui_teacher;

import java.util.ArrayList;
import java.util.HashMap;

import client.CEMSClient;
import client.ClientUI;
import entity.UpdateScoreRequest;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import logic.RequestToServer;

/**
 * The class contains the functionality for certifying scores of a exam
 * completed in the system. The system displays for each student his or her own
 * command and allows the teacher to make a change. If a change has been made in
 * the order, the system requires writing a reason for the change and only then
 * saves the new score in the system.
 * 
 * @author Yadin Amsalem
 * @author Nadav Dery
 *
 */
public class ScoreApprovalController extends GuiCommon {

	@FXML
	private ComboBox<String> selectStudent;

	@FXML
	private TextField textCurrentGrade;

	@FXML
	private TextArea textGradeChangeReason;
	@FXML
	private TextField textnewGradeField;

	@FXML
	private Button btnUpdate;

	@FXML
	private TextField textExamID;

	@FXML
	private Button btnViewStudentExam;

	@FXML
	private Label textNewGradeReqField;

	@FXML
	private Button btnShow;

	@FXML
	private Label txtExamIdError;

	@FXML
	private Label lblCheat;

	HashMap<String, Integer> stdScore = new HashMap<>();

	// <StudentID,Score>
	/**
	 * method check if score insert is legal
	 * 
	 * @param score the input of User for update score
	 * @return true if legal, else false
	 */
	public boolean isLegalScore(String score) {

		for (int i = 0; i < score.length(); i++) {
			if (!Character.isDigit(score.charAt(i))) {
				textNewGradeReqField.setTextFill(Color.RED);
				textNewGradeReqField.setText("Score Must Contains only digits.");
				textNewGradeReqField.setVisible(true);
				return false;
			}
		}
		int Score = Integer.parseInt(score);

		if (Score < 0 || Score > 100) {
			textNewGradeReqField.setTextFill(Color.RED);
			textNewGradeReqField.setText("Score must be between 0 to 100.");
			textNewGradeReqField.setVisible(true);
			return false;
		}
		return true;
	}

	/**
	 * method Update score changing and reason for change
	 * @param event occurs when pressing "Update"
	 */

	@FXML
	void btnUpdate(ActionEvent event) {

		textNewGradeReqField.setVisible(false);

		String changeReason = textGradeChangeReason.getText();
		String newGrade = textnewGradeField.getText();
		// check if student selected

		if (selectStudent.getSelectionModel().getSelectedItem() == null) {
			textNewGradeReqField.setTextFill(Color.RED);
			textNewGradeReqField.setText("Must choose student to update");
			textNewGradeReqField.setVisible(true);
			return;
		}
		// check if fields are empty
		if (changeReason.isEmpty() || newGrade.trim().isEmpty()) {
			textNewGradeReqField.setTextFill(Color.RED);
			textNewGradeReqField.setText("Must add grade and reason to update");
			textNewGradeReqField.setVisible(true);
			return;
		} else {
			if (!isLegalScore(newGrade))
				return;

			RequestToServer req = new RequestToServer("Update Grade");
			UpdateScoreRequest upReq = new UpdateScoreRequest();
			upReq.setExamID(textExamID.getText());
			upReq.setReasonOfUpdate(changeReason);
			upReq.setStudentID(selectStudent.getSelectionModel().getSelectedItem());
			upReq.setUpdatedScore(Integer.parseInt(newGrade));
			req.setRequestData(upReq);
			ClientUI.cems.accept(req);
			if (CEMSClient.responseFromServer.getResponseType().equals("FALSE")) {
				popUp("Update Grade has failed.");
				return;
			}
			textNewGradeReqField.setText("Grade Updated Successfuly");
			textNewGradeReqField.setTextFill(Color.GREEN);
			textNewGradeReqField.setVisible(true);

		}
	}

	/**
	 * select student from combobox
	 * @param event occurs when choosing sudent from combo box
	 */

	@FXML
	void selectStudent(ActionEvent event) {
		textCurrentGrade.setText(String.valueOf(stdScore.get(selectStudent.getSelectionModel().getSelectedItem())));
		CheatingDetection(selectStudent.getSelectionModel().getSelectedItem());
	}

	private void CheatingDetection(String selectedStudentID) {
		

	}
/**
 * method present all student that take the exam by exam id that teacher enter
 * @param event occurs when user press "Show"
 */
	@SuppressWarnings("unchecked")
	@FXML
	void ShowStudentByExamID(ActionEvent event) {
		textNewGradeReqField.setVisible(false);
		textnewGradeField.setText("");
		textGradeChangeReason.setText("");
		String ExamID = textExamID.getText();

		if (!checkForLegalID(ExamID))
			return;
		RequestToServer req = new RequestToServer("getAllExams");
		ClientUI.cems.accept(req);
		ArrayList<String> exams = (ArrayList<String>) CEMSClient.responseFromServer.getResponseData();
		boolean examexist = false;
		for (String curr : exams)
			if (curr.equals(ExamID)) {
				examexist = true;
			}
		if (examexist == false) {
			popUp("Exam Doesn't Exist, Try Again.");
			return;
		}

		RequestToServer req2 = new RequestToServer("getStudentsByExamID");
		req2.setRequestData(ExamID);
		ClientUI.cems.accept(req2);
		stdScore = (HashMap<String, Integer>) CEMSClient.responseFromServer.getResponseData();
		if (stdScore.isEmpty()) {
			popUp("This exam does not have any students who solved it.");
		}
		selectStudent.setDisable(false);
		selectStudent.setItems(FXCollections.observableArrayList(stdScore.keySet()));

	}

}
