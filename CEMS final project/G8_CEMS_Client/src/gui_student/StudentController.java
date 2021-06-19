package gui_student;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.ActiveExam;
import entity.Student;
import gui_cems.LoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.RequestToServer;

/**
 * The class included in the diagrams and contains all the functionality
 * that the start has, manages the left menu in the system and describes the
 * privileges that the student has in the system.
 * 
 * @author Hadar Iluz
 */
public class StudentController extends Application implements Initializable {

    @FXML
    private Label pressLogout;

    @FXML
    private ImageView imgStudent;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label textHelloStudent;

    @FXML
    private Label textStudentName;

    @FXML
    private Button btnStarExam;

    @FXML
    private ImageView imgPhone;

    @FXML
    private ImageView imgEmail;

    @FXML
    private Button btnViewExamInfo;

	private static LoginController loginController;
	protected Student student;
	public static GridPane root;
	public Scene scene;

	/**
	 *Initializes the controller at the moment it loads.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		student = (Student) ClientUI.loggedInUser.getUser();
		textStudentName.setText(student.getFirstName() + " " + student.getLastName());
	}

	/**
	 * @param primaryStage that display the screen and load fxml.
	 * @throws Exception which thrown in case the screen cannot be loaded.
	 */
	public void start(Stage primaryStage) throws Exception {
		root = new GridPane();
		scene = new Scene(root, 980, 580);// SCREENS size
		Pane newMnueLeft = FXMLLoader.load(getClass().getResource("StudentMenuLeft.fxml"));
		root.add(newMnueLeft, 0, 0);
		primaryStage.setTitle("CEMS-Computerized Exam Management System");
		primaryStage.setScene(scene);
		primaryStage.show();

		listenToCloseWindow(primaryStage);

	}

	/**
	 * @param event that loading the student's right screen after pressing a button "Start Exam".
	 */
	@FXML
	void btnStarExam(ActionEvent event) {
		try {
			EnterToExamController.setAllActiveExamBeforEnterToExam(getAllActiveExamBeforEnterToExam());
			btnStarExam.setDisable(true);
			btnViewExamInfo.setDisable(false);
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("EnterToExam.fxml"));
			root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param event that loading the student's right screen after pressing a button.
	 */
	@FXML
	void btnViewExamInfo(ActionEvent event) {
		try {
			btnViewExamInfo.setDisable(true);
			btnStarExam.setDisable(false);
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("ViewExam.fxml"));
			root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Event Logout that occur when clicking on logout at the left menu
	 * 
	 * @param event that display pop up message and ask if he want to logout.
	 */
	@FXML
	void pressLogout(MouseEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setContentText("Are you sure you want to logout ?");
		ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonData.NO);

		alert.getButtonTypes().setAll(okButton, noButton);
		alert.showAndWait().ifPresent(type -> {
			if (type == okButton) {	
				loginController = new LoginController();
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary(Main) window
				try {
					loginController.start(new Stage());
				} catch (IOException e) {
					e.printStackTrace();
				}
				loginController.performLogout(student);

			}
		});

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
			reqLogged.setRequestData(ClientUI.loggedInUser.getUser());
			ClientUI.cems.accept(reqLogged);
			// "USER LOGOUT"
			if (CEMSClient.responseFromServer.getResponseType().equals("USER LOGOUT")) {
				System.exit(0);
			}

		});

	}
	
	/**
	 * @return List of all active tests received as answer from server.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<ActiveExam> getAllActiveExamBeforEnterToExam() {
		
		ArrayList<ActiveExam> activeExamList= new ArrayList<ActiveExam>();
		RequestToServer req = new RequestToServer("getAllActiveExamBeforEnterToExam");
		ClientUI.cems.accept(req);
		activeExamList= (ArrayList<ActiveExam>) CEMSClient.responseFromServer.getResponseData();
		return activeExamList;
	}
		

}
