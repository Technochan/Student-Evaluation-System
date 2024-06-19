package com.ztai.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.ztai.dao.coding.CodingInfoDAO;
import com.ztai.model.CodingSection;

public class LoadCodingService {

	private CodingInfoDAO codingInfoDAO;

	public LoadCodingService() {
		codingInfoDAO = new CodingInfoDAO();
	}

	public void loadCodingPageInfo(String studentId) {
		codingInfoDAO.loadCodingPageDetails(studentId);
	}

	public ArrayList<CodingSection> getCodingSectionInfo() {
		return codingInfoDAO.getCodingSection();
	}

	public ArrayList<CodingSection> getCodingSectionQuestionInfo() {
		return codingInfoDAO.getCodingSectionQuestions();
	}

	public HashMap<String, CodingSection> getEachQuestionReport() {
		return codingInfoDAO.getEachQuestionReport();
	}

	public HashMap<Integer, Integer> getQuestionPerSectionReport() {
		return codingInfoDAO.getQuestionsPerSection();
	}

	public int getTotalSolvedCodingCount() {
		return codingInfoDAO.getTotalSolvedCoding();
	}

	public int getTotalAttemptsCoding() {
		return codingInfoDAO.getTotalAttemptsCoding();
	}

	public int getTotalMarksCoding() {
		return codingInfoDAO.getTotalMarksCoding();
	}

	public int getTotalQuestions() {
		return codingInfoDAO.getTotalQuestions();
	}

	public int getTotalMarks() {
		return codingInfoDAO.getTotalMarks();
	}

}
