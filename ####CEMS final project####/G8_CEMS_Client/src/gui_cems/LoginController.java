package gui_cems;

import java.io.IOException;

import client.CEMSClient;
import client.ClientUI;
import entity.Student;
import entity.Teacher;
import entity.User;
import gui_principal.PrincipalController;
import gui_student.StudentController;
import gui_teacher.TeacherController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.LoggedInUser;
import logic.RequestToServer;

//________/\\\\\\\\\___/\\\\\\\\\\\\\\\___/\\\\____________/\\\\______/\\\\\\\\\\\___        
//_____/\\\////////___\/\\\///////////___\/\\\\\\________/\\\\\\____/\\\/////////\\\_       
//___/\\\/____________\/\\\______________\/\\\//\\\____/\\\//\\\___\//\\\______\///__      
//__/\\\______________\/\\\\\\\\\\\______\/\\\\///\\\/\\\/_\/\\\____\////\\\_________     
// _\/\\\______________\/\\\///////_______\/\\\__\///\\\/___\/\\\_______\////\\\______    
//  _\//\\\_____________\/\\\______________\/\\\____\///_____\/\\\__________\////\\\___   
//   __\///\\\___________\/\\\______________\/\\\_____________\/\\\___/\\\______\//\\\__  
//    ____\////\\\\\\\\\__\/\\\\\\\\\\\\\\\__\/\\\_____________\/\\\__\///\\\\\\\\\\\/___ 
//     _______\/////////___\///////////////___\///______________\///_____\///////////_____

/**
 * A class that manages the current login state. This is a model class which is
 * typically associated with a single session, for the duration of the session.
 * 
 * we implement authentication (and thus produce authentication changes), will
 * indicate their result in this object using the login() or logout() methods.
 * 
 * This class also react to login state changes (typically, a user logging in or
 * logout or press on X) should listen to the changed() signal of this object.
 * 
 * @author Hadar_Iluz
 *
 */
public class LoginController extends GuiCommon{

	@FXML
	private MenuItem pressQuit;

	@FXML
	private Text textAboutCEMS;

	@FXML
	private MenuItem pressAboutCEMS;

	@FXML
	private ImageView background;

	@FXML
	private TextField txtUserName;

	@FXML
	private Label reqFieldPassword;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Button btnLogin;

	@FXML
	private ImageView logo;

	@FXML
	private ImageView phone;

	@FXML
	private ImageView emil;

	@FXML
	private Label reqFieldUserName;

	@FXML
	private Font x3;

	@FXML
	private Color x4;

	final String StudentMenu = "StudentController.fxml";
	final String PrincipalMenu = "PrincipalController.fxml";
	final String TeacherMenu = "TeacherController.fxml";
	public static User user;
	private static boolean press = false; // for AboutCEMS text.

	private static StudentController studentController;
	private static PrincipalController principalController;
	private static TeacherController teacherController;

