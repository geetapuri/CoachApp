package com.example.demo;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.PreparedStatement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



public class AttendanceJDBCTemplate implements AttendanceDAO{
	
private static Logger logger = LogManager.getLogger(AttendanceJDBCTemplate.class);

	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		
	}
	
	String result;

	@Override
	public String markAttendance(List<Attendance> data) {
		// TODO Auto-generated method stub
		//execute insert for list of kids and return success / failure
		
		
		
		//List<Kid> kids = data.getKidsList();
		//logger.info("size of kids object  : " + kids.size());
		//logger.info("details of data received: "+ data.getGroupID()
		//+ data.getDate() );
		
	
		
		String SQL = "insert into ATTENDANCE (DateOfAttendance, GROUPOFKIDS_GroupID, KID_KidID, PresentAbsent) "
				+ " values (?,?,?,?) "
				+ " ON DUPLICATE KEY UPDATE " 
				+ " PRESENTABSENT= values(PRESENTABSENT)";

		jdbcTemplateObject.batchUpdate( SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				
				//((List<Integer>) data).get(i);
				ps.setDate(1, java.sql.Date.valueOf(data.get(i).getDate()));
				ps.setString(2, data.get(i).getGroupID());
				ps.setString(3,  data.get(i).getKidID());
				ps.setString(4, data.get(i).getPresentAbsent());
				
				//check what kind of package is the child holding
				
				// per class : every present - invoice due 
				//per month: every start of month, invoice due
				// per 4 class: every 4 present, invoice due
				
				//check package
				String sql4 = "SELECT PackageID from GROUPOFKIDS WHERE GroupID = ?";
				
				String packageID = (String)jdbcTemplateObject.queryForObject(
						sql4, new Object[] { data.get(i).getGroupID() }, String.class);
				
				if (data.get(i).getPresentAbsent().equals("P")) {
					//String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter+1 "
							//+ " 	WHERE KidID = ?";
					//jdbcTemplateObject.update(sql3, data.get(i).getKidID());
				
				
					//List <Kid > kidList = jdbcTemplateObject.query(SQL,new Object[] 
				    		//{data.get(i).getKidID()} ,new KidMapper());
					
					switch (packageID) {
					case "1" :
						updateInvoiceHeaderForFourLesson(data.get(i).getKidID(), java.sql.Date.valueOf(data.get(i).getDate() ));
						logger.info("in 1");
						break;
					case "2" :
						
						updateInvoiceHeaderForMonth(data.get(i).getKidID(),java.sql.Date.valueOf(data.get(i).getDate() ), data.get(i).getGroupID());
						logger.info("in 2");
						break;
					case "3" :
						updateInvoiceHeaderForEveryLesson(data.get(i).getKidID(),java.sql.Date.valueOf(data.get(i).getDate()), 
														data.get(i).getGroupID());
						logger.info("in 3");
						break;
						
					}
					
					
				}
				else if (packageID.equals(2)) {
					updateInvoiceHeaderForMonth(data.get(i).getKidID(),java.sql.Date.valueOf(data.get(i).getDate() ), data.get(i).getGroupID());
					logger.info("in 2");
				}
				
				
				
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
			
		});
		
		String SQL2 = "INSERT INTO FEEMGMT (DateOfAttendance, Present, FeePaid, KidID, FeeID, GroupID)"
				+ " values(?,?,?,?,?,?)  "
				+ " ON DUPLICATE KEY UPDATE "
				+ " Present = values(Present)";
		
		jdbcTemplateObject.batchUpdate( SQL2, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				
				//((List<Integer>) data).get(i);
				ps.setDate(1, java.sql.Date.valueOf(data.get(i).getDate()));
				ps.setString(2, data.get(i).getPresentAbsent());
				ps.setString(3, "N");
				ps.setString(4,  data.get(i).getKidID());
				ps.setString(5, "1");
				ps.setString(6, data.get(i).getGroupID());
	
				//ps.setInt(5, i+11);
				
				
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
			
		});
		
	
		
		return result ="Success";
	}
	
	public void updateInvoiceHeaderForFourLesson(String kidID, Date date){
		String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter+1, InvoiceDate= ? "
		+ " 	WHERE KidID = ?";
			jdbcTemplateObject.update(sql3, date,kidID);
			
		int rows1 = jdbcTemplateObject.update(sql3, date,kidID);	
			
		String sql4 = "SELECT PresentCounter FROM INVOICE_HEADER WHERE KidID=?";
		//List <InvoiceHeader> invoiceList = jdbcTemplateObject.query(sql4, new InvoiceHeaderMapper());
		String presentCounter = (String)jdbcTemplateObject.queryForObject(
				sql4, new Object[] { kidID }, String.class);
		
		if (presentCounter.equals("4")) {
			String sql2 = "UPDATE INVOICE_HEADER SET InvoiceDue= 'Y' "
					+ " WHERE KidID=?";
			
			int rows = jdbcTemplateObject.update(sql2, kidID);
			//logger.info("rows updated = "+ rows);
			
		}
		
		
	}
	
	public void updateInvoiceHeaderForMonth(String kidID, Date date, String groupID){
		//if first week of month, set invoice due as yes
		
	  java.util.Calendar cal = java.util.Calendar.getInstance();
	  cal.setTime(date);
	  //int attendanceDate = cal.get(java.util.Calendar.MONTH);
	  cal.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
	  
	  int attendanceDate = cal.get(java.util.Calendar.DAY_OF_MONTH);
		
/*		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
*/		
		if (attendanceDate<=7) {
			
		 	String sql4 = "UPDATE INVOICE_HEADER SET InvoiceDue= 'Y', InvoiceDate=? "
					+ " , InvoiceAmount= InvoiceAmount+70 WHERE KidID=? AND GroupID=?  ";
			
			int rows = jdbcTemplateObject.update(sql4, date,kidID, groupID);
		}
		
		
	}
	
	public void updateInvoiceHeaderForEveryLesson(String kidID, Date date, String groupID){
		String sql = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter+1, "
				+ "	InvoiceDue= 'Y', InvoiceDate=?  "
				+ " WHERE KidID=? AND GroupID=?";
		
		int rows = jdbcTemplateObject.update(sql, date,kidID, groupID);
		
		String sql2 = "SELECT PresentCounter FROM INVOICE_HEADER WHERE "
				+ 	"	KidID=? AND GroupID =?";
		
		Integer presentCounter = jdbcTemplateObject.queryForObject(
				sql2, new Object[] { kidID, groupID }, Integer.class);
		
		if(presentCounter >= 1) {
			String sql3 = "UPDATE INVOICE_HEADER SET InvoiceAmount= 15*PresentCounter WHERE "
					+ "	KidID=? AND GroupID=? AND InvoiceDate =?";
			
			int rowsToUpdate = jdbcTemplateObject.update(sql3, kidID, groupID, date); 
		} else {
			return;
		}
		
	}
	
	
	

	public List<Attendance> checkAttendance(Attendance data) {
		// TODO Auto-generated method stub
		
		logger.info("date to be queried = " + data.getDate());
		logger.info("Group ID to be queried = " + data.getGroupID());

		String SQL = "SELECT * FROM ATTENDANCE WHERE GROUPOFKIDS_GROUPID=?"
				+ " AND DATEOFATTENDANCE=?" ;
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getGroupID() ,data.getDate()},new AttendanceMapper());
	    
	   
		return attendance;
	}
	
	public List<Attendance> viewAttendanceKid(Attendance data) {
		// TODO Auto-generated method stub
		
		
		String SQL = "SELECT * FROM ATTENDANCE WHERE KID_KidID=? ORDER BY "
				+ " DateOfAttendance DESC" ;
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getKidID() },new AttendanceMapper());
	    
	   
		return attendance;
	}
	
	public List<Attendance> viewAttendanceKidGroup(Attendance data) {
		// TODO Auto-generated method stub
		
		
		String SQL = "SELECT K.KidName, A.AttendanceID, A.DateOfAttendance, "
				+ "	A.PresentAbsent, A.KID_KidID, A.GROUPOFKIDS_GroupID"
				+ " FROM KID K, ATTENDANCE A WHERE A.KID_KidID=? "
				+ "	AND A.GROUPOFKIDS_GroupID=? "
				+ "	AND K.KidID = A.KID_KidID"
				+ "	ORDER BY "
				+ " A.DateOfAttendance DESC" ;
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getKidID(), data.getGroupID() },new AttendanceMapper());
	    
	   
		return attendance;
	}



	public List<Attendance> viewAttendanceGroupDate(Attendance data) {
		String SQL = " SELECT A.*, K.KIDNAME "
				+ " FROM ATTENDANCE A, KID K "
				+ " WHERE A.GROUPOFKIDS_GROUPID= ?"
				+ " AND DATE(A.DateOfAttendance) = ? "
				+ " AND A.KID_KIDID=K.KIDID "
				+ " ORDER BY  A.DateOfAttendance DESC";  
		
	    List <Attendance> attendance = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getGroupID(), java.sql.Date.valueOf(data.getDate()) },new AttendanceMapper());
	    
	   
		return attendance;
	}

}
