
package gui_cems;

import java.io.IOException;
import java.util.ArrayList;

import entity.ActiveExam;
import entity.Exam;
import entity.Student;
import entity.Teacher;
import entity.User;
import gui_principal.PrincipalController;
import gui_student.SolveExamController;
import gui_student.StartManualExamController;
import gui_student.StudentController;
import gui_teacher.TeacherController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.ResponseFromServer;

/**
 * This class contains functions common to different classes that inherit from
 * it. With the help of this department the reuse mechanism is implemented.
 * 
 * @author Hadar Iluz
 * @author Matar Asaf
 */
public class GuiCommon {

	public final String principalStatusScreen = "PRINCIPAL";
	public final String teacherStatusScreen = "TEACHER";

	/**
	 * create a popUp with a given message.
	 * 
	 * @param msg string text input to method to display in popUp message.
	 */
	public static void popUp(String msg) {
		final Stage dialog = new Stage();
		VBox dialogVbox = new VBox(20);
		Label lbl = new Label(msg);
		lbl.setPadding(new Insets(15));
		lbl.setAlignment(Pos.CENTER);
		lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		dialogVbox.getChildren().add(lbl);
		Scene dialogScene = new Scene(dialogVbox, lbl.getMinWidth(), lbl.getMinHeight());
		dialog.setScene(dialogScene);
		dialog.show();
	}

	/**
	 * this method checks if the given string includes letters.
	 * 
	 * @param str input to method to check if legal
	 * @return true if the String contains only digits.
	 */
	public boolean isOnlyDigits(String str) {
		boolean onlyDigits = true;
		for (char ch : str.toCharArray()) {
			if (!Character.isDigit(ch)) {
				onlyDigits = false;
				System.out.println("The string contains a character that he does not digit");
				break;
			}
		}
		System.out.println("isOnlyDigits returns:" + onlyDigits); // message to console
		return onlyDigits;
	}

	/**
	 * The method loads the desired right screen to which you want to move.
	 * 
	 * @param userObj  input to identify the user who wants to switch to the screen
	 * @param fxmlName input is the screen Name of the XML file of the screen to
	 *                 which you are moving by loading it
	 */
	public void displayNextScreen(User userObj, String fxmlName) {

		if (userObj instanceof Teacher) {
			try {
				Pane newPaneRight = FXMLLoader.load(getClass().getResource(fxmlName));
				newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				TeacherController.root.add(newPaneRight, 1, 0);
			} catch (IOException e) {
				System.out.println("Couldn't load!");
				e.printStackTrace();
			}
		}

		else if (userObj instanceof Student) {
			try {
				Pane newPaneRight = FXMLLoader.load(getClass().getResource(fxmlName));
				newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				StudentController.root.add(newPaneRight, 1, 0);
			} catch (IOException e) {
				System.out.println("Couldn't load!");
				e.printStackTrace();
			}
		}

		else if (userObj instanceof User) {
			try {
				Pane newPaneRight = FXMLLoader.load(getClass().getResource(fxmlName));
				newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				PrincipalController.root.add(newPaneRight, 1, 0);
			} catch (IOException e) {
				System.out.println("Couldn't load!");
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method sends information to all students who solve a particular exam
	 * when the exam is locked or time has been added to solve the exam. Depending
	 * on the information in res
	 * 
	 * @param res the response from server
	 */
	public static void handleNotifications(ResponseFromServer res) {
		if (res.getResponseType().startsWith("NOTIFICATION_STUDENT"))
			handleStudentNotifications(res);
		else if (res.getResponseType().startsWith("NOTIFICATION_TEACHER"))
			handleTeacherNotifications(res);
		else if (res.getResponseType().startsWith("NOTIFICATION_PRINCIPAL"))
			handlePrincipalNotifications(res);
	}

	/**
	 * @param res in send as input and include NOTIFICATION text
	 * Which gives an indication that alert should be sent to principal.
	 */
	private static void handlePrincipalNotifications(ResponseFromServer res) {
		if (res.getResponseType().equals("NOTIFICATION_PRINCIPAL_REQUEST_RECEIVED")) {
			PrincipalController.CopyAlertNotification("You have a new extenstion request for exam: " + (String)res.getResponseData());
		}
	}

	/**
	 * @param res in send as input and include NOTIFICATION text
	 * Which gives an indication that alert should be sent to teacher.
	 */
	@SuppressWarnings("unchecked")
	private static void handleTeacherNotifications(ResponseFromServer res) {
		if (res.getResponseType().equals("NOTIFICATION_TEACHER_POTENTIAL_COPY")) {
			ArrayList<Integer> suspected = (ArrayList<Integer>) res.getResponseData();
			if (suspected.size() == 1) {
				return;
			}
			StringBuilder SB = new StringBuilder(
					"Suspected in copy of Exam " + suspected.get(suspected.size() - 1) + ":\n");
			suspected.remove(suspected.size() - 1);
			for (Integer std : suspected) {
				SB.append(String.valueOf(std) + " \n");
			}
			TeacherController.CopyAlertNotification(SB.toString());
		}

		if (res.getResponseType().equals("NOTIFICATION_TEACHER_REQUEST_APPROVED")) {
			TeacherController.CopyAlertNotification(
					"Request for time extenstion approved for exam: " + (String) res.getResponseData());
		}

	}

	/**
	 * @param res in send as input and include NOTIFICATION text
	 * Which gives an indication that alert should be sent to student about extra time when
	 * he solve any type of exam..
	 */
	private static void handleStudentNotifications(ResponseFromServer res) {
		if (res.getResponseType().equals("NOTIFICATION_STUDENT_EXAM_LOCKED")) {
			if ((((Exam) res.getResponseData()).getActiveExamType()).equals("manual"))
				StartManualExamController.setFlagToLockExam((Boolean) true);
			else
				SolveExamController.setFlagToLockExam((Boolean) true);
		}
		if (res.getResponseType().equals("NOTIFICATION_STUDENT_ADDED_TIME")) {
			if ((((ActiveExam) res.getResponseData()).getActiveExamType()).equals("manual"))
				StartManualExamController.addTimeToExam(((ActiveExam) res.getResponseData()).getExtraTime());
			else
				SolveExamController.addTimeToExam(((ActiveExam) res.getResponseData()).getExtraTime());
		}
	}

	/**
	 * @param ExamID input to method to check if legal
	 * @return true if legal, else false
	 */
	public static boolean checkForLegalID(String ExamID) {
		if (ExamID.length() != 6) {
			popUp("Exam ID Must be 6 digits.");
			return false;
		}
		for (int i = 0; i < ExamID.length(); i++)
			if (!Character.isDigit(ExamID.charAt(i))) {
				popUp("Exam ID Must Contains only digits.");
				return false;
			}
		return true;
	}

	/**
	 * Method that check if the givenQuestion ID is legal
	 * 
	 * @param QuestionID input to method to check if legal
	 * @return true if legal, else false
	 */
	public static boolean checkForLegalquestionID(String QuestionID) {

		if (QuestionID.length() != 5) {
			popUp("Question ID Must be 5 digits.");
			return false;
		}
		for (int i = 0; i < QuestionID.length(); i++)
			if (!Character.isDigit(QuestionID.charAt(i))) {
				popUp("Question ID Must Contains only digits.");
				return false;
			}
		return true;
	}

}
