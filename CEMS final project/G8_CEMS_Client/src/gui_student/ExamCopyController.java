package gui_student;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import entity.Question;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/**
 * 
 * The class contains functionality for a student to view his grade and receive
 * a copy of the test.
 * 
 * @author Nadav and Yadin
 *
 */
public class ExamCopyController extends GuiCommon implements Initializable {

	@FXML
	private TableView<Question> tableQuestion;

	@FXML
	private TableColumn<Question, String> Question;

	@FXML
	private TableColumn<Question, String> YourAnswer;

	@FXML
	private TableColumn<Question, String> CorrectAns;

	@FXML
	private TableColumn<Question, String> ResAns;

	@FXML
	private Text textNavigation;
	@FXML
	private Button backBtn;

	private ObservableList<Question> data;

	/**
	 * method open previous screen 
	 * @param event occurs when User press "back" button
	 */
	
	
	@FXML
	void BackBtn(ActionEvent event) {
		displayNextScreen((User) ClientUI.loggedInUser.getUser(), "ViewExam.fxml");
	}

	/**
	 * initialize function to prepare the screen after it is loaded.
	 * 
	 * @param location The location used to resolve relative paths for the root object
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableQuestion.getColumns().clear();
		data = FXCollections.observableArrayList(ViewExamController.questions);
		Question.setCellValueFactory(new PropertyValueFactory<>("question"));
		YourAnswer.setCellValueFactory(new PropertyValueFactory<>("StdAns"));
		CorrectAns.setCellValueFactory(new PropertyValueFactory<>("correctAns"));
		ResAns.setCellValueFactory(new PropertyValueFactory<>("description"));
		tableQuestion.setItems(data);
		tableQuestion.getColumns().addAll(Question, YourAnswer, CorrectAns, ResAns);

	}

}
