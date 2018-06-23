package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class KidJDBCTemplate implements KidDAO {
	
	private static Logger logger = LogManager.getLogger(KidJDBCTemplate.class);

	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	//must not be ds - as by auto generation
	public void setDataSource(DataSource dataSource) {
		//this.dataSource = dataSource;
	    this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	   
	}

	@Override
	public List<Kid> getKids(String groupID) {
		// TODO Auto-generated method stub
		logger.info("calling getKids(groupID) now for groupID = "+ groupID);
		
		String SQL = "select KID.KidName, KID.KidID, KID.GROUPOFKIDS_GroupID, KID.PACKAGE_PACKAGEID  "
				+ " from KID"
				+ " where "
				+ " KID.GROUPOFKIDS_GROUPID = ?  ";
		
	    List <Kid> kid = jdbcTemplateObject.query(SQL, new Object[] {groupID}, new KidMapper());
	    
	   
	    return kid;
		
	
		
	
	}

	@Override
	public List<Kid> addKid(Kid kid) {
		String SQL = "INSERT INTO KID (GROUPOFKIDS_GROUPID, PACKAGE_PACKAGEID, KIDNAME) VALUES (?,?,?)";
		logger.info("inserting groupname as : " + kid.getGroupID());
		
		int resultOfQuery = jdbcTemplateObject.update( SQL, kid.getGroupID(), kid.getPackageID(), kid.getKidName() );
		
		logger.info("result of query = "+ resultOfQuery);
		
		if (resultOfQuery!=0) {
			
			String SQL2 = "Select * from KID where GROUPOFKIDS_GROUPID = ? ";
			List<Kid> kids =  jdbcTemplateObject.query(SQL2, new Object[] {kid.getGroupID()}, new KidMapper());
			
			return kids;
			}
			else return null;
		
	}

	@Override
	public List<Kid> getKids() {
		// TODO Auto-generated method stub
		logger.info("calling getKids() now ");
		
		String SQL = "select KID.KidName, KID.KidID,  "
				+ 		"GROUPOFKIDS.GroupID, GROUPOFKIDS.GroupName, "
				+ 		"PACKAGE.PackageName, PACKAGE.PackageID  "
				+ 		"from KID, GROUPOFKIDS, PACKAGE"
				+ 		" where KID.groupOfkids_groupID= GROUPOFKIDS.GroupID "
				+ 		" AND KID.package_packageID = PACKAGE.PackageID "
				+ 		" ORDER BY KID.KidID";
		
	    List <Kid> kids = jdbcTemplateObject.query(SQL, new CompleteKidMapper());
	    
	   
	    return kids;
	}
	
	public List<Kid> getKidsParent(String parentID) {
		// TODO Auto-generated method stub
		logger.info("calling getKidsParent() now ");
		
		String SQL = "select KID.KidName, KID.KidID,  "
				+ 		"GROUPOFKIDS.GroupID, GROUPOFKIDS.GroupName, "
				+ 		"PACKAGE.PackageName, PACKAGE.PackageID, PARENT.ParentID, PARENT.ParentName  "
				+ 		"from KID, GROUPOFKIDS, PACKAGE, PARENT"
				+ 		" where KID.groupOfkids_groupID= GROUPOFKIDS.GroupID "
				+ 		" AND KID.package_packageID = PACKAGE.PackageID "
				+ 		" AND PARENT.ParentID = ? "
				+ 		" AND KID.ParentID = PARENT.ParentID	" 
				
				+ 		" ORDER BY KID.KidID";
		
	    List <Kid> kids = jdbcTemplateObject.query(SQL, new Object[] {parentID} ,new CompleteKidMapper());
	    
	   
	    return kids;
	}

	public String updateKid(Kid data) {
		// TODO Auto-generated method stub
		logger.info("calling updateKid method now");
		
		String SQL = "UPDATE KID SET GROUPOFKIDS_GroupID=?, KidName=? " +
					"WHERE KidID=?";
		
		int result = jdbcTemplateObject.update(SQL, data.getGroupID(), 
													data.getKidName(),
													data.getKidID());
		
		logger.info("updated "+ result + "records");
		
		return Integer.toString(result);
	}

	public String deleteKid(Kid data) {
		// TODO Auto-generated method stub
		logger.info("calling deleteKid method now");
		String SQL = "DELETE FROM KID WHERE KidID=?";
		
		int result = jdbcTemplateObject.update(SQL, data.getKidID());
		
		return Integer.toString(result);
		
	}

	public List<Kid> getKidsList() {
		// TODO Auto-generated method stub
		logger.info("calling getKidsList() now ");
		
		String SQL = "SELECT * FROM KID";
		
	    List <Kid> kidsList = jdbcTemplateObject.query(SQL, new KidMapper());
		
		return kidsList;
	}
	
	

}
