package gui_teacher;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.Course;
import entity.Exam;
import entity.ExamStatus;
import entity.ProfessionCourseName;
import entity.Teacher;
import entity.User;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.RequestToServer;

/**
 * The class contains the functionality for various operations that can be
 * performed in the system in the context of exams that are in the database: 
 * -Conducting an existing test that is in inactive status. 
 * - Lock an active exam.
 * - Deleting an existing exam from the database. 
 * - Creating an active test in cems system. 
 * This screen combines the various operations and allows
 * you to centralize all the operations in a simple and convenient way for the user. 
 * The operations are not lost between the various screens.
 * 
 * In addition, the screen was used by us for the reuse of displaying all the
 * tests that are in the system for the principal, the identification is done by
 * the user who logs in to the system so that the screen is displayed to the
 * administrator without editing permissions, and contains all the tests. The
 * teacher can see all the exams she has created only.
 * 
 * @author Yadin Amsalem
 * @author Nadav Dery
 * @author Matar Asaf
 * @author Hadar Iluz
 * @version 1.0
 *
 */
public class ExamBankController extends GuiCommon implements Initializable {

	@FXML
	private Button btnEditExam;

	@FXML
	private Button btnDeleteExam;

	@FXML
	private TextField textExamID;

	@FXML
	private TableView<Exam> tableExam;

	@FXML
	private Button btnCreateNewExam;

	@FXML
	private Text textNavigation;

	@FXML
	private TableColumn<Exam, String> ExamID;

	@FXML
	private TableColumn<Exam, String> Proffesion;

	@FXML
	private TableColumn<Exam, Integer> Time;

	@FXML
	private TableColumn<Exam, ExamStatus> StatusC;

	@FXML
	private Button btnExamInfoPrincipal;

	@FXML
	private Button btnCreateActiveExam;

	@FXML
	private Button btnLockExam;

	@FXML
	private Text textMsg1;

	@FXML
	private Text textMsg2;

	private ObservableList<Exam> data;
	private static Teacher teacher;
	private static User principal;
	private boolean displayPrincipalView = false;
	private HashMap<String, String> profName;

	/**
	 * method selectExamFromTable return selected exam from combo box.
	 * 
	 * @param event occurs when User press On Exam from his Exam's Table
	 * 
	 */

	@FXML
	void selectExamFromTable(MouseEvent event) {
		ObservableList<Exam> Qlist;
		Qlist = tableExam.getSelectionModel().getSelectedItems();
		if (Qlist.isEmpty()) {
			return;
		}
		textExamID.setText(Qlist.get(0).getExamID());
		if (!displayPrincipalView) {
			if (Qlist.get(0).getExamStatus().equals(ExamStatus.active)) {
				btnLockExam.setDisable(false);
				btnDeleteExam.setDisable(true);
				btnCreateActiveExam.setDisable(true);
				btnEditExam.setDisable(true); // can't edit exam in status active
			} else {
				btnLockExam.setDisable(true);
				btnDeleteExam.setDisable(false);
				btnCreateActiveExam.setDisable(false);
				btnEditExam.setDisable(false);
			}
		} else {
			btnExamInfoPrincipal.setDisable(false);
		}
	}

	/**
	 * Method that search in data matching Exam ID , if found save Exam's details
	 * and return from method
	 * 
	 * @param ExamID use to compare and found match in data
	 * @return exam if found,else null
	 */
	private Exam GetTableDetails(String ExamID) {
		Exam exam;
		for (Exam e : data) {
			if (e.getExamID().equals(ExamID)) {
				exam = new Exam(ExamID);
				exam.setCourse(new Course(e.getCourse().getCourseID()));
				exam.setProfession(e.getProfession());
				exam.setActiveExamType(e.getActiveExamType());
				return exam;
			}
		}
		return null;
	}

