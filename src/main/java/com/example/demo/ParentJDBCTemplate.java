package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class ParentJDBCTemplate implements ParentDAO {
	private static Logger logger = LogManager.getLogger(KidJDBCTemplate.class);

	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	//must not be ds - as by auto generation
	public void setDataSource(DataSource dataSource) {
		//this.dataSource = dataSource;
	    this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	   
	}


	@Override
	public List<Parent> getParentID(Parent data) {
		// TODO Auto-generated method stub
		String SQL = "select * from PARENT "
				+ " where "
				+ " ParentName =  ?  ";
		
		List <Parent> parent = jdbcTemplateObject.query(SQL, new Object[] {data.getParentName()}, new ParentMapper());
		    
		   
		  return parent;
	}


	

}
