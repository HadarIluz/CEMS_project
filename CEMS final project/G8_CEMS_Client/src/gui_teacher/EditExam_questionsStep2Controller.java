package gui_teacher;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.Exam;
import entity.QuestionInExam;
import entity.Teacher;
import entity.User;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.RequestToServer;

/**
 * Class contains functionality for edit exam as part of 2 main steps. This
 * screen describes the second stage where the teacher sees all the questions
 * that appears in the exam she has chosen and she can update the score of all
 * the questions in table.
 * 
 * We reuse the screen to display all the question details of any exam in the
 * system that the principal has chosen to see what exam bank is. Therefore the
 * screen distinguishes between 2 types of users: Manager - viewing permissions
 * only. Teacher - editing permissions as described.
 * 
 * @author Hadar Iluz
 *
 */
public class EditExam_questionsStep2Controller extends GuiCommon implements Initializable {

	@FXML
	private Text textTitalScreen_step2;

	@FXML
	private ImageView imgStep1;

	@FXML
	private ImageView imgStep2;

	@FXML
	private Button btnBack;

	@FXML
	private Text textTotalScore;

	@FXML
	private Label textErrorMsg;

	@FXML
	private TableView<QuestionInExam> tableQuestion;

	@FXML
	private TableColumn<QuestionInExam, String> questionID;

	@FXML
	private TableColumn<QuestionInExam, String> questionScore;

	@FXML
	private TableColumn<QuestionInExam, String> question;

	@FXML
	private Text textQid;

	@FXML
	private Text ChosenQuestionID;

	@FXML
	private TextField txtChangeScore;

	@FXML
	private Button btnUpdateScore;

	@FXML
	private Text textNavigation;

	public static Exam exam;
	private static Teacher teacher;
	private static User principal;
	private static boolean displayPrincipalView;

	private static ArrayList<QuestionInExam> existsQuestions;
	private ObservableList<QuestionInExam> Qlist;

	/**
	 * @param event that occurs when the teacher selects a question from the table
	 *              and updates its score. The update is saved in the list of
	 *              questions with the score. The method displays a message if the
	 *              value is incorrect.
	 */
	@FXML
	void UpdateScore(ActionEvent event) {
		if (!(txtChangeScore.getText().trim().isEmpty())) {
			// check valid score
			int changeScore = Integer.parseInt(txtChangeScore.getText().trim());
			if (changeScore > 0 && changeScore < 101) {

				Qlist.get(0).setScore(Integer.valueOf(txtChangeScore.getText().trim()));
				updateScoreForSpecificQuestion(Integer.parseInt(txtChangeScore.getText().trim()));
				updateTotalScore();
			} else {
				popUp("Invalid Score");
			}
		}
	}

	/**
	 * @param changeScore input set into existsQuestions list in order to display
	 *                    the new score after update.
	 */
	private void updateScoreForSpecificQuestion(int changeScore) {
		for (QuestionInExam q : existsQuestions) {
			if (q.getQuestionID().equals(ChosenQuestionID.getText().trim())) {
				q.setScore(changeScore);
			}
		}
		initTable();
	}

	/**
	 * @param event that occurs when user press on back button, he return to step 1
	 *              of Edit Exam / Exam Details according to user Permissions,
	 *              teacher or principal.
	 */
	@FXML
	void btnBack(ActionEvent event) {
		// bring to the previous screen all the existsQuestions if the new after
		// update!.
		// when teacher will press on the save edit exam the data will saved in the DB
		// by server.
		EditExamController.setDataFromStep2(exam, displayPrincipalView, existsQuestions, true);
		if (!displayPrincipalView) {
			displayNextScreen(teacher, "/gui_teacher/EditExam.fxml");

		} else {
			displayNextScreen(principal, "/gui_teacher/EditExam.fxml");
		}
	}

	/**
	 * The function use calcTotalScore and display the total exam score with error
	 * message in case the exam total score is nor equal to 100 after edit
	 */
	private void updateTotalScore() {
		int sum = calcTotalScore();
		textTotalScore.setText(String.valueOf(sum));
		if (sum == 100) {
			textErrorMsg.setVisible(false);
			btnBack.setDisable(false);
		} else {
			textErrorMsg.setVisible(true);
			btnBack.setDisable(true);
		}

	}