	/**
	 * Method use to delete data of exam from the teacher's exam bank
	 * 
	 * @param event occurs when User press On Delete
	 */
	@FXML
	void btnDeleteExam(ActionEvent event) {

		if (!textExamID.getText().isEmpty()) {
			if (!checkForLegalID(textExamID.getText()))
				return;

			ObservableList<Exam> Qlist;

			Exam ExamToDelete = GetTableDetails(textExamID.getText());
			if (ExamToDelete == null) {
				popUp("Exam Id is not exist!");
				return;
			}
			Qlist = tableExam.getSelectionModel().getSelectedItems();
			RequestToServer req = new RequestToServer("DeleteExam");
			req.setRequestData(ExamToDelete);
			ClientUI.cems.accept(req);

			if (CEMSClient.responseFromServer.getResponseData().equals("FALSE"))
				System.out.println("failed to delete question");
			else
				data.removeAll(Qlist);
			initTableRows();
			textExamID.clear();
		}

	}

	/**
	 * Method use to edit data of exam from the teacher's exam bank
	 * 
	 * @param event occurs when User press On Edit
	 */

	@FXML
	void btnEditExam(ActionEvent event) {
		if (!textExamID.getText().isEmpty()) {
			Exam selectedExam = getExistExamDetails(textExamID.getText());
				EditExamController.setActiveExamState(selectedExam, super.teacherStatusScreen);
				displayNextScreen(teacher, "EditExam.fxml");
		}
	}

	/**
	 * Method open for user screen of create new exam
	 * 
	 * @param event occurs when User press On "Create New Exam"
	 * 
	 */

	@FXML
	void CreateNewExam(ActionEvent event) {
		textExamID.clear();
		CreateExam_step1Controller.setExamState(null);
		displayNextScreen(teacher, "CreateExam_step1.fxml");

	}

	/**
	 * Method initialize for user screen of Exam bank
	 * 
	 * @param location  for Url location
	 * @param resources of type ResourceBundle
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableExam.setEditable(false);
		RequestToServer req = new RequestToServer("getProfNames");
		ClientUI.cems.accept(req);
		profName = (HashMap<String, String>) CEMSClient.responseFromServer.getResponseData();

		if (ClientUI.loggedInUser.getUser() instanceof Teacher) {
			teacher = (Teacher) ClientUI.loggedInUser.getUser();
			initTableRows();
		}

		else if (ClientUI.loggedInUser.getUser() instanceof User) {
			// setUp before load screen.
			principal = (User) ClientUI.loggedInUser.getUser();
			btnCreateNewExam.setVisible(false);
			btnEditExam.setVisible(false);
			btnDeleteExam.setVisible(false);
			btnCreateActiveExam.setVisible(false);
			btnLockExam.setVisible(false);
			textMsg1.setVisible(false);
			textMsg2.setVisible(false);
			textNavigation.setVisible(true);
			btnExamInfoPrincipal.setVisible(true);
			btnExamInfoPrincipal.setDisable(true);
			displayPrincipalView = true;
			textNavigation.setVisible(true);
			fillTableForPrincipalALLexamsInSystem(); // set all exams in cems system into the table
		}
	}

	/**
	 * btnExamInfoPrincipal open screen of exam info of teacher with the chosen Exam
	 * ID
	 * 
	 * @param event occurs when User press On Edit
	 * 
	 */

	@FXML
	void btnExamInfoPrincipal(ActionEvent event) {
		if (!textExamID.getText().isEmpty()) {
			Exam selectedExam = getExistExamDetails(textExamID.getText());
			EditExamController.setActiveExamState(selectedExam, super.principalStatusScreen);
			displayNextScreen(principal, "/gui_teacher/EditExam.fxml");
		}

	}

	/**
	 * initTableRows get from server all exams of the logged teacher and insert into
	 * the table.
	 */

	@SuppressWarnings("unchecked")
	private void initTableRows() {
		RequestToServer req = new RequestToServer("getExams");
		req.setRequestData(teacher.getId());
		ArrayList<Exam> ExamsOfTeacher = new ArrayList<Exam>();
		ClientUI.cems.accept(req);
		ExamsOfTeacher = (ArrayList<Exam>) CEMSClient.responseFromServer.getResponseData();
		for (Exam curr : ExamsOfTeacher)
			curr.getProfession().setProfessionID(profName.get(curr.getExamID().substring(0, 2)));
		data = FXCollections.observableArrayList(ExamsOfTeacher);
		tableExam.getColumns().clear();
		ExamID.setCellValueFactory(new PropertyValueFactory<>("examID"));
		Proffesion.setCellValueFactory(new PropertyValueFactory<>("ProfessionName"));
		Time.setCellValueFactory(new PropertyValueFactory<>("timeOfExam"));
		StatusC.setCellValueFactory(new PropertyValueFactory<>("examStatus"));
		tableExam.setItems(data);
		tableExam.getColumns().addAll(ExamID, Proffesion, Time, StatusC);

	}

