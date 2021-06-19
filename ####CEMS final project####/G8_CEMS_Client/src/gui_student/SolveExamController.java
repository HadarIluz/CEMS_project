package gui_student;

import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.CEMSClient;
import client.ClientUI;
import entity.ActiveExam;
import entity.Exam;
import entity.ExamOfStudent;
import entity.QuestionInExam;
import entity.ReasonOfSubmit;
import entity.Student;
import gui_cems.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.RequestToServer;

/**
 * The class contains functionality for solving a student's computerized exam.
 * The student toggle through all the questions in the exam.
 * Gets information of how many questions he has to answer, a timer that can be
 * displayed and removed. 
 * If the teacher informs the student that there is extra
 * time for the exam, he receives a alert by real-time event.
 * 
 * @author Yuval Hayam
 *
 */
public class SolveExamController implements Initializable {

	@FXML
	private Button btnSubmitExam;

	@FXML
	private CheckBox checkBoxShowTime;

	@FXML
	private Button btnNext;

	@FXML
	private Button btnPrev;

	@FXML
	private Label lblTimeLeft;

	@FXML
	private RadioButton btnAnswer1;

	@FXML
	private RadioButton btnAnswer2;

	@FXML
	private RadioButton btnAnswer3;

	@FXML
	private RadioButton btnAnswer4;

	@FXML
	private Label lblQuestionNumber;

	@FXML
	private Label lblPoints;

	@FXML
	private TextArea txtQuestionDescription;

	@FXML
	private Label textQuestion;

	@FXML
	private Text txtnotification;

	@FXML
	private ImageView notificationIcon;

	@FXML
	private Label lblnotificationMsg;

	private static ActiveExam newActiveExam;
	private static Boolean lock;
	private static int addTime;
	private Timer timer;
	private int currentQuestion;
	private int[] studentAnswers;
	private AtomicInteger timeForTimer;
	private int timeLeft;
	private int timeToDeduct;

	/**
	 * @param event the method handles click on btnAnswer1
	 */
	@FXML
	void btnAnswer1(ActionEvent event) {
		studentAnswers[currentQuestion] = 1;

	}

	/**
	 * @param event the method handles click on btnAnswer2
	 */
	@FXML
	void btnAnswer2(ActionEvent event) {
		studentAnswers[currentQuestion] = 2;
	}

	/**
	 * @param event the method handles click on btnAnswer3
	 */
	@FXML
	void btnAnswer3(ActionEvent event) {
		studentAnswers[currentQuestion] = 3;
	}

	/**
	 * @param event the method handles click on btnAnswer4
	 */
	@FXML
	void btnAnswer4(ActionEvent event) {
		studentAnswers[currentQuestion] = 4;
	}

	/**
	 * @param event the method handles click on btnSubmitExam
	 */
	@FXML
	void btnSubmitExam(ActionEvent event) {
		submitExam(ReasonOfSubmit.initiated);
	}

