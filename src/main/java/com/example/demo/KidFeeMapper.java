package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class KidFeeMapper implements RowMapper {

	private static Logger logger = LogManager.getLogger(KidMapper.class);

	@Override
	public Kid mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in Kid fee mapper maprow");
		Kid kid = new Kid();
		kid.setKidName(rs.getString("kidName"));
		kid.setKidID(rs.getString("kidID"));
		kid.setPresent("A");
		kid.setGroupID(rs.getString("GroupID"));
		//kid.setGroupName(rs.getString("groupName"));
		kid.setPackageID(rs.getString("Package_packageID"));
		//kid.setPackageName(rs.getString("packageName"));
		kid.setCoachID(rs.getString("CoachID"));
		kid.setInvoiceAmount(rs.getString("InvoiceAmount"));
		kid.setInvoiceDue(rs.getString("InvoiceDue"));
		
		
		logger.info("received kidName as  "+ kid.getKidName());
		logger.info("received kidID as  "+ kid.getKidID());
		
		return kid;
	}
	

}
