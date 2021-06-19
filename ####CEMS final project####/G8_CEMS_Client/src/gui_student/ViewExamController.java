package gui_student;

import java.util.ArrayList;
import client.CEMSClient;
import client.ClientUI;
import entity.ActiveExam;
import entity.Exam;
import entity.ExamOfStudent;
import entity.Question;
import entity.QuestionRow;
import entity.Student;
import entity.User;
import gui_cems.GuiCommon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.RequestToServer;


/**
 * The department contains functionality for presenting the exam questions and
 * answers solved by the student.
 * 
 * @author Nadav Yadin
 *
 */
public class ViewExamController extends GuiCommon {

	@FXML
	private TextField txtExamID;

	@FXML
	private Button btnGetCopyOfExam;

	@FXML
	private Button btnViewGrade;

	@FXML
	private Text txtReqField;

	@FXML
	private Label lblCourse;

	@FXML
	private Label lblGrade;

	@FXML
	private Label textCourseName;

	@FXML
	private Label textGrade;

	@FXML
	private Label lblProfession;

	@FXML
	private Label textProfessionName;

	@FXML
	private Label lblDownload;

	@FXML
	private ImageView imgDownload;

	@FXML
	private Label lblExamIDNotFound;

	@FXML
	private ImageView imgRefresh;

	private User student = (User) ClientUI.loggedInUser.getUser();
	static ArrayList<QuestionRow> questionsID;
	static ArrayList<Question> questions;
	private String examType;

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * method present for the student the exam that he solved. if its compuetrized , its open a new screen with
	 * table of all details and the results. if manual, the file is downloaded as a form.
	 * @param event occurs when student press " get Copy" button
	 */
		
	void btnGetCopyOfExam(ActionEvent event) {

		if (examType.equals("computerized")) {
			RequestToServer req = new RequestToServer("getSolvedComputerizedExam");
			String[] details = { String.valueOf(student.getId()), txtExamID.getText() };
			req.setRequestData(details);
			ClientUI.cems.accept(req);
			questionsID = (ArrayList<QuestionRow>) CEMSClient.responseFromServer.getResponseData();

			questions = new ArrayList<Question>();
			for (QuestionRow curr : questionsID) {
				RequestToServer req2question = new RequestToServer("getAnswersOfMistakeQuestion");
				req2question.setRequestData(curr.getQuestionID());
				ClientUI.cems.accept(req2question);
				Question question;
				question = (Question) CEMSClient.responseFromServer.getResponseData();
				question.setCorrectAns(question.getAnswers()[question.getCorrectAnswerIndex() - 1]);
				if (curr.getStudentAnswer() != 0) {
					question.setStdAns(question.getAnswers()[curr.getStudentAnswer() - 1]);
				} else {
					question.setStdAns("You didn't choose any answer for that question!");
				}
				if (!question.getCorrectAns().equals(question.getStdAns())) {

					question.setDescription("X");
				} else {

					question.setDescription("V");
				}

				questions.add(question);
			}

			displayNextScreen(student, "solvedExam.fxml");

		} else {
			ExamOfStudent studentExam = new ExamOfStudent(new ActiveExam(new Exam(txtExamID.getText())),
					(Student) student, 0);
			RequestToServer req = new RequestToServer("downloadSolvedManualExam");
			req.setRequestData(studentExam);
			ClientUI.cems.accept(req);
			if (CEMSClient.responseFromServer.getResponseData().equals("Download Failed")) {
				popUp("Download Failed.");
				return;
			}
			popUp("Exam Download Successfully!");
		}

	}
	/**
	 * method check if the exam id that the student insert is legal and  available in database.
	 * @param event occurs when student press" View Grade" button 
	 */

	@FXML
	void btnViewGrade(ActionEvent event) {
		if (!checkForLegalID(txtExamID.getText())) {
			return;

		}

		RequestToServer req = new RequestToServer("StudentView grade");
		String[] StudentData = { txtExamID.getText(), String.valueOf(student.getId()) };

		req.setRequestData(StudentData);
		ClientUI.cems.accept(req);
		@SuppressWarnings("unchecked")
		ArrayList<String> Details = (ArrayList<String>) CEMSClient.responseFromServer.getResponseData();
		lblCourse.setVisible(true);
		lblProfession.setVisible(true);
		lblGrade.setVisible(true);
		textGrade.setVisible(true);
		textProfessionName.setVisible(true);
		textCourseName.setVisible(true);
		try {
			textGrade.setText(Details.get(0));
			examType = Details.get(1);
			textProfessionName.setText(Details.get(2));
			textCourseName.setText(Details.get(3));
			btnGetCopyOfExam.setDisable(false);
		} catch (IndexOutOfBoundsException e) {
			btnGetCopyOfExam.setDisable(true);
			lblCourse.setVisible(false);
			lblProfession.setVisible(false);
			lblGrade.setVisible(false);
			textGrade.setVisible(false);
			textProfessionName.setVisible(false);
			textCourseName.setVisible(false);
			popUp("Wrong ExamID, Please check again!");

		}

	}
	/**
	 * method refresh the screen for view Exam grades
	 * @param event occurs when User press on refresh sign
	 */

	@FXML
	void RefreshPage(MouseEvent event) {

		this.displayNextScreen(student, "ViewExam.fxml");

	}

}
