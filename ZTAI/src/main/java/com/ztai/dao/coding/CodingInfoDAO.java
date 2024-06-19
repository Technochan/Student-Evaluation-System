package com.ztai.dao.coding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ztai.model.CodingSection;
import com.ztai.util.DBConnectionManager;

public class CodingInfoDAO {
	
	private ArrayList<CodingSection> codingSection = new ArrayList<>();
	private ArrayList<CodingSection> codingSectionQuestions = new ArrayList<>();
	private ArrayList<CodingSection> eachSectionReport = new ArrayList<>();
	private HashMap<Integer, Integer> questionsPerSection = new HashMap<>();
	private HashMap<String, CodingSection> mp = new HashMap<>(); // student each question based attempts, scored

	private String studentId ;

	private int totalSolvedCoding ;
	private int totalAttemptsCoding;
	private int totalMarksCoding ;
	private int totalQuestions ;
	private int totalMarks ;

	public void loadCodingPageDetails(String studentId) {
		this.studentId = studentId;
		codingSection = loadCodingSections();
		codingSectionQuestions = loadCodingSectionQuestions();
		try {
			merge();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		loadNumberOfQuestionsPerSection();
		totalQuestions = getTotalQuestionsCount();
		totalMarks = getTotalMarksCount();

	}

	private void loadNumberOfQuestionsPerSection() {
		int sectionId = 1;
		int count = 0;
		for (CodingSection section : codingSectionQuestions) {
			if (sectionId == section.getSectionId()) {
				count++;
			} else {
				questionsPerSection.put(sectionId, count);
				sectionId++;
				count = 1;
			}
		}
		questionsPerSection.put(sectionId, count);
	}

	private int getTotalMarksCount() {
		int totalMarks = 0;
		String get_marks_count = "SELECT SUM(mark) AS total_marks FROM coding_questions";
		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement ps = con.prepareStatement(get_marks_count)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				totalMarks = rs.getInt("total_marks");
				return totalMarks;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalMarks;
	}

	private int getTotalQuestionsCount() {
		int totalQuestions = 0;
		String get_questions_count = "SELECT COUNT(*) AS total_questions FROM coding_questions";
		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement ps = con.prepareStatement(get_questions_count);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				totalQuestions = rs.getInt("total_questions");
				return totalQuestions;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalQuestions;
	}

	public ArrayList<CodingSection> loadCodingSections() {
		ArrayList<CodingSection> codingSections = new ArrayList<>();
		final String LOAD_SECTIONS = "SELECT * FROM coding_question_section";

		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement ps = con.prepareStatement(LOAD_SECTIONS);) {

			ResultSet rs = ps.executeQuery();

			int index = 0;
			while (rs.next()) {
				int sectionId = rs.getInt("section_id");
				String sectionName = rs.getString("section_name");
				CodingSection cs = new CodingSection(sectionId, sectionName);
				codingSections.add(cs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return codingSections;
	}
	
	private ArrayList<CodingSection> loadCodingSectionQuestions() {
    	
    	ArrayList<CodingSection> codingSectionQuestions = new ArrayList<>();
    	final String LOAD_SECTIONS = "select * from coding_questions order by section_id asc , question_id asc";
    	final String LOAD_STUDENT_INFO = "SELECT * FROM student_coding_info WHERE ztai_id = ? order by section_id asc, question_id asc";

    	try(Connection con = DBConnectionManager.getConnection()) {
    		// Load all coding questions
    		PreparedStatement ps = con.prepareStatement(LOAD_SECTIONS);
    		ResultSet rs = ps.executeQuery();
    		while (rs.next()) {
    			int sectionId = rs.getInt("section_id");
    			int questionId = rs.getInt("question_id");
    			String questionName = rs.getString("question_name");
    			String difficulty = rs.getString("difficulty");
    			int marks = rs.getInt("mark");
    			
    			CodingSection question = new CodingSection(sectionId, questionId, questionName, difficulty, marks);
    			codingSectionQuestions.add(question);
    		}
    		rs.close();
    		ps.close();

    		int totalAttempts = 0;
    		int totalSolvedCount = 0;
    		int totalScores = 0;
    		
    		int section = 1;
    	
    		// Load student coding information
    		ps = con.prepareStatement(LOAD_STUDENT_INFO);
    		ps.setString(1, studentId);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			int sectionId = rs.getInt("section_id");
    			int questionId = rs.getInt("question_id");
    			int attempts = rs.getInt("attempt");
    			int score = rs.getInt("score");
    			boolean solved = rs.getInt("status") == 1;
    			
    			CodingSection csq = new CodingSection(attempts, score, solved);
    			System.out.println(csq.getTotalScoredMark()+"   yelei score ley"+solved);
    			mp.put("section"+sectionId+"question"+questionId, csq);
    			
    			
    			for (CodingSection question : codingSectionQuestions) {
    				if (question.getSectionId() == sectionId && question.getQuestionId() == questionId) {
    					question.setTotalAttempts(attempts);  //  setAttempts(attempts);
    					question.setTotalScoredMark(score);   // setScore(score);
    					question.setSolved(solved);
    					break;
    				}
    			} 
    			if(section == sectionId) {
    				totalAttempts += attempts;
    				totalScores += score;
    				totalSolvedCount +=  solved ? 1 : 0;
    			} else {
    				int sectionScore = getSectionScore(section);
    				CodingSection cod = new CodingSection(totalAttempts,totalSolvedCount,totalScores,sectionScore);
    				eachSectionReport.add(cod);
    				totalAttempts = 0;
    				totalScores = 0;
    				totalSolvedCount = 0;
    				totalAttempts += attempts;
    				totalScores += score;
    				totalSolvedCount +=  solved ? 1 : 0;		
    				section++;
    			}    			
    		}
    	
    		    int sectionScore = getSectionScore(section);
    		    CodingSection cod = new CodingSection(totalAttempts, totalSolvedCount, totalScores, sectionScore);
    		    eachSectionReport.add(cod);
    	
    		
    		

    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return codingSectionQuestions;
    }



	private void merge() throws SQLException {
		int index = 0;
		for (CodingSection rep : eachSectionReport) {
			CodingSection cs = codingSection.get(index++);

			int totalAttempt = rep.getTotalAttempts();
			totalAttemptsCoding += totalAttempt;
			cs.setTotalAttempts(totalAttempt);

			int totalSolved = rep.getSolvedCount();
			totalSolvedCoding += totalSolved;
			cs.setSolvedCount(totalSolved);

			int mk = rep.getTotalScore(); // getTotalMarks();

			cs.setTotalScore(mk); // setTotalMarks(mk);
			cs.setQuestionCount(mk / 10);
			int scored = rep.getTotalScoredMark(); // getScoredMarks();
			cs.setTotalScoredMark(scored); // setScoredMarks(scored);
			totalMarksCoding += scored;
		}
		while (index < codingSection.size()) {
			CodingSection cs = codingSection.get(index);

			cs.setTotalAttempts(0);
			cs.setSolvedCount(0);
			int mk = getSectionScore(index + 1);
			cs.setTotalScore(mk); // setTotalMarks(mk);
			cs.setQuestionCount(mk / 10);
			cs.setTotalScoredMark(0); // setScoredMarks(0);
			index++;
		}
	}

	  private int getSectionScore(int section) throws SQLException {
	    	Connection connection = DBConnectionManager.getConnection();
	    	String get_SectionScore = "SELECT SUM(mark) AS totalScore FROM coding_questions WHERE section_id = ?";
	    	PreparedStatement ps = connection.prepareStatement(get_SectionScore);
	    	ps.setInt(1, section);
	    	ResultSet rs = ps.executeQuery();
	    	int score = 0 ;
	    	if (rs.next()) {
	    		score = rs.getInt("totalScore");
	    		System.out.println("scaore "+score);
	    	}
	    	return score;
	    }

	public boolean updateNewScore(int newScore, int sectionId, int questionId) {

		String query = "UPDATE student_coding_info SET score = ? WHERE ztai_id = ? AND section_id = ? AND question_id = ? AND score < ?";
		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(query);) {

			// Check if the new score is greater than the existing score

			stmt.setInt(1, newScore);
			stmt.setString(2, studentId); // Assuming you have studentId variable
			stmt.setInt(3, sectionId);
			stmt.setInt(4, questionId);
			stmt.setInt(5, newScore); // New score should be greater than existing score

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateAttemptCount(int sectionId, int questionId,String studentId) {
		System.out.println("Update attemos " + sectionId +"   "+questionId +" "+studentId);

		String selectSQL = "SELECT attempt FROM student_coding_info WHERE section_id = ? AND question_id = ? AND ztai_id = ?";
		String updateSQL = "UPDATE student_coding_info SET attempt = ? WHERE section_id = ? AND question_id = ? AND ztai_id = ?";

		try (Connection con = DBConnectionManager.getConnection();
				PreparedStatement selectStmt = con.prepareStatement(selectSQL);) {

			selectStmt.setInt(1, sectionId);
			selectStmt.setInt(2, questionId);
			selectStmt.setString(3, studentId);

			ResultSet resultSet = selectStmt.executeQuery();
			if (resultSet.next()) {
				int currentAttempt = resultSet.getInt("attempt");
				int newAttempt = currentAttempt + 1;

				// Update attempt count
				try (PreparedStatement updateStmt = con.prepareStatement(updateSQL)) {
					updateStmt.setInt(1, newAttempt);
					updateStmt.setInt(2, sectionId);
					updateStmt.setInt(3, questionId);
					updateStmt.setString(4, studentId);

					updateStmt.executeUpdate();
					System.out.println("Attempt count updated successfully.");
				}
			} else {
				System.out.println("No record found with the given sectionId and questionId.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<CodingSection> getCodingSection() {
		return codingSection;
	}

	public ArrayList<CodingSection> getCodingSectionQuestions() {
		return codingSectionQuestions;
	}

	public HashMap<Integer, Integer> getQuestionsPerSection() {
		return questionsPerSection;
	}

	public HashMap<String, CodingSection> getEachQuestionReport() {
		return mp;
	}

	public int getTotalSolvedCoding() {
		return totalSolvedCoding;
	}

	public int getTotalAttemptsCoding() {
		return totalAttemptsCoding;
	}

	public int getTotalMarksCoding() {
		return totalMarksCoding;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

}
