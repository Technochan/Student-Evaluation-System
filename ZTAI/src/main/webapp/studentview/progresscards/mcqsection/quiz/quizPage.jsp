<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setDateHeader("Expires", 0); // Proxies.
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz</title>
    <link rel="stylesheet" href="studentview/progresscards/mcqsection/quiz/quizPage.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<style>
#confirmation-dialog{
    position: absolute;
}

#confirmation-dialog .popup-container{
    width: 900px;
    height: 400px;
    background-color: black;
    padding:0px 50px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border:10px solid grey;
    border-radius: 10px;
}

#confirmation-dialog .popup-container a{
    padding: 10px 30px;
    color: #ffffff;
    font-size: 17px;
    border: none;
    cursor: pointer;
    background-color: rgb(74, 151, 30);
}

</style>
<body>
    <div class="header"></div>
    <div class="quiz-page-container">
        <div class="quiz-top-bar">
            <div class="quiz-question-section">
                <i class="bi bi-arrow-left" style="font-size: 30px;"></i>
                <h1>Ternary Operator</h1>
            </div>
        </div>
        <div class="quiz-question-container">
            <div class="time-points">
                <div class="time">
                    <h4 id="timer">20 seconds left</h4>
                </div>
                <div class="mark-status">
                    <div class="total-mcq-questions">
                        <p>Total questions: 20</p>
                    </div>
                    <div class="correct">
                        <p>Correct: 0</p>
                    </div>
                    <div class="wrong">
                        <p>Wrong: 0</p>
                    </div>
                </div>
            </div>
            <div class="timer-progress-bar">
                <div class="timer-progress" id="timer-progress" style="width: 100%;"></div>
            </div>
            <div class="quiz-question-info">
                <div class="quiz-question-name">
                    <h2></h2>
                </div>
                <div class="quiz-question-options">
                    <!-- Options will be dynamically added here -->
                </div>
            </div>
            <div class="quiz-question-container-footer">
                <div class="submit-btn">
                    <button id="submit-btn" disabled>Submit</button>
                </div>
                <div class="next-btn">
                    <button id="nxt-btn" disabled>Next Question</button>
                </div>
            </div>
        </div>
        
         <div id="confirmation-dialog" style="display: none;">
        <div class="popup-container">
            <h1>Quiz will end!</h1>
            <h2>The Quiz gets Completed. Now you can leave the page</h2>
            <a id="ok-popup" href="mcq" >OK</a>
        </div>
    </div>
       
    </div>
    <script> 
    // Prevent back button
    history.pushState(null, null, location.href);
    window.onpopstate = function () {
        history.go(1);
    };

    // Set a flag to detect refresh
    if (performance.navigation.type === performance.navigation.TYPE_RELOAD) {
        // If the page is being reloaded, navigate to another page
       var correctCount = parseInt(localStorage.getItem('correctCount'));
    var wrongCount = parseInt(localStorage.getItem('wrongCount'));
    var totalQuestions = parseInt(localStorage.getItem('totalQuestions'));
  
    console.log("refres "+correctCount);
    console.log("refres "+wrongCount);
    
    var xhr = new XMLHttpRequest();

    // Configure the request
    xhr.open('POST', 'quizend', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
	
    // Define the data to be sent
    var data = {
        correctCount: correctCount,
        wrongCount: wrongCount,
        totalQuestions: totalQuestions
    };

    // Convert data to JSON format
    var jsonData = JSON.stringify(data);
    console.log(jsonData);

    // Send the request
    xhr.send(jsonData);
    console.log("after call");
    // Remove items from local storage
    localStorage.removeItem('correctCount');
    localStorage.removeItem('wrongCount');
    localStorage.removeItem('totalQuestions');

    	window.location.href = 'mcq'; 
    }

    window.addEventListener('beforeunload', function(event) {
        // Set a flag in sessionStorage before unloading the page
        sessionStorage.setItem('isReloading', 'true');
    });

    // Optionally, you can clear the flag when the page fully loads to reset the state
    window.addEventListener('load', function(event) {
        sessionStorage.removeItem('isReloading');
    });
	
   
 // Define global variables
    var currentQuestionIndex = 0; // To keep track of the current question
    var questionsData = null; // To store the loaded questions data
    var timerInterval = null; // To store the interval ID for the timer
    var correctCount = 0; // Count of correct answers
    var wrongCount = 0; // Count of wrong answers
    var quizCompleted = false; // Flag to indicate if the quiz is completed

    // Function to handle clicking the "OK" button in the popup

    // Function to load questions from JSON
    function loadQuestions() {
        var sectionId = '${sectionId}';
        var questionId = '${questionId}';
        var startQuestionNumber = '${startQuestionNumber}';

        var jsonFilePath = "/ZTAI/quizjson/section" + sectionId + "question" + questionId + ".json";

        fetch(jsonFilePath)
            .then(response => response.json())
            .then(data => {
                questionsData = data;
                currentQuestionIndex = startQuestionNumber; // Adjust for zero-based indexing
                loadCurrentQuestion();
                updateStatus(); // Initial update of status
                startTimer(); // Start the timer when questions are loaded
            })
            .catch(error => console.error('Error loading questions:', error));
    }

    // Function to load the current question into HTML
    function loadCurrentQuestion() {
        var questionData = questionsData.questions[currentQuestionIndex];
        var questionName = document.querySelector('.quiz-question-name h2');
        questionName.textContent = (currentQuestionIndex + 1) + '. ' + questionData.question;

        var optionsContainer = document.querySelector('.quiz-question-options');
        optionsContainer.innerHTML = ''; // Clear previous options

        questionData.options.forEach((option, index) => {
            var optionDiv = document.createElement('div');
            optionDiv.className = 'option'; // Common class for all options
            optionDiv.textContent = String.fromCharCode(65 + index) + '. ' + option;
            optionDiv.onclick = function() {
                selectOption(optionDiv);
            };
            optionsContainer.appendChild(optionDiv);
        });

        // Reset button states
        document.getElementById('submit-btn').disabled = true;
        document.getElementById('submit-btn').style.backgroundColor = '';
        document.getElementById('nxt-btn').disabled = true;
        document.getElementById('nxt-btn').style.backgroundColor = '';

        // Start the timer
        startTimer();

    }

    // Function to handle option selection
    function selectOption(option) {
        var options = document.querySelectorAll('.quiz-question-options .option');
        options.forEach(opt => opt.classList.remove('selected'));
        option.classList.add('selected');
        document.getElementById('submit-btn').disabled = false;
    }

    // Function to start the timer
    function startTimer() {
        var timeLeft = 20;
        var timerElement = document.getElementById('timer');
        var timerProgress = document.getElementById('timer-progress');

        timerElement.textContent = timeLeft + ' seconds left';
        timerProgress.style.width = '100%';

        clearInterval(timerInterval);
        timerInterval = setInterval(function() {
            timeLeft--;
            timerElement.textContent = timeLeft + ' seconds left';
            timerProgress.style.width = (timeLeft / 20) * 100 + '%';

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                showCorrectAnswer();
                updateStatus();
                document.getElementById('nxt-btn').disabled = false;
            }
        }, 1000);
    }

    // Function to show the correct answer
    function showCorrectAnswer() {
        var questionData = questionsData.questions[currentQuestionIndex];
        var correctAnswer = questionData.answer;
        var options = document.querySelectorAll('.quiz-question-options .option');

        options.forEach(option => {
            var optionText = option.textContent.split('. ')[1];
            if (optionText === correctAnswer) {
                option.classList.add('correct');
            } else {
                option.classList.add('wrong');
            }
            option.onclick = null; // Disable option click
        });

        document.getElementById('submit-btn').disabled = true;
        document.getElementById('submit-btn').style.backgroundColor = '';
    }

    // Function to handle submit button click
    document.getElementById('submit-btn').onclick = function() {
        clearInterval(timerInterval);
        var selectedOption = document.querySelector('.quiz-question-options .selected');
        var questionData = questionsData.questions[currentQuestionIndex];
        var correctAnswer = questionData.answer;

        if (selectedOption) {
            var selectedAnswer = selectedOption.textContent.split('. ')[1];
            if (selectedAnswer === correctAnswer) {
                selectedOption.classList.add('correct');
                correctCount++;
            } else {
                selectedOption.classList.add('wrong');
                wrongCount++;
                var options = document.querySelectorAll('.quiz-question-options .option');
                options.forEach(option => {
                    if (option.textContent.split('. ')[1] === correctAnswer) {
                        option.classList.add('correct');
                    }
                });
            }
        } else {
            // If no option is selected and submit is pressed, count as wrong
            wrongCount++;
            var options = document.querySelectorAll('.quiz-question-options .option');
            options.forEach(option => {
                if (option.textContent.split('. ')[1] === correctAnswer) {
                    option.classList.add('correct');
                }
            });
        }

        updateStatus();

        // Disable option clicks after submission
        var options = document.querySelectorAll('.quiz-question-options .option');
        options.forEach(option => {
            option.onclick = null;
        });

        document.getElementById('nxt-btn').disabled = false;
        document.getElementById('nxt-btn').style.backgroundColor = 'blue';
    };

    // Function to load next question
    document.getElementById('nxt-btn').onclick = function() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionsData.questions.length) {
            loadCurrentQuestion();
        } else {
            showQuizEnd();
        }
    };
    
    // Function to show the quiz end popup
    function showQuizEnd() {
    
     clearInterval(timerInterval);
     var correctCount = document.querySelector('.correct p').textContent.split(': ')[1];
     var wrongCount = document.querySelector('.wrong p').textContent.split(': ')[1];
     var totalQuestions = document.querySelector('.total-mcq-questions p').textContent.split(': ')[1];
     // Create a new XMLHttpRequest object
     var xhr = new XMLHttpRequest();

     // Configure the request
     xhr.open('POST', 'quizend', true);
     xhr.setRequestHeader('Content-Type', 'application/json');
	
     // Define the data to be sent
     var data = {
         correctCount: correctCount,
         wrongCount: wrongCount,
         totalQuestions: totalQuestions
     };

     // Convert data to JSON format
     var jsonData = JSON.stringify(data);
     console.log(jsonData);

     // Send the request
     xhr.send(jsonData);
     console.log("after call");
     document.getElementById('confirmation-dialog').style.display = "block";
    
 }
    
 // Function to show the quiz end popup
   


    // Function to update the status
    function updateStatus() {
        document.querySelector('.total-mcq-questions p').textContent = 'Total questions: ' + questionsData.questions.length;
        document.querySelector('.correct p').textContent = 'Correct: ' + correctCount;
        document.querySelector('.wrong p').textContent = 'Wrong: ' + wrongCount;
        
        localStorage.setItem('correctCount', correctCount);
        localStorage.setItem('wrongCount', wrongCount);
        localStorage.setItem('totalQuestions',questionsData.questions.length);
    }

    // Load questions when the page loads
    window.onload = function() {
        loadQuestions();
    };
    
    
 
       

		
    </script>
    
</body>
</html>
