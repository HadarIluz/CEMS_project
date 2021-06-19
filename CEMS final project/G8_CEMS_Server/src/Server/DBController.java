package Server;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import entity.ActiveExam;
import entity.Course;
import entity.Exam;
import entity.ExamOfStudent;
import entity.ExamStatus;
import entity.ExtensionRequest;
import entity.Profession;
import entity.ProfessionCourseName;
import entity.Question;
import entity.QuestionInExam;
import entity.QuestionRow;
import entity.Student;
import entity.Teacher;
import entity.UpdateScoreRequest;
import entity.User;
import entity.UserType;
import gui_server.ServerFrameController;
import logic.ResponseFromServer;

/**
 * @author CEMS_Team
 *
 */
public class DBController {

	public Connection conn;
	public ServerFrameController serverFrame;

	public void connectDB(ServerFrameController serverFrame) {
		try {
			this.serverFrame = serverFrame;
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			serverFrame.printToTextArea("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			serverFrame.printToTextArea("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cems?useTime_zone=false", "root",
					"Aa123456");
			serverFrame.printToTextArea("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
			serverFrame.printToTextArea("SQLState: " + ex.getSQLState());
			serverFrame.printToTextArea("VendorError: " + ex.getErrorCode());
		}
	}

	/*
	 * checks if the user that try to login exists in the DB
	 * 
	 * @param obj of user which include student id to verify if exists.
	 */
	public ResponseFromServer verifyLoginUser(User obj) {

		User existUser = obj;
		ResponseFromServer response = null;
		existUser.setPassword(null); // put null in order to check at the end if user found or not by this id.
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM user WHERE id=?");
			pstmt.setInt(1, existUser.getId());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				existUser.setPassword(rs.getString(2));
				existUser.setFirstName(rs.getString(3));
				existUser.setLastName(rs.getString(4));
				existUser.setEmail(rs.getString(5));
				existUser.setUserType(UserType.valueOf(rs.getString(6)));
				rs.close();
			}

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		// in case not found any user match,
		// response ready to client with StatusMsg and
		// 'Object responseData', in case user found existUser include all data,
		// otherwise null.
		if (existUser.getPassword() != null) {
			response = new ResponseFromServer("USER FOUND IN CEMS SYSTEM");
			response.getStatusMsg().setStatus("USER FOUND");
		} else {
			response = new ResponseFromServer("USER NOT FOUND IN CEMS SYSTEM");
			response.getStatusMsg().setStatus("USER NOT FOUND");
		}
		response.setResponseData(existUser);
		return response;
	}

