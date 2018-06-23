package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class FeeMgmtJDBCTemplate implements FeeMgmtDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private static Logger logger = LogManager.getLogger(FeeMgmtJDBCTemplate.class);


	public void setDataSource(DataSource dataSource) {
		//this.dataSource = dataSource;
	    this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	   
	}

	@Override
	public String addFees(FeeMgmt data) {
		// TODO Auto-generated method stub
		
		logger.info("calling addFees method now");
		
		String SQL = "UPDATE FEEMGMT SET FeePaid= 'Y' " +
					" WHERE kidID=? AND DATEOFATTENDANCE =?";
		
		int result = jdbcTemplateObject.update(SQL, data.getKidID(), data.getDateOfAttendance());
		
		logger.info("updated "+ result + "records");
		
		//return Integer.toString(result);
		if (result!=0) return "SUCCESS";
		else return "FAILURE TO UPDATE";
	}

	@Override
	public List<FeeMgmt> viewFees(FeeMgmt data) {
		// TODO Auto-generated method stub
		logger.info("inside view Fees");
		
		String SQL = "SELECT * FROM FEEMGMT WHERE KIDID=? ORDER BY "
				+ " DATEOFATTENDANCE DESC" ;
		
	    List <FeeMgmt> feeList = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getKidID() },new FeeMgmtMapper());
		
	    return feeList;
	}

}
