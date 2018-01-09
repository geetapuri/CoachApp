package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class PackageMapper implements RowMapper {
	
private static Logger logger = LogManager.getLogger(PackageMapper.class);

	
	@Override
	public PackageDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		logger.info("in PackageMapper now");
		
		PackageDetails packageDetails = new PackageDetails();
		 
		packageDetails.setAgeGroupID(rs.getString("AGEGROUP_AGEGROUPID"));
		packageDetails.setPackageID(rs.getString("PACKAGEID"));
		packageDetails.setFeeID(rs.getString("FEE_FEEID"));
		packageDetails.setNumOfClasses(rs.getString("NUMOFCLASSES"));
		packageDetails.setPackageName(rs.getString("PackageName"));
		
		logger.info("package id obtained : " + packageDetails.getPackageID());
		logger.info("package name obtained: " + packageDetails.getPackageName());
		
		return packageDetails;
	}
	
}