	/**
	 * We know that this student exists and we bring the additional data he has in
	 * addition to the user.
	 * 
	 * @param student include all data of student that hold by User object.
	 * @return student with data
	 */
	public Student getStudentData_Logged(Student student) {

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT AVG FROM student WHERE id=?;");
			pstmt.setInt(1, student.getId());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				student.setStudentAvg(rs.getFloat(1));
				rs.close();
			}

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		return student;

	}

	/**
	 * @param student include all data of this logged student.
	 * @return student with data
	 */
	public Student getStudentCourses_Logged(Student student) {

		ArrayList<Course> coursesOfStudent = new ArrayList<Course>();
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM student_in_course WHERE student=?;");
			pstmt.setInt(1, student.getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Course newCourse = new Course(rs.getString(2));
				coursesOfStudent.add(newCourse); // add to list.
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		student.setCourses(coursesOfStudent);
		return student;
	}

	/**
	 * @param teacher include all data of this logged teacher.
	 * @return professionIDs with all the professions of the teacher
	 */
	public ArrayList<String> getTeacherProfessionIDs(Teacher teacher) {

		ArrayList<String> professionIDs = new ArrayList<String>();

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT profession FROM teacher_in_profession WHERE teacher=?;");
			pstmt.setInt(1, teacher.getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				professionIDs.add(rs.getString(1)); // add to list
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return professionIDs;
	}

	/**
	 * @param req to update score of student exam
	 * @return true id updated successfully and other return false
	 *
	 */
	public String UpdateScoreOfStudent(UpdateScoreRequest req) {

		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(
					"UPDATE exam_of_student SET score = ?,updateReason=? WHERE student=? AND exam=?;");
			pstmt.setInt(1, req.getUpdatedScore());
			pstmt.setString(2, req.getReasonOfUpdate());
			pstmt.setString(3, req.getStudentID());
			pstmt.setString(4, req.getExamID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return "FALSE";
		}
		return "TRUE";
	}

	/**
	 * Method return all students scores by examID given from User
	 * 
	 * @param examID the Exam we return his scores
	 * @return stdScore hashmap with a key of students id and exam score in value
	 */

	public HashMap<String, Integer> SetDetailsForScoreApprovel(String examID) {
		HashMap<String, Integer> stdScore = new HashMap<>();

		PreparedStatement pstmt;
		try {

			pstmt = conn.prepareStatement("SELECT * FROM exam_of_student  WHERE exam=?");
			pstmt.setString(1, examID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				stdScore.put(rs.getString(1), rs.getInt(4));
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stdScore;

	}

	/**
	 * @param question inserts new question to DB
	 * @return true if the question was created. Otherwise false
	 */
	public boolean createNewQuestion(Question question) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("INSERT INTO question VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			pstmt.setInt(1, question.getTeacher().getId());
			pstmt.setString(2, question.getQuestionID());
			pstmt.setString(3, question.getProfession().getProfessionID());
			pstmt.setString(4, question.getQuestion());
			pstmt.setString(5, question.getAnswers()[0]);
			pstmt.setString(6, question.getAnswers()[1]);
			pstmt.setString(7, question.getAnswers()[2]);
			pstmt.setString(8, question.getAnswers()[3]);
			pstmt.setInt(9, question.getCorrectAnswerIndex());
			pstmt.setString(10, question.getDescription());

			if (pstmt.executeUpdate() != 0) {
				return true;
			}
			// to do something with status
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @param professionID which includes profession id
	 * @return int, the number of questions in this profession
	 */
	public int getNumOfQuestionsInProfession(String professionID) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT SUM(profession=?) as sum FROM question;");
			pstmt.setString(1, professionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int x = rs.getInt(1);
				return x;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param courseID which includes course id
	 * @return the number of exams with this courseID
	 */
	public int getNumOfExamsInCourse(String courseID) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT SUM(course=?) as sum FROM exam;");
			pstmt.setString(1, courseID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int x = rs.getInt(1);
				return x;
			}
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * @param exam which contains information for exam
	 * @return true/false if creating a new exam in DB succeeded
	 */
	public boolean createNewExam(Exam exam) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("INSERT INTO exam VALUES(?, ?, ?, ?, ?, ?, ?,?,?);");
			pstmt.setString(1, exam.getExamID());
			pstmt.setString(2, exam.getProfession().getProfessionID());
			pstmt.setString(3, exam.getCourse().getCourseID());
			pstmt.setInt(4, exam.getTimeOfExam());
			pstmt.setString(5, exam.getCommentForTeacher());
			pstmt.setString(6, exam.getCommentForStudents());
			pstmt.setInt(7, exam.getAuthor().getId());
			pstmt.setObject(8, exam.getExamStatus().toString());
			pstmt.setString(9, exam.getActiveExamType());
			if (pstmt.executeUpdate() == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}

	/**
	 * @param examID                  which includes exam id
	 * @param examQuestionsWithScores arrayList Of all the questions we would like
	 *                                to add to exam
	 * @return true/false if inserting all questions and scores of exam with examID
	 *         into table question_in_exam succeeded
	 */
	public boolean addQuestionsInExam(String examID, ArrayList<QuestionInExam> examQuestionsWithScores) {
		PreparedStatement pstmt;
		try {
			for (QuestionInExam q : examQuestionsWithScores) {
				pstmt = conn.prepareStatement("INSERT INTO question_in_exam VALUES(?, ?, ?);");
				pstmt.setString(1, q.getQuestion().getQuestionID());
				pstmt.setInt(2, q.getScore());
				pstmt.setString(3, examID);
				if (pstmt.executeUpdate() != 1) {
					return false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * check if the activeExam exist in the DB
	 * 
	 * @param obj of ActiveExam which include exam to verify if exists.
	 * @return response to client
	 */
	public ResponseFromServer verifyActiveExam(Object obj) {
		ActiveExam existActiveExam = (ActiveExam) obj;
		ResponseFromServer response = null;
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT startTime,timeAllotedForTest,examCode FROM active_exam WHERE exam=?");
			pstmt.setString(1, existActiveExam.getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				existActiveExam.setStartTime(rs.getTime(1));
				existActiveExam.setTimeAllotedForTest(rs.getString(2));
				existActiveExam.setExamCode(rs.getString(3));
				rs.close();
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		// in case not found any active exam match.
		if (existActiveExam.getExamCode() == null) {
			response = new ResponseFromServer("ACTIVE EXAM NOT FOUND");
		} else
			response = new ResponseFromServer("ACTIVE EXAM FOUND");
		// ResponseFromServer class ready to client with StatusMsg and
		// 'Object responseData', in case active exam found existActiveExam include all
		// data, other null.
		response.setResponseData(existActiveExam);
		return response;
	}

	/**
	 * 
	 * @param extensionRequest which contains information about a extension request
	 *                         we would like to add to DB
	 * @return true if creating a new extension request in DB succeeded, else return
	 *         false
	 */
	public ResponseFromServer createNewExtensionRequest(ExtensionRequest extensionRequest) {
		PreparedStatement pstmt;
		ResponseFromServer response = null;

		try {
			pstmt = conn.prepareStatement("INSERT INTO extension_request VALUES(?, ?, ?);");
			pstmt.setString(1, extensionRequest.getActiveExam().getExam().getExamID());
			pstmt.setString(2, extensionRequest.getAdditionalTime());
			pstmt.setString(3, extensionRequest.getReason());
			if (pstmt.executeUpdate() == 1) {
				response = new ResponseFromServer("EXTENSION REQUEST CREATED");
			} else {
				response = new ResponseFromServer("EXTENSION REQUEST DIDN'T CREATED");
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;
	}

	/**
	 * method return all teacher's exams
	 * 
	 * @param obj is the id of the teacher(User)
	 * @return A list og all teacher's exams
	 */

	public ArrayList<Exam> GetTeacherExams(Object obj) {

		int ID;

		ArrayList<Exam> examsOfTeacher = new ArrayList<Exam>();

		ID = (Integer) obj;

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM exam WHERE author=?");

			pstmt.setInt(1, ID);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Exam exam = new Exam(rs.getString(1));
				exam.setProfession(new Profession(rs.getString(2)));
				exam.setCourse(new Course(rs.getString(3)));// addition
				exam.setTimeOfExam(Integer.parseInt(rs.getString(4)));
				exam.setExamStatus(ExamStatus.valueOf(rs.getString(8)));
				exam.setActiveExamType(rs.getString(9));
				examsOfTeacher.add(exam);

			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return examsOfTeacher;

	}

	/**
	 * Method Delete question from DB
	 * 
	 * @param question we delete
	 * @return true if deleted, else false
	 */

	public Boolean DeleteQuestion(Question question) {
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("DELETE FROM question WHERE questionID=? AND teacher=?");
			pstmt.setString(1, question.getQuestionID());
			pstmt.setInt(2, question.getTeacher().getId());
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
			return false;
		}
		return true;

	}

	/**
	 * method return all teacher's questions
	 * 
	 * @param obj for given teacher ID
	 * @return list of all teacher's questions
	 */

	public ArrayList<QuestionRow> GetTeacherQuestions(Object obj) {

		int id;

		ArrayList<QuestionRow> examsOfTeacher = new ArrayList<QuestionRow>();

		id = (Integer) obj;

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM question WHERE teacher=?");

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QuestionRow newQuestion = new QuestionRow();
				newQuestion.setQuestionID(rs.getString(2));
				newQuestion.setProfession(rs.getString(3));
				newQuestion.setQuestion(rs.getString(4));
				examsOfTeacher.add(newQuestion);
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return examsOfTeacher;// return null if no existing tests

	}

	/**
	 * @param activeExam Which contains information about an active exam that we
	 *                   would like to change the time allotted for its solution
	 * @return true if the additional Time for activeExam has been updated at table
	 *         active_exam in DB. else, return false
	 */
	public ResponseFromServer setTimeForActiveTest(ActiveExam activeExam) {
		ResponseFromServer response = null;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("UPDATE active_exam SET timeAllotedForTest=? WHERE exam=?");
			pstmt.setInt(1, activeExam.getTimeAllotedForTest());
			pstmt.setString(2, activeExam.getExam().getExamID());
			if (pstmt.executeUpdate() == 1) {
				response = deleteExtensionRequest(activeExam);
			} else
				response = new ResponseFromServer("TIME EXAM NOT UPDATED");
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;
	}

	/**
	 * 
	 * @param activeExam Which contains information for an active exam for which
	 *                   there is a extension request we would like to delete
	 * @return response for client
	 */
	public ResponseFromServer deleteExtensionRequest(ActiveExam activeExam) {
		ResponseFromServer response = null;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("DELETE FROM extension_request WHERE exam=?");
			pstmt.setString(1, activeExam.getExam().getExamID());
			if (pstmt.executeUpdate() == 1)
				response = new ResponseFromServer("EXTENSION REMOVED");
			else
				response = new ResponseFromServer("EXTENSION WAS NOT REMOVED");
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;
	}

	/**
	 * @param activeExam object which include 2 parameters of date and examcode for
	 *                   Query.
	 * @return response to client
	 */
	@SuppressWarnings("deprecation")
	public ResponseFromServer verifyActiveExam_byDate_and_Code(ActiveExam activeExam) {
		Exam exam = new Exam();
		ResponseFromServer response = null;
		/*** EnterToExam ***/
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(
					"SELECT exam,startTime, timeAllotedForTest, examType FROM active_exam WHERE examCode=? and startTime>=? and startTime<?;");
			pstmt.setString(1, activeExam.getExamCode());
			pstmt.setTime(2, new java.sql.Time(activeExam.getStartTime().getHours(),
					activeExam.getStartTime().getMinutes(), activeExam.getStartTime().getSeconds()));
			pstmt.setTime(3, new java.sql.Time(activeExam.getEndTimeToTakeExam().getHours(),
					activeExam.getEndTimeToTakeExam().getMinutes(), activeExam.getEndTimeToTakeExam().getSeconds()));
			// Time Range for start the exam:
			System.out.println(activeExam.getStartTime() + " - " + activeExam.getEndTimeToTakeExam());
			System.out.println(pstmt.toString());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				exam.setExamID(rs.getString(1));
				activeExam.setExam(exam);
				activeExam.setStartTime(rs.getTime(2));
				activeExam.setTimeAllotedForTest(rs.getString(3));
				activeExam.setActiveExamType(rs.getString(4));
				rs.close();
			}

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		if (activeExam.getActiveExamType() == null) {
			response = new ResponseFromServer("ACTIVE EXAM_NOT_EXIST");

		} else {
			response = new ResponseFromServer("ACTIVE EXAM EXIST");
			response.setResponseData(activeExam);
		}
		return response;

	}

	/**
	 * method return profession instance by profession ID
	 * 
	 * @param id of profession
	 * @return profession instance
	 */

	public Profession getProfessionByID(String id) {
		Profession p = new Profession(id);
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT professionName FROM profession WHERE professionID=?;");
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				p.setProfessionName(rs.getString(1));
				rs.close();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return p;
	}

	/**
	 * @return Returns a list of all the Extensions Requests in the database
	 */
	public ArrayList<ExtensionRequest> getExtensionsRequests() {
		ArrayList<ExtensionRequest> extensionRequestsList = new ArrayList<ExtensionRequest>();
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM extension_request ;");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Exam exam = new Exam(); // i remove null- need to check
				ActiveExam activeExam = new ActiveExam(exam);
				ExtensionRequest extensionRequest = new ExtensionRequest(activeExam);
				exam.setExamID(rs.getString(1));
				activeExam.setExam(exam);
				extensionRequest.setActiveExam(activeExam);
				extensionRequest.setAdditionalTime(rs.getString(2));
				extensionRequest.setReason(rs.getString(3));
				extensionRequestsList.add(extensionRequest);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		for (ExtensionRequest ex : extensionRequestsList) {
			ex.setActiveExam(getActiveExam(ex.getActiveExam()));
		}
		return extensionRequestsList;
	}

	/**
	 * @param activeExam that is initialized with a exam only
	 * @return Initializes the rest of the fields of an active exam and returns it
	 */
	public ActiveExam getActiveExam(ActiveExam activeExam) {

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM active_exam WHERE exam=?");
			pstmt.setString(1, activeExam.getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				activeExam.setStartTime(rs.getTime(2));
				activeExam.setTimeAllotedForTest(rs.getString(3));
				activeExam.setExamCode(rs.getString(4));
				activeExam.setActiveExamType(rs.getString(5));
				rs.close();
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		// in case not found any active exam match.
		if (activeExam.getExamCode() == null) {
			deleteExtensionRequest(activeExam);
		}
		return activeExam;
	}

	/**
	 * method delete exam from DB
	 * 
	 * @param exam the exam that delete from DB
	 * @return true if deleted' else false
	 */

	public boolean DeleteExam(Exam exam) {

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("DELETE FROM exam WHERE examID=? AND profession=? AND course=?");
			pstmt.setString(1, exam.getExamID());
			pstmt.setString(2, exam.getExamID().substring(0, 2));
			pstmt.setString(3, exam.getCourse().getCourseID());// need to be courseID
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * method return from DB all active exams
	 * 
	 * @return data of all active exams
	 */
	public ResponseFromServer getAllActiveExam() {

		ArrayList<ActiveExam> activeExamList = new ArrayList<ActiveExam>();
		ResponseFromServer response = null;
		/*** EnterToExam ***/
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM active_exam;");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Exam exam = new Exam();
				exam.setExamID(rs.getString(1));
				ActiveExam activeExam = new ActiveExam(exam);
				activeExam.setExam(exam);
				activeExam.setStartTime(rs.getTime(2));
				activeExam.setTimeAllotedForTest(rs.getString(3));
				activeExam.setExamCode(rs.getString(4));
				activeExam.setActiveExamType(rs.getString(5));
				activeExamList.add(activeExam);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		response = new ResponseFromServer("ACTIVE EXAMS FOUND");
		response.setResponseData(activeExamList);

		return response;
	}

	/**
	 * method for send question of teacher
	 * 
	 * @param requestData will give data of teacher and profession
	 * @return questions of teacher
	 */

	public ResponseFromServer getQuestionByProfessionAndTeacher(Question requestData) {
		ArrayList<Question> qList = new ArrayList<Question>();
		ResponseFromServer response = null;
		/*** EnterToExam ***/
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM cems.question WHERE teacher=? AND profession=?;");
			pstmt.setString(1, String.valueOf(requestData.getTeacher().getId()));
			pstmt.setString(2, requestData.getProfession().getProfessionID());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Question q = new Question();
				q.setQuestionID(rs.getString(2));
				q.setQuestion(rs.getString(4));
				String[] answers = new String[4];
				answers[0] = rs.getString(5);
				answers[1] = rs.getString(6);
				answers[2] = rs.getString(7);
				answers[3] = rs.getString(8);
				q.setAnswers(answers);
				q.setCorrectAnswerIndex(rs.getInt(9));
				q.setDescription(rs.getString(10));

				qList.add(q);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		if (qList.size() > 0) {
			response = new ResponseFromServer("Question bank FOUND");
			response.setResponseData(qList);
		} else {
			response = new ResponseFromServer("No Question Bank");
		}
		return response;
	}

	/**
	 * method return us the courses by profession
	 * 
	 * @param requestData data of profession that courses presented in him
	 * @return all courses in the profession
	 */

	public ResponseFromServer getCoursesByProfession(Profession requestData) {
		ArrayList<Course> cList = new ArrayList<Course>();
		ResponseFromServer response = null;
		/*** EnterToExam ***/
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT courseID, CourseName FROM cems.course WHERE profession=?;");
			pstmt.setString(1, requestData.getProfessionID());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Course c = new Course(rs.getString(1));
				c.setCourseName(rs.getString(2));
				cList.add(c);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		if (cList.size() > 0) {
			response = new ResponseFromServer("Courses FOUND");
			response.setResponseData(cList);
		} else {
			response = new ResponseFromServer("No courses");
		}
		return response;
	}

	/**
	 * 
	 * @return HashMap of all profession Id as Key and name as Value
	 */

	public HashMap<String, String> getProfNames() {
		HashMap<String, String> profName = new HashMap<String, String>();

		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM profession;");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				profName.put(rs.getString(1), rs.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return profName;
	}

	/**
	 * method return Map og all courses by Profession ID
	 * 
	 * @return HashMap of profession as Key and Value as Course name
	 * 
	 */

	public HashMap<String, ProfessionCourseName> getCoursesNames() {
		HashMap<String, ProfessionCourseName> courseMap = new HashMap<String, ProfessionCourseName>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM course;");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String profID = rs.getString(1);
				String courseID = rs.getString(2);
				String courseName = rs.getString(3);

				// create new profession
				if (!courseMap.containsKey(profID)) {
					ProfessionCourseName pcn = new ProfessionCourseName();
					HashMap<String, String> courses = new HashMap<String, String>();
					pcn.setProfessionID(profID);
					courses.put(courseID, courseName);
					pcn.setCourses(courses);
					courseMap.put(profID, pcn);
				}
				// add new course to existing profession
				else {
					courseMap.get(profID).getCourses().put(courseID, courseName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courseMap;
	}

	/**
	 * method check if exam exist in DB
	 * 
	 * @param ExamID given to check if exist
	 * @return "TRUE" if exist, "FALSE" else
	 */

	public String checkExamExist(String ExamID) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM exam_of_student WHERE exam=?;");
			pstmt.setString(1, ExamID);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			if (rs.first() == false)
				return "FALSE";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "TRUE";
	}

	/**
	 * method gets all scores from Exam given as input
	 * 
	 * @param examID exam given to check her average score
	 * @return array list of grades
	 */

	public ArrayList<Integer> gradesAverageCalc(String examID) {
		ArrayList<Integer> grades = new ArrayList<Integer>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT score FROM exam_of_student WHERE exam=?;");
			pstmt.setString(1, examID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				grades.add(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return grades;
	}

	/**
	 * method get all exam details
	 * 
	 * @param exam data of selected exam
	 * @return all exam given's data
	 */
	public ResponseFromServer getSelectedExamData_byID(Exam exam) {
		ResponseFromServer response = null;
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM exam WHERE examID=?");
			pstmt.setInt(1, Integer.parseInt(exam.getExamID()));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				Profession profession = new Profession(null);
				profession.setProfessionID(rs.getString(2));
				exam.setProfession(profession);

				Course course = new Course(null);
				course.setCourseID(rs.getString(3));
				exam.setCourse(course);

				exam.setTimeOfExam(Integer.parseInt(rs.getString(4)));
				exam.setCommentForTeacher(rs.getString(5));
				exam.setCommentForStudents(rs.getString(6));
				exam.setExamStatus(ExamStatus.valueOf((String) rs.getObject(8)));
				exam.setActiveExamType((String) rs.getObject(9));
				rs.close();
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		response = new ResponseFromServer("EXAM DATA");
		response.setResponseData(exam);

		return response;
	}

	/**
	 * method create new Active exam
	 * 
	 * @param newActiveExam inserted to DB
	 * @return respond that create exam success, else it will return exception
	 */

	@SuppressWarnings("deprecation")
	public ResponseFromServer createNewActiveExam(ActiveExam newActiveExam) {
		ResponseFromServer response = null;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("INSERT INTO active_exam VALUES(?, ?, ?, ?, ?);");
			pstmt.setString(1, newActiveExam.getExam().getExamID());
			pstmt.setTime(2, new java.sql.Time(newActiveExam.getStartTime().getHours(),
					newActiveExam.getStartTime().getMinutes(), newActiveExam.getStartTime().getSeconds()));
			pstmt.setInt(3, newActiveExam.getTimeAllotedForTest());
			pstmt.setString(4, newActiveExam.getExamCode());
			pstmt.setString(5, newActiveExam.getActiveExamType());
			if (pstmt.executeUpdate() != 0) {
				response = new ResponseFromServer("NEW ACTIVE EXAM CREATED");
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;

	}
	/*
	 * method return all exams in an ArrayList
	 */

	public ArrayList<Exam> GetAllExams() {
		ArrayList<Exam> examsList = new ArrayList<Exam>();
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM exam");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Profession profession = new Profession(null);
				profession.setProfessionID(rs.getString(2));

				Exam exam = new Exam(rs.getString(1), profession, new Course(rs.getString(3)),
						Integer.parseInt(rs.getString(4)));
				examsList.add(exam);

			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return examsList;

	}

	/**
	 * method save exam that edited
	 * 
	 * @param exam to be insert after edited
	 * @return massage if insert edited exam success or not
	 */

	public ResponseFromServer SaveEditExam(Exam exam) {
		ResponseFromServer response = null;
		PreparedStatement pstmt;
		// examID, profession, course, timeAllotedForTest, commentForTeacher,
		// commentForStudents, author, status, examType
		try {
			pstmt = conn.prepareStatement(
					"UPDATE exam SET timeAllotedForTest=?, commentForTeacher=?, commentForStudents=? WHERE examID=?");
			pstmt.setString(1, Integer.toString(exam.getTimeOfExam()));
			pstmt.setString(2, exam.getCommentForTeacher());
			pstmt.setString(3, exam.getCommentForStudents());
			pstmt.setString(4, exam.getExamID());
			if (pstmt.executeUpdate() == 1) {
				System.out.println("Edit Exam Saved");
				response = new ResponseFromServer("Edit Exam Saved");
				return response;
			} else {
				response = new ResponseFromServer("Edit Exam_NOT_Saved");
			}

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;
	}

	/**
	 * delete active exam from db
	 * 
	 * @param exam to be deleted
	 * @return true if success/ else false
	 */
	public boolean deleteActiveExam(ActiveExam exam) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("DELETE FROM active_exam WHERE exam=?");
			pstmt.setString(1, exam.getExam().getExamID());
			if (pstmt.executeUpdate() == 1)
				return true;
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * method Update exam in db
	 * 
	 * @param exam exam that Updated
	 * @return true if success, else false
	 */

	public Boolean updateExamStatus(Exam exam) {
		/* createNewActiveExam */
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("UPDATE exam SET status=? WHERE examID=?;");
			pstmt.setObject(1, exam.getExamStatus().toString());
			pstmt.setString(2, exam.getExamID());
			if (pstmt.executeUpdate() == 1) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	/**
	 * The method check if there any Extension request
	 * 
	 * @param extensionRequest for extension request of principal
	 * @return true is there is, else false
	 */

	public boolean checkIfExtensionRequestExists(ExtensionRequest extensionRequest) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM extension_request WHERE exam = ?;");
			pstmt.setString(1, extensionRequest.getActiveExam().getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			if (rs.first() == false)
				return false;
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return true;
	}

	/**
	 * method gets all teachers details
	 * 
	 * @return list of all teachers
	 */

	public ArrayList<Teacher> getTeachers() {
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		PreparedStatement pstmt;
		try {

			pstmt = conn.prepareStatement("SELECT * FROM user WHERE userType=\"Teacher\";");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Teacher teacher = new Teacher(rs.getInt(1), UserType.valueOf(rs.getString(6)));
				teacher.setFirstName(rs.getString(3));
				teacher.setLastName(rs.getString(4));
				teachers.add(teacher);
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return teachers;
	}

	/**
	 * method gets all students details
	 * 
	 * @return list of all students
	 */
	public ArrayList<Student> getStudents() {
		ArrayList<Student> students = new ArrayList<Student>();
		PreparedStatement pstmt;
		try {

			pstmt = conn.prepareStatement("SELECT * FROM user WHERE userType=\"Student\";");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), UserType.valueOf(rs.getString(6)));
				student.setFirstName(rs.getString(3));
				student.setLastName(rs.getString(4));
				students.add(student);
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return students;
	}

	/**
	 * method return all Student grades
	 * 
	 * @param id for Students ID
	 * @return HashMap of all student scores
	 */

	public HashMap<String, Integer> getStudentGrades(int id) {
		HashMap<String, Integer> ExamGrades = new HashMap<String, Integer>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT exam,score FROM cems.exam_of_student where student=?;");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ExamGrades.put(rs.getString(1), rs.getInt(2));
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return ExamGrades;
	}

	/**
	 * give all student's exams
	 * 
	 * @return hash Map of all exams of student
	 */

	public HashMap<String, ArrayList<Integer>> getAllStudentsExams() {
		HashMap<String, ArrayList<Integer>> exams = new HashMap<String, ArrayList<Integer>>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT exam,score FROM cems.exam_of_student;");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ArrayList<Integer> scoreList;
				if (exams.get(rs.getString(1)) == null) {
					scoreList = new ArrayList<Integer>();
					scoreList.add(rs.getInt(2));
					exams.put(rs.getString(1), scoreList);
				} else {
					scoreList = exams.get(rs.getString(1));
					scoreList.add(rs.getInt(2));
					exams.put(rs.getString(1), scoreList);
				}
			}

			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return exams;
	}

	/**
	 * method check students in active Exam
	 * 
	 * @param exam to check Students in the exam
	 * @return arrayList of students ID's
	 */

	public ArrayList<Integer> getStudentsInActiveExam(Exam exam) {
		ArrayList<Integer> students = new ArrayList<Integer>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM exam_of_student WHERE exam = ? AND totalTime = ?;");
			pstmt.setString(1, exam.getExamID());
			pstmt.setInt(2, 0);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				students.add(rs.getInt(1));
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return students;
	}

	/**
	 * 
	 * @param examOfStudent details of Exam Of Student to ne insert to DB
	 * @return if inserted to DB or not
	 */
	public ResponseFromServer InsertExamOfStudent(ExamOfStudent examOfStudent) {
		ResponseFromServer response = null;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("INSERT INTO exam_of_student VALUES(?, ?, ?, ?, ?,?,?);");
			pstmt.setInt(1, examOfStudent.getStudent().getId());
			pstmt.setString(2, examOfStudent.getActiveExam().getExam().getExamID());
			pstmt.setString(3, examOfStudent.getActiveExam().getActiveExamType());
			pstmt.setInt(4, 0);
			pstmt.setString(5, null);
			pstmt.setInt(6, 0);
			pstmt.setString(7, null);
			if (pstmt.executeUpdate() != 0) {
				response = new ResponseFromServer("NEW EXAM OF STUDENT HAS BEEN INSERT");
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;

	}

	/**
	 * @return a response for the server will all of the questions from the DB
	 */
	public ResponseFromServer GetAllQuestions_ToQuestionsBank() {
		ResponseFromServer response = null;
		ArrayList<QuestionRow> allQuestionList = new ArrayList<QuestionRow>();
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM question");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QuestionRow newQuestion = new QuestionRow();
				newQuestion.setQuestionID(rs.getString(2));
				newQuestion.setProfession(rs.getString(3));
				newQuestion.setQuestion(rs.getString(4));
				allQuestionList.add(newQuestion);
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		if (allQuestionList.size() > 0) {
			response = new ResponseFromServer("Question bank FOUND");
			response.setResponseData(allQuestionList);
		} else {
			response = new ResponseFromServer("No Question Bank");
		}

		return response;
	}

	/**
	 * 
	 * @param questionID to get all question data
	 * @return question with all data
	 */
	public Question getQuestionDataBy_questionID(String questionID) {
		/*** Question Bank-Principal step2 ***/
		Question q = new Question();
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM cems.question WHERE questionID=?");
			pstmt.setString(1, questionID);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				q.setQuestionID(rs.getString(2));
				q.setQuestion(rs.getString(4));

				Profession p = new Profession(null);
				p.setProfessionID(rs.getString(3));
				q.setProfession(p);

				String[] answers = new String[4];
				answers[0] = rs.getString(5);
				answers[1] = rs.getString(6);
				answers[2] = rs.getString(7);
				answers[3] = rs.getString(8);
				q.setAnswers(answers);
				q.setCorrectAnswerIndex(rs.getInt(9));
				q.setDescription(rs.getString(10));
				rs.close();
			}

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return q;

	}

	/**
	 * @param studentExam contains information about a student who solved an exam
	 * @return true if success inserting all students questions with answers from
	 *         exam
	 */
	public boolean insertStudentQuestions(ExamOfStudent studentExam) {
		PreparedStatement pstmt;

		for (QuestionInExam q : studentExam.getQuestionsAndAnswers().keySet()) {
			try {
				pstmt = conn.prepareStatement("INSERT INTO student_answers_in_exam VALUES(?, ?, ?, ?, ?);");
				pstmt.setInt(1, studentExam.getStudent().getId());
				pstmt.setString(2, studentExam.getActiveExam().getExam().getExamID());
				pstmt.setString(3, q.getQuestion().getQuestionID());
				pstmt.setInt(4, studentExam.getQuestionsAndAnswers().get(q));
				pstmt.setInt(5,
						studentExam.getQuestionsAndAnswers().get(q) == q.getQuestion().getCorrectAnswerIndex() ? 1 : 0);

				if (pstmt.executeUpdate() == 0) {
					return false;
				}
				// to do something with status
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	/**
	 * @param exam with only ID
	 * @return exam with comment for students
	 */
	public Exam getCommentForStudents(Exam exam) {
		PreparedStatement pstmt;
		try {

			pstmt = conn.prepareStatement("SELECT commentForStudents FROM exam WHERE examID=?;");
			pstmt.setString(1, exam.getExamID());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				exam.setCommentForStudents(rs.getString(1));
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return exam;

	}

	/**
	 * @param examID which include exam id
	 * @return array list of questions in exam including the question ID and score
	 */
	public ArrayList<QuestionInExam> getQuestionsOfExam(String examID) {
		ArrayList<QuestionInExam> list = new ArrayList<>();
		PreparedStatement pstmt;
		try {

			pstmt = conn.prepareStatement("SELECT question, score FROM question_in_exam WHERE exam=?;");
			pstmt.setString(1, examID);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QuestionInExam q = new QuestionInExam(rs.getInt(2), new Question(rs.getString(1)), null);
				list.add(q);
			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return list;
	}

	/**
	 * @param questionID which include question id
	 * @return a question with all the info (description, answers etc)
	 */
	public Question getFullQuestion(String questionID) {
		Question q = new Question(questionID);

		PreparedStatement pstmt;
		try {

			pstmt = conn.prepareStatement(
					"SELECT question, answer1, answer2, answer3, answer4, correctAnswerIndex, description FROM question WHERE questionID=?;");
			pstmt.setString(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				q.setQuestion(rs.getString(1));
				String[] answers = new String[4];
				answers[0] = rs.getString(2);
				answers[1] = rs.getString(3);
				answers[2] = rs.getString(4);
				answers[3] = rs.getString(5);
				q.setAnswers(answers);
				q.setCorrectAnswerIndex(rs.getInt(6));
				q.setDescription(rs.getString(7));

			}
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		return q;
	}

	/**
	 * @param studentExam Which contains information about a student who solves exam
	 * @return true or false if success in update the new exam of student to the DB
	 */
	public boolean updateStudentExam(ExamOfStudent studentExam) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(
					"UPDATE exam_of_student SET totalTime=?, reason_of_submit=?, score=? WHERE exam=? AND student=?");
			pstmt.setInt(1, studentExam.getTotalTime());
			pstmt.setString(2, studentExam.getReasonOfSubmit().toString());
			pstmt.setInt(3, studentExam.getScore());
			pstmt.setString(4, studentExam.getActiveExam().getExam().getExamID());
			pstmt.setInt(5, studentExam.getStudent().getId());

			if (pstmt.executeUpdate() != 0) {
				return true;
			}
			// to do something with status
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	/**
	 * 
	 * @param examOfStudent for verify exam
	 * @return true if verified. else,false
	 */

	public Boolean verifyExamOfStudentByExamID(ExamOfStudent examOfStudent) {
		/*** EnterToExam ***/
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM exam_of_student WHERE student=? AND exam=?;");
			pstmt.setInt(1, examOfStudent.getStudent().getId());
			pstmt.setString(2, examOfStudent.getActiveExam().getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.first() == true) {
				rs.close();
				return false;
			}

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return true;
	}

	/**
	 * 
	 * @param requestData array with all student and examID
	 * @return arrayList of all scores
	 */
	public ArrayList<String> getStudentScore(String[] requestData) {
		PreparedStatement pstmt;
		ArrayList<String> Details = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement("SELECT score,examType FROM exam_of_student WHERE student=? AND exam =?;");
			pstmt.setInt(1, Integer.parseInt(requestData[1]));
			pstmt.setString(2, requestData[0]);
			ResultSet rs = pstmt.executeQuery();
			rs.next();

			Details.add(String.valueOf(rs.getInt(1)));
			Details.add(rs.getString(2));

			rs.close();

			pstmt = conn.prepareStatement("SELECT professionName FROM profession WHERE professionID=? ;");
			pstmt.setString(1, requestData[0].substring(0, 2));
			rs = pstmt.executeQuery();
			rs.next();
			Details.add(rs.getString(1));
			rs.close();

			pstmt = conn.prepareStatement("SELECT courseName FROM course WHERE courseID=? AND profession=?;");
			pstmt.setString(1, requestData[0].substring(2, 4));
			pstmt.setString(2, requestData[0].substring(0, 2));
			rs = pstmt.executeQuery();
			rs.next();
			Details.add(rs.getString(1));
			rs.close();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		return Details;
	}

	/**
	 * 
	 * @param details of student that would like so see his solved exam
	 * @return array list with all questions
	 */

	public ArrayList<QuestionRow> getSolvedComputerizedExam(String[] details) {
		ArrayList<QuestionRow> questionsOfExam = new ArrayList<QuestionRow>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(
					"SELECT question,answer,correct FROM student_answers_in_exam where student =? and exam=?;");
			pstmt.setString(1, details[0]);
			pstmt.setString(2, details[1]);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QuestionRow question = new QuestionRow();
				question.setQuestionID(rs.getString(1));
				question.setStudentAnswer(rs.getInt(2));
				question.setCorrect(rs.getInt(3));
				questionsOfExam.add(question);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return questionsOfExam;
	}

	/**
	 * return the correct answer for question
	 * 
	 * @param questionID for answer
	 * @return question with right answer
	 */

	public Question correctAnswerForQuestion(String questionID) {

		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(
					"SELECT question,answer1,answer2,answer3,answer4,correctAnswerIndex FROM question where questionID=?;");
			pstmt.setString(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			Question question = new Question(questionID);
			question.setQuestion(rs.getString(1));
			String[] answers = { rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) };
			question.setAnswers(answers);
			question.setCorrectAnswerIndex(rs.getInt(6));
			question.setCorrectAns(answers[question.getCorrectAnswerIndex() - 1]);
			rs.close();
			return question;
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return null;
	}

	/**
	 * @param examID which include exam id
	 * @return the time when an active exam with ID exam id started
	 */
	public Time getStartTimeOfActiveExam(String examID) {

		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT startTime FROM active_exam WHERE exam=?");
			pstmt.setString(1, examID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Time startTime = rs.getTime(1, Calendar.getInstance());
				rs.close();
				return startTime;
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return null;
	}

	/**
	 * @param examID which include exam id
	 * @return the number of students that didn't submit the exam
	 */
	public int getNumberOfNotSubmitted(String examID) {
		PreparedStatement pstmt;
		int sum = 0;
		try {
			pstmt = conn
					.prepareStatement("SELECT SUM(exam=?) as sum FROM exam_of_student WHERE reason_of_submit IS NULL;");
			pstmt.setString(1, examID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return sum;

	}

	/**
	 * @param activeExam which contains information about an active exam
	 * @return true if documenting the exam was successful
	 */
	@SuppressWarnings("deprecation")
	public boolean documentExam(ActiveExam activeExam) {
		int initiated = initiatedSubmitInActiveExam(activeExam.getExam().getExamID());
		int forced = forcedSubmitInActiveExam(activeExam.getExam().getExamID());
		Time endTime = new Time(System.currentTimeMillis());
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("INSERT INTO exam_records VALUES(?, ?, ?, ?, ?, ?, ?);");
			pstmt.setString(1, activeExam.getExam().getExamID());
			pstmt.setTime(2, new java.sql.Time(endTime.getHours(), endTime.getMinutes(), endTime.getSeconds()));
			pstmt.setInt(3, activeExam.getTimeAllotedForTest());
			LocalTime currentTime = (new Time(System.currentTimeMillis())).toLocalTime();
			int actualTime = (currentTime.toSecondOfDay() - activeExam.getStartTime().toLocalTime().toSecondOfDay())
					/ 60;
			pstmt.setInt(4, actualTime);
			pstmt.setInt(5, initiated);
			pstmt.setInt(6, forced);
			pstmt.setInt(7, initiated + forced);

			if (pstmt.executeUpdate() != 0) {
				return true;
			}
			// to do something with status
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}

	/**
	 * @param examID
	 * @return the number of submitted exams that were forced by system
	 */
	private int forcedSubmitInActiveExam(String examID) {
		PreparedStatement pstmt;
		int sum = 0;
		try {
			pstmt = conn.prepareStatement(
					"SELECT SUM(exam=?) as sum FROM exam_of_student WHERE reason_of_submit='forced';");
			pstmt.setString(1, examID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return sum;
	}

	/**
	 * @param examID
	 * @return the number of submitted exams that were initiated by student
	 */
	private int initiatedSubmitInActiveExam(String examID) {
		PreparedStatement pstmt;
		int sum = 0;
		try {
			pstmt = conn.prepareStatement(
					"SELECT SUM(exam=?) as sum FROM exam_of_student WHERE reason_of_submit='initiated';");
			pstmt.setString(1, examID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return sum;
	}

	/**
	 * 
	 * @param question to be edited
	 * @return "true" if updated' "false" else
	 */

	public String EditQuestion(Question question) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(
					"UPDATE question SET teacher=?, questionID=? ,profession=? ,question=?, answer1=? ,answer2=? ,answer3=?, answer4=? ,correctAnswerIndex=? ,description=? WHERE teacher=? AND questionID=?;");
			pstmt.setInt(1, question.getTeacher().getId());
			pstmt.setString(2, question.getQuestionID());
			pstmt.setString(3, question.getProfession().getProfessionID());
			pstmt.setString(4, question.getQuestion());
			pstmt.setString(5, question.getAnswers()[0]);
			pstmt.setString(6, question.getAnswers()[1]);
			pstmt.setString(7, question.getAnswers()[2]);
			pstmt.setString(8, question.getAnswers()[3]);
			pstmt.setInt(9, question.getCorrectAnswerIndex());
			pstmt.setString(10, question.getDescription());
			pstmt.setInt(11, question.getTeacher().getId());
			pstmt.setString(12, question.getQuestionID());
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
			return "false";
		}

		return "true";
	}

	/**
	 * check if active exam is exist
	 * 
	 * @param activeExam to check if exist
	 * @return true if exist, else else
	 */

	public boolean activeExamExists(ActiveExam activeExam) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM active_exam WHERE exam = ?;");
			pstmt.setString(1, activeExam.getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			if (rs.first() == false)
				return false;
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return true;
	}

	/**
	 * 
	 * @param exam that belongs to the teacher we return
	 * @return id of teacher the create that exam
	 */

	public int getTeacherOfExam(Exam exam) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT author FROM exam WHERE examID = ?;");
			pstmt.setString(1, exam.getExamID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int teacherId = rs.getInt(1);
				return teacherId;
			}
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return 0;
	}

	/**
	 * 
	 * @return id of principal in order to send a message alert.
	 */

	public int getPrincipalId() {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT id FROM user WHERE userType = ?;");
			pstmt.setString(1, "Principal");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int principalId = rs.getInt(1);
				return principalId;
			}
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return 0;
	}

	/**
	 * @return ArrayList of all examsID
	 */
	public ArrayList<String> getAllExams() {
		ArrayList<String> examsID = new ArrayList<String>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT examID FROM exam ;");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				if (!examsID.contains(rs.getString(1)))
					examsID.add(rs.getString(1));
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		return examsID;
	}

	/**
	 * method return list of students that solve the test and suspected with copy
	 * 
	 * @param exams arrayList of exams of student
	 * @return array list with students that might be copy
	 */

	public ArrayList<Integer> getPotentialCopyList(ArrayList<ExamOfStudent> exams) {
		PreparedStatement pstmt;
		HashMap<Integer, ArrayList<Integer>> studentsAns = new HashMap<Integer, ArrayList<Integer>>();

		ArrayList<Integer> Answer1 = new ArrayList<Integer>();
		ArrayList<Integer> Answer2 = new ArrayList<Integer>();
		ArrayList<Integer> Answer3 = new ArrayList<Integer>();
		ArrayList<Integer> Answer4 = new ArrayList<Integer>();
		studentsAns.put(1, Answer1);
		studentsAns.put(2, Answer2);
		studentsAns.put(3, Answer3);
		studentsAns.put(4, Answer4);
		ArrayList<Integer> suspectedInCopy = new ArrayList<Integer>();
		if (exams.size() == 1) {
			return suspectedInCopy;
		}

		ArrayList<String> questionOfExam = new ArrayList<String>();
		Set<QuestionInExam> questioninExam = exams.get(0).getQuestionsAndAnswers().keySet();
		String SpecificExam = exams.get(0).getActiveExam().getExam().getExamID();
		for (QuestionInExam q : questioninExam) {
			questionOfExam.add(q.getQuestion().getQuestionID());
		}

		for (String q : questionOfExam) {
			try {
				pstmt = conn.prepareStatement(
						"SELECT * FROM cems.student_answers_in_exam where exam=? and question=? and correct=0;");
				pstmt.setString(1, SpecificExam);
				pstmt.setString(2, q);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					switch (rs.getInt(4)) {
					case 1: {
						Answer1.add(rs.getInt(1));
					}
						break;
					case 2: {
						Answer2.add(rs.getInt(1));
					}
						break;
					case 3: {
						Answer3.add(rs.getInt(1));
					}
						break;
					case 4: {
						Answer4.add(rs.getInt(1));
					}
					}
				}
			} catch (SQLException ex) {
				serverFrame.printToTextArea("SQLException: " + ex.getMessage());
			}
			for (int i = 1; i < 5; i++) {
				if (studentsAns.get(i).size() > 1) {
					suspectedInCopy.addAll(studentsAns.get(i));
				}
			}
			Answer1.clear();
			Answer2.clear();
			Answer3.clear();
			Answer4.clear();
		}

		ArrayList<Integer> suspectedInCopyDistinct = new ArrayList<Integer>();
		for (Integer curr : suspectedInCopy)
			if (!suspectedInCopyDistinct.contains(curr))
				suspectedInCopyDistinct.add(curr);
		suspectedInCopyDistinct.add(Integer.parseInt(SpecificExam));
		return suspectedInCopyDistinct;
	}

	/**
	 * method update scores of question
	 * 
	 * @param updatedQuestions array of question
	 * @return if updated or not
	 */

	public ResponseFromServer updateScoresOfEditExam(ArrayList<QuestionInExam> updatedQuestions) {
		ResponseFromServer response = null;
		PreparedStatement pstmt;
		try {
			for (QuestionInExam qID : updatedQuestions) {
				pstmt = conn.prepareStatement("UPDATE question_in_exam SET score=? WHERE question=?");
				pstmt.setInt(1, qID.getScore());
				pstmt.setString(2, qID.getQuestionID());

				System.out.println(pstmt);
				if (pstmt.executeUpdate() == 1) {
					System.out.println("Edit Exam Saved");
					response = new ResponseFromServer("Edit Exam Scores Updated");
				} else {
					response = new ResponseFromServer("Edit_Exam_Scores_NOT_Updated");
				}
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return response;
	}

	/**
	 * @param activeExam which contains information about an active test
	 * @return a list of examOfStudent for a given active exam
	 */
	public ArrayList<ExamOfStudent> getExamsOfStudentsByExamID(ActiveExam activeExam) {
		ArrayList<ExamOfStudent> examList = new ArrayList<>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT student FROM exam_of_student WHERE exam=?;");
			pstmt.setString(1, activeExam.getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ExamOfStudent e = new ExamOfStudent(activeExam, new Student(rs.getInt(1), UserType.Student));
				examList.add(e);
			}
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}

		return examList;
	}

	/**
	 * @param e an exam of student
	 * @return a hashmap for each question in exam - the student answer
	 */
	public HashMap<QuestionInExam, Integer> getQuestionsAndAnswersByExamOfStudent(ExamOfStudent e) {
		HashMap<QuestionInExam, Integer> qNa = new HashMap<>();

		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(
					"SELECT question,answer FROM student_answers_in_exam where student =? and exam=?;");
			pstmt.setInt(1, e.getStudent().getId());
			pstmt.setString(2, e.getActiveExam().getExam().getExamID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QuestionInExam q = new QuestionInExam(0, new Question(rs.getString(1)));
				qNa.put(q, rs.getInt(2));
			}
			rs.close();
		} catch (SQLException ex) {
			serverFrame.printToTextArea("SQLException: " + ex.getMessage());
		}
		return qNa;
	}

}
