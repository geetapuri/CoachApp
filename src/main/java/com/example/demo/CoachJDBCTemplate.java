package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class CoachJDBCTemplate implements CoachDAO{
	private static Logger logger = LogManager.getLogger(KidJDBCTemplate.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		
	}

	@Override
	public List<Coach> getCoachID(Coach data) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				String SQL = "select * from COACH "
						+ " where "
						+ " coachName =  ?  ";
				
				List <Coach> coach = jdbcTemplateObject.query(SQL, new Object[] {data.getCoachName()}, new CoachMapper());
				    
				   
				  return coach;
	}

}
