package com.ztai.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.ztai.service.LoadQuizJsonService;


public class UpdateQuizScoreAction extends ActionSupport{
	
	
	private LoadQuizJsonService loadQuizJsonService;
	
	public UpdateQuizScoreAction() {
		loadQuizJsonService = new LoadQuizJsonService();
	}

	private int correctCount;
    private int wrongCount;
    private int totalQuestions;
    
    @Override
    public String execute() throws Exception {
    	 try {
    		 String studentId = "ztai012024";
    		 
             HttpServletRequest request = ServletActionContext.getRequest();;
             // Read JSON data from the request
             BufferedReader reader = request.getReader();
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = reader.readLine()) != null) {
                 sb.append(line);
             }

 
             Gson gson = new Gson();
             QuizData quizData = gson.fromJson(sb.toString(), QuizData.class);
             correctCount = quizData.getCorrectCount();
             wrongCount = quizData.getWrongCount();
             totalQuestions = quizData.getTotalQuestions();
             
             // get these from session
             int sectionId = 1;
             int questionId = 1;


             loadQuizJsonService.updateQuizScore(correctCount,wrongCount,totalQuestions,sectionId, questionId,studentId);
             
             System.out.println("Get out "+correctCount);
             System.out.println("Get wroing "+wrongCount );
             System.out.println("Total "+totalQuestions);

             // Your logic here to update the database with correctCount and wrongCount

         } catch (IOException e) {
             e.printStackTrace();
         }
    	
    	return SUCCESS;
    }

	public int getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	 static class QuizData {
	        private int correctCount;
	        private int wrongCount;
	        private int totalQuestions;

	        public int getCorrectCount() {
	            return correctCount;
	        }

	        public void setCorrectCount(int correctCount) {
	            this.correctCount = correctCount;
	        }

	        public int getWrongCount() {
	            return wrongCount;
	        }

	        public void setWrongCount(int wrongCount) {
	            this.wrongCount = wrongCount;
	        }
	        public int getTotalQuestions() {
	        	return totalQuestions;
	        }
	        
	        public void setTotalQuestions(int totalQuestions) {
	        	this.totalQuestions = totalQuestions;
	        }
	    }
    
}