	/**
	 * @param reasonOfSubmit this method handles the submit of exam (if it's forced
	 *                       or initiated)
	 */
	private void submitExam(ReasonOfSubmit reasonOfSubmit) {
		int dialogResult = 0;
		if (reasonOfSubmit == ReasonOfSubmit.initiated) {
			Object[] options = { " Cancel ", " Submit " };
			JFrame frame = new JFrame("Submit Exam");
			dialogResult = JOptionPane.showOptionDialog(frame,
					"Please Note!\nOnce you click Submit you can't edit exam again.", null, JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null, // do not use a custom Icon
					options, // the titles of buttons
					null); // default button title
		}
		if (dialogResult == 1 || reasonOfSubmit == ReasonOfSubmit.forced) {
			btnSubmitExam.setDisable(true);
			timer.cancel();
			btnAnswer1.setDisable(true);
			btnAnswer2.setDisable(true);
			btnAnswer3.setDisable(true);
			btnAnswer4.setDisable(true);
			btnNext.setDisable(true);
			btnPrev.setDisable(true);

			HashMap<QuestionInExam, Integer> studentQuestions = new HashMap<>();
			for (int i = 0; i < studentAnswers.length; i++) {
				studentQuestions.put(newActiveExam.getExam().getExamQuestionsWithScores().get(i), studentAnswers[i]);
			}

			ExamOfStudent examOfStudent = new ExamOfStudent(newActiveExam, (Student) ClientUI.loggedInUser.getUser());
			examOfStudent.setQuestionsAndAnswers(studentQuestions);
			examOfStudent.setTotalTime(
					((newActiveExam.getTimeAllotedForTest() - timeToDeduct) * 60 - timeForTimer.get()) / 60);
			examOfStudent.setExamType("computerized");
			examOfStudent.setReasonOfSubmit(reasonOfSubmit);

			RequestToServer req = new RequestToServer("StudentFinishExam");
			req.setRequestData(examOfStudent);
			ClientUI.cems.accept(req);

			if (CEMSClient.responseFromServer.getResponseType().equals("Success student finish exam")) {
				GuiCommon.popUp("Submit was successfull. You may exit the exam");
			} else {
				GuiCommon.popUp("There has been an error. please contact your teacher");
			}
		}
	}

	/**
	 * @param event the method handles click on btnNext
	 */
	@FXML
	void btnNext(ActionEvent event) {
		currentQuestion++;
		loadQuestion(currentQuestion);
		unselectRadioButton();
	}

	/**
	 * @param event the method handles click on btnPrev
	 */
	@FXML
	void btnPrev(ActionEvent event) {
		currentQuestion--;
		loadQuestion(currentQuestion);
		unselectRadioButton();
	}

	/**
	 * When moving between questions, this cleans the picking of answers. only if
	 * the it was not answered before
	 */
	private void unselectRadioButton() {
		btnAnswer1.setSelected(false);
		btnAnswer2.setSelected(false);
		btnAnswer3.setSelected(false);
		btnAnswer4.setSelected(false);

		if (studentAnswers[currentQuestion] != 0) {
			switch (studentAnswers[currentQuestion]) {
			case 1:
				btnAnswer1.setSelected(true);
				break;
			case 2:
				btnAnswer2.setSelected(true);
				break;
			case 3:
				btnAnswer3.setSelected(true);
				break;
			case 4:
				btnAnswer4.setSelected(true);
				break;
			}

		}
	}

	/**
	 * @param event the method handles click on checkBoxShowTime
	 */
	@FXML
	void checkBoxShowTime(MouseEvent event) {
		lblTimeLeft.setVisible(!lblTimeLeft.visibleProperty().get());
	}

