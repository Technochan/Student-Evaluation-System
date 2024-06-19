package com.ztai.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ztai.dao.assignment.AssignmentInfoDAO;
import com.ztai.model.Assignment;
import com.ztai.model.SpecificAssignment;

public class LoadAssignmentService {
	
	private AssignmentInfoDAO assignmentInfoDAO;
	
	public LoadAssignmentService(){
		assignmentInfoDAO = new AssignmentInfoDAO();
	}
	

	public void loadAssignmentDetails(String studentId) {
		assignmentInfoDAO.loadAssignmentDetailsFromDB(studentId);
	}
	
	public void loadSpecificAssignment(int sectionId, int assignmentId,String studentId) {
		assignmentInfoDAO.loadSpecificAssignment(sectionId, assignmentId, studentId);
	}

	public List<Assignment> getAssignmentSections() {
		return assignmentInfoDAO.getAssignmentSections();
	}

	public List<Assignment> getAssignmentQuestions() {
		return assignmentInfoDAO.getAssignmentQuestions();
	}

	public int getAssignmentCompletedCount() {
		return assignmentInfoDAO.getAssignmentCompletedCount();
	}


	public List<String> getassignmentQuestionFile() {
		return assignmentInfoDAO.getAssignmentQuestionFile();
	}


	public SpecificAssignment getSpecificAssignmentObj() {
		return assignmentInfoDAO.getSpecificAssignmentInfo();
	}


	public Boolean submitAssignment(String studentId,File file,String downloadPath,SpecificAssignment specificAssignment) {
		  try {
	            FileUtils.writeByteArrayToFile(new File(downloadPath), FileUtils.readFileToByteArray(file));
	            assignmentInfoDAO.updateStudentSubmittedTable(studentId, specificAssignment,downloadPath);
	            System.out.println("Success download");
	            return true;
	        } catch (IOException e) {
	        	return false;
	        }
		
	}



}
