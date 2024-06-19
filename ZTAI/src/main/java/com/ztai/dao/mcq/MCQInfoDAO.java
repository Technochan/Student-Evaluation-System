package com.ztai.dao.mcq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ztai.model.QuizSection;
import com.ztai.util.DBConnectionManager;


public class MCQInfoDAO {

	ArrayList<QuizSection> quizSections = new ArrayList<QuizSection>();
	ArrayList<QuizSection> quizSectionQuestions = new ArrayList<QuizSection>();
	HashMap<Integer, Integer> questionsPerSectionQuiz = new HashMap<>();
	ArrayList<QuizSection> eachQuizSectionReport = new ArrayList<QuizSection>();
	private HashMap<String, QuizSection> mp = new HashMap<>(); // student each question based attempts, scored
	
	int totalStudentQuizSolved = 0;
	int totalStudentQuizMarks = 0;
	int totalQuizQuestions = 0;
	int totalQuizMarks = 0;
	String studentId;

	public void loadMCQPageDetails(String studentId) {
		this.studentId = studentId;
		quizSections = loadMCQSections();
		try {
			quizSectionQuestions = loadMCQSectionQuestions();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			merge();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadNumberOfQuestionsPerSection();
		totalQuizQuestions = getTotalQuizQuestionsCount();
		totalQuizMarks = getTotalQuizMarksCount();
	}

	private int getTotalQuizMarksCount() {
		int totalMarks = 0;
		String get_marks_count = "SELECT SUM(mark) AS total_marks FROM quiz_questions";

		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement ps = con.prepareStatement(get_marks_count);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				totalMarks = rs.getInt("total_marks");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalMarks;
	}

	private int getTotalQuizQuestionsCount() {
		int totalQuestions = 0;
		String get_questions_count = "SELECT COUNT(*) AS total_questions FROM quiz_questions";
		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement ps = con.prepareStatement(get_questions_count);) {
			ResultSet rs = ps.executeQuery();
			totalQuestions = 0;
			if (rs.next()) {
				totalQuestions = rs.getInt("total_questions");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalQuestions;
	}

	private void merge() throws SQLException {
		int index = 0;
		for (QuizSection rep : eachQuizSectionReport) {
			QuizSection cs = quizSections.get(index++);

			int totalSolved = rep.getSolvedCount();
			totalStudentQuizSolved += totalSolved;
			cs.setSolvedCount(totalSolved);

			int mk = rep.getTotalScore(); // getTotalMarks();
			
			cs.setTotalScore(mk); // setTotalMarks(mk);
			cs.setQuestionCount( getSectionQuestionCount(  cs.getSectionId())            ); //  mk / 10);
			int scored = rep.getTotalScoredMark(); // getScoredMarks();
			cs.setTotalScoredMark(scored); // setScoredMarks(scored);
			totalStudentQuizMarks += scored;
		}
		while (index < quizSections.size()) {
			QuizSection cs = quizSections.get(index);
			cs.setSolvedCount(0);
			int mk = getQuizSectionScore(index + 1);
			cs.setTotalScore(mk); // setTotalMarks(mk);
			cs.setQuestionCount( getSectionQuestionCount(  cs.getSectionId())            ); //mk / 10);
			cs.setTotalScoredMark(0); // setScoredMarks(0);
			index++;
		}
	}

	 public int getSectionQuestionCount(int sectionId) {
	        int questionCount = 0;
	        String query = "SELECT COUNT(*) AS question_count FROM quiz_questions WHERE section_id = ?";

	        try (Connection connection = DBConnectionManager.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	             
	            preparedStatement.setInt(1, sectionId);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    questionCount = resultSet.getInt("question_count");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return questionCount;
	    }

	private void loadNumberOfQuestionsPerSection() {
		int sectionId = 1;
		int count = 0;
		for (QuizSection section : quizSectionQuestions) {
			if (sectionId == section.getSectionId()) {
				count++;
			} else {
				questionsPerSectionQuiz.put(sectionId, count);
				sectionId++;
				count = 1;
			}
		}
		questionsPerSectionQuiz.put(sectionId, count);
	}

	private ArrayList<QuizSection> loadMCQSectionQuestions() throws SQLException {

		Connection connection = DBConnectionManager.getConnection();

		ArrayList<QuizSection> quizSectionQuestions = new ArrayList<>();
		final String LOAD_QUIZ_SECTIONS = "select * from quiz_questions order by section_id asc , question_id asc";
		final String LOAD_STUDENT_QUIZ_INFO = "SELECT * FROM student_quiz_info WHERE ztai_id = ? order by section_id asc, question_id asc";

		try {
			// Load all coding questions
			PreparedStatement ps = connection.prepareStatement(LOAD_QUIZ_SECTIONS);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int sectionId = rs.getInt("section_id");
				int questionId = rs.getInt("question_id");
				String questionName = rs.getString("question_name");
				String difficulty = rs.getString("difficulty");
				int marks = rs.getInt("mark");
				int duration = rs.getInt("duration");

				System.out.println(duration);
				QuizSection question = new QuizSection(sectionId, questionId, questionName, difficulty, marks,
						duration);
				quizSectionQuestions.add(question);
			}
			rs.close();
			ps.close();

			int totalSolvedCount = 0;
			int totalScores = 0;

			int section = 1;

			// Load student coding information
			ps = connection.prepareStatement(LOAD_STUDENT_QUIZ_INFO);
			ps.setString(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				int sectionId = rs.getInt("section_id");
				int questionId = rs.getInt("question_id");
				int score = rs.getInt("score");
				boolean solved = rs.getInt("status") == 1;
				
				QuizSection csq = new QuizSection(score, solved);
    			System.out.println(csq.getTotalScoredMark()+"   yelei score ley"+solved);
    			mp.put("section"+sectionId+"question"+questionId, csq);
    			

				for (QuizSection question : quizSectionQuestions) {
					if (question.getSectionId() == sectionId && question.getQuestionId() == questionId) {
						question.setTotalScoredMark(score); // setScore(score);
						question.setSolved(solved);
						break;
					}
				}
				if (section == sectionId) {
					totalScores += score;
					totalSolvedCount += solved ? 1 : 0;
				} else {
					int sectionScore = getQuizSectionScore(section);
					QuizSection qs = new QuizSection(totalSolvedCount, totalScores, sectionScore);
					eachQuizSectionReport.add(qs);
					totalScores = 0;
					totalSolvedCount = 0;
					totalScores += score;
					totalSolvedCount += solved ? 1 : 0;
					section++;
				}

			}
			// last one
			int sectionScore = getQuizSectionScore(section);
			QuizSection qs = new QuizSection(totalSolvedCount, totalScores, sectionScore);
			eachQuizSectionReport.add(qs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return quizSectionQuestions;
	}

	private int getQuizSectionScore(int section) throws SQLException {

		Connection connection = DBConnectionManager.getConnection();
		String get_SectionScore = "SELECT SUM(mark) AS totalScore FROM quiz_questions WHERE section_id = ?";
		PreparedStatement ps = connection.prepareStatement(get_SectionScore);
		ps.setInt(1, section);
		ResultSet rs = ps.executeQuery();
		int score = 0;
		if (rs.next()) {
			score = rs.getInt("totalScore");
			System.out.println("scaore " + score);
		}
		return score;

	}

	private ArrayList<QuizSection> loadMCQSections() {

		ArrayList<QuizSection> quizSections = new ArrayList<>();
		final String LOAD_QUIZ_SECTIONS = "SELECT * FROM quiz_question_section";

		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement ps = con.prepareStatement(LOAD_QUIZ_SECTIONS);) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int sectionId = rs.getInt("section_id");
				String sectionName = rs.getString("section_name");
				QuizSection qs = new QuizSection(sectionId, sectionName);
				quizSections.add(qs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizSections;
	}

	public int getStartQuestion(String studentId, int sectionId, int questionId) {
		int total = 1;
		String get_from_student = "select attend as last from student_quiz_info where ztai_id = ? and section_id = ? and question_id = ?";

		try (Connection con = DBConnectionManager.getConnection()) {
			PreparedStatement ps = con.prepareStatement(get_from_student);
			ps.setString(1, studentId);
			ps.setInt(2, sectionId);
			ps.setInt(3, questionId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int last = rs.getInt("last");
				String get_from_quiz_question = "select mark as total from quiz_questions where section_id = ? and question_id = ?";
				ps = con.prepareStatement(get_from_quiz_question);
				ps.setInt(1, sectionId);
				ps.setInt(2, questionId);

				rs = ps.executeQuery();

				if (rs.next()) {
					total = rs.getInt("total");
					total = total - last >= 0 ? total - last : 1;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}

	public void updateQuizScore(int correctCount, int wrongCount, int totalQuestions, int sectionId, int questionId,
			String studentId) {
		String selectQuery = "SELECT COUNT(*) AS count FROM student_quiz_info WHERE ztai_id = ? AND section_id = ? AND question_id = ?";
		String updateQuery = "UPDATE student_quiz_info SET score = ?, attend = ?, status = ? WHERE ztai_id = ? AND section_id = ? AND question_id = ?";
		String insertQuery = "INSERT INTO student_quiz_info (ztai_id, name, section_id, question_id, score, attend, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {

			// Set parameters for select query
			selectStmt.setString(1, studentId);
			selectStmt.setInt(2, sectionId);
			selectStmt.setInt(3, questionId);

			try (ResultSet rs = selectStmt.executeQuery()) {
				rs.next();
				int count = rs.getInt("count");

				if (count > 0) {
					try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
						updateStmt.setInt(1, correctCount);
						updateStmt.setInt(2, correctCount + wrongCount);
						updateStmt.setInt(3, (totalQuestions - (correctCount + wrongCount) >= 1) ? 0 : 1);
						updateStmt.setString(4, studentId);
						updateStmt.setInt(5, sectionId);
						updateStmt.setInt(6, questionId);
						updateStmt.executeUpdate();
						System.out.println("Updated existing record");
					}
				} else {
					try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
						insertStmt.setString(1, studentId);
						insertStmt.setString(2, "chan"); // assuming the student name is passed as an argument
						insertStmt.setInt(3, sectionId);
						insertStmt.setInt(4, questionId);
						insertStmt.setInt(5, correctCount);
						insertStmt.setInt(6, correctCount + wrongCount);
						insertStmt.setInt(7, (totalQuestions - (correctCount + wrongCount) >= 1) ? 0 : 1);
						insertStmt.executeUpdate();
						System.out.println("Inserted new record");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public HashMap<String, QuizSection> getMp() {
		return mp;
	}

	public void setMp(HashMap<String, QuizSection> mp) {
		this.mp = mp;
	}

	public HashMap<Integer, Integer> getQuestionsPerSectionQuiz() {
		return questionsPerSectionQuiz;
	}

	public void setQuestionsPerSectionQuiz(HashMap<Integer, Integer> questionsPerSectionQuiz) {
		this.questionsPerSectionQuiz = questionsPerSectionQuiz;
	}

	public ArrayList<QuizSection> getQuizSections() {
		return quizSections;
	}

	public void setQuizSections(ArrayList<QuizSection> quizSections) {
		this.quizSections = quizSections;
	}

	public ArrayList<QuizSection> getQuizSectionQuestions() {
		return quizSectionQuestions;
	}

	public void setQuizSectionQuestions(ArrayList<QuizSection> quizSectionQuestions) {
		this.quizSectionQuestions = quizSectionQuestions;
	}

	public int getTotalStudentQuizSolved() {
		return totalStudentQuizSolved;
	}

	public void setTotalStudentQuizSolved(int totalStudentQuizSolved) {
		this.totalStudentQuizSolved = totalStudentQuizSolved;
	}

	public int getTotalStudentQuizMarks() {
		return totalStudentQuizMarks;
	}

	public void setTotalStudentQuizMarks(int totalStudentQuizMarks) {
		this.totalStudentQuizMarks = totalStudentQuizMarks;
	}

	public int getTotalQuizQuestions() {
		return totalQuizQuestions;
	}

	public void setTotalQuizQuestions(int totalQuizQuestions) {
		this.totalQuizQuestions = totalQuizQuestions;
	}

	public int getTotalQuizMarks() {
		return totalQuizMarks;
	}

	public void setTotalQuizMarks(int totalQuizMarks) {
		this.totalQuizMarks = totalQuizMarks;
	}

}
