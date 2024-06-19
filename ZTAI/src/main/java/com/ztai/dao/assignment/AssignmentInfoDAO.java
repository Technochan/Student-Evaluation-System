package com.ztai.dao.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ztai.model.Assignment;
import com.ztai.model.SpecificAssignment;
import com.ztai.util.DBConnectionManager;



public class AssignmentInfoDAO {
	
	List<Assignment> assignmentSections = new ArrayList<>();
    List<Assignment> assignmentQuestions= new ArrayList<>();
    Set<String> completedAssignments = new HashSet<>();
    
	SpecificAssignment specificAssignmentInfo ;
	List<String> assignmentQuestionFile = new ArrayList<>();
    int assignmentCompletedCount = 0;
    
    private String studentId;
    
	public void loadAssignmentDetailsFromDB(String studentId) {
		this.studentId = studentId;
		loadCompletedAssignments();
		loadAllAssignments(); 
	}

	  private void loadAllAssignments() {
	        String sectionQuery = "SELECT * FROM assignment_section";
	        String questionsQuery = "SELECT * FROM assignment_questions";
			/* loadCodingPage lp = new loadCodingPage(); */
	        try (Connection con = DBConnectionManager.getConnection();
	             PreparedStatement sectionStmt = con.prepareStatement(sectionQuery);
	             PreparedStatement questionsStmt = con.prepareStatement(questionsQuery);
	             ResultSet sectionRs = sectionStmt.executeQuery();
	             ResultSet questionsRs = questionsStmt.executeQuery()) {

	            while (sectionRs.next()) {
	                int sectionId = sectionRs.getInt("section_id");
	                String sectionName = sectionRs.getString("section_name");
	                Assignment section = new Assignment(sectionId, sectionName);
	                assignmentSections.add(section);
	            }

	            while (questionsRs.next()) {
	                int sectionId = questionsRs.getInt("section_id");
	                int assignmentId = questionsRs.getInt("assignment_id");
	                String assignmentName = questionsRs.getString("title");
	                String assignmentKey = sectionId + "" + assignmentId;
	                boolean solved = completedAssignments.contains(assignmentKey);
	                if(solved) {
	                	assignmentCompletedCount++;
	                }
	                Assignment question = new Assignment(sectionId, assignmentId, assignmentName, solved);
	                assignmentQuestions.add(question);
	            }
	        } catch (SQLException e) {
				e.printStackTrace();
			}
	    }

	    private void loadCompletedAssignments() {
	        String query = "SELECT section_id, assignment_id FROM student_assignment_info WHERE ztai_id = ?";

	        try (Connection con = DBConnectionManager.getConnection();
	             PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, studentId);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    int sectionId = rs.getInt("section_id");
	                    int assignmentId = rs.getInt("assignment_id");
	                    String assignmentKey = sectionId + "" + assignmentId;
	                    completedAssignments.add(assignmentKey);
	                }
	            }
	        } catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    
		public void loadSpecificAssignment(int sectionId, int assignmentId,String studentId) {
			specificAssignmentInfo = loadCurrAssignmentDetails(sectionId,assignmentId,studentId); 
			assignmentQuestionFile = loadAssignmentContent(specificAssignmentInfo.getFilepath());
		}
		
		private List<String> loadAssignmentContent(String filepath) {
		    List<String> assignmentQuestionFile = new ArrayList<>();
		    try {
		        File file = new File(filepath);
		        FileReader fr = new FileReader(file);
		        BufferedReader br = new BufferedReader(fr);
		        
		        String line;
		        while ((line = br.readLine()) != null) {
		            assignmentQuestionFile.add(line);
		        }
		        br.close(); 
		    } catch (Exception e) {
		        e.printStackTrace();
		    } 
		    return assignmentQuestionFile;
		}


		private SpecificAssignment loadCurrAssignmentDetails(int sectionId, int assignmentId, String studentId) {
		    SpecificAssignment spa = null;
		    try (Connection con = DBConnectionManager.getConnection();
		         PreparedStatement ps = con.prepareStatement("SELECT * FROM assignment_questions WHERE section_id = ? AND assignment_id = ?");
		         PreparedStatement ps2 = con.prepareStatement("SELECT submitted_date FROM student_assignment_info WHERE ztai_id = ? AND section_id = ? AND assignment_id = ?")) {
		        
		        ps.setInt(1, sectionId);
		        ps.setInt(2, assignmentId);
		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            String title = rs.getString("title");
		            String postedDate = rs.getString("posted_date");
		            String filePath = rs.getString("filepath");

		            ps2.setString(1, studentId);
		            ps2.setInt(2, sectionId);
		            ps2.setInt(3, assignmentId);
		            ResultSet rs2 = ps2.executeQuery();

		            String submittedDate = "empty";
		            if (rs2.next()) {
		                submittedDate = rs2.getString("submitted_date");
		                System.out.println("loaded again: " + submittedDate);
		            }
		            System.out.println("posted date " + postedDate);
		            System.out.println("submitted date " + submittedDate);
		            spa = new SpecificAssignment(sectionId, assignmentId, title, postedDate, submittedDate, filePath);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return spa;
		}

		public void updateStudentSubmittedTable(String studentId,SpecificAssignment specificAssignment, String downloadPath) {
			int sectionId = specificAssignment.getSectionId();
		    int assignmentId = specificAssignment.getAssignmentId();
		    String query = "INSERT INTO student_assignment_info (ztai_id, section_id, assignment_id, submitted_date, filepath) VALUES (?, ?, ?, ?, ?)";
		    
		    LocalDate today = LocalDate.now();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
		    String currDate = today.format(formatter);
		    
		    try(Connection con = DBConnectionManager.getConnection();
		    		 PreparedStatement pstmt = con.prepareStatement(query)) {

		        pstmt.setString(1, studentId);
		        pstmt.setInt(2, sectionId);
		        pstmt.setInt(3, assignmentId);
		        pstmt.setString(4, currDate);
		        pstmt.setString(5, downloadPath);
		        pstmt.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace(); 
		    } 
			
		}
		
		public SpecificAssignment getSpecificAssignmentInfo() {
			return specificAssignmentInfo;
		}

		public void setSpecificAssignmentInfo(SpecificAssignment specificAssignmentInfo) {
			this.specificAssignmentInfo = specificAssignmentInfo;
		}

		public List<String> getAssignmentQuestionFile() {
			return assignmentQuestionFile;
		}

		public void setAssignmentQuestionFile(List<String> assignmentQuestionFile) {
			this.assignmentQuestionFile = assignmentQuestionFile;
		}

		public List<Assignment> getAssignmentSections() {
			return assignmentSections;
		}

		public void setAssignmentSections(List<Assignment> assignmentSections) {
			this.assignmentSections = assignmentSections;
		}

		public List<Assignment> getAssignmentQuestions() {
			return assignmentQuestions;
		}

		public void setAssignmentQuestions(List<Assignment> assignmentQuestions) {
			this.assignmentQuestions = assignmentQuestions;
		}

		public int getAssignmentCompletedCount() {
			return assignmentCompletedCount;
		}

		public void setAssignmentCompletedCount(int assignmentCompletedCount) {
			this.assignmentCompletedCount = assignmentCompletedCount;
		}

}
