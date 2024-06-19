package com.ztai.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ztai.service.LoadQuizJsonService;


public class LoadQuizQuestionAction extends ActionSupport{
	
	private LoadQuizJsonService loadQuizJsonService;
	
	public LoadQuizQuestionAction(){
		loadQuizJsonService = new LoadQuizJsonService();
	}
	
	 private String sectionId;
	 private String questionId;
	 private int startQuestionNumber;
	 
	  @Override
	    public String execute(){
		  String studentId = "ztai012024";
	        HttpServletRequest request = ServletActionContext.getRequest();
	        HttpSession session = request.getSession();

	        // Get sectionId and questionId from the URL parameters
	        sectionId = request.getParameter("sectionId");
	        questionId = request.getParameter("questionId");

	        // Add sectionId and questionId to session
	        session.setAttribute("sectionId", sectionId);
	        session.setAttribute("questionId", questionId);
	        startQuestionNumber = loadQuizJsonService.loadStartNumber(sectionId,questionId,studentId);

	        return SUCCESS;
	    }

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public int getStartQuestionNumber() {
		return startQuestionNumber;
	}

	public void setStartQuestionNumber(int startQuestionNumber) {
		this.startQuestionNumber = startQuestionNumber;
	}
	  
	  
}
