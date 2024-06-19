package com.ztai.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ztai.model.ProgressCard;
import com.ztai.util.DBConnectionManager;


public class ProgressCardDAO {

	public List<ProgressCard> getProgressCardsInfo() {
		String studentId = "ztai012024";
		
		String query = "select * from progresscards";
		List<ProgressCard> progressCardInfo = new ArrayList<>();
		
		try(Connection con = DBConnectionManager.getConnection();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery() ;
			
			while(rs.next()) {
				int cardId = rs.getInt("card_id");
				String cardName = rs.getString("card_name");
				int totalSections = rs.getInt("total_sections");
				int totalSubSections = rs.getInt("total_sub_sections");
				int totalMarks = rs.getInt("total_marks");
				String cardType = rs.getString("type");
				String tableName = rs.getString("student_info"); 
		
				int getStudentTotalMarks = getStudentTotalMark(con,studentId,tableName);
				int percentage = (int) Math.round((getStudentTotalMarks * 100.0) / totalMarks);
				System.out.println(percentage);
				ProgressCard pc = new ProgressCard(cardId, cardName,cardType, totalSections, totalSubSections, totalMarks,getStudentTotalMarks,percentage);
				progressCardInfo.add(pc);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return progressCardInfo;
		
	}
	private int getStudentTotalMark(Connection con, String studentId, String tableName) throws SQLException {
		int totalScores = 0;
		String query = "select SUM(score) as totalscore from "+ tableName +" where ztai_id = ?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, studentId);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			totalScores = rs.getInt("totalscore");
		} 
		return totalScores;
		
	}
}