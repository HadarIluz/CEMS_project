package gui_teacher;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.Question;
import entity.QuestionRow;
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
 * The class contains functionality for presenting all the questions belonging
 * to the teacher, you can create a new question and edit it.
 * 
 * @author Nadav Dery
 * @author Yadin Amsalem
 * @version 1.0 build 07/07/2021
 */

public class QuestionBankController extends GuiCommon implements Initializable {

	@FXML
	private Button btnEditQuestion;

	@FXML
	private Button btnDeleteQuestion;

	@FXML
	private TextField textQuestionID;

	@FXML
	private TableView<QuestionRow> tableQuestion;

	@FXML
	private TableColumn<QuestionRow, String> QuestionID;

	@FXML
	private TableColumn<QuestionRow, String> Proffesion;

	@FXML
	private TableColumn<QuestionRow, String> Question;

	@FXML
	private Text textMsg1;

	@FXML
	private Text textMsg2;

	@FXML
	private Button btnCreateNewQuestion;

	@FXML
	private Text textNavigation;

	@FXML
	private Button btnOpenQuestionInfo;

	private ObservableList<QuestionRow> data;
	private static boolean displayPrincipalView = false;
	private static Teacher teacher;
	private static User principal;
	protected static String chosenQuestionID;
	private HashMap<String, String> profName;

	/**
	 * method set text of questionID when user select a question row from table
	 * 
	 * @param event occurs when User press on a selected row from table
	 */

	@FXML
	void MouseC(MouseEvent event) {
		ObservableList<QuestionRow> Qlist;
		Qlist = tableQuestion.getSelectionModel().getSelectedItems();
		if (!Qlist.isEmpty()) {
			textQuestionID.setText(Qlist.get(0).getQuestionID());
		}
	}

	/**
	 * method move user to Create new Question screen
	 * 
	 * @param event occurs when User press "Create new Question"
	 */

	@FXML
	void btnCreateNewQuestion(ActionEvent event) {
		displayNextScreen(teacher, "CreateQuestion.fxml");
	}

	/**
	 * Method use to delete data of question from the teacher's question bank
	 * 
	 * @param event occurs when User press On Delete
	 */
	@FXML
	void btnDeleteQuestion(ActionEvent event) {

		if ((textQuestionID.getText().isEmpty())) {
			btnDeleteQuestion.setDisable(true);
		} else {

			if (!checkForLegalquestionID(textQuestionID.getText()))
				return;

			ObservableList<QuestionRow> Qlist;
			Question questionToDelete = new Question();
			questionToDelete.setQuestionID(textQuestionID.getText());
			questionToDelete.setTeacher(new Teacher(ClientUI.loggedInUser.getUser(), null));
			Qlist = tableQuestion.getSelectionModel().getSelectedItems();
			RequestToServer req = new RequestToServer("DeleteQuestion");
			req.setRequestData(questionToDelete);
			ClientUI.cems.accept(req);

			if (CEMSClient.responseFromServer.getResponseData().equals("FALSE"))
				System.out.println("failed to delete question");
			else
				data.removeAll(Qlist);
			initTableRows();
		}
	}

	/**
	 * Method use to edit data of question from the teacher's question bank
	 * 
	 * @param event occurs when User press On Edit
	 * 
	 */
	@FXML
	void btnEditQuestion(ActionEvent event) {

		if ((textQuestionID.getText().isEmpty())) {
			btnCreateNewQuestion.setDisable(true);

		} else {
			if (!checkForLegalquestionID(textQuestionID.getText()))
				return;
			chosenQuestionID = textQuestionID.getText();
			displayNextScreen(teacher, "EditQuestion.fxml");
		}
	}

	/**
	 * method open screen with all question info
	 * 
	 * @param event occure when user want to edit question
	 */

