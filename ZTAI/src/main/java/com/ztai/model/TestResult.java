package com.ztai.model;

import java.util.ArrayList;
import java.util.List;


public class TestResult {
	 private  String compilationError;
	 private  List<TestCaseResult> testCaseResults;
	 private int newScore;
	 
	 
	 public int getNewScore() {
		return newScore;
	}

	public void setNewScore(int newScore) {
		this.newScore = newScore;
	}

	public String getCompilationError() {
        return compilationError;
    }

    public List<TestCaseResult> getTestCaseResults() {
        return testCaseResults;
    }
    
    public void setCompilationError(String error) {
         compilationError = error;
    }

    public void setTestCaseResults(TestCaseResult result) {
    	if(testCaseResults == null)	{
    		 testCaseResults = new ArrayList<>();
    	}
    		
        testCaseResults.add(result);
    }
}
