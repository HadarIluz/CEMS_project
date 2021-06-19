
package gui_principal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.CEMSClient;
import client.ClientUI;
import entity.User;
import gui_cems.GuiCommon;
import gui_cems.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.RequestToServer;

/**
 * The class included in the diagrams and contains all the functionality
 * that the principal has, manages the left menu in the system and describes the
 * privileges that the principal has in the system.
 * 
 * @author Hadar_Iluz
 *
 */
public class PrincipalController extends Application implements Initializable {

	@FXML
	private ImageView person;

	@FXML
	private ImageView logo;

	@FXML
	private Label lblUserName;

	@FXML
	private ImageView phone;

	@FXML
	private ImageView messageContactUs;

	@FXML
	private Button btnGetReports;

	@FXML
	private Button btnApproveTimeExtension;

	@FXML
	private Button btnViewInfo;

	@FXML
	private Label pressLogout;

	@FXML
	private static ImageView msgRequest;

	@FXML
	private static Label lblNew;

	public LoginController loginController;
	protected User principal;
	public static GridPane root;
	public Scene scene;
	@SuppressWarnings("unused")
	static ColorAdjust colorAdjust = new ColorAdjust();
	private static String msgOfNotification = null;
	private String saveMsg;
	private Timer timer;



	/**
	 * The method that initializes the screen is currently loading
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		principal = ClientUI.loggedInUser.getUser();
		lblUserName.setText(principal.getFirstName() + " " + principal.getLastName());
		
		timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				if (msgOfNotification != null) {
					
					saveMsg=msgOfNotification;
					
					Platform.runLater(() -> GuiCommon.popUp(saveMsg));

					msgOfNotification = null;
				}

			}
		}, 0, 1000);
	}

	/**
	 * @param primaryStage the primary stage
	 * @throws Exception when stage not working
	 */
	public void start(Stage primaryStage) throws Exception {
		root = new GridPane();
		scene = new Scene(root, 980, 580);// SCREENS size
		Pane newMnueLeft = FXMLLoader.load(getClass().getResource("PrincipalMenuLeft.fxml"));
		root.add(newMnueLeft, 0, 0);
		primaryStage.setTitle("CEMS-Computerized Exam Management System");
		primaryStage.setScene(scene);
		primaryStage.show();

		listenToCloseWindow(primaryStage);
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
	 * @param event that loading the principal right screen after pressing a button.
	 */
	@FXML
	void btnApproveTimeExtension(ActionEvent event) {
		try {
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("ApprovalTimeExtension.fxml"));
			root.add(newPaneRight, 1, 0);
		} catch (IOException e) {
			System.out.println("Couldn't load- ApprovalTimeExtension.fxml");
			e.printStackTrace();
		}

	}

	/**
	 * @param event that loading the teacher's right screen after pressing a button
	 */
	@FXML
	void btnGetReports(ActionEvent event) {
		try {
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("PrincipalGetReports.fxml"));
			root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			System.out.println("Couldn't load- PrincipalGetReports.fxml");
			e.printStackTrace();
		}
	}

	/**
	 * @param event that load 'QuestionBank' screen in order to allow principal to
	 *              see data stored in CEMS system. We use teacher screens to
	 *              configure them in a way that will not be editable by principal.
	 */
	@FXML
	void btnViewExamBankinfo(ActionEvent event) {

		try {
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("/gui_teacher/ExamBank.fxml"));
			root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			System.out.println("Couldn't load fxml");
			e.printStackTrace();
		}

	}

	/**
	 * @param event that load 'QuestionBank' screen in order to allow principal to
	 *              see data stored in CEMS system. We use teacher screens to
	 *              configure them in a way that will not be editable by principal.
	 */
	@FXML
	void btnViewQuestionBankinfo(ActionEvent event) {
		try {
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("/gui_teacher/QuestionBank.fxml"));
			root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			System.out.println("Couldn't load fxml");
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

				loginController.performLogout(principal);

			}
		});

	}
	
	/**
	 * @param msg
	 * saves a messages from another process in the system. the run thread will check for this message
	 */
	public static void CopyAlertNotification(String msg) {
		msgOfNotification = msg;
	}

}