	/**
	 * @param event that occurs when clicking on 'login' button
	 * @throws IOException if failed.
	 */
	@FXML
	public void btnLogin(ActionEvent event) throws IOException {
		String userID = txtUserName.getText();
		String userPassword = txtPassword.getText();

		if (userID.trim().isEmpty()) {
			showMsg(reqFieldUserName, "User name is required field.");
		}
		if (userPassword.trim().isEmpty()) {
			showMsg(reqFieldPassword, "Password is required field.");
		}
		// in case fields not empty checks if exist in DB
		if (!userID.trim().isEmpty() && !userPassword.trim().isEmpty() && userID.length() == 9
				&& isOnlyDigits(userID)) {

			int id = Integer.parseInt(txtUserName.getText().trim());
			user = new User(id, userPassword);
			// create in 'Serializable' class my request from server.
			RequestToServer req = new RequestToServer("getUser");
			req.setRequestData(user);
			ClientUI.cems.accept(req); // send server pk(id) to DB in order to checks if user exist or not.

			if (CEMSClient.responseFromServer.getStatusMsg().getStatus().equals("USER NOT FOUND")) {
				System.out.println("press on login button and server returns: -->USER NOT FOUND");
				popUp("This user doesn`t exist in CEMS system.");
			}

			// handle case that user found and checks password
			else {
				user = (User) CEMSClient.responseFromServer.getResponseData();
				if (userPassword.equals(user.getPassword()) == false) {
					popUp("The password insert is incorrect. Please try again.");
				} else if (userID.equals(String.valueOf(user.getId())) == false) {
					popUp("The id insert is incorrect. Please try again.");
				} else {

					user.setLogged(1);

					Stage primaryStage = new Stage();
					GridPane root = new GridPane();
					@SuppressWarnings("unused")
					Scene scene = new Scene(root, 988, 586); // define screens size
					primaryStage.setTitle("CEMS-Computerized Exam Management System");
					user = (User) CEMSClient.responseFromServer.getResponseData();

					switch (user.getUserType().toString()) {

					case "Student": {

						Student student = new Student(user, 0, null);
						RequestToServer reqStudentData = new RequestToServer("getStudentData_Login");
						reqStudentData.setRequestData(student);
						ClientUI.cems.accept(reqStudentData);

						// Response from server = (Student)
						student = (Student) CEMSClient.responseFromServer.getResponseData(); // response: "STUDENT DATA"
						// Create new Student
						Student newStudent = new Student(user, student.getStudentAvg(), student.getCourses());
						ClientUI.loggedInUser = LoggedInUser.getInstance(newStudent);
						studentController = new StudentController();
						((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary(Main) window
						// call start function in studentController for initialization.
						try {
							studentController.start(new Stage());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
						break;
					case "Teacher": {

						Teacher teacher = new Teacher(user, null);
						RequestToServer reqTeacherData = new RequestToServer("getTeacherData_Login");
						reqTeacherData.setRequestData(teacher);
						ClientUI.cems.accept(reqTeacherData);

						// response from server teacher = (Teacher);
						teacher = (Teacher) CEMSClient.responseFromServer.getResponseData(); // response: "TEACHER DATA"
						// Create new teacher
						Teacher newTeacher = new Teacher(user, teacher.getProfessions());
						ClientUI.loggedInUser = LoggedInUser.getInstance(newTeacher);
						teacherController = new TeacherController();
						((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary(Main) window
						// call start function in teacherController for initialization.
						try {
							teacherController.start(new Stage());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
						break;

					case "Principal": {
						// Create new principal
						User principal = (User) CEMSClient.responseFromServer.getResponseData();
						ClientUI.loggedInUser = LoggedInUser.getInstance(principal);
						principalController = new PrincipalController();
						((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary(Main) window
						// call start function in principalController for initialization.
						try {
							principalController.start(new Stage());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
						break;

					}
					// print to console
					System.out.println(user.getId() + " login Successfully as: " + user.getUserType().toString());

					// sent to server pk(id) in order to change the login status of this user.
					RequestToServer reqLogged = new RequestToServer("UpdateUserLoggedIn");
					reqLogged.setRequestData(user);
					ClientUI.cems.accept(reqLogged);

				}

			}
			return;

		}

		// handle case that one of the parameters is invalid.
		else {
			popUp("One or more of the parameters which insert are incorrect.\n Please try again.");
			reqFieldUserName.setText("");
			reqFieldPassword.setText("");
		}
		txtUserName.clear();
		txtPassword.clear();

	}

	/**
	 * Display commend to user on the screen.
	 * 
	 * @param label
	 * @param msg
	 */
	private void showMsg(Label label, String msg) {
		label.setTextFill(Paint.valueOf("Red"));
		label.setText(msg);
	}



	/**
	 * Display HomePage after client connection.
	 * 
	 * @param primaryStage the primary stage
	 * @throws IOException when start isn't working
	 */
	public void start(Stage primaryStage) throws IOException {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui_cems/Login.fxml"));
			Scene scene = new Scene(root);
			// message to primaryStage
			primaryStage.setTitle("CEMS-Computerized Exam Management System");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

		listenToCloseWindow(primaryStage);
	}


	/**
	 * @param user object given to send as a request to server to update this user
	 *             isLogged status.
	 */
	public void performLogout(User user) {
		// sent to server pk(id) in order to change the login status of this user.
		RequestToServer reqLoged = new RequestToServer("UpdateUserLoggedOut");
		if (user instanceof User) {
			reqLoged.setRequestData(user);
		}
		if (user instanceof Student) {
			User student = new User(user.getId(), user.getUserType());
			reqLoged.setRequestData(student);
		}
		if (user instanceof Teacher) {
			User teacher = new User(user.getId(), user.getUserType());
			reqLoged.setRequestData(teacher);
		}
		ClientUI.cems.accept(reqLoged);
		ClientUI.loggedInUser.logOff();
	}

	/**
	 * @param event that displays and uploads a message when clicked on "AboutCEMS"
	 */
	@FXML
	void pressAboutCEMS(ActionEvent event) {
		textAboutCEMS.setVisible(messageAppearnce());
	}

	/**
	 * Private method for displaying and disappearing the message
	 * 
	 * @return boolean variable that specifies whether to display or hide the
	 *         message.
	 */
	private boolean messageAppearnce() {
		if (press == false)
			return press = true;
		else
			return press = false;
	}

	/**
	 * @param event that close login window and disconnect client.
	 */
	@FXML
	void pressQuit(ActionEvent event) {
		System.exit(0);
	}

	/**
	 * listen for close events on a JavaFX Stage, notified when the user clicks the
	 * button with the X on, in the upper right corner of the Stage
	 * 
	 * @param primaryStage
	 */
	private void listenToCloseWindow(Stage primaryStage) {

		primaryStage.setOnCloseRequest((event) -> {
			System.out.println("Closing Stage");
			RequestToServer reqLogged = new RequestToServer("ClientDisconected");
			reqLogged.setRequestData("ClientDisconected_from_login_fram");
			ClientUI.cems.accept(reqLogged);
			if (CEMSClient.responseFromServer.getResponseType().equals("USER LOGOUT")) {

			}

		});

	}

}
