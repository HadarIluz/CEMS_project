package gui_student;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.ActiveExam;
import entity.ExamOfStudent;
import entity.Student;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import logic.RequestToServer;

/**
 * The class included in the diagrams and contains functionality that supports
 * entry to the test, when the student wants to start a test he is required to
 * confirm conditions and can enter a test created as an active test within half
 * an hour from the start of the test. The department makes sure that all the
 * parameters entered are correct for the student connected to the system and
 * assigns the test he solves to the student.
 * 
 * @author Hadar Iluz
 *
 */
public class EnterToExamController extends GuiCommon implements Initializable {

	@FXML
	private Button btnStart;

	@FXML
	private TextField textExamCode;

	@FXML
	private TextField textStudentID;

	@FXML
	private CheckBox ApprovalInsrtuctions;

	@FXML
	private CheckBox CommitPreformByMyself;

	@FXML
	private Label textConfirm;

	@FXML
	private ComboBox<String> selectActiveExamFromCB;

	private static StudentController studentController;
	private static HashMap<String, ActiveExam> activeExamtMap = new HashMap<String, ActiveExam>();
	private static ArrayList<ActiveExam> activeExamtList = new ArrayList<ActiveExam>();
	private ArrayList<String> examIdList = new ArrayList<String>();
	private ActiveExam activeExam_selection;
	private Student student;

	/**
	 * The method checks that all the conditions for starting the exam have been
	 * approved and verifies the details entered by the student. Verifies that there
	 * is an existing exam right now and moves the student to the test solution
	 * screen according to the exam`s type.
	 * 
	 * @param event that occurs when clicking on 'start exam' button.
	 */
	@SuppressWarnings("static-access")
	@FXML
	void btnStart(ActionEvent event) {
		String examCode = textExamCode.getText().trim();
		String studentID = textStudentID.getText().trim();

		boolean condition = checkConditionToStart(examCode, studentID);
		if (condition) {

			/*
			 * Gets current time that student try to insert into his active exam The student
			 * has a half-hour range in which he can enter and begin construction from the
			 * exam`s start time.
			 */
			long now = System.currentTimeMillis();
			Time sqlEndRangeTimeToTakeExam = new Time(now);
			// now add half an hour, 1 800 000 milliseconds = 30 minutes
			long halfAnHourBefore = (long) now - 1800000;
			Time sqlTime = new Time(halfAnHourBefore);
			System.out.println(sqlTime);
			System.out.println(sqlEndRangeTimeToTakeExam);

			ActiveExam activeExam = new ActiveExam(sqlTime, sqlEndRangeTimeToTakeExam, examCode);
			// Request to server to return an data, examID for this examCode if exist.
			// if not return null.
			RequestToServer req = new RequestToServer("isActiveExamExist");
			req.setRequestData(activeExam);
			ClientUI.cems.accept(req);

			// 'main if'
			if ((CEMSClient.responseFromServer.getResponseType()).equals("ACTIVE EXAM EXIST")) {
				// At this point we found exam so we can be sure an object has arrived in this
				// response.
				activeExam = (ActiveExam) CEMSClient.responseFromServer.getResponseData();
				String existExamID = activeExam.getExam().getExamID();
				String ActiveExamType = activeExam.getActiveExamType();
				// message in console
				System.out.println("Respont: there is active examID: " + existExamID + " type: " + ActiveExamType);

				// ---Request server to check if this test is already associated to this
				// student.-----//
				boolean allowedToStartExam = checkExam_of_student_NotExists(activeExam, student);
				if (allowedToStartExam) {

					// -------Request from server to insert new row to student of exam.--------//
					RequestToServer reqStusentInExam = new RequestToServer("InsertExamOfStudent");
					ExamOfStudent examOfStudent = new ExamOfStudent(activeExam, student);
					reqStusentInExam.setRequestData(examOfStudent);
					ClientUI.cems.accept(reqStusentInExam);

					// The student has entered all the given details and transfer to exam screen
					// - computerized or manual
					switch (ActiveExamType) {
					case "manual": {
						// load manual start exam fxml
						try {
							StartManualExamController.setActiveExamState(activeExam);
							Pane newPaneRight = FXMLLoader.load(getClass().getResource("StartManualExam.fxml"));
							newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
							studentController.root.add(newPaneRight, 1, 0);
						} catch (IOException e) {
							System.out.println("Couldn't load!");
							e.printStackTrace();
						}
					}
						break;

					case "computerized": {
						// load computerized start exam fxml
						try {
							SolveExamController.setActiveExamState(activeExam);
							Pane newPaneRight = FXMLLoader.load(getClass().getResource("SolveExam.fxml"));
							newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
							studentController.root.add(newPaneRight, 1, 0);
						} catch (IOException e) {
							System.out.println("Couldn't load!");
							e.printStackTrace();
						}
					}
						break;
					}
				}

			} // End 'main if'.

			else {
				popUp("There is no active exam for the exam you selected.");
			}

		}
	}

