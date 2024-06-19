package com.ztai.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ztai.controller.LoadCodingInfoAction;
import com.ztai.dao.coding.CodingInfoDAO;
import com.ztai.model.CodingSection;
import com.ztai.model.TestResult;

public class CodeRunSubmitService {

	LoadCodingInfoAction loadCodingInfoAction;
	CompileAndRun compileRun;

	public CodeRunSubmitService(LoadCodingInfoAction loadCodingInfoAction) {
		this.loadCodingInfoAction = loadCodingInfoAction;
		compileRun = new CompileAndRun();
	}

	public TestResult perform(int count, String content) {
		String codeContent = content;

		String filePath = "/home/zs-gsch05/Desktop/Eclipse-New-Workspace/ZTAI/src/main/java/codesubmission";
		codeContent = "package codesubmission;" + "\n" + codeContent;
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try (FileWriter writer = new FileWriter(new File(dir, "Main.java"))) {
			System.out.println("Writtens");
			writer.write(codeContent);
			System.out.println(codeContent);
		} catch (IOException e) {
			e.printStackTrace();

		}

		
		TestResult trs = compileRun.start(count);

		HttpSession session = ServletActionContext.getRequest().getSession();
		// session.setAttribute("testResult", trs);

		System.out.println(session.getAttribute("sectionId") + "  got it");
		System.out.println(session.getAttribute("questionId") + "  got it");

		System.out.println(trs.getCompilationError() + "  error");
		System.out.println(trs.getNewScore() + "  new score");

		System.out.println("What happen");

		int score = trs.getNewScore();
		System.out.println(score + "   score ley");

		// for new score updation
		if (score > 0 && count != 2) {
			System.out.println("ena ley");

			int sectionId = Integer.parseInt((String) session.getAttribute("sectionId"));
			int questionId = Integer.parseInt((String) session.getAttribute("questionId"));
			CodingInfoDAO cd = new CodingInfoDAO();

			boolean changed = cd.updateNewScore(score, sectionId, questionId);
			if (changed)
				updateSessionScores(score, sectionId, questionId, session);

		}
		// for attempts increase
		if (count != 2) {
			System.out.println("Hello chan");
			int sectionId = Integer.parseInt((String) session.getAttribute("sectionId"));
			int questionId = Integer.parseInt((String) session.getAttribute("questionId"));
			CodingInfoDAO cd = new CodingInfoDAO();

			cd.updateAttemptCount(sectionId, questionId,"ztai012024");
			System.out.println("YEs bro returnedas");
			//updateSessionAttempts(sectionId, questionId, session);
		}

		System.out.println("Yes returned");
		return trs;
	}

	private void updateSessionAttempts(int sectionId, int questionId, HttpSession session) {
		String oldTotalAttemps = (String) session.getAttribute("totalStudentAttempts");
		int preTotalAttempts = Integer.parseInt(oldTotalAttemps);
		session.setAttribute("totalStudentAttempts", preTotalAttempts + 1);
		System.out.println("attempt new succes set");

		ArrayList<CodingSection> codingSections = loadCodingInfoAction.getCodingSections(); // (ArrayList<CodingSection>)
		System.out.println("Cheking cont get "+codingSections);// session.getAttribute("codingSections");
		for (CodingSection cs : codingSections) {
			if (cs.getSectionId() == sectionId) {
				cs.setTotalAttempts(cs.getTotalAttempts() + 1);
				break;
			}
		}

		ArrayList<CodingSection> codingSectionQuestions = loadCodingInfoAction.getCodingSectionQuestions(); // (ArrayList<CodingSection>)
																											// session.getAttribute("codingSectionQuestions");
		for (CodingSection csq : codingSectionQuestions) {
			if (csq.getSectionId() == sectionId && csq.getQuestionId() == questionId) {
				csq.setTotalAttempts(csq.getTotalAttempts() + 1);
				break;
			}
		}

		HashMap<String, CodingSection> mp = loadCodingInfoAction.getQuestioninfo(); // (HashMap<String,CodingSection>)session.getAttribute("questioninfo");
		
		String target = "section" + sectionId + "question" + questionId;
		CodingSection csq = mp.get(target);
		csq.setTotalAttempts(csq.getTotalAttempts() + 1);
		mp.put(target, csq);
	}

	private void updateSessionScores(int score, int sectionId, int questionId, HttpSession session) {

		int preTotalCardMarks = (int) session.getAttribute("totalStudentMarks");
		session.setAttribute("totalStudentMarks", preTotalCardMarks + score);

		ArrayList<CodingSection> codingSections = loadCodingInfoAction.getCodingSections(); // (ArrayList<CodingSection>)
																							// session.getAttribute("codingSections");
		for (CodingSection cs : codingSections) {
			if (cs.getSectionId() == sectionId) {
				cs.setTotalScoredMark(score); // setScoredMarks(score);
				break;
			}
		}

		ArrayList<CodingSection> codingSectionQuestions = loadCodingInfoAction.getCodingSectionQuestions(); // (ArrayList<CodingSection>)
																											// session.getAttribute("codingSectionQuestions");
		for (CodingSection csq : codingSectionQuestions) {
			if (csq.getSectionId() == sectionId && csq.getQuestionId() == questionId) {
				csq.setTotalScoredMark(score); // setScore(score);
				break;
			}
		}

		HashMap<String, CodingSection> mp = loadCodingInfoAction.getQuestioninfo(); // (HashMap<String,CodingSection>
																					// )session.getAttribute("questioninfo");
		String target = "section" + sectionId + "question" + questionId;
		CodingSection csq = mp.get(target);
		csq.setTotalScoredMark(score); // setScore(score);
		mp.put(target, csq);
	}
}
