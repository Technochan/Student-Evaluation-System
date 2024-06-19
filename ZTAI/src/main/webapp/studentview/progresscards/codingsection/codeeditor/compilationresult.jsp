   <!-- 4 types of result -->

<%@page import="com.ztai.model.TestCaseResult"%>
<%@page import="com.ztai.model.TestResult"%>
<%@page import="java.util.List"%>
  <div class="testcase-result-container">
<%
            // Retrieve TestResult object from session
            TestResult testResult = (TestResult) request.getAttribute("testResult");
            // Log the retrieved object
            System.out.println("TestResult object retrieved from session: " + testResult);

            // Check if TestResult object exists
            if (testResult != null) {
                String compilationError = testResult.getCompilationError();
                if (compilationError != null && !compilationError.isEmpty()) {
        %>
                    <div class="compilation-error">
                        <div class="status-message">
                            <h1 style="color: rgb(168, 166, 31);">Compilation Error</h1>
                        </div>
                        <div class="error-report">
                            <p><%= compilationError %></p>
                        </div>
                    </div>
        <%
                } else {
                    // Display test case results if they exist
                    List<TestCaseResult> testCaseResults = testResult.getTestCaseResults();
                    System.out.println(testCaseResults.size() +"     test case result size");
                    if (testCaseResults != null && !testCaseResults.isEmpty()) {
        %>
                        <div class="testcases">
        <%
                            for (TestCaseResult testCaseResult : testCaseResults) {
        %>
                                <div class="testcase">
                                    <h3>Test Case <%= testCaseResult.getIndex() %>:
        <%
                                        if (testCaseResult.isPassed()) {
        %>
                                            <span style="color: rgb(108, 172, 23);">Passed</span>
        <%
                                        } else {
        %>
                                            <span style="color: rgb(172, 23, 23);">Wrong Answer</span>
                                            <div>
                                                <p>Input: <%= testCaseResult.getInput() %></p>
                                                <p>Expected Output: <%= testCaseResult.getExpectedOutput() %></p>
                                                <p>Actual Output: <%= testCaseResult.getActualOutput() %></p>
                                            </div>
        <%
                                        }
        %>
                                    </h3>
                                </div>
        <%
                            }
        %>
                        </div>
        <%
                    } else {
                        // Display message if compiled successfully
        %>
                        <div class="compiled-success">
                            <div class="status-message">
                                <h1 style="color: rgb(63, 141, 27);">Compiled Successfully</h1>
                            </div>
                            <div class="status-report">
                                <h3>Input</h3>
                                <div class="given-input">
                                    <h3>Some input given</h3>
                                </div>
                                <h3>Output</h3>
                                <div class="your-output">
                                    <h3>Some output comes</h3>
                                </div>
                            </div>
                        </div>
        <%
                    }
                }
            }
            // Remove testResult from session
            session.removeAttribute("testResult");
        %>
      </div>  