package com.ztai.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.opensymphony.xwork2.ActionSupport;
import com.ztai.model.QuizSection;
import com.ztai.service.LoadMCQService;


public class LoadMCQInfoAction extends ActionSupport{


	private LoadMCQService loadMCQService;

	public LoadMCQInfoAction() {
		loadMCQService = new LoadMCQService();
	}

	private ArrayList<QuizSection> quizSections = new ArrayList<QuizSection>();
	private ArrayList<QuizSection> quizSectionQuestions = new ArrayList<QuizSection>();
	private HashMap<Integer, Integer> questionsPerSectionQuiz = new HashMap<>();
	private HashMap<String, QuizSection> quizInfo = new HashMap<>(); // student each question based attempts, scored

	private int totalQuizQuestions;
	private int totalQuizMarks;
	private int totalStudentQuizMarks;
	private int totalStudentQuizSolved;

	@Override
	public String execute() throws Exception {
		String studentId = "ztai012024";
		loadMCQService.loadMCQPageInfo(studentId);
		
		quizSections = loadMCQService.getQuizSectionInfo();
		quizSectionQuestions = loadMCQService.getQuizSectionQuestionsInfo();
		quizInfo = loadMCQService.getEachQuestionReport();
		questionsPerSectionQuiz = loadMCQService.getQuizQuestionPerSectionInfo();
		totalQuizQuestions = loadMCQService.getTotalQuizQuestionCount();
		totalQuizMarks = loadMCQService.getTotalQuizMarks();
		totalStudentQuizMarks = loadMCQService.getTotalStudentQuizMarks();
		totalStudentQuizSolved = loadMCQService.loadTotalStudentQuizSolvedCount();
		
		System.out.println(quizSections.get(0).getTotalScore() +"    haui");
		System.out.println(quizSections.get(0).getTotalScoredMark() +"   jhelk");
		
		return SUCCESS;
	}
	
	

	public HashMap<String, QuizSection> getQuizInfo() {
		return quizInfo;
	}



	public void setQuizInfo(HashMap<String, QuizSection> quizInfo) {
		this.quizInfo = quizInfo;
	}



	public ArrayList<QuizSection> getQuizSections() {
		return quizSections;
	}

	public void setQuizSections(ArrayList<QuizSection> quizSections) {
		this.quizSections = quizSections;
	}

	public ArrayList<QuizSection> getQuizSectionQuestions() {
		return quizSectionQuestions;
	}

	public void setQuizSectionQuestions(ArrayList<QuizSection> quizSectionQuestions) {
		this.quizSectionQuestions = quizSectionQuestions;
	}

	public HashMap<Integer, Integer> getQuestionsPerSectionQuiz() {
		return questionsPerSectionQuiz;
	}

	public void setQuestionsPerSectionQuiz(HashMap<Integer, Integer> questionsPerSectionQuiz) {
		this.questionsPerSectionQuiz = questionsPerSectionQuiz;
	}

	public int getTotalQuizQuestions() {
		return totalQuizQuestions;
	}

	public void setTotalQuizQuestions(int totalQuizQuestions) {
		this.totalQuizQuestions = totalQuizQuestions;
	}

	public int getTotalQuizMarks() {
		return totalQuizMarks;
	}

	public void setTotalQuizMarks(int totalQuizMarks) {
		this.totalQuizMarks = totalQuizMarks;
	}

	public int getTotalStudentQuizMarks() {
		return totalStudentQuizMarks;
	}

	public void setTotalStudentQuizMarks(int totalStudentQuizMarks) {
		this.totalStudentQuizMarks = totalStudentQuizMarks;
	}

	public int getTotalStudentQuizSolved() {
		return totalStudentQuizSolved;
	}

	public void setTotalStudentQuizSolved(int totalStudentQuizSolved) {
		this.totalStudentQuizSolved = totalStudentQuizSolved;
	}
	
	

	
}
