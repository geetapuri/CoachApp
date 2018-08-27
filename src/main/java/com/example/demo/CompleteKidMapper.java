package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class CompleteKidMapper implements RowMapper{
	
	private static Logger logger = LogManager.getLogger(KidMapper.class);
	
	@Override
	public Kid mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in Kid mapper maprow");
		Kid kid = new Kid();
		kid.setKidName(rs.getString("KidName"));
		kid.setKidID(rs.getString("KidID"));
		kid.setPresent("A");
		kid.setGroupID(rs.getString("GroupID"));
		kid.setGroupName(rs.getString("GroupName"));
		kid.setPackageID(rs.getString("PackageID"));
		kid.setPackageName(rs.getString("PackageName"));
		kid.setAvatarSrc(rs.getString("AVATAR"));
		
		logger.info("received kidName as  "+ kid.getKidName());
		logger.info("received kidID as  "+ kid.getKidID());
		
		return kid;
	}

}
