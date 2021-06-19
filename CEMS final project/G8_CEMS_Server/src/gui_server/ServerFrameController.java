package gui_server;

import java.io.IOException;

import Server.ServerUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The class included in diagrams and contains functionality that supports
 * displaying a log to the screen, such as alerts, receiving information and
 * requesting information from the server to the client and system errors.
 * 
 * @author CEMS_TEAM
 *
 */
public class ServerFrameController {

	final public static String DEFAULT_PORT = "5555";

	@FXML
	private VBox rootOfMain;

	@FXML
	private MenuBar manubar;

	@FXML
	private SplitPane paneSplitter;

	@FXML
	private Pane serevrUserInteraction;

	@FXML
	private TextField portxt;

	@FXML
	private Button btnStartServer;

	@FXML
	private Button btnStop;

	@FXML
	private Button btnExit;

	@FXML
	private TextArea txtArea;

	@FXML
	private Button ClearLogBtn;

	@FXML
	private Font x3;

	private boolean IsConnected = false;

	private String getPort() {
		return portxt.getText();
	}

	/**
	 * @param event that occurs when the user presses on stop button, the server
	 *              stops listening
	 * @throws Exception that indicates conditions that a reasonableapplication
	 *                   might want to catch.
	 */
	public void pressStopServerBtn(ActionEvent event) throws Exception {
		String port = getPort();
		if (port.trim().isEmpty())
			ServerUI.closeServer(DEFAULT_PORT, this);
		else
			ServerUI.closeServer(port, this);
		btnStartServer.setDisable(false);
		btnStop.setDisable(true);
		IsConnected = false;

	}

	/**
	 * @param event that occurs when the user presses on start button, the server
	 *              start listening
	 * @throws Exception that indicates conditions that a reasonableapplication
	 *                   might want to catch.
	 */
	public void pressStartServerBtn(ActionEvent event) throws Exception {
		String p;
		p = getPort();
		txtArea.setEditable(false); // user can not write in text area
		if (p.trim().isEmpty()) {
			ServerUI.runServer(DEFAULT_PORT, this);// default port
			btnStartServer.setDisable(true);
			btnStop.setDisable(false);

		} else {
			ServerUI.runServer(p, this);
			btnStartServer.setDisable(true);
			btnStop.setDisable(false);
		}
		IsConnected = true;

	}

	/**
	 * @param event that occurs when the user presses on exit button, the server
	 *              stops listening and exit.
	 * @throws InterruptedException that indicates conditions that a
	 *                              reasonable application might want to catch.
	 */
	@FXML
	void ExitFromServer(ActionEvent event) throws InterruptedException {
		if (IsConnected) {
			btnStop.fire();
		}
		System.exit(0);

	}

	/**
	 * @param event that occurs when the user presses on exit button, the server clear all log in the test area.
	 * @throws Exception when the txtArea update failed
	 */
	public void ClearLogTextArea(ActionEvent event) throws Exception {
		txtArea.clear();
		txtArea.setPromptText("");// clear the opening sentence of the text area
	}

	/**
	 * @param primaryStage that display the screen and load fxml.
	 * @throws IOException exception which thrown in case the screen cannot be loaded.
	 */
	public void start(Stage primaryStage) throws IOException {
		Pane root = FXMLLoader.load(getClass().getResource("/gui_server/ServerGUI.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param msg string message that print into log test area.
	 */
	public void printToTextArea(String msg) {
		txtArea.appendText(msg + "\n");
	}

}