package com.ztai.model;

public class Assignment extends Activity{
	
	private int assignmentId;
	private String assignmentName;
	private boolean isSolved;
	
	public Assignment(int sectionId, String sectionName) {
		super(sectionId, sectionName);
	}

	public Assignment(int sectionId, int assignmentId, String assignmentName, boolean isSolved) {
		super(sectionId);
		this.assignmentId = assignmentId;
		this.assignmentName = assignmentName;
		this.isSolved = isSolved;
	}

	public int getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
	
	
	
}
