package com.example.demo;

import java.sql.SQLException;


import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.PreparedStatement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



public class AttendanceJDBCTemplate implements AttendanceDAO{
	
private static Logger logger = LogManager.getLogger(AttendanceJDBCTemplate.class);

	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		
	}
	
	String result;

	@Override
	public String markAttendance(MarkAttendance data) {
		// TODO Auto-generated method stub
		//execute insert for list of kids and return success / failure
		
		
		
		List<Kid> kids = data.getKidsList();
		logger.info("size of kids object  : " + kids.size());
		logger.info("details of data received: "+ data.getGroupID()
		+ data.getDate() + data.getKidsList());
		
	
		
		String SQL = "insert into ATTENDANCE (DateOfAttendance, GROUPOFKIDS_GroupID, KID_KidID, PresentAbsent) "
				+ " values (?,?,?,?) "
				+ " ON DUPLICATE KEY UPDATE " 
				+ " PRESENTABSENT= values(PRESENTABSENT)";

		jdbcTemplateObject.batchUpdate( SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				
				//((List<Integer>) data).get(i);
				ps.setDate(1, data.getDate());
				ps.setString(2, data.getGroupID());
				ps.setString(3,  kids.get(i).getKidID());
				ps.setString(4, kids.get(i).getPresent());
				//ps.setInt(5, i+11);
				
				logger.info("kid ID = "+ kids.get(i).getKidID());
				logger.info("kid present = "+ kids.get(i).getPresent());
				
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return kids.size();
			}
			
		});
		
		String SQL2 = "INSERT INTO FEEMGMT (DateOfAttendance, Present, FeePaid, KidID, FeeID)"
				+ " values(?,?,?,?,?)  "
				+ " ON DUPLICATE KEY UPDATE "
				+ " Present = values(Present)";
		
		jdbcTemplateObject.batchUpdate( SQL2, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				
				//((List<Integer>) data).get(i);
				ps.setDate(1, data.getDate());
				ps.setString(2, kids.get(i).getPresent());
				ps.setString(3, "N");
				ps.setString(4,  kids.get(i).getKidID());
				ps.setString(5, "1");
				//ps.setInt(5, i+11);
				
				logger.info("kid ID = "+ kids.get(i).getKidID());
				logger.info("kid present = "+ kids.get(i).getPresent());
				
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return kids.size();
			}
			
				
			
		});
		
		return result ="Success";
	}
	
	

	public List<Attendance> checkAttendance(Attendance data) {
		// TODO Auto-generated method stub
		
		logger.info("date to be queried = " + data.getDate());
		logger.info("Group ID to be queried = " + data.getGroupID());

		String SQL = "SELECT * FROM ATTENDANCE WHERE GROUPOFKIDS_GROUPID=?"
				+ " AND DATEOFATTENDANCE=?" ;
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getGroupID() ,data.getDate()},new AttendanceMapper());
	    
	   
		return attendance;
	}
	
	public List<Attendance> viewAttendanceKid(Attendance data) {
		// TODO Auto-generated method stub
		
		
		String SQL = "SELECT * FROM ATTENDANCE WHERE KID_KidID=? ORDER BY "
				+ " DateOfAttendance DESC" ;
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getKidID() },new AttendanceMapper());
	    
	   
		return attendance;
	}

}
