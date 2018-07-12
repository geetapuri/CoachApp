package com.example.demo;

import java.sql.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;




public class CalendarJDBCTemplate implements CalendarDAO{
	
	private static Logger logger = LogManager.getLogger(CalendarJDBCTemplate.class);
		
		private DataSource dataSource;
		private JdbcTemplate jdbcTemplateObject;
	
	//Instead of dataSource I had written ds.. and got exceptions
		public void setDataSource(DataSource dataSource) {
			//this.dataSource = dataSource;
		    this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		   
		}
	
	@Override
	public List<Schedule> getSchedule(Date date) {
		// TODO Auto-generated method stub - query for finding the schedule based on date
		logger.info("Calling getSchedule()  ");
		
		logger.info("date to be queried = " + date);
	
		String SQL = "select CALENDAR.Time AS TIME, CALENDAR.Date As Date, CALENDAR.CalendarID As CalendarID,  "
				+ "GROUPOFKIDS.GroupName AS GroupName, GROUPOFKIDS.GroupID AS GroupID "
				+ "from CALENDAR, GROUPOFKIDS "
				+ "where CALENDAR.GroupOfKids_GroupID = GROUPOFKIDS.GroupID "
				+ "AND CALENDAR.Date like ?";
		
	    List <Schedule> schedule = jdbcTemplateObject.query(SQL, new Object[] {date},new CalendarMapper());
	    
	   
	    return schedule;
		
	}
	
	@Override
	public List<Schedule> getSchedule(Coach coach) {
		// TODO Auto-generated method stub - query for finding the schedule based on date
		logger.info("Calling getSchedule()  ");
		logger.info("calling schedule for coachID = " + coach.getCoachID());
		String coachID  = coach.getCoachID();
	
	
		
		String SQL = "select 'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
				"		G.GroupID, G.GroupName, G.CoachID " + 
				"from CALENDAR C, GROUPOFKIDS G " + 
				"where C.GroupOFKIDS_GroupID = G.GroupID " +
				" AND G.CoachID =?" +
				" ORDER BY G.GroupID ASC, C.Date DESC";
		
	    List <Schedule> schedule = jdbcTemplateObject.query(SQL,new Object[] {coachID},new CalendarMapper());
	    
	   
	    return schedule;
		
	}
	
