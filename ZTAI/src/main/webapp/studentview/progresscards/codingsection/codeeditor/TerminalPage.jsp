
<%@page import="com.ztai.model.CodingSection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link type="text/css" rel="stylesheet" href="studentview/progresscards/codingsection/codeeditor/TerminalPage.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- code editor lib -->
    <link rel="stylesheet" href="studentview/progresscards/codingsection/codeeditor/lib/codemirror.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="studentview/progresscards/codingsection/codeeditor/lib/codemirror.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.58.3/theme/eclipse.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="studentview/progresscards/codingsection/codeeditor/themes/dracula.css">
    <link rel="stylesheet" href="studentview/progresscards/codingsection/codeeditor/themes/material-ocean.css">
    <link rel="stylesheet" href="studentview/progresscards/codingsection/codeeditor/themes/material-palenight.css">
    <script src="studentview/progresscards/codingsection/codeeditor/mode/clike/clike.js"></script>
    <script src="studentview/progresscards/codingsection/codeeditor/addon/edit/closebrackets.js"></script>
    
       <style>
        /* Define CSS classes for enabled and disabled buttons */
        .enabled {
            background-color: rgb(21,104,220) !important; /* Change to your desired enabled color */
            color: white !important;
        }
        .disabled {
            background-color: rgb(44,44,44) !important; /* Change to your desired disabled color */
            color: white !important;
            pointer-events: none !important; /* Disable mouse events */
        }
    </style>
</head>
<body>
<div class="header"></div>

<div class="question-top-info">
    <div class="question-info-left">
        <a href="#" style="text-decoration: none; color: #ffffff;">
            <i class="bi bi-arrow-left" style="font-size: 30px;"></i>
        </a>
        <h1 class="question-name">Even or Odd</h1>
    </div>
    <div class="question-info-right">
<%

    HashMap<String, CodingSection> mp =  (HashMap<String, CodingSection>) request.getAttribute("questioninfo");
    HashMap<Integer, Integer> questionsPerSection = (HashMap<Integer, Integer>) request.getAttribute("questionsPerSection");

   
    String sectionId = (String) session.getAttribute("sectionId");
    String questionId = (String) session.getAttribute("questionId");
 // Print debug messages
    System.out.println("sectionId: gotttttttttt " + sectionId);
    System.out.println("questionId: gotttttttttt " + questionId);
    System.out.println("Request object: gotttttttttt " + request);
    
    int currentSectionId = Integer.parseInt(sectionId);
    int currentQuestionId = Integer.parseInt(questionId);
    
    int totalQuestionsInSection = questionsPerSection.get(currentSectionId);

    CodingSection cs = mp.get("section" + sectionId + "question" + questionId);

    if (cs != null && cs.getIsSolved()) {
        int attempts = cs.getTotalAttempts();
        int score = cs.getTotalScoredMark();
%>
        <div class="solved">Solved in <%= attempts %> attempt<%= attempts != 1 ? "s" : "" %></div>
        <div class="marks-obtained">Marks obtained: <%= score %> / 10</div>
<%
    } else if(cs != null) {
        int attempts = cs.getTotalAttempts();
        int score = cs.getTotalScoredMark();
%>
        <div class="not-solved" style=" width: 200px">Not solved - Attempts : <%= attempts %></div>
        <div class="marks-obtained">Marks obtained: <%= score %> / 10</div>
<%
    } else {
%>
        <div class="not-solved" style=" width: 200px">Not solved - Attempts : 0</div>
        <div class="marks-obtained">Marks obtained: 0 / 10</div>
<%
    }
%>
         <div class="difficulty-level">Easy</div>
        <button id="previous" onclick="updateQuestionId('prev')" class="<%= currentQuestionId == 1 ? "disabled" : "enabled" %>"><i class="bi bi-arrow-left-short" style="font-size: 30px;"></i>Prev</button>
        <button id="next" onclick="updateQuestionId('next')" class="<%= currentQuestionId == totalQuestionsInSection ? "disabled" : "enabled" %>">Next<i class="bi bi-arrow-right-short" style="font-size: 30px;"></i></button>
    </div>
</div>

<div class="question-console">
    <div class="question-statement-part">
        <h2>Question</h2>
        <hr>
        <div class="question-info">
            <p></p>
        </div>
        <br>

        <div class="question-examples">
            <h3>Example 1:</h3>
            <div class="input-output">
                <div class="input-info">
                    <h3>Input:</h3> 
                    <span class="input-value"></span>
                </div>
                <div class="output-info">
                    <h3>Output:</h3> 
                    <span class="output-value"></span>
                </div>
            </div>
        </div>
        
        <div class="question-examples">
            <h3>Example 2:</h3>
            <div class="input-output">
                <div class="input-info">
                    <h3>Input:</h3> 
                    <span class="input-value"></span>
                </div>
                <div class="output-info">
                    <h3>Output:</h3> 
                    <span class="output-value"></span>
                </div>
            </div>
        </div>
    </div>

    <div class="console-part">
        <div class="language-part">
            <div class="language">
                <h3>Language</h3>
                <span id="java">Java</span>
            </div>
            <div class="theme">
                <select name="theme" id="theme">
                    <option value="dracula">dracula</option>
                    <option value="eclipse">eclipse</option>
                    <option value="material-ocean">material-ocean</option>
                    <option value="material-palenight">material-palenight</option>
                </select>
            </div>
        </div>

        <!-- console box code -->
        <div class="console-box">
            <form id="codeForm" method="post">
                <textarea type="text" id="editor" class="form-control" aria-label="First name"></textarea>
                <div id="editorDiv" class="form-control" aria-label="First name"></div>
                <input type="hidden" name="codeContent" id="codeContent">
            </form>
        </div>

        <!-- submit buttons code -->
        <div class="submit-action">
            <button type="button" onclick="submitCode('runCode.action')">
                <div class="run-code">
                    <p>Run Code</p>
                </div>
            </button>
            <button type="button" onclick="submitCode('submitCode.action')">
                <div class="submit-code">
                    <p>Submit Code</p>
                </div>
            </button>
        </div>

      <div id="compilationResult">
  		<%@ include file="compilationresult.jsp" %>
    </div>
     
    </div>