	/**
	 * btnCreateActiveExam open screen of exam info of teacher with the chosen
	 * ExamID. It is allowed to perform the same exam but NOT in the same time
	 * checks by req to server in CreateActiveExamController.
	 * 
	 * @param event occurs when User press On "Create Active Exam"
	 */

	@FXML
	void btnCreateActiveExam(ActionEvent event) {
		// can not create active exam for exam in status: active.
		Exam selectedExam = getExistExamDetails(textExamID.getText());
		if ((textExamID.getText().isEmpty())) {
			btnCreateActiveExam.setDisable(true);

		} else {
			CreateActiveExamController.setActiveExamState(selectedExam);
			displayNextScreen(teacher, "CreateActiveExam.fxml");
		}
	}

	/**
	 * method gets from server all detail of exams with the key exam ID
	 * 
	 * @param examID is the key for the specific exam
	 * @return exam with details of examId
	 */

	private Exam getExistExamDetails(String examID) {

		Exam selectedExam = new Exam(examID);
		RequestToServer req = new RequestToServer("getSelectedExamData_byID");
		req.setRequestData(selectedExam);
		ClientUI.cems.accept(req);
		return selectedExam = (Exam) CEMSClient.responseFromServer.getResponseData();
	}

	/**
	 * The function get from server all exams stored in the system and insert into
	 * the table.
	 */
	@SuppressWarnings("unchecked")
	private void fillTableForPrincipalALLexamsInSystem() {
		RequestToServer req = new RequestToServer("getAllExamsStoredInSystem");
		ArrayList<Exam> examsList = new ArrayList<Exam>();
		ClientUI.cems.accept(req);
		examsList = (ArrayList<Exam>) CEMSClient.responseFromServer.getResponseData();
		TableColumn<Exam, String> course = new TableColumn<>("Course");

		for (Exam curr : examsList)
			curr.getProfession().setProfessionID(profName.get(curr.getExamID().substring(0, 2)));
		RequestToServer req2 = new RequestToServer("getCoursesNames");
		HashMap<String, ProfessionCourseName> coursesNames;
		ClientUI.cems.accept(req2);
		coursesNames = (HashMap<String, ProfessionCourseName>) CEMSClient.responseFromServer.getResponseData();

		for (Exam curr : examsList)
			curr.getCourse().setCourseID(coursesNames.get(curr.getExamID().substring(0, 2)).getCourses()
					.get(curr.getExamID().substring(2, 4)));

		data = FXCollections.observableArrayList(examsList);
		tableExam.getColumns().clear();
		ExamID.setCellValueFactory(new PropertyValueFactory<>("examID"));
		Proffesion.setCellValueFactory(new PropertyValueFactory<>("ProfessionName"));
		Time.setCellValueFactory(new PropertyValueFactory<>("timeOfExam"));
		course.setCellValueFactory(new PropertyValueFactory<>("courseID"));
		tableExam.setItems(data);
		tableExam.getColumns().addAll(ExamID, Proffesion, Time, course);
	}

	/**
	 * This method locks the test and sends all students who solve it a message that
	 * the test is locked and prevents them from submitting or downloading the test.
	 * 
	 * @param event occurs when user press on "Lock"
	 * 
	 */
	@FXML
	void btnLockExam(ActionEvent event) {
		if (textExamID.getText().isEmpty())
			popUp("Please select a exam.");
		else {
			Exam examToLock = GetTableDetails(textExamID.getText());
			ObservableList<Exam> Qlist = tableExam.getSelectionModel().getSelectedItems();
			RequestToServer req = new RequestToServer("getStudentsInActiveExam");
			req.setRequestData(examToLock);
			ClientUI.cems.accept(req);
			if (CEMSClient.responseFromServer.getResponseType().equals("EXAM LOCKED")) {
				popUp("The exam was successfully locked");
			} else {
				popUp("lock exam failed");
			}
			initTableRows();
			textExamID.clear();
		}
	}
}
