<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<div class="assignment-question-details">
                <div class="heading">
                        <h2>${specificAssignment.currAssignmentName} </h2>
                        <p class="submited-posted">Posted on : ${specificAssignment.postedDate}</p>
                    </div>
                    <div class="instructions">
                        <h3 >General Instructions</h3>
                        <ul style="margin-top: 10px;">
                            <li>The assignment can be submitted only once</li>
                            <li>Upload your assignment in zip file format</li>
                        </ul>
                    </div>

                <div class="question">
                    <h4>${specificAssignment.currAssignmentName}</h4>
                  <c:forEach var="assignmentQuestionFile" items = "${assignmentQuestionFile}">
                    <p>${assignmentQuestionFile}</p>
                  </c:forEach>
                
 		<form method="post" action="submitassignment.action" enctype="multipart/form-data">
    <c:choose>
        <c:when test="${!specificAssignment.submittedDate.equals('empty')}">
            <!-- Display the submitted date -->
             <br>
            <span class="submited-posted">Submitted on : ${specificAssignment.submittedDate}</span>
            <br>
           
        </c:when>
        <c:otherwise>
            <!-- Label for the file input -->
            <label for="file">Submit Here</label>
            <br><br>
            <!-- Input for file upload -->
            <input type="file" name="file" id="file" class="input-file" accept="*" required>
            <br>
            <!-- Button to submit the form -->
            <button type="submit" id="submit-btn">Submit Assignment</button>
        </c:otherwise>
    </c:choose>
</form>

    </div>
</div>