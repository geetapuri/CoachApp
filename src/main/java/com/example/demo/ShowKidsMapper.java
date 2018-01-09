package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class ShowKidsMapper implements RowMapper {
	
	private static Logger logger = LogManager.getLogger(ShowKidsMapper.class);

	@Override
	public ShowKids mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in ShowKidsMapper maprow");
		ShowKids kid = new ShowKids();
		kid.setKidName(rs.getString("kidName"));
		kid.setKidID(rs.getString("kidID"));
		kid.setGroupID(rs.getString("groupID"));
		kid.setGroupName(rs.getString("groupName"));
		kid.setPackageID(rs.getString("packageID"));
		kid.setPackageName(rs.getString("packageName"));
		
		logger.info("received kidName as  "+ kid.getKidName());
		logger.info("received kidID as  "+ kid.getKidID());
		
		
		return kid;
	}

}
