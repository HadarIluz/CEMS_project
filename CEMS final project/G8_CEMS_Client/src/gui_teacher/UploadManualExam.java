package gui_teacher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import client.CEMSClient;
import client.ClientUI;
import common.MyFile;
import entity.Exam;
import gui_cems.GuiCommon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import logic.RequestToServer;

/**
 * class allow to user to upload manual exam to the system
 * @author Yadin Amsalem
 * @author Nadav Dery
 * 
 */
public class UploadManualExam extends GuiCommon {

	@FXML
	private Button btnBrowse;

	@FXML
	private TextField txtPath;

	@FXML
	private Button btnUpload;

	@FXML
	private Button btnBack;

	@FXML
	private Label msgLabel;

	@FXML
	private Label examIDlbls;

	@FXML
	private Label ExamIDLAbel;

	private static Exam newExam;
	
	private File selectedExamFile;
	
	/**
	 *  method return new exam that save in "newExam" static variable
	 * @return new Exam from type Exam
	 */
	

	public static Exam getNewExam() {
		return newExam;
	}
	/**
	 * method set new exam  in "newExam" static variable
	 * @param newExam to set in "newExam" variable
	 */
	

	public static void setNewExam(Exam newExam) {
		UploadManualExam.newExam = newExam;
	}
/**
 * method return user to previous screen
 * @param event occurs when pressing "Back" button
 */
	
	
	@FXML
	void btnBack(ActionEvent event) {
		try {
			Pane newPaneRight = FXMLLoader.load(getClass().getResource("CreateExam_step1.fxml"));
			newPaneRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			TeacherController.root.add(newPaneRight, 1, 0);

		} catch (IOException e) {
			System.out.println("Couldn't load!");
			e.printStackTrace();
		}
	}
	
	/**
	 * method create new manual exam and save in system
	 * @param event occurs when pressing "Upload" button
	 */

	@SuppressWarnings("resource")
	@FXML
	void btnUploadPress(ActionEvent event) {
		if(!selectedExamFile.getAbsolutePath().endsWith(".docx")) {
			popUp("You Must Upload Word Document !");
			return;
		}
		RequestToServer req = new RequestToServer("createNewExam");
		req.setRequestData(newExam);
		ClientUI.cems.accept(req);
		String NewExamID = (String) CEMSClient.responseFromServer.getResponseData();
		if (NewExamID.length() == 6) {
			MyFile uploadFile = new MyFile(NewExamID+"_exam.docx");
			try {
				byte[] bytes = new byte[(int) selectedExamFile.length()];	

				FileInputStream fis = new FileInputStream(selectedExamFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				uploadFile.initArray(bytes.length);
				uploadFile.setSize(bytes.length);
				bis.read(uploadFile.getMybytearray(), 0, bytes.length);
				fis.close();

			} catch (IOException e) {
			popUp("Upload Failed.");
			return;
			}
			
			RequestToServer req2 = new RequestToServer("submitManualExam");
			req2.setRequestData(uploadFile);
			ClientUI.cems.accept(req2);
			if(CEMSClient.responseFromServer.getResponseType().equals("SUBMIT EXAM")) {
			msgLabel.setTextFill(Color.GREEN);
			msgLabel.setText("File Uploaded Successfully");
			msgLabel.setVisible(true);
			examIDlbls.setVisible(true);
			ExamIDLAbel.setText(NewExamID);
			ExamIDLAbel.setVisible(true);
			btnBack.setDisable(true);
			btnBrowse.setDisable(true);
			btnUpload.setDisable(true);
			}else {
				msgLabel.setTextFill(Color.RED);
				msgLabel.setText("File Upload Failed");
				msgLabel.setVisible(true);
			}
		} else {
			msgLabel.setTextFill(Color.RED);
			msgLabel.setText("File Upload Failed");
			msgLabel.setVisible(true);
		}
	}

	/**
	 * method to choose file from the user's computer
	 * @param event occurs when pressing "Browse File" button
	 */
	
	
	@FXML
	void onClickBroswe(ActionEvent event) {
		FileChooser fc = new FileChooser();
		selectedExamFile = fc.showOpenDialog(null);
		if (selectedExamFile != null) {
			txtPath.setText(selectedExamFile.getAbsolutePath());

		} 
		else
			popUp("File is not valid !");

	}

}
