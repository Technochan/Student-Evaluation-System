package com.ztai.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ztai.model.CodingSection;
import com.ztai.model.TestResult;
import com.ztai.service.CodeRunSubmitService;
import com.ztai.service.LoadCodingService;

public class LoadCodingInfoAction extends ActionSupport {

	private LoadCodingService loadCodingService;
	private CodeRunSubmitService codeRunSubmitService;
	
	public LoadCodingInfoAction() {
		loadCodingService = new LoadCodingService();
		codeRunSubmitService = new CodeRunSubmitService(this);
	}

	private ArrayList<CodingSection> codingSections = new ArrayList<CodingSection>();
	private ArrayList<CodingSection> codingSectionQuestions = new ArrayList<CodingSection>();
	private static HashMap<String, CodingSection> questioninfo = new HashMap<String, CodingSection>(); // student each question
																								// based
	// attempts , scored
	private static HashMap<Integer, Integer> questionsPerSection = new HashMap<>();
	
	private int totalSolvedCoding;
	private int totalAttemptsCoding;
	private int totalMarksCoding;
	private int totalQuestions;
	private int totalMarks;
	
	 private String sectionId;
	 private String questionId;
	 
	 private String codeContent;
	private TestResult testResult;
	 

	@Override
	public String execute() throws Exception {
		String studentId = "ztai012024";
		loadCodingService.loadCodingPageInfo(studentId);

		codingSections = loadCodingService.getCodingSectionInfo();
		codingSectionQuestions = loadCodingService.getCodingSectionQuestionInfo();
		questioninfo = loadCodingService.getEachQuestionReport();
		questionsPerSection = loadCodingService.getQuestionPerSectionReport();
		totalSolvedCoding = loadCodingService.getTotalSolvedCodingCount();
		totalAttemptsCoding = loadCodingService.getTotalAttemptsCoding();
		totalMarksCoding = loadCodingService.getTotalMarksCoding();
		totalQuestions = loadCodingService.getTotalQuestions();
		totalMarks = loadCodingService.getTotalMarks();
		
		System.out.println(codingSections.get(0).getTotalAttempts() + " attemps");
		System.out.println(codingSections.get(0).getSolvedCount() + " sol");
		System.out.println(codingSections.get(0).getTotalScoredMark() + " score");
		
		/*
		 * HttpServletRequest request = ServletActionContext.getRequest(); HttpSession
		 * session = request.getSession();
		 * 
		 * session.setAttribute("questioninfo", questioninfo);
		 * session.setAttribute("questionsPerSection", questionsPerSection);
		 */
		
		return SUCCESS;
	}
	
	public String storeIdInSession() {
		    HttpServletRequest request = ServletActionContext.getRequest();
	        HttpSession session = request.getSession();
	        sectionId = request.getParameter("sectionId");
	        questionId = request.getParameter("questionId");
	        session.setAttribute("sectionId", sectionId);
	        session.setAttribute("questionId", questionId);
	    	System.out.println(questioninfo.size()+"     runcode cheked");
			System.out.println(questionsPerSection.size()+"     runcode cheked");
		return SUCCESS;
	}
	
	public String runCode() {
		testResult = getTestResult(2);
		System.out.println(testResult +"     test result got bro");
		System.out.println(questioninfo.size()+"     runcode cheked");
		System.out.println(questionsPerSection.size()+"     runcode cheked");
		return SUCCESS;
	}
	public String submitCode() {
		testResult = getTestResult(0);
		System.out.println(questioninfo.size()+"     submit cheked");
		System.out.println(questionsPerSection.size()+"     submit cheked");
		return SUCCESS;
	}
	
	public TestResult getTestResult(int count) {
		return codeRunSubmitService.perform(count,codeContent);
	}
	
    public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
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

	public LoadCodingService getLoadCodingService() {
		return loadCodingService;
	}

	public void setLoadCodingService(LoadCodingService loadCodingService) {
		this.loadCodingService = loadCodingService;
	}

	public ArrayList<CodingSection> getCodingSections() {
		return codingSections;
	}

	public void setCodingSections(ArrayList<CodingSection> codingSections) {
		this.codingSections = codingSections;
	}

	public ArrayList<CodingSection> getCodingSectionQuestions() {
		return codingSectionQuestions;
	}

	public void setCodingSectionQuestions(ArrayList<CodingSection> codingSectionQuestions) {
		this.codingSectionQuestions = codingSectionQuestions;
	}

	public HashMap<String, CodingSection> getQuestioninfo() {
		return questioninfo;
	}

	public void setQuestioninfo(HashMap<String, CodingSection> questioninfo) {
		this.questioninfo = questioninfo;
	}

	public HashMap<Integer, Integer> getQuestionsPerSection() {
		return questionsPerSection;
	}

	public void setQuestionsPerSection(HashMap<Integer, Integer> questionsPerSection) {
		this.questionsPerSection = questionsPerSection;
	}

	public int getTotalSolvedCoding() {
		return totalSolvedCoding;
	}

	public void setTotalSolvedCoding(int totalSolvedCoding) {
		this.totalSolvedCoding = totalSolvedCoding;
	}

	public int getTotalAttemptsCoding() {
		return totalAttemptsCoding;
	}

	public void setTotalAttemptsCoding(int totalAttemptsCoding) {
		this.totalAttemptsCoding = totalAttemptsCoding;
	}

	public int getTotalMarksCoding() {
		return totalMarksCoding;
	}

	public void setTotalMarksCoding(int totalMarksCoding) {
		this.totalMarksCoding = totalMarksCoding;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}

}