	/**
	 * Checks whether the student has filled all the required fields, if not display
	 * popUp message.
	 * 
	 * @param examCode
	 * @param studentID
	 * @return Returns true if all fields are filled currently, otherwise returns
	 *         false.
	 */
	private boolean checkConditionToStart(String examCode, String studentID) {
		boolean approve1 = ApprovalInsrtuctions.isSelected();
		boolean approve2 = CommitPreformByMyself.isSelected();
		textConfirm.setVisible(false);
		boolean flag = true;

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Error:\n ");
		if (examCode.length() == 0 || studentID.length() == 0 || examCode.length() != 4) {
			strBuilder.append("One or more of the parameters which insert are incorrect.\n");
			flag = false;
		}
		if (examCode.matches("[a-zA-Z]+") || examCode.matches("[0-9]+")) {
			strBuilder.append("Exam code must include letters and digits.\n");
			flag = false;
		}
		if (!approve1 || !approve2) {
			textConfirm.setVisible(true);
			strBuilder.append("You need to confirm all condition.\n");
			flag = false;
		}
		if (activeExam_selection == null) {
			strBuilder.append("Exam must be selected, Please choose your exam.\n");
			flag = false;
		}
		if (studentID.equals(String.valueOf(student.getId())) == false && isOnlyDigits(studentID)) {
			strBuilder.append("\nThe ID you entered does not match your profile information.\n"
					+ "You can not take this exam. try again\n");
			flag = false;
		}
		if (!flag) {
			popUp(strBuilder.toString());
		}

		return flag;
	}

	/**
	 * The method verifies that there is an active exam in the system that the
	 * student can begin to solve. Prevents entry to any test for which the student
	 * is not registered. The test is performed by a corrupt request to the server
	 * that returns an answer whether it is possible or not.
	 * 
	 * @param activeExam that the student want to start and solve.
	 * @param student input indicates the identifying details of the student.
	 * @return True if the student may begin to solve the exam.
	 */
	private boolean checkExam_of_student_NotExists(ActiveExam activeExam, Student student) {
		ExamOfStudent examOfStudent = new ExamOfStudent(activeExam, student);
		RequestToServer req = new RequestToServer("checkExam_of_student_NotExistsBeforeStartExam");
		req.setRequestData(examOfStudent);
		ClientUI.cems.accept(req);

		if ((CEMSClient.responseFromServer.getResponseType()).equals("exam_of_student_allowed")) {
			return true;
		}
		popUp("Error:\nThis exam already has been taken by student id: " + student.getId());
		return false;
	}

	/**
	 * initialize function to prepare the screen after it is loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		student = (Student) ClientUI.loggedInUser.getUser();
		textConfirm.setVisible(false);
		activeExam_selection = null;
		// implement load active exam into Combo box.
		loadActiveExamToCombobox();

	}

	/**
	 * The method prepares the list of tests and loads them into the comboBox.
	 */
	private void loadActiveExamToCombobox() {
		setActiveExamtMap(activeExamtList);
		for (ActiveExam ae : activeExamtList) {
			examIdList.add(ae.getExam().getExamID());
		}
		selectActiveExamFromCB.setItems(FXCollections.observableArrayList(examIdList));
		selectActiveExamFromCB.setDisable(false);
	}

	/**
	 * @param activeExamtList containing all the active tests in the student's exam system.
	 */
	public static void setActiveExamtMap(ArrayList<ActiveExam> activeExamtList) {
		for (ActiveExam ae : activeExamtList) {
			activeExamtMap.put(ae.getExam().getExamID(), ae);
		}
	}

	/**
	 * @param event that occurs when a student selects a test from the comboBox.
	 */
	@FXML
	void selectActiveExam(ActionEvent event) {
		if (activeExamtMap.containsKey(selectActiveExamFromCB.getValue())) {
			activeExam_selection = activeExamtMap.get(selectActiveExamFromCB.getValue());
		}

	}

	/**
	 * Receive the list of active tests from the previous screen.
	 * @param activeExamListFromDB the exams to save
	 */
	public static void setAllActiveExamBeforEnterToExam(ArrayList<ActiveExam> activeExamListFromDB) {
		activeExamtList = activeExamListFromDB;
	}

}
