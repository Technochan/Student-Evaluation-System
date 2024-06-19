package com.ztai.controller;

import com.opensymphony.xwork2.ActionSupport;
import com.ztai.dao.Database;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReportIssueAction extends ActionSupport {
	private String issueId;
    private String title;
    private String description;
    private File file;
    private String fileContentType;
    private String fileFileName;
    private String email;
    private String ZTAIID;

    private Database database = Database.getInstance();

    // Getters and setters for all fields
    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getZTAIID() {
        return ZTAIID;
    }

    public void setZTAIID(String ZTAIID) {
        this.ZTAIID = ZTAIID;
    }

    @Override
    public String execute() {
        // Save the file if it is uploaded
    	System.out.println("Execute!..."+ getZTAIID());
        String filePath = null;
        if (file != null) {
            try {
                // Set the directory where you want to save the file
            	String desktopPath = "C:/Users/Srigowri N/eclipse-workspace/ZTAI/src/main/webapp/images/UploadedFiles/";
                File destDir = new File(desktopPath);
                if (!destDir.exists()) {
                    destDir.mkdirs(); // Create the directory if it does not exist
                }
                filePath = desktopPath + fileFileName;  
                File destFile = new File(filePath);
                FileUtils.copyFile(file, destFile);
            } catch (IOException e) {
                e.printStackTrace();
                return ERROR;
            }
        }

        // Insert data into the database
        try {
        	if(filePath!=null ) {
                database.addIssue(ZTAIID, title, description, filePath, fileFileName);
        	}
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    // Method to get issues for a specific candidate
    public List<Map<String, Object>> getIssue(String ZTAIID) {
        try {
            return database.getIssues(ZTAIID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get all issues
    public List<Map<String, Object>> getIssues() {
        try {
            return database.getIssues();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //Method to delete issues
    public String delete() {
        try {
            database.deleteIssue(issueId);
            System.out.println(email+ " ---- "+ ZTAIID);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Failed to delete the issue");
            return ERROR;
        }
    }
}
