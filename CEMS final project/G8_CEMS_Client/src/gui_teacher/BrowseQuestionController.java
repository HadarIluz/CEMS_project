package gui_teacher;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Question;
import entity.QuestionInExam;
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

/**
 * The class contains functionality for displaying all the relevant questions of
 * a selected test
 *
 */

public class BrowseQuestionController implements Initializable {

	@FXML
	private Button btnSelectQuestion;

	@FXML
	private TextField textQuestionScore;

	@FXML
	private TableView<Question> tableQuestion;

	@FXML
	private TableColumn<Question, String> QuestionID;

	@FXML
	private TableColumn<Question, String> Question;

	@FXML
	private Text textMsg1;

	private QuestionInExam selectedQ = null;
	private static ArrayList<Question> availableQuestions;
	private ObservableList<Question> Qlist;

	/**
	 * @param event handles click on table row
	 */
	@FXML
	void clickOnTableRow(MouseEvent event) {
		Qlist = tableQuestion.getSelectionModel().getSelectedItems();
	}

	/**
	 * @param event handles click on button selectQuestion
	 */
	@FXML
	void selectQuestion(ActionEvent event) {
		if (!textQuestionScore.getText().trim().isEmpty()) {
			int changeScore = Integer.parseInt(textQuestionScore.getText().trim());
			if (changeScore > 0 && changeScore < 101) {
				if (Qlist != null || selectedQ != null) {
					selectedQ = new QuestionInExam(Integer.parseInt(textQuestionScore.getText()), Qlist.get(0), null);
					btnSelectQuestion.getScene().getWindow().hide();
				}
			} else {
				GuiCommon.popUp("Invalid Score");
			}
		}

	}

	/**
	 * @return the selcted question
	 */
	public QuestionInExam getSelectedQuestion() {
		if (selectedQ != null) {
			return selectedQ;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableQuestion.getColumns().clear();
		QuestionID.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		Question.setCellValueFactory(new PropertyValueFactory<>("question"));

		tableQuestion.setItems(FXCollections.observableArrayList(availableQuestions));

		tableQuestion.getColumns().addAll(QuestionID, Question);

	}

	/**
	 * @param availableQ sets the available questions for the teacher
	 */
	public static void setAvailableQuestions(ArrayList<Question> availableQ) {
		availableQuestions = availableQ;
	}

}
