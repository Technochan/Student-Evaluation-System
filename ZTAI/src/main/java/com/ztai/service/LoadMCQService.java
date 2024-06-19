package com.ztai.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.ztai.dao.mcq.MCQInfoDAO;
import com.ztai.model.CodingSection;
import com.ztai.model.QuizSection;

public class LoadMCQService {
	
	private MCQInfoDAO mcqInfoDAO;
	
	public LoadMCQService() {
		mcqInfoDAO = new MCQInfoDAO();
	}
	
	public void loadMCQPageInfo(String studentId) {
		mcqInfoDAO.loadMCQPageDetails(studentId);
	}

	public ArrayList<QuizSection> getQuizSectionInfo() {
		return mcqInfoDAO.getQuizSections();
	}

	public ArrayList<QuizSection> getQuizSectionQuestionsInfo() {
		return mcqInfoDAO.getQuizSectionQuestions();
	}
	
	public HashMap<String, QuizSection> getEachQuestionReport() {
		return mcqInfoDAO.getMp();
	}


	public HashMap<Integer, Integer> getQuizQuestionPerSectionInfo() {
		return mcqInfoDAO.getQuestionsPerSectionQuiz();
	}

	public int getTotalQuizQuestionCount() {
		return mcqInfoDAO.getTotalQuizQuestions();
	}

	public int getTotalQuizMarks() {
		return mcqInfoDAO.getTotalQuizMarks();
	}

	public int getTotalStudentQuizMarks() {
		return mcqInfoDAO.getTotalStudentQuizMarks();
	}

	public int loadTotalStudentQuizSolvedCount() {
		return mcqInfoDAO.getTotalStudentQuizSolved();
	}

}
