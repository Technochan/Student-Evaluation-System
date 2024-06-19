package com.ztai.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.core.util.internal.HttpInputStreamUtil.Result;

import com.google.gson.Gson;
import com.ztai.model.TestCaseResult;
import com.ztai.model.TestResult;


public class CompileAndRun {
	
    	public TestResult start(int count) {
      	
        String sourceFile = "/home/zs-gsch05/Desktop/Eclipse-New-Workspace/ZTAI/src/main/java/codesubmission/Main.java"; // Change this to your source file
        String outputDir = "/home/zs-gsch05/Desktop/Eclipse-New-Workspace/ZTAI/src/main/java"; // Adjust to your classpath
        String className = "codesubmission.Main"; // Fully qualified class name
        String jsonFilePath = "/home/zs-gsch05/Desktop/Eclipse-New-Workspace/ZTAI/src/main/webapp/codingjson/section1question1.json"; // Change this to your JSON file path

        List<TestCase> testCases = loadTestCasesFromJson(jsonFilePath);
        System.out.println("size    vanakam    "+ testCases.size());

    	int currscore = 0;
    	TestResult tr = new TestResult();
        // Step 1: Compile the Java program
        String compileCommand = "javac -d " + outputDir + " " + sourceFile;
        String compileOutput = executeCommand(compileCommand);
        if (!compileOutput.isEmpty()) {
            System.out.println("Compilation Errors:");
            compileOutput = cleanupFilePath(compileOutput, sourceFile); 
			
			tr.setCompilationError(compileOutput);
			
        } else {
            // Step 2: Run the Java program for each test case if compilation succeeds
        	int loop = count == 0 ? testCases.size() : count;
        
        	for (int i = 0; i < loop; i++) {
                TestCase testCase = testCases.get(i);
                long startTime = System.nanoTime(); // Start time measurement
                String runCommand = "java -cp " + outputDir + " " + className;
                String programOutput = executeProgramWithInput(runCommand, testCase.getInput(), 5); // 5 seconds timeout
                long endTime = System.nanoTime(); // End time measurement
                long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime); // Calculate duration in milliseconds

                // Compare the output
                boolean passed = testCase.getOutput().trim().equalsIgnoreCase(programOutput.trim());
                // stop balance testcase here
                if(!passed) {
                	while(i < loop) {
                		TestCaseResult result = new TestCaseResult(i + 1, passed, testCase.getInput(), testCase.getOutput(), programOutput, duration);
                    	tr.setTestCaseResults(result);
                    	i++;
                	}
                	continue;
                	
                }
             
                TestCaseResult result = new TestCaseResult(i + 1, passed, testCase.getInput(), testCase.getOutput(), programOutput, duration);
                tr.setTestCaseResults(result);

                if (programOutput.equals("timeout")) break;
                currscore++;
            }
        }
        if(currscore > 0 && count != 2) {
        	
        	tr.setNewScore(currscore);
        }
        
        
        System.out.println("compilationError   "+tr.getCompilationError() );
        
		/*
		 * for(TestCaseResult e : testCaseResults) {
		 * System.out.println("test case no   " + e.getIndex());
		 * System.out.println("input  " +e.getInput());
		 * System.out.println("expected output   " +e.getExpectedOutput());
		 * System.out.println("actual output   " +e.getActualOutput());
		 * System.out.println("status:  "+e.isPassed());
		 * System.out.println("time taken   " +e.getDuration()); System.out.println(); }
		 */
       return tr;
    }

    private static List<TestCase> loadTestCasesFromJson(String jsonFilePath) {
        List<TestCase> testCases = new ArrayList<>();
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(jsonFilePath);
            TestCasesWrapper wrapper = gson.fromJson(reader, TestCasesWrapper.class);
            testCases = wrapper.getTestCases();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(TestCase t : testCases) {
        	System.out.println(t.getInput()+"   "+t.getOutput());
        }
        return testCases;
    }


    private static String executeProgramWithInput(String command, String input, int timeoutSeconds) {
        StringBuilder output = new StringBuilder();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            StringBuilder result = new StringBuilder();
            Process process = null;
            try {
                ProcessBuilder builder = new ProcessBuilder();
                builder.command("sh", "-c", command);
                process = builder.start();

                // Provide input to the program
                try (OutputStream os = process.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
                    writer.write(input);
                    writer.flush(); // Ensure the input is sent to the process
                }

                // Capture the output of the program
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                }

                // Capture any errors from the program
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                }

                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        });

        try {
            output.append(future.get(timeoutSeconds, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            future.cancel(true);
            return "timeout";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow(); // Properly shut down the executor service
        }

        return output.toString();
    }

    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", command);
            Process process = builder.start();

            // Capture the output and errors of the command
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                while ((line = errorReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    private static String cleanupFilePath(String output, String sourceFile) {
        return output.replace(sourceFile, "Main.java");
    }

    
}

class TestCasesWrapper {
    private List<TestCase> testCases;

    public List<TestCase> getTestCases() {
        return testCases;
    }
}

class TestCase {
    private String input;
    private String output;

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}