	@Override
	public List<Schedule> getSchedule(Schedule schedule) {
		// TODO Auto-generated method stub - query for finding the schedule based on date
		logger.info("Calling getSchedule()  ");
		
		String kidID = schedule.getKidID();
		
		String SQL = "select C.CalendarID, C.Date, C.Time, " + 
				"		G.GroupID, G.GroupName, G.CoachID, K.KidID	" + 
				"from CALENDAR C, GROUPOFKIDS G , KID K " + 
				"where C.GroupOFKIDS_GroupID = G.GroupID " + 
				" And K.GroupOfKids_GroupID = G.GroupID " + 
				"	And K.KidID = ? " +
				" ORDER BY C.Date DESC";
		
	    List <Schedule> returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {kidID}, new CalendarMapper());
	    
	   
	    return returnSchedule;
		
	}
	
	@Override
	public String addSchedule(Schedule schedule) {
		String SQL = "INSERT INTO CALENDAR (GROUPOFKIDS_GROUPID, TIME, DATE) VALUES (?,?,?)";
		logger.info("inserting DATE as : " + schedule.getDate());
		
		int resultOfQuery = jdbcTemplateObject.update( SQL, schedule.getGroupID(), schedule.getTime(), schedule.getDate() );
		
		logger.info("result of query after insert into calendar = "+ resultOfQuery);
		
		if (resultOfQuery!=0) {
			
			String SQL2 = "INSERT into ATTENDANCE (DateOfAttendance, GROUPOFKIDS_GroupID, " + 
					" KID_KidID, PresentAbsent) SELECT  ?, ?, KIDID , 'A' " + 
					" FROM KID where KID.GROUPOFKIDS_GroupID = ? ";
			
			int resultOfQuery2 = jdbcTemplateObject.update(SQL2, schedule.getDate(), schedule.getGroupID(), schedule.getGroupID());
			
			logger.info("result of query after insert into attendance = "+ resultOfQuery2);
			
			if (resultOfQuery2!=0)
			return "SUCCESS";
			else return "Problem in updating attendance";
			}
			else return "CANT UPDATE SCHEDULE";
	}
		
	
	public String updateSchedule(Schedule schedule) {
		String SQL = "UPDATE CALENDAR SET TIME=?, Date=?"
				+ " WHERE CalendarID=?";
		logger.info("inserting DATE as : " + schedule.getDate());
		
		int resultOfQuery = jdbcTemplateObject.update( SQL, schedule.getTime(), 
															schedule.getDate(),
															schedule.getCalendarID());
		
		logger.info("result of query after insert into calendar = "+ resultOfQuery);
		
		if (resultOfQuery!=0) {
			//TODO: delete from the attendance table and insert new rows based on updated date
			
			String SQL2 = "DELETE FROM ATTENDANCE WHERE GROUPOFKIDS_GroupID IN"
					+ "  (SELECT groupofkids_groupID FROM CALENDAR WHERE CalendarID=?)  ";
			
			int resultOfQuery2 = jdbcTemplateObject.update(SQL2, schedule.getCalendarID());
			
			if (resultOfQuery2!=0) {
			
				
				String SQL3 = "INSERT INTO ATTENDANCE (DateOfAttendance, GROUPOFKIDS_GroupID, KID_KidID, PresentAbsent) "
						+ " SELECT ? , ? , KIDID , 'A' " 
						+ " FROM KID where KID.GROUPOFKIDS_GroupID = ? " ;
				
				int resultOfQuery3 = jdbcTemplateObject.update(SQL3, schedule.getDate(), schedule.getGroupID(), schedule.getGroupID() );
				
				if (resultOfQuery3!=0) {
					return "SUCCESS";
				}
				else return "CANT UPDATE ATTENDANCE according to the SCHEDULE";
			}
			else return "CANT UPDATE ATTENDANCE according to the SCHEDULE";
			}
			else return "CANT UPDATE SCHEDULE";
	}
	
	public List<Schedule> getScheduleKidDate(Schedule schedule) {
		
	logger.info("Calling getScheduleKidDate()  ");
		
		String kidID = schedule.getKidID();
		//Date date = schedule.getDate();
		
		String SQL = "select C.CalendarID, C.Date, C.Time, " + 
				"		G.GroupID, G.GroupName, G.CoachID, K.KidID	" + 
				"from CALENDAR C, GROUPOFKIDS G , KID K " + 
				"where C.GROUPOFKIDS_GroupID = G.GroupID " + 
				" And K.GROUPOFKIDS_GroupID = G.GroupID " + 
				"	And K.KidID = ? And DATE(C.Date) = ?" +
				" ORDER BY C.Date DESC";
		
	    List <Schedule> returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {kidID, schedule.getDate()}, new CalendarMapper());
	    
	   
	    return returnSchedule;
		
	}
	
	public List<Schedule> getScheduleCoachDate(Schedule schedule) {
	logger.info("Calling getScheduleKidDate()  ");
		//TODO if groupID has come, sql based on that, else this one
		String coachID = schedule.getCoachID();
		String SQL;
		List <Schedule> returnSchedule;
		//Date date = schedule.getDate();
		if (schedule.getGroupID()!= null ) { 
			 SQL = "select 'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
					"G.GroupID, G.GroupName, G.CoachID	" + 
					"from CALENDAR C, GROUPOFKIDS G  " + 
					"where C.GROUPOFKIDS_GroupID = G.GroupID " + 
					"And G.CoachID = ? And DATE(C.Date) = ? " +
					" And G.GroupID = ? " +
					" ORDER BY C.Date DESC";
			  returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {coachID, schedule.getDate(), schedule.getGroupID()}, new CalendarMapper());
			    
			
		} else {
		
		 SQL = "select 'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
				"G.GroupID, G.GroupName, G.CoachID	" + 
				"from CALENDAR C, GROUPOFKIDS G  " + 
				"where C.GROUPOFKIDS_GroupID = G.GroupID " + 
				"And G.CoachID = ? And DATE(C.Date) = ?" +
				" ORDER BY C.Date DESC";
		  returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {coachID, schedule.getDate()}, new CalendarMapper());
		    
		}
	    
	   
	    return returnSchedule;
		
	}
}


	

