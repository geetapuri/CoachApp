package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
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
		
		String SQL = "select KID.KidName, KID.KidID, KID.GROUPOFKIDS_GroupID, KID.PACKAGE_PACKAGEID, "
				+ " GROUPOFKIDS.CoachID "
				+ " from KID, GROUPOFKIDS"
				+ " where "
				+ " KID.GROUPOFKIDS_GROUPID = ? "
				+ " AND KID.GROUPOFKIDS_GROUPID = GROUPOFKIDS.GroupID ";
		
	    List <Kid> kid = jdbcTemplateObject.query(SQL, new Object[] {groupID}, new KidMapper());
	    
	   
	    return kid;
		
	
		
	
	}

	@Override
	public List<Kid> addKid(Kid kid) {
		
		//get parent ID from parentName
		logger.info("parent name received as " + kid.getParentName());
		
		
		String parentID;
		String SQL1 = "SELECT ParentID from PARENT WHERE ParentName =?";
		try {
				 parentID =(String) jdbcTemplateObject.queryForObject(SQL1, 
			
									new Object[] {kid.getParentName()}, String.class);
				
				
		} catch (EmptyResultDataAccessException e) {
			 parentID=null;
		}
		
		if (parentID == null || parentID.equals("")) {
			String sql5 = "INSERT INTO PARENT (ParentName) VALUES(?)";
			int resultOfQuery5 = jdbcTemplateObject.update(sql5, kid.getParentName());
			parentID = (String) jdbcTemplateObject.queryForObject(SQL1, 
					new Object[] {kid.getParentName()}, String.class);
		}
		
		String SQL = "INSERT INTO KID (GROUPOFKIDS_GROUPID, PACKAGE_PACKAGEID, KidName, ParentID) VALUES (?,?,?,?)";
		logger.info("inserting groupname as : " + kid.getGroupID());
		
		
		int resultOfQuery = jdbcTemplateObject.update( SQL, kid.getGroupID(), kid.getPackageID(), kid.getKidName(), parentID );
		
		logger.info("result of query = "+ resultOfQuery);
		
		if (resultOfQuery!=0) {
			
			String sql4= "SELECT MAX(KidID) from KID ";
			
			String kidID= (String)jdbcTemplateObject.queryForObject(
					sql4,  String.class);
			
			String sql3 = "INSERT INTO INVOICE_HEADER (KidID,InvoiceDate,InvoiceAmount,"
					+ "	InvoiceDue, PresentCounter) VALUES (?, now(), '100', 'N', 0)";
			
			int resultOfQuery3 = jdbcTemplateObject.update(sql3, kidID);
			
			String SQL2 = "Select * from KID, GROUPOFKIDS where KID.GROUPOFKIDS_GROUPID = ? "
					+ " AND KID.GROUPOFKIDS_GROUPID = GROUPOFKIDS.GroupID";
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
		
		String SQL = "select KID.KidName, KID.KidID, KID.AVATAR,  "
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
	
	public List<Kid> getKidsCoach(String coachID) {
		// TODO Auto-generated method stub
		logger.info("calling getKidsCoach() now ");
		//String coachID = coach.getCoachID();
		
		String SQL = "select KID.KidName, KID.KidID, KID.AVATAR,  "
				+ 		"GROUPOFKIDS.GroupID, GROUPOFKIDS.GroupName, "
				+ 		"PACKAGE.PackageName, PACKAGE.PackageID, COACH.coachID, COACH.coachName  "
				+ 		"from KID, GROUPOFKIDS, PACKAGE, COACH"
				+ 		" where KID.groupOfkids_groupID= GROUPOFKIDS.GroupID "
				+ 		" AND KID.package_packageID = PACKAGE.PackageID "
				+ 		" AND COACH.coachID = ? "
				+ 		" AND GROUPOFKIDS.CoachID = COACH.coachID	" 				
				+ 		" ORDER BY GROUPOFKIDS.GroupID, KidName";
		
	    List <Kid> kids = jdbcTemplateObject.query(SQL, new Object[] {coachID} ,new CompleteKidMapper());
	    
	   
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

	public List<Kid> getKidsList(Coach coach) {
		// TODO Auto-generated method stub
		logger.info("calling getKidsList() now ");
		String coachID=coach.getCoachID();
		
		String SQL = "SELECT * FROM KID K,  GROUPOFKIDS G "
				+ " where K.GROUPOFKIDS_GroupID = G.GroupID "
				+ " AND G.CoachID= ?";
		
	    List <Kid> kidsList = jdbcTemplateObject.query(SQL,new Object[] {coachID}, new KidMapper());
		
		return kidsList;
	}

	public List<Kid> getKidsFee(String groupID) {
		logger.info("calling getKidsFee(groupID) now for groupID = "+ groupID);
		
		String SQL = "select KID.KidName, KID.KidID, KID.GROUPOFKIDS_GroupID, KID.PACKAGE_PACKAGEID, "
				+ " GROUPOFKIDS.CoachID, INVOICE_HEADER.InvoiceAmount, INVOICE_HEADER.InvoiceDue "
				+ " from KID, GROUPOFKIDS, INVOICE_HEADER"
				+ " where "
				+ " KID.GROUPOFKIDS_GROUPID = ? "
				+ " AND KID.GROUPOFKIDS_GROUPID = GROUPOFKIDS.GroupID "
				+ " AND KID.KidID = INVOICE_HEADER.KidID ";
		
	    List <Kid> kid = jdbcTemplateObject.query(SQL, new Object[] {groupID}, new KidFeeMapper());
	    
	   
	    return kid;	
	}
	
	

}