</div>

<script>
    // Function to fetch JSON data dynamically
    function fetchQuestionData(section, question) {
        // Construct the URL to fetch JSON data
        var jsonFilePath = "/ZTAI/codingjson/section"+section+"question"+question+".json";
        console.log(jsonFilePath);

        // Fetch JSON data
        fetch(jsonFilePath)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log(response);
            return response.json();
        })
        .then(data => {
        	
        	 // updating the question name
        	 document.querySelector('.question-name').innerText = data.questionName;
        	 
            // Update question description            
            document.querySelector('.question-info p').innerText = data.questionDescription;

            // Update test case examples
            var exampleContainers = document.querySelectorAll('.question-examples');
            data.testCases.forEach((testCase, index) => {
                console.log(testCase.input+" "+testCase.output);
                var exampleContainer = exampleContainers[index];
                var inputInfo = exampleContainer.querySelector('.input-info .input-value');
                var outputInfo = exampleContainer.querySelector('.output-info .output-value');
                inputInfo.innerText = testCase.input;
                outputInfo.innerText = testCase.output;
            });
        })
        .catch(error => {
            console.error('There was a problem fetching the data:', error);
        });
    }

    // Extract sectionId and questionId from URL
    var urlParams = new URLSearchParams(window.location.search);
    var section = urlParams.get('sectionId');
    var question = urlParams.get('questionId');

    // Call the fetchQuestionData function with extracted sectionId and questionId
    fetchQuestionData(section, question);
    
    
    var editor; // Declare editor variable outside so it can be accessed globally

    // Function to initialize CodeMirror editor
    function initializeEditor(theme) {
        editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
            mode: "text/x-java",
            theme: theme, // Set the theme dynamically
            lineNumbers: true,
            autoCloseBrackets: true,
        });
    }

    // Call initializeEditor function initially with default theme
    initializeEditor("dracula");

    // Event listener for theme selection dropdown
    document.getElementById("theme").addEventListener("change", function() {
        var selectedTheme = this.value; // Get the selected theme from the dropdown
        editor.setOption("theme", selectedTheme); // Update the theme dynamically
    });
    
    function submitCode(action) {
        var codeContent = editor.getValue();
        if (codeContent.trim() === "") {
            alert("Please write something in the editor.");
            return; // Stop further execution
        }

        document.getElementById("codeContent").value = codeContent;

        $.ajax({
            url: action,
            type: 'POST',
            data: $('#codeForm').serialize(),
            success: function(response) {
                console.log(response);
                var content = $(response).find(".testcase-result-container").html();
                if (content) {
                    $("#compilationResult").html(content);
                } else {
                    console.error("Specified content not found in the response.");
                }
                var update = $(response).find(".question-info-right").html();
                if(update){
                	  $(".question-info-right").html(update);
                } else {
                    console.error("Specified Updation not found in the response.");
                }
                
            },
            error: function(xhr, status, error) {
                console.error("Error submitting code:", error);
            }
        });
    }

    // call for next and prev buttons
     // Function to update URL with new question ID
  function updateQuestionId(action) {
    // Extract sectionId and current questionId from the URL
      var sectionId = '${sectionId}';
        var questionId = '${questionId}';
    // Log the initial values
    console.log("Initial sectionId:", sectionId);
    console.log("Initial questionId:", questionId);

    // Ensure sectionId and questionId are correctly parsed
    if (!sectionId || isNaN(questionId)) {
        console.error("Invalid sectionId or questionId");
        return;
    }

    // Increment or decrement questionId based on the action
    if (action === 'next') {
        questionId++;
    } else if (action === 'prev') {
        questionId--;
    }

    // Log the updated values
    console.log("Updated sectionId:", sectionId);
    console.log("Updated questionId:", questionId);

    // Convert sectionId and questionId to strings
   
    // Log the values after conversion
    console.log("Converted sectionId:", sectionId);
    console.log("Converted questionId:", questionId);

    // Construct the new URL with updated questionId using string concatenation
    var newUrl = "codeeditor?sectionId=" + sectionId + "&questionId=" + questionId;

    // Log the new URL for debugging
    console.log("newUrl:", newUrl);

    // Redirect to the new URL
    window.location.href = newUrl;
}


</script>


</body>
</html>
