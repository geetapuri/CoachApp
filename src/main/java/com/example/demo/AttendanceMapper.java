package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class AttendanceMapper implements RowMapper {
	private static Logger logger = LogManager.getLogger(AttendanceMapper.class);

	@Override
	public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in Attendance mapper maprow");
		Attendance attendance = new Attendance();
		attendance.setKidID(rs.getString("KID_KIDID"));
		attendance.setGroupID(rs.getString("GROUPOFKIDS_GROUPID"));
		attendance.setPresentAbsent(rs.getString("PRESENTABSENT"));
		attendance.setDate(rs.getDate("DATEOFATTENDANCE"));
		
		logger.info("date received : "+ attendance.getDate());
		logger.info("kid id received : "+ attendance.getKidID());
		
		return attendance;

	}

}
