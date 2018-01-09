package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class GroupMapper implements RowMapper {
	private static Logger logger = LogManager.getLogger(GroupMapper.class);

	
	@Override
	public GroupOfKids mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		logger.info("in GroupMapper now");
		
		GroupOfKids groupList = new GroupOfKids();
		groupList.setGroupName(rs.getString("GroupName"));
		groupList.setGroupID(rs.getString("GroupID"));
		
		logger.info("group name = "+ groupList.getGroupName());
		return groupList;
	}
	
	

}
