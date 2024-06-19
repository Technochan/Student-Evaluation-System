<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Assignment Submission</title>
  	<link rel="stylesheet" href="studentview/progresscards/assignmentsection/assignmentSubmission.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
</head>
<body>
 <div class="header"></div>
    <div class="assignment-page-container">
        <div class="assignment-name-section">
            <div class="back-button">
                <a href="#"><i class="bi bi-arrow-left-circle"></i>&nbsp; Back</a>
            </div>
            <h1>Java Assignment</h1>
            <hr>
            <br>
        <c:forEach var="assignmentSections" items="${assignmentSections}">
            <div class="assignment-heading-section">
                <div class="assignment-heading">
                    <h3>${assignmentSections.sectionId}.${assignmentSections.sectionName}</h3>
                    <span class="arrow">&#9660;</span>
                </div>
                <div class="assignment-section-dropdown">
                <c:forEach var="assignmentQuestions" items = "${assignmentQuestions}">
                <c:if test="${assignmentSections.sectionId == assignmentQuestions.sectionId}">
               <button id="ass-button" type="button" onclick="fetchAssignment('loadassignment?sectionId=${assignmentSections.sectionId}&assignmentId=${assignmentQuestions.assignmentId}')">
                	<c:choose>
                		<c:when test = "${assignmentQuestions.solved}">
                			<i class="bi bi-check-circle" style="color: green;"></i>
                		</c:when>
                		<c:otherwise>
                			<i class="bi bi-check-circle" style="color: red;"></i>
                		</c:otherwise>
                	</c:choose>
                    &nbsp; ${assignmentQuestions.assignmentName}
               </button>
              </c:if>
             </c:forEach>
               </div>
            </div>
            </c:forEach>
        </div>
        <div class="assignment-info-section">

            <div class="assigment-info-cards">
                <div class="total-sections">
                    <h2>Total Sections</h2>
                    <h1>${totalAssignmentSections}</h1>
                </div>
                <div class="total-assignment-count">
                    <h2>Total Assignments</h2>
                    <h1>${totalAssignmentQuestions}</h1>
                </div>
                <div class="completed-status-count">
                    <h2>Completed Status</h2>
                    <h1>${assignmentCompletedCount}</h1>
                    
                </div>
            </div>

          
          <div id="assignmentFetched">
       		 
    		</div>
    	
    	    <c:if test="${sessionScope.assignemntPostedStatus != null}">
            <div id="submitted-success">
                <c:choose>
                    <c:when test="${sessionScope.assignemntPostedStatus == 'Success'}">
                        <i class="bi bi-check-circle" style="color: green; font-size: 130px;"></i>
                        <h1>Assignment Posted Successfully</h1>
                    </c:when>
                    <c:otherwise>
                        <i class="bi bi-check-circle" style="color: red; font-size: 130px;"></i>
                        <h1>Assignment Not Posted</h1>
                    </c:otherwise>
                </c:choose>
                <button onclick="closeSuccess()">Ok</button>
            </div>
            <% session.removeAttribute("assignemntPostedStatus"); %>
        </c:if>


        
    	
        </div>
    </div>


<script>



function fadeOut(element) {
    var opacity = 1;
    var timer = setInterval(function() {
        if (opacity <= 0.1) {
            clearInterval(timer);
            element.style.display = 'none';
        }
        element.style.opacity = opacity;
        element.style.filter = 'alpha(opacity=' + opacity * 100 + ")";
        opacity -= opacity * 0.1;
    }, 50); 
}

function closeSuccess() {
    var element = document.getElementById('submitted-success');
    fadeOut(element);
}

function fetchAssignment(action) {
	  var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	console.log("Success");
	    	console.log(this.responseText);
	     document.getElementById("assignmentFetched").innerHTML = this.responseText;
	    }
	  };
	  xhttp.open("GET", action, true);
	  xhttp.send();
	}


    document.querySelectorAll('.assignment-heading-section').forEach(section => {
        section.querySelector('.assignment-heading').addEventListener('click', function() {
            toggleDropdown(section);
        });
    });

    function toggleDropdown(section) {
        var dropdown = section.querySelector('.assignment-section-dropdown');
        var arrow = section.querySelector('.arrow');
        section.classList.toggle('active');
        
        if (section.classList.contains('active')) {
            dropdown.style.maxHeight = dropdown.scrollHeight + "px";
    
        } else {
            dropdown.style.maxHeight = "0";

        }
        
        arrow.classList.toggle('active');
    }
</script>

</body>
</html>