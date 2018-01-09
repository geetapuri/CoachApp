package com.example.demo;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.PreparedStatement;

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
		
	
		
		String SQL = "insert into Attendance (DateOfAttendance, GROUPOFKIDS_GroupID, KID_KidID, PresentAbsent) values (?,?,?,?)";
		
	    
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
		
		logger.info("date to be queried = " + data.getDateOfAttendance());
		logger.info("Group ID to be queried = " + data.getGroupOfKids_GroupID());

		String SQL = "SELECT A.PRESENTABSENT, A.DATEOFATTENDANCE, G.GROUPNAME, K.KIDNAME, K.KIDID "
				+ " FROM ATTENDANCE A, GROUPOFKIDS G, KID K " + 
				" WHERE  A.KID_KidID = K.KIDID " + 
				" AND 	G.GROUPID=? " + 
				" AND  A.DateOfAttendance=?" ;
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getGroupOfKids_GroupID() ,data.getDateOfAttendance()},new AttendanceMapper());
	    
	   
		return attendance;
	}

}