	@FXML
	void btnOpenQuestionInfo(ActionEvent event) {
		if (displayPrincipalView) {
			if ((textQuestionID.getText().isEmpty())) {
				btnCreateNewQuestion.setDisable(true);
			} else {
				CreateQuestionController.setNextScreenData(textQuestionID.getText());
				displayNextScreen(principal, "CreateQuestion.fxml");
			}
		}
	}

	/**
	 * Method initialize for user screen of question bank
	 * 
	 * @param location  for Url location
	 * @param resources of type ResourceBundle
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textQuestionID.setEditable(false);
		RequestToServer req = new RequestToServer("getProfNames");
		ClientUI.cems.accept(req);
		profName = (HashMap<String, String>) CEMSClient.responseFromServer.getResponseData();
		if (ClientUI.loggedInUser.getUser() instanceof Teacher) {
			teacher = (Teacher) ClientUI.loggedInUser.getUser();
			initTableRows();
		}

		else if (ClientUI.loggedInUser.getUser() instanceof User) {
			principal = (User) ClientUI.loggedInUser.getUser();
			displayPrincipalView = true;

			btnOpenQuestionInfo.setDisable(false);
			btnOpenQuestionInfo.setVisible(true);

			textMsg1.setVisible(false);
			textMsg2.setVisible(false);
			btnCreateNewQuestion.setVisible(false);
			btnCreateNewQuestion.setVisible(false);
			btnEditQuestion.setVisible(false);
			btnEditQuestion.setVisible(false);
			btnDeleteQuestion.setVisible(false);
			btnDeleteQuestion.setVisible(false);
			textNavigation.setVisible(true);
			textQuestionID.setEditable(false);// CHECK!!!
			fillTableForPrincipal_ALLQuestionsInSystem(); // set all exams in cems system into the table

		}

	}

	/**
	 * method fill datails of question in principal screen
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	private void fillTableForPrincipal_ALLQuestionsInSystem() {
		RequestToServer req = new RequestToServer("getAllQuestionsStoredInSystem");
		ArrayList<QuestionRow> questionList = new ArrayList<QuestionRow>();
		ClientUI.cems.accept(req);

		questionList = (ArrayList<QuestionRow>) CEMSClient.responseFromServer.getResponseData();
		for (QuestionRow curr : questionList)
			curr.setProfession(profName.get(curr.getQuestionID().substring(0, 2)));

		data = FXCollections.observableArrayList(questionList);

		tableQuestion.getColumns().clear();
		QuestionID.setCellValueFactory(new PropertyValueFactory<>("QuestionID"));
		Proffesion.setCellValueFactory(new PropertyValueFactory<>("profession"));
		Question.setCellValueFactory(new PropertyValueFactory<>("Question"));

		tableQuestion.setItems(data);
		tableQuestion.getColumns().addAll(QuestionID, Proffesion, Question);
	}

	/**
	 * initTableRows get from server all exams of the logged teacher and insert into
	 * the table.
	 */
	@SuppressWarnings("unchecked")
	public void initTableRows() {
		textQuestionID.setEditable(true);
		RequestToServer req = new RequestToServer("getQuestions");
		req.setRequestData(ClientUI.loggedInUser.getUser().getId());
		ArrayList<QuestionRow> questionList = new ArrayList<QuestionRow>();

		ClientUI.cems.accept(req);
		questionList = (ArrayList<QuestionRow>) CEMSClient.responseFromServer.getResponseData();
		for (QuestionRow curr : questionList)
			curr.setProfession(profName.get(curr.getQuestionID().substring(0, 2)));

		data = FXCollections.observableArrayList(questionList);

		tableQuestion.getColumns().clear();
		QuestionID.setCellValueFactory(new PropertyValueFactory<>("QuestionID"));
		Proffesion.setCellValueFactory(new PropertyValueFactory<>("profession"));
		Question.setCellValueFactory(new PropertyValueFactory<>("Question"));

		tableQuestion.setItems(data);
		tableQuestion.getColumns().addAll(QuestionID, Proffesion, Question);
	}

}