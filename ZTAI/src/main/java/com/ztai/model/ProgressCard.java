package com.ztai.model;


public class ProgressCard {
	
	private int progressCardId;
	private String progressCardName;
	private String progressCardType;
	private int progressCardTotalSections;
	private int progressCardTotalSubSections;
	private int progressCardTotalMarks;
	private int progressCardStudentTotalMarks;
	private int percentage;

	public ProgressCard(int progressCardId, String progressCardName,String progressCardType, int progressCardTotalSections, int progressCardTotalSubSections, int progressCardTotalMarks, int progressCardStudentTotalMarks,int percentage) {
		this.progressCardId = progressCardId;
		this.progressCardName = progressCardName;
		this.progressCardType = progressCardType;
		this.progressCardTotalSections = progressCardTotalSections;
		this.progressCardTotalSubSections = progressCardTotalSubSections;
		this.progressCardTotalMarks = progressCardTotalMarks;
		this.progressCardStudentTotalMarks = progressCardStudentTotalMarks;
		this.percentage = percentage;
	}
	
	public int getProgressCardId() {
		return progressCardId;
	}
	public void setProgressCardId(int progressCardId) {
		this.progressCardId = progressCardId;
	}
	public String getProgressCardName() {
		return progressCardName;
	}
	public void setProgressCardName(String progressCardName) {
		this.progressCardName = progressCardName;
	}
	
	public String getProgressCardType() {
		return progressCardType;
	}

	public void setProgressCardType(String progressCardType) {
		this.progressCardType = progressCardType;
	}

	public int getProgressCardTotalSections() {
		return progressCardTotalSections;
	}
	public void setProgressCardTotalSections(int progressCardTotalSections) {
		this.progressCardTotalSections = progressCardTotalSections;
	}
	public int getProgressCardTotalSubSections() {
		return progressCardTotalSubSections;
	}
	public void setProgressCardTotalSubSections(int progressCardTotalSubSections) {
		this.progressCardTotalSubSections = progressCardTotalSubSections;
	}
	public int getProgressCardTotalMarks() {
		return progressCardTotalMarks;
	}
	public void setProgressCardTotalMarks(int progressCardTotalMarks) {
		this.progressCardTotalMarks = progressCardTotalMarks;
	}

	public int getProgressCardStudentTotalMarks() {
		return progressCardStudentTotalMarks;
	}

	public void setProgressCardStudentTotalMarks(int progressCardStudentTotalMarks) {
		this.progressCardStudentTotalMarks = progressCardStudentTotalMarks;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	
	
}
