package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class KidFeeMapperParent implements RowMapper {

	private static Logger logger = LogManager.getLogger(KidFeeMapperParent.class);

	@Override
	public Kid mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in Kid fee mapper maprow");
		Kid kid = new Kid();
		kid.setKidName(rs.getString("KidName"));
		kid.setKidID(rs.getString("KidID"));
		kid.setParentID(rs.getString("ParentID"));
		kid.setGroupID(rs.getString("GroupID"));
		kid.setInvoiceAmount(rs.getString("InvoiceAmount"));
		kid.setInvoiceDue(rs.getString("InvoiceDue"));
		kid.setGroupName(rs.getString("GroupName"));
		
		
		
		return kid;
	}
	

}
