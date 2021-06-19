package gui_teacher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.Exam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import logic.RequestToServer;

/**
 * The class contains functionality for step 3 of creating a test, the teacher
 * must confirm that she wants to create a new exam and then she is presented
 * with the examID of the new test she has created.
 * 
 * @author Yuval Hayam
 *
 */
public class CreateNewExam_step3Controller implements Initializable {

	@FXML
	private Text textMsg_newExam;

	@FXML
	private Button btnCreateNewExam;

	@FXML
	private ImageView imgStep1;

	@FXML
	private ImageView imgStep2;

	@FXML
	private ImageView imgStep3;

	@FXML
	private Text textExamID;

	@FXML
	private Button btnBack;

	@FXML
	private ImageView imgComputer;

	private static Exam newExam;

	/**
	 * @param event handles click on button back
	 */
	@FXML
	void btnBack(ActionEvent event) {
		CreateExam_addQ_step2Controller.setExamState(newExam);
		try {

			Pane newPaneRight = FXMLLoader.load(getClass().getResource("CreateExam_addQ_step2.fxml"));
			newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			TeacherController.root.add(newPaneRight, 1, 0);
			CreateExam_addQ_step2Controller.setExamState(newExam);

		} catch (IOException e) {
			System.out.println("Couldn't load!");
			e.printStackTrace();
		}
	}

	/**
	 * @param event handles click on button btnCreateNewExam
	 */
	@FXML
	void btnCreateNewExam(ActionEvent event) {
		RequestToServer req = new RequestToServer("createNewExam");
		req.setRequestData(newExam);
		ClientUI.cems.accept(req);
		if (CEMSClient.responseFromServer.getResponseType().equals("Success Create New Exam")) {
			textExamID.setText((String) CEMSClient.responseFromServer.getResponseData());
			textMsg_newExam.setVisible(true);
			btnBack.setDisable(true);
			CreateExam_step1Controller.setExamState(null);
		} else {
			textExamID.setText("There was a problem. please try again later");
			textExamID.setFill(javafx.scene.paint.Color.RED);
		}

		btnCreateNewExam.setDisable(true);

	}

	/**
	 * @param newExamInProgress exam we want to set
	 * allows other screens to set the current new exam
	 */
	public static void setExamState(Exam newExamInProgress) {
		newExam = newExamInProgress;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textExamID.setText("");
		textMsg_newExam.setVisible(false);
		btnCreateNewExam.setDisable(false);

	}

}
