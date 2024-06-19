package com.ztai.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ztai.model.Assignment;
import com.ztai.model.SpecificAssignment;
import com.ztai.service.LoadAssignmentService;


public class LoadAssignmentInfoAction extends ActionSupport {

	private LoadAssignmentService loadAssignmentService;

	public LoadAssignmentInfoAction() {
		loadAssignmentService = new LoadAssignmentService();
	}

	private  int sectionId;
	private  int assignmentId;
	SpecificAssignment specificAssignment ;
	String assignemntPostedStatus;

	List<Assignment> assignmentSections = new ArrayList<>();
	List<Assignment> assignmentQuestions = new ArrayList<>();
	List<String> assignmentQuestionFile = new ArrayList<>();
	
	private File file;

	int totalAssignmentSections = 0;
	int totalAssignmentQuestions = 0;
	int assignmentCompletedCount = 0;
	String studentId = "ztai012024";

	@Override
	public String execute() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();

        // Add sectionId and questionId to session
        session.setAttribute("sectionId", sectionId);
        session.setAttribute("questionId", assignmentId);

		loadAssignmentService.loadAssignmentDetails(studentId);
		assignmentSections = loadAssignmentService.getAssignmentSections();
		assignmentQuestions = loadAssignmentService.getAssignmentQuestions();
		assignmentCompletedCount = loadAssignmentService.getAssignmentCompletedCount();
		totalAssignmentSections = assignmentSections.size();
		totalAssignmentQuestions = assignmentQuestions.size();
		return SUCCESS;
	}

	public String loadSpecificAssignment() {
		System.out.println("Coming to load specific assignment");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String sec = request.getParameter("sectionId");
		String ass = request.getParameter("assignmentId");
		System.out.println(sec);
		System.out.println(ass);

        // Get sectionId and questionId from the URL parameters
        sectionId = Integer.parseInt((String) request.getParameter("sectionId")); 
        assignmentId = Integer.parseInt((String)request.getParameter("assignmentId"));
        System.out.println("After getting from session");

		loadAssignmentService.loadSpecificAssignment(sectionId, assignmentId,studentId);
		assignmentQuestionFile = loadAssignmentService.getassignmentQuestionFile();
		System.out.println("assignment gettesd" + assignmentQuestionFile);
		specificAssignment = loadAssignmentService.getSpecificAssignmentObj();
		System.out.println("assgnment specif obj + "+specificAssignment );
		return SUCCESS;
	}
	
	public String submitAssignment() {
		String fileName = "assignment"+specificAssignment.getAssignmentId()+".zip";
		String downloadPath = "/home/zs-gsch05/Desktop/Eclipse-New-Workspace/CodingPageSetup/src/main/webapp/assignmentdownloaded/chan/section"+specificAssignment.getSectionId()+"/"+ fileName; 
		boolean status = loadAssignmentService.submitAssignment(studentId,file,downloadPath,specificAssignment);
		if(status) {
			assignemntPostedStatus = "Success";
		} else {
			assignemntPostedStatus = "Failed";
		}
		/* session.setAttribute("assignemntPostedStatus","Success"); */
		return SUCCESS;
	}
	
	

	public String getAssignemntPostedStatus() {
		return assignemntPostedStatus;
	}

	public void setAssignemntPostedStatus(String assignemntPostedStatus) {
		this.assignemntPostedStatus = assignemntPostedStatus;
	}

	public SpecificAssignment getSpecificAssignment() {
		return specificAssignment;
	}

	public void setSpecificAssignment(SpecificAssignment specificAssignment) {
		this.specificAssignment = specificAssignment;
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

	public int getTotalAssignmentSections() {
		return totalAssignmentSections;
	}

	public void setTotalAssignmentSections(int totalAssignmentSections) {
		this.totalAssignmentSections = totalAssignmentSections;
	}

	public int getTotalAssignmentQuestions() {
		return totalAssignmentQuestions;
	}

	public void setTotalAssignmentQuestions(int totalAssignmentQuestions) {
		this.totalAssignmentQuestions = totalAssignmentQuestions;
	}

	public int getAssignmentCompletedCount() {
		return assignmentCompletedCount;
	}

	public void setAssignmentCompletedCount(int assignmentCompletedCount) {
		this.assignmentCompletedCount = assignmentCompletedCount;
	}

}
