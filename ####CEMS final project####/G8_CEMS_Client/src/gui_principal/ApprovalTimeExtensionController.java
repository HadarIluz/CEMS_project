package gui_principal;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import entity.ExtensionRequest;
import gui_cems.GuiCommon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logic.RequestToServer;

/**
 * The class contains the functionality of approving the extra time of an
 * active exam that solve now by group of students. 
 * The request is sent from the teacher to the principal. 
 * The principal receives a real-time alert when she
 * has a new request and thus knows to enter this screen.
 * 
 * All active tests will be presented to the principal, if the principal enters
 * and the test ends the application is irrelevant and therefore will not be
 * presented.
 * 
 * @author Matar Asaf
 *
 */

public class ApprovalTimeExtensionController implements Initializable {

	@FXML
	private ComboBox<String> selectExamExtension;

	@FXML
	private Label lblAdditionalTime;

	@FXML
	private TextArea textReasonField;

	@FXML
	private Button btnDecline;

	@FXML
	private Button btnApprove;

	private static HashMap<String, ExtensionRequest> extensionRequestMap = new HashMap<String, ExtensionRequest>();
	private static ArrayList<ExtensionRequest> extensionRequestList = new ArrayList<ExtensionRequest>();
	private ArrayList<String> examIdList = new ArrayList<String>();
	private ExtensionRequest selectedExtensionRequest;

	/**
	 * @param event that occurs when clicking on 'Approve' button
	 */
	@FXML
	void btnApprove(ActionEvent event) {
		// When no test is selected
		if (selectedExtensionRequest == null) {
			GuiCommon.popUp("Please choose a exam extension.");
		}
		// When a test is selected
		else {
			// Adding the time required for the exam time
			selectedExtensionRequest.getActiveExam()
					.setExtraTime(Integer.parseInt(selectedExtensionRequest.getAdditionalTime()));
			selectedExtensionRequest.getActiveExam()
					.setTimeAllotedForTest((selectedExtensionRequest.getActiveExam().getTimeAllotedForTest()
							+ selectedExtensionRequest.getActiveExam().getExtraTime()) + "");
			// Update the exam time and delete the extension Request in the database
			RequestToServer req = new RequestToServer("approvalTimeExtension");
			req.setRequestData(selectedExtensionRequest.getActiveExam());
			ClientUI.cems.accept(req);
			if (CEMSClient.responseFromServer.getStatusMsg().getStatus().equals("EXTENSION REMOVED")) {
				GuiCommon.popUp("The time to take the exam has been updated.");
				refreshFunc();
			}
		}
	}

	/**
	 * @param event that occurs when clicking on 'Decline' button
	 */
	@FXML
	void btnDecline(ActionEvent event) {
		// When no test is selected
		if (selectedExtensionRequest == null) {
			GuiCommon.popUp("Please choose a exam extension.");
		}
		// When a test is selected
		else {
			RequestToServer req = new RequestToServer("declineTimeExtension");
			req.setRequestData(selectedExtensionRequest.getActiveExam());
			ClientUI.cems.accept(req);
			if (CEMSClient.responseFromServer.getStatusMsg().getStatus().equals("EXTENSION REMOVED")) {
				GuiCommon.popUp("The time to take the exam has not changed.");
				refreshFunc();
			}
		}
	}

	/**
	 * @param event that occurs when principal select test with extension request
	 *              from the comboBox.
	 */
	@FXML
	void selectExamExtension(ActionEvent event) {
		if (extensionRequestMap.containsKey(selectExamExtension.getValue())) {
			selectedExtensionRequest = extensionRequestMap.get(selectExamExtension.getValue());
			lblAdditionalTime.setText(selectedExtensionRequest.getAdditionalTime());
			textReasonField.setText(selectedExtensionRequest.getReason());
		}
	}

	/**
	 * The method prepares the list of extension requests and loads them into the
	 * comboBox.
	 */
	@FXML
	public void loadExamExtensionsToCombobox() {
		setExtensionRequestMap(extensionRequestList);
		for (ExtensionRequest ex : extensionRequestList)
			examIdList.add(ex.getActiveExam().getExam().getExamID());
		selectExamExtension.setItems(FXCollections.observableList(examIdList));
	}

	/**
	 * initialize function to prepare the screen after it is loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		refreshFunc();
	}

	/**
	 * @param extensionRequestList containing all the extension requests in the DB.
	 */
	public static void setExtensionRequestMap(ArrayList<ExtensionRequest> extensionRequestList) {
		for (ExtensionRequest ex : extensionRequestList) {
			extensionRequestMap.put(ex.getActiveExam().getExam().getExamID(), ex);
		}
	}

	/**
	 * when principal approves or rejects a request this function deletes the
	 * information of the previous request and updates the comboBox.
	 */
	@SuppressWarnings("unchecked")
	void refreshFunc() {
		selectExamExtension.getItems().clear();
		RequestToServer req = new RequestToServer("getExtensionRequests");
		ClientUI.cems.accept(req);
		extensionRequestList = (ArrayList<ExtensionRequest>) CEMSClient.responseFromServer.getResponseData();
		selectedExtensionRequest = null;
		loadExamExtensionsToCombobox();
		lblAdditionalTime.setText("");
		textReasonField.setText("");
	}

}
