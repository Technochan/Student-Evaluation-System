package com.ztai.model;

public class CodingSection extends Activity{
	
	private int questionCount;
	private int totalAttempts;
	private int solvedCount;
	private String difficulty;
	
	public CodingSection(int sectionId,String sectionName) {
		super(sectionId,sectionName);
	}
	
	public CodingSection(int totalAttempts,int solvedCount,int totalScoredMark, int totalScore) {
		super(totalScore,totalScoredMark);
		this.totalAttempts = totalAttempts;
		this.solvedCount = solvedCount;
	}

	public CodingSection(int totalAttempts,int totalScoredMark,boolean isSolved) {
		super(totalScoredMark,isSolved);
		this.totalAttempts = totalAttempts;
	}
	
	public CodingSection(int sectionId, int questionId, String questionName, String difficulty, int totalScore) {
		super(sectionId, questionId, questionName, totalScore,0,false);
		this.sectionId = sectionId;
		this.questionId = questionId;
		this.questionName = questionName;
		this.difficulty = difficulty;
		totalAttempts = 0;
	}
	

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public int getTotalAttempts() {
		return totalAttempts;
	}

	public void setTotalAttempts(int totalAttempts) {
		this.totalAttempts = totalAttempts;
	}

	public int getSolvedCount() {
		return solvedCount;
	}

	public void setSolvedCount(int solvedCount) {
		this.solvedCount = solvedCount;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	

}
