package com.ztai.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztai.model.Admin;
import com.ztai.model.Candidates;

public class Database {

	public static ArrayList<Candidates> candInfo = new ArrayList<Candidates>();
	public static ArrayList<Admin> adminInfo = new ArrayList<Admin>();

	private static Database database;

	public static ArrayList<Candidates> getCandInfo() {

		return allCand();
	}

	public static ArrayList<Admin> getAdminInfo() {
		if (adminInfo.isEmpty()) {
			allAdmin();
		}
		return adminInfo;
	}

	private Database() {
	}

	public static Database getInstance() {
		if (database == null) {
			database = new Database();
		}
		return database;
	}

	private static final String URL = "jdbc:mysql://localhost:3306/coding";
	private static final String NAME = "root";
	private static final String PSWD = "admin";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
//		Database db = Database.getInstance();

		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL, NAME, PSWD);
	}

	public boolean addUser(String ZTAIID, String name, String email, String phone, String password, boolean authorize) {
		if (userExists(name, email)) {
			System.out.println("User exists with this email/username");
			return false; // User with the provided username or email already exists
		}

		String sql = "INSERT INTO student_info (ztai_id, name, email, phone, password, authorize) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, ZTAIID);
			stmt.setString(2, name);
			stmt.setString(3, email);
			stmt.setString(4, phone);
			stmt.setString(5, password);
			stmt.setBoolean(6, authorize);
			Candidates cand = new Candidates(ZTAIID, name, email, phone, password, false);
			Candidates.setCand(cand);
//			candInfo.add(cand);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if insertion successful
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		}
	}

	public boolean addAdmin(String ZTAIID, String name, String email, String phone, String password,
			boolean authorize) {
		if (userExists(name, email)) {
			System.out.println("User exists with this email/username");
			return false; // User with the provided username or email already exists
		}

		String sql = "INSERT INTO admin (ztai_id, name, email, phone, password, authorize) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, ZTAIID);
			stmt.setString(2, name);
			stmt.setString(3, email);
			stmt.setString(4, phone);
			stmt.setString(5, password);
			stmt.setBoolean(6, authorize);
			Admin admin = new Admin(ZTAIID, name, email, phone, password);
//			adminInfo.add(admin);
			Admin.setAdmin(admin);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if insertion successful
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		}
	}

	private boolean userExists(String name, String email) {
		String sql = "";
		if (email.endsWith("@admin.in")) {
			sql = "SELECT COUNT(*) FROM admin WHERE name = ? OR email = ?";

		} else {
			sql = "SELECT COUNT(*) FROM student_info WHERE name = ? OR email = ?";
		}
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, email);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);
					return count > 0; // Return true if user with username or email already exists
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean validateUser(String name, String email, String password) {
		String sql = "";
		boolean a = false;
		boolean c = false;
		if (email.endsWith("@admin.in")) {
			sql = "SELECT * FROM admin WHERE name = ? AND password = ?";
			a = true;
		} else {
			sql = "SELECT * FROM student_info WHERE name = ? AND password = ?";
			c = true;
		}
		try (Connection conn = Database.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, password);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					if (rs.getBoolean("authorize") == true) {
						if (a) {

//							Admin object = new Admin(rs.getString("ZTAIID"), rs.getString("name"), rs.getString("email"), 
//									rs.getString("phone"), rs.getString("password"));
//							System.out.println("done");
//							Admin.setAdmin(object);

							allAdmin();
							for (Admin admin : adminInfo) {
								if (admin.getEmail().equals(email)) {
									Admin object = new Admin(admin.getZTAIID(), admin.getName(), admin.getEmail(),
											admin.getPhone(), admin.getPassword());
									Admin.setAdmin(object);
									break;
								}
							}
							return true;
						}
						if (c) {
							Candidates object = new Candidates(rs.getString("ztai_id"), rs.getString("name"),
									rs.getString("email"), rs.getString("phone"), rs.getString("password"),
									rs.getBoolean("authorize"));
							System.out.println("done");
							Candidates.setCand(object);

//							allCand();
//							for (Candidates cand : candInfo) {
//								if (cand.getEmail() == email) {
//									Candidates object = new Candidates(cand.getZTAIID(), cand.getName(),
//											cand.getEmail(), cand.getPhone(), cand.getPassword());
//									Candidates.setCand(object);
//									return true;
//								}
//							}
							return true;
						}
					} else {
						System.out.println("User Not Authorized");
						return false;
					}
				}
				return true;
			}
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
			return false; // Return false in case of any exception
		}
	}

	public static ArrayList<Candidates> allCand() {
		String sql = "SELECT * from student_info";
		candInfo = new ArrayList<Candidates>();
		try (Connection conn = Database.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				String ZTAIID = result.getString(1);
				String name = result.getString(2);
				String email = result.getString(3);
				String phone = result.getString(4);
				String password = result.getString(5);
				boolean authorize = result.getBoolean(6);

				Candidates cand = new Candidates(ZTAIID, name, email, phone, password, authorize);
				candInfo.add(cand);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return candInfo;
	}

	public static void allAdmin() {
		String sql = "SELECT * from admin";
		adminInfo = new ArrayList<Admin>();
		try (Connection conn = Database.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				String ZTAIID = result.getString(1);
				String name = result.getString(2);
				String email = result.getString(3);
				String phone = result.getString(4);
				String password = result.getString(5);

				Admin admin = new Admin(ZTAIID, name, email, phone, password);
				adminInfo.add(admin);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean updateCandidate(String ZTAIID, String name, String email, String phone) {
		String sql = "UPDATE student_info SET name = ?, email = ?, phone = ? WHERE ztai_id = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, phone);
			stmt.setString(4, ZTAIID);
			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean authorize(String ZTAIID) {
		String sql = "UPDATE student_info set authorize = 1 where ztai_id = ? ";
		try (Connection conn = Database.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, ZTAIID);
			int rowsUpdated = stmt.executeUpdate();
			System.out.println("updated" + " " + rowsUpdated);
			return rowsUpdated > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addTodo(String ZTAIID, String todoNote) {
		String sql = "INSERT into todo(ztai_id, todoNote) values(?,?) ";
		try (Connection conn = Database.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, ZTAIID);
			stmt.setString(2, todoNote);
			System.out.println(todoNote);
			int rowsUpdated = stmt.executeUpdate();
			System.out.println("TODO updated" + " " + rowsUpdated);
			return rowsUpdated > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> getTodo(String ZTAIID) {
        List<String> todoItems = new ArrayList<>();
        String sql = "SELECT tid, todonote, completed FROM todo WHERE ztai_id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ZTAIID);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                int tid = result.getInt("tid");
                String todoNote = result.getString("todonote");
                boolean completed = result.getBoolean("completed");
                todoItems.add(tid + ":" + todoNote + ":" + completed);            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todoItems;
    }

	public boolean deleteTodo(int tid) {
		String sql = "DELETE from todo where tid = ?";
		try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tid);
            int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public boolean updateTodo(int tid, boolean completed) {
	    String sql = "UPDATE todo SET completed = ? WHERE tid = ?";
	    try (Connection conn = Database.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setBoolean(1, completed);
	        stmt.setInt(2, tid);
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public void addIssue(String ZTAIID, String title, String description, String fileURL, String fileFileName) {
        String sql = "INSERT INTO reportedissues (ztai_id, Title, Description, FileURL, FileName) VALUES (?, ?, ?, ?, ?)";
	    try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ZTAIID);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, fileURL);
            stmt.setString(5, fileFileName);
            stmt.executeUpdate();
        } catch (Exception e) {
			e.printStackTrace();	
        }
    }

    public List<Map<String, Object>> getIssues() {
        List<Map<String, Object>> issues = new ArrayList<>();
        String sql = "SELECT * FROM reportedissues";
	    try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> issue = new HashMap<>();
                issue.put("IssueID", rs.getInt("IssueID"));
                issue.put("ZTAIID", rs.getString("ztai_id"));
                issue.put("Title", rs.getString("Title"));
                issue.put("Description", rs.getString("Description"));
                issue.put("FileURL", rs.getString("FileURL"));
                issue.put("FileName", rs.getString("FileName"));
                issue.put("ReportedAt", rs.getTimestamp("ReportedAt"));
                issues.add(issue);
            }
        } catch (Exception e) {
			e.printStackTrace();
		}
        return issues;
    }

    public List<Map<String, Object>> getIssues(String ZTAIID) {
        List<Map<String, Object>> issues = new ArrayList<>();
        String sql = "SELECT * FROM reportedissues WHERE ztai_id = ?";
	    try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ZTAIID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> issue = new HashMap<>();
                    issue.put("IssueID", rs.getInt("IssueID"));
                    issue.put("ZTAIID", rs.getString("ztai_id"));
                    issue.put("Title", rs.getString("Title"));
                    issue.put("Description", rs.getString("Description"));
                    issue.put("FileURL", rs.getString("FileURL"));
                    issue.put("FileName", rs.getString("FileName"));
                    issue.put("ReportedAt", rs.getTimestamp("ReportedAt"));
                    issues.add(issue);
                }
            }
        } catch (Exception e) {
			e.printStackTrace();
		}
        return issues;
    }

	public boolean deleteIssue(String issueId) {
        String sql = "DELETE FROM reportedissues WHERE IssueID = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, issueId);
            stmt.executeUpdate();
            System.out.println("Deleted");
        } catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
    }
}
