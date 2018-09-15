package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class GroupJDBCTemplate implements GroupDAO{
	
private static Logger logger = LogManager.getLogger(GroupJDBCTemplate.class);

	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		
	}
	

	@Override
	public List<GroupOfKids> addGroup(GroupOfKids data) {
		// TODO Auto-generated method stub
		
		List <GroupOfKids> result = null;
		
		String sql3= "SELECT PackageID FROM PACKAGE WHERE PackageName=?";
		
		String packageID =(String) jdbcTemplateObject.queryForObject(sql3, 
					
					new Object[] {data.getPackageName()}, String.class);
		
		String SQL = "INSERT INTO GROUPOFKIDS (GROUPNAME, FeeAmount, CoachID, PackageID) VALUES (?,?,?,?)";
		logger.info("inserting groupname as : " + data.getGroupName());
		
		int resultOfQuery = jdbcTemplateObject.update( SQL, data.getGroupName(), data.getFeeAmount(),
														data.getCoachID(), packageID  );
		
		logger.info("result of query after insert into groupofkids = "+ resultOfQuery);
		
		if (resultOfQuery!=0) {
			
			String SQL2 = "Select * from GROUPOFKIDS ";
			List<GroupOfKids> groupList =  jdbcTemplateObject.query(SQL2, new GroupMapper());
			
			return groupList;
			}
			else return null;
		
	}


	@Override
	public List<GroupOfKids> getGroups(String coachID) {
		// TODO Auto-generated method stub
		
		String SQL = "SELECT * FROM GROUPOFKIDS WHERE "
				+ " CoachID = ?";
		
		List<GroupOfKids> groupList = jdbcTemplateObject.query(SQL, new Object[] {coachID}, new GroupMapper());
		
		return groupList;
		
	}


	public String updateGroup(GroupOfKids data) {
		// TODO Auto-generated method stub
		
		logger.info("calling updateKid method now");
		
		String SQL = "UPDATE GROUPOFKIDS SET GroupName=?, FeeAmount=? , PackageID=?  "
					+ " WHERE GroupID=?";
		
		int result = jdbcTemplateObject.update(SQL, data.getGroupName(), data.getFeeAmount(), data.getPackageID(),
													data.getGroupID());
		
		logger.info("updated "+ result + "records");
		
		return Integer.toString(result);
		
	}


}
