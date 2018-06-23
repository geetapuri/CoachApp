package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class FeeMgmtMapper implements RowMapper {

	private static Logger logger = LogManager.getLogger(FeeMgmtMapper.class);


	@Override
	public FeeMgmt mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in FeeMgmtMapper now");
		
		FeeMgmt feeList = new FeeMgmt();
		feeList.setDateOfAttendance(rs.getString("DateOfAttendance"));
		feeList.setPresent(rs.getString("Present"));
		feeList.setFeePaid(rs.getString("FeePaid"));
		feeList.setFeeMgmtID(rs.getString("KidID"));
		feeList.setFeeID(rs.getString("FeeID"));
		feeList.setKidID(rs.getString("KidID"));
		
		logger.info("kid ID = "+ feeList.getKidID());
		return feeList;
		
		
	}

}
