package gui_teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import client.CEMSClient;
import client.ClientUI;
import entity.ProfessionCourseName;
import gui_cems.GuiCommon;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import logic.RequestToServer;

/**
 * Class used as controller for the screen of teacher's statistics.
 * 
 * @author Nadav Dery and Yadin Amsalem
 * @version 1.0 10/06/2021
 */

public class TeacherStatisticsController extends GuiCommon {

	@FXML
	private TextField textExamID;

	@FXML
	private Label textMedian;

	@FXML
	private Label textCourse;

	@FXML
	private BarChart<String, Number> ExamHisto;

	@FXML
	private Button btnShowStatistic;

	@FXML
	private Label textErrorMessage;

	@FXML
	private Label textProfession;

	@FXML
	private Label textAverage;

	boolean flag = true; // true when we need to initialize the histogram with chart and false when we
							// already did that.

	@SuppressWarnings("rawtypes")
	private XYChart.Series chart = new XYChart.Series();// table of x and y

	@SuppressWarnings("unused")
	private TeacherController teacherController; // we will use it for load the next screen ! (using root).

	ArrayList<Integer> grades;

	HashMap<String, String> ProfName = new HashMap<String, String>();
	// profID,profName
	HashMap<String, ProfessionCourseName> ProfCourseName = new HashMap<String, ProfessionCourseName>();

	// profID,CourseID+name
	/**
	 * method check the if the exam id given by the user is legal and present
	 * Histogram of grades
	 * 
	 * @param event occurs when clicking on button " Show"
	 */
	@FXML
	void btnShowStatistic(MouseEvent event) {
		String ExamID = textExamID.getText();
		String ProfID = "" + ExamID.charAt(0) + ExamID.charAt(1);
		String CourseID = "" + ExamID.charAt(2) + ExamID.charAt(3);
		if (!checkForLegalID(ExamID))
			return;
		if (!checkExamExist(ExamID))
			return;

		getProffesionsNames();
		getCoursesNames();
		textProfession.setText(ProfName.get(ProfID) + "(" + ProfID + ")");
		textCourse.setText(ProfCourseName.get(ProfID).getCourses().get(CourseID) + "(" + CourseID + ")");
		textAverage.setText(String.valueOf(gradesAverageCalc(ExamID)));
		setGradesInHistogram(ExamID);
	}
	/**
	 * set all grades in histogram
	 * @param ExamID use for set Exam id in Histogram
	 */
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setGradesInHistogram(String ExamID) {
		chart.getData().clear();
		int[] amountOfstudents = new int[10];
		for (Integer curr : grades) {
			if (curr >= 0 && curr <= 10)
				amountOfstudents[0]++;
			else if (curr >= 11 && curr <= 20)
				amountOfstudents[1]++;
			else if (curr >= 21 && curr <= 30)
				amountOfstudents[2]++;
			else if (curr >= 31 && curr <= 40)
				amountOfstudents[3]++;
			else if (curr >= 41 && curr <= 50)
				amountOfstudents[4]++;
			else if (curr >= 51 && curr <= 60)
				amountOfstudents[5]++;
			else if (curr >= 61 && curr <= 70)
				amountOfstudents[6]++;
			else if (curr >= 71 && curr <= 80)
				amountOfstudents[7]++;
			else if (curr >= 81 && curr <= 90)
				amountOfstudents[8]++;
			else if (curr >= 91 && curr <= 100)
				amountOfstudents[9]++;
		}

		chart.getData().add(new XYChart.Data("0-10", amountOfstudents[0]));
		chart.getData().add(new XYChart.Data("11-20", amountOfstudents[1]));
		chart.getData().add(new XYChart.Data("21-30", amountOfstudents[2]));
		chart.getData().add(new XYChart.Data("31-40", amountOfstudents[3]));
		chart.getData().add(new XYChart.Data("41-50", amountOfstudents[4]));
		chart.getData().add(new XYChart.Data("51-60", amountOfstudents[5]));
		chart.getData().add(new XYChart.Data("61-70", amountOfstudents[6]));
		chart.getData().add(new XYChart.Data("71-80", amountOfstudents[7]));
		chart.getData().add(new XYChart.Data("81-90", amountOfstudents[8]));
		chart.getData().add(new XYChart.Data("91-100", amountOfstudents[9]));

		if (flag == true) {
			flag = false;
			chart.setName("Amount of student");
			ExamHisto.getData().add(chart);
		}

	}
/**
 * method calculate median of grades
 * @param grades list of all grade 
 */
	public void medianCalc(ArrayList<Integer> grades) {

		Collections.sort(grades);

		if (grades.size() % 2 == 0) {
			int first, second;
			first = grades.size() / 2 - 1;
			second = (grades.size() + 2) / 2 - 1;
			textMedian.setText(String.valueOf(((float) grades.get(first) + grades.get(second)) / 2));
		} else
			textMedian.setText(String.valueOf((float) grades.get((grades.size() + 1) / 2 - 1)));

	}
/**
 * method calculate average of grades
 * @param ExamID use for Exam id for the grades given
 * @return average of grades
 */
	
	
	@SuppressWarnings("unchecked")
	public float gradesAverageCalc(String ExamID) {

		float sum = 0;
		RequestToServer req = new RequestToServer("gradesAverageCalc");
		req.setRequestData(ExamID);
		ClientUI.cems.accept(req);
		grades = (ArrayList<Integer>) CEMSClient.responseFromServer.getResponseData();
		for (Integer a : grades)
			sum += a;
		sum /= grades.size();
		medianCalc(grades);
		return sum;
	}
/**
 * methods set in HashMap details of profession Names with profession id as key
 */
	@SuppressWarnings("unchecked")
	public void getProffesionsNames() {
		RequestToServer req = new RequestToServer("getProfNames");
		ClientUI.cems.accept(req);
		ProfName = (HashMap<String, String>) CEMSClient.responseFromServer.getResponseData();
	}
/**
 * method get all courses names 
 */
	@SuppressWarnings("unchecked")
	public void getCoursesNames() {
		RequestToServer req = new RequestToServer("getCoursesNames");
		ClientUI.cems.accept(req);
		ProfCourseName = (HashMap<String, ProfessionCourseName>) CEMSClient.responseFromServer.getResponseData();
	}
	/**method checl if exam Id is exist
	 * 
	 * @param ExamID for check if exist
	 * @return true if exist, else return false
	 */

	public boolean checkExamExist(String ExamID) {
		boolean isExsit = true;
		RequestToServer req = new RequestToServer("checkExamExist");
		req.setRequestData(ExamID);
		ClientUI.cems.accept(req);
		if (CEMSClient.responseFromServer.getResponseData().equals("FALSE")) {
			popUp("No detail for that exam!");
			isExsit = false;
		}
		return isExsit;

	}

}
