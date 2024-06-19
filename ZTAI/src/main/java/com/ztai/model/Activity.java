package com.ztai.model;

public abstract class Activity implements ActivityInterface {
	
	protected int sectionId;
	protected String sectionName;
	protected int questionId;
	protected String questionName;
	protected boolean isSolved;
	protected int totalScore;
	protected int totalScoredMark;
	
	
	public Activity(int sectionId) {
        this.sectionId = sectionId;
	}
	
	public Activity(int sectionId, String sectionName) {
	        this.sectionId = sectionId;
	        this.sectionName = sectionName;
	}

	 public Activity(int totalScore, int totalScoredMark) {
	        this.totalScore = totalScore;
	        this.totalScoredMark = totalScoredMark;
	}
	
	
	public Activity(int sectionId, String sectionName, int questionId, String questionName, boolean isSolved, int totalScore, int totalScoredMark) {
		this.sectionId = sectionId;
		this.sectionName = sectionName;
		this.questionId = questionId;
		this.questionName = questionName;
		this.isSolved = isSolved;
		this.totalScore = totalScore;
		this.totalScoredMark = totalScoredMark;
	}
	
	public Activity(int totalScoredMark, boolean isSolved) {
		this.totalScoredMark= totalScoredMark;
		this.isSolved = isSolved;
	}

	public Activity(int sectionId, int questionId, String questionName, int totalScore, int totalScoredMark, boolean isSolved) {
		this.sectionId = sectionId;
		this.questionId = questionId;
		this.questionName = questionName;
		this.totalScore = totalScore;
		this.totalScoredMark = totalScoredMark;
		this.isSolved = isSolved;
	}

	public Activity(int sectionId, int questionId, String questionName, int totalScore) {
		this.sectionId = sectionId;
		this.questionId = questionId;
		this.questionName = questionName;
		this.totalScore = totalScore;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public void setTotalScoredMark(int totalScoredMark) {
		this.totalScoredMark = totalScoredMark;
	}
	@Override
	public int getSectionId() {
		return sectionId;
	}
	@Override
	public String getSectionName() {
		return sectionName;
	}
	@Override
	public int getQuestionId() {
		return questionId;
	}
	@Override
	public String getQuestionName() {
		return questionName;
	}
	@Override
	public boolean getIsSolved() {
		return isSolved;
	}
	@Override
	public int getTotalScore() {
		return totalScore;
	}
	@Override
	public int getTotalScoredMark() {
		return totalScoredMark;
	}
	
	
}
