package com.ztai.model;


public class TestCaseResult {
	
    private int index;
    private boolean passed;
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private long duration;

    // Constructor
    public TestCaseResult(int index, boolean passed, String input, String expectedOutput, String actualOutput,long duration) {
        this.index = index;
        this.passed = passed;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.duration = duration;
    
    }

    // Getters and setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getActualOutput() {
        return actualOutput;
    }

    public void setActualOutput(String actualOutput) {
        this.actualOutput = actualOutput;
    }

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}