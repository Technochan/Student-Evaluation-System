package com.ztai.model;

public class QuizSection extends Activity{
	
	private int duration;
	private int questionCount;
	private int solvedCount;
	private String difficulty;
	
	public QuizSection(int sectionId,String sectionName) {
		super(sectionId, sectionName);
	}
	
	public QuizSection(int solvedCount,int totalScoredMarks,int totalScore) {
		super(totalScore,totalScoredMarks);
		this.solvedCount = solvedCount;
	}
	
	public QuizSection(int sectionId, int questionId, String questionName, String difficulty, int totalScore, int duration) {
		super(sectionId,questionId,questionName,totalScore);
		this.difficulty = difficulty;
		this.duration = duration;	
	}
	public QuizSection(int totalScoredMark, boolean isSolved) {
		super(totalScoredMark,isSolved);
	}
	

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
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
