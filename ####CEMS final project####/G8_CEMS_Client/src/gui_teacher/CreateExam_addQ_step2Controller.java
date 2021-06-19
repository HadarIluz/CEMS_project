package gui_teacher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Exam;
import entity.Question;
import entity.QuestionInExam;
import entity.QuestionInExamRow;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The class contains functionality for step 2 of creating a test, you can add
 * questions and update a score for each question. You can delete a question
 * Beyond step 3 is done only when the score of the questions is equal to 100.
 * 
 * @author Yuval Hayam
 *
 */
public class CreateExam_addQ_step2Controller implements Initializable {

	@FXML
	private Button btnCreateNewQuestion;

	@FXML
	private ImageView imgComputer;

	@FXML
	private Label text100;

	@FXML
	private ImageView imgStep1;

	@FXML
	private ImageView imgStep2;

	@FXML
	private ImageView imgStep3;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

	@FXML
	private Text textTotalScore;

	@FXML
	private Label textErrorMsg;

	@FXML
	private TableView<QuestionInExamRow> tableAddedQuestions;

	@FXML
	private TableColumn<QuestionInExamRow, String> questionID;

	@FXML
	private TableColumn<QuestionInExamRow, Integer> questionScore;

	@FXML
	private TableColumn<QuestionInExamRow, String> question;

	@FXML
	private Button btnBrowseQuestions;

	@FXML
	private Text ChosenQuestionID;

	@FXML
	private TextField txtChangeScore;

	@FXML
	private Button btnUpdateScore;

	@FXML
	private Button btnDelete;

	private static Exam newExam;

	private static ArrayList<Question> availableQuestions;
	private ObservableList<QuestionInExamRow> selectedQuestionsRows = FXCollections.observableArrayList();
	private ObservableList<QuestionInExamRow> Qlist;

	/**
	 * @param event handles click on button delete
	 */
	@FXML
	void DeleteFromExam(ActionEvent event) {
		if (Qlist != null) {
			selectedQuestionsRows.remove(Qlist.get(0));
			updateTotalScore();
		}
	}

	/**
	 * @param event handles click on button update score
	 */
	@FXML
	void UpdateScore(ActionEvent event) {
		if (!(txtChangeScore.getText().trim().isEmpty())) {
			// check valid score
			int changeScore = Integer.parseInt(txtChangeScore.getText().trim());
			if (changeScore > 0 && changeScore < 101) {
				if (Qlist != null) {
					Qlist.get(0).setScore(Integer.valueOf(txtChangeScore.getText().trim()));
					updateTotalScore();
				}
			} else {
				GuiCommon.popUp("Invalid Score");
			}
		}
	}

	/**
	 * @param event handles click on button back
	 */
	@FXML
	void btnBack(ActionEvent event) {
		try {
			setQuestionsInNewExam();
			CreateExam_step1Controller.setExamState(newExam);
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("CreateExam_step1.fxml"));
			newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			TeacherController.root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			System.out.println("Couldn't load!");
			e.printStackTrace();
		}

	}

	/**
	 * @param event handles click on button browse questions
	 */
	@FXML
	void btnBrowseQuestions(ActionEvent event) {
		BrowseQuestionController.setAvailableQuestions(availableQuestions);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BrowseQuestions.fxml"));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {
			return;
		}

		Stage inputStage = new Stage();
		inputStage.initOwner(TeacherController.root.getScene().getWindow());
		inputStage.setScene(newScene);
		inputStage.showAndWait();

		QuestionInExam q = loader.<BrowseQuestionController>getController().getSelectedQuestion();
		if (q != null) {
			q.setExam(newExam);
			insertRow(q);
		}
		listenToCloseWindow(inputStage);
	}

	/**
	 * listen for close events on a JavaFX Stage, notified when the user clicks the
	 * button with the X on, in the upper right corner of the Stage
	 * 
	 * @param primaryStage
	 */
	private void listenToCloseWindow(Stage inputStage) {
		inputStage.setOnCloseRequest((event) -> {
			System.out.println("Closing Stage");
		});

	}

	/**
	 * handles the update of the total score of exam after teacher changes score of
	 * q
	 */
	private void updateTotalScore() {
		int sum = 0;
		for (QuestionInExamRow q : selectedQuestionsRows) {
			sum += q.getScore();
		}

		textTotalScore.setText(String.valueOf(sum));
		if (sum == 100) {
			textErrorMsg.setVisible(false);
			btnNext.setDisable(false);
		} else {
			textErrorMsg.setVisible(true);
			btnNext.setDisable(true);
		}

	}

	/**
	 * @param event handles click on button next
	 */
	@FXML
	void btnNext(ActionEvent event) {
		setQuestionsInNewExam();
		CreateNewExam_step3Controller.setExamState(newExam);
		try {
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("CreateNewExam_step3.fxml"));
			newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			TeacherController.root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			System.out.println("Couldn't load!");
			e.printStackTrace();
		}

	}

	/**
	 * copies the questions the teacher chose to the new exam
	 */
	private void setQuestionsInNewExam() {
		ArrayList<QuestionInExam> finaleQusetionList = new ArrayList<QuestionInExam>();
		for (QuestionInExamRow q : selectedQuestionsRows) {
			finaleQusetionList.add(q.getQuestionObject());
		}
		newExam.setExamQuestionsWithScores(finaleQusetionList);
	}

	/**
	 * @param event handles click on a question in the table
	 */
	@FXML
	void chooseQ(MouseEvent event) {
		Qlist = tableAddedQuestions.getSelectionModel().getSelectedItems();
		if (!Qlist.isEmpty()) {
			txtChangeScore.setText(String.valueOf(Qlist.get(0).getScore()));
			ChosenQuestionID.setText(Qlist.get(0).getQuestionID());
		}

	}

	/**
	 * @param newExamInProgress new exam to save
	 * allows other screens to set the current new exam
	 */
	public static void setExamState(Exam newExamInProgress) {
		newExam = newExamInProgress;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtChangeScore.setText("0");
		initTableCols();

		if (newExam.getExamQuestionsWithScores() != null) {
			for (QuestionInExam q : newExam.getExamQuestionsWithScores()) {
				availableQuestions.remove(q.getQuestion());
				insertRow(q);
			}
		}

	}

	/**
	 * @param q puts question q in the table
	 */
	private void insertRow(QuestionInExam q) {
		selectedQuestionsRows.add(
				new QuestionInExamRow(q.getQuestion().getQuestionID(), q.getScore(), q.getQuestion().getQuestion(), q));
		tableAddedQuestions.refresh();
		availableQuestions.remove(q.getQuestion());
		updateTotalScore();

	}

	/**
	 * @param questionBank allows other screens to load available questions
	 */
	public static void loadAvailableQuestions(ArrayList<Question> questionBank) {
		availableQuestions = questionBank;
	}

	/**
	 * creates the table columns
	 */
	@SuppressWarnings("unchecked")
	public void initTableCols() {
		tableAddedQuestions.getColumns().clear();
		questionID.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		questionScore.setCellValueFactory(new PropertyValueFactory<>("score"));
		question.setCellValueFactory(new PropertyValueFactory<>("question"));

		tableAddedQuestions.setItems(selectedQuestionsRows);
		tableAddedQuestions.getColumns().addAll(questionID, questionScore, question);

	}

}
