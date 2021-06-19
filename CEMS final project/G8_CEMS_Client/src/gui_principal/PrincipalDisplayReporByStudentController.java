package gui_principal;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import gui_cems.GuiCommon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import logic.RequestToServer;

/*
 * The class is included in the diagrams and contains all the functionality
 * that the principal needs to do to see the requested report. This class
 * conducts various calculations for presenting the report.
 * 
 * @author Yadin Amsalem
 * @author Nadav Dery
 *
 */
public class PrincipalDisplayReporByStudentController extends GuiCommon implements Initializable {

	@FXML
	private BarChart<?, ?> ExamsHisto;

	@FXML
	private CategoryAxis ca;

	@FXML
	private NumberAxis na;

	@FXML
	private Label StudentIDLabel;

	@FXML
	private Label StudentNameLabel;

	@FXML
	private Label StudentAvgLabel;

	@FXML
	private Label StudentMedianLabel;

	HashMap<String, Integer> ExamGrades;

	/**
	 * This method initialize the histogram of specific student with the grades of
	 * all the exam he took.
	 * 
	 * @param location of javafx
	 * @param resources of javafx
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		StudentIDLabel.setText(String.valueOf(PrincipalGetReportsController.Id));
		StudentNameLabel.setText(PrincipalGetReportsController.UserName);
		RequestToServer req = new RequestToServer("getStudentGrades");
		req.setRequestData(PrincipalGetReportsController.Id);
		ClientUI.cems.accept(req);
		ExamGrades = new HashMap<String, Integer>();
		ExamGrades = (HashMap<String, Integer>) CEMSClient.responseFromServer.getResponseData();
		XYChart.Series chart = new XYChart.Series();
		if (!ExamGrades.isEmpty()) {
			calcAvgAndMedian();
			chart.setName("Score");
			for (String curr : ExamGrades.keySet())
				chart.getData().add(new XYChart.Data(curr, ExamGrades.get(curr)));
		} else
			popUp("This Student Doesn't Have Any Solved Exam");
		ExamsHisto.getData().add(chart);
	}

	/**
	 * This method calculate the average and the median of the student grades.
	 */
	private void calcAvgAndMedian() {

		ArrayList<Integer> grades = new ArrayList<Integer>();
		for (Integer curr : ExamGrades.values())
			grades.add(curr);
		Collections.sort(grades);

		if (grades.size() % 2 == 0) {
			int first, second;
			first = grades.size() / 2 - 1;
			second = (grades.size() + 2) / 2 - 1;
			StudentMedianLabel.setText(String.valueOf(((float) grades.get(first) + grades.get(second)) / 2));
		} else
			StudentMedianLabel.setText(String.valueOf((float) grades.get((grades.size() + 1) / 2 - 1)));
		float sum = 0;
		for (Integer a : grades)
			sum += a;
		sum /= grades.size();
		StudentAvgLabel.setText(new DecimalFormat("##.##").format(sum)); //shows only 2 digits after the decimal point
	}

}
