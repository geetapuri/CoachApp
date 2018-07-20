package com.example.demo;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
	public String addFees(List<FeeMgmt> data) {
		// TODO Auto-generated method stub
		
		logger.info("calling addFees method now");
		//TODO batchupdate like mark attendance here
	
		
		String SQL = "UPDATE FEEMGMT  SET FeePaid= ? " +
					" WHERE kidID=? AND  DATEOFATTENDANCE =?";
		
		jdbcTemplateObject.batchUpdate( SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				
				//((List<Integer>) data).get(i);
				ps.setString(1, data.get(i).getFeePaid());
				
				ps.setString(2,  data.get(i).getKidID());
				ps.setString(3, data.get(i).getDateOfAttendance());
				//ps.setInt(5, i+11);
				
				
				
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
			
		});
		
		
		return "SUCCESS ";
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
