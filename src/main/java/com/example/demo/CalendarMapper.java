package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class CalendarMapper implements RowMapper<Schedule>{
	
	private static Logger logger = LogManager.getLogger(CalendarMapper.class);

	@Override
	public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in calendar mapper maprow");
		Schedule schedule = new Schedule();
		schedule.setTime(rs.getString("TIME"));
		schedule.setDate(rs.getDate("Date"));
		schedule.setGroupName(rs.getString("GroupName"));
		schedule.setGroupID(rs.getString("GroupID"));
		schedule.setCalendarID(rs.getString("CalendarID"));
		
		
		return schedule;
	}

	

}
