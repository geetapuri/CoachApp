package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class ParentMapper implements RowMapper {

	private static Logger logger = LogManager.getLogger(ParentMapper.class);

	@Override
	public Parent mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		logger.info("in Parentmapper maprow");
		Parent parent = new Parent();
		parent.setParentName(rs.getString("ParentName"));
		parent.setParentID(rs.getString("ParentID"));
		
		
		logger.info("received parentName as  "+ parent.getParentName());
		logger.info("received parentID as  "+ parent.getParentID());
		
		return parent;
	}

}