	/**
	 * This method initializes the screen
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// bring all exam details (also questions and scores)
		RequestToServer req = new RequestToServer("getFullExamDetails");
		req.setRequestData(newActiveExam.getExam());
		ClientUI.cems.accept(req);

		newActiveExam.setExam((Exam) CEMSClient.responseFromServer.getResponseData());
		lock = false;
		addTime = 0;
		// set the timer
		LocalTime currentTime = (new Time(System.currentTimeMillis())).toLocalTime();
		timeToDeduct = (currentTime.toSecondOfDay() - newActiveExam.getStartTime().toLocalTime().toSecondOfDay()) / 60;
		int timeForStudent = (newActiveExam.getTimeAllotedForTest() - timeToDeduct) * 60;
		timeForTimer = new AtomicInteger(timeForStudent);
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// When extra time is received updates the timer and notifies the student
				if (addTime != 0) {
					newActiveExam.setExtraTime(addTime);
					newActiveExam.setTimeAllotedForTest(
							(newActiveExam.getTimeAllotedForTest() + newActiveExam.getExtraTime()) + "");
					timeLeft = timeForTimer.get() + (addTime * 60);
					timeForTimer.set(timeLeft);
					Platform.runLater(() -> lblnotificationMsg.setText("Please note, the exam time\nwas extended by "
							+ newActiveExam.getExtraTime() + " minutes."));
					notificationIcon.setVisible(true);
					txtnotification.setVisible(true);
					lblnotificationMsg.setVisible(true);
					addTime = 0;
				}
				int hours = timeForTimer.get() / 3600;
				int minutes = (timeForTimer.get() % 3600) / 60;
				int seconds = timeForTimer.get() % 60;
				String str = String.format("Time left: %02d:%02d:%02d", hours, minutes, seconds);
				Platform.runLater(() -> lblTimeLeft.setText(str));
				timeForTimer.decrementAndGet();
				if (timeForTimer.get() == 0 || lock) {
					Platform.runLater(() -> stopExam());
				}
			}
		}, 0, 1000);

		ToggleGroup group = new ToggleGroup();
		btnAnswer1.setToggleGroup(group);
		btnAnswer2.setToggleGroup(group);
		btnAnswer3.setToggleGroup(group);
		btnAnswer4.setToggleGroup(group);

		studentAnswers = new int[newActiveExam.getExam().getExamQuestionsWithScores().size()];

		currentQuestion = 0;
		loadQuestion(currentQuestion);
	}

	/**
	 * @param i this method loads question i to the screen
	 */
	private void loadQuestion(int i) {
		int qNum = i + 1;
		lblQuestionNumber
				.setText("Question " + qNum + " / " + newActiveExam.getExam().getExamQuestionsWithScores().size());
		QuestionInExam q = newActiveExam.getExam().getExamQuestionsWithScores().get(i);
		lblPoints.setText("<" + q.getScore() + "> Points");
		txtQuestionDescription.setText(q.getQuestion().getDescription());
		textQuestion.setText(q.getQuestion().getQuestion());
		btnAnswer1.setText(q.getQuestion().getAnswers()[0]);
		btnAnswer2.setText(q.getQuestion().getAnswers()[1]);
		btnAnswer3.setText(q.getQuestion().getAnswers()[2]);
		btnAnswer4.setText(q.getQuestion().getAnswers()[3]);

		if (currentQuestion == 0) {
			btnPrev.setDisable(true);
			if (newActiveExam.getExam().getExamQuestionsWithScores().size() != 1) {
				btnNext.setDisable(false);
			}
		} else if (currentQuestion == newActiveExam.getExam().getExamQuestionsWithScores().size() - 1) {
			btnNext.setDisable(true);
			if (newActiveExam.getExam().getExamQuestionsWithScores().size() != 1) {
				btnPrev.setDisable(false);
			}
		} else {
			btnPrev.setDisable(false);
			btnNext.setDisable(false);
		}
	}

	/**
	 * Clicking on the notification disappears it from the screen.
	 *
	 * @param event that occurs when clicking on 'imgNotification' ImageView
	 */
	@FXML
	void clickImgNotification(MouseEvent event) {
		notificationIcon.setVisible(false);
		txtnotification.setVisible(false);
		lblnotificationMsg.setVisible(false);
	}

	/**
	 * This method stops the exam in a case it's forced
	 */
	private void stopExam() {
		btnSubmitExam.setDisable(true);
		GuiCommon.popUp("The exam is locked!");
		submitExam(ReasonOfSubmit.forced);
	}

	/**
	 * Receive ActiveExam from the previous screen.
	 * 
	 * @param newActiveExamInProgress the new exam
	 */
	public static void setActiveExamState(ActiveExam newActiveExamInProgress) {
		newActiveExam = newActiveExamInProgress;
	}

	/**
	 * Receive temp = true from the server when a teacher locks up the test
	 * 
	 * @param temp if to lock
	 */
	public static void setFlagToLockExam(Boolean temp) {
		lock = temp;
	}

	/**
	 * Receives from the server the time the teacher added to the test
	 * 
	 * @param time to add
	 */
	public static void addTimeToExam(int time) {
		addTime = time;
	}

}
