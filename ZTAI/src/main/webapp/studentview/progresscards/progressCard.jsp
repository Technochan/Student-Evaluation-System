<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>All Cards</title>
<link rel="stylesheet" href="studentview/progresscards/progressCard.css">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

</head>
<body>
	
    <div class="header"></div>


    <div class="assignment-page-assignment-cards">


        <!-- card 1 -->
     <c:forEach var="progressCardInfo" items="${progressCardsInfo}">
        <div class="assignment-page-assignment-card-info">
            <h1>${progressCardInfo.progressCardName}</h1>
            <br>
            <hr style="border: 1px solid rgb(99, 99, 99);">
            <div class="assignment-card-module-info">
                <div class="module-info">
                    <h2>${progressCardInfo.progressCardTotalSections }</h2>
                    <p>Total Sections</p>
                </div>
                <div class="questions-info">
                    <h2>${progressCardInfo.progressCardTotalSubSections}</h2>
                    <p>Total Sub-Sections</p>
                </div>
                <div class="markscored-info">
                    <h2>${progressCardInfo.progressCardStudentTotalMarks } / ${progressCardInfo.progressCardTotalMarks}</h2>
                    <p>Marks Scored</p>
                </div>
            </div>
            <hr style="border: 1px solid rgb(99, 99, 99);">
            <div class="card-action">
                <div class="open-button">
                    <a href="${progressCardInfo.progressCardType}"><i class="bi bi-play-circle-fill" style="font-size: 25px;"></i><h2>Open</h2></a>
                </div>
                <hr style="height: 100px; border: 1px solid rgb(99, 99, 99);">
                <div class="progress-report-round">
                    <div class="percent">
                      <svg>
                        <circle cx="55" cy="55" r="50"></circle>
                        <circle cx="55" cy="55" r="50" style="--percent: ${progressCardInfo.percentage}"></circle>
                      </svg>
                      <div class="number">
                        <h3>${progressCardInfo.percentage}<span>%</span></h3>
                      </div>
                    </div>
                  </div>

            </div>
        </div>
</c:forEach>

    </div>


</body>
</html>