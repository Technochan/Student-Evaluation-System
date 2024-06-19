<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>


<link rel="stylesheet"
	href="studentview/progresscards/codingsection/CodingHomePage.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<style>
</style>
<body>

	<!-- header start -->
	<div class="header"></div>
	<!-- header end -->

	<div class="assignment-view-container">
		<!-- Title about assignment -->
		<div class="assignment-view-heading">
			<a href="javascript:history.back()"><i
				class="bi bi-arrow-left back-arrow"></i></a>
			<h1>Heading about the assignment</h1>
		</div>

		<!-- progress card info -->
		<div class="progress-cards">
			<div class="total-questions">
				<p>Total Questions</p>
				<h1>${totalQuestions}</h1>
			</div>
			<div class="solved">
				<p>Solved</p>
				<h1>${totalSolvedCoding}</h1>
			</div>
			<div class="attempted">
				<p>attempted</p>
				<h1>${totalAttemptsCoding}</h1>
			</div>
			<div class="total-marks">
				<p>Marks</p>
				<h1>${totalMarksCoding}/${totalMarks}</h1>
			</div>
		</div>
		<br>
		<!-- progress line  -->
		<div class="progress-line">
			<h3>Assignment Progress</h3>
			<div class="line-back">
				<c:set var="totalStudentMarks" value="${totalMarksCoding}" />
				<c:set var="totalMarks" value="${totalMarks}" />

				<c:set var="percentage"
					value="${totalMarks != 0 ? (totalStudentMarks * 100.0 / totalMarks) : 0}" />
				<fmt:formatNumber var="roundedPercentage" value="${percentage}"
					pattern="##" />

				<div class="blue-line" style="width: ${roundedPercentage}%;"></div>
				<span>${roundedPercentage} %</span>
			</div>
		</div>


		<br>

		<!-- assignment-dropdown-card -->
		<div class="assignment-dropdown-container">

			<!-- card one -->
			<c:forEach var="codingSection" items="${codingSections}">
				<div class="assignmentDropdownCard" onclick="toggleDropdown(this)">
					<div class="card-info">
						<div class="card-left-info">
							<div class="card-number">${codingSection.sectionId}</div>
							<h3 class="card-title">${codingSection.sectionName}</h3>
							<div class="status-icon">Icon</div>
						</div>
						<div class="card-right-info">
							<div class="data-of-completion">
								Completed within <span id="date">29/07/2023</span>
							</div>
							<div class="array-icon">&#x25BC;</div>
						</div>
					</div>

					<!-- dropdown section start -->
					<div class="dropdown-sections">
						<div class="dropdown-section-info">
							<div class="attempted-info">
								<p>
									Attempted : <b>${codingSection.totalAttempts} /
										${codingSection.questionCount}</b>
								</p>
							</div>
							<div class="solved-info">
								<p>
									Solved : <b> ${codingSection.solvedCount} /
										${codingSection.questionCount}</b>
								</p>
							</div>
							<div class="marks-scored-info">
								<p>
									Marks Scored : <b> ${codingSection.totalScoredMark} /
										${codingSection.totalScore}</b>
								</p>
							</div>
						</div>
						<!-- dropdown question part start -->
						<div class="dropdown-question-section">
							<div class="dropdown-question-menu">
								<h3 style="margin-right: 230px;">Question</h3>
								<h3>Status</h3>
								<h3 style="margin-left: 20px;">Score</h3>
								<h3>Attempts</h3>
								<h3>Type</h3>
								<h3>Difficulty</h3>
								<h3>Action</h3>
							</div>

							<c:forEach var="codingSectionQuestion"
								items="${codingSectionQuestions}">
								<c:if
									test="${codingSection.sectionId == codingSectionQuestion.sectionId}">
									<div class="dropdown-question-info">
										<div class="question-title">
											<h3>${codingSectionQuestion.questionName}</h3>
										</div>
										<div class="question-status-info">
											<div class="status" style="width: 80px;">
												<c:choose>
													<c:when test="${codingSectionQuestion.isSolved}">
														<div class="icon">Ic</div>
														<h3>Solved</h3>
													</c:when>
													<c:otherwise>
														<div class="icon">Ic</div>
														<h3 style="color: rgb(179, 34, 34)">Not Solved</h3>
													</c:otherwise>
												</c:choose>
											</div>
											<div class="score" style="margin-left: 120px; width: 80px">
												<h3>${codingSectionQuestion.totalScoredMark}/
													${codingSectionQuestion.totalScore}</h3>
											</div>
											<div class="attempts" style="margin-left: 150px;">
												<h3>${codingSectionQuestion.totalAttempts}</h3>
											</div>
											<div class="type" style="margin-left: 150px;">
												<p>Code</p>
											</div>
											<div class="difficulty" style="margin-left: 100px;">
												<p>${codingSectionQuestion.difficulty}</p>
											</div>
											<c:choose>
												<c:when test="${codingSectionQuestion.isSolved}">
													<a class="action"
														style="margin-left: 100px; margin-right: 10px; background-color: rgb(47, 117, 33)"
														href="codeeditor?sectionId=${codingSection.sectionId}&questionId=${codingSectionQuestion.questionId}">Solved</a>
												</c:when>
												<c:otherwise>
													<a class="action"
														style="margin-left: 100px; margin-right: 10px;"
														href="codeeditor?sectionId=${codingSection.sectionId}&questionId=${codingSectionQuestion.questionId}">Solve</a>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>

	<script>
		window
				.addEventListener(
						'pageshow',
						function(event) {
							if (event.persisted
									|| (window.performance && window.performance.navigation.type == 2)) {
								window.location.reload();
							}
						});

		function toggleDropdown(assignmentDropdownCard) {
			const dropdownSections = assignmentDropdownCard
					.querySelector('.dropdown-sections');
			assignmentDropdownCard.classList.toggle('open');

			if (assignmentDropdownCard.classList.contains('open')) {
				dropdownSections.style.maxHeight = dropdownSections.scrollHeight
						+ 'px';
			} else {
				dropdownSections.style.maxHeight = 0;
			}
		}
	</script>

</body>


</html>
