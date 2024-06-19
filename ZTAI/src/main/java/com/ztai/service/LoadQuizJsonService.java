package com.ztai.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ztai.dao.mcq.MCQInfoDAO;
import com.ztai.util.DBConnectionManager;

public class LoadQuizJsonService {

	private MCQInfoDAO mcqInfoDAO;
	
	public LoadQuizJsonService() {
		mcqInfoDAO = new MCQInfoDAO();
	}
	
	public int loadStartNumber(String sectionId,String questionId, String studentId) {
		
     int section_Id = Integer.parseInt(sectionId);
     int question_Id = Integer.parseInt(questionId);
		  
	 int startQuestionNumber = mcqInfoDAO.getStartQuestion(studentId,section_Id,question_Id);
	 return startQuestionNumber;
	}

	public void updateQuizScore(int correctCount, int wrongCount, int totalQuestions, int sectionId, int questionId,String studentId) {
		mcqInfoDAO.updateQuizScore(correctCount, wrongCount, totalQuestions,sectionId, questionId,studentId);
	}

    

}
