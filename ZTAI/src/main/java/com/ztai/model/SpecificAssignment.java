package com.ztai.model;

public class SpecificAssignment {
	
	private int sectionId;
	private int assignmentId;
	private String currAssignmentName;
	private String postedDate;
	private String submittedDate;
	private String filepath;
	
	
	public SpecificAssignment(int sectionId, int assignmentId, String currAssignmentName, String postedDate, String submittedDate,String filepath) {
		this.sectionId = sectionId;
		this.assignmentId = assignmentId;
		this.currAssignmentName = currAssignmentName;
		this.postedDate = postedDate;
		this.submittedDate = submittedDate;
		this.filepath = filepath;
	}
	
	
	public int getSectionId() {
		return sectionId;
	}


	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}


	public int getAssignmentId() {
		return assignmentId;
	}


	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}


	public String getCurrAssignmentName() {
		return currAssignmentName;
	}
	public void setCurrAssignmentName(String currAssignmentName) {
		this.currAssignmentName = currAssignmentName;
	}
	public String getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}
	public String getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(String submittedDate) {
		this.submittedDate = submittedDate;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	
	
}
