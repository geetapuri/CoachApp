package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class CoachMapper implements RowMapper{

	private static Logger logger = LogManager.getLogger(ParentMapper.class);

	@Override
	public Coach mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in CoachMapper  maprow");
		Coach coach = new Coach();
		coach.setCoachName(rs.getString("coachName"));
		coach.setCoachID(rs.getString("coachID"));
		
		
		
		return coach;
	}

}
