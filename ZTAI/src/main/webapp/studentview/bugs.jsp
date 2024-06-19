<%@page import="com.ztai.controller.ReportIssueAction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.ztai.model.*" %>


<%
    String email = request.getParameter("email");
    String ZTAIID = request.getParameter("ztaiid");

    ReportIssueAction reportIssueAction = new ReportIssueAction();
    List<Map<String, Object>> issues;

    if (email != null && email.endsWith("gmail.com")) {
        issues = reportIssueAction.getIssue(ZTAIID);
    } else {
        issues = reportIssueAction.getIssues();
    }
    
    System.out.println(email);
    System.out.println(ZTAIID);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bugs & Feedback</title>
    <style>
        body {
            background-color: black;
            color: white;
            font-family: Arial, sans-serif;
        }
        .issue-table {
            width: 100%;
            border-collapse: collapse;
        }
        .issue-row {
            background-color: #1b1919;
            margin: 10px 0;
            padding: 10px;
            border-radius: 5px;
            display: flex;
            align-items: center;
        }
        .issue-image {
            width: 200px;
            height: 110px;
            margin-right: 30px;
            cursor: pointer;
        }
        .issue-content {
            flex-grow: 1;
        }
        .issue-title {
            font-size: 1.2em;
            margin: 0;
        }
        .issue-description {
            margin: 10px 0;
        }
        .delete-button {
            background-color: red;
            color: white;
            border: none;
            padding: 10px;
            cursor: pointer;
            border-radius: 5px;
        }
        .description-popup, .image-popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #fff;
            color: #000;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            z-index: 1000;
        }
        .popup-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
    </style>
</head>
<body>
    <h2>Bugs & Feedback</h2>
    <div>
        <div class="issue-list">
            <% if (issues != null) {
                for (Map<String, Object> issue : issues) { 
            %>
                    <div class="issue-row">
                        <img src="${pageContext.request.contextPath}/images/UploadedFiles/<%= issue.get("FileName") %>" class="issue-image" onclick="showImage('${pageContext.request.contextPath}/images/UploadedFiles/<%= issue.get("FileName") %>')" alt="Issue Image"/>
                        <div class="issue-content">
                            <h3 class="issue-title"><%= issue.get("Title") %></h3>
                            <p class="issue-description"><%= issue.get("Description") %></p>
                            <form action="deleteIssue" method="post">
                                <input type="hidden" name="issueId" value="<%= issue.get("IssueID") %>"/>
                                <input type="hidden" name="email" value="<%= email %>"/>
                                <input type="hidden" name="ZTAIID" value="<%= ZTAIID %>"/>
                                <button type="submit" class="delete-button">Delete</button>
                            </form>
                        </div>
                    </div>
            <% 
                }
            } else { %>
                <div>No issues reported.</div>
            <% } %>
        </div>
    </div>

    <div class="popup-overlay" id="popupOverlay" onclick="closePopup()"></div>
    <div class="image-popup" id="imagePopup">
        <img id="popupImage" src="" alt="Popup Image" style="width: 100%; height: auto;"/>
    </div>

    <button onclick="goBack()" class="back-button">Back</button>
    
    <script>
        function showImage(filePath) {
            document.getElementById("popupImage").src = filePath;
            document.getElementById("popupOverlay").style.display = 'block';
            document.getElementById("imagePopup").style.display = 'block';
        }

        function closePopup() {
            document.getElementById("popupOverlay").style.display = 'none';
            document.getElementById("imagePopup").style.display = 'none';
        }

        function goBack() {
            var email = '<%= email %>';            
            if (email && email.endsWith("gmail.com")) {
                window.location.href = "<%= request.getContextPath() %>studentview/candDashboard.jsp";
            } else {
                window.location.href = "<%= request.getContextPath() %>/adminDashboard.jsp";
            }
        }
    </script>
</body>
</html>
