package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class ShowKidsFeeMapper implements RowMapper {

	private static Logger logger = LogManager.getLogger(FeeMgmtMapper.class);


	@Override
	public ShowKidsFee mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in FeeMgmtMapper now");
		
		ShowKidsFee kidsFeeList = new ShowKidsFee();
		kidsFeeList.setDateOfAttendance(rs.getString("DateOfAttendance"));
		kidsFeeList.setPresent(rs.getString("Present"));
		kidsFeeList.setFeePaid(rs.getString("FeePaid"));
		kidsFeeList.setFeeMgmtID(rs.getString("FeeMgmtID"));
		kidsFeeList.setFeeID(rs.getString("FeeID"));
		kidsFeeList.setKidID(rs.getString("KidID"));
		kidsFeeList.setGroupID(rs.getString("GroupID"));
		kidsFeeList.setKidName(rs.getString("KidName"));
		
		logger.info("kid ID = "+ kidsFeeList.getKidID());
		return kidsFeeList;
		
		
	}
}
