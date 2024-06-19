package com.ztai.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class LoadCodeEditorAction extends ActionSupport{
	
	 private String sectionId;
	 private String questionId;
	 

    @Override
    public String execute() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();

        // Get sectionId and questionId from the URL parameters
        sectionId = request.getParameter("sectionId");
        questionId = request.getParameter("questionId");

        // Add sectionId and questionId to session
        session.setAttribute("sectionId", sectionId);
        session.setAttribute("questionId", questionId);

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
    
    

}
