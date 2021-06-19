package gui_teacher;

import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.ActiveExam;
import entity.Exam;
import entity.ExamStatus;
import entity.Teacher;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import logic.RequestToServer;

/**
 * The class contains the functionality for creating a new active exam that
 * exists within the exam bank. The teacher switches to this screen after
 * clicking Create an active test and selecting the test as inactive. The
 * controller displays details of the selected test and allows to select an
 * hour, checks for no conflicts and adds the test as active in the system.
 * 
 * @author Hadar Iluz
 *
 */
public class CreateActiveExamController extends GuiCommon implements Initializable {

	@FXML
	private Button btnSaveActiveExam;

	@FXML
	private TextField textExamCode;

	@FXML
	private ComboBox<String> selectTime;

	@FXML
	private ImageView imgClock;

	@FXML
	private TextField textExamID;

	@FXML
	private TextField textCourse;

	@FXML
	private TextField textProfession;
	
    @FXML
    private Label examType;

	private static Exam exam;
	private String[] startTimeArr = { "08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00",
			"11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00",
			"15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00" };

	private Time selectedTime;

	/**
	 * @param event that occurs when the teacher press on create active exam button
	 */
	@FXML
	void btnSaveActiveExam(ActionEvent event) {
		String examCode = textExamCode.getText().trim();

		if (checkConditionToSaveActiveExam(examCode)) {

			exam.setAuthor((Teacher) ClientUI.loggedInUser.getUser());
			exam.setExamStatus(ExamStatus.active);
			String activeExamType= exam.getActiveExamType();
			ActiveExam newActiveExam = new ActiveExam(selectedTime, exam, examCode, activeExamType,
					exam.getTimeOfExam());

			// Request from server to insert new active exam into DB.
			// Request from server to update status filed for this exam: [ENUM('active')].
			RequestToServer reqCreateExam = new RequestToServer("createNewActiveExam");
			reqCreateExam.setRequestData(newActiveExam);
			ClientUI.cems.accept(reqCreateExam);

			if (CEMSClient.responseFromServer.getStatusMsg().getStatus().equals("NEW ACTIVE EXAM CREATED")) {
				popUp("New active exam has been successfully created in the system.");
				displayNextScreen((Teacher) ClientUI.loggedInUser.getUser(), "ExamBank.fxml");
			}
		}

	}

	/**
	 * @param examCode
	 * @return Returns true if all editable and selectable details are correct.
	 *         Otherwise, displays a message and returns a false.
	 */
	private boolean checkConditionToSaveActiveExam(String examCode) {

		boolean flag = true;
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("Error:\n");
		if (examCode.length() != 4 || textExamCode.getText().isEmpty()) {
			strBuilder.append("Exam code must include 4 characters and digits.\n");
			flag = false;
		}
		if (examCode.matches("[a-zA-Z]+") || examCode.matches("[0-9]+")) {
			strBuilder.append("Exam code must include letters and digits.\n");
			flag = false;
		}
		if (selectedTime == null) {
			strBuilder.append("Please choose start time for this exam.\n");
			flag = false;
		}

		if (!flag) {
			popUp(strBuilder.toString());
		}
		return flag;
	}


	/**
	 * Receive the selected Exam from previous screen.
	 * 
	 * @param selectedExam the exam we selected
	 */
	public static void setActiveExamState(Exam selectedExam) {
		exam = selectedExam;
	}

	/**
	 * The function initializes the relevant fields at the moment the screen loads.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedTime = null;
		textExamID.setText(exam.getExamID());
		textCourse.setText(exam.getCourse().getCourseID());
		textProfession.setText(exam.getProfession().getProfessionID());
		examType.setText(exam.getActiveExamType()+"");
		selectTime.setItems(FXCollections.observableArrayList(startTimeArr)); // load time to combo Box.

	}

	/**
	 * @param event that occurs when the teacher chooses start time from combo box.
	 */
	@FXML
	void selectTime(ActionEvent event) {
		selectedTime = java.sql.Time.valueOf(selectTime.getValue());
	}

}