	/**
	 * @return total score of exam by calculation of adding the score of each
	 *         questions in the exam.
	 */
	private int calcTotalScore() {
		int sum = 0;
		for (QuestionInExam q : existsQuestions) {
			sum += q.getScore();
		}
		return sum;
	}

	/**
	 * method set text of questionID when user select a question row from table
	 * 
	 * @param event that occurs when User press on a selected row from table
	 */
	@FXML
	void chooseQ(MouseEvent event) {
		Qlist = tableQuestion.getSelectionModel().getSelectedItems();
		if (Qlist.isEmpty()) {
			return;
		}
		txtChangeScore.setText(String.valueOf(Qlist.get(0).getScore()));
		ChosenQuestionID.setText(Qlist.get(0).getQuestionID());

	}

	/**
	 * initialize function to prepare the screen after it is loaded. Tack user data
	 * according to screen status from the previous action.
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Handles the case teacher switches between the screens several
		// times, we keep the latest updates she has made.
		if (existsQuestions==null) {
			// bring all exam details (also questions and scores)
			RequestToServer req = new RequestToServer("getFullExamDetails");
			req.setRequestData(exam);
			ClientUI.cems.accept(req);

			exam = (Exam) CEMSClient.responseFromServer.getResponseData();
			existsQuestions = exam.getExamQuestionsWithScores(); // Return ArrayList<QuestionInExam>
		}
		if(existsQuestions!=null) {
			RequestToServer req = new RequestToServer("getFullExamDetails");
			req.setRequestData(exam);
			ClientUI.cems.accept(req);

			exam = (Exam) CEMSClient.responseFromServer.getResponseData();
			existsQuestions = exam.getExamQuestionsWithScores(); 
			initTable();
		}

		if (!displayPrincipalView) {
			teacher = (Teacher) ClientUI.loggedInUser.getUser();

			// verify teacher not delete all of this questions before she edit this exam!
			if (exam.getExamQuestionsWithScores() != null) {
				initTable();
			}
			// when exam open at first for edit the total score is 100 !.
			textTotalScore.setText(Integer.toString(calcTotalScore()));
			System.out.println(calcTotalScore());
			textErrorMsg.setVisible(false);

		} else if (ClientUI.loggedInUser.getUser() instanceof User) {
			// setUp before load screen.
			principal = (User) ClientUI.loggedInUser.getUser();
			displayPrincipalView = true;

			btnUpdateScore.setDisable(false);
			btnUpdateScore.setVisible(false);
			txtChangeScore.setDisable(false);
			txtChangeScore.setVisible(false);
			textErrorMsg.setDisable(false);
			textErrorMsg.setVisible(false);
			textTitalScreen_step2.setText("Exams Details");
			textTotalScore.setDisable(false);
			textTotalScore.setVisible(false);
			textTotalScore.setDisable(false);
			textTotalScore.setVisible(false);
			textQid.setDisable(false);
			textQid.setVisible(false);
			ChosenQuestionID.setDisable(false);
			ChosenQuestionID.setVisible(false);
			textNavigation.setVisible(true);
			// verify teacher not delete all of this questions before she edit this exam!
			if (exam.getExamQuestionsWithScores() != null) {
				initTable();
			}

		}

	}

	/**
	 * Initializes the table with all the latest data of the existing questions in
	 * the exam, display the question, question id and its current score.
	 */
	@SuppressWarnings("unchecked")
	public void initTable() {
		for (QuestionInExam curr : existsQuestions) {
			curr.setQuestionID();
			curr.setQuestionDescription();
		}
		Qlist = FXCollections.observableArrayList(existsQuestions);

		tableQuestion.getColumns().clear();
		questionID.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		questionScore.setCellValueFactory(new PropertyValueFactory<>("score"));
		question.setCellValueFactory(new PropertyValueFactory<>("questionDescription"));

		tableQuestion.setItems(Qlist);
		tableQuestion.getColumns().addAll(questionID, questionScore, question);

	}

	/**
	 * @param examData              with all updated details.
	 * @param displayPrincipalView2 the current screen mode according to logged
	 *                              user.
	 * @param updatedQuestions the new questions
	 */
	public static void setnextScreenData(Exam examData, boolean displayPrincipalView2,
			ArrayList<QuestionInExam> updatedQuestions) {
		exam = examData;
		displayPrincipalView = displayPrincipalView2;
		existsQuestions = updatedQuestions;

	}

}